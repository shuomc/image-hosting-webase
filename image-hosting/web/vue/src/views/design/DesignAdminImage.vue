<template>
  <div class="w-full min-h-screen bg-white flex">
    <!-- 左侧导航栏 -->
    <div class="w-48 bg-gray-50 border-r-2 border-black p-6 space-y-8">
      <!-- Logo -->
      <div class="flex items-center gap-2 mb-8">
        <div class="w-8 h-8 border-2 border-black bg-black text-white flex items-center justify-center text-sm font-bold">A</div>
        <span class="font-bold text-black text-sm">AdminPanel</span>
      </div>

      <!-- 导航菜单 -->
      <nav class="space-y-2">
        <!-- 仪表盘 -->
        <div class="flex items-center gap-2 px-3 py-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
          <span class="text-xs font-bold text-black">仪表盘</span>
        </div>

        <!-- 用户管理 -->
        <div class="flex items-center gap-2 px-3 py-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
          <span class="text-xs font-bold text-black">用户管理</span>
        </div>

        <!-- 图片管理 (选中) -->
        <div class="flex items-center gap-2 px-3 py-2 border-2 border-black bg-white">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="18" height="18" rx="0" ry="0"/>
            <circle cx="8.5" cy="8.5" r="1.5"/>
            <polyline points="21 15 16 10 5 21"/>
          </svg>
          <span class="text-xs font-bold text-black">图片管理</span>
        </div>

        <!-- 系统设置 -->
        <div class="flex items-center gap-2 px-3 py-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="3"/>
            <path d="M12 1v6m0 6v6M4.22 4.22l4.24 4.24m3.08 3.08l4.24 4.24M1 12h6m6 0h6m-15.78 7.78l4.24-4.24m3.08-3.08l4.24-4.24"/>
          </svg>
          <span class="text-xs font-bold text-black">系统设置</span>
        </div>
      </nav>
    </div>

    <!-- 主内容区 -->
    <div class="flex-1 p-8">
      <!-- 顶部栏 -->
      <div class="flex items-center justify-between mb-8 pb-4 border-b-2 border-black">
        <h1 class="text-2xl font-bold text-black">图片管理</h1>
        <div class="flex items-center gap-4">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
            <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
          </svg>
          <div class="flex items-center gap-2 px-3 py-1 border-2 border-black bg-white">
            <div class="w-6 h-6 bg-black text-white flex items-center justify-center text-xs font-bold rounded-full">A</div>
            <span class="text-xs font-bold text-black">Administrator</span>
            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="6 9 12 15 18 9"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- 选项卡和搜索 -->
      <div class="flex items-center justify-between mb-6">
        <div class="flex gap-2">
          <button class="px-4 py-2 border-2 border-black bg-black text-white text-xs font-bold">全部</button>
          <button class="px-4 py-2 border-2 border-black bg-white text-black text-xs font-bold">公开</button>
          <button class="px-4 py-2 border-2 border-black bg-white text-black text-xs font-bold">私密</button>
        </div>
        <div class="flex items-center border-2 border-black bg-white max-w-sm">
          <input type="text" placeholder="搜索图片ID或文件名..." class="flex-1 px-3 py-2 bg-transparent text-black text-sm outline-none" />
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mr-2 text-gray-600">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
        </div>
      </div>

      <!-- 图片网格 -->
      <div class="grid grid-cols-4 gap-4 mb-6">
        <!-- 图片卡片1 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">Snipaste-2025-12-09.png</div>
            <div class="text-xs text-gray-600 mb-2">103.11 KB | PNG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">T</div>
              <span class="text-gray-600 truncate">cc6d664-445-8bce-a0d1-38bb8dbffbc</span>
            </div>
          </div>
        </div>

        <!-- 图片卡片2 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">Snipaste-2025-11-24.png</div>
            <div class="text-xs text-gray-600 mb-2">453.42 KB | PNG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">S</div>
              <span class="text-gray-600 truncate">0xef3f-c432-433</span>
            </div>
          </div>
        </div>

        <!-- 图片卡片3 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">Snipaste-2025-11-27.png</div>
            <div class="text-xs text-gray-600 mb-2">456.21 KB | PNG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">M</div>
              <span class="text-gray-600 truncate">cc6d664-445-8bce-a0d1</span>
            </div>
          </div>
        </div>

        <!-- 图片卡片4 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">IMG_3217.JPG</div>
            <div class="text-xs text-gray-600 mb-2">2.28 MB | JPEG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">L</div>
              <span class="text-gray-600 truncate">bd26a97-1f3a-45d5-a87c</span>
            </div>
          </div>
        </div>

        <!-- 图片卡片5 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">IMG_3656.JPG</div>
            <div class="text-xs text-gray-600 mb-2">1.72 MB | JPEG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">U</div>
              <span class="text-gray-600 truncate">cc6d664-445-8bce-a0d1</span>
            </div>
          </div>
        </div>

        <!-- 图片卡片6 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">IMG_3231.JPG</div>
            <div class="text-xs text-gray-600 mb-2">1.34 MB | JPEG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">P</div>
              <span class="text-gray-600 truncate">cc6d664-445-8bce-a0d1</span>
            </div>
          </div>
        </div>

        <!-- 图片卡片7 -->
        <div class="border-2 border-black bg-white overflow-hidden">
          <div class="h-32 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="p-3">
            <div class="text-xs font-bold text-black mb-1">IMG_5237.JPG</div>
            <div class="text-xs text-gray-600 mb-2">6.83 MB | JPEG</div>
            <div class="flex items-center gap-2 text-xs">
              <div class="w-6 h-6 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">Q</div>
              <span class="text-gray-600 truncate">f3bde64e-7f12-45d5-a87c</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="flex justify-between items-center">
        <div class="text-xs text-gray-600">显示 1 到 7，共 7 条记录</div>
        <div class="flex gap-2">
          <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">上一页</button>
          <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">下一页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const images = ref([
  { id: 1, name: 'Snipaste-2025-12-09.png', size: '103.11 KB', format: 'PNG', user: 'T' },
  { id: 2, name: 'Snipaste-2025-11-24.png', size: '453.42 KB', format: 'PNG', user: 'S' },
  { id: 3, name: 'Snipaste-2025-11-27.png', size: '456.21 KB', format: 'PNG', user: 'M' },
  { id: 4, name: 'IMG_3217.JPG', size: '2.28 MB', format: 'JPEG', user: 'L' },
  { id: 5, name: 'IMG_3656.JPG', size: '1.72 MB', format: 'JPEG', user: 'U' },
  { id: 6, name: 'IMG_3231.JPG', size: '1.34 MB', format: 'JPEG', user: 'P' },
  { id: 7, name: 'IMG_5237.JPG', size: '6.83 MB', format: 'JPEG', user: 'Q' }
])

const filterType = ref('all')
const searchQuery = ref('')
</script>

<style scoped>
</style>
