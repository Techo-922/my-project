package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.ExpiryReminderDao;
import com.entity.ExpiryReminderEntity;
import com.service.ExpiryReminderService;

@Service("expiryReminderService")
public class ExpiryReminderServiceImpl extends ServiceImpl<ExpiryReminderDao, ExpiryReminderEntity> implements ExpiryReminderService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExpiryReminderServiceImpl.class);
	
	@Autowired
	private ExpiryReminderDao expiryReminderDao;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ExpiryReminderEntity> page = this.selectPage(
                new Query<ExpiryReminderEntity>(params).getPage(),
                new EntityWrapper<ExpiryReminderEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<ExpiryReminderEntity> wrapper) {
		Page<ExpiryReminderEntity> page = this.selectPage(
                new Query<ExpiryReminderEntity>(params).getPage(),
                wrapper
        );
	    return new PageUtils(page);
 	}
 	
 	@Override
    public Map<String, Object> getReminderDetail(Long id) {
        if (id == null) {
            logger.warn("查询提醒详情失败 - ID为空");
            return null;
        }
        
        Map<String, Object> detail = expiryReminderDao.selectReminderDetail(id);
        
        if (detail == null) {
            logger.warn("提醒不存在 - ID: {}", id);
        }
        
        return detail;
    }
    
    @Override
    @Transactional
    public boolean markAsRead(Long id, Long userid) {
        if (id == null || userid == null) {
            logger.warn("标记已读失败 - 参数为空");
            return false;
        }
        
        ExpiryReminderEntity reminder = this.selectById(id);
        
        if (reminder == null) {
            logger.warn("提醒不存在 - ID: {}", id);
            return false;
        }
        
        // 验证提醒属于当前用户
        if (!reminder.getUserid().equals(userid)) {
            logger.warn("权限验证失败 - 用户{}尝试访问用户{}的提醒{}", 
                userid, reminder.getUserid(), id);
            return false;
        }
        
        reminder.setStatus("read");
        boolean result = this.updateById(reminder);
        
        if (result) {
            logger.info("提醒{}已标记为已读", id);
        }
        
        return result;
    }
    
    @Override
    public int getUnreadCount(Long userid) {
        if (userid == null) {
            logger.warn("查询未读提醒数量失败 - 用户ID为空");
            return 0;
        }
        
        int count = expiryReminderDao.selectUnreadCount(userid);
        logger.debug("用户{}的未读提醒数量: {}", userid, count);
        
        return count;
    }
    
    @Override
    @Transactional
    public boolean createReminder(Long userid, Long userShicaiId, Date remindDate) {
        if (userid == null || userShicaiId == null || remindDate == null) {
            logger.warn("创建提醒失败 - 参数为空");
            return false;
        }
        
        ExpiryReminderEntity reminder = new ExpiryReminderEntity();
        reminder.setUserid(userid);
        reminder.setUserShicaiId(userShicaiId);
        reminder.setRemindDate(remindDate);
        reminder.setStatus("pending");
        reminder.setAddtime(new Date());
        
        boolean result = this.insert(reminder);
        
        if (result) {
            logger.info("为用户{}的食材{}创建提醒成功", userid, userShicaiId);
        } else {
            logger.error("为用户{}的食材{}创建提醒失败", userid, userShicaiId);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public void deletePendingRemindersByShicai(Long userShicaiId) {
        if (userShicaiId == null) {
            logger.warn("删除待处理提醒失败 - 食材ID为空");
            return;
        }
        
        // 删除状态为pending或sent的提醒
        boolean result = this.delete(
            new EntityWrapper<ExpiryReminderEntity>()
                .eq("user_shicai_id", userShicaiId)
                .in("status", Arrays.asList("pending", "sent"))
        );
        
        if (result) {
            logger.info("删除食材{}的待处理提醒成功", userShicaiId);
        }
    }
    
    @Override
    @Transactional
    public void updateReminderDateByShicai(Long userShicaiId, Date newExpiryDate) {
        if (userShicaiId == null || newExpiryDate == null) {
            logger.warn("更新提醒日期失败 - 参数为空");
            return;
        }
        
        // 计算新的提醒日期（过期日期前3天）
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newExpiryDate);
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        Date newRemindDate = calendar.getTime();
        
        // 查询该食材的所有待处理提醒
        List<ExpiryReminderEntity> reminders = this.selectList(
            new EntityWrapper<ExpiryReminderEntity>()
                .eq("user_shicai_id", userShicaiId)
                .in("status", Arrays.asList("pending", "sent"))
        );
        
        if (reminders != null && !reminders.isEmpty()) {
            for (ExpiryReminderEntity reminder : reminders) {
                reminder.setRemindDate(newRemindDate);
                this.updateById(reminder);
            }
            logger.info("更新食材{}的{}条提醒日期成功", userShicaiId, reminders.size());
        }
    }
}
