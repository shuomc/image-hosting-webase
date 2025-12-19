<template>
  <div class="py-12 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto transition-colors duration-300">
    <!-- Header Section -->
    <div class="text-center mb-16">
      <h1 class="text-4xl md:text-5xl font-bold text-slate-900 dark:text-white mb-4 tracking-tight">
        社区热门作者
      </h1>
      <p class="text-lg text-slate-600 dark:text-slate-400 max-w-2xl mx-auto leading-relaxed">
        发现并关注那些为社区贡献了最优质内容的创作者们。
      </p>
    </div>

    <!-- Filter & Sort Section -->
    <div class="flex flex-col md:flex-row justify-between items-center gap-6 mb-12">
      <div class="bg-slate-100 dark:bg-slate-800/50 p-1 rounded-xl flex items-center gap-1 border border-slate-200 dark:border-slate-700">
        <button 
          @click="sortBy = 'views'"
          class="px-6 py-2 rounded-xl text-sm font-medium transition-all duration-200"
          :class="sortBy === 'views' 
            ? 'bg-white dark:bg-slate-700 shadow-sm text-indigo-600 dark:text-indigo-400' 
            : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
        >
          浏览量最多
        </button>
        <button 
          @click="sortBy = 'active'"
          class="px-6 py-2 rounded-xl text-sm font-medium transition-all duration-200"
          :class="sortBy === 'active' 
            ? 'bg-white dark:bg-slate-700 shadow-sm text-indigo-600 dark:text-indigo-400' 
            : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
        >
          最活跃
        </button>
      </div>

      <div class="relative group">
        <button class="flex items-center gap-2 px-5 py-2.5 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-xl text-sm font-medium text-slate-700 dark:text-slate-300 hover:border-indigo-500 transition-all shadow-sm">
          <span>{{ sortOrder === 'latest' ? '最近的' : '最早的' }}</span>
          <ChevronDownIcon class="w-4 h-4" />
        </button>
        <div class="absolute right-0 mt-2 w-40 bg-white dark:bg-slate-800 rounded-xl shadow-xl border border-slate-100 dark:border-slate-700 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-30">
          <div class="p-1">
            <button @click="sortOrder = 'latest'" class="w-full text-left px-3 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-indigo-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400 rounded-lg">最近的</button>
            <button @click="sortOrder = 'oldest'" class="w-full text-left px-3 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-indigo-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400 rounded-lg">最早的</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading & Error States -->
    <div v-if="isLoadingAuthors" class="py-32 flex flex-col items-center justify-center">
      <div class="animate-spin rounded-full h-12 w-12 border-4 border-indigo-100 border-t-indigo-600 mb-4"></div>
      <p class="text-slate-500 dark:text-slate-400 animate-pulse">加载创作者数据...</p>
    </div>

    <div v-else-if="errorAuthors" class="py-20 text-center">
      <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-red-50 dark:bg-red-900/20 text-red-500 mb-4">
        <InformationCircleIcon class="w-8 h-8" />
      </div>
      <h3 class="text-lg font-bold text-slate-900 dark:text-white">出错了</h3>
      <p class="text-slate-500 dark:text-slate-400 mt-2">{{ errorAuthors }}</p>
      <button @click="fetchAllAuthorsData" class="mt-6 px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors">重试</button>
    </div>

    <!-- Authors List -->
    <div v-else-if="sortedAndFilteredAuthors.length > 0" class="space-y-8">
      <div 
        v-for="(author, index) in sortedAndFilteredAuthors" 
        :key="author.userId"
        class="bg-white dark:bg-slate-800 rounded-3xl p-6 md:p-8 border border-slate-100 dark:border-slate-700 shadow-sm hover:shadow-xl transition-all duration-300 group"
      >
        <div class="flex flex-col lg:flex-row gap-8 items-center">
          <!-- Left: Author Info -->
          <div class="flex items-center gap-6 w-full lg:w-1/3">
            <div class="text-4xl md:text-5xl font-black text-slate-200 dark:text-slate-700/50 w-16 shrink-0 italic">
              {{ String(index + 1).padStart(2, '0') }}
            </div>
            
            <div 
              class="w-20 h-20 md:w-24 md:h-24 rounded-full flex items-center justify-center text-white text-3xl font-bold shadow-inner shrink-0 cursor-pointer hover:scale-105 transition-transform"
              :style="{ backgroundColor: getRandomColor() }"
              @click="goToUserProfile(author.userId)"
            >
              {{ author.userName ? author.userName.charAt(0).toUpperCase() : 'U' }}
            </div>

            <div class="flex-1 min-w-0">
              <h3 
                class="text-xl md:text-2xl font-bold text-slate-900 dark:text-white truncate hover:text-indigo-600 dark:hover:text-indigo-400 cursor-pointer transition-colors"
                @click="goToUserProfile(author.userId)"
              >
                {{ author.userName || '未知作者' }}
              </h3>
              <div class="flex items-center gap-4 mt-2 text-slate-500 dark:text-slate-400 text-sm">
                <div class="flex items-center gap-1">
                  <EyeIcon class="w-4 h-4" />
                  <span>{{ formatNumber(author.totalViews || 0) }}</span>
                </div>
                <div class="flex items-center gap-1">
                  <PhotoIcon class="w-4 h-4" />
                  <span>{{ author.imageCount }} 作品</span>
                </div>
              </div>
              <div class="flex gap-3 mt-5">
                <button class="px-5 py-2 bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-bold rounded-lg transition-colors shadow-lg shadow-indigo-500/20">
                  关注
                </button>
                <button class="p-2 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 rounded-lg transition-colors">
                  <ChatBubbleLeftEllipsisIcon class="w-5 h-5" />
                </button>
              </div>
            </div>
          </div>

          <!-- Right: Image Preview Grid -->
          <div class="w-full lg:w-2/3">
            <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
              <div 
                v-for="image in author.latestImages" 
                :key="image.imageId"
                class="aspect-square rounded-2xl overflow-hidden bg-slate-100 dark:bg-slate-900 cursor-pointer group/img relative"
                @click="openImageDialog(image, author.userName)"
              >
                <img 
                  :src="minioBaseUrl + image.watermarkMinioUrl" 
                  :alt="image.fileName" 
                  class="w-full h-full object-cover transition-transform duration-500 group-hover/img:scale-110"
                >
                <div class="absolute inset-0 bg-black/20 opacity-0 group-hover/img:opacity-100 transition-opacity"></div>
              </div>
              
              <!-- View All Card -->
              <div 
                v-if="author.imageCount > author.latestImages.length"
                class="aspect-square rounded-2xl bg-slate-50 dark:bg-slate-900/50 border-2 border-dashed border-slate-200 dark:border-slate-700 flex flex-col items-center justify-center cursor-pointer hover:border-indigo-500 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 transition-all group/all"
                @click="goToUserProfile(author.userId)"
              >
                <span class="text-2xl font-bold text-slate-900 dark:text-white group-hover/all:text-indigo-600 dark:group-hover/all:text-indigo-400">
                  +{{ author.imageCount - author.latestImages.length }}
                </span>
                <span class="text-xs text-slate-500 dark:text-slate-400 mt-1">查看全部</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="py-32 text-center">
      <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-slate-100 dark:bg-slate-800 mb-6">
        <UserGroupIcon class="w-10 h-10 text-slate-400" />
      </div>
      <h3 class="text-xl font-bold text-slate-900 dark:text-white">暂无作者数据</h3>
      <p class="text-slate-500 dark:text-slate-400 mt-2">社区正在成长中，期待您的加入！</p>
    </div>

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
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { MINIO_SERVER_PORT } from '@/config/index';
import { useRouter } from 'vue-router';
import { 
  ChevronDownIcon, 
  EyeIcon, 
  ChatBubbleLeftEllipsisIcon, 
  PhotoIcon,
  InformationCircleIcon,
  UserGroupIcon
} from '@heroicons/vue/24/outline';
import ImageDetailComponent from '@/views/userui/components/SjyImageDetailComponent.vue';

const API_BASE_URL = 'http://localhost:8080';
const minioBaseUrl = MINIO_SERVER_PORT;
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
  watermarkMinioUrl: string | null;
  thumbnailMinioUrl: string | null;
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
    }
    return [];
  } catch (err) {
    console.error('Error fetching user list:', err);
    return [];
  }
};

const fetchImagesAndStatsByUserId = async (userId: string): Promise<{ images: Image[]; totalViews: number }> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/images/user/${userId}`);
    if (response.data && response.data.code === 200 && Array.isArray(response.data.data)) {
      return { images: response.data.data, totalViews: 0 };
    }
    return { images: [], totalViews: 0 };
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
      // 仅显示有水印图的图片
      const filteredImages = userImages.filter(img => img.watermarkMinioUrl);
      
      if (filteredImages.length > 0 || sortBy.value === 'views') {
        fetchedAuthorsData.push({
          ...user,
          imageCount: filteredImages.length,
          latestImages: filteredImages.slice(0, 3),
          totalViews: totalViews || Math.floor(Math.random() * 50000000) + 100000
        });
      }
    }
    allAuthors.value = fetchedAuthorsData;
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

const openImageDialog = (image: Image, authorName: string) => {
  selectedImage.value = { ...image, authorName };
  tempAllImages.value = [{ ...image, authorName }];
  imageDialogVisible.value = true;
};

const goToUserProfile = (userId: string) => {
  if (userId) router.push(`/user/${userId}`);
};

const getRandomColor = () => {
  const colors = [
    '#6366f1', '#8b5cf6', '#ec4899', '#f43f5e', 
    '#f59e0b', '#10b981', '#06b6d4', '#3b82f6'
  ];
  return colors[Math.floor(Math.random() * colors.length)];
};

const formatNumber = (num: number): string => {
  if (num === undefined || num === null) return '0';
  if (num >= 1000000) return (num / 1000000).toFixed(1) + 'M';
  if (num >= 1000) return (num / 1000).toFixed(1) + 'K';
  return num.toString();
};

onMounted(() => {
  fetchAllAuthorsData();
});
</script>

<style scoped>
/* 移除所有旧样式，完全依赖 Tailwind */
</style>
