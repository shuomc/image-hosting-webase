<template>
  <header class="h-16 w-full px-6 flex justify-between items-center 
                 bg-white/70 dark:bg-slate-900/70 backdrop-blur-xl 
                 border-b border-slate-200/60 dark:border-slate-800/60
                 transition-colors duration-300 relative z-30">
    
    <div class="flex items-center gap-3">
      <div class="flex flex-col">
        <span class="text-xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600 dark:from-indigo-400 dark:to-purple-400 tracking-tight">
          Image Hosting
        </span>
      </div>
    </div>

    <div class="flex items-center gap-2">
      
      <button @click="toggleTheme" 
        class="p-2 rounded-full text-slate-500 hover:bg-slate-100 dark:text-slate-400 dark:hover:bg-slate-800 transition-colors focus:outline-none"
        title="切换主题">
        <SunIcon v-if="isDark" class="w-5 h-5 text-yellow-400" />
        <MoonIcon v-else class="w-5 h-5 text-indigo-600" />
      </button>

      <div class="h-6 w-px bg-slate-200 dark:bg-slate-700 mx-1"></div>

      <div class="relative" ref="userDropdownRef">
        <button class="flex items-center gap-3 focus:outline-none group" @click="toggleDropdown($event)">
          <div class="flex flex-col items-end hidden sm:flex">
            <span class="text-sm font-semibold text-slate-700 dark:text-slate-200 leading-tight">
              {{ userStore.userInfo?.userName || 'User' }}
            </span>
            <span class="text-[10px] text-slate-400 uppercase tracking-wider">User</span>
          </div>
          
          <div class="h-9 w-9 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-500 text-white flex items-center justify-center text-sm font-bold shadow-md shadow-indigo-500/20 ring-2 ring-white dark:ring-slate-800 transition-transform group-hover:scale-105">
            {{ userStore.userInfo?.userName ? userStore.userInfo.userName.charAt(0).toUpperCase() : '?' }}
          </div>
          
          <ChevronDownIcon class="h-4 w-4 text-slate-400 transition-transform duration-200" :class="{'rotate-180': isDropdownOpen}" />
        </button>

        <transition name="dropdown">
          <div v-if="isDropdownOpen"
            class="absolute right-0 mt-3 w-70 bg-white dark:bg-slate-800 rounded-xl shadow-2xl border border-slate-100 dark:border-slate-700 overflow-hidden origin-top-right z-50">
            
            <div class="p-4 border-b border-slate-100 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-800/50">
              <p class="text-sm font-bold text-slate-800 dark:text-slate-100">当前账号</p>
              <p class="text-xs text-slate-500 dark:text-slate-400 truncate mt-1">{{userStore.userInfo?.userEmail || 'no-email@example.com' }}</p>
              <p class="text-xs text-slate-500 dark:text-slate-400 truncate mt-1">{{userStore.userInfo?.userId || 'no-email@example.com' }}</p>
            </div>

            <div class="p-2 pt-1 pb-1 space-y-0.5">
              
              <button @click= "router.push({ name: 'Profile' })"
                class="flex items-center w-full gap-3 px-3 py-2 text-left text-sm text-purple-600 hover:bg-purple-50 dark:text-purple-400 dark:hover:bg-purple-900/20 rounded-xl transition-colors duration-200">
                <UserIcon class="w-5 h-5 text-purple-600 dark:text-purple-400" />
                个人中心
              </button>

              <button @click="handleLogout"
                class="flex items-center w-full gap-3 px-3 py-2 text-left text-sm text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-xl transition-colors duration-200">
                <ArrowRightOnRectangleIcon class="w-5 h-5 text-red-500" />
                退出登录
              </button>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </header>
</template>

<script lang="ts" setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { SunIcon, MoonIcon, ChevronDownIcon, UserIcon, ArrowRightOnRectangleIcon } from '@heroicons/vue/24/outline';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { logout } from '@/api/auth/login';
import { ElMessage } from 'element-plus';
import { ro } from 'element-plus/es/locales.mjs';

const router = useRouter();
const userStore = useUserStore();
const isDropdownOpen = ref(false);
const userDropdownRef = ref<HTMLElement | null>(null);

// === 主题切换逻辑 (支持系统首选项变更) ===
const isDark = ref(false);

const toggleTheme = () => {
  isDark.value = !isDark.value;
  if (isDark.value) {
    document.documentElement.classList.add('dark');
    localStorage.setItem('theme', 'dark');
  } else {
    document.documentElement.classList.remove('dark');
    localStorage.setItem('theme', 'light');
  }
};

const initTheme = () => {
  const savedTheme = localStorage.getItem('theme');
  const systemDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
  
  if (savedTheme === 'dark' || (!savedTheme && systemDark)) {
    isDark.value = true;
    document.documentElement.classList.add('dark');
  } else {
    isDark.value = false;
    document.documentElement.classList.remove('dark');
  }
};

// 监听系统主题变化，实时同步（现代浏览器）
let mq: MediaQueryList | null = null;
let mqListener: ((e: MediaQueryListEvent) => void) | null = null;
const setupSystemThemeListener = () => {
  if (typeof window === 'undefined' || !('matchMedia' in window)) return;
  mq = window.matchMedia('(prefers-color-scheme: dark)');
  mqListener = (e: MediaQueryListEvent) => {
    // 仅在用户未强制设置（localStorage 没有值）时遵循系统变化
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) return;
    isDark.value = e.matches;
    if (isDark.value) document.documentElement.classList.add('dark');
    else document.documentElement.classList.remove('dark');
  };
  if ('addEventListener' in mq) mq.addEventListener('change', mqListener);
  else mq.addListener && mq.addListener(mqListener as any);
};
// =================

const toggleDropdown = (event?: MouseEvent) => {
  // prevent document click handler from immediately closing the dropdown
  if (event) event.stopPropagation();
  isDropdownOpen.value = !isDropdownOpen.value;
};

const handleLogout = async () => {
  try {
    await logout();
    userStore.clearLoginState();
    router.push('/auth/login');
    ElMessage.success('已安全退出');
  } catch (error) {
    console.error('退出登录失败:', error);
    ElMessage.error('退出登录异常');
  }
};

const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as Node;
  if (userDropdownRef.value && !userDropdownRef.value.contains(target)) {
    isDropdownOpen.value = false;
  }
};

onMounted(async () => {
  initTheme(); // 初始化主题
  setupSystemThemeListener();
  if (!userStore.userInfoLoaded) {
    await userStore.loadUserInfo();
  }
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
  if (mq && mqListener) {
    if ('removeEventListener' in mq) mq.removeEventListener('change', mqListener);
    else mq.removeListener && mq.removeListener(mqListener as any);
  }
});
</script>

<style scoped>
.dropdown-enter-active,
.dropdown-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-5px) scale(0.95);
}
</style>