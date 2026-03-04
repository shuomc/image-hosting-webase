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
        <div class="flex items-center gap-2 px-3 py-2 border-2 border-black bg-white">
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

        <!-- 图片管理 -->
        <div class="flex items-center gap-2 px-3 py-2">
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
        <h1 class="text-2xl font-bold text-black">仪表盘</h1>
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

      <!-- 统计卡片 -->
      <div class="grid grid-cols-4 gap-4 mb-8">
        <!-- 总用户数 -->
        <div class="border-2 border-black bg-white p-4">
          <div class="flex items-center justify-between mb-3">
            <span class="text-xs font-bold text-black">总用户数</span>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
              <circle cx="9" cy="7" r="4"/>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
          </div>
          <div class="text-2xl font-bold text-black mb-2">2</div>
          <div class="text-xs text-red-600">↓ 0% 较上月</div>
        </div>

        <!-- 图片总数 -->
        <div class="border-2 border-black bg-white p-4">
          <div class="flex items-center justify-between mb-3">
            <span class="text-xs font-bold text-black">图片总数</span>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
              <rect x="3" y="3" width="18" height="18"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
          </div>
          <div class="text-2xl font-bold text-black mb-2">7</div>
          <div class="text-xs text-red-600">↓ 0% 较上月</div>
        </div>

        <!-- 存储占用 -->
        <div class="border-2 border-black bg-white p-4">
          <div class="flex items-center justify-between mb-3">
            <span class="text-xs font-bold text-black">存储占用</span>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            </svg>
          </div>
          <div class="text-2xl font-bold text-black mb-2">13 MB</div>
          <div class="text-xs text-red-600">↓ 0% 较上月</div>
        </div>

        <!-- NFT交易量 -->
        <div class="border-2 border-black bg-white p-4">
          <div class="flex items-center justify-between mb-3">
            <span class="text-xs font-bold text-black">NFT交易量</span>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
              <circle cx="12" cy="12" r="1"/>
              <circle cx="19" cy="12" r="1"/>
              <circle cx="5" cy="12" r="1"/>
            </svg>
          </div>
          <div class="text-2xl font-bold text-black mb-2">13</div>
          <div class="text-xs text-red-600">↓ 0% 较上月</div>
        </div>
      </div>

      <!-- 下方两个图表 -->
      <div class="grid grid-cols-2 gap-6">
        <!-- 上传趋势 -->
        <div class="border-2 border-black bg-white p-6">
          <h3 class="text-sm font-bold text-black mb-4">上传趋势</h3>
          <div class="h-48 bg-gray-100 border-2 border-black flex items-center justify-center">
            <div class="text-center">
              <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400 mx-auto mb-2">
                <polyline points="23 6 13.5 15.5 8.5 10.5 1 17"/>
                <polyline points="17 6 23 6 23 12"/>
              </svg>
              <div class="text-xs text-gray-600">趋势图表</div>
            </div>
          </div>
        </div>

        <!-- 图片分布统计 -->
        <div class="border-2 border-black bg-white p-6">
          <h3 class="text-sm font-bold text-black mb-4">图片分布统计</h3>
          <div class="flex items-center justify-around">
            <!-- 饼图1 -->
            <div class="text-center">
              <div class="h-32 w-32 border-4 border-black bg-gray-100 rounded-full flex items-center justify-center">
                <div class="text-xs text-gray-600">图表</div>
              </div>
              <div class="flex gap-2 justify-center mt-3 text-xs">
                <div class="flex items-center gap-1">
                  <div class="w-2 h-2 bg-black"></div>
                  <span>上传</span>
                </div>
                <div class="flex items-center gap-1">
                  <div class="w-2 h-2 bg-gray-400"></div>
                  <span>编辑</span>
                </div>
              </div>
            </div>

            <!-- 饼图2 -->
            <div class="text-center">
              <div class="h-32 w-32 border-4 border-black bg-gray-100 rounded-full flex items-center justify-center">
                <div class="text-xs text-gray-600">图表</div>
              </div>
              <div class="flex gap-2 justify-center mt-3 text-xs">
                <div class="flex items-center gap-1">
                  <div class="w-2 h-2 bg-black"></div>
                  <span>公开</span>
                </div>
                <div class="flex items-center gap-1">
                  <div class="w-2 h-2 bg-gray-400"></div>
                  <span>私有</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const stats = ref({
  totalUsers: 2,
  totalImages: 7,
  storageUsed: '13 MB',
  nftTransactions: 13
})
</script>

<style scoped>
</style>
