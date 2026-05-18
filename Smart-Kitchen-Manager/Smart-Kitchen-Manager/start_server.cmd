@echo off
chcp 65001 >nul
echo ========================================
echo 启动智能菜谱推荐系统后端服务
echo ========================================
echo.
echo 正在启动Spring Boot应用...
echo 请等待应用启动完成（大约需要30-60秒）
echo.
echo 启动完成后，可以访问：
echo - 前台页面: http://localhost:8081/springbootct3p7/front/front/dist/index.html
echo - 后台管理: http://localhost:8081/springbootct3p7/admin/admin/dist/index.html
echo.
echo ========================================
echo.

call mvnw.cmd spring-boot:run
