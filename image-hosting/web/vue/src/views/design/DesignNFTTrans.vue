<template>
  <div class="w-full min-h-screen bg-white pt-4 pb-10">
    <div class="container mx-auto px-4 max-w-6xl">
      
      <!-- 标题和返回按钮 -->
      <div class="flex items-start justify-between mb-8">
        <div>
          <h1 class="text-2xl font-bold text-black">全网交易记录</h1>
          <p class="text-sm text-gray-600 mt-2">查对链区块上一笔 NFT 交易动态</p>
        </div>
        <button class="px-3 py-2 border-2 border-black bg-white text-black font-bold text-sm flex items-center gap-1">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M15 18l-6-6 6-6"/>
          </svg>
          返回
        </button>
      </div>

      <!-- 交易须知 -->
      <div class="mb-8">
        <div class="flex items-center gap-2 mb-4">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
            <circle cx="12" cy="12" r="1"/>
          </svg>
          <h2 class="text-lg font-bold text-black">交易须知</h2>
        </div>

        <div class="grid grid-cols-3 gap-4">
          <!-- 卡片1 -->
          <div class="border-2 border-black bg-white p-4">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-8 h-8 border-2 border-black bg-purple-200 flex items-center justify-center text-sm font-bold">1</div>
              <span class="font-bold text-black">不可撤改</span>
            </div>
            <p class="text-xs text-gray-600">所有交易记录上链，数据蔗板无可，无法修改与查阅。</p>
          </div>

          <!-- 卡片2 -->
          <div class="border-2 border-black bg-white p-4">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-8 h-8 border-2 border-black bg-blue-200 flex items-center justify-center text-sm font-bold">2</div>
              <span class="font-bold text-black">请明公开</span>
            </div>
            <p class="text-xs text-gray-600">说以交易信息相问题相印证件，记录前可查真慰信息与交易目合。</p>
          </div>

          <!-- 卡片3 -->
          <div class="border-2 border-black bg-white p-4">
            <div class="flex items-center gap-3 mb-3">
              <div class="w-8 h-8 border-2 border-black bg-green-200 flex items-center justify-center text-sm font-bold">3</div>
              <span class="font-bold text-black">无可更新</span>
            </div>
            <p class="text-xs text-gray-600">无论过过上一交易，之后结交公共信息都能即时显示。</p>
          </div>
        </div>
      </div>

      <!-- 交易趋势分析 -->
      <div class="mb-8 border-2 border-black bg-white p-6">
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black">
              <circle cx="12" cy="12" r="1"/>
            </svg>
            <h3 class="text-lg font-bold text-black">交易趋势分析</h3>
          </div>
          <div class="flex gap-2">
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">日</button>
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">周</button>
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">月</button>
          </div>
        </div>

        <!-- 图表占位 -->
        <div class="h-64 bg-gray-100 border-2 border-black flex items-center justify-center">
          <div class="text-center">
            <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-400 mx-auto mb-2">
              <polyline points="23 6 13.5 15.5 8.5 10.5 1 17"/>
              <polyline points="17 6 23 6 23 12"/>
            </svg>
            <div class="text-xs text-gray-600">交易趋势图表</div>
          </div>
        </div>
      </div>

      <!-- NFT 交易表格 -->
      <div class="border-2 border-black bg-white overflow-hidden">
        <table class="w-full">
          <thead>
            <tr class="border-b-2 border-black bg-gray-200">
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">NFT 信息</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">交易哈希</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">发送方</th>
              <th class="px-4 py-3 text-left font-bold text-black text-sm">接收方</th>
            </tr>
          </thead>
          <tbody>
            <!-- 行1 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <div class="flex items-center gap-3">
                  <div class="w-10 h-10 border-2 border-black bg-gray-200 flex items-center justify-center text-xs">IMG</div>
                  <div>
                    <div class="text-sm font-bold text-black">Snipaste-2025-11-27.png</div>
                    <div class="text-xs text-gray-600">ID: 2404987267067025151</div>
                  </div>
                </div>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xdker1aaatfr2f063c...</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xc0b00b00b00b00b00b00b...</td>
              <td class="px-4 py-3 text-xs font-mono text-gray-600">0xe31f5dce2ebb8ef76ddf5a8...</td>
            </tr>

            <!-- 行2 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <div class="flex items-center gap-3">
                  <div class="w-10 h-10 border-2 border-black bg-gray-200 flex items-center justify-center text-xs">IMG</div>
                  <div>
                    <div class="text-sm font-bold text-black">IMG_3656.JPG</div>
                    <div class="text-xs text-gray-600">ID: 10982749373041868882</div>
                  </div>
                </div>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xef cc1626dd84d32f88f0c9dd0b...</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xfcc1626d84d4328dd0b8c...</td>
              <td class="px-4 py-3 text-xs font-mono text-gray-600">0xe31f5dce2ebb8ef76ddf5a8...</td>
            </tr>

            <!-- 行3 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <div class="flex items-center gap-3">
                  <div class="w-10 h-10 border-2 border-black bg-gray-200 flex items-center justify-center text-xs">IMG</div>
                  <div>
                    <div class="text-sm font-bold text-black">未命名</div>
                    <div class="text-xs text-gray-600">ID: 199829114375213089</div>
                  </div>
                </div>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xe73f1b13540f5f2f...</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xc0b00b00b00b00b00b00b...</td>
              <td class="px-4 py-3 text-xs font-mono text-gray-600">0xe31f5dce2ebb8ef76ddf5a8...</td>
            </tr>
          </tbody>
        </table>

        <!-- 分页 -->
        <div class="border-t-2 border-black px-4 py-3 flex justify-between items-center bg-gray-50">
          <div class="text-xs text-gray-600">共 100 条记录</div>
          <div class="flex gap-2">
            <button class="px-2 py-1 border-2 border-black bg-white text-black text-xs font-bold">上一页</button>
            <span class="px-2 py-1 text-xs text-black">第 1 页</span>
            <button class="px-2 py-1 border-2 border-black bg-white text-black text-xs font-bold">下一页</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const currentTimeframe = ref('day')
</script>

<style scoped>
</style>
