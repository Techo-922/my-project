package com.scheduled;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ExpiryReminderEntity;
import com.entity.UserShicaiEntity;
import com.service.ExpiryReminderService;
import com.service.UserShicaiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 过期提醒定时任务
 * 每天凌晨12点自动扫描即将过期的食材并创建提醒
 * 
 * @author 
 * @date 2023-04-25
 */
@Component
public class ExpiryReminderScheduledTask {
    
    private static final Logger log = LoggerFactory.getLogger(ExpiryReminderScheduledTask.class);
    
    @Autowired
    private UserShicaiService userShicaiService;
    
    @Autowired
    private ExpiryReminderService expiryReminderService;
    
    /**
     * 定时检查过期食材
     * 每天凌晨12点（午夜0点）执行
     * cron表达式：秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiringShicai() {
        long startTime = System.currentTimeMillis();
        log.info("开始执行过期食材检查任务");
        
        try {
            // 1. 查询所有状态为"new"的食材
            List<UserShicaiEntity> shicaiList = userShicaiService.selectList(
                new EntityWrapper<UserShicaiEntity>().eq("status", "new")
            );
            
            log.info("扫描到{}条待检查食材", shicaiList.size());
            
            // 2. 遍历处理
            int createdCount = 0;
            int expiredCount = 0;
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            Date threeDaysLater = calendar.getTime();
            
            for (UserShicaiEntity shicai : shicaiList) {
                Date expiryDate = shicai.getExpiryDate();
                if (expiryDate == null) {
                    continue;
                }
                
                // 检查是否已过期
                if (expiryDate.before(now)) {
                    // 创建紧急提醒
                    boolean created = expiryReminderService.createReminder(
                        shicai.getUserid(), 
                        shicai.getId(), 
                        now
                    );
                    
                    if (created) {
                        // 更新食材状态
                        shicai.setStatus("expired");
                        userShicaiService.updateById(shicai);
                        expiredCount++;
                    }
                }
                // 检查是否3天内过期
                else if (expiryDate.before(threeDaysLater)) {
                    // 检查是否已有未读提醒（避免重复创建）
                    int unreadCount = expiryReminderService.selectCount(
                        new EntityWrapper<ExpiryReminderEntity>()
                            .eq("user_shicai_id", shicai.getId())
                            .in("status", Arrays.asList("pending", "sent"))
                    );
                    
                    if (unreadCount == 0) {
                        // 创建提醒（提醒日期为过期日期前3天）
                        calendar.setTime(expiryDate);
                        calendar.add(Calendar.DAY_OF_MONTH, -3);
                        Date remindDate = calendar.getTime();
                        
                        boolean created = expiryReminderService.createReminder(
                            shicai.getUserid(), 
                            shicai.getId(), 
                            remindDate
                        );
                        
                        if (created) {
                            createdCount++;
                        }
                    }
                }
            }
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("过期食材检查任务完成，耗时{}ms，创建{}条提醒，标记{}条过期", 
                duration, createdCount, expiredCount);
            
            // 性能警告
            if (duration > 30000) {
                log.warn("定时任务执行时间过长：{}ms", duration);
            }
            
        } catch (Exception e) {
            log.error("过期食材检查任务执行失败", e);
            // 不抛出异常，确保不影响系统其他功能
        }
    }
    
    /**
     * 手动触发定时任务（用于测试）
     */
    public void manualTrigger() {
        log.info("手动触发过期食材检查任务");
        checkExpiringShicai();
    }
}
