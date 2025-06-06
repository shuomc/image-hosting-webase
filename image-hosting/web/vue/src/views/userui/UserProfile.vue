<template>
  <div class="user-profile-page">
    <header class="header">
      <div class="header-left">
        <span class="logo-text">pexels</span>
        <div class="search-bar">
          <input type="text" placeholder="搜索免费图片和视频" class="search-input">
          <button class="search-button">
            <el-icon>
              <Search />
            </el-icon>
          </button>
        </div>
      </div>
      <div class="header-right">
        <button class="header-button">探索</button>
        <button class="header-button">
          <router-link to="/licence">许可证</router-link>
        </button>
        <button class="header-button">上传 <el-icon>
            <Upload />
          </el-icon></button>
        <button class="join-button" @click="goToLogin">加入</button>
      </div>
    </header>

    <section class="profile-header"
      :style="{ backgroundImage: profileHeaderBackgroundImage ? `url(${profileHeaderBackgroundImage})` : 'none' }">
      <div class="profile-header-overlay"></div>
      <div class="profile-header-content">
        <div class="profile-avatar-wrapper">
          <div class="profile-avatar-circle" :style="{ backgroundColor: getRandomColor() }">
            <span>{{ userName.charAt(0).toUpperCase() }}</span>
          </div>
        </div>
        <h1 class="user-name">{{ userName }}</h1>
        <div class="profile-actions">
          <button class="action-button follow-button">关注</button>
          <button class="action-button">消息</button>
          <button class="action-button">捐赠</button>
        </div>
        <p class="user-bio">
          法国摄影师。Pexels 专属摄影师。开放合作。
        </p>
        <div class="profile-stats">
          <div class="stat-item">
            <strong>1.6 M</strong>
            <span>公开照片</span>
          </div>
          <div class="stat-item">
            <strong>20.9 K</strong>
            <span>总浏览量</span>
          </div>
          <div class="stat-item">
            <strong>0</strong>
            <span>总点赞</span>
          </div>
        </div>
      </div>
    </section>

    <nav class="profile-tabs">
      <button :class="['tab-item', { active: activeTab === 'explore' }]" @click="activeTab = 'explore'">探索</button>
      <button :class="['tab-item', { active: activeTab === 'collections' }]"
        @click="activeTab = 'collections'">收藏</button>
      <button :class="['tab-item', { active: activeTab === 'about' }]" @click="activeTab = 'about'">关于我</button>
      <button :class="['tab-item', { active: activeTab === 'followers' }]" @click="activeTab = 'followers'">关注者</button>
      <button :class="['tab-item', { active: activeTab === 'statistics' }]" @click="activeTab = 'statistics'">统计数据</button>
    </nav>

    <main class="profile-content">
      <div v-if="activeTab === 'explore'">
        <div v-if="isLoading" class="loading-message">加载图片中...</div>
        <div v-else-if="error" class="error-message">{{ error }}</div>
        <div v-else-if="userImages.length === 0" class="no-images-message">该用户还没有上传图片。</div>
        <div v-else class="image-grid masonry-container">
          <div class="image-card" v-for="image in userImages" :key="image.imageId" @click="openImageDetail(image, index)">
            <img :src="'http://localhost:9000' + image.minioUrl" :alt="image.fileName" class="image-thumbnail">
            <div class="image-info">
              <span>作者： {{ image.authorName || '未知作者' }}</span>
            </div>
          </div>
        </div>

        <ImageDetailComponent v-model="isDetailDialogVisible" :image="selectedImage" :images="userImages"
          :initial-index="selectedImageIndex" @navigate="handleImageNavigation" />
      </div>
      <div v-else-if="activeTab === 'statistics'">
        <EchartDataComponent />
      </div>
      <div v-else class="placeholder-content">
        <p>此区域功能正在开发中...</p>
      </div>
    </main>

    <footer class="footer-placeholder">
      <FooterComponent />
    </footer>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router'; // Import useRouter
import axios from 'axios';
import { Search, Upload } from '@element-plus/icons-vue';
import ImageDetailComponent from './components/ImageDetailComponent.vue';
import FooterComponent from './components/FooterComponent.vue';
import EchartDataComponent from './components/EchartDataComponent.vue';

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
}

// Define UserInfo interface
interface UserInfo {
  userId: string;
  userName: string;
  userEmail: string;
  userRole: string;
}

const API_BASE_URL = 'http://localhost:8080';

const route = useRoute();
const router = useRouter(); // Get the router instance
const currentUserId = ref<string>('');
const userName = ref<string>('加载中...');
const userImages = ref<Image[]>([]);
const activeTab = ref('explore');
const isLoading = ref(false);
const error = ref<string | null>(null);

const profileHeaderBackgroundImage = ref<string | null>(null); // New ref for background image

const userCache = new Map<string, UserInfo>();

// Function to generate a random color for the avatar background
const getRandomColor = () => {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
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

const fetchUserUploadedImages = async (userId: string) => {
  isLoading.value = true;
  error.value = null;
  userImages.value = [];
  profileHeaderBackgroundImage.value = null; // Reset background image on new fetch

  try {
    const response = await axios.get(`${API_BASE_URL}/api/images/user/${userId}`);
    if (response.data && response.data.code === 200 && Array.isArray(response.data.data)) {
      const fetchedImages: Image[] = response.data.data.map((img: Image) => ({
        ...img,
        authorName: userName.value // The author of these images is the profile owner
      }));
      userImages.value = fetchedImages;
      console.log(`Fetched images for user ${userId}:`, userImages.value);

      // Randomly select one image for the background
      if (userImages.value.length > 0) {
        const randomIndex = Math.floor(Math.random() * userImages.value.length);
        profileHeaderBackgroundImage.value = 'http://localhost:9000' + userImages.value[randomIndex].minioUrl;
      }

    } else {
      error.value = response.data.msg || 'Failed to fetch user images: Unexpected response format.';
      userImages.value = [];
    }
  } catch (err) {
    console.error(`Error fetching images for user ${userId}:`, err);
    error.value = 'Failed to fetch user images. 请稍后再试。';
    userImages.value = [];
  } finally {
    isLoading.value = false;
  }
};

// 图片详情模态框相关状态
const isDetailDialogVisible = ref(false);
const selectedImage = ref<Image | null>(null);
const selectedImageIndex = ref(0);

// 打开图片详情的方法
const openImageDetail = (image: Image, index: number) => {
  selectedImage.value = image;
  selectedImageIndex.value = index;
  isDetailDialogVisible.value = true;
};

// 处理图片详情模态框内部的导航事件
const handleImageNavigation = (newIndex: number) => {
  selectedImageIndex.value = newIndex;
  selectedImage.value = userImages.value[newIndex]; // 更新 selectedImage
};

const loadUserProfile = async (userId: string) => {
  if (!userId) {
    userName.value = '用户ID缺失';
    error.value = '用户ID缺失，无法加载用户资料。';
    return;
  }

  const userInfo = await fetchUserInfo(userId);
  if (userInfo) {
    userName.value = userInfo.userName;
  } else {
    userName.value = '未知用户';
    error.value = '无法获取用户信息。';
    return;
  }

  await fetchUserUploadedImages(userId);
};

// Navigate to login page
const goToLogin = () => {
  router.push('/auth/login');
};

onMounted(() => {
  currentUserId.value = route.params.userId as string;
  loadUserProfile(currentUserId.value);
});

watch(() => route.params.userId, (newUserId) => {
  if (newUserId && newUserId !== currentUserId.value) {
    currentUserId.value = newUserId as string;
    loadUserProfile(currentUserId.value);
  }
});
</script>

<style scoped>
/* Basic Layout Styles - designed to mimic the provided image without Element Plus components */

.user-profile-page {
  font-family: Arial, sans-serif;
  color: #333;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #fff;
  border-bottom: 1px solid #eee;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  position: sticky;
  /* Make it sticky */
  top: 0;
  z-index: 1000;
  /* Ensure it's above other content */
}

.header-left {
  display: flex;
  align-items: center;
}

.logo-text {
  font-weight: bold;
  font-size: 24px;
  color: #333;
  margin-right: 20px;
}

.search-bar {
  display: flex;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  height: 40px;
}

.search-input {
  border: none;
  padding: 0 15px;
  font-size: 16px;
  outline: none;
  flex-grow: 1;
}

.search-button {
  background-color: #f5f7fa;
  border: none;
  padding: 0 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #606266;
}

.search-button:hover {
  background-color: #e4e6eb;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-button {
  background: none;
  border: none;
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 5px;
}

.header-button:hover {
  color: #409eff;
}

.join-button {
  background-color: #00b05b;
  color: #fff;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

.join-button:hover {
  background-color: #009e52;
}

/* Profile Header Section with Background Image */
.profile-header {
  position: relative;
  /* Needed for overlay */
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  background-color: #f8f8f8;
  /* Fallback color */
  background-size: cover;
  background-position: center;
  border-bottom: 1px solid #eee;
  text-align: center;
  color: #fff;
  /* Text color over background */
}

.profile-header-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  /* Dark overlay */
  backdrop-filter: blur(8px);
  /* Apply blur */
  -webkit-backdrop-filter: blur(8px);
  /* For Safari */
  z-index: 1;
  /* Below content, above background image */
}

.profile-header-content {
  position: relative;
  z-index: 2;
  /* Ensure content is above overlay */
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  /* Ensure content takes full width for centering */
}

/* Author Avatar Circle */
.profile-avatar-wrapper {
  margin-bottom: 20px;
}

.profile-avatar-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 50px;
  /* Larger font for initial */
  font-weight: bold;
  color: #fff;
  border: 4px solid rgba(255, 255, 255, 0.8);
  /* White border for contrast */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  /* Stronger shadow */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
}

.user-name {
  font-size: 38px;
  /* Larger font for name */
  font-weight: bold;
  margin-bottom: 15px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  /* Text shadow for readability */
}

.profile-actions {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.action-button {
  background-color: rgba(255, 255, 255, 0.2);
  /* Semi-transparent white */
  border: 1px solid rgba(255, 255, 255, 0.5);
  /* White border */
  padding: 4px 25px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  color: #fff;
  /* White text */
  font-weight: 500;
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.action-button:hover {
  background-color: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.8);
}

.follow-button {
  background-color: #00b05b;
  padding: 12px 25px;
  margin: 1px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  color: #fff;
  /* White text */
  font-weight: 500;
}

.follow-button:hover {
  background-color: #009e52;
}

.user-bio {
  font-size: 16px;
  color: #fff;
  max-width: 600px;
  margin-bottom: 30px;
  line-height: 1.6;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.4);
}

.profile-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  font-size: 14px;
  color: #fff;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.4);
}

.stat-item strong {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

/* Tabs Section */
.profile-tabs {
  display: flex;
  justify-content: center;
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
  margin-bottom: 20px;
  position: sticky;
  /* Make it sticky */
  top: 60px;
  /* Below the main header */
  z-index: 999;
  /* Below the main header, above content */
}

.tab-item {
  background: none;
  border: none;
  padding: 15px 25px;
  cursor: pointer;
  font-size: 16px;
  color: #606266;
  position: relative;
}

.tab-item:hover {
  color: #409eff;
}

.tab-item.active {
  color: #409eff;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background-color: #409eff;
}

/* Content Section (Image Grid for 'explore') */
.profile-content {
  flex-grow: 1;
  padding: 0 20px 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.loading-message,
.error-message,
.no-images-message,
.placeholder-content {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #777;
}

.image-grid {
  column-count: 3;
  /* For masonry effect */
  column-gap: 20px;
}

.image-card {
  margin-bottom: 20px;
  break-inside: avoid-column;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: inline-block;
  cursor: pointer;
  width: 100%;
}

.image-thumbnail {
  width: 100%;
  height: auto;
  display: block;
  object-fit: cover;
}

.image-info {
  padding: 10px;
  font-size: 14px;
  color: #606266;
  text-align: right;
  background-color: #f9f9f9;
}

/* Footer Placeholder */
.footer-placeholder {
  margin-top: 40px;
  padding: 20px;
  text-align: center;
  background-color: #f5f5f5;
  border-top: 1px solid #eee;
  color: #777;
}
</style>
