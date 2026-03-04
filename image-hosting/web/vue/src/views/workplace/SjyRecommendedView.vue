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

        <!-- Quick Search (Removed from Header) -->
        <div class="flex-1 max-w-md mx-4 hidden">
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
      
      <!-- Hero Carousel -->
      <div v-if="images.length > 0" class="mb-10 rounded-2xl overflow-hidden shadow-2xl relative group">
        <el-carousel 
          indicator-position="outside" 
          height="400px" 
          v-loading="isLoading"
          class="rounded-2xl overflow-hidden border border-slate-200 dark:border-slate-800"
        >
          <el-carousel-item v-for="(image, index) in images.slice(0, 5)" :key="image.imageId">
            <div 
              class="relative w-full h-full cursor-pointer overflow-hidden"
              @click="openImageDialog(image, index)"
            >
              <img 
                :src="image.watermarkMinioUrl || image.minioUrl" 
                class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105"
                alt="Featured Image"
              />
              <!-- Carousel Overlay -->
              <div class="absolute inset-x-0 bottom-0 bg-gradient-t from-black/80 via-black/40 to-transparent p-8">
                <div class="flex flex-col gap-2">
                  <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-500 text-white w-fit mb-2">
                    精选推荐
                  </span>
                  <h2 class="text-2xl font-bold text-white drop-shadow-md">{{ image.fileName }}</h2>
                  <div class="flex items-center gap-3 mt-2">
                    <div class="flex items-center gap-2">
                      <div class="w-8 h-8 rounded-full bg-white/20 backdrop-blur-md flex items-center justify-center text-sm font-bold text-white border border-white/30">
                        {{ image.authorName ? image.authorName.charAt(0).toUpperCase() : 'U' }}
                      </div>
                      <span class="text-white/90 font-medium">{{ image.authorName }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- Main Search Area -->
      <div class="max-w-2xl mx-auto mb-10">
        <div class="relative group">
          <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
            <MagnifyingGlassIcon class="h-6 w-6 text-slate-400 group-focus-within:text-indigo-500 transition-colors" />
          </div>
          <input 
            type="text" 
            class="block w-full pl-12 pr-4 py-4 border border-slate-200 dark:border-slate-700 rounded-2xl leading-5 bg-white dark:bg-slate-800 text-slate-900 dark:text-slate-100 placeholder-slate-400 focus:outline-none focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 transition-all text-lg shadow-sm group-hover:shadow-md" 
            placeholder="搜索你想要的灵感..." 
            v-model="searchQuery"
            @keyup.enter="handleSearch"
          >
          <div class="absolute inset-y-0 right-0 py-2 pr-2 flex items-center">
            <button 
              @click="handleSearch"
              class="px-6 h-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-xl transition-colors shadow-lg shadow-indigo-500/30 flex items-center gap-2"
            >
              <span>搜索</span>
            </button>
          </div>
        </div>
      </div>

      <!-- Filter Tags (Optional Quick Filters) -->
      <div class="flex items-center justify-center mb-10 flex-col gap-4">
        <div class="flex gap-3 p-1.5 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-full shadow-sm overflow-x-auto no-scrollbar max-w-full">
          <button 
            v-for="tag in ['全部', '风景', '人像', '二次元', '极简', '赛博朋克']" 
            :key="tag"
            class="px-6 py-2 rounded-full text-sm font-semibold whitespace-nowrap transition-all duration-300 transform active:scale-95"
            :class="activeTag === tag 
              ? 'bg-indigo-600 text-white shadow-lg shadow-indigo-500/40' 
              : 'text-slate-600 dark:text-slate-400 hover:bg-indigo-50 dark:hover:bg-indigo-900/30 hover:text-indigo-600 dark:hover:text-indigo-400'"
            @click="activeTag = tag"
          >
            {{ tag }}
          </button>
        </div>
        
        <!-- Custom Category Input -->
        <div class="flex items-center gap-2">
          <div class="relative">
            <input 
              type="text" 
              placeholder="输入自定义分类筛选..." 
              v-model="customCategory"
              @keyup.enter="activeTag = customCategory"
              class="pl-9 pr-4 py-2 rounded-full border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 transition-all w-64 shadow-sm"
            >
            <TagIcon class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
          </div>
          <button 
            v-if="customCategory"
            @click="activeTag = customCategory"
            class="p-2 rounded-full bg-indigo-50 text-indigo-600 hover:bg-indigo-100 transition-colors"
          >
            <MagnifyingGlassIcon class="w-5 h-5" />
          </button>
        </div>
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
          v-for="(image, index) in filteredImages" 
          :key="image.imageId"
          class="break-inside-avoid group relative rounded-xl overflow-hidden bg-white dark:bg-slate-800 shadow-sm hover:shadow-xl transition-all duration-300 cursor-zoom-in border border-slate-100 dark:border-slate-800"
          @click="openImageDialog(image, index)"
        >
          <!-- Image -->
          <img 
            :src="(image.watermarkMinioUrl || image.thumbnailMinioUrl)" 
            :alt="image.fileName"
            class="w-full h-auto object-cover transform scale-100 group-hover:scale-105 transition-transform duration-700 ease-out"
            loading="lazy"
          >
          
          <!-- Info Overlay (Visible on Hover) -->
          <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col justify-end p-4">
            <div class="flex flex-col gap-2">
              <div class="flex items-center gap-2">
                <span v-if="image.category" class="px-2 py-0.5 rounded-md bg-white/20 backdrop-blur-md text-[10px] font-bold text-white border border-white/20">
                  {{ image.category }}
                </span>
              </div>
              
              <div class="flex justify-between items-end">
                <div class="text-white overflow-hidden flex-1 mr-2">
                  <p class="font-bold truncate text-sm drop-shadow-sm">{{ image.fileName }}</p>
                  <p v-if="image.description" class="text-[11px] text-white/80 line-clamp-1 mt-0.5 leading-tight italic">"{{ image.description }}"</p>
                  <div class="flex items-center gap-1.5 mt-2">
                    <div class="w-5 h-5 rounded-full bg-indigo-500 border border-white/30 flex items-center justify-center text-[10px] font-black text-white">
                      {{ image.authorName ? image.authorName.charAt(0).toUpperCase() : 'U' }}
                    </div>
                    <p class="text-xs text-white/90 font-medium tracking-wide truncate">{{ image.authorName }}</p>
                  </div>
                </div>
                <div class="flex gap-2">
                  <button @click.stop="downloadImage(image)" class="p-2 bg-white/20 hover:bg-white text-white hover:text-indigo-600 rounded-lg backdrop-blur-sm transition-all duration-200">
                    <ArrowDownTrayIcon class="w-4 h-4" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Load More Trigger -->
      <div v-if="filteredImages.length > 0" class="mt-12 flex justify-center">
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
import { ref, onMounted, computed } from 'vue';
import request from '@/utils/request';
import FooterComponent from '@/views/userui/components/SjyFooterComponent.vue';
import SjyImageDetailComponent from '@/views/userui/components/SjyImageDetailComponent.vue';
import {
  MagnifyingGlassIcon,
  ArrowPathIcon,
  BellIcon,
  PhotoIcon,
  ArrowDownTrayIcon,
  ChevronDownIcon,
  TagIcon
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
  category: string | null;
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
const customCategory = ref('');

// Dialog State
const imageDialogVisible = ref(false);
const selectedImage = ref<Image | null>(null);
const selectedIndex = ref(0);

// Computed for filtering
const filteredImages = computed(() => {
  let result = images.value;
  
  // 1. Tag/Category filtering
  if (activeTag.value !== '全部') {
    result = result.filter(img => img.category === activeTag.value);
  }
  
  // 2. Search query filtering
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(img => 
      img.fileName.toLowerCase().includes(query) || 
      (img.description && img.description.toLowerCase().includes(query)) ||
      (img.authorName && img.authorName.toLowerCase().includes(query))
    );
  }
  
  return result;
});

// Cache
const userCache = new Map<string, UserInfo>();

// Methods
const downloadImage = (image: Image) => {
  const token = localStorage.getItem('token');
  const downloadUrl = `${API_BASE_URL}/api/images/minio/${image.imageId}${token ? `?Authorization=${token}` : ''}`;
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

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
  selectedImage.value = filteredImages.value[index];
};

// Lifecycle
onMounted(() => {
  fetchImages();
});
</script>

<style scoped>
/* Hide scrollbar for chrome/safari/opera */
.no-scrollbar::-webkit-scrollbar {
  display: none;
}

/* Hide scrollbar for IE, Edge and Firefox */
.no-scrollbar {
  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;  /* Firefox */
}

/* Custom indicator style for el-carousel */
:deep(.el-carousel__indicator--horizontal .el-carousel__button) {
  width: 12px;
  height: 4px;
  border-radius: 2px;
}

:deep(.el-carousel__indicator.is-active .el-carousel__button) {
  width: 24px;
  background-color: #4f46e5; /* indigo-600 */
}
</style>
