package com.utils;

/**
 * 缓存键工具类
 * 
 * 统一管理Redis缓存的Key命名规则，避免Key冲突
 * 
 * @author 
 * @date 2023-04-25
 */
public class CacheKeyUtil {

    /**
     * 缓存键前缀
     */
    private static final String PREFIX = "recipe:";

    /**
     * 用户推荐结果缓存键前缀
     * 格式: recipe:recommend:user:{userId}:{recommendType}:{sortType}:{pageNum}:{pageSize}
     */
    private static final String RECOMMEND_PREFIX = PREFIX + "recommend:user:";

    /**
     * 热门食谱列表缓存键
     * 格式: recipe:hot:list:{pageNum}:{pageSize}
     */
    private static final String HOT_RECIPES_PREFIX = PREFIX + "hot:list:";

    /**
     * 食材-食谱关联关系缓存键
     * 格式: recipe:ingredient:relation
     */
    private static final String INGREDIENT_RELATION_KEY = PREFIX + "ingredient:relation";

    /**
     * 用户食材库存缓存键
     * 格式: recipe:user:ingredients:{userId}
     */
    private static final String USER_INGREDIENTS_PREFIX = PREFIX + "user:ingredients:";

    /**
     * 用户过敏信息缓存键
     * 格式: recipe:user:allergy:{userId}
     */
    private static final String USER_ALLERGY_PREFIX = PREFIX + "user:allergy:";

    /**
     * 用户收藏食谱缓存键
     * 格式: recipe:user:favorites:{userId}
     */
    private static final String USER_FAVORITES_PREFIX = PREFIX + "user:favorites:";

    /**
     * 用户浏览历史缓存键
     * 格式: recipe:user:history:{userId}
     */
    private static final String USER_HISTORY_PREFIX = PREFIX + "user:history:";

    /**
     * 食谱详情缓存键
     * 格式: recipe:detail:{recipeId}
     */
    private static final String RECIPE_DETAIL_PREFIX = PREFIX + "detail:";

    /**
     * 生成用户推荐结果缓存键
     * 
     * @param userId 用户ID
     * @param recommendType 推荐类型
     * @param sortType 排序类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 缓存键
     */
    public static String getRecommendKey(Long userId, String recommendType, String sortType, Integer pageNum, Integer pageSize) {
        return RECOMMEND_PREFIX + userId + ":" + recommendType + ":" + sortType + ":" + pageNum + ":" + pageSize;
    }

    /**
     * 生成用户推荐结果缓存键模式（用于批量删除）
     * 
     * @param userId 用户ID
     * @return 缓存键模式
     */
    public static String getRecommendKeyPattern(Long userId) {
        return RECOMMEND_PREFIX + userId + ":*";
    }

    /**
     * 生成热门食谱列表缓存键
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 缓存键
     */
    public static String getHotRecipesKey(Integer pageNum, Integer pageSize) {
        return HOT_RECIPES_PREFIX + pageNum + ":" + pageSize;
    }

    /**
     * 生成热门食谱列表缓存键模式（用于批量删除）
     * 
     * @return 缓存键模式
     */
    public static String getHotRecipesKeyPattern() {
        return HOT_RECIPES_PREFIX + "*";
    }

    /**
     * 获取食材-食谱关联关系缓存键
     * 
     * @return 缓存键
     */
    public static String getIngredientRelationKey() {
        return INGREDIENT_RELATION_KEY;
    }

    /**
     * 生成用户食材库存缓存键
     * 
     * @param userId 用户ID
     * @return 缓存键
     */
    public static String getUserIngredientsKey(Long userId) {
        return USER_INGREDIENTS_PREFIX + userId;
    }

    /**
     * 生成用户过敏信息缓存键
     * 
     * @param userId 用户ID
     * @return 缓存键
     */
    public static String getUserAllergyKey(Long userId) {
        return USER_ALLERGY_PREFIX + userId;
    }

    /**
     * 生成用户收藏食谱缓存键
     * 
     * @param userId 用户ID
     * @return 缓存键
     */
    public static String getUserFavoritesKey(Long userId) {
        return USER_FAVORITES_PREFIX + userId;
    }

    /**
     * 生成用户浏览历史缓存键
     * 
     * @param userId 用户ID
     * @return 缓存键
     */
    public static String getUserHistoryKey(Long userId) {
        return USER_HISTORY_PREFIX + userId;
    }

    /**
     * 生成食谱详情缓存键
     * 
     * @param recipeId 食谱ID
     * @return 缓存键
     */
    public static String getRecipeDetailKey(Long recipeId) {
        return RECIPE_DETAIL_PREFIX + recipeId;
    }

    /**
     * 清除用户相关的所有缓存
     * 
     * @param userId 用户ID
     * @return 缓存键模式
     */
    public static String getUserAllCachePattern(Long userId) {
        return PREFIX + "*:" + userId + ":*";
    }
}
