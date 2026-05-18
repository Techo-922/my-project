-- 调试用SQL - 测试单行插入
USE `springbootct3p7`;

-- 先查看表结构
SHOW CREATE TABLE user_shicai;

-- 测试插入单行数据
INSERT INTO `user_shicai` (`userid`, `shicai_name`, `quantity`, `unit`, `purchase_date`, `expiry_date`, `status`, `addtime`) VALUES 
(20, '西红柿', 500, '克', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 'new', DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 查看插入结果
SELECT * FROM user_shicai WHERE userid = 20;
