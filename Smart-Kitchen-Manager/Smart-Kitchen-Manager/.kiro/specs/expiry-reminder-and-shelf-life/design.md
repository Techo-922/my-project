# 设计文档：过期提醒与保质期参考库模块

## 概述

本模块为智能菜谱推荐系统提供食材过期提醒和保质期参考功能。系统通过定时任务自动监控用户食材库中的食材过期情况，提前创建提醒通知用户；同时提供标准的食材保质期参考库，帮助用户在添加食材时设置合理的过期日期。

### 核心功能

1. **过期提醒管理**：查询、状态更新、删除提醒记录
2. **定时任务**：每天凌晨12点自动扫描即将过期的食材并创建提醒
3. **保质期参考库**：提供标准食材保质期信息的增删改查功能
4. **关联管理**：提醒与用户食材的级联操作

### 技术栈

- Spring Boot 2.x
- MyBatis-Plus 3.x
- Spring Scheduled（定时任务）
- MySQL 5.7+
- Java 8+

## 架构设计

### 系统架构


```
┌─────────────────────────────────────────────────────────────┐
│                        前端层                                │
│  (Vue.js - 管理后台 / 用户前端)                              │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP/REST API
┌────────────────────┴────────────────────────────────────────┐
│                     Controller 层                            │
│  ┌──────────────────────┐  ┌──────────────────────────┐    │
│  │ExpiryReminderController│  │ShicaiShelfLifeController│    │
│  └──────────┬───────────┘  └──────────┬───────────────┘    │
└─────────────┼──────────────────────────┼──────────────────┘
              │                          │
┌─────────────┴──────────────────────────┴──────────────────┐
│                      Service 层                             │
│  ┌──────────────────────┐  ┌──────────────────────────┐   │
│  │ExpiryReminderService │  │ShicaiShelfLifeService    │   │
│  │  - 业务逻辑处理       │  │  - 业务逻辑处理           │   │
│  │  - 数据验证           │  │  - 数据验证               │   │
│  └──────────┬───────────┘  └──────────┬───────────────┘   │
│             │                          │                    │
│  ┌──────────┴──────────────────────────┴───────────────┐  │
│  │         ExpiryReminderScheduledTask                   │  │
│  │         - 定时扫描过期食材                             │  │
│  │         - 创建提醒记录                                 │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────┼──────────────────────────┼──────────────────┘
              │                          │
┌─────────────┴──────────────────────────┴──────────────────┐
│                       DAO 层                                │
│  ┌──────────────────────┐  ┌──────────────────────────┐   │
│  │ExpiryReminderDao     │  │ShicaiShelfLifeDao        │   │
│  │  + MyBatis Mapper    │  │  + MyBatis Mapper        │   │
│  └──────────┬───────────┘  └──────────┬───────────────┘   │
└─────────────┼──────────────────────────┼──────────────────┘
              │                          │
┌─────────────┴──────────────────────────┴──────────────────┐
│                      数据库层                               │
│  ┌──────────────────────┐  ┌──────────────────────────┐   │
│  │expiry_reminder 表    │  │shicai_shelf_life 表      │   │
│  │user_shicai 表        │  │                          │   │
│  └──────────────────────┘  └──────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 分层职责

**Controller 层**：
- 接收HTTP请求，参数验证
- 调用Service层处理业务逻辑
- 封装响应结果（使用R对象）
- 权限验证（@LoginUser注解）

**Service 层**：
- 实现核心业务逻辑
- 数据验证和转换
- 事务管理
- 调用DAO层进行数据操作

**DAO 层**：
- 数据库CRUD操作
- 使用MyBatis-Plus简化开发
- 复杂查询使用XML配置

**定时任务**：
- 独立的调度组件
- 使用@Scheduled注解配置执行时间
- 扫描食材并创建提醒


## 组件和接口

### 1. ExpiryReminderController

过期提醒控制器，提供提醒管理的REST API接口。

#### 接口列表

**1.1 分页查询提醒列表**
```
GET /expiryreminder/page
参数：
  - page: 页码（默认1）
  - limit: 每页数量（默认10）
  - status: 提醒状态（可选：pending/sent/read）
  - userid: 用户ID（从session获取）
响应：R对象包含PageUtils
```

**1.2 查询提醒详情**
```
GET /expiryreminder/info/{id}
参数：
  - id: 提醒ID
响应：R对象包含ExpiryReminderEntity及关联的UserShicaiEntity
```

**1.3 标记提醒为已读**
```
POST /expiryreminder/markAsRead
参数：
  - id: 提醒ID
响应：R对象表示操作结果
```

**1.4 删除提醒**
```
POST /expiryreminder/delete
参数：
  - ids: 提醒ID数组
响应：R对象表示操作结果
```

**1.5 查询未读提醒数量**
```
GET /expiryreminder/unreadCount
参数：
  - userid: 用户ID（从session获取）
响应：R对象包含未读数量
```

**1.6 手动触发定时任务（测试用）**
```
POST /expiryreminder/triggerCheck
响应：R对象表示执行结果
```

### 2. ExpiryReminderService

过期提醒服务接口，定义业务逻辑方法。

#### 方法签名

```java
public interface ExpiryReminderService extends IService<ExpiryReminderEntity> {
    
    /**
     * 分页查询提醒列表
     */
    PageUtils queryPage(Map<String, Object> params, Wrapper<ExpiryReminderEntity> wrapper);
    
    /**
     * 查询提醒详情（包含关联的食材信息）
     */
    Map<String, Object> getReminderDetail(Long id);
    
    /**
     * 标记提醒为已读
     */
    boolean markAsRead(Long id, Long userid);
    
    /**
     * 查询未读提醒数量
     */
    int getUnreadCount(Long userid);
    
    /**
     * 为指定食材创建提醒
     */
    boolean createReminder(Long userid, Long userShicaiId, Date remindDate);
    
    /**
     * 删除指定食材的所有待处理提醒
     */
    void deletePendingRemindersByShicai(Long userShicaiId);
    
    /**
     * 更新食材关联的提醒日期
     */
    void updateReminderDateByShicai(Long userShicaiId, Date newExpiryDate);
}
```


### 3. ShicaiShelfLifeController

保质期参考库控制器，提供保质期信息管理的REST API接口。

#### 接口列表

**3.1 分页查询保质期参考库**
```
GET /shicaishelflife/page
参数：
  - page: 页码（默认1）
  - limit: 每页数量（默认10）
  - shicaiName: 食材名称（可选，模糊查询）
响应：R对象包含PageUtils
```

**3.2 根据食材ID查询保质期**
```
GET /shicaishelflife/getByShicaiId/{shicaiId}
参数：
  - shicaiId: 食材ID
响应：R对象包含ShicaiShelfLifeEntity
```

**3.3 查询详情**
```
GET /shicaishelflife/info/{id}
参数：
  - id: 记录ID
响应：R对象包含ShicaiShelfLifeEntity
```

**3.4 添加保质期参考**
```
POST /shicaishelflife/save
参数：ShicaiShelfLifeEntity JSON对象
  - shicaiId: 食材ID（必填）
  - shelfLifeDays: 保质期天数（必填，>0）
  - storageMethod: 存储方式（必填）
响应：R对象表示操作结果
```

**3.5 更新保质期参考**
```
POST /shicaishelflife/update
参数：ShicaiShelfLifeEntity JSON对象
  - id: 记录ID（必填）
  - shelfLifeDays: 保质期天数（可选）
  - storageMethod: 存储方式（可选）
响应：R对象表示操作结果
```

**3.6 删除保质期参考**
```
POST /shicaishelflife/delete
参数：
  - ids: 记录ID数组
响应：R对象表示操作结果
```

**3.7 查询所有保质期参考（不分页）**
```
GET /shicaishelflife/list
响应：R对象包含所有ShicaiShelfLifeEntity列表
```

### 4. ShicaiShelfLifeService

保质期参考库服务接口，定义业务逻辑方法。

#### 方法签名

```java
public interface ShicaiShelfLifeService extends IService<ShicaiShelfLifeEntity> {
    
    /**
     * 分页查询保质期参考库
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据食材ID查询保质期信息
     */
    ShicaiShelfLifeEntity getByShicaiId(Long shicaiId);
    
    /**
     * 添加保质期参考（带验证）
     */
    boolean saveWithValidation(ShicaiShelfLifeEntity entity);
    
    /**
     * 更新保质期参考（带验证）
     */
    boolean updateWithValidation(ShicaiShelfLifeEntity entity);
    
    /**
     * 验证保质期数据
     */
    boolean validateShelfLife(ShicaiShelfLifeEntity entity);
}
```


### 5. ExpiryReminderScheduledTask

定时任务组件，负责自动扫描即将过期的食材并创建提醒。

#### 核心方法

```java
@Component
public class ExpiryReminderScheduledTask {
    
    /**
     * 定时检查过期食材
     * 每天凌晨12点（午夜0点）执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiringShicai();
    
    /**
     * 手动触发检查（用于测试）
     */
    public void manualTrigger();
}
```

#### 执行逻辑

1. 记录任务开始日志
2. 查询所有状态为"new"的用户食材
3. 遍历食材列表：
   - 计算距离过期日期的天数
   - 如果已过期：创建紧急提醒，更新食材状态为"expired"
   - 如果3天内过期：检查是否已有未读提醒，没有则创建提醒
4. 批量插入提醒记录
5. 记录任务结束日志和统计信息
6. 异常处理：捕获异常，记录错误日志，不影响系统其他功能

## 数据模型

### ExpiryReminderEntity

过期提醒实体类（已存在）

```java
@TableName("expiry_reminder")
public class ExpiryReminderEntity {
    @TableId
    private Long id;                    // 主键ID
    private Long userid;                // 用户ID
    private Long userShicaiId;          // 用户食材ID
    private Date remindDate;            // 提醒日期
    private String status;              // 状态：pending/sent/read
    private Date addtime;               // 添加时间
}
```

**字段说明**：
- `status`：提醒状态
  - `pending`：待发送（刚创建）
  - `sent`：已发送（系统已推送）
  - `read`：已读（用户已查看）

### ShicaiShelfLifeEntity

食材保质期参考实体类（已存在）

```java
@TableName("shicai_shelf_life")
public class ShicaiShelfLifeEntity {
    @TableId
    private Long id;                    // 主键ID
    private Long shicaiId;              // 食材ID
    private Integer shelfLifeDays;      // 保质期天数
    private String storageMethod;       // 存储方式
    private Date addtime;               // 添加时间
}
```

**字段说明**：
- `shelfLifeDays`：标准保质期天数，必须大于0
- `storageMethod`：存储方式，如"冷藏"、"冷冻"、"常温"等

### UserShicaiEntity

用户食材实体类（已存在，关联使用）

```java
@TableName("user_shicai")
public class UserShicaiEntity {
    @TableId
    private Long id;                    // 主键ID
    private Long userid;                // 用户ID
    private String shicaiName;          // 食材名称
    private Integer quantity;           // 数量
    private String unit;                // 单位
    private Date purchaseDate;          // 购买日期
    private Date expiryDate;            // 过期日期
    private String status;              // 状态：new/used/expired/discarded
    private Date addtime;               // 添加时间
}
```


### 数据库表关系

```
┌─────────────────┐         ┌──────────────────┐
│  yonghu (用户)  │         │  user_shicai     │
│                 │         │  (用户食材)       │
│  - id           │◄────────│  - id            │
│  - zhanghao     │  1:N    │  - userid        │
│  - mima         │         │  - shicaiName    │
│  - ...          │         │  - expiryDate    │
└─────────────────┘         │  - status        │
                            └────────┬─────────┘
                                     │
                                     │ 1:N
                                     ▼
                            ┌──────────────────┐
                            │ expiry_reminder  │
                            │ (过期提醒)        │
                            │  - id            │
                            │  - userid        │
                            │  - userShicaiId  │
                            │  - remindDate    │
                            │  - status        │
                            └──────────────────┘

┌──────────────────────┐
│ shicai_shelf_life    │
│ (保质期参考库)        │
│  - id                │
│  - shicaiId          │
│  - shelfLifeDays     │
│  - storageMethod     │
└──────────────────────┘
```

**关系说明**：
1. 一个用户可以有多个食材（1:N）
2. 一个食材可以有多个提醒记录（1:N）
3. 保质期参考库独立存在，通过食材ID关联

## 正确性属性

*属性是一个特征或行为，应该在系统的所有有效执行中保持为真——本质上是关于系统应该做什么的正式声明。属性充当人类可读规范和机器可验证正确性保证之间的桥梁。*

### 属性1：用户提醒隔离

*对于任意*用户查询提醒列表的请求，返回的所有提醒记录都应该属于该用户。

**验证：需求 1.1**

### 属性2：分页数据一致性

*对于任意*有效的分页参数（page、limit），返回的数据量应该不超过limit，且分页信息（总数、总页数）应该正确反映实际数据。

**验证：需求 1.2, 4.3**

### 属性3：状态过滤正确性

*对于任意*提醒状态参数（pending/sent/read），查询返回的所有提醒记录的状态都应该匹配该参数。

**验证：需求 1.3**

### 属性4：提醒详情完整性

*对于任意*存在的提醒ID，查询详情应该返回提醒信息和关联的用户食材信息（名称、数量、单位、过期日期）。

**验证：需求 1.4, 6.1**

### 属性5：提醒列表排序

*对于任意*提醒列表查询，返回的列表应该按remindDate字段倒序排列。

**验证：需求 1.5**

### 属性6：已读状态更新

*对于任意*提醒记录，标记为已读后，该提醒的status字段应该变为"read"。

**验证：需求 2.1**

### 属性7：提醒删除完整性

*对于任意*提醒记录，删除操作后，该提醒应该不再存在于数据库中。

**验证：需求 2.2**

### 属性8：权限验证

*对于任意*用户尝试更新不属于自己的提醒，操作应该被拒绝。

**验证：需求 2.4**


### 属性9：未读提醒过滤

*对于任意*用户，查询未读提醒列表时，状态为"read"的提醒不应该出现在结果中。

**验证：需求 2.5**

### 属性10：定时任务扫描范围

*对于任意*定时任务执行，只有状态为"new"的用户食材应该被扫描。

**验证：需求 3.1**

### 属性11：即将过期提醒创建

*对于任意*距离过期日期小于等于3天的"new"状态食材，定时任务执行后应该存在一条状态为"pending"的提醒记录（如果之前不存在未读提醒）。

**验证：需求 3.2**

### 属性12：过期食材处理

*对于任意*已过期的"new"状态食材，定时任务执行后应该创建提醒记录，且食材状态应该更新为"expired"。

**验证：需求 3.3**

### 属性13：提醒去重

*对于任意*用户食材，如果已存在状态为"pending"或"sent"的提醒，定时任务不应该创建新的提醒。

**验证：需求 3.4**

### 属性14：定时任务异常隔离

*对于任意*定时任务执行异常，应该记录错误日志，且不影响系统其他功能的正常运行。

**验证：需求 3.6**

### 属性15：保质期查询完整性

*对于任意*保质期参考库查询请求，返回的所有记录都应该包含shicaiId、shelfLifeDays和storageMethod字段。

**验证：需求 4.1**

### 属性16：食材名称搜索

*对于任意*食材名称搜索参数，返回的所有保质期记录的食材名称都应该包含该搜索关键字。

**验证：需求 4.2**

### 属性17：食材ID查询唯一性

*对于任意*食材ID，查询保质期信息应该返回唯一的一条记录或空结果。

**验证：需求 4.4**

### 属性18：保质期数据验证

*对于任意*保质期参考添加请求，如果shicaiId、shelfLifeDays或storageMethod为空，或shelfLifeDays小于等于0，操作应该被拒绝。

**验证：需求 5.1, 5.5**

### 属性19：保质期更新一致性

*对于任意*保质期参考更新操作，更新后查询该记录应该返回更新后的数据。

**验证：需求 5.2**

### 属性20：保质期删除完整性

*对于任意*保质期参考记录，删除操作后，该记录应该不再存在于数据库中。

**验证：需求 5.3**

### 属性21：食材删除级联

*对于任意*用户食材，删除该食材后，所有关联的提醒记录都应该被删除。

**验证：需求 6.2**

### 属性22：食材状态联动

*对于任意*用户食材，当状态更新为"used"或"discarded"时，该食材的所有状态为"pending"或"sent"的提醒都应该被删除。

**验证：需求 6.3**

### 属性23：过期日期联动

*对于任意*用户食材，当过期日期更新时，关联的提醒记录的remindDate应该相应更新（过期日期前3天）。

**验证：需求 6.4**


### 属性24：响应格式统一性

*对于任意*API接口调用，返回的响应都应该是R对象格式，包含code、msg和data字段。

**验证：需求 7.2**

### 属性25：成功响应规范

*对于任意*成功的API调用，应该返回HTTP状态码200，且R对象的code为0（或成功标识）。

**验证：需求 7.3**

### 属性26：错误响应规范

*对于任意*失败的API调用，应该返回明确的错误信息和错误码。

**验证：需求 7.4**

### 属性27：自动时间戳

*对于任意*新增的提醒或保质期记录，addtime字段应该自动设置为当前时间。

**验证：需求 8.2**

### 属性28：数据库异常处理

*对于任意*数据库操作失败，应该抛出明确的异常信息。

**验证：需求 8.3**

### 属性29：事务原子性

*对于任意*包含多个数据库操作的事务，如果任一操作失败，所有操作都应该回滚。

**验证：需求 8.5**

### 属性30：定时任务日志记录

*对于任意*定时任务执行，应该记录执行开始和结束的日志。

**验证：需求 9.3**

### 属性31：大数据量分页

*对于任意*数据量超过1000条的查询，应该使用分页查询而不是一次性加载所有数据。

**验证：需求 10.4**

## 错误处理

### 异常类型

1. **参数验证异常**
   - 场景：必填参数为空、参数格式错误、参数值非法
   - 处理：返回400错误码和具体错误信息
   - 示例：保质期天数小于等于0

2. **权限验证异常**
   - 场景：用户尝试访问或修改不属于自己的数据
   - 处理：返回403错误码和权限不足提示
   - 示例：用户A尝试标记用户B的提醒为已读

3. **数据不存在异常**
   - 场景：查询或操作的数据不存在
   - 处理：返回404错误码和数据不存在提示
   - 示例：查询不存在的提醒ID

4. **数据库操作异常**
   - 场景：数据库连接失败、SQL执行错误、事务回滚
   - 处理：记录详细错误日志，返回500错误码和通用错误提示
   - 示例：数据库连接超时

5. **定时任务异常**
   - 场景：定时任务执行过程中发生错误
   - 处理：记录错误日志，不影响系统其他功能，下次继续执行
   - 示例：扫描食材时发生空指针异常

### 错误响应格式

```json
{
  "code": 400,
  "msg": "保质期天数必须大于0",
  "data": null
}
```

### 日志记录策略

- **INFO级别**：正常业务操作（查询、添加、更新、删除）
- **WARN级别**：业务警告（数据不存在、权限不足）
- **ERROR级别**：系统异常（数据库错误、定时任务失败）
- **DEBUG级别**：详细调试信息（SQL语句、参数值）


## 测试策略

### 单元测试

使用JUnit 5和Mockito进行单元测试，覆盖以下场景：

**Service层测试**：
1. 提醒查询逻辑测试
   - 测试分页查询
   - 测试状态过滤
   - 测试用户隔离
2. 提醒状态管理测试
   - 测试标记已读
   - 测试权限验证
   - 测试删除操作
3. 保质期参考库测试
   - 测试数据验证
   - 测试CRUD操作
   - 测试唯一性约束
4. 定时任务测试
   - 测试扫描逻辑
   - 测试提醒创建
   - 测试去重逻辑
   - 测试异常处理

**DAO层测试**：
1. 基础CRUD操作测试
2. 自定义查询方法测试
3. 关联查询测试

### 属性测试

使用JUnit-Quickcheck或jqwik进行属性测试，每个测试至少运行100次迭代。

**测试配置**：
```java
@Property(trials = 100)
```

**测试标签格式**：
```java
/**
 * Feature: expiry-reminder-and-shelf-life, Property 1: 用户提醒隔离
 * 验证：需求 1.1
 */
```

**核心属性测试**：
1. 属性1-9：提醒查询和状态管理
2. 属性10-14：定时任务逻辑
3. 属性15-20：保质期参考库
4. 属性21-23：级联操作
5. 属性24-31：系统规范

### 集成测试

使用Spring Boot Test进行集成测试：

1. **Controller层集成测试**
   - 使用MockMvc测试HTTP接口
   - 测试请求参数验证
   - 测试响应格式
   - 测试权限控制

2. **定时任务集成测试**
   - 测试手动触发功能
   - 测试完整的扫描和创建流程
   - 测试与数据库的交互

3. **事务测试**
   - 测试事务回滚
   - 测试级联操作的事务一致性

### 测试数据生成

**属性测试生成器**：
```java
// 生成随机用户ID
@ForAll @LongRange(min = 1, max = 10000) Long userid

// 生成随机提醒状态
@ForAll("reminderStatus") String status

@Provide
Arbitrary<String> reminderStatus() {
    return Arbitraries.of("pending", "sent", "read");
}

// 生成随机日期
@ForAll @DateRange(min = "2023-01-01", max = "2025-12-31") Date date

// 生成随机保质期天数
@ForAll @IntRange(min = 1, max = 365) Integer shelfLifeDays
```

### 测试覆盖率目标

- 代码行覆盖率：≥ 80%
- 分支覆盖率：≥ 70%
- 方法覆盖率：≥ 90%

### 性能测试

1. **定时任务性能测试**
   - 测试扫描10000条食材的执行时间
   - 目标：< 30秒

2. **查询性能测试**
   - 测试分页查询1000条数据的响应时间
   - 目标：< 500ms

3. **批量操作性能测试**
   - 测试批量创建100条提醒的执行时间
   - 目标：< 2秒

## 实现注意事项

### 1. 定时任务实现

```java
@Component
@Slf4j
public class ExpiryReminderScheduledTask {
    
    @Autowired
    private UserShicaiService userShicaiService;
    
    @Autowired
    private ExpiryReminderService expiryReminderService;
    
    /**
     * 每天凌晨12点执行
     * cron表达式：秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiringShicai() {
        long startTime = System.currentTimeMillis();
        log.info("开始执行过期食材检查任务");
        
        try {
            // 1. 查询所有状态为"new"的食材
            List<UserShicaiEntity> shicaiList = userShicaiService.selectList(
                new EntityWrapper<UserShicaiEntity>().eq("status", "new")
            );
            
            log.info("扫描到{}条待检查食材", shicaiList.size());
            
            // 2. 遍历处理
            int createdCount = 0;
            int expiredCount = 0;
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            Date threeDaysLater = calendar.getTime();
            
            for (UserShicaiEntity shicai : shicaiList) {
                Date expiryDate = shicai.getExpiryDate();
                if (expiryDate == null) continue;
                
                // 检查是否已过期
                if (expiryDate.before(now)) {
                    // 创建紧急提醒
                    expiryReminderService.createReminder(
                        shicai.getUserid(), 
                        shicai.getId(), 
                        now
                    );
                    // 更新食材状态
                    shicai.setStatus("expired");
                    userShicaiService.updateById(shicai);
                    expiredCount++;
                }
                // 检查是否3天内过期
                else if (expiryDate.before(threeDaysLater)) {
                    // 检查是否已有未读提醒
                    int unreadCount = expiryReminderService.selectCount(
                        new EntityWrapper<ExpiryReminderEntity>()
                            .eq("user_shicai_id", shicai.getId())
                            .in("status", Arrays.asList("pending", "sent"))
                    );
                    
                    if (unreadCount == 0) {
                        // 创建提醒（提醒日期为过期日期前3天）
                        calendar.setTime(expiryDate);
                        calendar.add(Calendar.DAY_OF_MONTH, -3);
                        expiryReminderService.createReminder(
                            shicai.getUserid(), 
                            shicai.getId(), 
                            calendar.getTime()
                        );
                        createdCount++;
                    }
                }
            }
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("过期食材检查任务完成，耗时{}ms，创建{}条提醒，标记{}条过期", 
                duration, createdCount, expiredCount);
            
            // 性能警告
            if (duration > 30000) {
                log.warn("定时任务执行时间过长：{}ms", duration);
            }
            
        } catch (Exception e) {
            log.error("过期食材检查任务执行失败", e);
            // 不抛出异常，确保不影响系统其他功能
        }
    }
    
    /**
     * 手动触发（用于测试）
     */
    public void manualTrigger() {
        log.info("手动触发过期食材检查任务");
        checkExpiringShicai();
    }
}
```


### 2. 级联操作实现

在UserShicaiController中添加级联删除逻辑：

```java
@RequestMapping("/delete")
public R delete(@RequestBody Long[] ids){
    // 删除食材前，先删除关联的提醒
    for (Long id : ids) {
        expiryReminderService.delete(
            new EntityWrapper<ExpiryReminderEntity>()
                .eq("user_shicai_id", id)
        );
    }
    
    userShicaiService.deleteBatchIds(Arrays.asList(ids));
    return R.ok();
}

@RequestMapping("/update")
@Transactional
public R update(@RequestBody UserShicaiEntity userShicai, HttpServletRequest request){
    UserShicaiEntity oldEntity = userShicaiService.selectById(userShicai.getId());
    
    // 如果状态变为used或discarded，删除待处理提醒
    if (("used".equals(userShicai.getStatus()) || "discarded".equals(userShicai.getStatus()))
        && !"used".equals(oldEntity.getStatus()) && !"discarded".equals(oldEntity.getStatus())) {
        expiryReminderService.deletePendingRemindersByShicai(userShicai.getId());
    }
    
    // 如果过期日期变化，更新提醒日期
    if (userShicai.getExpiryDate() != null 
        && !userShicai.getExpiryDate().equals(oldEntity.getExpiryDate())) {
        expiryReminderService.updateReminderDateByShicai(
            userShicai.getId(), 
            userShicai.getExpiryDate()
        );
    }
    
    userShicaiService.updateById(userShicai);
    return R.ok();
}
```

### 3. 权限验证实现

在ExpiryReminderService中实现权限验证：

```java
@Override
public boolean markAsRead(Long id, Long userid) {
    ExpiryReminderEntity reminder = this.selectById(id);
    
    if (reminder == null) {
        log.warn("提醒不存在 - ID: {}", id);
        return false;
    }
    
    // 验证提醒属于当前用户
    if (!reminder.getUserid().equals(userid)) {
        log.warn("权限验证失败 - 用户{}尝试访问用户{}的提醒{}", 
            userid, reminder.getUserid(), id);
        return false;
    }
    
    reminder.setStatus("read");
    return this.updateById(reminder);
}
```

### 4. 数据验证实现

在ShicaiShelfLifeService中实现数据验证：

```java
@Override
public boolean validateShelfLife(ShicaiShelfLifeEntity entity) {
    if (entity.getShicaiId() == null) {
        log.error("食材ID不能为空");
        return false;
    }
    
    if (entity.getShelfLifeDays() == null || entity.getShelfLifeDays() <= 0) {
        log.error("保质期天数必须大于0");
        return false;
    }
    
    if (StringUtils.isBlank(entity.getStorageMethod())) {
        log.error("存储方式不能为空");
        return false;
    }
    
    return true;
}

@Override
public boolean saveWithValidation(ShicaiShelfLifeEntity entity) {
    // 数据验证
    if (!validateShelfLife(entity)) {
        return false;
    }
    
    // 检查是否已存在
    ShicaiShelfLifeEntity existing = this.selectOne(
        new EntityWrapper<ShicaiShelfLifeEntity>()
            .eq("shicai_id", entity.getShicaiId())
    );
    
    if (existing != null) {
        log.warn("食材ID{}的保质期参考已存在", entity.getShicaiId());
        return false;
    }
    
    entity.setAddtime(new Date());
    return this.insert(entity);
}
```

### 5. MyBatis Mapper XML配置

ExpiryReminderDao.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dao.ExpiryReminderDao">

    <!-- 查询提醒详情（包含关联的食材信息） -->
    <select id="selectReminderDetail" resultType="map">
        SELECT 
            er.*,
            us.shicai_name,
            us.quantity,
            us.unit,
            us.expiry_date as shicai_expiry_date
        FROM expiry_reminder er
        LEFT JOIN user_shicai us ON er.user_shicai_id = us.id
        WHERE er.id = #{id}
    </select>
    
    <!-- 查询未读提醒数量 -->
    <select id="selectUnreadCount" resultType="int">
        SELECT COUNT(*)
        FROM expiry_reminder
        WHERE userid = #{userid}
        AND status IN ('pending', 'sent')
    </select>

</mapper>
```

ShicaiShelfLifeDao.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.dao.ShicaiShelfLifeDao">

    <!-- 根据食材ID查询保质期信息 -->
    <select id="selectByShicaiId" resultType="com.entity.ShicaiShelfLifeEntity">
        SELECT *
        FROM shicai_shelf_life
        WHERE shicai_id = #{shicaiId}
        LIMIT 1
    </select>

</mapper>
```

### 6. 配置文件

application.yml中添加定时任务配置：

```yaml
spring:
  task:
    scheduling:
      pool:
        size: 5
      thread-name-prefix: scheduled-task-
```

### 7. 索引优化

建议添加以下数据库索引：

```sql
-- expiry_reminder表索引
CREATE INDEX idx_userid ON expiry_reminder(userid);
CREATE INDEX idx_user_shicai_id ON expiry_reminder(user_shicai_id);
CREATE INDEX idx_status ON expiry_reminder(status);
CREATE INDEX idx_remind_date ON expiry_reminder(remind_date);

-- shicai_shelf_life表索引
CREATE INDEX idx_shicai_id ON shicai_shelf_life(shicai_id);

-- user_shicai表索引（如果不存在）
CREATE INDEX idx_status_expiry ON user_shicai(status, expiry_date);
```

## 部署和运维

### 部署清单

1. 数据库表创建（expiry_reminder、shicai_shelf_life）
2. 添加数据库索引
3. 部署后端服务
4. 验证定时任务配置
5. 初始化保质期参考数据

### 监控指标

1. **定时任务监控**
   - 执行频率：每天1次
   - 执行时长：< 30秒
   - 失败率：< 1%

2. **API性能监控**
   - 查询接口响应时间：< 500ms
   - 更新接口响应时间：< 200ms
   - 错误率：< 0.1%

3. **数据监控**
   - 提醒记录增长趋势
   - 未读提醒数量
   - 过期食材数量

### 运维建议

1. **定期清理**：定期清理已读且超过30天的提醒记录
2. **数据备份**：每天备份提醒和保质期数据
3. **日志归档**：定时任务日志保留30天
4. **性能优化**：根据监控数据调整索引和查询策略

## 总结

本设计文档详细描述了过期提醒与保质期参考库模块的架构、接口、数据模型、正确性属性和实现细节。通过定时任务自动监控食材过期情况，结合完善的提醒管理和保质期参考功能，为用户提供智能的食材管理体验。

核心特性：
- 自动化的过期检查和提醒创建
- 完善的权限验证和数据隔离
- 级联操作保证数据一致性
- 全面的属性测试保证系统正确性
- 性能优化和异常处理保证系统稳定性
