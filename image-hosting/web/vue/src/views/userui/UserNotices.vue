<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-950 font-sans text-slate-900 dark:text-slate-100 transition-colors duration-300">
    <!-- 背景渐变装饰 -->
    <div class="fixed inset-0 z-0 pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-500/5 blur-[120px]"></div>
      <div class="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-500/5 blur-[120px]"></div>
    </div>

    <!-- Navigation -->
    <nav class="sticky top-0 left-0 right-0 z-50 bg-white/70 dark:bg-slate-900/70 backdrop-blur-xl border-b border-slate-200/60 dark:border-slate-800/60 shadow-sm py-4">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center">
          <div class="flex items-center gap-3 cursor-pointer group" @click="router.push('/')">
            <div class="w-10 h-10 bg-gradient-to-br from-indigo-600 to-violet-600 rounded-xl flex items-center justify-center text-white font-bold text-xl shadow-lg shadow-indigo-500/20 group-hover:scale-105 transition-transform">
              IH
            </div>
            <div class="flex flex-col">
              <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-violet-600 dark:from-indigo-400 dark:to-violet-400">
                Image-Hosting
              </span>
              <span class="text-[10px] text-slate-400 dark:text-slate-500 uppercase tracking-widest font-medium">通知中心</span>
            </div>
          </div>
          <!-- <button 
            @click="router.push('/')" 
            class="flex items-center gap-2 px-4 py-2 rounded-xl bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-sm font-medium text-slate-600 dark:text-slate-300 transition-all active:scale-95"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4"><path d="m15 18-6-6 6-6"/></svg>
            返回首页
          </button> -->
        </div>
      </div>
    </nav>

    <main class="relative z-10 max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-6 mb-12">
        <div>
          <h1 class="text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">系统公告</h1>
          <p class="mt-2 text-slate-500 dark:text-slate-400">获取最新的平台动态与维护通知</p>
        </div>
        
        <!-- 搜索栏 -->
        <div class="relative w-full sm:w-72 group">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-indigo-500 transition-colors"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
          <input 
            v-model="searchQuery"
            type="text" 
            placeholder="搜索公告标题或内容..." 
            class="w-full pl-10 pr-4 py-2.5 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-2xl text-sm focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all shadow-sm"
          />
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="isLoading" class="py-32 flex flex-col items-center justify-center gap-4">
        <div class="relative w-16 h-16">
          <div class="absolute inset-0 border-4 border-indigo-100 dark:border-indigo-900/30 rounded-full"></div>
          <div class="absolute inset-0 border-4 border-indigo-600 rounded-full border-t-transparent animate-spin"></div>
        </div>
        <span class="text-slate-400 dark:text-slate-500 font-medium animate-pulse">正在同步公告信息...</span>
      </div>

      <!-- 空状态 -->
      <div v-else-if="filteredNotices.length === 0" class="py-24 flex flex-col items-center justify-center text-center">
        <div class="w-24 h-24 bg-slate-100 dark:bg-slate-800 rounded-full flex items-center justify-center mb-6 text-slate-300 dark:text-slate-600">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M6 16.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0"/><path d="M11 6.3a9 9 0 0 0-8.6 6.7"/><path d="M12 21a9 9 0 0 0 8.6-6.7"/><path d="M22 12c0-5.5-4.5-10-10-10"/><path d="m3 21 1.9-1.9"/></svg>
        </div>
        <h3 class="text-lg font-semibold text-slate-900 dark:text-white">暂无相关公告</h3>
        <p class="mt-2 text-slate-500 dark:text-slate-400 max-w-xs">目前没有记录，如果有新的动态我们会第一时间在此更新。</p>
        <button v-if="searchQuery" @click="searchQuery = ''" class="mt-6 text-indigo-600 dark:text-indigo-400 hover:underline text-sm font-medium">清除搜索条件</button>
      </div>

      <!-- 公告列表 -->
      <div v-else class="grid grid-cols-1 gap-6">
        <div 
          v-for="(notice, index) in filteredNotices" 
          :key="notice.noticeId" 
          class="group bg-white dark:bg-slate-900/50 rounded-3xl shadow-sm border border-slate-200/60 dark:border-slate-800/60 p-6 sm:p-8 transition-all hover:shadow-xl hover:shadow-indigo-500/5 hover:-translate-y-1 relative overflow-hidden"
          :style="{ transitionDelay: `${index * 50}ms` }"
        >
          <!-- 装饰背景 -->
          <div class="absolute top-0 right-0 w-32 h-32 bg-gradient-to-br from-indigo-500/5 to-transparent rounded-bl-full pointer-events-none transition-opacity opacity-0 group-hover:opacity-100"></div>

          <div class="flex flex-col gap-6">
            <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl bg-indigo-50 dark:bg-indigo-900/30 flex items-center justify-center text-indigo-600 dark:text-indigo-400">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5"><path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/><path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/></svg>
                </div>
                <div>
                  <h2 class="text-xl font-bold text-slate-900 dark:text-white group-hover:text-indigo-600 dark:group-hover:text-indigo-400 transition-colors">
                    {{ notice.title }}
                  </h2>
                  <div class="flex items-center gap-2 mt-1">
                    <span class="inline-flex items-center px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider bg-indigo-100 dark:bg-indigo-900/50 text-indigo-600 dark:text-indigo-300">
                      系统通知
                    </span>
                    <span class="text-xs text-slate-400 dark:text-slate-500">
                      {{ formatDate(notice.createTime) }}
                    </span>
                  </div>
                </div>
              </div>
              
              <!-- 发布标识 (仅展示效果) -->
              <div v-if="index === 0" class="flex-shrink-0 self-start sm:self-center">
                <span class="px-3 py-1 bg-rose-50 dark:bg-rose-900/20 text-rose-600 dark:text-rose-400 text-[10px] font-black uppercase rounded-full border border-rose-100 dark:border-rose-900/30 animate-pulse">NEW</span>
              </div>
            </div>

            <div class="prose dark:prose-invert max-w-none">
              <div class="text-slate-600 dark:text-slate-300 leading-relaxed text-sm sm:text-base border-l-2 border-slate-100 dark:border-slate-800 pl-4 py-1">
                <p class="whitespace-pre-wrap">{{ notice.content }}</p>
              </div>
            </div>

            <div class="flex items-center justify-between pt-4 border-t border-slate-50 dark:border-slate-800/50">
              <div class="flex items-center gap-2 text-slate-400">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3.5 h-3.5"><circle cx="12" cy="12" r="10"/><path d="M12 6v6l4 2"/></svg>
                <span class="text-xs">{{ timeAgo(notice.createTime) }}</span>
              </div>
              <button class="text-xs font-bold text-indigo-600 dark:text-indigo-400 flex items-center gap-1 hover:gap-2 transition-all">
                标记为已读 <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3.5 h-3.5"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { getNoticeList } from '@/api/notice';

interface Notice {
  noticeId: string;
  title: string;
  content: string;
  createTime: string;
}

const router = useRouter();
const notices = ref<Notice[]>([]);
const isLoading = ref(true);
const searchQuery = ref('');

// 搜索过滤
const filteredNotices = computed(() => {
  if (!searchQuery.value) return notices.value;
  const q = searchQuery.value.toLowerCase();
  return notices.value.filter(n => 
    n.title?.toLowerCase().includes(q) || 
    n.content?.toLowerCase().includes(q)
  );
});

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleString('zh-CN', {
    month: 'long',
    day: 'numeric',
    year: 'numeric'
  });
};

const timeAgo = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const now = new Date();
  const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);
  
  if (seconds < 60) return '刚刚';
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return `${minutes} 分钟前`;
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return `${hours} 小时前`;
  const days = Math.floor(hours / 24);
  if (days < 30) return `${days} 天前`;
  return formatDate(dateStr);
};

const fetchNotices = async () => {
  isLoading.value = true;
  try {
    const response = await getNoticeList();
    // 根据后端返回的数据格式处理，通常 response 已经是 data 部分了 (如果是经 interceptor 处理过的)
    if (response) {
      // 如果 response 本身就是数据列表
      if (Array.isArray(response)) {
        notices.value = response;
      } else if (response.data) {
        notices.value = response.data;
      }
    }
  } catch (error) {
    console.error('Failed to fetch notices:', error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  fetchNotices();
});
</script>

<style scoped>
.prose {
  font-family: inherit;
}

@keyframes slide-up {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.group {
  animation: slide-up 0.5s ease-out forwards;
}
</style>
