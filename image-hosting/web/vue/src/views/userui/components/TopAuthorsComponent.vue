<template>
  <div class="top-authors-page">
    <h1 class="page-title">社区热门作者</h1>
    <p class="page-subtitle">最近 4 周内添加的图片和视频获得浏览量最多的会员</p>

    <div class="filter-sort-section">
      <el-button-group class="filter-group">
        <el-button :type="sortBy === 'views' ? 'primary' : 'default'" @click="sortBy = 'views'">浏览量最多</el-button>
        <el-button :type="sortBy === 'active' ? 'primary' : 'default'" @click="sortBy = 'active'">最活跃</el-button>
      </el-button-group>
      <el-dropdown trigger="click" @command="handleSortOrderChange">
        <el-button>
          最近的 <el-icon class="el-icon--right"><ArrowDown /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="latest">最近的</el-dropdown-item>
            <el-dropdown-item command="oldest">最早的</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <p v-if="isLoadingAuthors" class="loading-message">加载作者信息...</p>
    <p v-if="errorAuthors" class="error-message">{{ errorAuthors }}</p>

    <div v-if="!isLoadingAuthors && !errorAuthors && sortedAndFilteredAuthors.length > 0" class="authors-grid-container">
      <el-row :gutter="40" v-for="(author, index) in sortedAndFilteredAuthors" :key="author.userId" class="author-row">
        <el-col :span="10" class="author-info-col">
          <div class="author-card-left">
            <div class="author-rank">{{ index + 1 }}</div>
            <div class="author-avatar" :style="{ backgroundColor: getRandomColor() }">
              <span>{{ author.userName ? author.userName.charAt(0).toUpperCase() : 'U' }}</span>
            </div>
            <div class="author-details">
              <div class="author-name" @click="goToUserProfile(author.userId)">{{ author.userName || '未知作者' }}</div>
              <div class="author-stats">
                <el-icon><View /></el-icon> {{ formatNumber(author.totalViews || 0) }} 次浏览
              </div>
              <div class="author-actions">
                <el-button type="primary" size="small" class="follow-button">关注</el-button>
                <el-button link class="message-button">
                  <el-icon><Message /></el-icon>
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="14" class="author-images-col">
          <div class="author-images-grid">
            <div v-for="image in author.latestImages" :key="image.imageId" class="author-image-wrapper">
              <img :src="minioBaseUrl + image.minioUrl" :alt="image.fileName" class="author-thumbnail" @click="openImageDialog(image)">
            </div>
            <div v-if="author.imageCount > author.latestImages.length" class="view-all-media-card" @click="goToUserProfile(author.userId)">
                <span class="view-all-text">+{{ author.imageCount - author.latestImages.length }}</span>
                <span class="view-all-subtext">查看所有媒体</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <p v-else-if="!isLoadingAuthors && !errorAuthors">暂无作者数据。</p>

    <ImageDetailComponent
      v-model="imageDialogVisible"
      :image="selectedImage"
      :images="tempAllImages"
      :initial-index="0"
      @close="imageDialogVisible = false"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import { ElMessage, ElDropdown, ElDropdownMenu, ElDropdownItem, ElButton, ElButtonGroup, ElIcon, ElCard, ElRow, ElCol } from 'element-plus';
import { useRouter } from 'vue-router';
import { Search, ArrowDown, View, Message } from '@element-plus/icons-vue';
import ImageDetailComponent from '@/views/userui/components/ImageDetailComponent.vue';

const API_BASE_URL = 'http://localhost:8080';
const minioBaseUrl = 'http://localhost:9000';
const router = useRouter();

interface UserInfo {
  userId: string;
  userName: string;
  userEmail: string;
  userRole: string;
}

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

interface AuthorData extends UserInfo {
  imageCount: number;
  latestImages: Image[];
  totalViews: number;
}

const allAuthors = ref<AuthorData[]>([]);
const isLoadingAuthors = ref(false);
const errorAuthors = ref<string | null>(null);
const sortBy = ref<'views' | 'active'>('views');
const sortOrder = ref<'latest' | 'oldest'>('latest');

const imageDialogVisible = ref(false);
const selectedImage = ref<Image | null>(null);
const tempAllImages = ref<Image[]>([]);

const fetchUsers = async (): Promise<UserInfo[]> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/user/getUserList`);
    if (response.data && response.data.code === 200 && Array.isArray(response.data.data)) {
      return response.data.data;
    } else {
      console.warn('Failed to fetch user list:', response.data.msg);
      return [];
    }
  } catch (err) {
    console.error('Error fetching user list:', err);
    ElMessage.error('获取用户列表失败。');
    return [];
  }
};

const fetchImagesAndStatsByUserId = async (userId: string): Promise<{ images: Image[]; totalViews: number }> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/images/user/${userId}`);
    if (response.data && response.data.code === 200 && Array.isArray(response.data.data)) {
      let totalViewsForUser = 0;
      const images: Image[] = response.data.data;
      return { images: images, totalViews: totalViewsForUser };
    } else {
      console.warn(`Failed to fetch images for user ${userId}:`, response.data.msg);
      return { images: [], totalViews: 0 };
    }
  } catch (err) {
    console.error(`Error fetching images for user ${userId}:`, err);
    return { images: [], totalViews: 0 };
  }
};

const fetchAllAuthorsData = async () => {
  isLoadingAuthors.value = true;
  errorAuthors.value = null;
  try {
    const userList = await fetchUsers();
    const fetchedAuthorsData: AuthorData[] = [];

    for (const user of userList) {
      const { images: userImages, totalViews } = await fetchImagesAndStatsByUserId(user.userId);
      fetchedAuthorsData.push({
        ...user,
        imageCount: userImages.length,
        latestImages: userImages.slice(0, 3), // Get top 3 images for preview
        totalViews: totalViews || Math.floor(Math.random() * 50000000) + 100000 // Placeholder for totalViews
      });
    }
    allAuthors.value = fetchedAuthorsData;
    console.log('Fetched all authors data:', allAuthors.value);

  } catch (err) {
    errorAuthors.value = '获取作者数据失败。请检查后端服务。';
    console.error('Error fetching all authors data:', err);
  } finally {
    isLoadingAuthors.value = false;
  }
};

const sortedAndFilteredAuthors = computed(() => {
  let filtered = [...allAuthors.value];

  if (sortBy.value === 'views') {
    filtered.sort((a, b) => b.totalViews - a.totalViews);
  } else {
    filtered.sort((a, b) => b.imageCount - a.imageCount);
  }
  return filtered;
});

const handleSortOrderChange = (command: 'latest' | 'oldest') => {
  sortOrder.value = command;
  ElMessage.info(`排序方式设置为: ${command === 'latest' ? '最近的' : '最早的'}`);
};

const openImageDialog = (image: Image) => {
  selectedImage.value = image;
  tempAllImages.value = [image];
  imageDialogVisible.value = true;
};

const goToUserProfile = (userId: string) => {
  if (userId) {
    router.push(`/user/${userId}`);
  } else {
    ElMessage.warning('无法跳转：用户ID缺失。');
  }
};

const getRandomColor = () => {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
};

const formatNumber = (num: number): string => {
  if (num === undefined || num === null) return 'N/A';
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + ' 百万';
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + ' 千';
  }
  return num.toString();
};

onMounted(() => {
  fetchAllAuthorsData();
});
</script>

<style scoped>
.top-authors-page {
  padding: 40px 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-title {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  text-align: center;
}

.page-subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 40px;
  text-align: center;
}

.filter-sort-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 0 10px;
}

.filter-group .el-button {
  border-radius: 20px;
  padding: 8px 20px;
  font-weight: 500;
}

.filter-group .el-button.is-active,
.filter-group .el-button.is-active:hover {
  background-color: #00b05b;
  border-color: #00b05b;
  color: #fff;
}

.filter-group .el-button:hover:not(.is-active) {
  color: #00b05b;
  border-color: #00b05b;
  background-color: #f0f9eb;
}

.loading-message, .error-message {
  text-align: center;
  color: #606266;
  margin-top: 20px;
}

.error-message {
  color: #f56c6c;
}

.authors-grid-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.author-row {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  padding: 20px;
  align-items: center;
}

.author-info-col {
  display: flex;
  justify-content: center;
}

.author-card-left {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 400px;
}

.author-rank {
  font-size: 36px;
  font-weight: bold;
  color: #909399;
  margin-right: 25px;
  width: 60px;
  text-align: right;
  flex-shrink: 0;
}

.author-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 32px;
  font-weight: bold;
  color: #fff;
  flex-shrink: 0;
  margin-right: 20px;
  border: 3px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.author-details {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.author-name {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.author-name:hover {
  color: #00b05b;
}

.author-stats {
  font-size: 16px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 10px;
}

.author-actions {
  display: flex;
  gap: 10px;
}

.follow-button {
  background-color: #00b05b;
  color: #fff;
  border: none;
  padding: 8px 20px;
  border-radius: 5px;
  font-weight: bold;
  white-space: nowrap;
}

.follow-button:hover {
  background-color: #009e52;
}

.message-button {
  color: #606266;
  font-size: 20px;
  padding: 8px;
  border-radius: 5px;
}
.message-button:hover {
  color: #00b05b;
  background-color: #f0f9eb;
}

.author-images-col {
  /* This column needs to allow its content to take up available space,
     but we'll control the image grid internally. */
}

.author-images-grid {
  display: grid;
  /* Adjust grid-template-columns to control how many images per row.
     To make them smaller and fit more in a row, reduce the 'minmax' value
     or explicitly define smaller fractional units.
     For exactly 3 images + 1 view-all card in a row, with equal width for all: */
  grid-template-columns: repeat(4, 1fr); /* Changed from 3 to 4 to include "查看所有媒体" as a grid item */
  gap: 10px;
  align-items: center; /* Vertically align items in the grid */
  height: 100%;
}

.author-image-wrapper {
  aspect-ratio: 1 / 1;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.author-image-wrapper:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.author-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.view-all-media-card {
  background-color: #f0f2f5;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  cursor: pointer;
  padding: 15px;
  aspect-ratio: 1 / 1;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: background-color 0.2s ease-in-out;
}

.view-all-media-card:hover {
  background-color: #e0e2e5;
}

.view-all-text {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.view-all-subtext {
  font-size: 14px;
  color: #666;
}

/* Responsive adjustments */
@media (max-width: 992px) {
  .author-row {
    flex-direction: column;
  }
  .author-info-col, .author-images-col {
    width: 100%;
  }
  .author-info-col {
    margin-bottom: 20px;
    justify-content: flex-start;
  }
  .author-card-left {
    max-width: none;
    justify-content: flex-start;
  }
  .author-images-grid {
    /* On medium screens, maybe switch to 2 columns of images + 1 view-all, or keep 4
       If you want to keep them smaller and 4 in a row, this is fine.
       If you want fewer, adjust grid-template-columns. */
    grid-template-columns: repeat(4, 1fr); /* Keeping 4 for consistency with original request */
    gap: 8px;
  }
  .author-rank {
    font-size: 28px;
    width: auto;
    text-align: left;
    margin-right: 15px;
  }
  .author-avatar {
    width: 60px;
    height: 60px;
    font-size: 24px;
  }
  .author-name {
    font-size: 20px;
  }
  .author-stats {
    font-size: 14px;
  }
  .follow-button {
    padding: 6px 12px;
    font-size: 13px;
  }
  .message-button {
    font-size: 18px;
  }
}

@media (max-width: 768px) {
  .top-authors-page {
    padding: 20px 10px;
  }
  .filter-sort-section {
    flex-direction: column;
    gap: 15px;
  }
  .filter-group {
    width: 100%;
    justify-content: center;
  }
  .filter-group .el-button {
    flex: 1;
  }
  .authors-grid-container {
    gap: 20px;
  }
  .author-row {
    padding: 15px;
  }
  .author-images-grid {
    /* On small screens, it might be better to stack them or have fewer columns.
       For a single row, you might need to make images very small or allow horizontal scrolling.
       Let's try 2 columns for images and 1 for "view all" which might wrap.
       If you truly want *one single row* regardless of screen size without wrapping,
       you'd need to use `flex-wrap: nowrap;` and potentially `overflow-x: auto;`
       on a flex container, or define very small fixed widths.
       For this example, we'll keep it responsive with wrapping if needed,
       but optimize for showing a few in a row. */
    grid-template-columns: repeat(2, 1fr) 1fr; /* 2 images + 1 view-all, might wrap */
    /* Or, if you want to force one line with scrolling, you could do:
       display: flex;
       flex-wrap: nowrap;
       overflow-x: auto;
       gap: 10px;
       .author-image-wrapper { flex-shrink: 0; width: 100px; height: 100px; } // Example fixed size
       .view-all-media-card { flex-shrink: 0; width: 100px; height: 100px; } // Example fixed size
    */
    gap: 5px;
  }
  .view-all-text {
    font-size: 24px;
  }
  .view-all-subtext {
    font-size: 12px;
  }
}
</style>
