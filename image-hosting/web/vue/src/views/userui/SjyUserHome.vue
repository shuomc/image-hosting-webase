<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-900 font-sans text-slate-900 dark:text-slate-100 transition-colors duration-300">
    
    <!-- Intro Section (Full Screen) -->
    <section class="relative h-screen flex flex-col items-center justify-center overflow-hidden bg-slate-50 dark:bg-slate-900">
      <!-- Background Blobs -->
      <div class="absolute inset-0 pointer-events-none overflow-hidden">
        <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-indigo-300/30 dark:bg-indigo-600/20 rounded-full blur-3xl animate-blob"></div>
        <div class="absolute bottom-1/4 right-1/4 w-96 h-96 bg-purple-300/30 dark:bg-purple-600/20 rounded-full blur-3xl animate-blob animation-delay-2000"></div>
      </div>

      <div class="relative z-10 text-center px-4">
        <div class="w-20 h-20 mx-auto bg-gradient-to-br from-indigo-600 to-violet-600 rounded-2xl flex items-center justify-center text-white font-bold text-4xl shadow-2xl shadow-indigo-500/30 mb-8">
          IH
        </div>
        <h1 class="text-5xl md:text-7xl lg:text-8xl font-bold tracking-tight text-slate-900 dark:text-white mb-6">
          Image-Hosting<span class="text-indigo-600 dark:text-indigo-400">.NFT</span>
        </h1>
        <p class="text-xl md:text-2xl text-slate-600 dark:text-slate-400 max-w-2xl mx-auto leading-relaxed">
          下一代去中心化数字资产平台
        </p>
      </div>

      <!-- Scroll Indicator -->
      <button 
        class="absolute bottom-10 left-1/2 -translate-x-1/2 animate-bounce"
        @click="scrollToContent"
      >
        <div class="flex flex-col items-center gap-2 text-slate-400 dark:text-slate-500 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">
          <span class="text-xs uppercase tracking-widest">Scroll</span>
          <ChevronDownIcon class="w-6 h-6" />
        </div>
      </button>
    </section>

    <!-- Main Content Wrapper -->
    <div id="main-content" class="relative bg-slate-50 dark:bg-slate-900 min-h-screen">

      <!-- Navigation -->
      <nav 
        class="sticky top-0 left-0 right-0 z-50 transition-all duration-300 border-b"
        :class="isScrolled 
          ? 'bg-white/80 dark:bg-slate-900/80 backdrop-blur-md border-slate-200 dark:border-slate-800 shadow-sm py-3' 
          : 'bg-transparent border-transparent py-5'"
      >
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between items-center">
            <!-- Logo -->
            <div class="flex items-center gap-2 cursor-pointer" @click="router.push('/')">
              <div class="w-8 h-8 bg-gradient-to-br from-indigo-600 to-violet-600 rounded-lg flex items-center justify-center text-white font-bold text-lg shadow-lg shadow-indigo-500/30">
                IH
              </div>
              <span class="text-xl font-bold text-slate-900 dark:text-white">
                Image-Hosting<span class="text-indigo-600 dark:text-indigo-400">.NFT</span>
              </span>
            </div>

            <!-- Desktop Menu -->
            <div class="hidden md:flex items-center space-x-8">
              <a href="#" class="text-sm font-medium text-slate-600 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">探索</a>
              <router-link to="/licence" class="text-sm font-medium text-slate-600 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">许可证</router-link>
              
              <!-- More Dropdown -->
              <div class="relative group">
                <button class="flex items-center gap-1 text-sm font-medium text-slate-600 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">
                  更多
                  <ChevronDownIcon class="w-4 h-4" />
                </button>
                <div class="absolute right-0 mt-2 w-48 bg-white dark:bg-slate-800 rounded-xl shadow-xl border border-slate-100 dark:border-slate-700 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                  <div class="py-2">
                    <a href="#" class="block px-4 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400">关于我们</a>
                    <a href="#" class="block px-4 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400">博客</a>
                    <a href="#" class="block px-4 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400">常见问题</a>
                  </div>
                </div>
              </div>
            </div>

            <!-- Auth Buttons -->
            <div class="flex items-center gap-4">
              <router-link to="/auth/login" class="hidden md:block text-sm font-medium text-slate-600 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">
                登录
              </router-link>
              <router-link to="/register" class="px-5 py-2.5 bg-slate-900 dark:bg-white hover:bg-slate-800 dark:hover:bg-slate-100 text-white dark:text-slate-900 text-sm font-medium rounded-full transition-all shadow-lg">
                免费加入
              </router-link>
            </div>
          </div>
        </div>
      </nav>

      <!-- Hero Section -->
      <header class="relative pt-20 pb-20 lg:pt-32 lg:pb-32 overflow-hidden">
        <div class="absolute inset-0">
          <div class="absolute top-0 left-1/2 -translate-x-1/2 w-full h-full bg-gradient-to-b from-indigo-100/50 via-slate-50/50 to-slate-50 dark:from-indigo-900/20 dark:via-slate-900/50 dark:to-slate-900"></div>
        </div>

        <div class="relative z-10 max-w-4xl mx-auto px-4 text-center">
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-50 dark:bg-indigo-900/30 border border-indigo-100 dark:border-indigo-800 text-indigo-600 dark:text-indigo-400 text-xs font-semibold mb-6">
            <span class="relative flex h-2 w-2">
              <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-indigo-400 opacity-75"></span>
              <span class="relative inline-flex rounded-full h-2 w-2 bg-indigo-500"></span>
            </span>
            Web3.0 图床新时代
          </div>
          
          <h2 class="text-4xl md:text-6xl lg:text-7xl font-bold tracking-tight text-slate-900 dark:text-white mb-6">
            发现 <span class="text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-violet-600 dark:from-indigo-400 dark:to-violet-400">数字资产</span> 的<br/>无限可能
          </h2>
          
          <p class="text-lg md:text-xl text-slate-600 dark:text-slate-400 mb-10 max-w-2xl mx-auto leading-relaxed">
            基于区块链技术的下一代去中心化图床。安全存储，NFT铸造，让您的每一次创作都独一无二。
          </p>

          <!-- Search Bar -->
          <div class="max-w-2xl mx-auto relative">
            <div class="flex items-center bg-white dark:bg-slate-800 rounded-xl shadow-xl p-2 border border-slate-200 dark:border-slate-700">
              <div class="pl-4 pr-2 text-slate-400">
                <MagnifyingGlassIcon class="w-6 h-6" />
              </div>
              <input 
                v-model="searchQuery"
                type="text" 
                placeholder="搜索免费图片、NFT藏品..." 
                class="flex-1 h-12 bg-transparent border-none focus:ring-0 text-slate-700 dark:text-slate-200 placeholder-slate-400 text-base w-full outline-none"
                @keyup.enter="handleSearch"
              />
              <button 
                @click="handleSearch"
                class="px-6 py-3 bg-indigo-600 hover:bg-indigo-700 dark:bg-indigo-500 dark:hover:bg-indigo-600 text-white font-medium rounded-lg transition-colors"
              >
                搜索
              </button>
            </div>
          </div>

          <!-- Stats -->
          <div class="mt-12 flex justify-center gap-8 md:gap-16 text-slate-500 dark:text-slate-400">
            <div class="text-center">
              <div class="text-2xl font-bold text-slate-900 dark:text-white">10K+</div>
              <div class="text-sm">创作者</div>
            </div>
            <div class="text-center">
              <div class="text-2xl font-bold text-slate-900 dark:text-white">500K+</div>
              <div class="text-sm">NFT资产</div>
            </div>
            <div class="text-center">
              <div class="text-2xl font-bold text-slate-900 dark:text-white">0Gas</div>
              <div class="text-sm">极速铸造</div>
            </div>
          </div>
        </div>
      </header>

      <!-- Main Content -->
      <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-20">
        
        <!-- Tabs -->
        <div class="flex justify-center mb-12 overflow-x-auto">
          <div class="bg-white/50 dark:bg-slate-800/50 backdrop-blur p-1.5 rounded-full border border-slate-200 dark:border-slate-700 flex items-center gap-1 shadow-sm">
            <button 
              v-for="tab in tabs" 
              :key="tab.id"
              @click="activeTab = tab.id"
              class="px-6 py-2.5 rounded-full text-sm font-medium transition-all duration-200 whitespace-nowrap"
              :class="activeTab === tab.id 
                ? 'bg-white dark:bg-slate-700 shadow-md text-indigo-700 dark:text-indigo-300' 
                : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100/50 dark:hover:bg-slate-700/50'"
            >
              {{ tab.label }}
            </button>
          </div>
        </div>

        <!-- Content Area -->
        <div class="min-h-[400px]">
          <!-- Home Tab: Image Grid -->
          <div v-if="activeTab === 'home'">
            <div class="flex justify-between items-end mb-8">
              <div>
                <h2 class="text-2xl font-bold text-slate-900 dark:text-white">热门素材</h2>
                <p class="text-slate-500 dark:text-slate-400 mt-1">每日更新的精选优质图片</p>
              </div>
              
              <!-- Filter Dropdown -->
              <div class="relative group z-30">
                <button class="flex items-center gap-2 px-4 py-2 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg text-sm font-medium text-slate-700 dark:text-slate-300 hover:border-indigo-300 dark:hover:border-indigo-500 transition-all">
                  <span>推荐排序</span>
                  <ChevronDownIcon class="w-4 h-4" />
                </button>
                <div class="absolute right-0 mt-2 w-40 bg-white dark:bg-slate-800 rounded-xl shadow-xl border border-slate-100 dark:border-slate-700 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200">
                  <div class="p-1">
                    <button class="w-full text-left px-3 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-indigo-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400 rounded-lg">最新上传</button>
                    <button class="w-full text-left px-3 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-indigo-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400 rounded-lg">最多下载</button>
                    <button class="w-full text-left px-3 py-2 text-sm text-slate-600 dark:text-slate-300 hover:bg-indigo-50 dark:hover:bg-slate-700 hover:text-indigo-600 dark:hover:text-indigo-400 rounded-lg">编辑精选</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Masonry Grid -->
            <div class="columns-1 md:columns-2 lg:columns-3 gap-6 space-y-6">
              <div 
                v-for="image in images" 
                :key="image.imageId"
                class="break-inside-avoid group relative rounded-2xl overflow-hidden bg-slate-100 dark:bg-slate-800 cursor-pointer"
                @click="openImageDialog(image)"
              >
                <img 
                  :src="image.watermarkMinioUrl || image.thumbnailMinioUrl" 
                  :alt="image.fileName" 
                  class="w-full h-auto object-cover transition-transform duration-500 group-hover:scale-105"
                  loading="lazy"
                >
                <!-- Overlay -->
                <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col justify-end p-6">
                  <div class="flex justify-between items-center text-white">
                    <div class="flex items-center gap-2">
                      <div class="w-8 h-8 rounded-full bg-white/20 backdrop-blur flex items-center justify-center text-xs font-bold">
                        {{ image.authorName ? image.authorName.charAt(0).toUpperCase() : 'U' }}
                      </div>
                      <span class="text-sm font-medium truncate max-w-[120px]">{{ image.authorName || '未知作者' }}</span>
                    </div>
                    <button 
                      class="p-2 bg-white/20 hover:bg-white text-white hover:text-slate-900 rounded-full backdrop-blur transition-all" 
                      @click.stop="downloadImage(image)"
                    >
                      <ArrowDownTrayIcon class="w-5 h-5" />
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Loading State -->
            <div v-if="isLoading" class="py-20 flex justify-center">
              <div class="animate-spin rounded-full h-12 w-12 border-4 border-indigo-100 border-t-indigo-600"></div>
            </div>
            
            <!-- Empty State -->
            <div v-if="!isLoading && images.length === 0" class="py-20 text-center">
              <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-slate-100 dark:bg-slate-800 mb-4">
                <MagnifyingGlassIcon class="w-8 h-8 text-slate-400" />
              </div>
              <h3 class="text-lg font-medium text-slate-900 dark:text-white">暂无图片</h3>
              <p class="text-slate-500 dark:text-slate-400 mt-2">尝试搜索其他关键词或稍后再试</p>
            </div>

            <!-- Load More -->
            <div class="mt-16 flex justify-center">
              <button class="px-8 py-3 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300 font-medium rounded-full hover:border-indigo-600 dark:hover:border-indigo-500 hover:text-indigo-600 dark:hover:text-indigo-400 transition-all shadow-sm hover:shadow-md">
                加载更多
              </button>
            </div>
          </div>

          <!-- Other Tabs -->
          <div v-else-if="activeTab === 'top-authors'">
            <TopAuthors />
          </div>

          <div v-else-if="activeTab === 'challenges'">
            <ChallengesPage />
          </div>

          <div v-else class="py-32 text-center">
            <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-indigo-50 dark:bg-indigo-900/30 mb-6">
              <InformationCircleIcon class="w-10 h-10 text-indigo-400" />
            </div>
            <h3 class="text-xl font-bold text-slate-900 dark:text-white">即将推出</h3>
            <p class="text-slate-500 dark:text-slate-400 mt-2 max-w-md mx-auto">此功能正在紧锣密鼓地开发中，敬请期待！</p>
          </div>
        </div>
      </main>

      <!-- Footer -->
      <div class="bg-white dark:bg-slate-900 border-t border-slate-100 dark:border-slate-800">
        <FooterComponent />
      </div>
    </div>

    <!-- Image Modal -->
    <Teleport to="body">
      <Transition name="modal">
        <div 
          v-if="imageDialogVisible && selectedImage" 
          class="fixed inset-0 z-[9999] flex items-center justify-center"
        >
          <!-- Backdrop -->
          <div 
            class="absolute inset-0 bg-black/80 backdrop-blur-sm"
            @click="closeImageDialog"
          ></div>

          <!-- Modal Panel -->
          <div class="relative w-full max-w-6xl max-h-[90vh] m-4 bg-white dark:bg-slate-800 rounded-2xl shadow-2xl overflow-hidden flex flex-col">
            
            <!-- Modal Header -->
            <div class="flex items-center justify-between px-6 py-4 border-b border-slate-100 dark:border-slate-700 shrink-0">
              <div class="flex items-center gap-4">
                <div 
                  class="flex items-center gap-3 cursor-pointer"
                  @click="goToUserProfile(selectedImage.userId)"
                >
                  <div class="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-500 to-purple-500 flex items-center justify-center text-white font-bold shadow-md">
                    {{ selectedImage.authorName ? selectedImage.authorName.charAt(0).toUpperCase() : 'U' }}
                  </div>
                  <div>
                    <div class="font-bold text-slate-900 dark:text-white hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">
                      {{ selectedImage.authorName || 'Unknown' }}
                    </div>
                    <div class="text-xs text-slate-500 dark:text-slate-400">关注 · 捐赠</div>
                  </div>
                </div>
              </div>

              <div class="flex items-center gap-3">
                <button class="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-full transition-colors" title="收藏">
                  <StarIcon class="w-6 h-6" />
                </button>
                <button class="p-2 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 rounded-full transition-colors" title="添加到合集">
                  <PlusCircleIcon class="w-6 h-6" />
                </button>
                
                <div class="h-6 w-px bg-slate-200 dark:bg-slate-700 mx-2"></div>

                <button 
                  @click="downloadImage(selectedImage)"
                  class="flex items-center gap-2 px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-lg transition-colors shadow-lg"
                >
                  <span>免费下载</span>
                  <ArrowDownTrayIcon class="w-5 h-5" />
                </button>
                
                <button 
                  @click="closeImageDialog"
                  class="ml-4 p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-full transition-colors"
                >
                  <XMarkIcon class="w-6 h-6" />
                </button>
              </div>
            </div>

            <!-- Modal Body -->
            <div class="flex-1 overflow-hidden flex flex-col lg:flex-row min-h-0">
              <!-- Image Area -->
              <div class="flex-1 relative flex items-center justify-center p-4 lg:p-10 bg-slate-100 dark:bg-slate-900 overflow-auto">
                <img 
                  :src="selectedImage.watermarkMinioUrl || selectedImage.thumbnailMinioUrl" 
                  :alt="selectedImage.fileName" 
                  class="max-w-full max-h-full object-contain shadow-2xl rounded-lg"
                >
              </div>

              <!-- Sidebar -->
              <div class="w-full lg:w-96 bg-white dark:bg-slate-800 border-t lg:border-t-0 lg:border-l border-slate-100 dark:border-slate-700 p-6 overflow-y-auto shrink-0">
                <div class="space-y-8">
                  <!-- License Info -->
                  <div class="p-4 bg-indigo-50 dark:bg-indigo-900/20 rounded-xl border border-indigo-100 dark:border-indigo-900/30">
                    <div class="flex items-start gap-3">
                      <InformationCircleIcon class="w-5 h-5 text-indigo-600 dark:text-indigo-400 mt-0.5 shrink-0" />
                      <div>
                        <h4 class="font-bold text-indigo-900 dark:text-indigo-300 text-sm">商业授权许可</h4>
                        <p class="text-xs text-indigo-700 dark:text-indigo-400 mt-1">此图片为水印预览图，如需原图请前往市场。</p>
                      </div>
                    </div>
                  </div>

                  <!-- Details -->
                  <div>
                    <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">详细信息</h3>
                    <div class="space-y-3">
                      <div class="flex justify-between text-sm">
                        <span class="text-slate-500 dark:text-slate-400">分辨率</span>
                        <span class="font-medium text-slate-900 dark:text-slate-200">Watermark</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-slate-500 dark:text-slate-400">文件大小</span>
                        <span class="font-medium text-slate-900 dark:text-slate-200">{{ selectedImage.size ? (selectedImage.size / 1024 / 1024).toFixed(2) + ' MB' : '未知' }}</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-slate-500 dark:text-slate-400">格式</span>
                        <span class="font-medium text-slate-900 dark:text-slate-200">{{ selectedImage.contentType || 'JPG' }}</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-slate-500 dark:text-slate-400">上传时间</span>
                        <span class="font-medium text-slate-900 dark:text-slate-200">{{ selectedImage.createTime ? new Date(selectedImage.createTime).toLocaleDateString() : '未知' }}</span>
                      </div>
                    </div>
                  </div>

                  <!-- Description -->
                  <div v-if="selectedImage.description">
                    <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">描述</h3>
                    <p class="text-sm text-slate-600 dark:text-slate-300 leading-relaxed">{{ selectedImage.description }}</p>
                  </div>

                  <!-- Tags -->
                  <div>
                    <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">标签</h3>
                    <div class="flex flex-wrap gap-2">
                      <span 
                        v-for="tag in getTags(selectedImage.description)" 
                        :key="tag"
                        class="px-3 py-1 bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 dark:hover:bg-slate-600 text-slate-600 dark:text-slate-300 text-xs rounded-full cursor-pointer transition-colors"
                      >
                        {{ tag }}
                      </span>
                    </div>
                  </div>

                  <!-- Actions -->
                  <div class="pt-6 border-t border-slate-100 dark:border-slate-700 flex gap-4">
                    <button class="flex-1 flex items-center justify-center gap-2 py-2.5 border border-slate-200 dark:border-slate-600 rounded-lg text-sm font-medium text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-slate-900 dark:hover:text-white transition-colors">
                      <ShareIcon class="w-4 h-4" />
                      分享
                    </button>
                    <button class="flex-1 flex items-center justify-center gap-2 py-2.5 border border-slate-200 dark:border-slate-600 rounded-lg text-sm font-medium text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-slate-900 dark:hover:text-white transition-colors">
                      <FlagIcon class="w-4 h-4" />
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
import { MINIO_SERVER_PORT } from '@/config/index';
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

  // 通过后端接口下载水印图，触发浏览器文件下载
  const downloadUrl = `${API_BASE_URL}/api/images/watermark/${image.imageId}`;
  
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
/* Modal Transition */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-active .relative,
.modal-leave-active .relative {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .relative,
.modal-leave-to .relative {
  transform: scale(0.95);
  opacity: 0;
}

/* Blob Animation */
@keyframes blob {
  0% { transform: translate(0px, 0px) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
  100% { transform: translate(0px, 0px) scale(1); }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}
</style>
