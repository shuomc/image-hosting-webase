<template>
  <div class="w-full min-h-screen bg-white pt-4 pb-10">
    <div class="container mx-auto px-4 max-w-6xl">
      
      <!-- 标题区域 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-black">NFT 交易市场</h1>
        <p class="text-sm text-gray-600 mt-2">探索、购买和出售独特的数字资产</p>
      </div>

      <!-- 我的上架商品 -->
      <div class="mb-12">
        <div class="flex items-center gap-2 mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
            <circle cx="12" cy="12" r="1"/>
          </svg>
          <h2 class="text-xl font-bold text-black">我的上架商品</h2>
        </div>

        <div class="grid grid-cols-2 gap-6 mb-8">
          <!-- 商品卡片 1 -->
          <div class="border-2 border-black bg-white">
            <div class="relative bg-gray-300 h-64 flex items-center justify-center border-b-2 border-black">
              <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-500">
                <rect width="18" height="18" x="3" y="3"/>
                <circle cx="9" cy="9" r="2"/>
                <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/>
              </svg>
              <!-- 状态标签 -->
              <div class="absolute top-3 right-3 border-2 border-black bg-blue-400 px-2 py-1 text-xs font-bold text-black">正在出售</div>
            </div>
            
            <div class="p-4">
              <div class="flex justify-between items-start mb-3">
                <div>
                  <div class="font-bold text-black">IMG_3656.JPG</div>
                  <div class="text-xs text-gray-600 mt-1">#11</div>
                </div>
              </div>

              <div class="border-t-2 border-black pt-3 mb-3">
                <div class="text-xs text-gray-600 mb-1">售价</div>
                <div class="text-lg font-bold text-black">11.0000 ETH</div>
              </div>

              <!-- 操作按钮 -->
              <div class="flex gap-2">
                <button class="flex-1 px-2 py-2 border-2 border-black bg-white text-black text-xs font-bold flex items-center justify-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M12 5v14M5 12h14"/>
                  </svg>
                  编辑
                </button>
                <button class="flex-1 px-2 py-2 border-2 border-black bg-white text-black text-xs font-bold flex items-center justify-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" x2="9" y1="9" y2="15"/>
                    <line x1="9" x2="15" y1="9" y2="15"/>
                  </svg>
                  删除
                </button>
              </div>
            </div>
          </div>

          <!-- 商品卡片 2 -->
          <div class="border-2 border-black bg-white">
            <div class="relative bg-gray-300 h-64 flex items-center justify-center border-b-2 border-black">
              <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-500">
                <rect width="18" height="18" x="3" y="3"/>
                <circle cx="9" cy="9" r="2"/>
                <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/>
              </svg>
              <!-- 状态标签 -->
              <div class="absolute top-3 right-3 border-2 border-black bg-blue-400 px-2 py-1 text-xs font-bold text-black">正在出售</div>
            </div>
            
            <div class="p-4">
              <div class="flex justify-between items-start mb-3">
                <div>
                  <div class="font-bold text-black">DSC_5723.JPG</div>
                  <div class="text-xs text-gray-600 mt-1">#8</div>
                </div>
              </div>

              <div class="border-t-2 border-black pt-3 mb-3">
                <div class="text-xs text-gray-600 mb-1">售价</div>
                <div class="text-lg font-bold text-black">1.0000 ETH</div>
              </div>

              <!-- 操作按钮 -->
              <div class="flex gap-2">
                <button class="flex-1 px-2 py-2 border-2 border-black bg-white text-black text-xs font-bold flex items-center justify-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M12 5v14M5 12h14"/>
                  </svg>
                  编辑
                </button>
                <button class="flex-1 px-2 py-2 border-2 border-black bg-white text-black text-xs font-bold flex items-center justify-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="15" x2="9" y1="9" y2="15"/>
                    <line x1="9" x2="15" y1="9" y2="15"/>
                  </svg>
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 市场探索 -->
      <div class="mb-12">
        <div class="flex items-center gap-2 mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
            <circle cx="12" cy="12" r="1"/>
          </svg>
          <h2 class="text-xl font-bold text-black">市场探索</h2>
        </div>

        <!-- 搜索和排序 -->
        <div class="flex gap-4 mb-8">
          <div class="flex-1 flex items-center border-2 border-black bg-white px-3">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-600">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.35-4.35"/>
            </svg>
            <input type="text" placeholder="搜索 NFT..." class="flex-1 px-3 py-2 bg-white text-sm text-black outline-none placeholder-gray-500"/>
          </div>

          <select class="border-2 border-black bg-white px-4 py-2 text-sm font-bold text-black">
            <option>最新上架</option>
            <option>价格最低</option>
            <option>价格最高</option>
            <option>交易最热</option>
          </select>
        </div>

        <!-- 空状态 -->
        <div class="flex flex-col items-center justify-center py-16 border-2 border-black bg-gray-50">
          <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400 mb-4">
            <path d="M9 11H7a4 4 0 0 0-4 4v3h6v-7z"/>
            <path d="M15 11h2a4 4 0 0 1 4 4v3h-6v-7z"/>
            <path d="M12 17v4M9 20h6"/>
          </svg>
          <div class="text-center">
            <h3 class="text-lg font-bold text-black mb-1">市场空空如也</h3>
            <p class="text-sm text-gray-600">暂时没有符合用户出售的 NFT。</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const searchQuery = ref('')
const sortBy = ref('latest')
</script>

<style scoped>
</style>
