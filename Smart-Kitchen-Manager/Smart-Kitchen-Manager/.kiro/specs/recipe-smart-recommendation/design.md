# 设计文档

## 概述

本文档描述了食谱智能推荐功能的技术设计。该功能基于用户当前的食材库存，计算食谱与用户食材的匹配度，并过滤包含过敏原的食谱，为用户提供个性化的食谱推荐。

系统采用Spring Boot + MyBatis Plus架构，通过优化的SQL查询和应用层计算相结合的方式，实现高效的推荐算法。

## 架构

### 系统分层

```
Controller层 (CaipuxinxiController)
    ↓
Service层 (CaipuxinxiService)
    ↓
Dao层 (CaipuxinxiDao)
    ↓
数据库 (MySQL)
```

### 核心组件

1. **推荐控制器 (RecommendationController)**
   - 处理HTTP请求
   - 参数验证
   - 响应格式化

2. **推荐服务 (RecommendationService)**
   - 食材匹配算法
   - 匹配度计算
   - 过敏原过滤
   - 结果排序

3. **数据访问层 (CaipuxinxiDao, UserShicaiDao, YonghuDao)**
   - 优化的SQL查询
   - 数据聚合

## 组件和接口

### 1. 推荐响应DTO

```java
public class RecipeRecommendationDTO {
    private Long id;                          // 食谱ID
    private String caipumingcheng;            // 食谱名称
    private String caipufengmian;             // 食谱封面
    private String caishileixing;             // 菜式类型
    private String pengrenfangshi;            // 烹饪方式
    private Integer fenshu;                   // 分数
    private String zhizuoliucheng;            // 制作流程
    private Double matchRate;                 // 匹配度（0-100）
    private Boolean highPriority;             // 是否高优先级（≥60%）
    private List<String> requiredIngredients; // 所需食材列表
    private List<String> missingIngredients;  // 缺失食材列表
    private Integer missingCount;             // 缺失食材数量
}
```

### 2. Controller接口

```java
@RestController
@RequestMapping("/caipuxinxi")
public class CaipuxinxiController {
    
    /**
     * 智能推荐接口
     * GET /caipuxinxi/recommend
     * 
     * @param userId 用户ID（必传）
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @param sortType 排序类型（matchRate-匹配度, popularity-热度）
     * @return 推荐结果分页数据
     */
    @RequestMapping("/recommend")
    public R recommend(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "matchRate") String sortType
    );
}
```

### 3. Service接口

```java
public interface CaipuxinxiService {
    
    /**
     * 获取智能推荐列表
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param sortType 排序类型
     * @return 推荐结果分页数据
     */
    PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType);
}
```

### 4. Dao接口

```java
public interface CaipuxinxiDao {
    
    /**
     * 查询所有食谱及其所需食材
     * 使用LEFT JOIN避免N+1查询
     * 
     * @return 食谱列表（包含食材信息）
     */
    List<Map<String, Object>> selectRecipesWithIngredients();
}

public interface UserShicaiDao {
    
    /**
     * 查询用户有效食材
     * 
     * @param userid 用户ID
     * @return 用户食材列表
     */
    List<UserShicaiEntity> selectValidIngredientsByUserId(@Param("userid") Long userid);
}

public interface YonghuDao {
    
    /**
     * 查询用户过敏信息
     * 
     * @param id 用户ID
     * @return 用户实体
     */
    YonghuEntity selectById(Long id);
}
```

## 数据模型

### 数据库表关系

```
yonghu (用户表)
  ├─ id
  ├─ allergy_info (过敏信息)
  └─ health_preference (健康偏好)

user_shicai (用户食材库)
  ├─ id
  ├─ userid (外键 → yonghu.id)
  ├─ shicai_name (食材名称)
  ├─ quantity (数量)
  ├─ status (状态: new/used/expired/discarded)
  └─ expiry_date (过期日期)

caipuxinxi (食谱信息)
  ├─ id
  ├─ caipumingcheng (菜谱名称)
  ├─ cailiao (材料，逗号分隔)
  ├─ zhizuoliucheng (制作流程)
  ├─ fenshu (分数)
  └─ clicknum (点击次数)
```

### 食材数据格式

食谱的`cailiao`字段存储格式：
```
"鸡蛋,西红柿,食用油,盐,糖"
```

## 核心算法设计

### 推荐算法流程

```
1. 获取用户有效食材
   ↓
2. 获取用户过敏信息
   ↓
3. 获取所有食谱及食材
   ↓
4. 过滤含过敏原的食谱
   ↓
5. 计算每个食谱的匹配度
   ↓
6. 按匹配度或热度排序
   ↓
7. 分页返回结果
```

### 匹配度计算算法

```java
/**
 * 匹配度计算公式
 * 
 * matchRate = (ownedIngredientsCount / totalRequiredIngredientsCount) * 100
 * 
 * 其中:
 * - ownedIngredientsCount: 用户拥有的食材数量
 * - totalRequiredIngredientsCount: 食谱所需的总食材数量
 * 
 * 示例:
 * 食谱需要: [鸡蛋, 西红柿, 食用油, 盐, 糖] (5种)
 * 用户拥有: [鸡蛋, 西红柿, 盐] (3种)
 * 匹配度 = (3 / 5) * 100 = 60%
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
```

### 过敏原过滤算法

```java
/**
 * 过敏原过滤
 * 
 * 逻辑:
 * 1. 获取用户过敏信息（逗号分隔的字符串）
 * 2. 将过敏信息转换为Set（不区分大小写）
 * 3. 检查食谱食材是否包含任何过敏原
 * 4. 如果包含，排除该食谱
 */
private boolean containsAllergen(List<String> ingredients, Set<String> allergens) {
    if (allergens == null || allergens.isEmpty()) {
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
```

### 排序策略

```java
/**
 * 排序策略
 * 
 * 1. matchRate (默认): 按匹配度降序
 * 2. popularity: 按点击次数降序
 * 
 * 当匹配度相同时，使用食谱ID作为次要排序条件以保持一致性
 */
private void sortRecommendations(List<RecipeRecommendationDTO> recommendations, String sortType) {
    if ("popularity".equals(sortType)) {
        recommendations.sort(
            Comparator.comparing(RecipeRecommendationDTO::getFenshu).reversed()
                .thenComparing(RecipeRecommendationDTO::getId)
        );
    } else {
        // 默认按匹配度排序
        recommendations.sort(
            Comparator.comparing(RecipeRecommendationDTO::getMatchRate).reversed()
                .thenComparing(RecipeRecommendationDTO::getId)
        );
    }
}
```

## 正确性属性

*属性是一个特征或行为，应该在系统的所有有效执行中保持为真——本质上是关于系统应该做什么的形式化陈述。属性作为人类可读规范和机器可验证正确性保证之间的桥梁。*

### 属性 1: 有效食材过滤

*对于任何*用户ID，检索的食材列表应该只包含状态为'new'或'used'的食材，不包含'expired'或'discarded'状态的食材

**验证: 需求 1.1, 1.2**

### 属性 2: 匹配度计算正确性

*对于任何*食谱和用户食材集合，计算的匹配度应该等于（用户拥有的所需食材数量 / 食谱总食材数量）* 100，且结果在0到100之间

**验证: 需求 2.1, 2.4**

### 属性 3: 过敏原排除

*对于任何*包含用户过敏原的食谱，该食谱不应出现在推荐结果中

**验证: 需求 4.2**

### 属性 4: 匹配度排序

*对于任何*推荐结果列表（sortType=matchRate），列表中的每个食谱的匹配度应该大于或等于其后续食谱的匹配度

**验证: 需求 3.1**

### 属性 5: 高优先级标记

*对于任何*匹配度≥60%的食谱，highPriority字段应该为true；匹配度<60%的食谱，highPriority字段应该为false

**验证: 需求 3.2**

### 属性 6: 缺失食材计数

*对于任何*食谱，missingCount应该等于（食谱总食材数量 - 用户拥有的食材数量），且missingIngredients列表的大小应该等于missingCount

**验证: 需求 2.3, 3.3**

### 属性 7: 分页一致性

*对于任何*有效的pageNum和pageSize参数，返回的结果数量应该不超过pageSize，且总记录数应该与实际匹配的食谱数量一致

**验证: 需求 8.1, 8.2**

### 属性 8: 参数验证

*对于任何*缺失userId参数的请求，系统应该返回HTTP 400错误

**验证: 需求 5.2**

### 属性 9: 响应数据完整性

*对于任何*推荐结果中的食谱，应该包含id、caipumingcheng、matchRate、requiredIngredients、missingIngredients和zhizuoliucheng字段

**验证: 需求 6.1, 6.2, 6.3, 6.4, 6.5, 6.6**

### 属性 10: 过敏原匹配不区分大小写

*对于任何*用户过敏原和食材名称，过敏原匹配应该不区分大小写（例如："花生"应该匹配"花生"、"花生油"、"HUASHENG"）

**验证: 需求 4.4**

## 错误处理

### 错误场景

1. **用户ID缺失**
   - HTTP状态码: 400
   - 错误消息: "用户ID不能为空"

2. **用户不存在**
   - HTTP状态码: 404
   - 错误消息: "用户不存在"

3. **用户无有效食材**
   - HTTP状态码: 200
   - 返回空列表，提示消息: "您当前没有可用食材，请先添加食材"

4. **无匹配食谱**
   - HTTP状态码: 200
   - 返回空列表，提示消息: "暂无匹配的食谱推荐"

5. **数据库查询异常**
   - HTTP状态码: 500
   - 错误消息: "系统异常，请稍后重试"

### 异常处理策略

```java
try {
    // 业务逻辑
} catch (IllegalArgumentException e) {
    return R.error(400, e.getMessage());
} catch (EntityNotFoundException e) {
    return R.error(404, e.getMessage());
} catch (Exception e) {
    log.error("推荐系统异常", e);
    return R.error(500, "系统异常，请稍后重试");
}
```

## 测试策略

### 单元测试

单元测试用于验证特定示例和边缘情况：

1. **匹配度计算测试**
   - 测试完全匹配（100%）
   - 测试部分匹配（60%）
   - 测试无匹配（0%）
   - 测试空食材列表

2. **过敏原过滤测试**
   - 测试包含过敏原的食谱被排除
   - 测试不包含过敏原的食谱保留
   - 测试无过敏信息的用户
   - 测试大小写不敏感匹配

3. **排序测试**
   - 测试按匹配度排序
   - 测试按热度排序
   - 测试相同匹配度的稳定排序

4. **分页测试**
   - 测试第一页
   - 测试中间页
   - 测试最后一页
   - 测试超出范围的页码

### 属性测试

属性测试用于验证通用属性在所有输入下都成立：

每个属性测试应该运行至少100次迭代，使用随机生成的测试数据。

**测试框架**: JUnit 5 + QuickTheories (Java属性测试库)

**示例属性测试**:

```java
@Test
public void property_matchRateAlwaysBetween0And100() {
    qt()
        .forAll(
            lists().of(strings().allPossible()).ofSizeBetween(1, 20),  // 食谱食材
            sets().of(strings().allPossible()).ofSizeBetween(0, 30)    // 用户食材
        )
        .check((requiredIngredients, userIngredients) -> {
            Double matchRate = calculateMatchRate(requiredIngredients, userIngredients);
            return matchRate >= 0.0 && matchRate <= 100.0;
        });
}
```

### 集成测试

1. **端到端推荐流程测试**
   - 创建测试用户和食材数据
   - 调用推荐接口
   - 验证返回结果的正确性

2. **数据库查询性能测试**
   - 测试大量食谱数据下的查询性能
   - 验证无N+1查询问题

## 性能优化

### 数据库查询优化

1. **避免N+1查询**
   ```sql
   -- 不好的做法: 先查食谱，再循环查每个食谱的食材
   SELECT * FROM caipuxinxi;
   -- 然后对每个食谱执行:
   SELECT cailiao FROM caipuxinxi WHERE id = ?;
   
   -- 好的做法: 一次性查询所有数据
   SELECT id, caipumingcheng, cailiao, zhizuoliucheng, fenshu, clicknum
   FROM caipuxinxi;
   ```

2. **索引优化**
   ```sql
   -- 为常用查询字段添加索引
   CREATE INDEX idx_user_shicai_userid_status ON user_shicai(userid, status);
   CREATE INDEX idx_caipuxinxi_fenshu ON caipuxinxi(fenshu);
   ```

3. **查询结果缓存**
   - 用户食材列表可以缓存5分钟
   - 食谱列表可以缓存30分钟

### 应用层优化

1. **并行处理**
   - 用户食材查询和食谱查询可以并行执行

2. **早期过滤**
   - 先过滤过敏原，减少后续计算量

3. **懒加载**
   - 只在需要时才解析食材字符串

## 实现注意事项

1. **食材名称标准化**
   - 去除前后空格
   - 统一大小写（建议转小写比较）
   - 处理同义词（如"西红柿"和"番茄"）

2. **数据一致性**
   - 确保user_shicai表的shicai_name与caipuxinxi表的cailiao字段使用相同的命名规范

3. **并发安全**
   - 推荐计算是只读操作，无需加锁
   - 使用线程安全的数据结构

4. **日志记录**
   - 记录每次推荐请求的用户ID和结果数量
   - 记录异常情况便于排查

5. **监控指标**
   - 推荐接口响应时间
   - 推荐结果平均匹配度
   - 用户无匹配结果的比例
