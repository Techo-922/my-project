package com.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RecommendationRequest单元测试
 * 测试DTO的getter/setter方法和构造函数
 */
class RecommendationRequestTest {

    @Test
    void testDefaultConstructor() {
        RecommendationRequest request = new RecommendationRequest();
        assertNotNull(request);
        // 验证默认值
        assertEquals(1, request.getPageNum());
        assertEquals(10, request.getPageSize());
        assertEquals("matchRate", request.getSortType());
        assertNull(request.getUserId());
    }

    @Test
    void testConstructorWithUserId() {
        Long userId = 123L;
        RecommendationRequest request = new RecommendationRequest(userId);
        
        assertEquals(userId, request.getUserId());
        assertEquals(1, request.getPageNum());
        assertEquals(10, request.getPageSize());
        assertEquals("matchRate", request.getSortType());
    }

    @Test
    void testConstructorWithAllParameters() {
        Long userId = 456L;
        Integer pageNum = 2;
        Integer pageSize = 20;
        String sortType = "popularity";
        
        RecommendationRequest request = new RecommendationRequest(userId, pageNum, pageSize, sortType);
        
        assertEquals(userId, request.getUserId());
        assertEquals(pageNum, request.getPageNum());
        assertEquals(pageSize, request.getPageSize());
        assertEquals(sortType, request.getSortType());
    }

    @Test
    void testUserIdGetterAndSetter() {
        RecommendationRequest request = new RecommendationRequest();
        Long expectedUserId = 789L;
        request.setUserId(expectedUserId);
        assertEquals(expectedUserId, request.getUserId());
    }

    @Test
    void testPageNumGetterAndSetter() {
        RecommendationRequest request = new RecommendationRequest();
        Integer expectedPageNum = 5;
        request.setPageNum(expectedPageNum);
        assertEquals(expectedPageNum, request.getPageNum());
    }

    @Test
    void testPageSizeGetterAndSetter() {
        RecommendationRequest request = new RecommendationRequest();
        Integer expectedPageSize = 50;
        request.setPageSize(expectedPageSize);
        assertEquals(expectedPageSize, request.getPageSize());
    }

    @Test
    void testSortTypeGetterAndSetter() {
        RecommendationRequest request = new RecommendationRequest();
        String expectedSortType = "popularity";
        request.setSortType(expectedSortType);
        assertEquals(expectedSortType, request.getSortType());
    }

    @Test
    void testAllFieldsTogether() {
        RecommendationRequest request = new RecommendationRequest();
        
        // 设置所有字段
        request.setUserId(100L);
        request.setPageNum(3);
        request.setPageSize(15);
        request.setSortType("matchRate");
        
        // 验证所有字段
        assertEquals(100L, request.getUserId());
        assertEquals(3, request.getPageNum());
        assertEquals(15, request.getPageSize());
        assertEquals("matchRate", request.getSortType());
    }

    @Test
    void testDefaultValuesOverride() {
        RecommendationRequest request = new RecommendationRequest();
        
        // 验证默认值
        assertEquals(1, request.getPageNum());
        assertEquals(10, request.getPageSize());
        assertEquals("matchRate", request.getSortType());
        
        // 覆盖默认值
        request.setPageNum(2);
        request.setPageSize(20);
        request.setSortType("popularity");
        
        // 验证覆盖后的值
        assertEquals(2, request.getPageNum());
        assertEquals(20, request.getPageSize());
        assertEquals("popularity", request.getSortType());
    }

    @Test
    void testNullUserId() {
        RecommendationRequest request = new RecommendationRequest();
        assertNull(request.getUserId());
        
        request.setUserId(null);
        assertNull(request.getUserId());
    }
}
