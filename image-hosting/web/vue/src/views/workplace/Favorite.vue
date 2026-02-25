<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-900 font-sans text-slate-900 dark:text-slate-100 transition-colors duration-300">
    
    <!-- Navigation -->
    <!-- <nav class="top-0 left-0 right-0 z-50 bg-white/80 dark:bg-slate-900/80 backdrop-blur-md border-b border-slate-200 dark:border-slate-800 shadow-sm py-3">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center">

          <div class="flex items-center gap-2 cursor-pointer" @click="router.push('/')">
            <div class="w-8 h-8 bg-gradient-to-br from-indigo-600 to-violet-600 rounded-lg flex items-center justify-center text-white font-bold text-sm shadow-lg shadow-indigo-500/30">
              IH
            </div>
            <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-700 dark:from-white dark:to-slate-300">
              ImageHosting
            </span>
          </div>

          <div class="hidden md:flex items-center space-x-8">
            <a @click="router.push('/')" class="text-sm font-medium text-slate-600 dark:text-slate-300 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors cursor-pointer">首页</a>
            <a @click="router.push('/explore')" class="text-sm font-medium text-slate-600 dark:text-slate-300 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors cursor-pointer">发现</a>
            <a @click="router.push('/pricing')" class="text-sm font-medium text-slate-600 dark:text-slate-300 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors cursor-pointer">定价</a>
            <a @click="router.push('/about')" class="text-sm font-medium text-slate-600 dark:text-slate-300 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors cursor-pointer">关于</a>
          </div>

        </div>
      </div>
    </nav> -->

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 min-h-[calc(100vh-200px)]">
      
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-slate-900 dark:text-white flex items-center gap-3">
          <StarIcon class="w-8 h-8 text-yellow-500" />
          我的收藏
        </h1>
        <p class="mt-2 text-slate-500 dark:text-slate-400">这里保存了您所有喜爱的图片。</p>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading" class="py-20 flex justify-center">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
      
      <!-- Empty State -->
      <div v-if="!isLoading && images.length === 0" class="py-20 text-center">
        <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-slate-100 dark:bg-slate-800 mb-6">
          <StarIcon class="w-10 h-10 text-slate-300 dark:text-slate-600" />
        </div>
        <h3 class="text-xl font-bold text-slate-900 dark:text-white">暂无收藏</h3>
        <p class="text-slate-500 dark:text-slate-400 mt-2 max-w-md mx-auto">您还没有收藏任何图片。去发现页面看看吧！</p>
        <button @click="router.push('/')" class="mt-6 px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg transition-colors">
          去发现
        </button>
      </div>

      <!-- Masonry Grid -->
      <div v-else class="columns-1 md:columns-2 lg:columns-3 gap-6 space-y-6">
        <div 
          v-for="(image, index) in images" 
          :key="image.imageId"
          class="break-inside-avoid group relative rounded-xl overflow-hidden bg-white dark:bg-slate-800 shadow-sm hover:shadow-xl transition-all duration-300 cursor-zoom-in"
          @click="openImageDialog(image, index)"
        >
          <!-- Image -->
          <img 
            :src="(image.watermarkMinioUrl || image.thumbnailMinioUrl)" 
            :alt="image.fileName"
            class="w-full h-auto object-cover transform group-hover:scale-105 transition-transform duration-500"
            loading="lazy"
          >
          
          <!-- Overlay -->
          <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-black/0 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col justify-end p-4">
            <div class="flex justify-between items-end">
              <div class="text-white">
                <p class="font-medium truncate">{{ image.fileName }}</p>
                <p class="text-xs text-white/80">{{ image.authorName }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

    </main>

    <!-- Footer -->
    <div class="bg-white dark:bg-slate-900 border-t border-slate-100 dark:border-slate-800">
      <FooterComponent />
    </div>

    <!-- Image Detail Modal -->
    <SjyImageDetailComponent
      v-model="imageDialogVisible"
      :image="selectedImage"
      :images="images"
      :initial-index="selectedIndex"
      @close="closeImageDialog"
      @navigate="handleNavigate"
    />

  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';
import FooterComponent from '@/views/userui/components/SjyFooterComponent.vue';
import SjyImageDetailComponent from '@/views/userui/components/SjyImageDetailComponent.vue';
import { StarIcon } from '@heroicons/vue/24/solid';

// Interfaces
interface Image {
  imageId: string;
  minioUrl: string;
  watermarkMinioUrl: string;
  thumbnailMinioUrl: string;
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

interface UserInfo {
  userId: string;
  userName: string;
  userEmail: string;
  userRole: string;
}

// Config
const API_BASE_URL = 'http://localhost:8080';
const router = useRouter();

// State
const images = ref<Image[]>([]);
const isLoading = ref(false);

// Dialog State
const imageDialogVisible = ref(false);
const selectedImage = ref<Image | null>(null);
const selectedIndex = ref(0);

// Cache
const userCache = new Map<string, UserInfo>();

// Methods
const fetchUserInfo = async (userId: string): Promise<UserInfo | null> => {
  if (userCache.has(userId)) return userCache.get(userId)!;

  try {
    const response = await request.get(`/api/user/getUserById`, {
      params: { userId }
    });
    const res = response as any;
    if (res.code === 200 && res.data) {
      const userInfo: UserInfo = res.data;
      userCache.set(userId, userInfo);
      return userInfo;
    }
    return null;
  } catch (err) {
    console.error(`Error fetching user info:`, err);
    return null;
  }
};

const fetchFavorites = async () => {
  isLoading.value = true;
  try {
    const response = await request.get(`/api/favorites/list`);
    const res = response as any;
    if (res.code === 200 && Array.isArray(res.data)) {
      const fetchedImages: Image[] = await Promise.all(res.data.map(async (img: Image) => {
        const userInfo = await fetchUserInfo(img.userId);
        return {
          ...img,
          authorName: userInfo ? userInfo.userName : '未知作者',
          createTime: img.createTime || img.uploadTime
        };
      }));
      images.value = fetchedImages;
    } else {
      images.value = [];
    }
  } catch (err) {
    console.error('Error fetching favorites:', err);
    images.value = [];
  } finally {
    isLoading.value = false;
  }
};

const openImageDialog = (image: Image, index: number) => {
  selectedImage.value = image;
  selectedIndex.value = index;
  imageDialogVisible.value = true;
};

const closeImageDialog = () => {
  imageDialogVisible.value = false;
  // Refresh list when closing dialog in case favorites changed
  fetchFavorites();
};

const handleNavigate = (index: number) => {
  selectedIndex.value = index;
  selectedImage.value = images.value[index];
};

// Lifecycle
onMounted(() => {
  fetchFavorites();
});
</script>
