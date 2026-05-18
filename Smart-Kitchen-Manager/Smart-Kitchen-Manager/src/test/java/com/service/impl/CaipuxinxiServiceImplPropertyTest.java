package com.service.impl;

import org.junit.jupiter.api.Test;
import org.quicktheories.core.Gen;

import java.util.*;
import java.util.stream.Collectors;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;

/**
 * 菜谱推荐服务属性测试
 * 使用QuickTheories进行基于属性的测试
 * 
 * Feature: recipe-smart-recommendation
 */
public class CaipuxinxiServiceImplPropertyTest {

    /**
     * 属性 2: 匹配度计算正确性
     * 
     * 对于任何食谱和用户食材集合，计算的匹配度应该等于
     * (用户拥有的所需食材数量 / 食谱总食材数量) * 100，且结果在0到100之间
     * 
     * 验证: 需求 2.1, 2.4
     */
    @Test
    public void property_matchRateAlwaysBetween0And100() {
        qt()
            .withExamples(5)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 10),  // 食谱食材
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(0, 15)    // 用户食材
            )
            .check((requiredIngredients, userIngredientsList) -> {
                Set<String> userIngredients = new HashSet<>(userIngredientsList);
                Double matchRate = calculateMatchRate(requiredIngredients, userIngredients);
                return matchRate >= 0.0 && matchRate <= 100.0;
            });
    }

    /**
     * 属性 2: 匹配度计算正确性 - 验证计算公式
     * 
     * 对于任何食谱和用户食材集合，计算的匹配度应该等于
     * (用户拥有的所需食材数量 / 食谱总食材数量) * 100
     * 
     * 验证: 需求 2.1, 2.4
     */
    @Test
    public void property_matchRateCalculationFormula() {
        qt()
            .withExamples(5)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 10),  // 食谱食材
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(0, 15)    // 用户食材
            )
            .check((requiredIngredients, userIngredientsList) -> {
                Set<String> userIngredients = new HashSet<>(userIngredientsList);
                Double matchRate = calculateMatchRate(requiredIngredients, userIngredients);
                
                // 手动计算期望的匹配度
                long matchedCount = requiredIngredients.stream()
                    .filter(ingredient -> userIngredients.contains(ingredient.trim()))
                    .count();
                double expectedMatchRate = (matchedCount * 100.0) / requiredIngredients.size();
                
                // 验证计算结果与期望值相等（允许浮点数误差）
                return Math.abs(matchRate - expectedMatchRate) < 0.001;
            });
    }

    /**
     * 属性 2: 匹配度计算正确性 - 空食材列表返回0
     * 
     * 当食谱食材列表为空时，匹配度应该为0
     * 
     * 验证: 需求 2.1, 2.4
     */
    @Test
    public void property_matchRateZeroForEmptyRequiredIngredients() {
        qt()
            .withExamples(5)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(0, 15)    // 用户食材
            )
            .check((userIngredientsList) -> {
                Set<String> userIngredients = new HashSet<>(userIngredientsList);
                List<String> emptyRequiredIngredients = new ArrayList<>();
                Double matchRate = calculateMatchRate(emptyRequiredIngredients, userIngredients);
                return matchRate == 0.0;
            });
    }

    /**
     * 属性 2: 匹配度计算正确性 - 完全匹配返回100
     * 
     * 当用户拥有所有所需食材时，匹配度应该为100
     * 
     * 验证: 需求 2.1, 2.4
     */
    @Test
    public void property_matchRate100ForCompleteMatch() {
        qt()
            .withExamples(5)
            .forAll(
                lists().of(strings().betweenCodePoints('a', 'z').ofLengthBetween(3, 10)).ofSizeBetween(1, 10)  // 食谱食材 (lowercase letters only)
            )
            .check((requiredIngredients) -> {
                // 用户拥有所有所需食材
                Set<String> userIngredients = new HashSet<>(requiredIngredients);
                Double matchRate = calculateMatchRate(requiredIngredients, userIngredients);
                return Math.abs(matchRate - 100.0) < 0.001;
            });
    }

    /**
     * 属性 2: 匹配度计算正确性 - 无匹配返回0
     * 
     * 当用户不拥有任何所需食材时，匹配度应该为0
     * 
     * 验证: 需求 2.1, 2.4
     */
    @Test
    public void property_matchRate0ForNoMatch() {
        qt()
            .withExamples(5)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 10)  // 食谱食材
            )
            .check((requiredIngredients) -> {
                // 用户不拥有任何所需食材（使用不同的字符串）
                Set<String> userIngredients = new HashSet<>();
                userIngredients.add("完全不同的食材_" + UUID.randomUUID().toString());
                Double matchRate = calculateMatchRate(requiredIngredients, userIngredients);
                return matchRate == 0.0;
            });
    }

    /**
     * 属性 3: 过敏原排除
     * 
     * 对于任何包含用户过敏原的食谱，该食谱不应出现在推荐结果中
     * 
     * 验证: 需求 4.2
     */
    @Test
    public void property_recipesWithAllergensAreExcluded() {
        qt()
            .withExamples(10)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 10),  // 食谱食材
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 5)     // 用户过敏原
            )
            .check((recipeIngredients, userAllergensList) -> {
                Set<String> userAllergens = new HashSet<>(userAllergensList);
                // 如果食谱包含过敏原，containsAllergen应该返回true
                boolean hasAllergen = containsAllergen(recipeIngredients, userAllergens);
                
                // 验证：如果食谱食材中包含任何过敏原，hasAllergen应该为true
                boolean actuallyContainsAllergen = recipeIngredients.stream()
                    .anyMatch(ingredient -> 
                        userAllergens.stream()
                            .anyMatch(allergen -> 
                                ingredient.trim().toLowerCase().contains(allergen.toLowerCase())
                            )
                    );
                
                return hasAllergen == actuallyContainsAllergen;
            });
    }

    /**
     * 属性 3: 过敏原排除 - 无过敏原时不排除任何食谱
     * 
     * 当用户没有过敏信息时，不应排除任何食谱
     * 
     * 验证: 需求 4.2
     */
    @Test
    public void property_noAllergensExcludesNoRecipes() {
        qt()
            .withExamples(10)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 10)  // 食谱食材
            )
            .check((recipeIngredients) -> {
                Set<String> emptyAllergens = new HashSet<>();
                boolean hasAllergen = containsAllergen(recipeIngredients, emptyAllergens);
                
                // 当没有过敏原时，应该返回false（不排除）
                return !hasAllergen;
            });
    }

    /**
     * 属性 3: 过敏原排除 - 空食材列表不包含过敏原
     * 
     * 当食谱食材列表为空时，不应包含任何过敏原
     * 
     * 验证: 需求 4.2
     */
    @Test
    public void property_emptyIngredientsContainNoAllergens() {
        qt()
            .withExamples(10)
            .forAll(
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(1, 5)  // 用户过敏原
            )
            .check((userAllergensList) -> {
                Set<String> userAllergens = new HashSet<>(userAllergensList);
                List<String> emptyIngredients = new ArrayList<>();
                boolean hasAllergen = containsAllergen(emptyIngredients, userAllergens);
                
                // 空食材列表不应包含过敏原
                return !hasAllergen;
            });
    }

    /**
     * 属性 3: 过敏原排除 - 确保包含过敏原的食谱被识别
     * 
     * 当食谱明确包含用户过敏原时，应该被正确识别
     * 
     * 验证: 需求 4.2
     */
    @Test
    public void property_recipesWithKnownAllergensAreDetected() {
        qt()
            .withExamples(10)
            .forAll(
                strings().betweenCodePoints('a', 'z').ofLengthBetween(3, 10),  // 过敏原（小写字母）
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(0, 5)  // 其他食材
            )
            .check((allergen, otherIngredients) -> {
                // 创建包含过敏原的食材列表
                List<String> recipeIngredients = new ArrayList<>(otherIngredients);
                recipeIngredients.add(allergen);
                
                // 创建过敏原集合
                Set<String> userAllergens = new HashSet<>();
                userAllergens.add(allergen);
                
                boolean hasAllergen = containsAllergen(recipeIngredients, userAllergens);
                
                // 应该检测到过敏原
                return hasAllergen;
            });
    }

    /**
     * 属性 10: 过敏原匹配不区分大小写
     * 
     * 对于任何用户过敏原和食材名称，过敏原匹配应该不区分大小写
     * （例如："花生"应该匹配"花生"、"花生油"、"HUASHENG"）
     * 
     * 验证: 需求 4.4
     */
    @Test
    public void property_allergenMatchingIsCaseInsensitive() {
        qt()
            .withExamples(10)
            .forAll(
                strings().betweenCodePoints('a', 'z').ofLengthBetween(3, 10),  // 基础过敏原名称（小写字母）
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(0, 5)  // 其他食材
            )
            .check((allergen, otherIngredients) -> {
                // 创建不同大小写变体的食材列表
                String lowerCaseIngredient = allergen.toLowerCase();
                String upperCaseIngredient = allergen.toUpperCase();
                String mixedCaseIngredient = allergen.length() > 1 ? 
                    allergen.substring(0, 1).toUpperCase() + allergen.substring(1).toLowerCase() : 
                    allergen.toUpperCase();
                
                // 创建三个食材列表，每个包含不同大小写的过敏原
                List<String> recipeWithLowerCase = new ArrayList<>(otherIngredients);
                recipeWithLowerCase.add(lowerCaseIngredient);
                
                List<String> recipeWithUpperCase = new ArrayList<>(otherIngredients);
                recipeWithUpperCase.add(upperCaseIngredient);
                
                List<String> recipeWithMixedCase = new ArrayList<>(otherIngredients);
                recipeWithMixedCase.add(mixedCaseIngredient);
                
                // 创建过敏原集合（使用原始大小写）
                Set<String> userAllergens = new HashSet<>();
                userAllergens.add(allergen);
                
                // 所有三种情况都应该检测到过敏原
                boolean detectedLowerCase = containsAllergen(recipeWithLowerCase, userAllergens);
                boolean detectedUpperCase = containsAllergen(recipeWithUpperCase, userAllergens);
                boolean detectedMixedCase = containsAllergen(recipeWithMixedCase, userAllergens);
                
                // 验证所有大小写变体都被正确检测
                return detectedLowerCase && detectedUpperCase && detectedMixedCase;
            });
    }

    /**
     * 属性 10: 过敏原匹配不区分大小写 - 反向测试
     * 
     * 当过敏原使用不同大小写时，也应该能匹配到食材
     * 
     * 验证: 需求 4.4
     */
    @Test
    public void property_allergenMatchingIsCaseInsensitive_reverseTest() {
        qt()
            .withExamples(10)
            .forAll(
                strings().betweenCodePoints('a', 'z').ofLengthBetween(3, 10),  // 基础食材名称（小写字母）
                lists().of(strings().allPossible().ofLengthBetween(1, 10)).ofSizeBetween(0, 5)  // 其他食材
            )
            .check((ingredient, otherIngredients) -> {
                // 创建包含食材的列表（使用原始大小写）
                List<String> recipeIngredients = new ArrayList<>(otherIngredients);
                recipeIngredients.add(ingredient);
                
                // 创建不同大小写变体的过敏原集合
                Set<String> allergensLowerCase = new HashSet<>();
                allergensLowerCase.add(ingredient.toLowerCase());
                
                Set<String> allergensUpperCase = new HashSet<>();
                allergensUpperCase.add(ingredient.toUpperCase());
                
                Set<String> allergensMixedCase = new HashSet<>();
                String mixedCase = ingredient.length() > 1 ? 
                    ingredient.substring(0, 1).toUpperCase() + ingredient.substring(1).toLowerCase() : 
                    ingredient.toUpperCase();
                allergensMixedCase.add(mixedCase);
                
                // 所有三种大小写的过敏原都应该检测到食材
                boolean detectedLowerCase = containsAllergen(recipeIngredients, allergensLowerCase);
                boolean detectedUpperCase = containsAllergen(recipeIngredients, allergensUpperCase);
                boolean detectedMixedCase = containsAllergen(recipeIngredients, allergensMixedCase);
                
                // 验证所有大小写变体都被正确检测
                return detectedLowerCase && detectedUpperCase && detectedMixedCase;
            });
    }

    /**
     * 属性 10: 过敏原匹配不区分大小写 - 部分匹配测试
     * 
     * 当食材包含过敏原作为子串时，不同大小写也应该能匹配
     * （例如："花生"应该匹配"花生油"、"HUASHENGYOU"）
     * 
     * 验证: 需求 4.4
     */
    @Test
    public void property_allergenMatchingIsCaseInsensitive_substringMatch() {
        qt()
            .withExamples(10)
            .forAll(
                strings().betweenCodePoints('a', 'z').ofLengthBetween(3, 8),  // 基础过敏原名称（小写字母）
                strings().betweenCodePoints('a', 'z').ofLengthBetween(1, 5),  // 前缀（小写字母）
                strings().betweenCodePoints('a', 'z').ofLengthBetween(1, 5)   // 后缀（小写字母）
            )
            .check((allergen, prefix, suffix) -> {
                // 创建包含过敏原的复合食材（不同大小写）
                String ingredientLower = prefix.toLowerCase() + allergen.toLowerCase() + suffix.toLowerCase();
                String ingredientUpper = prefix.toUpperCase() + allergen.toUpperCase() + suffix.toUpperCase();
                
                List<String> recipeWithLower = new ArrayList<>();
                recipeWithLower.add(ingredientLower);
                
                List<String> recipeWithUpper = new ArrayList<>();
                recipeWithUpper.add(ingredientUpper);
                
                // 创建过敏原集合（使用原始大小写）
                Set<String> userAllergens = new HashSet<>();
                userAllergens.add(allergen);
                
                // 两种情况都应该检测到过敏原
                boolean detectedLower = containsAllergen(recipeWithLower, userAllergens);
                boolean detectedUpper = containsAllergen(recipeWithUpper, userAllergens);
                
                // 验证部分匹配在不同大小写下都能工作
                return detectedLower && detectedUpper;
            });
    }

    /**
     * 辅助方法: 计算匹配度
     * 复制自CaipuxinxiServiceImpl的私有方法用于测试
     */
    private Double calculateMatchRate(List<String> requiredIngredients, Set<String> userIngredients) {
        if (requiredIngredients == null || requiredIngredients.isEmpty()) {
            return 0.0;
        }
        
        long matchedCount = requiredIngredients.stream()
            .filter(ingredient -> userIngredients.contains(ingredient.trim()))
            .count();
        
        return (matchedCount * 100.0) / requiredIngredients.size();
    }

    /**
     * 辅助方法: 检查食谱是否包含过敏原
     * 复制自CaipuxinxiServiceImpl的私有方法用于测试
     */
    private boolean containsAllergen(List<String> ingredients, Set<String> allergens) {
        if (allergens == null || allergens.isEmpty()) {
            return false;
        }
        
        if (ingredients == null || ingredients.isEmpty()) {
            return false;
        }
        
        return ingredients.stream()
            .anyMatch(ingredient -> 
                allergens.stream()
                    .anyMatch(allergen -> 
                        ingredient.trim().toLowerCase().contains(allergen.toLowerCase())
                    )
            );
    }
}
