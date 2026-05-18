/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50727 (5.7.27-log)
 Source Host           : localhost:3306
 Source Schema         : springbootct3p7

 Target Server Type    : MySQL
 Target Server Version : 50727 (5.7.27-log)
 File Encoding         : 65001

 Date: 06/01/2026 01:34:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aboutus
-- ----------------------------
DROP TABLE IF EXISTS `aboutus`;
CREATE TABLE `aboutus`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `subtitle` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  `picture1` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片1',
  `picture2` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片2',
  `picture3` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片3',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关于我们' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aboutus
-- ----------------------------
INSERT INTO `aboutus` VALUES (1, '2023-04-25 00:11:24', '关于我们', 'ABOUT US', '不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?\n你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。', 'upload/aboutus_picture1.jpg', 'upload/aboutus_picture2.jpg', 'upload/aboutus_picture3.jpg');

-- ----------------------------
-- Table structure for caipuxinxi
-- ----------------------------
DROP TABLE IF EXISTS `caipuxinxi`;
CREATE TABLE `caipuxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caipumingcheng` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜谱名称',
  `caipufengmian` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '菜谱封面',
  `caishileixing` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜式类型',
  `pengrenfangshi` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '烹饪方式',
  `fenshu` int(11) NOT NULL COMMENT '分数',
  `cailiao` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '材料',
  `zhizuoliucheng` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '制作流程',
  `faburiqi` date NULL DEFAULT NULL COMMENT '发布日期',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int(11) NULL DEFAULT 0 COMMENT '点击次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜谱信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of caipuxinxi
-- ----------------------------
INSERT INTO `caipuxinxi` VALUES (31, '2023-04-25 00:11:24', '宫保鸡丁', 'upload/caipuxinxi_caipufengmian1.jpg', '川菜', '炒', 95, '鸡胸肉,花生米,干辣椒,花椒,葱,姜,蒜,生抽,老抽,料酒,白糖,醋,淀粉', '1. 鸡胸肉切丁，加料酒、盐、淀粉腌制15分钟\n2. 调制宫保汁：生抽、老抽、白糖、醋、淀粉、水混合\n3. 热油炒花生米至金黄，捞出备用\n4. 锅中留底油，爆香干辣椒和花椒\n5. 下鸡丁快速翻炒至变色\n6. 加入葱姜蒜，倒入宫保汁\n7. 最后加入花生米翻炒均匀即可', '2023-04-25', '2026-01-06 00:24:41', 22);
INSERT INTO `caipuxinxi` VALUES (32, '2023-04-25 00:11:24', '白切鸡', 'upload/caipuxinxi_caipufengmian2.jpg', '粤菜', '煮', 88, '三黄鸡,姜,葱,料酒,盐', '1. 鸡洗净，去除内脏\n2. 锅中加水，放入姜片、葱段、料酒烧开\n3. 将整鸡放入，水要没过鸡身\n4. 大火煮开后转小火煮15分钟\n5. 关火焖10分钟\n6. 捞出鸡放入冰水中浸泡10分钟\n7. 切块装盘，配姜葱蘸料食用', '2023-04-25', '2026-01-05 19:25:30', 31);
INSERT INTO `caipuxinxi` VALUES (33, '2023-04-25 00:11:24', '糖醋鲤鱼', 'upload/caipuxinxi_caipufengmian3.jpg', '鲁菜', '炸', 85, '鲤鱼,番茄酱,白糖,醋,生抽,淀粉,葱,姜,蒜', '1. 鲤鱼去鳞去内脏，两面打花刀\n2. 用盐、料酒腌制20分钟\n3. 裹上淀粉，下油锅炸至金黄酥脆\n4. 调糖醋汁：番茄酱、白糖、醋、生抽、水、淀粉混合\n5. 锅中留底油，爆香葱姜蒜\n6. 倒入糖醋汁煮至浓稠\n7. 将汁浇在炸好的鱼上即可', '2023-04-25', '2026-01-06 00:27:11', 16);
INSERT INTO `caipuxinxi` VALUES (34, '2023-04-25 00:11:24', '松鼠桂鱼', 'upload/caipuxinxi_caipufengmian4.jpg', '苏菜', '炸', 86, '桂鱼,番茄酱,白糖,醋,青豆,玉米粒,淀粉', '1. 桂鱼去骨，鱼肉打十字花刀\n2. 裹上淀粉，炸至金黄定型\n3. 调糖醋汁：番茄酱、白糖、醋、水、淀粉\n4. 锅中炒香青豆和玉米粒\n5. 倒入糖醋汁煮至浓稠\n6. 将汁浇在炸好的鱼上\n7. 鱼头竖起呈松鼠状即可', '2023-04-25', '2026-01-06 01:32:30', 33);
INSERT INTO `caipuxinxi` VALUES (35, '2023-04-25 00:11:24', '西湖醋鱼', 'upload/caipuxinxi_caipufengmian5.jpg', '浙菜', '煮', 43, '草鱼,姜,葱,镇江香醋,白糖,生抽,料酒', '1. 草鱼洗净，从背部剖开\n2. 锅中水烧开，加姜片、葱段、料酒\n3. 将鱼放入煮3分钟后翻面再煮3分钟\n4. 捞出鱼装盘\n5. 另起锅调醋汁：香醋、白糖、生抽、鱼汤\n6. 煮至浓稠后勾芡\n7. 将醋汁浇在鱼上即可', '2023-04-25', '2026-01-05 23:57:54', 31);
INSERT INTO `caipuxinxi` VALUES (36, '2023-04-25 00:11:24', '佛跳墙', 'upload/caipuxinxi_caipufengmian6.jpg,upload/caipuxinxi_caipufengmian7.jpg,upload/caipuxinxi_caipufengmian8.jpg', '闽菜', '炖', 88, '鲍鱼,海参,花胶,鱼翅,鸽蛋,香菇,笋,火腿,高汤', '1. 所有食材提前泡发处理好\n2. 鲍鱼、海参、花胶分别焯水\n3. 砂锅底部铺笋片和火腿\n4. 依次放入海参、鲍鱼、花胶、香菇\n5. 加入高汤，大火烧开\n6. 转小火炖2小时\n7. 最后加入鸽蛋和鱼翅再炖30分钟\n8. 调味后即可食用', '2023-04-25', '2023-04-25 08:11:24', 6);
INSERT INTO `caipuxinxi` VALUES (37, '2023-04-25 00:11:24', '剁椒鱼头', 'upload/caipuxinxi_caipufengmian7.jpg,upload/caipuxinxi_caipufengmian8.jpg,upload/caipuxinxi_caipufengmian9.jpg', '湘菜', '蒸', 66, '鱼头,剁椒,姜,蒜,葱,蒸鱼豉油,料酒', '1. 鱼头洗净，从中间劈开\n2. 用盐和料酒腌制15分钟\n3. 鱼头摆盘，铺上姜末和蒜末\n4. 均匀铺上剁椒\n5. 水开后上锅蒸10分钟\n6. 取出后淋上蒸鱼豉油\n7. 撒上葱花，浇热油即可', '2023-04-25', '2023-04-25 08:11:24', 7);
INSERT INTO `caipuxinxi` VALUES (38, '2023-04-25 00:11:24', '臭鳜鱼', 'upload/caipuxinxi_caipufengmian8.jpg,upload/caipuxinxi_caipufengmian9.jpg,upload/caipuxinxi_caipufengmian10.jpg', '徽菜', '煎', 4, '鳜鱼,姜,蒜,干辣椒,料酒,生抽,老抽,白糖', '1. 鳜鱼腌制发酵3-5天（或购买腌制好的）\n2. 鱼洗净沥干水分\n3. 锅中热油，煎鱼至两面金黄\n4. 加入姜片、蒜瓣、干辣椒爆香\n5. 加料酒、生抽、老抽、白糖\n6. 加水没过鱼身，大火烧开\n7. 转中火焖煮15分钟\n8. 收汁后装盘即可', '2023-04-25', '2026-01-05 18:13:15', 11);

-- ----------------------------
-- Table structure for caishileixing
-- ----------------------------
DROP TABLE IF EXISTS `caishileixing`;
CREATE TABLE `caishileixing`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caishileixing` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜式类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜式类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of caishileixing
-- ----------------------------
INSERT INTO `caishileixing` VALUES (21, '2023-04-25 00:11:24', '川菜');
INSERT INTO `caishileixing` VALUES (22, '2023-04-25 00:11:24', '粤菜');
INSERT INTO `caishileixing` VALUES (23, '2023-04-25 00:11:24', '鲁菜');
INSERT INTO `caishileixing` VALUES (24, '2023-04-25 00:11:24', '苏菜');
INSERT INTO `caishileixing` VALUES (25, '2023-04-25 00:11:24', '浙菜');
INSERT INTO `caishileixing` VALUES (26, '2023-04-25 00:11:24', '闽菜');
INSERT INTO `caishileixing` VALUES (27, '2023-04-25 00:11:24', '湘菜');
INSERT INTO `caishileixing` VALUES (28, '2023-04-25 00:11:24', '徽菜');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'upload/picture1.jpg');
INSERT INTO `config` VALUES (2, 'picture2', 'upload/picture2.jpg');
INSERT INTO `config` VALUES (3, 'picture3', 'upload/picture3.jpg');

-- ----------------------------
-- Table structure for diet_statistics
-- ----------------------------
DROP TABLE IF EXISTS `diet_statistics`;
CREATE TABLE `diet_statistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `record_date` datetime NULL DEFAULT NULL COMMENT '记录日期',
  `meal_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '餐次类型：breakfast/lunch/dinner/snack',
  `food_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '食物名称',
  `calories` int(11) NULL DEFAULT NULL COMMENT '卡路里(kcal)',
  `protein` decimal(10, 1) NULL DEFAULT NULL COMMENT '蛋白质(g)',
  `carbs` decimal(10, 1) NULL DEFAULT NULL COMMENT '碳水化合物(g)',
  `fat` decimal(10, 1) NULL DEFAULT NULL COMMENT '脂肪(g)',
  `notes` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userid`(`userid`) USING BTREE,
  INDEX `idx_record_date`(`record_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1767604673417 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '饮食统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of diet_statistics
-- ----------------------------
INSERT INTO `diet_statistics` VALUES (1, 11, '2023-12-27 08:00:00', 'breakfast', '鸡蛋三明治', 350, 15.5, 35.0, 18.0, '早餐', '2023-12-27 08:00:00');
INSERT INTO `diet_statistics` VALUES (2, 11, '2023-12-27 12:00:00', 'lunch', '西红柿炒鸡蛋+米饭', 450, 18.0, 65.0, 12.0, '午餐', '2023-12-27 12:00:00');
INSERT INTO `diet_statistics` VALUES (3, 11, '2023-12-27 18:00:00', 'dinner', '清蒸鱼+青菜', 280, 32.0, 15.0, 8.0, '晚餐', '2023-12-27 18:00:00');
INSERT INTO `diet_statistics` VALUES (4, 11, '2023-12-26 08:00:00', 'breakfast', '牛奶燕麦', 300, 10.0, 45.0, 8.0, '早餐', '2023-12-26 08:00:00');
INSERT INTO `diet_statistics` VALUES (5, 11, '2023-12-26 12:00:00', 'lunch', '鸡胸肉沙拉', 320, 35.0, 20.0, 10.0, '午餐', '2023-12-26 12:00:00');
INSERT INTO `diet_statistics` VALUES (6, 12, '2023-12-27 08:00:00', 'breakfast', '豆浆油条', 400, 12.0, 50.0, 18.0, '早餐', '2023-12-27 08:00:00');
INSERT INTO `diet_statistics` VALUES (7, 12, '2023-12-27 12:00:00', 'lunch', '红烧肉+米饭', 650, 28.0, 68.0, 32.0, '午餐', '2023-12-27 12:00:00');
INSERT INTO `diet_statistics` VALUES (8, 13, '2023-12-27 08:00:00', 'breakfast', '鸡蛋灌饼', 380, 14.0, 50.0, 14.0, '早餐', '2023-12-27 08:00:00');
INSERT INTO `diet_statistics` VALUES (9, 13, '2023-12-27 12:00:00', 'lunch', '鸡胸肉+糙米饭', 520, 45.0, 55.0, 8.0, '午餐', '2023-12-27 12:00:00');
INSERT INTO `diet_statistics` VALUES (1767604673416, 11, '2026-01-05 17:17:44', 'breakfast', '西红柿', 5, 3.0, 2.0, 2.0, '', '2026-01-05 17:17:53');

-- ----------------------------
-- Table structure for discusscaipuxinxi
-- ----------------------------
DROP TABLE IF EXISTS `discusscaipuxinxi`;
CREATE TABLE `discusscaipuxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜谱信息评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discusscaipuxinxi
-- ----------------------------

-- ----------------------------
-- Table structure for expiry_reminder
-- ----------------------------
DROP TABLE IF EXISTS `expiry_reminder`;
CREATE TABLE `expiry_reminder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `user_shicai_id` bigint(20) NOT NULL,
  `remind_date` datetime NULL DEFAULT NULL,
  `status` enum('pending','sent','read') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '过期提醒' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of expiry_reminder
-- ----------------------------

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '留言人id',
  `username` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言内容',
  `cpicture` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '留言图片',
  `reply` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复内容',
  `rpicture` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '留言信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES (91, '2023-04-25 00:11:24', 1, '用户名1', 'upload/messages_avatarurl1.jpg', '留言内容1', 'upload/messages_cpicture1.jpg', '回复内容1', 'upload/messages_rpicture1.jpg');
INSERT INTO `messages` VALUES (92, '2023-04-25 00:11:24', 2, '用户名2', 'upload/messages_avatarurl2.jpg', '留言内容2', 'upload/messages_cpicture2.jpg', '回复内容2', 'upload/messages_rpicture2.jpg');
INSERT INTO `messages` VALUES (93, '2023-04-25 00:11:24', 3, '用户名3', 'upload/messages_avatarurl3.jpg', '留言内容3', 'upload/messages_cpicture3.jpg', '回复内容3', 'upload/messages_rpicture3.jpg');
INSERT INTO `messages` VALUES (94, '2023-04-25 00:11:24', 4, '用户名4', 'upload/messages_avatarurl4.jpg', '留言内容4', 'upload/messages_cpicture4.jpg', '回复内容4', 'upload/messages_rpicture4.jpg');
INSERT INTO `messages` VALUES (95, '2023-04-25 00:11:24', 5, '用户名5', 'upload/messages_avatarurl5.jpg', '留言内容5', 'upload/messages_cpicture5.jpg', '回复内容5', 'upload/messages_rpicture5.jpg');
INSERT INTO `messages` VALUES (96, '2023-04-25 00:11:24', 6, '用户名6', 'upload/messages_avatarurl6.jpg', '留言内容6', 'upload/messages_cpicture6.jpg', '回复内容6', 'upload/messages_rpicture6.jpg');
INSERT INTO `messages` VALUES (97, '2023-04-25 00:11:24', 7, '用户名7', 'upload/messages_avatarurl7.jpg', '留言内容7', 'upload/messages_cpicture7.jpg', '回复内容7', 'upload/messages_rpicture7.jpg');
INSERT INTO `messages` VALUES (98, '2023-04-25 00:11:24', 8, '用户名8', 'upload/messages_avatarurl8.jpg', '留言内容8', 'upload/messages_cpicture8.jpg', '回复内容8', 'upload/messages_rpicture8.jpg');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `introduction` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `picture` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (61, '2023-04-25 00:11:24', '有梦想，就要努力去实现', '不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。', 'upload/news_picture1.jpg', '<p>不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?</p><p>你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。</p><p>真正有行动力的人不需要别人告诉他如何做，因为他已经在做了。就算碰到问题，他也会自己想办法，自己动手去解决或者主动寻求可以帮助他的人，而不是等着别人为自己解决问题。</p><p>首先要学习独立思考。花一点时间想一下自己喜欢什么，梦想是什么，不要别人说想环游世界，你就说你的梦想是环游世界。</p><p>很多人说现实束缚了自己，其实在这个世界上，我们一直都可以有很多选择，生活的决定权也—直都在自己手上，只是我们缺乏行动力而已。</p><p>如果你觉得安于现状是你想要的，那选择安于现状就会让你幸福和满足;如果你不甘平庸，选择一条改变、进取和奋斗的道路，在这个追求的过程中，你也一样会感到快乐。所谓的成功，即是按照自己想要的生活方式生活。最糟糕的状态，莫过于当你想要选择一条不甘平庸、改变、进取和奋斗的道路时，却以一种安于现状的方式生活，最后抱怨自己没有得到想要的人生。</p><p>因为喜欢，你不是在苦苦坚持，也因为喜欢，你愿意投入时间、精力，长久以往，获得成功就是自然而然的事情。</p>');
INSERT INTO `news` VALUES (62, '2023-04-25 00:11:24', '又是一年毕业季', '又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。', 'upload/news_picture2.jpg', '<p>又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。</p><p>过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。一直都是晚会的忠实参与者，无论是台前还是幕后，忽然间，角色转变，那种感觉确实难以用语言表达。</p><p>	过去的三年，总是默默地期盼着这个好雨时节，因为这时候，会有灿烂的阳光，会有满目的百花争艳，会有香甜的冰激凌，这是个毕业的季节，当时不经世事的我们会殷切地期待学校那一大堆的活动，期待穿上绚丽的演出服或者礼仪服，站在大礼堂镁光灯下尽情挥洒我们的澎拜的激情。</p><p>百感交集，隔岸观火与身临其境的感觉竟是如此不同。从来没想过一场晚会送走的是我们自己的时候会是怎样的感情，毕业就真的意味着结束吗?倔强的我们不愿意承认，谢谢学弟学妹们慷慨的将这次的主题定为“我们在这里”。我知道，这可能是他们对我们这些过来人的尊敬和施舍。</p><p>没有为这场晚会排练、奔波，没有为班级、学生会、文学院出点力，还真有点不习惯，百般无奈中，用“工作忙”个万能的借口来搪塞自己，欺骗别人。其实自己心里明白，那只是在逃避，只是不愿面对繁华落幕后的萧条和落寞。大四了，大家各奔东西，想凑齐班上的人真的是难上加难，敏燕从越南回来，刚落地就匆匆回了学校，那么恋家的人也启程回来了，睿睿学姐也是从家赶来跟我们团圆。大家—如既往的寒暄、打趣、调侃对方，似乎一切又回到了当初的单纯美好。</p><p>看着舞台上活泼可爱的学弟学妹们，如同一群机灵的小精灵，清澈的眼神，稚嫩的肢体，轻快地步伐，用他们那热情洋溢的舞姿渲染着在场的每一个人，我知道，我不应该羡慕嫉妒他们，不应该顾自怜惜逝去的青春，不应该感叹夕阳无限好，曾经，我们也拥有过，曾经，我们也年轻过，曾经，我们也灿烂过。我深深地告诉自己，人生的每个阶段都是美的，年轻有年轻的活力，成熟也有成熟的魅力。多—份稳重、淡然、优雅，也是漫漫时光掠影遗留下的.珍贵赏赐。</p>');
INSERT INTO `news` VALUES (63, '2023-04-25 00:11:24', '挫折路上，坚持常在心间', '回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。', 'upload/news_picture3.jpg', '<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>	或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。</p>');
INSERT INTO `news` VALUES (64, '2023-04-25 00:11:24', '挫折是另一个生命的开端', '当遇到挫折或失败，你是看见失败还是看见机会?挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。', 'upload/news_picture4.jpg', '<p>当遇到挫折或失败，你是看见失败还是看见机会?</p><p>挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。</p><p>人生在世，从古到今，不分天子平民，机遇虽有不同，但总不免有身陷困境或遭遇难题之处，这时候唯有通权达变，才能使人转危为安，甚至反败为胜。</p><p>大部分的人，一生当中，最痛苦的经验是失去所爱的人，其次是丢掉一份工作。其实，经得起考验的人，就算是被开除也不会惊慌，要学会面对。</p><p>	“塞翁失马，焉知非福。”人生的道路，并不是每一步都迈向成功，这就是追求的意义。我们还要认识到一点，挫折作为一种情绪状态和一种个人体验，各人的耐受性是大不相同的，有的人经历了一次次挫折，就能够坚忍不拔，百折不挠;有的人稍遇挫折便意志消沉，一蹶不振。所以，挫折感是一种主观感受，因为人的目的和需要不同，成功标准不同，所以同一种活动对于不同的人可能会造成不同的挫折感受。</p><p>凡事皆以平常心来看待，对于生命顺逆不要太执著。能够“破我执”是很高层的人生境界。</p><p>人事的艰难就是一种考验。就像—支剑要有磨刀来磨，剑才会利:一块璞玉要有粗石来磨，才会发出耀眼的光芒。我们能够做到的，只是如何减少、避免那些由于自身的原因所造成的挫折，而在遇到痛苦和挫折之后，则力求化解痛苦，争取幸福。我们要知道，痛苦和挫折是双重性的，它既是我们人生中难以完全避免的，也是我们在争取成功时，不可缺少的一种动力。因为我认为，推动我们奋斗的力量，不仅仅是对成功的渴望，还有为摆脱痛苦和挫折而进行的奋斗。</p>');
INSERT INTO `news` VALUES (65, '2023-04-25 00:11:24', '你要去相信，没有到不了的明天', '有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。', 'upload/news_picture5.jpg', '<p>有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。</p><p>不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。</p><p>	不管你现在是在图书馆里背着怎么也看不进去的英语单词，还是你现在迷茫地看不清未来的方向不知道要往哪走。</p><p>不管你现在是在努力着去实现梦想却没能拉近与梦想的距离，还是你已经慢慢地找不到自己的梦想了。</p><p>你都要去相信，没有到不了的明天。</p><p>	有的时候你的梦想太大，别人说你的梦想根本不可能实现;有的时候你的梦想又太小，又有人说你胸无大志;有的时候你对死党说着将来要去环游世界的梦想，却换来他的不屑一顾，于是你再也不提自己的梦想;有的时候你突然说起将来要开个小店的愿望，却发现你讲述的那个人，并没有听到你在说什么。</p><p>不过又能怎么样呢，未来始终是自己的，梦想始终是自己的，没有人会来帮你实现它。</p><p>也许很多时候我们只是需要朋友的一句鼓励，一句安慰，却也得不到。但是相信我，世界上还有很多人，只是想要和你说说话。</p><p>因为我们都一样。一样的被人说成固执，一样的在追逐他们眼里根本不在意的东西。</p><p>所以，又有什么关系呢，别人始终不是你、不能懂你的心情，你又何必多去解释呢。这个世界会来阻止你，困难也会接踵而至，其实真正关键的只有自己，有没有那个倔强。</p><p>这个世界上没有不带伤的人，真正能治愈自己的，只有自己。</p>');
INSERT INTO `news` VALUES (66, '2023-04-25 00:11:24', '离开是一种痛苦，是一种勇气，但同样也是一个考验，是一个新的开端', '无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。', 'upload/news_picture6.jpg', '<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>');
INSERT INTO `news` VALUES (67, '2023-04-25 00:11:24', 'Leave未必是一种痛苦', '无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。', 'upload/news_picture7.jpg', '<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>');
INSERT INTO `news` VALUES (68, '2023-04-25 00:11:24', '坚持才会成功', '回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。', 'upload/news_picture8.jpg', '<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>	或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。</p>');

-- ----------------------------
-- Table structure for pingfenxinxi
-- ----------------------------
DROP TABLE IF EXISTS `pingfenxinxi`;
CREATE TABLE `pingfenxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `caipumingcheng` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜谱名称',
  `caishileixing` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜式类型',
  `fenshu` int(11) NOT NULL COMMENT '分数',
  `zhanghao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `xingming` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `pingfenriqi` date NULL DEFAULT NULL COMMENT '评分日期',
  `crossuserid` bigint(20) NULL DEFAULT NULL COMMENT '跨表用户id',
  `crossrefid` bigint(20) NULL DEFAULT NULL COMMENT '跨表主键id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评分信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pingfenxinxi
-- ----------------------------
INSERT INTO `pingfenxinxi` VALUES (41, '2023-04-25 00:11:24', '宫保鸡丁', '川菜', 90, '账号1', '姓名1', '2023-04-25', 1, 1);
INSERT INTO `pingfenxinxi` VALUES (42, '2023-04-25 00:11:24', '白切鸡', '粤菜', 98, '账号2', '姓名2', '2023-04-25', 2, 2);
INSERT INTO `pingfenxinxi` VALUES (43, '2023-04-25 00:11:24', '糖醋鲤鱼', '鲁菜', 87, '账号3', '姓名3', '2023-04-25', 3, 3);
INSERT INTO `pingfenxinxi` VALUES (44, '2023-04-25 00:11:24', '松鼠桂鱼', '苏菜', 65, '账号4', '姓名4', '2023-04-25', 4, 4);
INSERT INTO `pingfenxinxi` VALUES (45, '2023-04-25 00:11:24', '西湖醋鱼', '浙菜', 88, '账号5', '姓名5', '2023-04-25', 5, 5);
INSERT INTO `pingfenxinxi` VALUES (46, '2023-04-25 00:11:24', '佛跳墙', '闽菜', 67, '账号6', '姓名6', '2023-04-25', 6, 6);
INSERT INTO `pingfenxinxi` VALUES (47, '2023-04-25 00:11:24', '剁椒鱼头', '湘菜', 94, '账号7', '姓名7', '2023-04-25', 7, 7);
INSERT INTO `pingfenxinxi` VALUES (48, '2023-04-25 00:11:24', '臭鳜鱼', '徽菜', 1, '账号8', '姓名8', '2023-04-25', 8, 8);

-- ----------------------------
-- Table structure for shicai_shelf_life
-- ----------------------------
DROP TABLE IF EXISTS `shicai_shelf_life`;
CREATE TABLE `shicai_shelf_life`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shicai_id` bigint(20) NOT NULL,
  `shelf_life_days` int(11) NULL DEFAULT NULL,
  `storage_method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '保质期参考库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shicai_shelf_life
-- ----------------------------
INSERT INTO `shicai_shelf_life` VALUES (1, 1, 7, '冷藏保存，温度2-8℃', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (2, 2, 3, '冷藏保存，尽快食用', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (3, 3, 180, '常温阴凉干燥处保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (4, 4, 365, '密封常温保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (5, 5, 30, '冷藏保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (6, 6, 14, '冷藏保存，避免挤压', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (7, 7, 90, '冷冻保存，-18℃以下', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (8, 8, 60, '冷冻保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (9, 9, 5, '冷藏保存，2-4℃', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (10, 10, 365, '密封常温保存，避光', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (11, 11, 7, '常温保存，避免阳光直射', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (12, 12, 14, '冷藏保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (13, 13, 30, '常温保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (14, 14, 180, '密封常温保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (15, 15, 90, '冷冻保存', '2023-04-25 08:11:24');
INSERT INTO `shicai_shelf_life` VALUES (16, 1, 7, '冷藏保存，温度2-8℃', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (17, 2, 3, '冷藏保存，尽快食用', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (18, 3, 5, '冷藏保存，避免水分', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (19, 4, 7, '冷藏保存，独立包装', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (20, 5, 14, '冷藏保存，避免挤压', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (21, 6, 10, '常温阴凉处保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (22, 7, 30, '常温干燥处保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (23, 8, 7, '冷藏保存，避免阳光', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (24, 9, 3, '冷藏保存，2-4℃', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (25, 10, 2, '冷藏保存，尽快食用', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (26, 11, 90, '冷冻保存，-18℃以下', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (27, 12, 60, '冷冻保存，-18℃以下', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (28, 13, 90, '冷冻保存，密封包装', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (29, 14, 180, '冷冻保存，真空包装', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (30, 15, 1, '冷藏保存，当天食用', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (31, 16, 2, '冷藏保存，0-4℃', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (32, 17, 60, '冷冻保存，-18℃以下', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (33, 18, 90, '冷冻保存，密封包装', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (34, 19, 30, '冷藏保存，避免震动', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (35, 20, 7, '冷藏保存，2-6℃', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (36, 21, 180, '冷藏保存，开封后7天', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (37, 22, 30, '冷藏保存，密封保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (38, 23, 180, '常温阴凉干燥处保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (39, 24, 365, '密封常温保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (40, 25, 180, '常温干燥处保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (41, 26, 90, '常温保存，避免潮湿', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (42, 27, 365, '密封常温保存，避光', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (43, 28, 730, '密封常温保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (44, 29, 365, '常温保存，避免潮湿', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (45, 30, 180, '冷藏保存，开封后', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (46, 31, 3, '冷藏保存，2-4℃', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (47, 32, 7, '冷藏保存，密封保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (48, 33, 180, '常温干燥处保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (49, 34, 365, '密封常温保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (50, 35, 7, '常温保存，避免阳光直射', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (51, 36, 14, '冷藏保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (52, 37, 5, '常温保存，成熟后冷藏', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (53, 38, 30, '常温保存', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (54, 39, 7, '冷藏保存，避免挤压', '2026-01-05 17:10:48');
INSERT INTO `shicai_shelf_life` VALUES (55, 40, 14, '冷藏保存', '2026-01-05 17:10:48');

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `refid` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `tablename` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `picture` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片',
  `type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '类型(1:收藏,21:赞,22:踩,31:竞拍参与,41:关注)',
  `inteltype` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1767605202966 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storeup
-- ----------------------------
INSERT INTO `storeup` VALUES (1, '2023-12-20 10:00:00', 12, 31, 'caipuxinxi', '西红柿炒鸡蛋', 'upload/caipuxinxi_caipufengmian1.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (2, '2023-12-21 11:00:00', 12, 32, 'caipuxinxi', '土豆炖牛肉', 'upload/caipuxinxi_caipufengmian2.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (3, '2023-12-22 12:00:00', 12, 33, 'caipuxinxi', '清炒白菜', 'upload/caipuxinxi_caipufengmian3.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (4, '2023-12-23 10:00:00', 13, 35, 'caipuxinxi', '鸡胸肉沙拉', 'upload/caipuxinxi_caipufengmian5.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (5, '2023-12-24 11:00:00', 13, 31, 'caipuxinxi', '西红柿炒鸡蛋', 'upload/caipuxinxi_caipufengmian1.jpg', '1', NULL, NULL);
INSERT INTO `storeup` VALUES (1767605202965, '2026-01-05 17:26:42', 11, 32, 'caipuxinxi', '土豆炖牛肉', 'upload/caipuxinxi_caipufengmian2.jpg', '1', '家常菜', NULL);

-- ----------------------------
-- Table structure for systemintro
-- ----------------------------
DROP TABLE IF EXISTS `systemintro`;
CREATE TABLE `systemintro`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `subtitle` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  `picture1` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片1',
  `picture2` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片2',
  `picture3` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片3',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关于我们' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of systemintro
-- ----------------------------
INSERT INTO `systemintro` VALUES (1, '2023-04-25 00:11:24', '系统简介', 'SYSTEM INTRODUCTION', '当遇到挫折或失败，你是看见失败还是看见机会?挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。人生在世，从古到今，不分天子平民，机遇虽有不同，但总不免有身陷困境或遭遇难题之处，这时候唯有通权达变，才能使人转危为安，甚至反败为胜。大部分的人，一生当中，最痛苦的经验是失去所爱的人，其次是丢掉一份工作。其实，经得起考验的人，就算是被开除也不会惊慌，要学会面对。', 'upload/systemintro_picture1.jpg', 'upload/systemintro_picture2.jpg', 'upload/systemintro_picture3.jpg');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'token表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 12, '账号2', 'yonghu', '用户', 'ysxcarubr9cwz97kwn934tlvgrfnaxyz', '2023-04-25 00:18:12', '2026-01-06 02:27:03');
INSERT INTO `token` VALUES (2, 11, '账号1', 'yonghu', '用户', 'wd0uk6qam4nebgmbzpecryw2y9iys0l8', '2026-01-05 17:11:11', '2026-01-06 02:32:02');
INSERT INTO `token` VALUES (3, 1, 'admin', 'users', '管理员', 'agskgmnpcg6bnvasvr27e3kyfdikm7vx', '2026-01-05 19:03:16', '2026-01-05 20:03:16');

-- ----------------------------
-- Table structure for user_shicai
-- ----------------------------
DROP TABLE IF EXISTS `user_shicai`;
CREATE TABLE `user_shicai`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL,
  `shicai_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '食材名称',
  `quantity` int(11) NULL DEFAULT NULL,
  `unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `purchase_date` datetime NULL DEFAULT NULL,
  `expiry_date` datetime NULL DEFAULT NULL,
  `status` enum('new','used','expired','discarded') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户食材库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_shicai
-- ----------------------------
INSERT INTO `user_shicai` VALUES (1, 11, '西红柿', 500, '克', '2023-12-20 10:00:00', '2027-12-16 10:00:00', 'new', '2023-12-20 10:00:00');
INSERT INTO `user_shicai` VALUES (2, 11, '鸡蛋', 10, '个', '2023-12-18 09:00:00', '2023-12-25 09:00:00', 'expired', '2023-12-18 09:00:00');
INSERT INTO `user_shicai` VALUES (3, 11, '大米', 5000, '克', '2023-11-01 08:00:00', '2026-01-05 18:55:45', 'expired', '2023-11-01 08:00:00');
INSERT INTO `user_shicai` VALUES (4, 11, '牛奶', 1000, '毫升', '2023-12-22 10:00:00', '2023-12-29 10:00:00', 'expired', '2023-12-22 10:00:00');
INSERT INTO `user_shicai` VALUES (5, 11, '土豆', 12, '个', '2023-12-15 08:00:00', '2024-01-14 08:00:00', 'expired', '2023-12-15 08:00:00');
INSERT INTO `user_shicai` VALUES (6, 12, '白菜', 2000, '克', '2023-12-21 09:00:00', '2023-12-28 09:00:00', 'expired', '2023-12-21 09:00:00');
INSERT INTO `user_shicai` VALUES (7, 12, '猪肉', 1000, '克', '2023-12-23 10:00:00', '2023-12-26 10:00:00', 'expired', '2023-12-23 10:00:00');
INSERT INTO `user_shicai` VALUES (8, 13, '鸡胸肉', 800, '克', '2023-12-24 09:00:00', '2023-12-27 09:00:00', 'expired', '2023-12-24 09:00:00');
INSERT INTO `user_shicai` VALUES (9, 13, '西兰花', 500, '克', '2023-12-22 10:00:00', '2023-12-29 10:00:00', 'expired', '2023-12-22 10:00:00');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', 'admin', '管理员', '2023-04-25 00:11:24');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhanghao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `mima` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `xingming` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `xingbie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `youxiang` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `shoujihaoma` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `touxiang` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像',
  `health_preference` json NULL COMMENT '健康偏好',
  `allergy_info` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '过敏信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `zhanghao`(`zhanghao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (11, '2023-04-25 00:11:24', '账号1', '123456', '姓名1', '男', '773890001@qq.com', '13823888881', 'upload/yonghu_touxiang1.jpg', '{\"diet\": \"低糖\", \"goal\": \"减脂\"}', '花生');
INSERT INTO `yonghu` VALUES (12, '2023-04-25 00:11:24', '账号2', '123456', '姓名2', '男', '773890002@qq.com', '13823888882', 'upload/yonghu_touxiang2.jpg', '{\"diet\": \"素食\", \"goal\": \"养生\"}', '海鲜');
INSERT INTO `yonghu` VALUES (13, '2023-04-25 00:11:24', '账号3', '123456', '姓名3', '男', '773890003@qq.com', '13823888883', 'upload/yonghu_touxiang3.jpg', '{\"diet\": \"高蛋白\", \"goal\": \"增肌\"}', '无');

SET FOREIGN_KEY_CHECKS = 1;
