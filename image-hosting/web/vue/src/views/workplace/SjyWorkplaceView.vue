<template>
  <div class="flex h-screen w-full overflow-hidden bg-slate-50 dark:bg-slate-950 transition-colors duration-300 relative">
    
    <div class="absolute inset-0 z-0 pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-500/10 blur-[120px]"></div>
      <div class="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-500/10 blur-[120px]"></div>
    </div>

    <aside :class="[ leftCollapsed ? 'w-20' : 'w-64', 'flex-shrink-0 z-20 hidden md:block transition-all duration-300' ]">
      <WorkplaceLeftbarComponent @toggle-collapse="onLeftToggle" />
    </aside>

    <div class="flex-1 flex flex-col min-w-0 relative z-10">
      
      <WorkplaceTopbarComponent />

      <main class="flex-1 overflow-y-auto p-4 scroll-smooth scrollbar-hide">
        <div class="mx-auto max-w-7xl h-full">
           <router-view v-slot="{ Component }">
            <transition name="fade-slide" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import WorkplaceLeftbarComponent from '@/components/WorkplaceLeftbarComponent.vue';
import WorkplaceTopbarComponent from '@/components/WorkplaceTopbarComponent.vue';
import { ref, onMounted } from 'vue';

const leftCollapsed = ref(false);
const onLeftToggle = (collapsed: boolean) => {
  leftCollapsed.value = collapsed;
};

onMounted(() => {
  try {
    const v = localStorage.getItem('leftCollapsed');
    if (v === '1') leftCollapsed.value = true;
  } catch {}
});
</script>

<style scoped>
/* 简单的页面切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 隐藏滚动条但保留功能 (Chrome/Safari) */
.scrollbar-hide::-webkit-scrollbar {
    display: none;
}
.scrollbar-hide {
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
}
</style>

<style scoped></style>
