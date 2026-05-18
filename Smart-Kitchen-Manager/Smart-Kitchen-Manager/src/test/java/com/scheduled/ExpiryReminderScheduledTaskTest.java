package com.scheduled;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ExpiryReminderEntity;
import com.entity.UserShicaiEntity;
import com.service.ExpiryReminderService;
import com.service.UserShicaiService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 过期提醒定时任务测试
 * 使用@DirtiesContext确保每个测试都有独立的Spring上下文
 * 
 * @author 
 * @date 2023-04-25
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExpiryReminderScheduledTaskTest {
    
    @Autowired
    private ExpiryReminderScheduledTask scheduledTask;
    
    @Autowired
    private UserShicaiService userShicaiService;
    
    @Autowired
    private ExpiryReminderService expiryReminderService;
    
    private Long testUserid = 999L;
    
    @BeforeAll
    public void setUp() {
        // 清理测试数据
        cleanupTestData();
    }
    
    @AfterAll
    public void tearDown() {
        // 清理测试数据
        cleanupTestData();
    }
    
    private void cleanupTestData() {
        try {
            expiryReminderService.delete(new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid));
            userShicaiService.delete(new EntityWrapper<UserShicaiEntity>().eq("userid", testUserid));
        } catch (Exception e) {
            // 忽略清理错误
        }
    }
    
    @Test
    @DirtiesContext
    public void testCheckExpiringShicai_ShouldCreateReminderForExpiringSoon() {
        // 准备测试数据：创建一个2天后过期的食材
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date expiryDate = calendar.getTime();
        
        UserShicaiEntity shicai = new UserShicaiEntity();
        shicai.setUserid(testUserid);
        shicai.setShicaiName("测试食材");
        shicai.setQuantity(1);
        shicai.setUnit("个");
        shicai.setPurchaseDate(new Date());
        shicai.setExpiryDate(expiryDate);
        shicai.setStatus("new");
        shicai.setAddtime(new Date());
        
        userShicaiService.insert(shicai);
        Long shicaiId = shicai.getId();
        
        // 记录执行前的提醒数量
        int beforeCount = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        
        // 执行定时任务
        scheduledTask.manualTrigger();
        
        // 验证：提醒数量应该增加1
        int afterCount = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        
        assertEquals(beforeCount + 1, afterCount, "应该创建一条提醒");
        
        // 清理
        expiryReminderService.delete(new EntityWrapper<ExpiryReminderEntity>().eq("user_shicai_id", shicaiId));
        userShicaiService.deleteById(shicaiId);
    }
    
    @Test
    public void testCheckExpiringShicai_ShouldUpdateStatusForExpired() {
        // 准备测试数据：创建一个已过期的食材
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date expiryDate = calendar.getTime();
        
        UserShicaiEntity shicai = new UserShicaiEntity();
        shicai.setUserid(testUserid);
        shicai.setShicaiName("过期食材");
        shicai.setQuantity(1);
        shicai.setUnit("个");
        shicai.setPurchaseDate(new Date());
        shicai.setExpiryDate(expiryDate);
        shicai.setStatus("new");
        shicai.setAddtime(new Date());
        
        userShicaiService.insert(shicai);
        Long shicaiId = shicai.getId();
        
        // 记录执行前的提醒数量和过期食材数量
        int beforeReminderCount = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        int beforeExpiredCount = userShicaiService.selectCount(
            new EntityWrapper<UserShicaiEntity>()
                .eq("userid", testUserid)
                .eq("status", "expired")
        );
        
        // 执行定时任务
        scheduledTask.manualTrigger();
        
        // 验证：应该创建了提醒
        int afterReminderCount = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        assertEquals(beforeReminderCount + 1, afterReminderCount, "应该创建一条提醒");
        
        // 验证：过期食材数量应该增加1
        int afterExpiredCount = userShicaiService.selectCount(
            new EntityWrapper<UserShicaiEntity>()
                .eq("userid", testUserid)
                .eq("status", "expired")
        );
        assertEquals(beforeExpiredCount + 1, afterExpiredCount, "应该有一条食材被标记为expired");
        
        // 清理
        expiryReminderService.delete(new EntityWrapper<ExpiryReminderEntity>().eq("user_shicai_id", shicaiId));
        userShicaiService.deleteById(shicaiId);
    }
    
    @Test
    @DirtiesContext
    public void testCheckExpiringShicai_ShouldNotCreateDuplicateReminder() {
        // 准备测试数据：创建一个2天后过期的食材
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date expiryDate = calendar.getTime();
        
        UserShicaiEntity shicai = new UserShicaiEntity();
        shicai.setUserid(testUserid);
        shicai.setShicaiName("测试食材");
        shicai.setQuantity(1);
        shicai.setUnit("个");
        shicai.setPurchaseDate(new Date());
        shicai.setExpiryDate(expiryDate);
        shicai.setStatus("new");
        shicai.setAddtime(new Date());
        
        userShicaiService.insert(shicai);
        Long shicaiId = shicai.getId();
        
        // 第一次执行定时任务
        scheduledTask.manualTrigger();
        
        // 记录第一次执行后的提醒数量
        int afterFirstRun = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        
        // 第二次执行定时任务
        scheduledTask.manualTrigger();
        
        // 验证：提醒数量不应该增加（不会重复创建）
        int afterSecondRun = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        
        assertEquals(afterFirstRun, afterSecondRun, "不应该创建重复的提醒");
        
        // 清理
        expiryReminderService.delete(new EntityWrapper<ExpiryReminderEntity>().eq("user_shicai_id", shicaiId));
        userShicaiService.deleteById(shicaiId);
    }
    
    @Test
    public void testCheckExpiringShicai_ShouldOnlyScanNewStatus() {
        // 准备测试数据：创建一个已使用的食材（状态为used）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date expiryDate = calendar.getTime();
        
        UserShicaiEntity shicai = new UserShicaiEntity();
        shicai.setUserid(testUserid);
        shicai.setShicaiName("已使用食材");
        shicai.setQuantity(1);
        shicai.setUnit("个");
        shicai.setPurchaseDate(new Date());
        shicai.setExpiryDate(expiryDate);
        shicai.setStatus("used");
        shicai.setAddtime(new Date());
        
        userShicaiService.insert(shicai);
        Long shicaiId = shicai.getId();
        
        // 记录执行前的提醒数量
        int beforeCount = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        
        // 执行定时任务
        scheduledTask.manualTrigger();
        
        // 验证：提醒数量不应该增加（因为状态不是new）
        int afterCount = expiryReminderService.selectCount(
            new EntityWrapper<ExpiryReminderEntity>().eq("userid", testUserid)
        );
        
        assertEquals(beforeCount, afterCount, "不应该为非new状态的食材创建提醒");
        
        // 清理
        userShicaiService.deleteById(shicaiId);
    }
    
    @Test
    public void testManualTrigger_ShouldExecuteSuccessfully() {
        // 测试手动触发方法是否能正常执行
        assertDoesNotThrow(() -> {
            scheduledTask.manualTrigger();
        }, "手动触发定时任务应该不抛出异常");
    }
}
