<template>
  <aside 
    class="h-screen bg-white dark:bg-slate-900 border-r border-slate-200 dark:border-slate-800 flex flex-col transition-all duration-300 z-20"
    :class="collapsed ? 'w-20' : 'w-64'"
  >
    <!-- Logo Area -->
    <div class="h-16 flex items-center justify-center border-b border-slate-100 dark:border-slate-800">
      <div class="flex items-center gap-2" v-if="!collapsed">
        <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center text-white font-bold shadow-lg shadow-indigo-500/30">
          A
        </div>
        <span class="font-bold text-lg text-slate-800 dark:text-white tracking-tight">AdminPanel</span>
      </div>
      <div v-else class="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center text-white font-bold shadow-lg">
        A
      </div>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 p-4 space-y-2 overflow-y-auto scrollbar-hide">
      <router-link 
        v-for="item in menuItems" 
        :key="item.path" 
        :to="item.path"
        class="flex items-center px-3 py-3 rounded-xl transition-all duration-200 group relative overflow-hidden"
        :class="isActive(item.path) 
          ? 'bg-indigo-50 text-indigo-600 dark:bg-indigo-900/20 dark:text-indigo-400 font-medium shadow-sm' 
          : 'text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-slate-200'"
      >
        <component :is="item.icon" class="w-6 h-6 flex-shrink-0 transition-transform duration-300 group-hover:scale-110" />
        
        <span 
          v-if="!collapsed" 
          class="ml-3 whitespace-nowrap transition-opacity duration-300"
        >
          {{ item.name }}
        </span>

        <!-- Tooltip for collapsed state -->
        <div 
          v-if="collapsed"
          class="absolute left-full ml-2 px-2 py-1 bg-slate-800 text-white text-xs rounded opacity-0 group-hover:opacity-100 pointer-events-none transition-opacity z-50 whitespace-nowrap"
        >
          {{ item.name }}
        </div>
      </router-link>
    </nav>

    <!-- Footer / Collapse Toggle -->
    <div class="p-4 border-t border-slate-100 dark:border-slate-800">
      <button 
        @click="$emit('toggle-collapse')"
        class="w-full flex items-center justify-center p-2 rounded-lg text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 transform transition-transform duration-300" :class="collapsed ? 'rotate-180' : ''" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 19l-7-7 7-7m8 14l-7-7 7-7" />
        </svg>
      </button>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { 
  HomeIcon, 
  UsersIcon, 
  PhotoIcon, 
  Cog6ToothIcon, 
  CubeTransparentIcon,
  BellIcon
} from '@heroicons/vue/24/outline';

const props = defineProps<{
  collapsed: boolean
}>();

const emit = defineEmits(['toggle-collapse']);
const route = useRoute();

const menuItems = [
  { name: '仪表盘', path: '/admin/dashboard', icon: HomeIcon },
  { name: '用户管理', path: '/admin/users', icon: UsersIcon },
  { name: '图片管理', path: '/admin/images', icon: PhotoIcon },
  { name: '公告管理', path: '/admin/notices', icon: BellIcon },
  { name: '系统设置', path: '/admin/settings', icon: Cog6ToothIcon },
];

const isActive = (path: string) => {
  return route.path.startsWith(path);
};
</script>

<style scoped>
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}
.scrollbar-hide {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>
