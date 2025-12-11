<template>
  <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Toolbar -->
    <div class="p-6 border-b border-slate-100 dark:border-slate-700 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div class="relative max-w-sm w-full">
        <input 
          type="text" 
          placeholder="搜索用户..." 
          class="w-full pl-10 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
        />
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-slate-400 absolute left-3 top-2.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
      <div class="flex gap-2">
        <button class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-medium transition-colors flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          添加用户
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="bg-slate-50 dark:bg-slate-900/50 text-slate-500 dark:text-slate-400 text-xs uppercase tracking-wider">
            <th class="px-6 py-4 font-semibold">用户</th>
            <th class="px-6 py-4 font-semibold">角色</th>
            <th class="px-6 py-4 font-semibold">状态</th>
            <th class="px-6 py-4 font-semibold">注册时间</th>
            <th class="px-6 py-4 font-semibold text-right">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 dark:divide-slate-700">
          <tr v-for="user in users" :key="user.id" class="hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors">
            <td class="px-6 py-4">
              <div class="flex items-center">
                <img :src="user.avatar" class="h-10 w-10 rounded-full object-cover border border-slate-200 dark:border-slate-600" alt="" />
                <div class="ml-4">
                  <div class="text-sm font-medium text-slate-900 dark:text-white">{{ user.name }}</div>
                  <div class="text-xs text-slate-500 dark:text-slate-400">{{ user.email }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4">
              <span class="px-2 py-1 text-xs font-semibold rounded-full bg-blue-50 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400 border border-blue-100 dark:border-blue-800">
                {{ user.role }}
              </span>
            </td>
            <td class="px-6 py-4">
              <span v-if="user.status === 'Active'" class="px-2 py-1 text-xs font-semibold rounded-full bg-green-50 text-green-600 dark:bg-green-900/20 dark:text-green-400 border border-green-100 dark:border-green-800 flex w-fit items-center gap-1">
                <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span> Active
              </span>
              <span v-else class="px-2 py-1 text-xs font-semibold rounded-full bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-400 border border-slate-200 dark:border-slate-600">
                Inactive
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-slate-500 dark:text-slate-400">
              {{ user.joinedAt }}
            </td>
            <td class="px-6 py-4 text-right">
              <button class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 text-sm font-medium mr-3">编辑</button>
              <button class="text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300 text-sm font-medium">禁用</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="p-4 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between">
      <span class="text-sm text-slate-500 dark:text-slate-400">Showing 1 to 5 of 12 entries</span>
      <div class="flex gap-2">
        <button class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-600 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-50">Previous</button>
        <button class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-600 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">Next</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// Mock Data
const users = ref([
  { id: 1, name: 'Alice Johnson', email: 'alice@example.com', role: 'Admin', status: 'Active', joinedAt: '2023-10-15', avatar: 'https://i.pravatar.cc/150?u=a042581f4e29026024d' },
  { id: 2, name: 'Bob Smith', email: 'bob@example.com', role: 'User', status: 'Active', joinedAt: '2023-11-02', avatar: 'https://i.pravatar.cc/150?u=a042581f4e29026704d' },
  { id: 3, name: 'Charlie Brown', email: 'charlie@example.com', role: 'User', status: 'Inactive', joinedAt: '2023-12-10', avatar: 'https://i.pravatar.cc/150?u=a04258114e29026302d' },
  { id: 4, name: 'Diana Prince', email: 'diana@example.com', role: 'User', status: 'Active', joinedAt: '2024-01-05', avatar: 'https://i.pravatar.cc/150?u=a042581f4e29026024d' },
  { id: 5, name: 'Evan Wright', email: 'evan@example.com', role: 'User', status: 'Active', joinedAt: '2024-02-20', avatar: 'https://i.pravatar.cc/150?u=a048581f4e29026024d' },
]);
</script>
