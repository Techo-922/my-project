-- 1. 创建数据库（适配MySQL 8.0默认字符集）
CREATE DATABASE IF NOT EXISTS `springbootct3p7` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_0900_ai_ci;

USE `springbootct3p7`;

-- 禁用外键检查，允许删除有外键约束的表
SET FOREIGN_KEY_CHECKS = 0;

-- 按依赖顺序删除所有表
DROP TABLE IF EXISTS `expiry_reminder`;
DROP TABLE IF EXISTS `discusscaipuxinxi`;
DROP TABLE IF EXISTS `pingfenxinxi`;
DROP TABLE IF EXISTS `user_shicai`;
DROP TABLE IF EXISTS `diet_statistics`;
DROP TABLE IF EXISTS `shicai_shelf_life`;
DROP TABLE IF EXISTS `storeup`;
DROP TABLE IF EXISTS `messages`;
DROP TABLE IF EXISTS `caipuxinxi`;
DROP TABLE IF EXISTS `caishileixing`;
DROP TABLE IF EXISTS `yonghu`;
DROP TABLE IF EXISTS `shicai`;
DROP TABLE IF EXISTS `aboutus`;
DROP TABLE IF EXISTS `config`;
DROP TABLE IF EXISTS `news`;
DROP TABLE IF EXISTS `systemintro`;
DROP TABLE IF EXISTS `token`;
DROP TABLE IF EXISTS `users`;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 重新创建 yonghu 表（包含 zhanghao 字段，与测试数据一致）
CREATE TABLE `yonghu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `zhanghao` varchar(200) NOT NULL COMMENT '账号（与 test_data.sql 匹配）',
  `mima` varchar(200) NOT NULL COMMENT '密码',
  `xingming` varchar(200) NOT NULL COMMENT '姓名',
  `xingbie` varchar(200) DEFAULT NULL COMMENT '性别',
  `youxiang` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `shoujihaoma` varchar(200) DEFAULT NULL COMMENT '手机号码',
  `touxiang` longtext COMMENT '头像',
  `health_preference` json COMMENT '健康偏好',
  `allergy_info` varchar(500) DEFAULT NULL COMMENT '过敏信息',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_zhanghao` (`zhanghao`) COMMENT '账号唯一（避免重复注册）'
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 插入用户测试数据（适配新表结构：包含 zhanghao、xingming 等字段）
LOCK TABLES `yonghu` WRITE;
INSERT INTO `yonghu` (`id`, `zhanghao`, `mima`, `xingming`, `xingbie`, `youxiang`, `shoujihaoma`, `touxiang`, `health_preference`, `allergy_info`, `addtime`) VALUES 
(11,'user11','123456','用户11','男','user11@qq.com','13823888881','upload/yonghu_avatar11.jpg','{"diet": "低糖", "goal": "减脂"}','花生','2023-04-25 00:11:24'),
(12,'user12','123456','用户12','男','user12@qq.com','13823888882','upload/yonghu_avatar12.jpg','{"diet": "素食", "goal": "养生"}','海鲜','2023-04-25 00:11:24'),
(13,'user13','123456','用户13','男','user13@qq.com','13823888883','upload/yonghu_avatar13.jpg','{"diet": "高蛋白", "goal": "增肌"}','无','2023-04-25 00:11:24'),
(14,'user14','123456','用户14','男','user14@qq.com','13823888884','upload/yonghu_avatar14.jpg','{"diet": "低脂", "goal": "护心"}','牛奶','2023-04-25 00:11:24'),
(15,'user15','123456','用户15','男','user15@qq.com','13823888885','upload/yonghu_avatar15.jpg','{"diet": "生酮", "goal": "控糖"}','麸质','2023-04-25 00:11:24'),
(16,'user16','123456','用户16','男','user16@qq.com','13823888886','upload/yonghu_avatar16.jpg','{"diet": "均衡", "goal": "保持"}','无','2023-04-25 00:11:24'),
(17,'user17','123456','用户17','男','user17@qq.com','13823888887','upload/yonghu_avatar17.jpg','{"diet": "纯素", "goal": "环保"}','大豆','2023-04-25 00:11:24'),
(18,'user18','123456','用户18','男','user18@qq.com','13823888888','upload/yonghu_avatar18.jpg','{"diet": "低碳", "goal": "健康"}','鸡蛋','2023-04-25 00:11:24');
UNLOCK TABLES;

-- 2. 食材基础表（新增，支撑食材库和保质期参考功能）
CREATE TABLE `shicai` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '食材主键ID',
  `shicai_name` varchar(200) NOT NULL COMMENT '食材名称',
  `category` varchar(100) DEFAULT NULL COMMENT '食材分类（蔬菜/肉类/粮油等）',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shicai_name` (`shicai_name`) COMMENT '食材名称唯一，避免重复'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='食材基础表';

-- 插入食材基础数据（40种常见食材）
LOCK TABLES `shicai` WRITE;
INSERT INTO `shicai` (`id`, `shicai_name`, `category`, `addtime`) VALUES
-- 蔬菜类 (1-8)
(1, '白菜', '蔬菜', '2023-04-25 08:11:24'),
(2, '生菜', '蔬菜', '2023-04-25 08:11:24'),
(3, '菠菜', '蔬菜', '2023-04-25 08:11:24'),
(4, '西兰花', '蔬菜', '2023-04-25 08:11:24'),
(5, '番茄', '蔬菜', '2023-04-25 08:11:24'),
(6, '土豆', '蔬菜', '2023-04-25 08:11:24'),
(7, '洋葱', '蔬菜', '2023-04-25 08:11:24'),
(8, '黄瓜', '蔬菜', '2023-04-25 08:11:24'),
-- 肉类 (9-14)
(9, '猪肉', '肉类', '2023-04-25 08:11:24'),
(10, '牛肉', '肉类', '2023-04-25 08:11:24'),
(11, '鸡肉', '肉类', '2023-04-25 08:11:24'),
(12, '羊肉', '肉类', '2023-04-25 08:11:24'),
(13, '猪排骨', '肉类', '2023-04-25 08:11:24'),
(14, '牛排', '肉类', '2023-04-25 08:11:24'),
-- 水产类 (15-18)
(15, '鲜鱼', '水产', '2023-04-25 08:11:24'),
(16, '虾', '水产', '2023-04-25 08:11:24'),
(17, '冻鱼', '水产', '2023-04-25 08:11:24'),
(18, '海鲜', '水产', '2023-04-25 08:11:24'),
-- 蛋奶类 (19-22)
(19, '鸡蛋', '蛋奶', '2023-04-25 08:11:24'),
(20, '牛奶', '蛋奶', '2023-04-25 08:11:24'),
(21, '酸奶', '蛋奶', '2023-04-25 08:11:24'),
(22, '奶酪', '蛋奶', '2023-04-25 08:11:24'),
-- 主食类 (23-26)
(23, '大米', '主食', '2023-04-25 08:11:24'),
(24, '面粉', '主食', '2023-04-25 08:11:24'),
(25, '面条', '主食', '2023-04-25 08:11:24'),
(26, '馒头', '主食', '2023-04-25 08:11:24'),
-- 调味品类 (27-30)
(27, '食用油', '调味品', '2023-04-25 08:11:24'),
(28, '酱油', '调味品', '2023-04-25 08:11:24'),
(29, '食盐', '调味品', '2023-04-25 08:11:24'),
(30, '醋', '调味品', '2023-04-25 08:11:24'),
-- 豆制品类 (31-34)
(31, '豆腐', '豆制品', '2023-04-25 08:11:24'),
(32, '豆干', '豆制品', '2023-04-25 08:11:24'),
(33, '黄豆', '豆制品', '2023-04-25 08:11:24'),
(34, '绿豆', '豆制品', '2023-04-25 08:11:24'),
-- 水果类 (35-40)
(35, '苹果', '水果', '2023-04-25 08:11:24'),
(36, '香蕉', '水果', '2023-04-25 08:11:24'),
(37, '草莓', '水果', '2023-04-25 08:11:24'),
(38, '橙子', '水果', '2023-04-25 08:11:24'),
(39, '葡萄', '水果', '2023-04-25 08:11:24'),
(40, '西瓜', '水果', '2023-04-25 08:11:24');
UNLOCK TABLES;

-- 3. 关于我们表
CREATE TABLE `aboutus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '副标题',
  `content` longtext NOT NULL COMMENT '内容',
  `picture1` longtext COMMENT '图片1',
  `picture2` longtext COMMENT '图片2',
  `picture3` longtext COMMENT '图片3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='关于我们';

-- 插入关于我们数据
LOCK TABLES `aboutus` WRITE;
INSERT INTO `aboutus` VALUES (1,'2023-04-25 00:11:24','关于我们','ABOUT US','不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?
你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。','upload/aboutus_picture1.jpg','upload/aboutus_picture2.jpg','upload/aboutus_picture3.jpg');
UNLOCK TABLES;

-- 4. 菜式类型表
CREATE TABLE `caishileixing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caishileixing` varchar(200) NOT NULL COMMENT '菜式类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_caishileixing` (`caishileixing`) COMMENT '菜式类型唯一'
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜式类型';

-- 插入菜式类型数据
LOCK TABLES `caishileixing` WRITE;
INSERT INTO `caishileixing` VALUES 
(21,'2023-04-25 00:11:24','菜式类型1'),
(22,'2023-04-25 00:11:24','菜式类型2'),
(23,'2023-04-25 00:11:24','菜式类型3'),
(24,'2023-04-25 00:11:24','菜式类型4'),
(25,'2023-04-25 00:11:24','菜式类型5'),
(26,'2023-04-25 00:11:24','菜式类型6'),
(27,'2023-04-25 00:11:24','菜式类型7'),
(28,'2023-04-25 00:11:24','菜式类型8');
UNLOCK TABLES;

-- 5. 菜谱信息表（优化索引，适配推荐功能）
CREATE TABLE `caipuxinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caipumingcheng` varchar(200) NOT NULL COMMENT '菜谱名称',
  `caipufengmian` longtext COMMENT '菜谱封面（多图用逗号分隔）',
  `caishileixing` varchar(200) NOT NULL COMMENT '菜式类型（关联caishileixing表）',
  `pengrenfangshi` varchar(200) DEFAULT NULL COMMENT '烹饪方式',
  `fenshu` int(11) NOT NULL COMMENT '分数',
  `cailiao` longtext COMMENT '材料（格式：食材ID:数量:单位;...）',
  `zhizuoliucheng` longtext COMMENT '制作流程',
  `faburiqi` date DEFAULT NULL COMMENT '发布日期',
  `clicktime` datetime DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int(11) DEFAULT '0' COMMENT '点击次数',
  PRIMARY KEY (`id`),
  KEY `idx_caishileixing_fenshu` (`caishileixing`, `fenshu`) COMMENT '菜式类型+评分，支撑高分菜谱筛选',
  KEY `idx_caipumingcheng` (`caipumingcheng`) COMMENT '菜谱名称搜索索引'
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜谱信息';

-- 插入菜谱信息数据
LOCK TABLES `caipuxinxi` WRITE;
INSERT INTO `caipuxinxi` VALUES 
(31,'2023-04-25 00:11:24','菜谱名称1','upload/caipuxinxi_caipufengmian1.jpg,upload/caipuxinxi_caipufengmian2.jpg,upload/caipuxinxi_caipufengmian3.jpg','菜式类型1','烹饪方式1',1,'1:500:克;2:200:克','制作流程1','2023-04-25','2023-04-25 08:11:24',1),
(32,'2023-04-25 00:11:24','菜谱名称2','upload/caipuxinxi_caipufengmian2.jpg,upload/caipuxinxi_caipufengmian3.jpg,upload/caipuxinxi_caipufengmian4.jpg','菜式类型2','烹饪方式2',2,'3:300:克;4:100:毫升','制作流程2','2023-04-25','2023-04-25 08:11:24',2),
(33,'2023-04-25 00:11:24','菜谱名称3','upload/caipuxinxi_caipufengmian3.jpg,upload/caipuxinxi_caipufengmian4.jpg,upload/caipuxinxi_caipufengmian5.jpg','菜式类型3','烹饪方式3',3,'5:2:个;6:100:克','制作流程3','2023-04-25','2023-04-25 08:11:24',3),
(34,'2023-04-25 00:11:24','菜谱名称4','upload/caipuxinxi_caipufengmian4.jpg,upload/caipuxinxi_caipufengmian5.jpg,upload/caipuxinxi_caipufengmian6.jpg','菜式类型4','烹饪方式4',4,'7:200:克;8:50:克','制作流程4','2023-04-25','2023-04-25 08:11:24',4),
(35,'2023-04-25 00:11:24','菜谱名称5','upload/caipuxinxi_caipufengmian5.jpg,upload/caipuxinxi_caipufengmian6.jpg,upload/caipuxinxi_caipufengmian7.jpg','菜式类型5','烹饪方式5',5,'9:300:毫升;10:150:克','制作流程5','2023-04-25','2023-04-25 08:11:24',5),
(36,'2023-04-25 00:11:24','菜谱名称6','upload/caipuxinxi_caipufengmian6.jpg,upload/caipuxinxi_caipufengmian7.jpg,upload/caipuxinxi_caipufengmian8.jpg','菜式类型6','烹饪方式6',6,'11:3:个;12:200:克','制作流程6','2023-04-25','2023-04-25 08:11:24',6),
(37,'2023-04-25 00:11:24','菜谱名称7','upload/caipuxinxi_caipufengmian7.jpg,upload/caipuxinxi_caipufengmian8.jpg,upload/caipuxinxi_caipufengmian9.jpg','菜式类型7','烹饪方式7',7,'13:400:克;14:100:克','制作流程7','2023-04-25','2023-04-25 08:11:24',7),
(38,'2023-04-25 00:11:24','菜谱名称8','upload/caipuxinxi_caipufengmian8.jpg,upload/caipuxinxi_caipufengmian9.jpg,upload/caipuxinxi_caipufengmian10.jpg','菜式类型8','烹饪方式8',8,'15:250:克;1:100:克','制作流程8','2023-04-25','2023-04-25 08:18:38',9);
UNLOCK TABLES;

-- 6. 配置文件表
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户ID（NULL表示系统全局配置）',
  `name` varchar(100) NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`),
  KEY `idx_userid_name` (`userid`, `name`) COMMENT '用户+配置名称索引，支撑个性化配置'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配置文件';

-- 插入配置数据
LOCK TABLES `config` WRITE;
INSERT INTO `config` VALUES 
(1,NULL,'picture1','upload/picture1.jpg'),
(2,NULL,'picture2','upload/picture2.jpg'),
(3,NULL,'picture3','upload/picture3.jpg');
UNLOCK TABLES;

-- 7. 菜谱评论表（补充外键，保障数据一致性）
CREATE TABLE `discusscaipuxinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联菜谱ID（caipuxinxi表）',
  `userid` bigint(20) NOT NULL COMMENT '用户ID（yonghu表）',
  `avatarurl` longtext COMMENT '用户头像',
  `nickname` varchar(200) DEFAULT NULL COMMENT '用户名',
  `content` longtext NOT NULL COMMENT '评论内容',
  `reply` longtext COMMENT '回复内容',
  PRIMARY KEY (`id`),
  KEY `fk_discuss_caipu_caipuxinxi` (`refid`),
  KEY `fk_discuss_caipu_yonghu` (`userid`),
  CONSTRAINT `fk_discuss_caipu_caipuxinxi` FOREIGN KEY (`refid`) REFERENCES `caipuxinxi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_discuss_caipu_yonghu` FOREIGN KEY (`userid`) REFERENCES `yonghu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜谱信息评论表';

-- 8. 留言信息表
CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '留言人ID（yonghu表）',
  `username` varchar(200) DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext COMMENT '头像',
  `content` longtext NOT NULL COMMENT '留言内容',
  `cpicture` longtext COMMENT '留言图片',
  `reply` longtext COMMENT '回复内容',
  `rpicture` longtext COMMENT '回复图片',
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`) COMMENT '按用户查询留言索引'
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='留言信息';

-- 插入留言数据
LOCK TABLES `messages` WRITE;
INSERT INTO `messages` VALUES 
(91,'2023-04-25 00:11:24',11,'姓名1','upload/messages_avatarurl1.jpg','留言内容1','upload/messages_cpicture1.jpg','回复内容1','upload/messages_rpicture1.jpg'),
(92,'2023-04-25 00:11:24',12,'姓名2','upload/messages_avatarurl2.jpg','留言内容2','upload/messages_cpicture2.jpg','回复内容2','upload/messages_rpicture2.jpg'),
(93,'2023-04-25 00:11:24',13,'姓名3','upload/messages_avatarurl3.jpg','留言内容3','upload/messages_cpicture3.jpg','回复内容3','upload/messages_rpicture3.jpg'),
(94,'2023-04-25 00:11:24',14,'姓名4','upload/messages_avatarurl4.jpg','留言内容4','upload/messages_cpicture4.jpg','回复内容4','upload/messages_rpicture4.jpg'),
(95,'2023-04-25 00:11:24',15,'姓名5','upload/messages_avatarurl5.jpg','留言内容5','upload/messages_cpicture5.jpg','回复内容5','upload/messages_rpicture5.jpg'),
(96,'2023-04-25 00:11:24',16,'姓名6','upload/messages_avatarurl6.jpg','留言内容6','upload/messages_cpicture6.jpg','回复内容6','upload/messages_rpicture6.jpg'),
(97,'2023-04-25 00:11:24',17,'姓名7','upload/messages_avatarurl7.jpg','留言内容7','upload/messages_cpicture7.jpg','回复内容7','upload/messages_rpicture7.jpg'),
(98,'2023-04-25 00:11:24',18,'姓名8','upload/messages_avatarurl8.jpg','留言内容8','upload/messages_cpicture8.jpg','回复内容8','upload/messages_rpicture8.jpg');
UNLOCK TABLES;

-- 9. 公告信息表
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `introduction` longtext COMMENT '简介',
  `picture` longtext NOT NULL COMMENT '图片',
  `content` longtext NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `idx_title` (`title`) COMMENT '公告标题搜索索引'
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告信息';

-- 插入公告数据
LOCK TABLES `news` WRITE;
INSERT INTO `news` VALUES 
(61,'2023-04-25 00:11:24','有梦想，就要努力去实现','不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。','upload/news_picture1.jpg','<p>不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?</p><p>你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。</p><p>真正有行动力的人不需要别人告诉他如何做，因为他已经在做了。就算碰到问题，他也会自己想办法，自己动手去解决或者主动寻求可以帮助他的人，而不是等着别人为自己解决问题。</p><p>首先要学习独立思考。花一点时间想一下自己喜欢什么，梦想是什么，不要别人说想环游世界，你就说你的梦想是环游世界。</p><p>很多人说现实束缚了自己，其实在这个世界上，我们一直都可以有很多选择，生活的决定权也—直都在自己手上，只是我们缺乏行动力而已。</p><p>如果你觉得安于现状是你想要的，那选择安于现状就会让你幸福和满足;如果你不甘平庸，选择一条改变、进取和奋斗的道路，在这个追求的过程中，你也一样会感到快乐。所谓的成功，即是按照自己想要的生活方式生活。最糟糕的状态，莫过于当你想要选择一条不甘平庸、改变、进取和奋斗的道路时，却以一种安于现状的方式生活，最后抱怨自己没有得到想要的人生。</p><p>因为喜欢，你不是在苦苦坚持，也因为喜欢，你愿意投入时间、精力，长久以往，获得成功就是自然而然的事情。</p>'),
(62,'2023-04-25 00:11:24','又是一年毕业季','又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。','upload/news_picture2.jpg','<p>又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。</p><p>过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。一直都是晚会的忠实参与者，无论是台前还是幕后，忽然间，角色转变，那种感觉确实难以用语言表达。</p><p>过去的三年，总是默默地期盼着这个好雨时节，因为这时候，会有灿烂的阳光，会有满目的百花争艳，会有香甜的冰激凌，这是个毕业的季节，当时不经世事的我们会殷切地期待学校那一大堆的活动，期待穿上绚丽的演出服或者礼仪服，站在大礼堂镁光灯下尽情挥洒我们的澎拜的激情。</p><p>百感交集，隔岸观火与身临其境的感觉竟是如此不同。从来没想过一场晚会送走的是我们自己的时候会是怎样的感情，毕业就真的意味着结束吗?倔强的我们不愿意承认，谢谢学弟学妹们慷慨的将这次的主题定为“我们在这里”。我知道，这可能是他们对我们这些过来人的尊敬和施舍。</p><p>没有为这场晚会排练、奔波，没有为班级、学生会、文学院出点力，还真有点不习惯，百般无奈中，用“工作忙”个万能的借口来搪塞自己，欺骗别人。其实自己心里明白，那只是在逃避，只是不愿面对繁华落幕后的萧条和落寞。大四了，大家各奔东西，想凑齐班上的人真的是难上加难，敏燕从越南回来，刚落地就匆匆回了学校，那么恋家的人也启程回来了，睿睿学姐也是从家赶来跟我们团圆。大家—如既往的寒暄、打趣、调侃对方，似乎一切又回到了当初的单纯美好。</p><p>看着舞台上活泼可爱的学弟学妹们，如同一群机灵的小精灵，清澈的眼神，稚嫩的肢体，轻快地步伐，用他们那热情洋溢的舞姿渲染着在场的每一个人，我知道，我不应该羡慕嫉妒他们，不应该顾自怜惜逝去的青春，不应该感叹夕阳无限好，曾经，我们也拥有过，曾经，我们也年轻过，曾经，我们也灿烂过。我深深地告诉自己，人生的每个阶段都是美的，年轻有年轻的活力，成熟也有成熟的魅力。多—份稳重、淡然、优雅，也是漫漫时光掠影遗留下的珍贵赏赐。</p>'),
(63,'2023-04-25 00:11:24','挫折路上，坚持常在心间','回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。','upload/news_picture3.jpg','<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。</p>'),
(64,'2023-04-25 00:11:24','挫折是另一个生命的开端','当遇到挫折或失败，你是看见失败还是看见机会?挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。','upload/news_picture4.jpg','<p>当遇到挫折或失败，你是看见失败还是看见机会?</p><p>挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。</p><p>人生在世，从古到今，不分天子平民，机遇虽有不同，但总不免有身陷困境或遭遇难题之处，这时候唯有通权达变，才能使人转危为安，甚至反败为胜。</p><p>大部分的人，一生当中，最痛苦的经验是失去所爱的人，其次是丢掉一份工作。其实，经得起考验的人，就算是被开除也不会惊慌，要学会面对。</p><p>“塞翁失马，焉知非福。”人生的道路，并不是每一步都迈向成功，这就是追求的意义。我们还要认识到一点，挫折作为一种情绪状态和一种个人体验，各人的耐受性是大不相同的，有的人经历了一次次挫折，就能够坚忍不拔，百折不挠;有的人稍遇挫折便意志消沉，一蹶不振。所以，挫折感是一种主观感受，因为人的目的和需要不同，成功标准不同，所以同一种活动对于不同的人可能会造成不同的挫折感受。</p><p>凡事皆以平常心来看待，对于生命顺逆不要太执著。能够“破我执”是很高层的人生境界。</p><p>人事的艰难就是一种考验。就像—支剑要有磨刀来磨，剑才会利:一块璞玉要有粗石来磨，才会发出耀眼的光芒。我们能够做到的，只是如何减少、避免那些由于自身的原因所造成的挫折，而在遇到痛苦和挫折之后，则力求化解痛苦，争取幸福。我们要知道，痛苦和挫折是双重性的，它既是我们人生中难以完全避免的，也是我们在争取成功时，不可缺少的一种动力。因为我认为，推动我们奋斗的力量，不仅仅是对成功的渴望，还有为摆脱痛苦和挫折而进行的奋斗。</p>'),
(65,'2023-04-25 00:11:24','你要去相信，没有到不了的明天','有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。','upload/news_picture5.jpg','<p>有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。</p><p>不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。</p><p>不管你现在是在图书馆里背着怎么也看不进去的英语单词，还是你现在迷茫地看不清未来的方向不知道要往哪走。</p><p>不管你现在是在努力着去实现梦想却没能拉近与梦想的距离，还是你已经慢慢地找不到自己的梦想了。</p><p>你都要去相信，没有到不了的明天。</p><p>有的时候你的梦想太大，别人说你的梦想根本不可能实现;有的时候你的梦想又太小，又有人说你胸无大志;有的时候你对死党说着将来要去环游世界的梦想，却换来他的不屑一顾，于是你再也不提自己的梦想;有的时候你突然说起将来要开个小店的愿望，却发现你讲述的那个人，并没有听到你在说什么。</p><p>不过又能怎么样呢，未来始终是自己的，梦想始终是自己的，没有人会来帮你实现它。</p><p>也许很多时候我们只是需要朋友的一句鼓励，一句安慰，却也得不到。但是相信我，世界上还有很多人，只是想要和你说说话。</p><p>因为我们都一样。一样的被人说成固执，一样的在追逐他们眼里根本不在意的东西。</p><p>所以，又有什么关系呢，别人始终不是你、不能懂你的心情，你又何必多去解释呢。这个世界会来阻止你，困难也会接踵而至，其实真正关键的只有自己，有没有那个倔强。</p><p>这个世界上没有不带伤的人，真正能治愈自己的，只有自己。</p>'),
(66,'2023-04-25 00:11:24','离开是一种痛苦，是一种勇气，但同样也是一个考验，是一个新的开端','无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。','upload/news_picture6.jpg','<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>'),
(67,'2023-04-25 00:11:24','Leave未必是一种痛苦','无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。','upload/news_picture7.jpg','<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>'),
(68,'2023-04-25 00:11:24','坚持才会成功','回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。','upload/news_picture8.jpg','<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。</p>');
UNLOCK TABLES;

-- 10. 评分信息表（优化关联字段，修复注释）
CREATE TABLE `pingfenxinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caipuid` bigint(20) NOT NULL COMMENT '菜谱ID（关联caipuxinxi表）',
  `caishileixing` varchar(200) DEFAULT NULL COMMENT '菜式类型',
  `fenshu` int(11) NOT NULL COMMENT '分数（1-10分）',
  `userid` bigint(20) NOT NULL COMMENT '用户ID（关联yonghu表）',
  `pingfenriqi` date DEFAULT NULL COMMENT '评分日期',
  PRIMARY KEY (`id`),
  KEY `idx_caipuid_userid` (`caipuid`, `userid`) COMMENT '菜谱+用户唯一索引，避免重复评分',
  CONSTRAINT `fk_pingfen_caipu` FOREIGN KEY (`caipuid`) REFERENCES `caipuxinxi` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_pingfen_yonghu` FOREIGN KEY (`userid`) REFERENCES `yonghu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评分信息';

-- 插入评分数据
LOCK TABLES `pingfenxinxi` WRITE;
INSERT INTO `pingfenxinxi` VALUES 
(41,'2023-04-25 00:11:24',31,'菜式类型1',1,11,'2023-04-25'),
(42,'2023-04-25 00:11:24',32,'菜式类型2',2,12,'2023-04-25'),
(43,'2023-04-25 00:11:24',33,'菜式类型3',3,13,'2023-04-25'),
(44,'2023-04-25 00:11:24',34,'菜式类型4',4,14,'2023-04-25'),
(45,'2023-04-25 00:11:24',35,'菜式类型5',5,15,'2023-04-25'),
(46,'2023-04-25 00:11:24',36,'菜式类型6',6,16,'2023-04-25'),
(47,'2023-04-25 00:11:24',37,'菜式类型7',7,17,'2023-04-25'),
(48,'2023-04-25 00:11:24',38,'菜式类型8',8,18,'2023-04-25');
UNLOCK TABLES;

-- 11. 收藏表（优化索引，支撑收藏功能）
CREATE TABLE `storeup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `refid` bigint(20) NOT NULL COMMENT '关联ID（菜谱/其他表ID）',
  `tablename` varchar(200) NOT NULL COMMENT '关联表名（caipuxinxi/...）',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `picture` longtext NOT NULL COMMENT '图片',
  `type` varchar(200) DEFAULT '1' COMMENT '类型(1:收藏,21:赞,22:踩,31:竞拍参与,41:关注)',
  `inteltype` varchar(200) DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_ref_table` (`userid`, `refid`, `tablename`, `type`) COMMENT '用户+关联信息+类型唯一，避免重复操作',
  KEY `idx_userid_type` (`userid`, `type`) COMMENT '按用户+类型查询收藏/点赞'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';

-- 12. 系统简介表
CREATE TABLE `systemintro` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '副标题',
  `content` longtext NOT NULL COMMENT '内容',
  `picture1` longtext COMMENT '图片1',
  `picture2` longtext COMMENT '图片2',
  `picture3` longtext COMMENT '图片3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统简介';

-- 插入系统简介数据
LOCK TABLES `systemintro` WRITE;
INSERT INTO `systemintro` VALUES (1,'2023-04-25 00:11:24','系统简介','SYSTEM INTRODUCTION','当遇到挫折或失败，你是看见失败还是看见机会?挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。人生在世，从古到今，不分天子平民，机遇虽有不同，但总不免有身陷困境或遭遇难题之处，这时候唯有通权达变，才能使人转危为安，甚至反败为胜。大部分的人，一生当中，最痛苦的经验是失去所爱的人，其次是丢掉一份工作。其实，经得起考验的人，就算是被开除也不会惊慌，要学会面对。','upload/systemintro_picture1.jpg','upload/systemintro_picture2.jpg','upload/systemintro_picture3.jpg');
UNLOCK TABLES;

-- 13. Token表（优化过期时间配置，补全语句）
CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名（yonghu/users）',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '令牌',
  `expiratedtime` datetime NOT NULL COMMENT '过期时间',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`) COMMENT '令牌唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Token表';

-- 补充：用户食材库表（test_data.sql 依赖）
CREATE TABLE `user_shicai` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户ID（关联yonghu表）',
  `shicai_name` varchar(200) NOT NULL COMMENT '食材名称',
  `quantity` int DEFAULT NULL COMMENT '数量',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位（克/毫升/个等）',
  `purchase_date` datetime DEFAULT NULL COMMENT '采购日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '过期日期',
  `status` enum('new', 'used', 'expired', 'discarded') NOT NULL DEFAULT 'new' COMMENT '食材状态',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userid`) COMMENT '用户ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户食材库';

-- 补充：饮食统计表（test_data.sql 依赖）
CREATE TABLE `diet_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户ID（关联yonghu表）',
  `record_date` datetime DEFAULT NULL COMMENT '记录日期',
  `meal_type` varchar(50) DEFAULT NULL COMMENT '餐次类型：breakfast/lunch/dinner/snack',
  `food_name` varchar(200) DEFAULT NULL COMMENT '食物名称',
  `calories` int(11) DEFAULT NULL COMMENT '卡路里(kcal)',
  `protein` decimal(10,1) DEFAULT NULL COMMENT '蛋白质(g)',
  `carbs` decimal(10,1) DEFAULT NULL COMMENT '碳水化合物(g)',
  `fat` decimal(10,1) DEFAULT NULL COMMENT '脂肪(g)',
  `notes` text COMMENT '备注',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `idx_userid_recorddate` (`userid`, `record_date`) COMMENT '用户+日期索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='饮食统计';

-- 可选：添加外键约束（保障数据一致性）
ALTER TABLE `user_shicai`
ADD CONSTRAINT `fk_user_shicai_yonghu` FOREIGN KEY (`userid`)
REFERENCES `yonghu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `diet_statistics`
ADD CONSTRAINT `fk_diet_statistics_yonghu` FOREIGN KEY (`userid`)
REFERENCES `yonghu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 14. 管理员用户表（users表）
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(100) DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员用户表';

-- 插入管理员数据
LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (1,'admin','admin','管理员','2023-04-25 00:11:24');
UNLOCK TABLES;

-- 15. 保质期参考库表（shicai_shelf_life）
CREATE TABLE `shicai_shelf_life` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shicai_id` bigint(20) NOT NULL COMMENT '食材ID（关联shicai表）',
  `shicai_name` varchar(200) DEFAULT NULL COMMENT '食材名称（冗余字段，方便查询）',
  `category` varchar(100) DEFAULT NULL COMMENT '食材分类（冗余字段，方便查询）',
  `shelf_life_days` int(11) DEFAULT NULL COMMENT '保质期天数',
  `storage_method` varchar(200) DEFAULT NULL COMMENT '存储方法',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_shicai_id` (`shicai_id`) COMMENT '食材ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='保质期参考库';

-- 插入保质期参考库数据
LOCK TABLES `shicai_shelf_life` WRITE;
INSERT INTO `shicai_shelf_life` (`id`, `shicai_id`, `shicai_name`, `category`, `shelf_life_days`, `storage_method`, `addtime`) VALUES 
(1, 1, '白菜', '蔬菜', 7, '冷藏保存，温度2-8℃', '2023-04-25 08:11:24'),
(2, 2, '猪肉', '肉类', 3, '冷藏保存，尽快食用', '2023-04-25 08:11:24'),
(3, 3, '大米', '粮油', 180, '常温阴凉干燥处保存', '2023-04-25 08:11:24'),
(4, 4, '食用油', '粮油', 365, '密封常温保存', '2023-04-25 08:11:24'),
(5, 5, '鸡蛋', '蛋奶', 30, '冷藏保存', '2023-04-25 08:11:24'),
(6, 6, '西红柿', '蔬菜', 14, '冷藏保存，避免挤压', '2023-04-25 08:11:24'),
(7, 7, '鸡肉', '肉类', 90, '冷冻保存，-18℃以下', '2023-04-25 08:11:24'),
(8, 8, '鱼', '海鲜', 60, '冷冻保存', '2023-04-25 08:11:24'),
(9, 9, '牛奶', '蛋奶', 5, '冷藏保存，2-4℃', '2023-04-25 08:11:24'),
(10, 10, '酱油', '调味品', 365, '密封常温保存，避光', '2023-04-25 08:11:24'),
(11, 11, '土豆', '蔬菜', 7, '常温保存，避免阳光直射', '2023-04-25 08:11:24'),
(12, 12, '豆腐', '豆制品', 14, '冷藏保存', '2023-04-25 08:11:24'),
(13, 13, '苹果', '水果', 30, '常温保存', '2023-04-25 08:11:24'),
(14, 14, '面粉', '粮油', 180, '密封常温保存', '2023-04-25 08:11:24'),
(15, 15, '虾', '海鲜', 90, '冷冻保存', '2023-04-25 08:11:24');
UNLOCK TABLES;

-- 16. 过期提醒表（expiry_reminder）
CREATE TABLE `expiry_reminder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户ID（关联yonghu表）',
  `user_shicai_id` bigint(20) NOT NULL COMMENT '用户食材ID（关联user_shicai表）',
  `remind_date` datetime DEFAULT NULL COMMENT '提醒日期',
  `status` enum('pending','sent','read') NOT NULL DEFAULT 'pending' COMMENT '提醒状态',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_userid_status` (`userid`, `status`) COMMENT '用户+状态索引',
  KEY `fk_reminder_user_shicai` (`user_shicai_id`),
  CONSTRAINT `fk_reminder_yonghu` FOREIGN KEY (`userid`) REFERENCES `yonghu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reminder_user_shicai` FOREIGN KEY (`user_shicai_id`) REFERENCES `user_shicai` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='过期提醒';

-- 插入用户食材库测试数据
LOCK TABLES `user_shicai` WRITE;
INSERT INTO `user_shicai` VALUES 
(1, 11, '白菜', 500, '克', '2023-12-20 10:00:00', '2023-12-27 10:00:00', 'new', '2023-12-20 10:00:00'),
(2, 11, '猪肉', 300, '克', '2023-12-24 09:00:00', '2023-12-27 09:00:00', 'new', '2023-12-24 09:00:00'),
(3, 11, '大米', 1000, '克', '2023-11-01 08:00:00', '2024-04-29 08:00:00', 'new', '2023-11-01 08:00:00'),
(4, 11, '食用油', 500, '毫升', '2023-10-15 10:00:00', '2024-10-14 10:00:00', 'new', '2023-10-15 10:00:00'),
(5, 11, '鸡蛋', 12, '个', '2023-12-15 08:00:00', '2024-01-14 08:00:00', 'new', '2023-12-15 08:00:00'),
(6, 11, '番茄', 500, '克', '2023-12-22 09:00:00', '2024-01-05 09:00:00', 'new', '2023-12-22 09:00:00'),
(7, 11, '鸡肉', 1000, '克', '2023-11-20 10:00:00', '2024-02-18 10:00:00', 'new', '2023-11-20 10:00:00'),
(8, 11, '牛肉', 500, '克', '2023-12-01 09:00:00', '2024-01-30 09:00:00', 'new', '2023-12-01 09:00:00'),
(9, 12, '白菜', 300, '克', '2023-12-25 10:00:00', '2024-01-01 10:00:00', 'new', '2023-12-25 10:00:00'),
(10, 12, '大米', 2000, '克', '2023-10-01 08:00:00', '2024-03-29 08:00:00', 'new', '2023-10-01 08:00:00'),
(11, 12, '鸡蛋', 6, '个', '2023-12-20 08:00:00', '2024-01-19 08:00:00', 'new', '2023-12-20 08:00:00'),
(12, 12, '牛奶', 1000, '毫升', '2023-12-23 09:00:00', '2023-12-28 09:00:00', 'new', '2023-12-23 09:00:00'),
(13, 13, '猪肉', 400, '克', '2023-12-26 10:00:00', '2023-12-29 10:00:00', 'new', '2023-12-26 10:00:00'),
(14, 13, '食用油', 250, '毫升', '2023-09-01 10:00:00', '2024-08-31 10:00:00', 'new', '2023-09-01 10:00:00'),
(15, 13, '番茄', 300, '克', '2023-12-20 09:00:00', '2024-01-03 09:00:00', 'new', '2023-12-20 09:00:00'),
(16, 11, '面粉', 500, '克', '2023-08-01 10:00:00', '2024-07-31 10:00:00', 'new', '2023-08-01 10:00:00'),
(17, 11, '土豆', 5, '个', '2023-12-23 08:00:00', '2023-12-30 08:00:00', 'new', '2023-12-23 08:00:00'),
(18, 12, '胡萝卜', 500, '克', '2023-12-18 09:00:00', '2024-01-01 09:00:00', 'new', '2023-12-18 09:00:00');
UNLOCK TABLES;

-- 插入饮食统计测试数据（已在diet_statistics表创建时插入）

-- 完成数据库初始化
-- 注意：执行此SQL前请确保已备份现有数据
