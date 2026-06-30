#!/bin/bash

# 定义 Webase 部署目录的路径
USER_NAME="你的用户名"
WEBASE_DIR="/home/$USER_NAME/webase-deploy"

echo "--- 正在检查 Webase 部署目录 ---"

# 检查目标目录是否存在
if [ ! -d "$WEBASE_DIR" ]; then
    echo "错误：目标目录 $WEBASE_DIR 不存在。"
    echo "请编辑脚本，将 WEBASE_DIR 变量替换为正确的路径。"
    exit 1
fi

echo "目标目录：$WEBASE_DIR"
echo "--- 正在使用 sudo 权限执行 Webase 停止操作 (stopAll) ---"

# 使用 sudo 切换到目标目录并执行停止命令
# sh -c 用于在 sudo 环境中执行多个命令（cd 和 python3）
sudo sh -c "cd \"$WEBASE_DIR\" && python3 deploy.py stopAll"

# 检查停止命令的退出状态
if [ $? -eq 0 ]; then
    echo "Webase 服务已成功停止。"
else
    echo "警告：Webase 服务停止命令执行完毕，但可能存在非零退出码或部分服务未停止。"
fi

echo "--- 操作完成 ---"
