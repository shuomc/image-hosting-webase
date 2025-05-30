<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <el-row align="middle" style="width: 100%;">
          <el-col :span="4" class="logo">
            <span class="logo-text">pexels</span>
          </el-col>

          <el-col :span="20" class="header-right-items">
            <div class="nav-container">
              <el-menu mode="horizontal" :ellipsis="false" class="header-menu">
                <el-menu-item index="explore">探索</el-menu-item>
                <el-menu-item index="license">许可证</el-menu-item>

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

      <el-main class="main-content">
        <div class="hero-section" :style="{ backgroundImage: `url(${heroBackgroundImage})` }">
          <div class="hero-overlay"></div>
          <div class="hero-content">
            <h1>才华横溢的摄影师在这里免费分享最精彩的素材图片和视频。</h1>
            <el-input v-model="searchQuery" placeholder="搜索免费图片" size="large" class="hero-search-input">
              <template #prepend>
                <el-dropdown>
                  <el-button>
                    图片<el-icon class="el-icon--right"><arrow-down /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item>图片</el-dropdown-item>
                      <el-dropdown-item>视频</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template #append>
                <el-button :icon="Search" @click="handleSearch" />
              </template>
            </el-input>
          </div>
        </div>

        <div class="tabs-section">
          <el-tabs v-model="activeTab" class="pexels-tabs">
            <el-tab-pane label="主页" name="home"></el-tab-pane>
            <el-tab-pane label="视频" name="videos"></el-tab-pane>
            <el-tab-pane label="热门作者排行榜" name="top-authors"></el-tab-pane>
            <el-tab-pane label="挑战赛" name="challenges"></el-tab-pane>
          </el-tabs>
        </div>

        <div class="image-grid-section">
          <div class="section-header">
            <h2>免费素材图片</h2>
            <el-dropdown>
              <el-button>
                人气<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>人气</el-dropdown-item>
                  <el-dropdown-item>最新</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>

          <div class="masonry-container">
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/1036324/pexels-photo-1036324.jpeg?auto=compress&cs=tinysrgb&w=1260&h=1000&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by DEF (Taller)</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/1105766/pexels-photo-1105766.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by GHI</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/15286/pexels-photo-15286.jpeg?auto=compress&cs=tinysrgb&w=1260&h=500&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by JKL (Shorter)</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/1036324/pexels-photo-1036324.jpeg?auto=compress&cs=tinysrgb&w=1260&h=1000&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by DEF (Taller)</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/1036324/pexels-photo-1036324.jpeg?auto=compress&cs=tinysrgb&w=1260&h=1000&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by DEF (Taller)</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
            <el-card shadow="hover" class="image-card">
              <img
                src="https://images.pexels.com/photos/357335/pexels-photo-357335.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                alt="Placeholder Image" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by ABC</span>
              </div>
            </el-card>
          </div>

          <div class="pagination-section">
            <el-pagination background layout="prev, pager, next" :total="1000"></el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>

  <div class="footer">
    <FooterComponent />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import FooterComponent from '@/views/userui/components/FooterComponent.vue';
import {
  Search,
  ArrowDown,
  MoreFilled,   // For the "..." icon
  Message,      // For "更改语言"
  Share,        // Example social icon
  ChatDotRound, // Example social icon
  Phone,        // Example social icon
  Headset       // Example social icon
} from '@element-plus/icons-vue';

const searchQuery = ref('');
const activeTab = ref('home');
const heroBackgroundImage = ref('https://images.pexels.com/photos/2085998/pexels-photo-2085998.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200'); // Example background image

const handleSearch = () => {
  console.log('Searching for:', searchQuery.value);
  // Implement your search logic here
};

// You would typically fetch images based on activeTab and search query
const fetchImages = () => {
  console.log('Fetching images for tab:', activeTab.value);
};

// Watch for activeTab changes to fetch new images
// import { watch } from 'vue';
// watch(activeTab, fetchImages);

</script>

<style scoped>
.common-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  height: 60px;
  /* Fixed height for header */
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
  /* 与右侧按钮保持间距 */
}

.header-menu .el-menu-item {
  height: 100%;
  line-height: 60px;
  padding: 0 15px;
}

.join-button {
  margin-left: 0;
}

.header-menu .el-menu-item {
  height: 100%;
  line-height: 60px;
  /* Match header height */
  padding: 0 15px;
  /* Add padding for spacing */
}

/* Style for the "..." dropdown trigger in the header menu */
.more-dropdown .el-dropdown-link {
  cursor: pointer;
  color: #606266;
  font-size: 14px;
  display: flex;
  align-items: center;
  padding: 0 15px;
  /* Adjust padding to match menu items */
  height: 60px;
  /* Match header height */
  /* margin-left: 15px; */
  /* Space from other menu items if needed */
}

.more-dropdown .el-dropdown-link:hover {
  color: #409eff;
  /* Element Plus primary color on hover */
}

.join-button {
  margin-left: 20px;
  /* Space from the "..." dropdown */
}

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


.main-content {
  padding: 0;
  /* Remove default padding from el-main */
  flex-grow: 1;
  /* Allow main content to take remaining space */
}

.hero-section {
  position: relative;
  height: 450px;
  /* Adjust height as needed */
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  text-align: center;
  overflow: hidden;
  /* Ensure overlay doesn't spill */
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  /* Dark overlay for text readability */
  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
  padding: 20px;
}

.hero-content h1 {
  font-size: 40px;
  margin-bottom: 30px;
  line-height: 1.3;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.hero-search-input {
  width: 100%;
  max-width: 600px;
}

.hero-search-input .el-input-group__prepend,
.hero-search-input .el-input-group__append {
  background-color: #fff;
  color: #333;
  border-color: #dcdfe6;
}

.tabs-section {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
  display: flex;
  justify-content: center;
  /* Center the tabs */
}

.pexels-tabs {
  width: 100%;
  max-width: 1200px;
  /* Max width for tabs to match content */
}

.pexels-tabs .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
  color: #606266;
}

.pexels-tabs .el-tabs__item.is-active {
  color: #409eff;
  /* Element Plus primary color */
}

.image-grid-section {
  padding: 40px 20px;
  max-width: 1200px;
  /* Max width for content */
  margin: 0 auto;
  /* Center the content */
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.masonry-container {
  column-count: 3;
  /* You can adjust the number of columns */
  column-gap: 20px;
  /* Gap between columns */
}

.image-card {
  margin-bottom: 20px;
  /* Space between cards within a column */
  break-inside: avoid-column;
  /* Prevent cards from breaking across columns */
  border-radius: 8px;
  overflow: hidden;
  display: inline-block;
  /* Essential for CSS columns to work correctly */
  width: 100%;
  /* Make each card take full width of its column */
}

.image-card .el-card__body {
  padding: 0;
}

.image-thumbnail {
  width: 100%;
  height: auto;
  /* IMPORTANT: Set height to auto to respect aspect ratio */
  object-fit: cover;
  /* Still useful for images that might not perfectly fit */
  display: block;
}

.image-info {
  padding: 10px;
  background-color: #f5f7fa;
  font-size: 14px;
  color: #606266;
  text-align: right;
}

.pagination-section {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style>
