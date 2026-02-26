<template>
  <div class="min-h-screen bg-white font-sans text-black">
    
    <!-- Intro Section (Full Screen) -->
    <section class="relative h-screen flex flex-col items-center justify-center overflow-hidden bg-gray-200">
      <div class="relative z-10 text-center px-4">
        <div class="w-20 h-20 mx-auto bg-gray-400 flex items-center justify-center text-black font-bold text-4xl mb-8">
          IH
        </div>
        <h1 class="text-5xl font-bold text-black mb-6">
          Image-Hosting.NFT
        </h1>
        <p class="text-xl text-gray-600 max-w-2xl mx-auto">
          下一代去中心化数字资产平台
        </p>
      </div>

      <!-- Scroll Indicator -->
      <button 
        class="absolute bottom-10 left-1/2 -translate-x-1/2"
        @click="scrollToContent"
      >
        <div class="flex flex-col items-center gap-2 text-gray-600">
          <span class="text-xs">Scroll</span>
          <ChevronDownIcon class="w-6 h-6" />
        </div>
      </button>
    </section>

    <!-- Main Content Wrapper -->
    <div id="main-content" class="relative bg-white min-h-screen">

      <!-- Navigation -->
      <nav class="sticky top-0 left-0 right-0 z-50 border-b border-gray-400 bg-white py-3">
        <div class="max-w-7xl mx-auto px-4">
          <div class="flex justify-between items-center">
            <!-- Logo -->
            <div class="flex items-center gap-2 cursor-pointer" @click="router.push('/')">
              <div class="w-8 h-8 bg-gray-400 flex items-center justify-center text-black font-bold text-lg">
                IH
              </div>
              <span class="text-xl font-bold text-black">
                Image-Hosting.NFT
              </span>
            </div>

            <!-- Desktop Menu -->
            <div class="flex items-center space-x-8">
              <a href="#" class="text-sm font-medium text-black">探索</a>
              <router-link to="/licence" class="text-sm font-medium text-black">许可证</router-link>
              <a href="#" class="text-sm font-medium text-black">更多</a>
            </div>

            <!-- Auth Buttons -->
            <div class="flex items-center gap-4">
              <router-link to="/auth/login" class="text-sm font-medium text-black">
                登录
              </router-link>
              <button class="px-5 py-2.5 bg-black text-white text-sm font-medium border border-black">
                免费加入
              </button>
            </div>
          </div>
        </div>
      </nav>

      <!-- Hero Section -->
      <header class="relative pt-20 pb-20 overflow-hidden bg-gray-100">
        <div class="max-w-4xl mx-auto px-4 text-left">
          <div class="border border-gray-400 bg-white p-2 mb-6" style="width: fit-content;">
            <span class="text-xs font-bold text-black">Web3.0 图床新时代</span>
          </div>
          
          <h2 class="text-4xl font-bold text-black mb-6">
            发现数字资产的无限可能
          </h2>
          
          <p class="text-lg text-black mb-10 max-w-2xl">
            基于区块链技术的下一代去中心化图床。安全存储，NFT铸造，让您的每一次创作都独一无二。
          </p>

          <!-- Search Bar -->
          <div class="max-w-2xl mb-10">
            <div class="flex items-center bg-white border-2 border-black p-2">
              <input 
                v-model="searchQuery"
                type="text" 
                placeholder="搜索免费图片、NFT藏品..." 
                class="flex-1 bg-transparent border-none text-black placeholder-gray-600 text-base outline-none"
                @keyup.enter="handleSearch"
              />
              <button 
                @click="handleSearch"
                class="px-6 py-2 bg-black text-white font-medium border border-black"
              >
                搜索
              </button>
            </div>
          </div>

          <!-- Stats -->
          <div class="flex gap-16">
            <div>
              <div class="text-2xl font-bold text-black">10K+</div>
              <div class="text-sm text-black">创作者</div>
            </div>
            <div>
              <div class="text-2xl font-bold text-black">500K+</div>
              <div class="text-sm text-black">NFT资产</div>
            </div>
            <div>
              <div class="text-2xl font-bold text-black">0Gas</div>
              <div class="text-sm text-black">极速铸造</div>
            </div>
          </div>
        </div>
      </header>

      <!-- Main Content -->
      <main class="max-w-7xl mx-auto px-4 pb-20">
        
        <!-- Tabs -->
        <div class="flex mb-12 overflow-x-auto gap-1">
          <button 
            v-for="tab in tabs" 
            :key="tab.id"
            @click="activeTab = tab.id"
            class="px-4 py-2 text-sm font-medium border border-gray-400"
            :class="activeTab === tab.id 
              ? 'bg-black text-white' 
              : 'bg-white text-black'"
          >
            {{ tab.label }}
          </button>
        </div>

        <!-- Content Area -->
        <div class="min-h-96">
          <!-- Home Tab: Image Grid -->
          <div v-if="activeTab === 'home'">
            <div class="flex justify-between items-end mb-8">
              <div>
                <h2 class="text-2xl font-bold text-black">热门素材</h2>
                <p class="text-gray-600 mt-1">每日更新的精选优质图片</p>
              </div>
              
              <!-- Filter Dropdown -->
              <div class="relative group z-30">
                <button class="flex items-center gap-2 px-4 py-2 bg-white border-2 border-black text-sm font-medium text-black">
                  <span>推荐排序</span>
                  <ChevronDownIcon class="w-4 h-4" />
                </button>
              </div>
            </div>

            <!-- Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div 
                v-for="image in images" 
                :key="image.imageId"
                class="break-inside-avoid relative bg-gray-300 border-2 border-gray-400 cursor-pointer aspect-square"
                @click="openImageDialog(image)"
              >
                <!-- Icon Placeholder -->
                <div class="absolute inset-0 flex items-center justify-center">
                  <MagnifyingGlassIcon class="w-12 h-12 text-gray-600" />
                </div>
                <!-- Info Bar -->
                <div class="absolute bottom-0 left-0 right-0 bg-gray-400 p-2 border-t-2 border-gray-600">
                  <div class="text-xs text-black font-medium">{{ image.authorName || '未知作者' }}</div>
                </div>
              </div>
            </div>

            <!-- Loading State -->
            <div v-if="isLoading" class="py-20 flex justify-center">
              <div class="text-black">加载中...</div>
            </div>
            
            <!-- Empty State -->
            <div v-if="!isLoading && images.length === 0" class="py-20 text-center border-2 border-gray-400 bg-gray-100">
              <div class="text-black">暂无图片</div>
              <p class="text-gray-600 mt-2">尝试搜索其他关键词或稍后再试</p>
            </div>

            <!-- Load More -->
            <div class="mt-8 flex justify-center">
              <button class="px-8 py-3 bg-white border-2 border-black text-black font-medium">
                加载更多
              </button>
            </div>
          </div>

          <!-- Other Tabs -->
          <div v-else-if="activeTab === 'top-authors'">
            <div class="border-2 border-gray-400 bg-gray-100 p-8">
              <h3 class="text-black font-bold mb-4">热门作者</h3>
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div v-for="i in 6" :key="i" class="bg-gray-300 border-2 border-gray-500 p-4">
                  <div class="w-12 h-12 bg-gray-400 border border-black mb-2"></div>
                  <div class="text-black font-medium">作者 {{ i }}</div>
                  <div class="text-xs text-gray-700">描述文本</div>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="activeTab === 'challenges'">
            <div class="border-2 border-gray-400 bg-gray-100 p-8">
              <h3 class="text-black font-bold mb-4">挑战赛</h3>
              <div class="space-y-4">
                <div v-for="i in 2" :key="i" class="bg-white border-2 border-black p-4">
                  <div class="grid grid-cols-2 gap-2 mb-4">
                    <div class="bg-gray-300 border border-gray-500 aspect-square flex items-center justify-center">
                      <MagnifyingGlassIcon class="w-8 h-8 text-gray-600" />
                    </div>
                    <div class="bg-gray-300 border border-gray-500 aspect-square flex items-center justify-center">
                      <MagnifyingGlassIcon class="w-8 h-8 text-gray-600" />
                    </div>
                  </div>
                  <h4 class="text-black font-bold">挑战 {{ i }}</h4>
                  <p class="text-xs text-black">描述文本</p>
                  <div class="mt-2 flex gap-2">
                    <button class="px-4 py-2 bg-black text-white text-xs font-medium border border-black">参加</button>
                    <button class="px-4 py-2 bg-white text-black text-xs font-medium border-2 border-black">了解</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="py-20 text-center border-2 border-gray-400 bg-gray-100">
            <div class="text-black">即将推出</div>
            <p class="text-gray-600 mt-2">此功能正在开发中</p>
          </div>
        </div>
      </main>

      <!-- Footer -->
      <div class="bg-white border-t-2 border-black p-8">
        <div class="max-w-7xl mx-auto px-4 text-center text-black">
          <p>© 2024 Image-Hosting.NFT</p>
        </div>
      </div>
    </div>

    <!-- Image Modal -->
    <Teleport to="body">
      <Transition name="modal">
        <div 
          v-if="imageDialogVisible && selectedImage" 
          class="fixed inset-0 z-50 flex items-center justify-center bg-gray-600"
        >
          <!-- Modal Panel -->
          <div class="relative w-full max-w-4xl bg-white border-4 border-black overflow-hidden flex flex-col">
            
            <!-- Modal Header -->
            <div class="flex items-center justify-between px-4 py-3 border-b-2 border-black shrink-0">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-gray-400 border-2 border-black flex items-center justify-center text-black font-bold">
                  {{ selectedImage.authorName ? selectedImage.authorName.charAt(0).toUpperCase() : 'U' }}
                </div>
                <div>
                  <div class="font-bold text-black">
                    {{ selectedImage.authorName || 'Unknown' }}
                  </div>
                  <div class="text-xs text-gray-700">关注 · 捐赠</div>
                </div>
              </div>

              <div class="flex items-center gap-2">
                <button 
                  @click="downloadImage(selectedImage)"
                  class="px-4 py-2 bg-black text-white font-bold border border-black text-xs"
                >
                  下载
                </button>
                
                <button 
                  @click="closeImageDialog"
                  class="px-3 py-2 bg-white text-black border-2 border-black"
                >
                  X
                </button>
              </div>
            </div>

            <!-- Modal Body -->
            <div class="flex-1 overflow-hidden flex flex-col lg:flex-row min-h-0">
              <!-- Image Area -->
              <div class="flex-1 relative flex items-center justify-center p-4 bg-gray-300 border-b-2 lg:border-b-0 lg:border-r-2 border-black overflow-auto min-h-96">
                <div class="w-full h-full bg-gray-400 border-2 border-gray-600 flex items-center justify-center">
                  <MagnifyingGlassIcon class="w-16 h-16 text-gray-600" />
                </div>
              </div>

              <!-- Sidebar -->
              <div class="w-full lg:w-64 bg-white border-l-2 border-black p-4 overflow-y-auto shrink-0">
                <div class="space-y-4">
                  <!-- License Info -->
                  <div class="p-3 bg-gray-200 border-2 border-black">
                    <h4 class="font-bold text-black text-xs">商业授权许可</h4>
                    <p class="text-xs text-black mt-1">此图片为水印预览图，如需原图请前往市场。</p>
                  </div>

                  <!-- Details -->
                  <div>
                    <h3 class="text-xs font-bold text-black uppercase mb-2">详细信息</h3>
                    <div class="space-y-1 text-xs">
                      <div class="flex justify-between">
                        <span class="text-gray-700">分辨率</span>
                        <span class="font-medium text-black">Watermark</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-700">文件大小</span>
                        <span class="font-medium text-black">{{ selectedImage.size ? (selectedImage.size / 1024 / 1024).toFixed(2) + ' MB' : '未知' }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-700">格式</span>
                        <span class="font-medium text-black">{{ selectedImage.contentType || 'JPG' }}</span>
                      </div>
                    </div>
                  </div>

                  <!-- Actions -->
                  <div class="pt-2 border-t-2 border-black flex gap-2">
                    <button class="flex-1 py-2 border-2 border-black text-black text-xs font-medium bg-white">
                      分享
                    </button>
                    <button class="flex-1 py-2 border-2 border-black text-black text-xs font-medium bg-white">
                      举报
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import FooterComponent from '@/views/userui/components/SjyFooterComponent.vue';
import TopAuthors from '@/views/userui/components/SjyTopAuthorsComponent.vue';
import ChallengesPage from '@/views/userui/components/SjyChallengesComponent.vue';
import {
  MagnifyingGlassIcon,
  ChevronDownIcon,
  XMarkIcon,
  ArrowDownTrayIcon,
  StarIcon,
  PlusCircleIcon,
  InformationCircleIcon,
  FlagIcon,
  ShareIcon
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
const router = useRouter();

// State
const searchQuery = ref('');
const activeTab = ref('home');
const isScrolled = ref(false);
const images = ref<Image[]>([]);
const isLoading = ref(false);
const error = ref<string | null>(null);

// Dialog State
const imageDialogVisible = ref(false);
const selectedImage = ref<Image | null>(null);

// Tabs Config
const tabs = [
  { id: 'home', label: '主页' },
  { id: 'videos', label: '视频' },
  { id: 'top-authors', label: '热门作者' },
  { id: 'challenges', label: '挑战赛' }
];

// Cache
const userCache = new Map<string, UserInfo>();

// Methods
const handleScroll = () => {
  isScrolled.value = window.scrollY > 50;
};

const scrollToContent = () => {
  const content = document.getElementById('main-content');
  if (content) {
    content.scrollIntoView({ behavior: 'smooth' });
  }
};

const handleSearch = () => {
  console.log('Searching for:', searchQuery.value);
};

const fetchUserInfo = async (userId: string): Promise<UserInfo | null> => {
  if (userCache.has(userId)) return userCache.get(userId)!;

  try {
    const response = await axios.get(`${API_BASE_URL}/api/user/getUserById`, {
      params: { userId }
    });
    if (response.data?.code === 200 && response.data.data) {
      const userInfo: UserInfo = response.data.data;
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
  error.value = null;
  try {
    const response = await axios.get(`${API_BASE_URL}/api/images/public`);
    if (response.data?.code === 200 && Array.isArray(response.data.data)) {
      const fetchedImages: Image[] = await Promise.all(response.data.data.map(async (img: Image) => {
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
    console.error('Error fetching images:', err);
    error.value = 'Failed to fetch images.';
    images.value = [];
  } finally {
    isLoading.value = false;
  }
};

const openImageDialog = (image: Image) => {
  selectedImage.value = image;
  imageDialogVisible.value = true;
};

const closeImageDialog = () => {
  imageDialogVisible.value = false;
};

// Handle body scroll lock
watch(imageDialogVisible, (visible) => {
  document.body.style.overflow = visible ? 'hidden' : '';
});

const getTags = (description: string | null) => {
  if (!description) return [];
  return description.split(' ').filter(tag => tag.length > 0);
};

const downloadImage = (image: Image | null) => {
  if (!image?.imageId) return;

  const token = localStorage.getItem('token');
  // 通过后端接口下载水印图，触发浏览器文件下载
  const downloadUrl = `${API_BASE_URL}/api/images/watermark/${image.imageId}${token ? `?Authorization=${token}` : ''}`;
  
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName || 'image.jpg');
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

const goToUserProfile = (userId: string | undefined) => {
  if (userId) router.push(`/user/${userId}`);
};

// Lifecycle
onMounted(() => {
  fetchImages();
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
  document.body.style.overflow = '';
});
</script>

<style scoped>
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
</style>
