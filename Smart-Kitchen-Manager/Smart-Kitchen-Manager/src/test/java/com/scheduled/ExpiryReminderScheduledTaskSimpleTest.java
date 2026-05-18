package com.scheduled;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 过期提醒定时任务简单测试
 * 验证定时任务能够正常执行
 * 
 * @author 
 * @date 2023-04-25
 */
@SpringBootTest
public class ExpiryReminderScheduledTaskSimpleTest {
    
    @Autowired
    private ExpiryReminderScheduledTask scheduledTask;
    
    @Test
    public void testScheduledTaskExists() {
        assertNotNull(scheduledTask, "定时任务组件应该被正确注入");
    }
    
    @Test
    public void testManualTrigger_ShouldExecuteWithoutException() {
        // 测试手动触发方法是否能正常执行（不抛出异常）
        assertDoesNotThrow(() -> {
            scheduledTask.manualTrigger();
        }, "手动触发定时任务应该不抛出异常");
    }
    
    @Test
    public void testCheckExpiringShicai_ShouldExecuteWithoutException() {
        // 测试定时任务方法是否能正常执行（不抛出异常）
        assertDoesNotThrow(() -> {
            scheduledTask.checkExpiringShicai();
        }, "定时任务执行应该不抛出异常");
    }
}
