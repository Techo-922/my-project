package com.dao;

import com.entity.UserShicaiEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;

/**
 * UserShicaiDao属性测试
 * 
 * Feature: recipe-smart-recommendation
 * Property 1: 有效食材过滤
 * Validates: Requirements 1.1, 1.2
 */
@SpringBootTest
@Transactional
class UserShicaiDaoPropertyTest {

    @Autowired
    private UserShicaiDao userShicaiDao;

    /**
     * 属性 1: 有效食材过滤
     * 
     * 对于任何用户ID，检索的食材列表应该只包含状态为'new'或'used'的食材，
     * 不包含'expired'或'discarded'状态的食材
     * 
     * Validates: Requirements 1.1, 1.2
     */
    @Test
    void property_validIngredientsFilterOnlyNewOrUsed() {
        qt()
            .withExamples(5)  // 减少到5个示例
            .forAll(
                longs().between(1L, 100L),  // userId
                lists().of(strings().betweenCodePoints('a', 'z').ofLengthBetween(3, 10)).ofSizeBetween(1, 5),  // ingredient names (lowercase letters only)
                lists().of(integers().between(0, 3)).ofSizeBetween(1, 5)  // status indices
            )
            .checkAssert((userId, ingredientNames, statusIndices) -> {
                // 清理测试数据
                userShicaiDao.delete(null);
                
                // 定义有效的状态值
                String[] validStatuses = {"new", "used", "expired", "discarded"};
                
                // 插入测试数据
                int size = Math.min(ingredientNames.size(), statusIndices.size());
                for (int i = 0; i < size; i++) {
                    UserShicaiEntity entity = new UserShicaiEntity();
                    entity.setUserid(userId);
                    entity.setShicaiName(ingredientNames.get(i));
                    entity.setStatus(validStatuses[statusIndices.get(i)]);
                    entity.setQuantity(100);
                    entity.setUnit("克");
                    entity.setPurchaseDate(new Date());
                    entity.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L));
                    entity.setAddtime(new Date());
                    userShicaiDao.insert(entity);
                }
                
                // 查询有效食材
                List<UserShicaiEntity> validIngredients = userShicaiDao.selectValidIngredientsByUserId(userId);
                
                // 验证：所有返回的食材状态必须是'new'或'used'
                for (UserShicaiEntity ingredient : validIngredients) {
                    String status = ingredient.getStatus();
                    assertTrue(
                        "new".equals(status) || "used".equals(status),
                        "Found invalid status: " + status + ". Only 'new' or 'used' should be returned."
                    );
                    
                    // 验证：不应该包含'expired'或'discarded'状态
                    assertFalse(
                        "expired".equals(status) || "discarded".equals(status),
                        "Found excluded status: " + status + ". 'expired' and 'discarded' should be filtered out."
                    );
                }
                
                // 验证：返回的食材数量应该等于插入的'new'或'used'状态的食材数量
                long expectedCount = 0;
                for (int i = 0; i < size; i++) {
                    String status = validStatuses[statusIndices.get(i)];
                    if ("new".equals(status) || "used".equals(status)) {
                        expectedCount++;
                    }
                }
                assertEquals(expectedCount, validIngredients.size(),
                    "The number of returned ingredients should match the number of 'new' or 'used' ingredients inserted.");
            });
    }

    /**
     * 单元测试：验证特定示例
     * 测试用户有'new'和'used'状态的食材时，两者都应该被返回
     */
    @Test
    void testSelectValidIngredients_WithNewAndUsedStatus() {
        Long userId = 999L;
        
        // 清理测试数据
        userShicaiDao.delete(null);
        
        // 插入'new'状态的食材
        UserShicaiEntity newIngredient = new UserShicaiEntity();
        newIngredient.setUserid(userId);
        newIngredient.setShicaiName("鸡蛋");
        newIngredient.setStatus("new");
        newIngredient.setQuantity(10);
        newIngredient.setUnit("个");
        newIngredient.setPurchaseDate(new Date());
        newIngredient.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L));
        newIngredient.setAddtime(new Date());
        userShicaiDao.insert(newIngredient);
        
        // 插入'used'状态的食材
        UserShicaiEntity usedIngredient = new UserShicaiEntity();
        usedIngredient.setUserid(userId);
        usedIngredient.setShicaiName("西红柿");
        usedIngredient.setStatus("used");
        usedIngredient.setQuantity(5);
        usedIngredient.setUnit("个");
        usedIngredient.setPurchaseDate(new Date());
        usedIngredient.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L));
        usedIngredient.setAddtime(new Date());
        userShicaiDao.insert(usedIngredient);
        
        // 查询有效食材
        List<UserShicaiEntity> validIngredients = userShicaiDao.selectValidIngredientsByUserId(userId);
        
        // 验证
        assertEquals(2, validIngredients.size());
        assertTrue(validIngredients.stream().anyMatch(i -> "鸡蛋".equals(i.getShicaiName())));
        assertTrue(validIngredients.stream().anyMatch(i -> "西红柿".equals(i.getShicaiName())));
    }

    /**
     * 单元测试：验证边缘情况
     * 测试用户只有'expired'和'discarded'状态的食材时，应该返回空列表
     */
    @Test
    void testSelectValidIngredients_WithOnlyExpiredAndDiscarded() {
        Long userId = 998L;
        
        // 清理测试数据
        userShicaiDao.delete(null);
        
        // 插入'expired'状态的食材
        UserShicaiEntity expiredIngredient = new UserShicaiEntity();
        expiredIngredient.setUserid(userId);
        expiredIngredient.setShicaiName("过期牛奶");
        expiredIngredient.setStatus("expired");
        expiredIngredient.setQuantity(1);
        expiredIngredient.setUnit("瓶");
        expiredIngredient.setPurchaseDate(new Date());
        expiredIngredient.setExpiryDate(new Date(System.currentTimeMillis() - 86400000L));
        expiredIngredient.setAddtime(new Date());
        userShicaiDao.insert(expiredIngredient);
        
        // 插入'discarded'状态的食材
        UserShicaiEntity discardedIngredient = new UserShicaiEntity();
        discardedIngredient.setUserid(userId);
        discardedIngredient.setShicaiName("丢弃的面包");
        discardedIngredient.setStatus("discarded");
        discardedIngredient.setQuantity(1);
        discardedIngredient.setUnit("个");
        discardedIngredient.setPurchaseDate(new Date());
        discardedIngredient.setExpiryDate(new Date(System.currentTimeMillis() - 86400000L));
        discardedIngredient.setAddtime(new Date());
        userShicaiDao.insert(discardedIngredient);
        
        // 查询有效食材
        List<UserShicaiEntity> validIngredients = userShicaiDao.selectValidIngredientsByUserId(userId);
        
        // 验证：应该返回空列表
        assertTrue(validIngredients.isEmpty(), "Should return empty list when user only has expired or discarded ingredients");
    }

    /**
     * 单元测试：验证边缘情况
     * 测试用户不存在时，应该返回空列表
     */
    @Test
    void testSelectValidIngredients_WithNonExistentUser() {
        Long nonExistentUserId = 99999L;
        
        // 查询不存在的用户
        List<UserShicaiEntity> validIngredients = userShicaiDao.selectValidIngredientsByUserId(nonExistentUserId);
        
        // 验证：应该返回空列表
        assertNotNull(validIngredients);
        assertTrue(validIngredients.isEmpty(), "Should return empty list for non-existent user");
    }
}
