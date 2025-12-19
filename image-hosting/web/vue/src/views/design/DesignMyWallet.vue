<template>
  <div class="w-full min-h-screen bg-white py-8">
    <div class="container mx-auto px-4 max-w-6xl">
      
      <!-- 标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-black mb-2">钱包余额</h1>
        <p class="text-sm text-gray-600">管理你的链上资产与交易记录</p>
      </div>

      <!-- 余额卡片 -->
      <div class="border-2 border-black bg-gray-100 p-8 mb-8">
        <div class="text-center mb-8">
          <p class="text-sm text-gray-600 mb-4">当前可用余额</p>
          <div class="text-5xl font-bold text-black">
            117.0000 <span class="text-2xl ml-2">ETH</span>
          </div>
        </div>

        <!-- 按钮组 -->
        <div class="flex justify-center gap-4">
          <button class="px-8 py-3 bg-black text-white font-bold text-base border-2 border-black hover:bg-gray-800">
            充值
          </button>
          <button class="px-8 py-3 bg-white text-black font-bold text-base border-2 border-black hover:bg-gray-50">
            提现
          </button>
        </div>
      </div>

      <!-- 交易历史 -->
      <div class="border-2 border-black bg-white">
        <!-- 表头 -->
        <div class="flex items-center justify-between px-4 py-3 border-b-2 border-black bg-gray-200">
          <h2 class="font-bold text-black text-base">交易历史</h2>
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-black cursor-pointer">
            <polyline points="23 4 23 10 17 10"/>
            <path d="M20.49 15a9 9 0 1 1-2-8.83"/>
          </svg>
        </div>

        <!-- 表格 -->
        <table class="w-full">
          <thead>
            <tr class="border-b-2 border-black bg-gray-50">
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">类型</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">金额(ETH)</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">交易地址</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">时间</th>
              <th class="px-4 py-3 text-left font-bold text-black text-sm">状态</th>
            </tr>
          </thead>
          <tbody>
            <!-- 充值 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-green-200 text-black text-xs font-bold border-2 border-black">充值</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">11.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xef3f...c432</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/11 16:34:53</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 充值 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-green-200 text-black text-xs font-bold border-2 border-black">充值</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">6.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xef3f...c433</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/9 13:17:52</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 转出 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-purple-200 text-black text-xs font-bold border-2 border-black">转出</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">0.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xfaec...c984</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/0 15:08:11</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 充值 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-green-200 text-black text-xs font-bold border-2 border-black">充值</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">2.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xef3f...c432</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/9 15:00:40</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 充入 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-red-200 text-black text-xs font-bold border-2 border-black">充入</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">2.2000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xfaec...c984</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/9 14:33:19</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 转出 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-purple-200 text-black text-xs font-bold border-2 border-black">转出</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">0.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xfaec...c984</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/09 14:18:17</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 转出 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-purple-200 text-black text-xs font-bold border-2 border-black">转出</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">0.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xfaec...c984</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/09 14:14:05</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>

            <!-- 转出 -->
            <tr class="border-b-2 border-black">
              <td class="border-r-2 border-black px-4 py-3">
                <span class="inline-block px-2 py-1 bg-purple-200 text-black text-xs font-bold border-2 border-black">转出</span>
              </td>
              <td class="border-r-2 border-black px-4 py-3 text-sm font-bold text-black">2.0000</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs font-mono text-gray-600">0xfaec...c984</td>
              <td class="border-r-2 border-black px-4 py-3 text-xs text-gray-600">2025/12/09 14:10:06</td>
              <td class="px-4 py-3">
                <span class="text-xs font-bold text-green-600">成功</span>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 分页 -->
        <div class="border-t-2 border-black px-4 py-3 flex justify-between items-center bg-gray-50">
          <div class="text-xs text-gray-600">共 50 条记录</div>
          <div class="flex gap-2">
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">上一页</button>
            <span class="px-3 py-1 text-xs text-black">第 1 页</span>
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">下一页</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const balance = ref(117.0000)
</script>

<style scoped>
</style>
