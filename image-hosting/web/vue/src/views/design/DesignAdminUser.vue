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

        <!-- 用户管理 (选中) -->
        <div class="flex items-center gap-2 px-3 py-2 border-2 border-black bg-white">
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
        <h1 class="text-2xl font-bold text-black">用户管理</h1>
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

      <!-- 搜索栏和添加按钮 -->
      <div class="flex items-center justify-between mb-6">
        <div class="flex-1 max-w-96 flex items-center border-2 border-black bg-white">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="ml-2 text-gray-600">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
          <input type="text" placeholder="搜索用户..." class="flex-1 px-3 py-2 bg-transparent text-black text-sm outline-none" />
        </div>
        <button class="ml-4 px-4 py-2 bg-black text-white font-bold text-sm border-2 border-black flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"/>
            <line x1="5" y1="12" x2="19" y2="12"/>
          </svg>
          添加用户
        </button>
      </div>

      <!-- 用户表格 -->
      <div class="border-2 border-black bg-white overflow-hidden">
        <table class="w-full">
          <thead>
            <tr class="border-b-2 border-black bg-gray-200">
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">用户</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">角色</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">状态</th>
              <th class="border-r-2 border-black px-4 py-3 text-left font-bold text-black text-sm">注册时间</th>
              <th class="px-4 py-3 text-left font-bold text-black text-sm">操作</th>
            </tr>
          </thead>
          <tbody>
            <!-- 用户1 -->
            <tr class="border-b-2 border-black hover:bg-gray-50">
              <td class="border-r-2 border-black px-4 py-4">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">T</div>
                  <div>
                    <div class="text-sm font-bold text-black">test</div>
                    <div class="text-xs text-gray-600">116465957@qq.com</div>
                  </div>
                </div>
              </td>
              <td class="border-r-2 border-black px-4 py-4">
                <span class="text-xs font-bold text-blue-600 bg-blue-100 px-2 py-1 border-2 border-blue-600">User</span>
              </td>
              <td class="border-r-2 border-black px-4 py-4">
                <span class="text-xs font-bold text-green-600">● Active</span>
              </td>
              <td class="border-r-2 border-black px-4 py-4 text-xs text-gray-600">2025/12/9</td>
              <td class="px-4 py-4 text-xs">
                <button class="font-bold text-blue-600 border-b border-blue-600">编辑</button>
                <span class="mx-2 text-gray-600">|</span>
                <button class="font-bold text-red-600 border-b border-red-600">禁用</button>
              </td>
            </tr>

            <!-- 用户2 -->
            <tr class="border-b-2 border-black hover:bg-gray-50">
              <td class="border-r-2 border-black px-4 py-4">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 bg-black text-white rounded-full flex items-center justify-center text-xs font-bold">S</div>
                  <div>
                    <div class="text-sm font-bold text-black">shuomc</div>
                    <div class="text-xs text-gray-600">op09@qq1361@qq.com</div>
                  </div>
                </div>
              </td>
              <td class="border-r-2 border-black px-4 py-4">
                <span class="text-xs font-bold text-purple-600 bg-purple-100 px-2 py-1 border-2 border-purple-600">admin</span>
              </td>
              <td class="border-r-2 border-black px-4 py-4">
                <span class="text-xs font-bold text-green-600">● Active</span>
              </td>
              <td class="border-r-2 border-black px-4 py-4 text-xs text-gray-600">2025/12/8</td>
              <td class="px-4 py-4 text-xs">
                <button class="font-bold text-blue-600 border-b border-blue-600">编辑</button>
                <span class="mx-2 text-gray-600">|</span>
                <button class="font-bold text-red-600 border-b border-red-600">禁用</button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 分页 -->
        <div class="border-t-2 border-black px-4 py-3 flex justify-between items-center bg-gray-50">
          <div class="text-xs text-gray-600">Showing 1 to 2 of 2 entries</div>
          <div class="flex gap-2">
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">Previous</button>
            <button class="px-3 py-1 border-2 border-black bg-white text-black text-xs font-bold">Next</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const users = ref([
  {
    id: 1,
    name: 'test',
    email: '116465957@qq.com',
    role: 'User',
    status: 'Active',
    registeredDate: '2025/12/9'
  },
  {
    id: 2,
    name: 'shuomc',
    email: 'op09@qq1361@qq.com',
    role: 'admin',
    status: 'Active',
    registeredDate: '2025/12/8'
  }
])

const searchQuery = ref('')
</script>

<style scoped>
</style>
