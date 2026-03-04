<template>
  <div class="w-full min-h-screen bg-white py-8">
    <div class="container mx-auto px-4 max-w-7xl">
      
      <!-- 标题和保存按钮 -->
      <div class="flex items-start justify-between mb-8">
        <div>
          <h1 class="text-3xl font-bold text-black mb-2">个人资料</h1>
          <p class="text-sm text-gray-600">管理您的个人信息与账户设置</p>
        </div>
        <button class="px-4 py-2 bg-black text-white font-bold text-sm border-2 border-black flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="20 6 9 17 4 12"/>
          </svg>
          保存修改
        </button>
      </div>

      <div class="grid grid-cols-3 gap-6">
        <!-- 左侧面板 -->
        <div class="space-y-6">
          <!-- 用户卡片 -->
          <div class="border-2 border-black bg-white p-6">
            <div class="text-center mb-4">
              <div class="w-20 h-20 bg-black text-white rounded-full flex items-center justify-center text-2xl font-bold mx-auto mb-3">
                T
              </div>
              <div class="text-lg font-bold text-black">test</div>
              <div class="text-xs text-gray-600 mt-1">xxxxxxxxx@qq.com</div>
            </div>
            <div class="flex gap-2 justify-center">
              <span class="px-2 py-1 border-2 border-black bg-white text-black text-xs font-bold">USER</span>
              <span class="px-2 py-1 border-2 border-black bg-green-200 text-black text-xs font-bold">Active</span>
            </div>
          </div>

          <!-- 存储空间卡片 -->
          <div class="border-2 border-black bg-white p-6">
            <div class="flex items-center gap-2 mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                <polyline points="17 8 12 3 7 8"/>
                <line x1="12" y1="3" x2="12" y2="15"/>
              </svg>
              <h3 class="font-bold text-black text-sm">存储空间</h3>
            </div>
            <div class="text-center">
              <div class="text-sm text-gray-600 mb-2">已用空间</div>
              <div class="text-xl font-bold text-black mb-3">0 B / 1 GB</div>
              <div class="h-2 border-2 border-black bg-gray-200">
                <div class="h-full bg-black" style="width: 0%"></div>
              </div>
              <div class="text-xs text-gray-600 mt-2">您已使用0.0%的存储空间</div>
            </div>
          </div>
        </div>

        <!-- 右侧面板 -->
        <div class="col-span-2 space-y-6">
          <!-- 基本信息 -->
          <div class="border-2 border-black bg-white p-6">
            <div class="flex items-center gap-2 mb-6 pb-4 border-b-2 border-black">
              <div class="w-1 h-6 bg-black"></div>
              <h2 class="font-bold text-black text-lg">基本信息</h2>
            </div>

            <!-- 表单字段 -->
            <div class="grid grid-cols-2 gap-4 mb-4">
              <!-- 用户名 -->
              <div>
                <label class="text-xs font-bold text-black block mb-2">用户名</label>
                <div class="flex items-center border-2 border-black bg-white">
                  <input type="text" value="test" disabled class="flex-1 px-3 py-2 bg-transparent text-black text-sm outline-none" />
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mr-2 text-gray-600">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </div>
              </div>

              <!-- 邮箱地址 -->
              <div>
                <label class="text-xs font-bold text-black block mb-2">邮箱地址</label>
                <div class="flex items-center border-2 border-black bg-white">
                  <input type="email" value="116465957@qq.com" disabled class="flex-1 px-3 py-2 bg-transparent text-black text-sm outline-none" />
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="mr-2 text-gray-600">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </div>
              </div>
            </div>

            <div class="grid grid-cols-2 gap-4 mb-4">
              <!-- 昵称 -->
              <div>
                <label class="text-xs font-bold text-black block mb-2">昵称/描述</label>
                <input type="text" placeholder="设置一个对你的昵称" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>

              <!-- 手机号码 -->
              <div>
                <label class="text-xs font-bold text-black block mb-2">手机号码</label>
                <input type="tel" placeholder="编辑手机号" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
              </div>
            </div>

            <!-- 个人网站 -->
            <div class="mb-4">
              <label class="text-xs font-bold text-black block mb-2">个人网站 / 作品集</label>
              <input type="url" placeholder="https://www.example.com" class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none" />
            </div>

            <!-- 个人简介 -->
            <div>
              <label class="text-xs font-bold text-black block mb-2">个人简介</label>
              <textarea placeholder="介绍一下你自己..." class="w-full px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none resize-none" rows="4"></textarea>
            </div>
          </div>

          <!-- Web3绑定 -->
          <div class="border-2 border-black bg-white p-6">
            <div class="flex items-center gap-2 mb-6 pb-4 border-b-2 border-black">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              </svg>
              <h2 class="font-bold text-black text-lg">Web3 绑定</h2>
            </div>

            <!-- 区块链地址 -->
            <div class="mb-4">
              <label class="text-xs font-bold text-black block mb-2">区块链钱包地址</label>
              <div class="flex items-center gap-2">
                <input type="text" value="0xfccc1666dcd51353bbc5244dda9db9dcfcdc904" class="flex-1 px-3 py-2 border-2 border-black bg-white text-black text-sm outline-none font-mono" />
                <button class="px-2 py-2 border-2 border-black bg-white text-black font-bold text-xs">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/>
                    <rect x="9" y="2" width="6" height="4"/>
                  </svg>
                </button>
              </div>
              <div class="text-xs text-green-600 mt-2 flex items-center gap-1">
                <span class="w-2 h-2 bg-green-600 rounded-full"></span>
                已绑定至 FISCO BCOS 网络
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

const profileData = ref({
  username: 'test',
  email: '116465957@qq.com',
  nickname: '',
  phone: '',
  website: '',
  bio: '',
  walletAddress: '0xfccc1666dcd51353bbc5244dda9db9dcfcdc904'
})
</script>

<style scoped>
</style>
