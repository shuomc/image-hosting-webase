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
              <el-button type="primary" class="join-button">
                <router-link to="/auth/login">
                  加入
                </router-link>
              </el-button>
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
                    图片<el-icon class="el-icon--right"><ArrowDown /></el-icon>
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
            <el-tab-pane label="挑战赛" name="challenges"></el-tab-pane> </el-tabs>
        </div>

        <div v-if="activeTab === 'home'" class="image-grid-section">
          <div class="section-header">
            <h2>免费素材图片</h2>
            <el-dropdown>
              <el-button>
                人气<el-icon class="el-icon--right"><ArrowDown /></el-icon>
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
            <el-card
              shadow="hover"
              class="image-card"
              v-for="image in images"
              :key="image.imageId"
              @click="openImageDialog(image)"
            >
              <img :src="'http://localhost:9000' + image.minioUrl" :alt="image.fileName" class="image-thumbnail">
              <div class="image-info">
                <span>Photo by {{ image.authorName || '未知作者' }}</span>
              </div>
            </el-card>
            <p v-if="images.length === 0 && !isLoading">No images to display.</p>
            <p v-if="isLoading">Loading images...</p>
            <p v-if="error">{{ error }}</p>
          </div>

          <div class="pagination-section">
            <el-pagination background layout="prev, pager, next" :total="1000"></el-pagination>
          </div>
        </div>

        <div v-else-if="activeTab === 'top-authors'">
          <TopAuthors />
        </div>

        <div v-else-if="activeTab === 'challenges'">
          <ChallengesPage />
        </div>

        <div v-else class="placeholder-content">
          <p>此区域功能正在开发中...</p>
        </div>

      </el-main>
    </el-container>
  </div>

  <div class="footer">
    <FooterComponent />
  </div>

  <el-dialog
    v-model="imageDialogVisible"
    :show-close="false"
    class="image-dialog-custom"
    width="80%"
    align-center
  >
    <div class="dialog-content-wrapper" v-if="selectedImage">
      <div class="dialog-header-custom">
        <el-button :icon="CloseBold" circle @click="imageDialogVisible = false" class="dialog-close-button"></el-button>
        <div class="dialog-header-right">
          <el-tooltip
            class="box-item"
            effect="dark"
            :content="selectedImage.userId"
            placement="bottom"
            v-if="selectedImage.userId"
          >
            <div class="author-info" @click="goToUserProfile(selectedImage.userId)">
              <div class="avatar-circle" :style="{ backgroundColor: getRandomColor() }">
                <span>{{ selectedImage.authorName ? selectedImage.authorName.charAt(0).toUpperCase() : 'U' }}</span>
              </div>
              <div class="author-text">
                <span class="author-name">{{ selectedImage.authorName || 'Unknown Author' }}</span>
                <span class="follow-text">关注 · 捐赠</span>
              </div>
            </div>
          </el-tooltip>
          <div class="header-actions">
            <el-button class="header-action-button" @click="downloadImage(selectedImage)">
              <el-icon><Download /></el-icon>
            </el-button>
            <el-button class="header-action-button">
              <el-icon><Star /></el-icon>
            </el-button>
            <el-button class="header-action-button">
              <el-icon><CirclePlus /></el-icon>
            </el-button>
            <el-button type="primary" class="download-button" @click="downloadImage(selectedImage)">免费下载</el-button>
            <el-dropdown trigger="hover">
              <span class="el-dropdown-link-more">
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>下载为JPG</el-dropdown-item>
                  <el-dropdown-item>快速保存为PNG</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <div class="dialog-body-custom">
        <el-row :gutter="20" style="width: 100%; height: 100%;">
          <el-col :span="18" class="image-display-area">
            <img :src="'http://localhost:9000' + selectedImage.minioUrl" :alt="selectedImage.fileName" class="full-image">
            <div class="navigation-arrows">
              <el-button :icon="ArrowLeftBold" circle class="nav-arrow left"></el-button>
              <el-button :icon="ArrowRightBold" circle class="nav-arrow right"></el-button>
            </div>
          </el-col>
          <el-col :span="6" class="image-details-sidebar">
            <div class="sidebar-section download-options">
              <h3>免费下载</h3>
              <el-select v-model="selectedResolution" placeholder="选择尺寸" class="resolution-select">
                <el-option label="原始尺寸" value="original"></el-option>
                <el-option label="大尺寸" value="large"></el-option>
                <el-option label="中尺寸" value="medium"></el-option>
                <el-option label="小尺寸" value="small"></el-option>
              </el-select>
              <el-button type="primary" class="sidebar-download-button" @click="downloadImage(selectedImage)">免费下载</el-button>
            </div>

            <div class="sidebar-section photo-info">
              <h3>图片信息</h3>
              <div class="info-item">
                <el-icon><Camera /></el-icon>
                <span>拍摄于： {{ selectedImage.createTime ? new Date(selectedImage.createTime).toLocaleDateString() : '未知日期' }}</span>
              </div>
              <div class="info-item">
                <el-icon><FullScreen /></el-icon>
                <span>尺寸： {{ selectedImage.size ? (selectedImage.size / 1024 / 1024).toFixed(2) + ' MB' : '未知大小' }}</span>
              </div>
              <div class="info-item">
                <el-icon><InfoFilled /></el-icon>
                <span>许可证：免费用于个人和商业用途</span>
              </div>
            </div>

            <div class="sidebar-section description-section" v-if="selectedImage.description">
              <h3>描述</h3>
              <p>{{ selectedImage.description }}</p>
            </div>

            <div class="sidebar-section tags">
              <h3>相关标签</h3>
              <div class="tag-list">
                <el-tag size="small" v-for="tag in getTags(selectedImage.description)" :key="tag">{{ tag }}</el-tag>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div class="dialog-footer-custom">
        <div class="footer-left">
          <el-button link>
            <el-icon><Share /></el-icon> 分享
          </el-button>
          <el-button link>
            <el-icon><Flag /></el-icon> 举报
          </el-button>
        </div>
        <div class="footer-right">
          <el-button link>
            <el-icon><Collection /></el-icon> 添加到合集
          </el-button>
          <el-button link>
            <el-icon><EditPen /></el-icon>
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import FooterComponent from '@/views/userui/components/FooterComponent.vue';
import TopAuthors from '@/views/userui/components/TopAuthorsComponent.vue';
import ChallengesPage from '@/views/userui/components/ChallengesComponent.vue'; // 导入 ChallengesPage 组件
import { ElMessage } from 'element-plus';
import {
  Search,
  ArrowDown,
  MoreFilled,
  Message,
  Share,
  ChatDotRound,
  Phone,
  Headset,
  CloseBold,
  Download,
  Star,
  CirclePlus,
  ArrowLeftBold,
  ArrowRightBold,
  Camera,
  FullScreen,
  InfoFilled,
  Flag,
  Collection,
  EditPen
} from '@element-plus/icons-vue';

// Define Image interface
interface Image {
  imageId: string;
  minioUrl: string;
  fileName: string;
  userId: string;
  contentType: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime?: string;
  createTime?: string;
  authorName?: string;
  isToggling?: boolean;
  isDeleting?: boolean;
}

// Define UserInfo interface
interface UserInfo {
  userId: string;
  userName: string;
  userEmail: string;
  userRole: string;
}

const API_BASE_URL = 'http://localhost:8080';
const router = useRouter();

const searchQuery = ref('');
const activeTab = ref('home');
const heroBackgroundImage = ref('https://images.pexels.com/photos/2085998/pexels-photo-2085998.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200'); //

const images = ref<Image[]>([]);
const isLoading = ref(false);
const error = ref<string | null>(null);

// Dialog specific refs
const imageDialogVisible = ref(false);
const selectedImage = ref<Image | null>(null);
const selectedResolution = ref('original');

// Simple in-memory cache for user info
const userCache = new Map<string, UserInfo>();

const handleSearch = () => {
  console.log('Searching for:', searchQuery.value);
};

const fetchUserInfo = async (userId: string): Promise<UserInfo | null> => {
  if (userCache.has(userId)) {
    return userCache.get(userId)!;
  }

  try {
    const response = await axios.get(`${API_BASE_URL}/api/user/getUserById`, {
      params: { userId }
    });
    if (response.data && response.data.code === 200 && response.data.data) {
      const userInfo: UserInfo = response.data.data;
      userCache.set(userId, userInfo);
      return userInfo;
    } else {
      console.warn(`Failed to fetch user info for userId: ${userId}`, response.data.msg);
      return null;
    }
  } catch (err) {
    console.error(`Error fetching user info for userId: ${userId}`, err);
    return null;
  }
};

const fetchImages = async () => {
  isLoading.value = true;
  error.value = null;
  try {
    const response = await axios.get(`${API_BASE_URL}/api/images/public`);
    if (response.data && response.data.code === 200 && Array.isArray(response.data.data)) {
      const fetchedImages: Image[] = await Promise.all(response.data.data.map(async (img: Image) => {
        const userInfo = await fetchUserInfo(img.userId);
        return {
          ...img,
          authorName: userInfo ? userInfo.userName : '未知作者',
          createTime: img.createTime || img.uploadTime
        };
      }));
      images.value = fetchedImages;
      console.log('Fetched and enriched images:', images.value);
    } else {
      error.value = response.data.msg || 'Failed to fetch images: Unexpected response format.';
      images.value = [];
    }
  } catch (err) {
    console.error('Error fetching images:', err);
    error.value = 'Failed to fetch images. Please try again later.';
    images.value = [];
  } finally {
    isLoading.value = false;
  }
};

const openImageDialog = (image: Image) => {
  selectedImage.value = image;
  imageDialogVisible.value = true;
};

const getRandomColor = () => {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
};

const getTags = (description: string | null) => {
  if (!description) return [];
  return description.split(' ').filter(tag => tag.length > 0);
};

const downloadImage = (image: Image | null) => {
  if (!image || !image.imageId || !image.fileName) {
    ElMessage.warning('图片信息不完整，无法下载。');
    return;
  }

  const downloadUrl = `${API_BASE_URL}/api/images/minio/${image.imageId}`;

  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName);

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

const goToUserProfile = (userId: string | undefined) => {
  if (userId) {
    console.log(`Navigating to user profile: /user/${userId}`);
    router.push(`/user/${userId}`);
  } else {
    ElMessage.warning('无法跳转：用户ID缺失。');
  }
};

onMounted(() => {
  fetchImages();
});
</script>

<style scoped>
/* Existing styles remain */
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
  margin-left: 0;
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

.join-button {
  margin-left: 20px;
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
  flex-grow: 1;
}

.hero-section {
  position: relative;
  height: 450px;
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  text-align: center;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
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
}

.pexels-tabs {
  width: 100%;
  max-width: 1200px;
}

.pexels-tabs .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
  color: #606266;
}

.pexels-tabs .el-tabs__item.is-active {
  color: #409eff;
}

.image-grid-section {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
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
  column-gap: 20px;
}

.image-card {
  margin-bottom: 20px;
  break-inside: avoid-column;
  border-radius: 8px;
  overflow: hidden;
  display: inline-block;
  width: 100%;
  cursor: pointer;
}

.image-card .el-card__body {
  padding: 0;
}

.image-thumbnail {
  width: 100%;
  height: auto;
  object-fit: cover;
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

/* Image Dialog Custom Styles */
.image-dialog-custom {
  --el-dialog-bg-color: transparent;
  --el-dialog-box-shadow: none;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 12px;
}

.image-dialog-custom .el-dialog__body {
  padding: 0 !important;
  height: calc(100vh - 150px);
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  background-color: white;
  border: 1px solid #ccc;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.image-dialog-custom .el-dialog__header {
  padding: 0 !important;
  display: none;
}


.dialog-content-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.dialog-header-custom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: rgba(0, 0, 0, 0.05);
  color: #333;
  z-index: 10;
  position: relative;
  width: 100%;
  box-sizing: border-box;
  border-bottom: 1px solid #eee;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

.dialog-close-button {
  background: transparent;
  border: none;
  font-size: 24px;
  color: #606266;
  opacity: 0.7;
}
.dialog-close-button:hover {
  opacity: 1;
  background-color: rgba(0, 0, 0, 0.05);
}

.dialog-header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer; /* Indicate clickable */
}

.avatar-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  flex-shrink: 0;
}

.author-text {
  display: flex;
  flex-direction: column;
  color: #333;
}

.author-name {
  font-weight: bold;
  font-size: 16px;
}

.follow-text {
  font-size: 12px;
  opacity: 0.8;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-action-button {
  background-color: #f0f2f5;
  border: none;
  color: #606266;
  padding: 8px;
  border-radius: 5px;
}

.header-action-button .el-icon {
  font-size: 18px;
}

.header-action-button:hover {
  background-color: #e4e6eb;
}

.download-button {
  background-color: #00b05b;
  border: none;
  color: #fff;
  padding: 8px 15px;
  border-radius: 5px;
  font-weight: bold;
}

.download-button:hover {
  background-color: #009e52;
}

.el-dropdown-link-more {
  cursor: pointer;
  color: #606266;
  opacity: 0.7;
  font-size: 18px;
}

.el-dropdown-link-more:hover {
  opacity: 1;
}

.dialog-body-custom {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  padding: 20px;
  box-sizing: border-box;
}

.image-display-area {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  height: 100%;
}

.full-image {
  max-width: 100%;
  max-height: calc(100vh - 300px);
  object-fit: contain;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.navigation-arrows {
  position: absolute;
  top: 50%;
  width: 100%;
  display: flex;
  justify-content: space-between;
  transform: translateY(-50%);
  padding: 0 10px;
}

.nav-arrow {
  background-color: rgba(0, 0, 0, 0.3);
  color: #fff;
  border: none;
  font-size: 24px;
}
.nav-arrow:hover {
  background-color: rgba(0, 0, 0, 0.5);
}

.image-details-sidebar {
  background-color: #fff;
  color: #333;
  padding: 20px;
  border-radius: 8px;
  height: 100%;
  max-height: calc(100vh - 300px);
  overflow-y: auto;
  margin-left: auto;
  box-sizing: border-box;
}


.sidebar-section {
  margin-bottom: 25px;
}

.sidebar-section h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.resolution-select {
  width: 100%;
  margin-bottom: 15px;
}

.sidebar-download-button {
  width: 100%;
  background-color: #00b05b;
  border: none;
  font-weight: bold;
  padding: 10px 0;
}
.sidebar-download-button:hover {
  background-color: #009e52;
}

.photo-info .info-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #555;
}

.info-item .el-icon {
  font-size: 16px;
  color: #777;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.el-tag {
  background-color: #e6e6e6;
  color: #555;
  border-radius: 4px;
}

.description-section p {
  font-size: 14px;
  line-height: 1.5;
  color: #555;
}

.dialog-footer-custom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: rgba(0, 0, 0, 0.05);
  color: #333;
  width: 100%;
  box-sizing: border-box;
  z-index: 10;
  border-top: 1px solid #eee;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
}

.dialog-footer-custom .el-button.is-link {
  color: #606266;
  opacity: 0.7;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.dialog-footer-custom .el-button.is-link:hover {
  opacity: 1;
}

.footer-left, .footer-right {
  display: flex;
  gap: 20px;
}
</style>
