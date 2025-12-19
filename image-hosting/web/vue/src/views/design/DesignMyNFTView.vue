<template>
  <div class="w-full min-h-screen bg-gray-50 pt-4 pb-10">
    <div class="container mx-auto px-4 max-w-7xl">
      
      <!-- 标题 -->
      <div class="mb-8">
        <h1 class="text-2xl font-bold text-black">我的数字资产</h1>
        <div class="text-sm mt-2 text-gray-600">钱包地址: 0x63f5dde2ebb8ef76ddf5a8313bd76552eee66e432</div>
      </div>

      <!-- 控制栏 -->
      <div class="mb-8 flex justify-between">
        <div class="flex gap-2">
          <!-- 视图切换 -->
          <div class="flex border-2 border-black">
            <button 
              @click="currentLayout = 'grid'" 
              :class="currentLayout === 'grid' ? 'bg-gray-300' : 'bg-white'"
              class="px-3 py-2 border-r-2 border-black text-black font-bold text-sm">
              网格
            </button>
            <button 
              @click="currentLayout = 'list'"
              :class="currentLayout === 'list' ? 'bg-gray-300' : 'bg-white'"
              class="px-3 py-2 text-black font-bold text-sm">
              列表
            </button>
          </div>

          <!-- 模式切换 -->
          <div class="flex border-2 border-black">
            <button 
              @click="viewMode = 'owned'"
              :class="viewMode === 'owned' ? 'bg-gray-300' : 'bg-white'"
              class="px-3 py-2 border-r-2 border-black text-black font-bold text-sm">
              持有的
            </button>
            <button 
              @click="viewMode = 'created'"
              :class="viewMode === 'created' ? 'bg-gray-300' : 'bg-white'"
              class="px-3 py-2 text-black font-bold text-sm">
              创建的
            </button>
          </div>
        </div>

        <!-- 分页 -->
        <div class="flex gap-2 border-2 border-black bg-white px-3 py-2">
          <button class="text-xs text-black font-bold">上一页</button>
          <span class="text-xs text-black">第1页/共1页</span>
          <button class="text-xs text-black font-bold">下一页</button>
        </div>
      </div>

      <!-- 网格视图 -->
      <div v-if="currentLayout === 'grid'" class="grid grid-cols-4 gap-4">
        <div v-for="nft in 10" :key="nft" class="border-2 border-black bg-white">
          <!-- 图片占位 -->
          <div class="w-full h-48 bg-gray-200 border-b-2 border-black flex items-center justify-center">
            <div class="text-center">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mx-auto text-gray-400">
                <rect width="18" height="18" x="3" y="3"/>
                <circle cx="9" cy="9" r="2"/>
                <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/>
              </svg>
              <div class="text-xs text-gray-500 mt-2">NFT图片</div>
            </div>
          </div>

          <!-- 信息 -->
          <div class="p-3">
            <div class="font-bold text-black text-sm">IMG_{{ nft }}.JPG</div>
            <div class="text-xs text-gray-600 mt-1">首先描述</div>
            
            <!-- 价格和状态 -->
            <div class="mt-3 pt-3 border-t-2 border-black flex justify-between items-center">
              <div>
                <div class="text-xs text-gray-600 font-bold">当前价格</div>
                <div class="text-sm font-bold text-black">{{ nft % 2 === 0 ? '--' : '0.5 ETH' }}</div>
              </div>
              <div class="text-xs font-bold px-2 py-1 border-2 border-black" :class="nft % 2 === 0 ? 'bg-white text-black' : 'bg-green-200 text-black'">
                {{ nft % 2 === 0 ? '未售' : '在售' }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-else-if="currentLayout === 'list'" class="border-2 border-black bg-white overflow-hidden">
        <table class="w-full">
          <thead>
            <tr class="border-b-2 border-black bg-gray-200">
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm w-16">图片</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">名称</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">Token ID</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">价格</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">状态</th>
              <th class="px-4 py-3 text-left font-bold text-black text-sm">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="nft in 10" :key="nft" class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <div class="w-12 h-12 bg-gray-200 border-2 border-black flex items-center justify-center">
                  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400">
                    <rect width="18" height="18" x="3" y="3"/>
                    <circle cx="9" cy="9" r="2"/>
                    <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/>
                  </svg>
                </div>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">IMG_{{ nft }}.JPG</td>
              <td class="border-r-2 border-black px-4 py-3 text-sm text-gray-600">#{{ nft }}</td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">{{ nft % 2 === 0 ? '--' : '0.5 ETH' }}</td>
              <td class="border-r-2 border-black px-4 py-3">
                <div class="border-2 border-black px-2 py-1 text-xs font-bold w-fit" :class="nft % 2 === 0 ? 'bg-white text-black' : 'bg-green-200 text-black'">
                  {{ nft % 2 === 0 ? '未售' : '在售' }}
                </div>
              </td>
              <td class="px-4 py-3">
                <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">详情</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const currentLayout = ref<'grid' | 'list'>('grid')
const viewMode = ref<'owned' | 'created'>('owned')
</script>

<style scoped>
</style>
