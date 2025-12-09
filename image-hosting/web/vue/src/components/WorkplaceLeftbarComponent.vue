<template>
  <div :class="[ 'h-full flex flex-col bg-white/50 dark:bg-slate-900/50 backdrop-blur-lg border-r border-slate-200/60 dark:border-slate-800/60 transition-colors duration-300', collapsed ? 'items-center' : '' ]">
    
    <div class="flex items-center justify-between px-3 py-3">
      <button @click="toggleCollapse" class="p-1.5 rounded-md hover:bg-slate-100 dark:hover:bg-slate-800/50 transition" :title="collapsed ? '展开' : '收起'">
        <svg v-if="!collapsed" xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-slate-500" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clip-rule="evenodd"/></svg>
        <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-slate-500" viewBox="0 0 20 20" fill="currentColor"><path d="M7 4a1 1 0 00-1 1v10a1 1 0 001 1h6a1 1 0 001-1V5a1 1 0 00-1-1H7z"/></svg>
      </button>
    </div>

    <div class="flex-1 overflow-y-auto py-3 px-1 space-y-1 custom-scrollbar">
      
      <div class="px-3 mb-2 text-xs font-semibold text-slate-400 uppercase tracking-wider">
        Main Menu
      </div>

      <router-link 
        v-for="item in menuItems" 
        :key="item.name"
        :to="{ name: item.name }"
        class="group flex items-center px-3 py-2.5 mb-1 rounded-xl transition-all duration-200 ease-in-out relative overflow-hidden"
        :class="itemClass(item)">
        <span v-if="isActive(item.name)" :class="['absolute top-1/2 -translate-y-1/2 rounded-r-full', collapsed ? 'left-0 w-1 h-8' : 'left-0 w-1 h-8', isActive(item.name) ? 'bg-indigo-500' : '']"></span>

        <component :is="item.icon" 
          class="w-5 h-5 mr-3 transition-colors duration-200"
          :class="iconClass(item)" 
        />
        
        <span v-show="!collapsed" class="truncate text-sm">{{ item.label }}</span>
      </router-link>

      <div class="my-4 border-t border-slate-200/60 dark:border-slate-700/60 mx-3"></div>
      
      <div class="px-3 mb-2 text-xs font-semibold text-slate-400 uppercase tracking-wider">
        System
      </div>

      <router-link 
        v-for="item in systemItems" 
        :key="item.name"
        :to="{ name: item.name }"
        class="group flex items-center px-3 py-2.5 mb-1 rounded-xl transition-all duration-200 ease-in-out relative"
        :class="itemClass(item)">
        <span v-if="isActive(item.name)" :class="['absolute top-1/2 -translate-y-1/2 rounded-r-full', collapsed ? 'left-0 w-1 h-8' : 'left-0 w-1 h-8', isActive(item.name) ? 'bg-indigo-500' : '']"></span>
        <component :is="item.icon" class="w-5 h-5 mr-3" :class="iconClass(item)" />
        <span v-show="!collapsed" class="truncate text-sm">{{ item.label }}</span>
      </router-link>

    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';
import { computed, ref, onMounted } from 'vue';
import {
  HomeIcon,
  PhotoIcon,
  ArrowUpTrayIcon,
  FolderIcon,
  CubeIcon, // 用于 NFT
  UserCircleIcon,
  Cog6ToothIcon,
  InformationCircleIcon,
  CreditCardIcon, // 用于余额/交易等
} from '@heroicons/vue/24/outline'; // 导入 outline 风格的图标

const route = useRoute();

const isActive = (menuItemName: string) => {
  return route.meta.activeMenu === menuItemName || route.name === menuItemName;
};
// 菜单数据
const menuItems = [
  { name: 'Recommended', label: '推荐', icon: HomeIcon },
  { name: 'MyImages', label: '我的图片', icon: PhotoIcon },
  { name: 'UploadImage', label: '图片上传', icon: ArrowUpTrayIcon },
  { name: 'MyFiles', label: '我的文件', icon: FolderIcon },
  { name: 'UploadFile', label: '文件上传', icon: ArrowUpTrayIcon },
  { name: 'NFTMarket', label: 'NFT市场', icon: CubeIcon },
  { name: 'MyNFT', label: '我的NFT', icon: CubeIcon },
  { name: 'NFTTransactions', label: 'NFT交易', icon: CreditCardIcon },
  { name: 'NFTBalance', label: 'NFT余额', icon: CreditCardIcon },
];

const systemItems = [
  { name: 'Profile', label: '个人中心', icon: UserCircleIcon },
  { name: 'Settings', label: '设置', icon: Cog6ToothIcon },
  { name: 'About', label: '关于', icon: InformationCircleIcon },
];

// 左侧折叠状态：在组件内管理并通知父组件
const emit = defineEmits<{
  (e: 'toggle-collapse', collapsed: boolean): void
}>();

const collapsed = ref(false);

const toggleCollapse = () => {
  collapsed.value = !collapsed.value;
  try {
    localStorage.setItem('leftCollapsed', collapsed.value ? '1' : '0');
  } catch {}
  emit('toggle-collapse', collapsed.value);
};

onMounted(() => {
  try {
    const v = localStorage.getItem('leftCollapsed');
    if (v === '1') collapsed.value = true;
  } catch {}
});

const itemClass = (item: any) => {
  const active = isActive(item.name);
  return [
    collapsed.value ? 'justify-center' : 'justify-start',
    'group flex items-center px-3 py-2.5 mb-1 rounded-xl transition-all duration-200 ease-in-out relative overflow-hidden',
    active
      ? 'bg-indigo-50 text-indigo-600 dark:bg-indigo-500/10 dark:text-indigo-400 font-semibold'
      : 'text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800/50 hover:text-slate-900 dark:hover:text-slate-200'
  ];
};

const iconClass = (item: any) => {
  return isActive(item.name) ? 'text-indigo-600 dark:text-indigo-400' : 'text-slate-400 group-hover:text-slate-600 dark:group-hover:text-slate-300';
};
</script>

<style scoped>
/* 自定义滚动条 (可选) */
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.3);
  border-radius: 4px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: rgba(156, 163, 175, 0.5);
}
</style>