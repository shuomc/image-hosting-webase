<template>
  <el-header class="header">
    <el-row align="middle" style="width: 100%;">
      <el-col :span="4" class="logo">
        <span class="logo-text">pexels</span>
      </el-col>

      <el-col :span="20" class="header-right-items">
        <div class="nav-container">
          <el-menu mode="horizontal" :ellipsis="false" class="header-menu">
            <el-menu-item index="explore">探索</el-menu-item>
            <el-menu-item index="license">
              <router-link to="/licence">
                许可证
              </router-link>
            </el-menu-item>

            <el-dropdown trigger="hover" class="more-dropdown">
              <span class="el-dropdown-link">
                <el-icon>
                  <MoreFilled />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>登录</el-dropdown-item>
                  <el-dropdown-item>图片和视频 API</el-dropdown-item>
                  <el-dropdown-item>应用和插件</el-dropdown-item>
                  <el-dropdown-item>帮助中心</el-dropdown-item>
                  <el-dropdown-item>举报内容</el-dropdown-item>
                  <el-dropdown-item>合作伙伴</el-dropdown-item>
                  <el-dropdown-item>版本说明和条款</el-dropdown-item>
                  <el-dropdown-item>
                    <el-icon>
                      <Message />
                    </el-icon> 更改语言
                  </el-dropdown-item>
                  <el-dropdown-item class="social-icons-row">
                    <el-icon>
                      <Share />
                    </el-icon>
                    <el-icon>
                      <ChatDotRound />
                    </el-icon>
                    <el-icon>
                      <Phone />
                    </el-icon>
                    <el-icon>
                      <Headset />
                    </el-icon>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-menu>
          <el-button type="primary" class="join-button">加入</el-button>
        </div>
      </el-col>
    </el-row>
  </el-header>
</template>

<script lang="ts" setup>
import {
  ElHeader,
  ElRow,
  ElCol,
  ElMenu,
  ElMenuItem,
  ElDropdown,
  ElDropdownMenu,
  ElDropdownItem,
  ElButton,
  ElIcon
} from 'element-plus';
import {
  MoreFilled,
  Message,
  Share,
  ChatDotRound,
  Phone,
  Headset
} from '@element-plus/icons-vue';

</script>

<style scoped>
.header {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  height: 60px;
}

.logo-text {
  font-weight: bold;
  font-size: 24px;
  color: #333;
}

.header-right-items {
  flex-grow: 1;
  display: flex;
  justify-content: flex-end;
}

.nav-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
}

.header-menu {
  border-bottom: none;
  display: flex;
  align-items: center;
  height: 100%;
  margin-right: 15px;
}

.header-menu .el-menu-item {
  height: 100%;
  line-height: 60px;
  padding: 0 15px;
}

.join-button {
  margin-left: 0; /* Adjusted for direct use within nav-container */
}

.more-dropdown .el-dropdown-link {
  cursor: pointer;
  color: #606266;
  font-size: 14px;
  display: flex;
  align-items: center;
  padding: 0 15px;
  height: 60px;
}

.more-dropdown .el-dropdown-link:hover {
  color: #409eff;
}

/* The original .join-button had margin-left: 20px; here it's removed if inside nav-container and then set on join-button */
/* if you want space, add margin-left to .join-button class */

.social-icons-row {
  display: flex;
  justify-content: space-around;
  padding: 5px 0;
}

.social-icons-row .el-icon {
  margin: 0 5px;
  font-size: 20px;
  cursor: pointer;
}
</style>
