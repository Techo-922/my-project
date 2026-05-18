-- ============================================
-- 安全的索引创建脚本（自动检查索引是否存在）
-- ============================================
-- 使用方法：直接运行此脚本，会自动跳过已存在的索引
-- ============================================

USE `springbootct3p7`;

DELIMITER $$

-- 创建临时存储过程用于安全创建索引
DROP PROCEDURE IF EXISTS create_index_if_not_exists$$
CREATE PROCEDURE create_index_if_not_exists(
    IN table_name VARCHAR(128),
    IN index_name VARCHAR(128),
    IN index_definition TEXT
)
BEGIN
    DECLARE index_exists INT DEFAULT 0;
    
    -- 检查索引是否存在
    SELECT COUNT(*) INTO index_exists
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = table_name
    AND INDEX_NAME = index_name;
    
    -- 如果索引不存在，则创建
    IF index_exists = 0 THEN
        SET @sql = index_definition;
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SELECT CONCAT('创建索引: ', index_name, ' 在表 ', table_name) AS result;
    ELSE
        SELECT CONCAT('索引已存在，跳过: ', index_name, ' 在表 ', table_name) AS result;
    END IF;
END$$

DELIMITER ;

-- ============================================
-- 1. expiry_reminder 表索引
-- ============================================

CALL create_index_if_not_exists(
    'expiry_reminder',
    'idx_userid',
    'CREATE INDEX idx_userid ON expiry_reminder(userid)'
);

CALL create_index_if_not_exists(
    'expiry_reminder',
    'idx_user_shicai_id',
    'CREATE INDEX idx_user_shicai_id ON expiry_reminder(user_shicai_id)'
);

CALL create_index_if_not_exists(
    'expiry_reminder',
    'idx_status',
    'CREATE INDEX idx_status ON expiry_reminder(status)'
);

CALL create_index_if_not_exists(
    'expiry_reminder',
    'idx_remind_date',
    'CREATE INDEX idx_remind_date ON expiry_reminder(remind_date)'
);

CALL create_index_if_not_exists(
    'expiry_reminder',
    'idx_userid_status',
    'CREATE INDEX idx_userid_status ON expiry_reminder(userid, status)'
);

CALL create_index_if_not_exists(
    'expiry_reminder',
    'idx_shicai_status',
    'CREATE INDEX idx_shicai_status ON expiry_reminder(user_shicai_id, status)'
);

-- ============================================
-- 2. shicai_shelf_life 表索引
-- ============================================

CALL create_index_if_not_exists(
    'shicai_shelf_life',
    'idx_shicai_id',
    'CREATE UNIQUE INDEX idx_shicai_id ON shicai_shelf_life(shicai_id)'
);

-- ============================================
-- 3. user_shicai 表索引
-- ============================================

CALL create_index_if_not_exists(
    'user_shicai',
    'idx_status_expiry',
    'CREATE INDEX idx_status_expiry ON user_shicai(status, expiry_date)'
);

-- ============================================
-- 清理临时存储过程
-- ============================================

DROP PROCEDURE IF EXISTS create_index_if_not_exists;

-- ============================================
-- 验证索引创建结果
-- ============================================

SELECT '========== expiry_reminder 表索引 ==========' AS '';

SELECT 
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX,
    CASE WHEN NON_UNIQUE = 0 THEN 'UNIQUE' ELSE 'NON-UNIQUE' END AS uniqueness,
    INDEX_TYPE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'springbootct3p7'
AND TABLE_NAME = 'expiry_reminder'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

SELECT '========== shicai_shelf_life 表索引 ==========' AS '';

SELECT 
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX,
    CASE WHEN NON_UNIQUE = 0 THEN 'UNIQUE' ELSE 'NON-UNIQUE' END AS uniqueness,
    INDEX_TYPE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'springbootct3p7'
AND TABLE_NAME = 'shicai_shelf_life'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

SELECT '========== user_shicai 表索引 ==========' AS '';

SELECT 
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX,
    CASE WHEN NON_UNIQUE = 0 THEN 'UNIQUE' ELSE 'NON-UNIQUE' END AS uniqueness,
    INDEX_TYPE
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = 'springbootct3p7'
AND TABLE_NAME = 'user_shicai'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

SELECT '索引创建/验证完成！' AS message;
