package com.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.service.ShoppingService;
import com.dto.ShoppingListRequest;
import com.dto.ShoppingListResponse;
import com.dto.ShoppingItemDTO;
import com.dao.UserShicaiDao;
import com.dao.CaipuxinxiDao;
import com.dao.YonghuDao;
import com.dao.ShicaiShelfLifeDao;
import com.entity.UserShicaiEntity;
import com.entity.CaipuxinxiEntity;
import com.entity.YonghuEntity;
import com.entity.ShicaiShelfLifeEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能采购清单服务实现
 * 
 * @author 
 * @date 2023-04-25
 */
@Service("shoppingService")
public class ShoppingServiceImpl implements ShoppingService {
    
    private static final Logger log = LoggerFactory.getLogger(ShoppingServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private UserShicaiDao userShicaiDao;
    
    @Autowired
    private CaipuxinxiDao caipuxinxiDao;
    
    @Autowired
    private YonghuDao yonghuDao;
    
    @Autowired
    private ShicaiShelfLifeDao shicaiShelfLifeDao;
    
    /**
     * 生成采购建议清单
     * 
     * 算法步骤：
     * 1. 读取用户现有食材库存（排除过期、已丢弃的）
     * 2. 分析目标食谱所需食材及用量，计算缺口食材
     * 3. 结合保质期参考库，推荐食材采购量
     * 4. 按食材分类分组排序
     * 5. 排除用户过敏原
     * 
     * @param request 采购清单请求参数
     * @return 采购清单响应
     */
    @Override
    public ShoppingListResponse generateShoppingList(ShoppingListRequest request) {
        long startTime = System.currentTimeMillis();
        
        log.info("开始生成采购清单 - 用户ID: {}, 食谱数: {}, 阈值: {}", 
            request.getUserId(), 
            request.getRecipeIds() != null ? request.getRecipeIds().size() : 0,
            request.getThreshold());
        
        try {
            // 步骤1: 读取用户现有食材库存（排除过期、已丢弃的）
            List<UserShicaiEntity> userIngredients = userShicaiDao.selectValidIngredientsByUserId(request.getUserId());
            log.debug("用户 {} 拥有 {} 种有效食材", request.getUserId(), userIngredients.size());
            
            // 构建用户食材库存Map: key=食材名称(小写), value=库存实体
            Map<String, UserShicaiEntity> userStockMap = userIngredients.stream()
                .collect(Collectors.toMap(
                    entity -> entity.getShicaiName().trim().toLowerCase(),
                    entity -> entity,
                    (existing, replacement) -> existing // 如果有重复，保留第一个
                ));
            
            // 步骤2: 获取用户过敏信息
            YonghuEntity user = yonghuDao.selectById(request.getUserId());
            Set<String> allergySet = new HashSet<>();
            if (user != null && user.getAllergyInfo() != null && !user.getAllergyInfo().trim().isEmpty()) {
                allergySet = Arrays.stream(user.getAllergyInfo().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());
                log.debug("用户 {} 有 {} 种过敏原", request.getUserId(), allergySet.size());
            }
            
            // 步骤3: 分析目标食谱所需食材
            Map<String, RecipeIngredientInfo> requiredIngredientsMap = new HashMap<>();
            List<String> targetRecipeNames = new ArrayList<>();
            
            if (request.getRecipeIds() != null && !request.getRecipeIds().isEmpty()) {
                for (Long recipeId : request.getRecipeIds()) {
                    CaipuxinxiEntity recipe = caipuxinxiDao.selectById(recipeId);
                    if (recipe == null) {
                        log.warn("食谱不存在 - ID: {}", recipeId);
                        continue;
                    }
                    
                    targetRecipeNames.add(recipe.getCaipumingcheng());
                    
                    // 解析食谱食材
                    String cailiaoStr = recipe.getCailiao();
                    if (cailiaoStr == null || cailiaoStr.trim().isEmpty()) {
                        continue;
                    }
                    
                    List<String> ingredients = Arrays.stream(cailiaoStr.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                    
                    // 累计每个食材的需求
                    for (String ingredient : ingredients) {
                        String ingredientKey = ingredient.toLowerCase();
                        
                        // 检查是否为过敏原
                        boolean isAllergen = allergySet.stream()
                            .anyMatch(allergen -> ingredientKey.contains(allergen));
                        
                        if (isAllergen) {
                            log.debug("跳过过敏原食材: {}", ingredient);
                            continue;
                        }
                        
                        RecipeIngredientInfo info = requiredIngredientsMap.getOrDefault(ingredientKey, 
                            new RecipeIngredientInfo(ingredient));
                        info.addRecipe(recipe.getCaipumingcheng());
                        info.incrementQuantity(1.0); // 默认每个食谱需要1份
                        requiredIngredientsMap.put(ingredientKey, info);
                    }
                }
            }
            
            log.debug("分析完成，共需要 {} 种食材", requiredIngredientsMap.size());
            
            // 步骤4: 计算缺口食材并生成采购清单
            List<ShoppingItemDTO> shoppingItems = new ArrayList<>();
            
            for (Map.Entry<String, RecipeIngredientInfo> entry : requiredIngredientsMap.entrySet()) {
                String ingredientKey = entry.getKey();
                RecipeIngredientInfo info = entry.getValue();
                
                // 获取当前库存
                UserShicaiEntity stock = userStockMap.get(ingredientKey);
                double currentStock = 0.0;
                String unit = "份";
                
                if (stock != null) {
                    currentStock = stock.getQuantity() != null ? stock.getQuantity().doubleValue() : 0.0;
                    unit = stock.getUnit() != null ? stock.getUnit() : "份";
                }
                
                // 计算需求量
                double requiredQuantity = info.getTotalQuantity();
                
                // 应用安全库存系数
                if (request.getIncludeSafetyStock()) {
                    requiredQuantity *= request.getSafetyStockFactor();
                }
                
                // 计算缺口量
                double gapQuantity = requiredQuantity - currentStock;
                
                // 判断是否需要采购
                boolean needToBuy = false;
                if (currentStock == 0) {
                    // 完全没有库存，必须采购
                    needToBuy = true;
                } else if (gapQuantity > 0) {
                    // 库存不足，需要补充
                    needToBuy = true;
                } else {
                    // 检查库存是否低于阈值
                    double stockRatio = currentStock / requiredQuantity;
                    if (stockRatio < request.getThreshold()) {
                        needToBuy = true;
                        gapQuantity = requiredQuantity * (1 - request.getThreshold()) - currentStock;
                    }
                }
                
                if (!needToBuy) {
                    log.debug("食材 {} 库存充足，无需采购", info.getIngredientName());
                    continue;
                }
                
                // 创建采购项
                ShoppingItemDTO item = new ShoppingItemDTO();
                item.setShicaiName(info.getIngredientName());
                item.setCurrentStock(currentStock);
                item.setGapQuantity(Math.max(0, gapQuantity));
                item.setSuggestedQuantity(Math.ceil(Math.max(0, gapQuantity))); // 向上取整
                item.setUnit(unit);
                item.setUsedInRecipes(String.join(", ", info.getRecipeNames()));
                
                // 设置分类（简单分类逻辑，可根据实际需求扩展）
                item.setCategory(categorizeIngredient(info.getIngredientName()));
                
                // 设置优先级
                if (currentStock == 0) {
                    item.setPriority(1); // 高优先级：完全没有库存
                    item.setMustBuy(true);
                } else if (gapQuantity > requiredQuantity * 0.5) {
                    item.setPriority(2); // 中优先级：缺口较大
                    item.setMustBuy(true);
                } else {
                    item.setPriority(3); // 低优先级：缺口较小
                    item.setMustBuy(false);
                }
                
                // 获取保质期信息（这里简化处理，实际应该有食材ID映射）
                // 由于没有食材ID，这里使用默认值
                item.setShelfLifeDays(7); // 默认7天
                item.setStorageMethod("常温");
                item.setShelfLifeTip(generateShelfLifeTip(7));
                
                shoppingItems.add(item);
            }
            
            log.debug("生成了 {} 个采购项", shoppingItems.size());
            
            // 步骤5: 按分类分组
            Map<String, List<ShoppingItemDTO>> itemsByCategory = shoppingItems.stream()
                .collect(Collectors.groupingBy(
                    ShoppingItemDTO::getCategory,
                    LinkedHashMap::new,
                    Collectors.toList()
                ));
            
            // 步骤6: 排序（按优先级和分类）
            shoppingItems.sort(Comparator
                .comparing(ShoppingItemDTO::getPriority)
                .thenComparing(ShoppingItemDTO::getCategory)
                .thenComparing(ShoppingItemDTO::getShicaiName));
            
            // 构建响应
            ShoppingListResponse response = new ShoppingListResponse();
            response.setUserId(request.getUserId());
            response.setItems(shoppingItems);
            response.setItemsByCategory(itemsByCategory);
            response.setTotalItems(shoppingItems.size());
            response.setMustBuyItems((int) shoppingItems.stream().filter(ShoppingItemDTO::getMustBuy).count());
            response.setOptionalItems(shoppingItems.size() - response.getMustBuyItems());
            response.setTargetRecipes(targetRecipeNames);
            response.setGenerateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            
            long endTime = System.currentTimeMillis();
            log.info("采购清单生成完成 - 用户ID: {}, 采购项数: {}, 耗时: {}ms", 
                request.getUserId(), shoppingItems.size(), (endTime - startTime));
            
            return response;
            
        } catch (Exception e) {
            log.error("生成采购清单时发生异常 - 用户ID: {}", request.getUserId(), e);
            throw new RuntimeException("生成采购清单失败", e);
        }
    }
    
    /**
     * 导出采购清单为JSON格式
     * 
     * @param userId 用户ID
     * @param listId 清单ID（可选，暂未使用）
     * @return JSON字符串
     */
    @Override
    public String exportToJson(Long userId, Long listId) {
        log.info("导出采购清单为JSON - 用户ID: {}, 清单ID: {}", userId, listId);
        
        try {
            // 生成默认采购清单
            ShoppingListRequest request = new ShoppingListRequest();
            request.setUserId(userId);
            
            ShoppingListResponse response = generateShoppingList(request);
            
            // 转换为JSON
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            
            log.info("采购清单导出成功 - 用户ID: {}", userId);
            return json;
            
        } catch (Exception e) {
            log.error("导出采购清单时发生异常 - 用户ID: {}", userId, e);
            throw new RuntimeException("导出采购清单失败", e);
        }
    }
    
    /**
     * 食材分类（简单分类逻辑）
     * 
     * @param ingredientName 食材名称
     * @return 分类名称
     */
    private String categorizeIngredient(String ingredientName) {
        String name = ingredientName.toLowerCase();
        
        // 蔬菜类
        if (name.contains("菜") || name.contains("瓜") || name.contains("豆") || 
            name.contains("菇") || name.contains("笋") || name.contains("芽") ||
            name.contains("西红柿") || name.contains("番茄") || name.contains("土豆") ||
            name.contains("萝卜") || name.contains("白菜") || name.contains("青菜")) {
            return "蔬菜类";
        }
        
        // 肉类
        if (name.contains("肉") || name.contains("鸡") || name.contains("鸭") || 
            name.contains("鱼") || name.contains("虾") || name.contains("蟹") ||
            name.contains("猪") || name.contains("牛") || name.contains("羊")) {
            return "肉类";
        }
        
        // 调料类
        if (name.contains("油") || name.contains("盐") || name.contains("酱") || 
            name.contains("醋") || name.contains("糖") || name.contains("料") ||
            name.contains("葱") || name.contains("姜") || name.contains("蒜")) {
            return "调料类";
        }
        
        // 主食类
        if (name.contains("米") || name.contains("面") || name.contains("粉") || 
            name.contains("饼") || name.contains("馒头") || name.contains("包子")) {
            return "主食类";
        }
        
        // 蛋奶类
        if (name.contains("蛋") || name.contains("奶") || name.contains("酸奶") || 
            name.contains("奶酪") || name.contains("黄油")) {
            return "蛋奶类";
        }
        
        // 默认分类
        return "其他";
    }
    
    /**
     * 生成保质期提示
     * 
     * @param shelfLifeDays 保质期天数
     * @return 保质期提示文本
     */
    private String generateShelfLifeTip(Integer shelfLifeDays) {
        if (shelfLifeDays == null || shelfLifeDays <= 0) {
            return "请尽快食用";
        }
        
        if (shelfLifeDays <= 3) {
            return "保质期较短，建议3天内食用";
        } else if (shelfLifeDays <= 7) {
            return "保质期约1周，请注意保存";
        } else if (shelfLifeDays <= 30) {
            return "保质期约1个月，可适量采购";
        } else {
            return "保质期较长，可放心采购";
        }
    }
    
    /**
     * 食谱食材信息内部类
     * 用于累计每个食材在多个食谱中的需求
     */
    private static class RecipeIngredientInfo {
        private String ingredientName;
        private double totalQuantity;
        private Set<String> recipeNames;
        
        public RecipeIngredientInfo(String ingredientName) {
            this.ingredientName = ingredientName;
            this.totalQuantity = 0.0;
            this.recipeNames = new HashSet<>();
        }
        
        public void addRecipe(String recipeName) {
            this.recipeNames.add(recipeName);
        }
        
        public void incrementQuantity(double quantity) {
            this.totalQuantity += quantity;
        }
        
        public String getIngredientName() {
            return ingredientName;
        }
        
        public double getTotalQuantity() {
            return totalQuantity;
        }
        
        public Set<String> getRecipeNames() {
            return recipeNames;
        }
    }
}
