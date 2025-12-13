<template>
  <header class="h-16 bg-white/80 dark:bg-slate-900/80 backdrop-blur-md border-b border-slate-200 dark:border-slate-800 flex items-center justify-between px-6 sticky top-0 z-10 transition-colors duration-300">
    <!-- Left: Breadcrumbs or Title -->
    <div class="flex items-center">
      <h2 class="text-lg font-semibold text-slate-800 dark:text-white capitalize">
        {{ currentRouteName }}
      </h2>
    </div>

    <!-- Right: Actions -->
    <div class="flex items-center gap-4">
      <!-- Theme Toggle (Optional, if global theme store exists) -->
      
      <!-- Notifications -->
      <button class="p-2 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-full transition-colors relative">
        <span class="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full animate-pulse"></span>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
        </svg>
      </button>

      <!-- User Profile Dropdown -->
      <div class="relative group">
        <button class="flex items-center gap-3 p-1.5 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors">
          <div class="w-8 h-8 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 text-white flex items-center justify-center text-xs font-bold border border-slate-200 dark:border-slate-700">
            A
          </div>
          <span class="text-sm font-medium text-slate-700 dark:text-slate-200 hidden sm:block">Administrator</span>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
        </button>

        <!-- Dropdown Menu -->
        <div class="absolute right-0 mt-2 w-48 bg-white dark:bg-slate-800 rounded-xl shadow-xl border border-slate-100 dark:border-slate-700 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 transform origin-top-right z-50">
          <div class="py-1">
            <a href="#" class="block px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700/50">个人资料</a>
            <a href="#" class="block px-4 py-2 text-sm text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700/50">设置</a>
            <div class="border-t border-slate-100 dark:border-slate-700 my-1"></div>
            <button @click="$emit('logout')" class="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20">退出登录</button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const emit = defineEmits(['logout']);

const currentRouteName = computed(() => {
  const nameMap: Record<string, string> = {
    'AdminDashboard': '仪表盘',
    'AdminUsers': '用户管理',
    'AdminImages': '图片管理',
    'AdminSettings': '系统设置'
  };
  return nameMap[route.name as string] || 'Admin Panel';
});
</script>
