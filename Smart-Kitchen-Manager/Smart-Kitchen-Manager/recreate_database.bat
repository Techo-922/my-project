@echo off
echo ========================================
echo 重新创建数据库 springbootct3p7
echo ========================================
echo.

echo 步骤 1: 删除旧数据库并创建新数据库...
mysql -u root -pSwx2004 -e "DROP DATABASE IF EXISTS springbootct3p7; CREATE DATABASE springbootct3p7 DEFAULT CHARACTER SET utf8mb4;"

echo.
echo 步骤 2: 导入数据库结构...
mysql -u root -pSwx2004 springbootct3p7 < db\springbootct3p7.sql

echo.
echo 步骤 3: 导入测试数据...
mysql -u root -pSwx2004 springbootct3p7 < db\test_data.sql

echo.
echo ========================================
echo 数据库重建完成！
echo ========================================
pause
