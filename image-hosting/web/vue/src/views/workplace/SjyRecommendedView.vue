<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-900 font-sans text-slate-900 dark:text-slate-100 transition-colors duration-300">
    
    <!-- Header Section -->
    <header class="top-0 z-40 bg-white/80 dark:bg-slate-900/80 backdrop-blur-md border-b border-slate-200 dark:border-slate-800">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div class="flex items-center gap-4">
          <h1 class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-violet-600 dark:from-indigo-400 dark:to-violet-400">
            为你推荐
          </h1>
          <div class="hidden md:flex items-center text-sm text-slate-500 dark:text-slate-400 border-l border-slate-200 dark:border-slate-700 pl-4">
            发现优质创意灵感
          </div>
        </div>

        <!-- Quick Search -->
        <div class="flex-1 max-w-md mx-4 hidden sm:block">
          <div class="relative group">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <MagnifyingGlassIcon class="h-5 w-5 text-slate-400 group-focus-within:text-indigo-500 transition-colors" />
            </div>
            <input 
              type="text" 
              class="block w-full pl-10 pr-3 py-2 border border-slate-200 dark:border-slate-700 rounded-full leading-5 bg-slate-100 dark:bg-slate-800 text-slate-900 dark:text-slate-100 placeholder-slate-400 focus:outline-none focus:bg-white dark:focus:bg-slate-900 focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 transition-all sm:text-sm" 
              placeholder="搜索灵感..." 
              v-model="searchQuery"
              @keyup.enter="handleSearch"
            >
          </div>
        </div>

        <div class="flex items-center gap-3">
          <button class="p-2 text-slate-500 hover:text-indigo-600 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 rounded-full transition-colors">
            <BellIcon class="w-6 h-6" />
          </button>
          <button 
            @click="refreshImages"
            class="p-2 text-slate-500 hover:text-indigo-600 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 rounded-full transition-colors"
            :class="{ 'animate-spin': isLoading }"
          >
            <ArrowPathIcon class="w-6 h-6" />
          </button>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      
      <!-- Filter Tags (Optional Quick Filters) -->
      <div class="flex gap-2 mb-8 overflow-x-auto pb-2 scrollbar-hide">
        <button 
          v-for="tag in ['全部', '风景', '人像', '二次元', '极简', '赛博朋克']" 
          :key="tag"
          class="px-4 py-1.5 rounded-full text-sm font-medium whitespace-nowrap transition-all"
          :class="activeTag === tag 
            ? 'bg-indigo-600 text-white shadow-md shadow-indigo-500/30' 
            : 'bg-white dark:bg-slate-800 text-slate-600 dark:text-slate-300 border border-slate-200 dark:border-slate-700 hover:border-indigo-500 hover:text-indigo-500'"
          @click="activeTag = tag"
        >
          {{ tag }}
        </button>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading && images.length === 0" class="py-20 flex justify-center">
        <div class="flex flex-col items-center gap-4">
          <div class="animate-spin rounded-full h-10 w-10 border-b-2 border-indigo-600"></div>
          <p class="text-slate-500 text-sm">正在探索精彩内容...</p>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else-if="images.length === 0" class="py-20 text-center">
        <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-slate-100 dark:bg-slate-800 mb-4">
          <PhotoIcon class="w-8 h-8 text-slate-400" />
        </div>
        <h3 class="text-lg font-medium text-slate-900 dark:text-white">暂无推荐内容</h3>
        <p class="text-slate-500 dark:text-slate-400 mt-2">稍后再来看看吧</p>
      </div>

      <!-- Masonry Grid -->
      <div v-else class="columns-1 sm:columns-2 lg:columns-3 xl:columns-4 gap-6 space-y-6">
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
            class="w-full h-auto object-cover transform group-hover:scale-105 transition-transform duration-700 ease-out"
            loading="lazy"
          >
          
          <!-- Overlay -->
          <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-black/0 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col justify-end p-4">
            <div class="flex justify-between items-end">
              <div class="text-white overflow-hidden">
                <p class="font-medium truncate text-sm">{{ image.fileName }}</p>
                <div class="flex items-center gap-1 mt-1">
                  <div class="w-4 h-4 rounded-full bg-indigo-500 flex items-center justify-center text-[10px] font-bold text-white">
                    {{ image.authorName ? image.authorName.charAt(0).toUpperCase() : 'U' }}
                  </div>
                  <p class="text-xs text-white/90 truncate">{{ image.authorName }}</p>
                </div>
              </div>
              <div class="flex gap-2">
                <button class="p-1.5 bg-white/20 hover:bg-white text-white hover:text-indigo-600 rounded-full backdrop-blur-sm transition-colors">
                  <ArrowDownTrayIcon class="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Load More Trigger -->
      <div v-if="images.length > 0" class="mt-12 flex justify-center">
        <button class="px-6 py-2.5 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300 text-sm font-medium rounded-full hover:border-indigo-500 hover:text-indigo-600 dark:hover:text-indigo-400 transition-all shadow-sm hover:shadow-md flex items-center gap-2">
          <span>加载更多</span>
          <ChevronDownIcon class="w-4 h-4" />
        </button>
      </div>

    </main>

    <!-- Footer -->
    <div class="bg-white dark:bg-slate-900 border-t border-slate-100 dark:border-slate-800 mt-auto">
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
import request from '@/utils/request';
import FooterComponent from '@/views/userui/components/SjyFooterComponent.vue';
import SjyImageDetailComponent from '@/views/userui/components/SjyImageDetailComponent.vue';
import {
  MagnifyingGlassIcon,
  ArrowPathIcon,
  BellIcon,
  PhotoIcon,
  ArrowDownTrayIcon,
  ChevronDownIcon
} from '@heroicons/vue/24/outline';

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

// State
const images = ref<Image[]>([]);
const isLoading = ref(false);
const searchQuery = ref('');
const activeTag = ref('全部');

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

const fetchImages = async () => {
  isLoading.value = true;
  try {
    // Using public images endpoint for recommendation for now
    const response = await request.get(`/api/images/public`);
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
      // Shuffle for "random recommendation" feel
      images.value = fetchedImages.sort(() => Math.random() - 0.5);
    } else {
      images.value = [];
    }
  } catch (err) {
    console.error('Error fetching images:', err);
    images.value = [];
  } finally {
    isLoading.value = false;
  }
};

const refreshImages = () => {
  fetchImages();
};

const handleSearch = () => {
  console.log('Searching for:', searchQuery.value);
  // Implement search logic here
};

const openImageDialog = (image: Image, index: number) => {
  selectedImage.value = image;
  selectedIndex.value = index;
  imageDialogVisible.value = true;
};

const closeImageDialog = () => {
  imageDialogVisible.value = false;
};

const handleNavigate = (index: number) => {
  selectedIndex.value = index;
  selectedImage.value = images.value[index];
};

// Lifecycle
onMounted(() => {
  fetchImages();
});
</script>

<style scoped>
/* Hide scrollbar for chrome/safari/opera */
.scrollbar-hide::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
.scrollbar-hide {
  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;  /* Firefox */
}
</style>
