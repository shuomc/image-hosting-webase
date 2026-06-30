#!/bin/bash

# 定义 Webase 部署目录的路径
USER_NAME="你的用户名"
WEBASE_DIR="/home/$USER_NAME/webase-deploy"

# 检查目标目录是否存在
if [ ! -d "$WEBASE_DIR" ]; then
    echo "错误：目标目录 $WEBASE_DIR 不存在。"
    echo "请编辑脚本，将 WEBASE_DIR 变量替换为正确的路径。"
    exit 1
fi

echo "--- 正在使用 sudo 权限执行 Webase 重启操作 ---"

# 使用 sudo 切换到目标目录并执行停止命令
sudo sh -c "cd \"$WEBASE_DIR\" && python3 deploy.py stopAll"

# 检查停止命令是否成功
if [ $? -eq 0 ]; then
    echo "Webase 服务已成功停止。"
else
    echo "警告：Webase 服务停止命令可能未完全成功，继续尝试启动。"
fi

# 使用 sudo 切换到目标目录并执行启动命令
sudo sh -c "cd \"$WEBASE_DIR\" && python3 deploy.py startAll"

# 检查启动命令是否成功
if [ $? -eq 0 ]; then
    echo "Webase 服务已成功启动。"
else
    echo "错误：Webase 服务启动命令失败！"
fi

echo "--- 操作完成 ---"
