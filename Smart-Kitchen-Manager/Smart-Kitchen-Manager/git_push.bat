@echo off
chcp 65001 >nul
echo ========================================
echo    Git 快速上传脚本
echo ========================================
echo.

REM 检查是否是 Git 仓库
git status >nul 2>&1
if errorlevel 1 (
    echo [提示] 当前不是 Git 仓库，正在初始化...
    git init
    echo [完成] Git 仓库初始化成功
    echo.
)

REM 添加所有文件
echo [步骤 1/4] 添加所有文件到暂存区...
git add .
echo [完成] 文件添加完成
echo.

REM 查看状态
echo [步骤 2/4] 查看将要提交的文件...
git status
echo.

REM 提交
set /p commit_message=请输入提交信息（直接回车使用默认信息）：
if "%commit_message%"=="" (
    set commit_message=feat: 更新项目代码
)
echo [步骤 3/4] 提交到本地仓库...
git commit -m "%commit_message%"
echo [完成] 提交成功
echo.

REM 推送
echo [步骤 4/4] 推送到远程仓库...
git push
if errorlevel 1 (
    echo.
    echo [错误] 推送失败！可能的原因：
    echo 1. 还没有关联远程仓库
    echo 2. 需要先设置上游分支
    echo.
    echo 请手动执行以下命令：
    echo git remote add origin https://github.com/你的用户名/仓库名.git
    echo git push -u origin master
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo    上传完成！
echo ========================================
pause
