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

        <!-- 图片管理 -->
        <div class="flex items-center gap-2 px-3 py-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="18" height="18" rx="0" ry="0"/>
            <circle cx="8.5" cy="8.5" r="1.5"/>
            <polyline points="21 15 16 10 5 21"/>
          </svg>
          <span class="text-xs font-bold text-black">图片管理</span>
        </div>

        <!-- 系统设置 (选中) -->
        <div class="flex items-center gap-2 px-3 py-2 border-2 border-black bg-white">
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
        <h1 class="text-2xl font-bold text-black">系统设置</h1>
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

      <!-- 滚动内容区域 -->
      <div class="space-y-6 pb-24">
        <!-- 基础设置 -->
        <div class="border-2 border-black bg-white p-6">
          <div class="mb-6">
            <h2 class="text-lg font-bold text-black mb-2">基础设置</h2>
            <p class="text-xs text-gray-600">管理网站的基础信息与公共设置</p>
          </div>

          <div class="space-y-4">
            <!-- 网站名称和描述 -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-bold text-black block mb-2">网站名称</label>
                <input type="text" value="ImageHost Pro" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
              <div>
                <label class="text-xs font-bold text-black block mb-2">网站描述</label>
                <input type="text" value="Secure & Fast Image Hosting with Blockchain" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
            </div>

            <!-- 开放注册 -->
            <div class="flex items-center justify-between pt-2">
              <div>
                <p class="text-xs font-bold text-black">开放注册</p>
                <p class="text-xs text-gray-600">允许用户自主注册账户</p>
              </div>
              <div class="flex items-center gap-2">
                <input type="checkbox" checked class="w-4 h-4 cursor-pointer" />
                <div class="w-12 h-6 border-2 border-black bg-black rounded-full"></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 存储设置 -->
        <div class="border-2 border-black bg-white p-6">
          <div class="mb-6">
            <h2 class="text-lg font-bold text-black mb-2">存储设置</h2>
            <p class="text-xs text-gray-600">配置图片存储服务(MinIO/S3)</p>
          </div>

          <div class="space-y-4">
            <!-- API Endpoint 和 Console URL -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-bold text-black block mb-2">API Endpoint</label>
                <input type="text" value="http://localhost:19000" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
              <div>
                <label class="text-xs font-bold text-black block mb-2">Console URL</label>
                <div class="flex items-center gap-2">
                  <input type="text" value="http://localhost:19090" class="flex-1 px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
                  <button class="px-2 py-2 border-2 border-black bg-white text-black">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                    </svg>
                  </button>
                </div>
              </div>
            </div>

            <!-- Bucket Name 和 Access Key -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-bold text-black block mb-2">Bucket Name</label>
                <input type="text" value="images" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
              <div>
                <label class="text-xs font-bold text-black block mb-2">Access Key</label>
                <input type="password" value="••••••••••" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
            </div>

            <!-- Secret Key -->
            <div>
              <label class="text-xs font-bold text-black block mb-2">Secret Key</label>
              <input type="password" value="••••••••••" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
            </div>
          </div>
        </div>

        <!-- 区块链设置 -->
        <div class="border-2 border-black bg-white p-6">
          <div class="mb-6">
            <h2 class="text-lg font-bold text-black mb-2">区块链设置</h2>
            <p class="text-xs text-gray-600">配置 Web3RPC 和区块链服务地址</p>
          </div>

          <div class="space-y-4">
            <!-- Web3RPC 管理员地址和区块链智能API地址 -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="text-xs font-bold text-black block mb-2">Web3RPC 管理员地址</label>
                <div class="flex items-center gap-2">
                  <input type="text" value="http://192.168.136.128:5000" class="flex-1 px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
                  <button class="px-2 py-2 border-2 border-black bg-white text-black">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                    </svg>
                  </button>
                </div>
              </div>
              <div>
                <label class="text-xs font-bold text-black block mb-2">区块链智能 API 地址</label>
                <input type="text" value="http://localhost:8001" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 保存按钮 (固定底部) -->
      <div class="fixed bottom-8 right-8">
        <button class="px-6 py-3 bg-black text-white font-bold text-sm border-2 border-black">
          保存修改
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const settings = ref({
  siteName: 'ImageHost Pro',
  siteDescription: 'Secure & Fast Image Hosting with Blockchain',
  allowRegistration: true,
  minioEndpoint: 'http://localhost:19000',
  minioConsole: 'http://localhost:19090',
  minioBucket: 'images',
  minioAccessKey: '••••••••••',
  minioSecretKey: '••••••••••',
  web3RpcAdmin: 'http://192.168.136.128:5000',
  web3ApiEndpoint: 'http://localhost:8001'
})
</script>

<style scoped>
</style>
