-- 测试数据 SQL
-- 使用数据库
USE `springbootct3p7`;

-- 临时禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 清空现有测试数据（避免主键冲突）
DELETE FROM storeup WHERE userid IN (11, 12, 13);
DELETE FROM user_shicai WHERE userid IN (11, 12, 13);
DELETE FROM diet_statistics WHERE userid IN (11, 12, 13);
DELETE FROM caipuxinxi WHERE id IN (31, 32, 33, 34, 35);
DELETE FROM yonghu WHERE id IN (11, 12, 13);
DELETE FROM yonghu WHERE zhanghao IN ('账号1', '账号2', '账号3');

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 1. 插入测试用户数据
-- yonghu表字段：id, addtime, zhanghao, mima, xingming, xingbie, youxiang, shoujihaoma, touxiang, health_preference, allergy_info
INSERT INTO `yonghu` (`id`, `addtime`, `zhanghao`, `mima`, `xingming`, `xingbie`, `youxiang`, `shoujihaoma`, `touxiang`, `health_preference`, `allergy_info`) VALUES 
(11, '2023-04-25 00:11:24', '账号1', '123456', '姓名1', '男', '773890001@qq.com', '13823888881', 'upload/yonghu_touxiang1.jpg', '{"diet": "低糖", "goal": "减脂"}', '花生'),
(12, '2023-04-25 00:11:24', '账号2', '123456', '姓名2', '男', '773890002@qq.com', '13823888882', 'upload/yonghu_touxiang2.jpg', '{"diet": "素食", "goal": "养生"}', '海鲜'),
(13, '2023-04-25 00:11:24', '账号3', '123456', '姓名3', '男', '773890003@qq.com', '13823888883', 'upload/yonghu_touxiang3.jpg', '{"diet": "高蛋白", "goal": "增肌"}', '无');

-- 2. 插入用户食材数据
-- user_shicai表字段：id, userid, shicai_name, quantity, unit, purchase_date, expiry_date, status, addtime
INSERT INTO `user_shicai` (`id`, `userid`, `shicai_name`, `quantity`, `unit`, `purchase_date`, `expiry_date`, `status`, `addtime`) VALUES 
(1, 11, '西红柿', 500, '克', '2023-12-20 10:00:00', '2023-12-27 10:00:00', 'new', '2023-12-20 10:00:00'),
(2, 11, '鸡蛋', 10, '个', '2023-12-18 09:00:00', '2023-12-25 09:00:00', 'new', '2023-12-18 09:00:00'),
(3, 11, '大米', 5000, '克', '2023-11-01 08:00:00', '2024-05-01 08:00:00', 'new', '2023-11-01 08:00:00'),
(4, 11, '牛奶', 1000, '毫升', '2023-12-22 10:00:00', '2023-12-29 10:00:00', 'new', '2023-12-22 10:00:00'),
(5, 11, '土豆', 12, '个', '2023-12-15 08:00:00', '2024-01-14 08:00:00', 'new', '2023-12-15 08:00:00'),
(6, 12, '白菜', 2000, '克', '2023-12-21 09:00:00', '2023-12-28 09:00:00', 'new', '2023-12-21 09:00:00'),
(7, 12, '猪肉', 1000, '克', '2023-12-23 10:00:00', '2023-12-26 10:00:00', 'new', '2023-12-23 10:00:00'),
(8, 13, '鸡胸肉', 800, '克', '2023-12-24 09:00:00', '2023-12-27 09:00:00', 'new', '2023-12-24 09:00:00'),
(9, 13, '西兰花', 500, '克', '2023-12-22 10:00:00', '2023-12-29 10:00:00', 'new', '2023-12-22 10:00:00');

-- 3. 插入饮食统计数据
-- diet_statistics表字段：id, userid, record_date, meal_type, food_name, calories, protein, carbs, fat, notes, addtime
INSERT INTO `diet_statistics` (`id`, `userid`, `record_date`, `meal_type`, `food_name`, `calories`, `protein`, `carbs`, `fat`, `notes`, `addtime`) VALUES
(1, 11, '2023-12-27 08:00:00', 'breakfast', '鸡蛋三明治', 350, 15.5, 35.0, 18.0, '早餐', '2023-12-27 08:00:00'),
(2, 11, '2023-12-27 12:00:00', 'lunch', '西红柿炒鸡蛋+米饭', 450, 18.0, 65.0, 12.0, '午餐', '2023-12-27 12:00:00'),
(3, 11, '2023-12-27 18:00:00', 'dinner', '清蒸鱼+青菜', 280, 32.0, 15.0, 8.0, '晚餐', '2023-12-27 18:00:00'),
(4, 11, '2023-12-26 08:00:00', 'breakfast', '牛奶燕麦', 300, 10.0, 45.0, 8.0, '早餐', '2023-12-26 08:00:00'),
(5, 11, '2023-12-26 12:00:00', 'lunch', '鸡胸肉沙拉', 320, 35.0, 20.0, 10.0, '午餐', '2023-12-26 12:00:00'),
(6, 12, '2023-12-27 08:00:00', 'breakfast', '豆浆油条', 400, 12.0, 50.0, 18.0, '早餐', '2023-12-27 08:00:00'),
(7, 12, '2023-12-27 12:00:00', 'lunch', '红烧肉+米饭', 650, 28.0, 68.0, 32.0, '午餐', '2023-12-27 12:00:00'),
(8, 13, '2023-12-27 08:00:00', 'breakfast', '鸡蛋灌饼', 380, 14.0, 50.0, 14.0, '早餐', '2023-12-27 08:00:00'),
(9, 13, '2023-12-27 12:00:00', 'lunch', '鸡胸肉+糙米饭', 520, 45.0, 55.0, 8.0, '午餐', '2023-12-27 12:00:00');

-- 4. 插入菜谱信息数据（如果需要测试推荐功能）
-- caipuxinxi表字段：id, addtime, caipumingcheng, caipufengmian, caishileixing, pengrenfangshi, fenshu, cailiao, zhizuoliucheng, faburiqi, clicktime, clicknum
INSERT INTO `caipuxinxi` (`id`, `addtime`, `caipumingcheng`, `caipufengmian`, `caishileixing`, `pengrenfangshi`, `fenshu`, `cailiao`, `zhizuoliucheng`, `faburiqi`, `clicktime`, `clicknum`) VALUES 
(31, '2023-04-25 00:11:24', '西红柿炒鸡蛋', 'upload/caipuxinxi_caipufengmian1.jpg', '家常菜', '炒', 85, '西红柿2个,鸡蛋3个,盐适量,糖少许', '1.西红柿切块 2.鸡蛋打散 3.先炒鸡蛋 4.加入西红柿翻炒 5.调味出锅', '2023-04-25', '2023-04-25 08:11:24', 15),
(32, '2023-04-25 00:11:24', '土豆炖牛肉', 'upload/caipuxinxi_caipufengmian2.jpg', '家常菜', '炖', 90, '土豆3个,牛肉500克,葱姜蒜适量', '1.牛肉切块焯水 2.土豆切块 3.炖煮1小时 4.调味出锅', '2023-04-25', '2023-04-25 08:11:24', 20),
(33, '2023-04-25 00:11:24', '清炒白菜', 'upload/caipuxinxi_caipufengmian3.jpg', '素菜', '炒', 75, '白菜500克,蒜3瓣,盐适量', '1.白菜洗净切段 2.蒜切片 3.热油爆香蒜片 4.加入白菜翻炒 5.调味出锅', '2023-04-25', '2023-04-25 08:11:24', 12),
(34, '2023-04-25 00:11:24', '红烧猪肉', 'upload/caipuxinxi_caipufengmian4.jpg', '家常菜', '烧', 88, '猪肉500克,酱油,料酒,冰糖', '1.猪肉切块焯水 2.炒糖色 3.加入猪肉翻炒 4.加水炖煮40分钟', '2023-04-25', '2023-04-25 08:11:24', 25),
(35, '2023-04-25 00:11:24', '鸡胸肉沙拉', 'upload/caipuxinxi_caipufengmian5.jpg', '健身餐', '煮', 92, '鸡胸肉200克,生菜,西兰花,橄榄油', '1.鸡胸肉煮熟切片 2.蔬菜洗净 3.混合装盘 4.淋上橄榄油', '2023-04-25', '2023-04-25 08:11:24', 30);

-- 5. 插入收藏数据（用于个性化推荐测试）
-- storeup表字段：id, addtime, userid, refid, tablename, name, picture, type, inteltype, remark
-- 用户12收藏了一些菜谱（与用户11有相似食材）
INSERT INTO `storeup` (`id`, `addtime`, `userid`, `refid`, `tablename`, `name`, `picture`, `type`, `inteltype`, `remark`) VALUES 
(1, '2023-12-20 10:00:00', 12, 31, 'caipuxinxi', '西红柿炒鸡蛋', 'upload/caipuxinxi_caipufengmian1.jpg', '1', NULL, NULL),
(2, '2023-12-21 11:00:00', 12, 32, 'caipuxinxi', '土豆炖牛肉', 'upload/caipuxinxi_caipufengmian2.jpg', '1', NULL, NULL),
(3, '2023-12-22 12:00:00', 12, 33, 'caipuxinxi', '清炒白菜', 'upload/caipuxinxi_caipufengmian3.jpg', '1', NULL, NULL);

-- 用户13收藏了一些菜谱
INSERT INTO `storeup` (`id`, `addtime`, `userid`, `refid`, `tablename`, `name`, `picture`, `type`, `inteltype`, `remark`) VALUES 
(4, '2023-12-23 10:00:00', 13, 35, 'caipuxinxi', '鸡胸肉沙拉', 'upload/caipuxinxi_caipufengmian5.jpg', '1', NULL, NULL),
(5, '2023-12-24 11:00:00', 13, 31, 'caipuxinxi', '西红柿炒鸡蛋', 'upload/caipuxinxi_caipufengmian1.jpg', '1', NULL, NULL);

COMMIT;
