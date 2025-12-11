<template>
  <div class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Filters -->
    <div class="bg-white dark:bg-slate-800 p-4 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 flex flex-wrap gap-4 items-center justify-between">
      <div class="flex gap-2">
        <button class="px-4 py-2 rounded-xl text-sm font-medium bg-indigo-50 text-indigo-600 dark:bg-indigo-900/20 dark:text-indigo-400 border border-indigo-100 dark:border-indigo-800">全部</button>
        <button class="px-4 py-2 rounded-xl text-sm font-medium text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700/50 border border-transparent">公开</button>
        <button class="px-4 py-2 rounded-xl text-sm font-medium text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700/50 border border-transparent">私有</button>
        <button class="px-4 py-2 rounded-xl text-sm font-medium text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700/50 border border-transparent">违规</button>
      </div>
      <div class="flex gap-2">
        <input 
          type="text" 
          placeholder="搜索图片ID或名称..." 
          class="w-64 px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all text-sm"
        />
      </div>
    </div>

    <!-- Image Grid -->
    <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
      <div v-for="img in images" :key="img.id" class="group relative bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
        <!-- Image Thumbnail -->
        <div class="aspect-square overflow-hidden bg-slate-100 dark:bg-slate-900 relative">
          <img :src="img.url" class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" alt="" />
          
          <!-- Overlay Actions -->
          <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center gap-2">
            <button class="p-2 bg-white/20 backdrop-blur-md rounded-full text-white hover:bg-white/40 transition-colors" title="查看详情">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
            </button>
            <button class="p-2 bg-red-500/80 backdrop-blur-md rounded-full text-white hover:bg-red-600 transition-colors" title="删除">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Info -->
        <div class="p-4">
          <h4 class="text-sm font-medium text-slate-900 dark:text-white truncate">{{ img.name }}</h4>
          <div class="flex justify-between items-center mt-2">
            <span class="text-xs text-slate-500 dark:text-slate-400">{{ img.size }}</span>
            <span class="text-xs px-2 py-0.5 rounded-full bg-slate-100 dark:bg-slate-700 text-slate-600 dark:text-slate-300">{{ img.type }}</span>
          </div>
          <div class="mt-3 pt-3 border-t border-slate-100 dark:border-slate-700 flex items-center gap-2">
            <img :src="img.uploaderAvatar" class="w-5 h-5 rounded-full" alt="" />
            <span class="text-xs text-slate-500 dark:text-slate-400 truncate">{{ img.uploader }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// Mock Data
const images = ref([
  { id: 1, name: 'Sunset.jpg', url: 'https://picsum.photos/id/10/400/400', size: '2.4 MB', type: 'JPG', uploader: 'Alice', uploaderAvatar: 'https://i.pravatar.cc/150?u=a042581f4e29026024d' },
  { id: 2, name: 'Mountain.png', url: 'https://picsum.photos/id/11/400/400', size: '1.8 MB', type: 'PNG', uploader: 'Bob', uploaderAvatar: 'https://i.pravatar.cc/150?u=a042581f4e29026704d' },
  { id: 3, name: 'City.jpg', url: 'https://picsum.photos/id/12/400/400', size: '3.1 MB', type: 'JPG', uploader: 'Charlie', uploaderAvatar: 'https://i.pravatar.cc/150?u=a04258114e29026302d' },
  { id: 4, name: 'Ocean.webp', url: 'https://picsum.photos/id/13/400/400', size: '0.9 MB', type: 'WEBP', uploader: 'Diana', uploaderAvatar: 'https://i.pravatar.cc/150?u=a042581f4e29026024d' },
  { id: 5, name: 'Forest.jpg', url: 'https://picsum.photos/id/14/400/400', size: '4.2 MB', type: 'JPG', uploader: 'Evan', uploaderAvatar: 'https://i.pravatar.cc/150?u=a048581f4e29026024d' },
  { id: 6, name: 'Desert.jpg', url: 'https://picsum.photos/id/15/400/400', size: '1.5 MB', type: 'JPG', uploader: 'Alice', uploaderAvatar: 'https://i.pravatar.cc/150?u=a042581f4e29026024d' },
  { id: 7, name: 'River.png', url: 'https://picsum.photos/id/16/400/400', size: '2.1 MB', type: 'PNG', uploader: 'Bob', uploaderAvatar: 'https://i.pravatar.cc/150?u=a042581f4e29026704d' },
  { id: 8, name: 'Sky.jpg', url: 'https://picsum.photos/id/17/400/400', size: '1.2 MB', type: 'JPG', uploader: 'Charlie', uploaderAvatar: 'https://i.pravatar.cc/150?u=a04258114e29026302d' },
]);
</script>
