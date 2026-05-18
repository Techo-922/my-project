# 需求文档：过期提醒与保质期参考库模块

## 简介

本模块为智能菜谱推荐系统提供食材过期提醒和保质期参考功能。系统需要自动监控用户食材库中的食材过期情况，并通过定时任务提前提醒用户；同时提供标准的食材保质期参考库，帮助用户在添加食材时设置合理的过期日期。

## 术语表

- **System**: 智能菜谱推荐系统
- **ExpiryReminderController**: 过期提醒控制器
- **ExpiryReminderService**: 过期提醒服务层
- **ExpiryReminderDao**: 过期提醒数据访问层
- **ShicaiShelfLifeController**: 保质期参考库控制器
- **ShicaiShelfLifeService**: 保质期参考库服务层
- **ShicaiShelfLifeDao**: 保质期参考库数据访问层
- **ScheduledTask**: 定时任务
- **UserShicai**: 用户食材
- **ExpiryReminder**: 过期提醒记录
- **ShicaiShelfLife**: 食材保质期参考信息
- **ReminderStatus**: 提醒状态（pending-待发送, sent-已发送, read-已读）
- **ShicaiStatus**: 食材状态（new-新鲜, used-已使用, expired-已过期, discarded-已丢弃）

## 需求

### 需求 1：过期提醒查询

**用户故事：** 作为用户，我想查看我的食材过期提醒列表，以便及时了解哪些食材即将过期或已过期。

#### 验收标准

1. WHEN 用户请求查询过期提醒列表 THEN THE System SHALL 返回该用户的所有过期提醒记录
2. WHEN 用户请求分页查询过期提醒 THEN THE System SHALL 支持分页参数并返回分页结果
3. WHEN 用户请求按状态筛选提醒 THEN THE System SHALL 根据提醒状态（pending、sent、read）进行过滤
4. WHEN 用户请求查询单条提醒详情 THEN THE System SHALL 返回该提醒的完整信息包括关联的食材信息
5. WHEN 查询提醒列表时 THEN THE System SHALL 按提醒日期倒序排列结果

### 需求 2：过期提醒状态管理

**用户故事：** 作为用户，我想标记提醒为已读状态，以便管理我已经处理的提醒。

#### 验收标准

1. WHEN 用户标记提醒为已读 THEN THE System SHALL 将该提醒状态更新为"read"
2. WHEN 用户删除提醒记录 THEN THE System SHALL 从数据库中删除该提醒
3. WHEN 系统发送提醒后 THEN THE System SHALL 自动将提醒状态从"pending"更新为"sent"
4. WHEN 更新提醒状态时 THEN THE System SHALL 验证提醒记录属于当前用户
5. WHEN 提醒状态为"read"时 THEN THE System SHALL 不再在未读提醒列表中显示该记录

### 需求 3：定时任务自动检查过期食材

**用户故事：** 作为系统管理员，我希望系统能自动检查即将过期的食材并创建提醒，以便用户及时收到通知。

#### 验收标准

1. WHEN 定时任务执行时 THEN THE System SHALL 扫描所有状态为"new"的用户食材
2. WHEN 食材距离过期日期小于等于3天 THEN THE System SHALL 创建一条状态为"pending"的过期提醒
3. WHEN 食材已经过期 THEN THE System SHALL 创建紧急提醒并将食材状态更新为"expired"
4. WHEN 创建提醒时 THEN THE System SHALL 检查是否已存在相同食材的未读提醒，避免重复创建
5. WHEN 定时任务执行时 THEN THE System SHALL 每天凌晨12点（午夜0点）自动运行一次
6. WHEN 定时任务执行失败时 THEN THE System SHALL 记录错误日志但不影响系统其他功能

### 需求 4：保质期参考库查询

**用户故事：** 作为用户，我想查询食材的标准保质期信息，以便在添加食材时设置合理的过期日期。

#### 验收标准

1. WHEN 用户请求查询保质期参考库 THEN THE System SHALL 返回所有食材的保质期参考信息
2. WHEN 用户按食材名称搜索 THEN THE System SHALL 返回匹配的保质期参考记录
3. WHEN 用户请求分页查询 THEN THE System SHALL 支持分页参数并返回分页结果
4. WHEN 用户根据食材ID查询 THEN THE System SHALL 返回该食材的保质期天数和存储方式
5. WHEN 查询结果为空时 THEN THE System SHALL 返回空列表而不是错误

### 需求 5：保质期参考库管理

**用户故事：** 作为管理员，我想管理保质期参考库的数据，以便维护准确的食材保质期信息。

#### 验收标准

1. WHEN 管理员添加新的保质期参考 THEN THE System SHALL 验证食材ID、保质期天数和存储方式不为空
2. WHEN 管理员更新保质期参考 THEN THE System SHALL 更新指定记录的保质期天数和存储方式
3. WHEN 管理员删除保质期参考 THEN THE System SHALL 从数据库中删除该记录
4. WHEN 添加保质期参考时 THEN THE System SHALL 检查该食材ID是否已存在参考信息
5. WHEN 保质期天数小于等于0时 THEN THE System SHALL 拒绝保存并返回错误信息

### 需求 6：过期提醒与用户食材关联

**用户故事：** 作为用户，我想在查看提醒时能看到关联的食材详细信息，以便快速了解是哪个食材需要处理。

#### 验收标准

1. WHEN 查询提醒详情时 THEN THE System SHALL 同时返回关联的用户食材信息（名称、数量、单位、过期日期）
2. WHEN 用户食材被删除时 THEN THE System SHALL 同时删除关联的所有提醒记录
3. WHEN 用户食材状态变为"used"或"discarded"时 THEN THE System SHALL 自动删除该食材的待处理提醒
4. WHEN 用户更新食材过期日期时 THEN THE System SHALL 重新计算并更新相关提醒的提醒日期

### 需求 7：API接口规范

**用户故事：** 作为前端开发者，我需要清晰的API接口规范，以便正确调用后端服务。

#### 验收标准

1. THE System SHALL 提供RESTful风格的API接口
2. THE System SHALL 在所有接口响应中使用统一的R对象封装返回结果
3. WHEN 接口调用成功时 THEN THE System SHALL 返回HTTP状态码200和成功标识
4. WHEN 接口调用失败时 THEN THE System SHALL 返回明确的错误信息和错误码
5. THE System SHALL 在需要用户认证的接口上添加@LoginUser注解进行权限验证
6. THE System SHALL 支持跨域请求（CORS）

### 需求 8：数据持久化

**用户故事：** 作为系统架构师，我需要确保数据正确持久化到数据库，以便保证数据的完整性和一致性。

#### 验收标准

1. THE System SHALL 使用MyBatis-Plus作为ORM框架进行数据库操作
2. WHEN 保存数据时 THEN THE System SHALL 自动设置addtime字段为当前时间
3. WHEN 执行数据库操作失败时 THEN THE System SHALL 抛出明确的异常信息
4. THE System SHALL 在Mapper XML文件中定义复杂的SQL查询语句
5. THE System SHALL 使用事务管理确保数据操作的原子性

### 需求 9：定时任务配置

**用户故事：** 作为系统管理员，我需要能够配置定时任务的执行时间，以便根据实际需求调整检查频率。

#### 验收标准

1. THE System SHALL 使用Spring的@Scheduled注解实现定时任务
2. THE System SHALL 支持通过配置文件修改定时任务的执行时间
3. WHEN 定时任务执行时 THEN THE System SHALL 记录执行开始和结束的日志
4. WHEN 定时任务执行时间过长时 THEN THE System SHALL 记录警告日志
5. THE System SHALL 支持手动触发定时任务用于测试和紧急情况

### 需求 10：性能优化

**用户故事：** 作为系统架构师，我需要确保系统在大量数据情况下仍能高效运行，以便提供良好的用户体验。

#### 验收标准

1. WHEN 定时任务扫描食材时 THEN THE System SHALL 使用批量查询减少数据库访问次数
2. WHEN 创建提醒时 THEN THE System SHALL 使用批量插入提高写入效率
3. WHEN 查询提醒列表时 THEN THE System SHALL 使用索引优化查询性能
4. WHEN 数据量超过1000条时 THEN THE System SHALL 使用分页查询避免内存溢出
5. THE System SHALL 对频繁查询的保质期参考数据进行缓存
