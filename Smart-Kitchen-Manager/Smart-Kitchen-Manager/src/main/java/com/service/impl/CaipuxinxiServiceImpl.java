package com.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.utils.RedisCache;
import com.utils.CacheKeyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import com.dao.CaipuxinxiDao;
import com.dao.UserShicaiDao;
import com.dao.YonghuDao;
import com.dao.StoreupDao;
import com.entity.CaipuxinxiEntity;
import com.entity.UserShicaiEntity;
import com.entity.YonghuEntity;
import com.entity.StoreupEntity;
import com.service.CaipuxinxiService;
import com.entity.vo.CaipuxinxiVO;
import com.entity.view.CaipuxinxiView;
import com.dto.RecipeRecommendationDTO;

@Service("caipuxinxiService")
public class CaipuxinxiServiceImpl extends ServiceImpl<CaipuxinxiDao, CaipuxinxiEntity> implements CaipuxinxiService {
	
	private static final Logger log = LoggerFactory.getLogger(CaipuxinxiServiceImpl.class);
	
	@Autowired
	private UserShicaiDao userShicaiDao;
	
	@Autowired
	private YonghuDao yonghuDao;
	
	@Autowired
	private StoreupDao storeupDao;
	
	@Autowired
	private RedisCache redisCache;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CaipuxinxiEntity> page = this.selectPage(
                new Query<CaipuxinxiEntity>(params).getPage(),
                new EntityWrapper<CaipuxinxiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<CaipuxinxiEntity> wrapper) {
		  Page<CaipuxinxiView> page =new Query<CaipuxinxiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<CaipuxinxiVO> selectListVO(Wrapper<CaipuxinxiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public CaipuxinxiVO selectVO(Wrapper<CaipuxinxiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<CaipuxinxiView> selectListView(Wrapper<CaipuxinxiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public CaipuxinxiView selectView(Wrapper<CaipuxinxiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<CaipuxinxiEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<CaipuxinxiEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<CaipuxinxiEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }

    /**
     * 计算匹配度
     * 
     * 匹配度计算公式: (已拥有食材数 / 总需求食材数) * 100
     * 
     * @param requiredIngredients 食谱所需食材列表
     * @param userIngredients 用户拥有的食材集合
     * @return 匹配度（0-100）
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
     * 检查食谱是否包含过敏原
     * 
     * 逻辑:
     * 1. 如果用户没有过敏信息，返回false（不排除任何食谱）
     * 2. 检查食谱食材列表中是否包含任何过敏原
     * 3. 使用不区分大小写的匹配
     * 
     * @param ingredients 食谱食材列表
     * @param allergens 用户过敏原集合
     * @return 如果包含过敏原返回true，否则返回false
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

    /**
     * 识别用户拥有的所需食材
     * 
     * 从食谱所需食材列表中筛选出用户拥有的食材
     * 
     * @param requiredIngredients 食谱所需食材列表
     * @param userIngredients 用户拥有的食材集合
     * @return 用户拥有的所需食材列表
     */
    private List<String> identifyOwnedIngredients(List<String> requiredIngredients, Set<String> userIngredients) {
        if (requiredIngredients == null || requiredIngredients.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        if (userIngredients == null || userIngredients.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        return requiredIngredients.stream()
            .filter(ingredient -> userIngredients.contains(ingredient.trim()))
            .map(String::trim)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 识别用户缺少的所需食材
     * 
     * 从食谱所需食材列表中筛选出用户缺少的食材
     * 
     * @param requiredIngredients 食谱所需食材列表
     * @param userIngredients 用户拥有的食材集合
     * @return 用户缺少的所需食材列表
     */
    private List<String> identifyMissingIngredients(List<String> requiredIngredients, Set<String> userIngredients) {
        if (requiredIngredients == null || requiredIngredients.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        if (userIngredients == null || userIngredients.isEmpty()) {
            return new java.util.ArrayList<>(requiredIngredients);
        }
        
        return requiredIngredients.stream()
            .filter(ingredient -> !userIngredients.contains(ingredient.trim()))
            .map(String::trim)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 对推荐结果进行排序
     * 
     * 排序策略:
     * 1. matchRate (默认): 按匹配度降序，匹配度相同时按食谱ID升序
     * 2. popularity: 按分数降序，分数相同时按食谱ID升序
     * 
     * @param recommendations 推荐结果列表
     * @param sortType 排序类型
     */
    private void sortRecommendations(List<com.dto.RecipeRecommendationDTO> recommendations, String sortType) {
        if (recommendations == null || recommendations.isEmpty()) {
            return;
        }
        
        if ("popularity".equals(sortType)) {
            // 按热度（分数）排序
            recommendations.sort(
                java.util.Comparator.comparing(com.dto.RecipeRecommendationDTO::getFenshu, 
                    java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder()))
                    .thenComparing(com.dto.RecipeRecommendationDTO::getId)
            );
        } else {
            // 默认按匹配度排序
            recommendations.sort(
                java.util.Comparator.comparing(com.dto.RecipeRecommendationDTO::getMatchRate, 
                    java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder()))
                    .thenComparing(com.dto.RecipeRecommendationDTO::getId)
            );
        }
    }

    /**
     * 标记高优先级并设置缺失食材信息
     * 
     * 逻辑:
     * 1. 匹配度 >= 60% 标记为高优先级
     * 2. 匹配度 < 60% 包含缺失食材数量
     * 
     * @param dto 推荐结果DTO
     */
    private void markPriorityAndMissingInfo(com.dto.RecipeRecommendationDTO dto) {
        if (dto == null) {
            return;
        }
        
        Double matchRate = dto.getMatchRate();
        if (matchRate == null) {
            matchRate = 0.0;
        }
        
        // 标记高优先级
        dto.setHighPriority(matchRate >= 60.0);
        
        // 设置缺失食材数量
        List<String> missingIngredients = dto.getMissingIngredients();
        if (missingIngredients != null) {
            dto.setMissingCount(missingIngredients.size());
        } else {
            dto.setMissingCount(0);
        }
    }

    /**
     * 获取智能推荐列表
     * 
     * 基于用户当前的食材库存，计算食谱与用户食材的匹配度，
     * 并过滤包含过敏原的食谱，为用户提供个性化的食谱推荐。
     * 
     * @param userId 用户ID
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @param sortType 排序类型（matchRate-匹配度, popularity-热度）
     * @return 推荐结果分页数据
     */
    @Override
    public PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType) {
        long startTime = System.currentTimeMillis();
        
        log.info("========== 开始处理推荐请求 ==========");
        log.info("用户ID: {}, 页码: {}, 每页大小: {}, 排序类型: {}", 
            userId, pageNum, pageSize, sortType);
        
        try {
            // 1. 获取用户有效食材
            log.info("正在查询用户 {} 的有效食材...", userId);
            List<UserShicaiEntity> userIngredientsList = userShicaiDao.selectValidIngredientsByUserId(userId);
            log.info("查询结果: userIngredientsList = {}", userIngredientsList);
            log.info("查询结果大小: {}", userIngredientsList == null ? "null" : userIngredientsList.size());
            
            // 如果用户没有有效食材，返回空列表
            if (userIngredientsList == null || userIngredientsList.isEmpty()) {
                log.warn("用户 {} 没有有效食材，返回空推荐列表", userId);
                Page<RecipeRecommendationDTO> page = new Page<>(pageNum, pageSize);
                page.setRecords(new ArrayList<>());
                page.setTotal(0);
                return new PageUtils(page);
            }
            
            log.debug("用户 {} 拥有 {} 种有效食材", userId, userIngredientsList.size());
            
            // 将用户食材转换为Set，便于快速查找
            Set<String> userIngredientsSet = userIngredientsList.stream()
                .map(entity -> entity.getShicaiName().trim().toLowerCase())
                .collect(Collectors.toSet());
            
            // 2. 获取用户过敏信息
            YonghuEntity user = yonghuDao.selectById(userId);
            Set<String> allergySet = new HashSet<>();
            if (user != null && user.getAllergyInfo() != null && !user.getAllergyInfo().trim().isEmpty()) {
                allergySet = Arrays.stream(user.getAllergyInfo().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());
                log.debug("用户 {} 有 {} 种过敏原", userId, allergySet.size());
            }
            
            // 3. 获取所有食谱及食材
            List<Map<String, Object>> recipesList = baseMapper.selectAllRecipesWithIngredients();
            log.debug("查询到 {} 个食谱", recipesList.size());
            
            // 4. 计算匹配度并过滤过敏原
            List<RecipeRecommendationDTO> recommendations = new ArrayList<>();
            int filteredByAllergyCount = 0;
            
            for (Map<String, Object> recipeMap : recipesList) {
                // 解析食材列表
                String cailiaoStr = (String) recipeMap.get("cailiao");
                if (cailiaoStr == null || cailiaoStr.trim().isEmpty()) {
                    continue;
                }
                
                List<String> requiredIngredients = Arrays.stream(cailiaoStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
                
                if (requiredIngredients.isEmpty()) {
                    continue;
                }
                
                // 检查是否包含过敏原
                if (containsAllergen(requiredIngredients, allergySet)) {
                    filteredByAllergyCount++;
                    continue;
                }
                
                // 计算匹配度
                Double matchRate = calculateMatchRate(requiredIngredients, userIngredientsSet);
                
                // 识别缺失食材
                List<String> missingIngredients = identifyMissingIngredients(requiredIngredients, userIngredientsSet);
                
                // 构建DTO
                RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
                dto.setId(((Number) recipeMap.get("id")).longValue());
                dto.setCaipumingcheng((String) recipeMap.get("caipumingcheng"));
                dto.setCaipufengmian((String) recipeMap.get("caipufengmian"));
                dto.setCaishileixing((String) recipeMap.get("caishileixing"));
                dto.setPengrenfangshi((String) recipeMap.get("pengrenfangshi"));
                
                Object fenshuObj = recipeMap.get("fenshu");
                if (fenshuObj != null) {
                    dto.setFenshu(((Number) fenshuObj).intValue());
                }
                
                dto.setZhizuoliucheng((String) recipeMap.get("zhizuoliucheng"));
                dto.setMatchRate(matchRate);
                dto.setRequiredIngredients(requiredIngredients);
                dto.setMissingIngredients(missingIngredients);
                
                // 标记高优先级和缺失食材数量
                markPriorityAndMissingInfo(dto);
                
                recommendations.add(dto);
            }
            
            log.debug("过滤后剩余 {} 个推荐食谱（因过敏原过滤了 {} 个）", 
                recommendations.size(), filteredByAllergyCount);
            
            // 5. 排序
            sortRecommendations(recommendations, sortType);
            
            // 6. 分页
            int total = recommendations.size();
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);
            
            List<RecipeRecommendationDTO> pageRecords;
            if (fromIndex >= total) {
                pageRecords = new ArrayList<>();
            } else {
                pageRecords = new ArrayList<>(recommendations.subList(fromIndex, toIndex));
            }
            
            Page<RecipeRecommendationDTO> page = new Page<>(pageNum, pageSize);
            page.setRecords(pageRecords);
            page.setTotal(total);
            
            long endTime = System.currentTimeMillis();
            log.info("推荐请求处理完成 - 用户ID: {}, 返回结果数: {}/{}, 耗时: {}ms", 
                userId, pageRecords.size(), total, (endTime - startTime));
            
            return new PageUtils(page);
            
        } catch (Exception e) {
            log.error("处理推荐请求时发生异常 - 用户ID: {}", userId, e);
            throw e;
        }
    }

    /**
     * 获取智能推荐列表（优化版，支持缓存和多种推荐策略）
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param sortType 排序类型
     * @param recommendType 推荐类型（stock_based/hot/personalized）
     * @param refresh 是否刷新缓存
     * @return 推荐结果分页数据
     */
    @Override
    public PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, 
                                       String sortType, String recommendType, Boolean refresh) {
        return getRecommendations(userId, pageNum, pageSize, sortType, recommendType, refresh, null);
    }
    
    /**
     * 获取智能推荐列表（优化版，支持缓存和搜索过滤）
     */
    @Override
    public PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, 
                                       String sortType, String recommendType, Boolean refresh, 
                                       Map<String, Object> searchParams) {
        long startTime = System.currentTimeMillis();
        
        log.info("开始处理优化推荐请求 - 用户ID: {}, 推荐类型: {}, 排序: {}, 刷新: {}, 搜索参数: {}", 
            userId, recommendType, sortType, refresh, searchParams);
        
        try {
            // 1. 检查缓存（如果不刷新且无搜索条件）
            boolean hasSearchParams = searchParams != null && !searchParams.isEmpty();
            if ((refresh == null || !refresh) && !hasSearchParams) {
                String cacheKey = CacheKeyUtil.getRecommendKey(userId, recommendType, sortType, pageNum, pageSize);
                Object cachedResult = redisCache.get(cacheKey);
                
                if (cachedResult != null) {
                    log.info("命中缓存 - 用户ID: {}, Key: {}", userId, cacheKey);
                    try {
                        String json = objectMapper.writeValueAsString(cachedResult);
                        PageUtils pageUtils = objectMapper.readValue(json, PageUtils.class);
                        return pageUtils;
                    } catch (Exception e) {
                        log.warn("缓存数据反序列化失败，重新计算 - 用户ID: {}", userId, e);
                    }
                }
            }
            
            // 2. 调用原有方法获取推荐结果
            PageUtils result;
            switch (recommendType) {
                case "hot":
                    result = getHotRecommendations(userId, pageNum, pageSize, sortType);
                    break;
                case "personalized":
                    result = getPersonalizedRecommendations(userId, pageNum, pageSize, sortType);
                    break;
                case "stock_based":
                default:
                    result = getStockBasedRecommendations(userId, pageNum, pageSize, sortType);
                    break;
            }
            
            // 3. 如果有搜索参数，过滤结果
            if (hasSearchParams && result != null && result.getList() != null) {
                List<?> originalList = result.getList();
                List<Object> filteredList = new ArrayList<>();
                
                for (Object item : originalList) {
                    if (item instanceof RecipeRecommendationDTO) {
                        RecipeRecommendationDTO dto = (RecipeRecommendationDTO) item;
                        if (matchesSearchParamsDTO(dto, searchParams)) {
                            filteredList.add(dto);
                        }
                    }
                }
                
                // 重新分页
                int total = filteredList.size();
                int start = (pageNum - 1) * pageSize;
                int end = Math.min(start + pageSize, total);
                
                if (start < total) {
                    result.setList(filteredList.subList(start, end));
                } else {
                    result.setList(new ArrayList<>());
                }
                result.setTotal(total);
                result.setTotalPage((total + pageSize - 1) / pageSize);
            }
            
            // 4. 缓存结果（仅在无搜索条件时）
            if (!hasSearchParams) {
                String cacheKey = CacheKeyUtil.getRecommendKey(userId, recommendType, sortType, pageNum, pageSize);
                redisCache.set(cacheKey, result, 1, TimeUnit.HOURS);
            }
            
            long endTime = System.currentTimeMillis();
            log.info("优化推荐请求处理完成 - 用户ID: {}, 耗时: {}ms", userId, (endTime - startTime));
            
            return result;
            
        } catch (Exception e) {
            log.error("处理优化推荐请求时发生异常 - 用户ID: {}", userId, e);
            throw new RuntimeException("推荐服务异常", e);
        }
    }
    
    /**
     * 检查DTO是否匹配搜索参数
     */
    private boolean matchesSearchParamsDTO(RecipeRecommendationDTO dto, Map<String, Object> searchParams) {
        if (searchParams == null || searchParams.isEmpty()) {
            return true;
        }
        
        // 检查菜谱名称（模糊匹配）
        if (searchParams.containsKey("caipumingcheng")) {
            String searchName = (String) searchParams.get("caipumingcheng");
            String recipeName = dto.getCaipumingcheng();
            if (searchName != null && !searchName.trim().isEmpty()) {
                if (recipeName == null || !recipeName.toLowerCase().contains(searchName.toLowerCase())) {
                    return false;
                }
            }
        }
        
        // 检查菜式类型（精确匹配）
        if (searchParams.containsKey("caishileixing")) {
            String searchType = (String) searchParams.get("caishileixing");
            String recipeType = dto.getCaishileixing();
            if (searchType != null && !searchType.trim().isEmpty()) {
                if (recipeType == null || !recipeType.equals(searchType)) {
                    return false;
                }
            }
        }
        
        // 检查烹饪方式（模糊匹配）
        if (searchParams.containsKey("pengrenfangshi")) {
            String searchMethod = (String) searchParams.get("pengrenfangshi");
            String recipeMethod = dto.getPengrenfangshi();
            if (searchMethod != null && !searchMethod.trim().isEmpty()) {
                if (recipeMethod == null || !recipeMethod.toLowerCase().contains(searchMethod.toLowerCase())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 基于库存的推荐（增强版）
     * 
     * 融合用户健康偏好和热门权重
     */
    private PageUtils getStockBasedRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType) {
        log.debug("执行基于库存的推荐 - 用户ID: {}", userId);
        
        // 1. 获取用户有效食材
        List<UserShicaiEntity> userIngredientsList = userShicaiDao.selectValidIngredientsByUserId(userId);
        if (userIngredientsList == null || userIngredientsList.isEmpty()) {
            log.warn("用户 {} 没有有效食材，返回空推荐列表", userId);
            Page<RecipeRecommendationDTO> page = new Page<>(pageNum, pageSize);
            page.setRecords(new ArrayList<>());
            page.setTotal(0);
            return new PageUtils(page);
        }
        
        Set<String> userIngredientsSet = userIngredientsList.stream()
            .map(entity -> entity.getShicaiName().trim().toLowerCase())
            .collect(Collectors.toSet());
        
        // 2. 获取用户过敏信息和健康偏好
        YonghuEntity user = yonghuDao.selectById(userId);
        Set<String> allergySet = getUserAllergySet(user);
        String healthPreference = user != null ? user.getHealthPreference() : null;
        
        // 3. 获取用户收藏和浏览历史（用于去重）
        Set<Long> userFavorites = getUserFavoriteRecipeIds(userId);
        Set<Long> userHistory = getUserHistoryRecipeIds(userId);
        
        // 4. 获取所有食谱
        List<Map<String, Object>> recipesList = baseMapper.selectAllRecipesWithIngredients();
        
        // 5. 计算推荐分数
        List<RecipeRecommendationDTO> recommendations = new ArrayList<>();
        
        for (Map<String, Object> recipeMap : recipesList) {
            Long recipeId = ((Number) recipeMap.get("id")).longValue();
            
            // 去重：跳过已收藏或近期浏览的食谱
            if (userFavorites.contains(recipeId) || userHistory.contains(recipeId)) {
                continue;
            }
            
            // 解析食材
            String cailiaoStr = (String) recipeMap.get("cailiao");
            if (cailiaoStr == null || cailiaoStr.trim().isEmpty()) {
                continue;
            }
            
            List<String> requiredIngredients = Arrays.stream(cailiaoStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
            
            if (requiredIngredients.isEmpty()) {
                continue;
            }
            
            // 过滤过敏原
            if (containsAllergen(requiredIngredients, allergySet)) {
                continue;
            }
            
            // 计算匹配度
            Double matchRate = calculateMatchRate(requiredIngredients, userIngredientsSet);
            
            // 检查健康偏好匹配
            boolean matchesHealthPreference = checkHealthPreference(recipeMap, healthPreference);
            
            // 计算热门权重
            Double popularityScore = calculatePopularityScore(recipeMap);
            
            // 计算综合分数：匹配度 * 热门系数 * 健康偏好加成
            Double finalScore = matchRate * popularityScore;
            if (matchesHealthPreference) {
                finalScore *= 1.2; // 健康偏好匹配加成20%
            }
            
            // 构建DTO
            RecipeRecommendationDTO dto = buildRecommendationDTO(recipeMap, requiredIngredients, 
                userIngredientsSet, matchRate, finalScore);
            
            recommendations.add(dto);
        }
        
        // 6. 排序
        sortRecommendationsByScore(recommendations, sortType);
        
        // 7. 分页
        return paginateRecommendations(recommendations, pageNum, pageSize);
    }
    
    /**
     * 热门推荐
     */
    private PageUtils getHotRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType) {
        log.debug("执行热门推荐 - 用户ID: {}", userId);
        
        // 检查热门食谱缓存
        String cacheKey = CacheKeyUtil.getHotRecipesKey(pageNum, pageSize);
        Object cachedResult = redisCache.get(cacheKey);
        
        if (cachedResult != null) {
            log.debug("命中热门食谱缓存");
            try {
                String json = objectMapper.writeValueAsString(cachedResult);
                return objectMapper.readValue(json, PageUtils.class);
            } catch (Exception e) {
                log.warn("热门食谱缓存反序列化失败", e);
            }
        }
        
        // 获取用户过敏信息
        YonghuEntity user = yonghuDao.selectById(userId);
        Set<String> allergySet = getUserAllergySet(user);
        
        // 获取所有食谱并按热度排序
        List<Map<String, Object>> recipesList = baseMapper.selectAllRecipesWithIngredients();
        
        List<RecipeRecommendationDTO> recommendations = new ArrayList<>();
        
        for (Map<String, Object> recipeMap : recipesList) {
            // 解析食材
            String cailiaoStr = (String) recipeMap.get("cailiao");
            if (cailiaoStr == null || cailiaoStr.trim().isEmpty()) {
                continue;
            }
            
            List<String> requiredIngredients = Arrays.stream(cailiaoStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
            
            // 过滤过敏原
            if (containsAllergen(requiredIngredients, allergySet)) {
                continue;
            }
            
            // 计算热门分数
            Double popularityScore = calculatePopularityScore(recipeMap);
            
            // 构建DTO
            RecipeRecommendationDTO dto = buildRecommendationDTO(recipeMap, requiredIngredients, 
                new HashSet<>(), 0.0, popularityScore);
            
            recommendations.add(dto);
        }
        
        // 按热度排序
        recommendations.sort(Comparator.comparing(RecipeRecommendationDTO::getFenshu, 
            Comparator.nullsLast(Comparator.reverseOrder())));
        
        // 分页
        PageUtils result = paginateRecommendations(recommendations, pageNum, pageSize);
        
        // 缓存热门食谱（6小时）
        redisCache.set(cacheKey, result, 6, TimeUnit.HOURS);
        
        return result;
    }
    
    /**
     * 个性化推荐（协同过滤基础版）
     */
    private PageUtils getPersonalizedRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType) {
        log.debug("执行个性化推荐 - 用户ID: {}", userId);
        
        // 1. 获取用户食材库存
        List<UserShicaiEntity> userIngredients = userShicaiDao.selectValidIngredientsByUserId(userId);
        if (userIngredients == null || userIngredients.isEmpty()) {
            return getHotRecommendations(userId, pageNum, pageSize, sortType);
        }
        
        Set<String> userIngredientsSet = userIngredients.stream()
            .map(entity -> entity.getShicaiName().trim().toLowerCase())
            .collect(Collectors.toSet());
        
        // 2. 查找相似用户（拥有相似食材的用户）
        List<Long> similarUserIds = findSimilarUsers(userId, userIngredientsSet);
        
        // 3. 获取相似用户喜欢的食谱
        Set<Long> recommendedRecipeIds = new HashSet<>();
        for (Long similarUserId : similarUserIds) {
            Set<Long> favorites = getUserFavoriteRecipeIds(similarUserId);
            recommendedRecipeIds.addAll(favorites);
        }
        
        // 4. 排除当前用户已收藏的
        Set<Long> userFavorites = getUserFavoriteRecipeIds(userId);
        recommendedRecipeIds.removeAll(userFavorites);
        
        // 5. 获取推荐食谱详情
        if (recommendedRecipeIds.isEmpty()) {
            return getStockBasedRecommendations(userId, pageNum, pageSize, sortType);
        }
        
        YonghuEntity user = yonghuDao.selectById(userId);
        Set<String> allergySet = getUserAllergySet(user);
        
        List<RecipeRecommendationDTO> recommendations = new ArrayList<>();
        
        for (Long recipeId : recommendedRecipeIds) {
            CaipuxinxiEntity recipe = baseMapper.selectById(recipeId);
            if (recipe == null) {
                continue;
            }
            
            // 解析食材
            String cailiaoStr = recipe.getCailiao();
            if (cailiaoStr == null || cailiaoStr.trim().isEmpty()) {
                continue;
            }
            
            List<String> requiredIngredients = Arrays.stream(cailiaoStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
            
            // 过滤过敏原
            if (containsAllergen(requiredIngredients, allergySet)) {
                continue;
            }
            
            // 计算匹配度
            Double matchRate = calculateMatchRate(requiredIngredients, userIngredientsSet);
            Double popularityScore = calculatePopularityScore(convertEntityToMap(recipe));
            
            // 构建DTO
            RecipeRecommendationDTO dto = buildRecommendationDTO(convertEntityToMap(recipe), 
                requiredIngredients, userIngredientsSet, matchRate, popularityScore);
            
            recommendations.add(dto);
        }
        
        // 排序
        sortRecommendationsByScore(recommendations, sortType);
        
        // 分页
        return paginateRecommendations(recommendations, pageNum, pageSize);
    }

    /**
     * 获取用户过敏原集合
     */
    private Set<String> getUserAllergySet(YonghuEntity user) {
        Set<String> allergySet = new HashSet<>();
        if (user != null && user.getAllergyInfo() != null && !user.getAllergyInfo().trim().isEmpty()) {
            allergySet = Arrays.stream(user.getAllergyInfo().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
        }
        return allergySet;
    }
    
    /**
     * 获取用户收藏的食谱ID集合
     */
    private Set<Long> getUserFavoriteRecipeIds(Long userId) {
        try {
            List<StoreupEntity> storeups = storeupDao.selectList(
                new EntityWrapper<StoreupEntity>()
                    .eq("userid", userId)
                    .eq("tablename", "caipuxinxi")
                    .eq("type", 1)
            );
            
            return storeups.stream()
                .map(StoreupEntity::getRefid)
                .collect(Collectors.toSet());
        } catch (Exception e) {
            log.warn("获取用户收藏失败 - 用户ID: {}", userId, e);
            return new HashSet<>();
        }
    }
    
    /**
     * 获取用户浏览历史食谱ID集合（最近20条）
     */
    private Set<Long> getUserHistoryRecipeIds(Long userId) {
        try {
            // 从缓存获取浏览历史
            String cacheKey = CacheKeyUtil.getUserHistoryKey(userId);
            Object cached = redisCache.get(cacheKey);
            
            if (cached != null) {
                @SuppressWarnings("unchecked")
                Set<Long> history = (Set<Long>) cached;
                return history;
            }
            
            // 如果缓存没有，返回空集合
            return new HashSet<>();
        } catch (Exception e) {
            log.warn("获取用户浏览历史失败 - 用户ID: {}", userId, e);
            return new HashSet<>();
        }
    }
    
    /**
     * 检查食谱是否匹配用户健康偏好
     */
    private boolean checkHealthPreference(Map<String, Object> recipeMap, String healthPreference) {
        if (healthPreference == null || healthPreference.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 解析健康偏好JSON
            Map<String, Object> preference = objectMapper.readValue(
                healthPreference, 
                new TypeReference<Map<String, Object>>() {}
            );
            
            String dietType = (String) preference.get("dietType");
            if (dietType == null || dietType.isEmpty()) {
                return false;
            }
            
            // 获取食谱类型
            String caishileixing = (String) recipeMap.get("caishileixing");
            if (caishileixing == null) {
                return false;
            }
            
            // 匹配逻辑
            switch (dietType.toLowerCase()) {
                case "vegetarian": // 素食
                    return caishileixing.contains("素") || caishileixing.contains("蔬菜");
                case "low_fat": // 低脂
                    return caishileixing.contains("清淡") || caishileixing.contains("蒸");
                case "high_protein": // 高蛋白
                    return caishileixing.contains("肉") || caishileixing.contains("蛋");
                default:
                    return false;
            }
        } catch (Exception e) {
            log.debug("解析健康偏好失败", e);
            return false;
        }
    }
    
    /**
     * 计算热门权重
     * 
     * 综合考虑：收藏量、评分、点击量
     * 公式：(收藏量 * 0.4 + 评分 * 10 * 0.3 + 点击量/100 * 0.3) / 100 + 1
     * 返回值范围：1.0 - 2.0
     */
    private Double calculatePopularityScore(Map<String, Object> recipeMap) {
        try {
            // 获取收藏量（通过查询storeup表）
            Long recipeId = ((Number) recipeMap.get("id")).longValue();
            int storeupCount = storeupDao.selectCount(
                new EntityWrapper<StoreupEntity>()
                    .eq("refid", recipeId)
                    .eq("tablename", "caipuxinxi")
            );
            
            // 获取评分
            Object fenshuObj = recipeMap.get("fenshu");
            int fenshu = fenshuObj != null ? ((Number) fenshuObj).intValue() : 0;
            
            // 获取点击量
            Object clicknumObj = recipeMap.get("clicknum");
            int clicknum = clicknumObj != null ? ((Number) clicknumObj).intValue() : 0;
            
            // 计算综合分数
            double score = (storeupCount * 0.4 + fenshu * 10 * 0.3 + clicknum / 100.0 * 0.3) / 100.0 + 1.0;
            
            // 限制在1.0-2.0范围内
            return Math.min(2.0, Math.max(1.0, score));
            
        } catch (Exception e) {
            log.debug("计算热门权重失败", e);
            return 1.0;
        }
    }
    
    /**
     * 查找相似用户（基于食材相似度）
     * 
     * 简化版协同过滤：找出拥有相似食材的其他用户
     */
    private List<Long> findSimilarUsers(Long userId, Set<String> userIngredients) {
        try {
            // 获取所有用户的食材
            List<UserShicaiEntity> allUserIngredients = userShicaiDao.selectList(
                new EntityWrapper<UserShicaiEntity>()
                    .ne("user_id", userId)
                    .eq("status", "正常")
            );
            
            // 按用户分组
            Map<Long, Set<String>> userIngredientsMap = allUserIngredients.stream()
                .collect(Collectors.groupingBy(
                    UserShicaiEntity::getUserid,
                    Collectors.mapping(
                        entity -> entity.getShicaiName().trim().toLowerCase(),
                        Collectors.toSet()
                    )
                ));
            
            // 计算相似度并排序
            List<Map.Entry<Long, Double>> similarities = new ArrayList<>();
            
            for (Map.Entry<Long, Set<String>> entry : userIngredientsMap.entrySet()) {
                Long otherUserId = entry.getKey();
                Set<String> otherIngredients = entry.getValue();
                
                // 计算Jaccard相似度
                Set<String> intersection = new HashSet<>(userIngredients);
                intersection.retainAll(otherIngredients);
                
                Set<String> union = new HashSet<>(userIngredients);
                union.addAll(otherIngredients);
                
                double similarity = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
                
                if (similarity > 0.3) { // 相似度阈值
                    similarities.add(new java.util.AbstractMap.SimpleEntry<>(otherUserId, similarity));
                }
            }
            
            // 按相似度排序，取前10个
            return similarities.stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.warn("查找相似用户失败", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 构建推荐DTO
     */
    private RecipeRecommendationDTO buildRecommendationDTO(Map<String, Object> recipeMap, 
                                                          List<String> requiredIngredients,
                                                          Set<String> userIngredients,
                                                          Double matchRate,
                                                          Double finalScore) {
        RecipeRecommendationDTO dto = new RecipeRecommendationDTO();
        dto.setId(((Number) recipeMap.get("id")).longValue());
        dto.setCaipumingcheng((String) recipeMap.get("caipumingcheng"));
        dto.setCaipufengmian((String) recipeMap.get("caipufengmian"));
        dto.setCaishileixing((String) recipeMap.get("caishileixing"));
        dto.setPengrenfangshi((String) recipeMap.get("pengrenfangshi"));
        
        Object fenshuObj = recipeMap.get("fenshu");
        if (fenshuObj != null) {
            dto.setFenshu(((Number) fenshuObj).intValue());
        }
        
        dto.setZhizuoliucheng((String) recipeMap.get("zhizuoliucheng"));
        dto.setMatchRate(matchRate);
        dto.setRequiredIngredients(requiredIngredients);
        dto.setMissingIngredients(identifyMissingIngredients(requiredIngredients, userIngredients));
        
        markPriorityAndMissingInfo(dto);
        
        return dto;
    }
    
    /**
     * 按综合分数排序
     */
    private void sortRecommendationsByScore(List<RecipeRecommendationDTO> recommendations, String sortType) {
        if (recommendations == null || recommendations.isEmpty()) {
            return;
        }
        
        if ("popularity".equals(sortType)) {
            recommendations.sort(
                Comparator.comparing(RecipeRecommendationDTO::getFenshu, 
                    Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(RecipeRecommendationDTO::getId)
            );
        } else {
            recommendations.sort(
                Comparator.comparing(RecipeRecommendationDTO::getMatchRate, 
                    Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(RecipeRecommendationDTO::getId)
            );
        }
    }
    
    /**
     * 分页处理
     */
    private PageUtils paginateRecommendations(List<RecipeRecommendationDTO> recommendations, 
                                              Integer pageNum, Integer pageSize) {
        int total = recommendations.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        List<RecipeRecommendationDTO> pageRecords;
        if (fromIndex >= total) {
            pageRecords = new ArrayList<>();
        } else {
            pageRecords = new ArrayList<>(recommendations.subList(fromIndex, toIndex));
        }
        
        Page<RecipeRecommendationDTO> page = new Page<>(pageNum, pageSize);
        page.setRecords(pageRecords);
        page.setTotal(total);
        
        return new PageUtils(page);
    }
    
    /**
     * 将Entity转换为Map
     */
    private Map<String, Object> convertEntityToMap(CaipuxinxiEntity entity) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", entity.getId());
        map.put("caipumingcheng", entity.getCaipumingcheng());
        map.put("caipufengmian", entity.getCaipufengmian());
        map.put("caishileixing", entity.getCaishileixing());
        map.put("pengrenfangshi", entity.getPengrenfangshi());
        map.put("fenshu", entity.getFenshu());
        map.put("zhizuoliucheng", entity.getZhizuoliucheng());
        map.put("cailiao", entity.getCailiao());
        map.put("clicknum", entity.getClicknum());
        return map;
    }
    
    /**
     * 清除用户推荐缓存
     */
    @Override
    public void clearUserRecommendCache(Long userId) {
        try {
            String pattern = CacheKeyUtil.getRecommendKeyPattern(userId);
            Long deletedCount = redisCache.deleteByPattern(pattern);
            log.info("清除用户推荐缓存 - 用户ID: {}, 删除数量: {}", userId, deletedCount);
        } catch (Exception e) {
            log.error("清除用户推荐缓存失败 - 用户ID: {}", userId, e);
        }
    }
    
    /**
     * 清除热门食谱缓存
     */
    @Override
    public void clearHotRecipesCache() {
        try {
            String pattern = CacheKeyUtil.getHotRecipesKeyPattern();
            Long deletedCount = redisCache.deleteByPattern(pattern);
            log.info("清除热门食谱缓存 - 删除数量: {}", deletedCount);
        } catch (Exception e) {
            log.error("清除热门食谱缓存失败", e);
        }
    }
    
    /**
     * 检查食谱是否匹配搜索参数
     * 
     * @param recipeMap 食谱数据
     * @param searchParams 搜索参数
     * @return 是否匹配
     */
    private boolean matchesSearchParams(Map<String, Object> recipeMap, Map<String, Object> searchParams) {
        if (searchParams == null || searchParams.isEmpty()) {
            return true;
        }
        
        // 检查菜谱名称（模糊匹配）
        if (searchParams.containsKey("caipumingcheng")) {
            String searchName = (String) searchParams.get("caipumingcheng");
            String recipeName = (String) recipeMap.get("caipumingcheng");
            if (searchName != null && !searchName.trim().isEmpty()) {
                if (recipeName == null || !recipeName.toLowerCase().contains(searchName.toLowerCase())) {
                    return false;
                }
            }
        }
        
        // 检查菜式类型（精确匹配）
        if (searchParams.containsKey("caishileixing")) {
            String searchType = (String) searchParams.get("caishileixing");
            String recipeType = (String) recipeMap.get("caishileixing");
            if (searchType != null && !searchType.trim().isEmpty()) {
                if (recipeType == null || !recipeType.equals(searchType)) {
                    return false;
                }
            }
        }
        
        // 检查烹饪方式（模糊匹配）
        if (searchParams.containsKey("pengrenfangshi")) {
            String searchMethod = (String) searchParams.get("pengrenfangshi");
            String recipeMethod = (String) recipeMap.get("pengrenfangshi");
            if (searchMethod != null && !searchMethod.trim().isEmpty()) {
                if (recipeMethod == null || !recipeMethod.toLowerCase().contains(searchMethod.toLowerCase())) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
