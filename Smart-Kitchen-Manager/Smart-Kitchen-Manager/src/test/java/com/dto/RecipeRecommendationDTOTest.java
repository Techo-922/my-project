package com.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RecipeRecommendationDTO单元测试
 * 测试DTO的getter/setter方法和构造函数
 */
class RecipeRecommendationDTOTest {

    @Test
    void testDefaultConstructor() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        assertNotNull(dto);
    }

    @Test
    void testIdGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        Long expectedId = 123L;
        dto.setId(expectedId);
        assertEquals(expectedId, dto.getId());
    }

    @Test
    void testCaipumingchengGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        String expectedName = "西红柿炒鸡蛋";
        dto.setCaipumingcheng(expectedName);
        assertEquals(expectedName, dto.getCaipumingcheng());
    }

    @Test
    void testCaipufengmianGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        String expectedCover = "http://example.com/cover.jpg";
        dto.setCaipufengmian(expectedCover);
        assertEquals(expectedCover, dto.getCaipufengmian());
    }

    @Test
    void testCaishileixingGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        String expectedType = "家常菜";
        dto.setCaishileixing(expectedType);
        assertEquals(expectedType, dto.getCaishileixing());
    }

    @Test
    void testPengrenfangshiGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        String expectedMethod = "炒";
        dto.setPengrenfangshi(expectedMethod);
        assertEquals(expectedMethod, dto.getPengrenfangshi());
    }

    @Test
    void testFenshuGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        Integer expectedScore = 95;
        dto.setFenshu(expectedScore);
        assertEquals(expectedScore, dto.getFenshu());
    }

    @Test
    void testZhizuoliuchengGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        String expectedProcess = "1. 打鸡蛋 2. 切西红柿 3. 炒制";
        dto.setZhizuoliucheng(expectedProcess);
        assertEquals(expectedProcess, dto.getZhizuoliucheng());
    }

    @Test
    void testMatchRateGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        Double expectedRate = 75.5;
        dto.setMatchRate(expectedRate);
        assertEquals(expectedRate, dto.getMatchRate());
    }

    @Test
    void testHighPriorityGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        Boolean expectedPriority = true;
        dto.setHighPriority(expectedPriority);
        assertEquals(expectedPriority, dto.getHighPriority());
    }

    @Test
    void testRequiredIngredientsGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        List<String> expectedIngredients = Arrays.asList("鸡蛋", "西红柿", "食用油");
        dto.setRequiredIngredients(expectedIngredients);
        assertEquals(expectedIngredients, dto.getRequiredIngredients());
        assertEquals(3, dto.getRequiredIngredients().size());
    }

    @Test
    void testMissingIngredientsGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        List<String> expectedMissing = Arrays.asList("盐", "糖");
        dto.setMissingIngredients(expectedMissing);
        assertEquals(expectedMissing, dto.getMissingIngredients());
        assertEquals(2, dto.getMissingIngredients().size());
    }

    @Test
    void testMissingCountGetterAndSetter() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        Integer expectedCount = 2;
        dto.setMissingCount(expectedCount);
        assertEquals(expectedCount, dto.getMissingCount());
    }

    @Test
    void testAllFieldsTogether() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        
        // 设置所有字段
        dto.setId(1L);
        dto.setCaipumingcheng("西红柿炒鸡蛋");
        dto.setCaipufengmian("cover.jpg");
        dto.setCaishileixing("家常菜");
        dto.setPengrenfangshi("炒");
        dto.setFenshu(90);
        dto.setZhizuoliucheng("制作流程");
        dto.setMatchRate(80.0);
        dto.setHighPriority(true);
        dto.setRequiredIngredients(Arrays.asList("鸡蛋", "西红柿", "油", "盐", "糖"));
        dto.setMissingIngredients(Arrays.asList("盐", "糖"));
        dto.setMissingCount(2);
        
        // 验证所有字段
        assertEquals(1L, dto.getId());
        assertEquals("西红柿炒鸡蛋", dto.getCaipumingcheng());
        assertEquals("cover.jpg", dto.getCaipufengmian());
        assertEquals("家常菜", dto.getCaishileixing());
        assertEquals("炒", dto.getPengrenfangshi());
        assertEquals(90, dto.getFenshu());
        assertEquals("制作流程", dto.getZhizuoliucheng());
        assertEquals(80.0, dto.getMatchRate());
        assertTrue(dto.getHighPriority());
        assertEquals(5, dto.getRequiredIngredients().size());
        assertEquals(2, dto.getMissingIngredients().size());
        assertEquals(2, dto.getMissingCount());
    }

    @Test
    void testNullValues() {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        
        // 默认值应该为null
        assertNull(dto.getId());
        assertNull(dto.getCaipumingcheng());
        assertNull(dto.getMatchRate());
        assertNull(dto.getHighPriority());
        assertNull(dto.getRequiredIngredients());
        assertNull(dto.getMissingIngredients());
        assertNull(dto.getMissingCount());
    }
}
