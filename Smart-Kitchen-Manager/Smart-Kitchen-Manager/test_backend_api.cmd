@echo off
chcp 65001 >nul
echo ========================================
echo 后端API测试脚本
echo ========================================
echo.

echo [1/3] 测试食材列表接口...
curl -s http://localhost:8081/springbootct3p7/shicai/list
echo.
echo.

echo [2/3] 测试保质期列表接口...
curl -s "http://localhost:8081/springbootct3p7/shicaishelflife/list?page=1&limit=10"
echo.
echo.

echo [3/3] 测试保质期详情接口（ID=1）...
curl -s http://localhost:8081/springbootct3p7/shicaishelflife/info/1
echo.
echo.

echo ========================================
echo 测试完成！
echo ========================================
echo.
echo 如果看到JSON数据，说明后端正常运行。
echo 如果看到错误或空白，请检查后端服务是否启动。
echo.
pause
