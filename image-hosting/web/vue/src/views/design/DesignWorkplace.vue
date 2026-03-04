<template>
  <div class="flex h-screen w-full overflow-hidden bg-white">
    
    <!-- Sidebar -->
    <aside :class="[leftCollapsed ? 'w-16' : 'w-48', 'flex-shrink-0 z-20 hidden md:block bg-gray-200 border-r-2 border-black']">
      <div class="h-full flex flex-col border-b-2 border-black p-2">
        <!-- Logo -->
        <div class="h-12 bg-gray-400 border-2 border-black flex items-center justify-center font-bold text-black mb-4">
          IH
        </div>
        
        <!-- Toggle Button -->
        <button 
          @click="onLeftToggle(!leftCollapsed)"
          class="h-8 bg-black text-white border border-black font-bold text-xs w-full mb-4"
        >
          {{ leftCollapsed ? '>' : '<' }}
        </button>

        <!-- Menu Items -->
        <div v-if="!leftCollapsed" class="flex-1 space-y-2">
          <div class="h-8 bg-gray-300 border-2 border-black flex items-center px-2 text-black text-xs font-bold">菜单</div>
          <div class="h-8 bg-white border-2 border-black flex items-center px-2 text-black text-xs">项目 1</div>
          <div class="h-8 bg-white border-2 border-black flex items-center px-2 text-black text-xs">项目 2</div>
          <div class="h-8 bg-white border-2 border-black flex items-center px-2 text-black text-xs">项目 3</div>
        </div>
      </div>
    </aside>

    <!-- Main Content -->
    <div class="flex-1 flex flex-col min-w-0">
      
      <!-- Topbar -->
      <header class="h-12 border-b-2 border-black bg-gray-100 flex items-center justify-between px-4">
        <div class="text-black font-bold text-sm">工作台</div>
        <div class="h-8 bg-gray-400 border-2 border-black px-3 flex items-center text-black text-xs font-bold">用户菜单</div>
      </header>

      <!-- Main Area -->
      <main class="flex-1 overflow-y-auto p-4 bg-white">
        <div class="mx-auto max-w-7xl h-full">
          <div class="border-2 border-black p-4 bg-gray-50">
            <div class="text-black font-bold mb-4">内容区域</div>
            <div class="grid grid-cols-3 gap-4">
              <div v-for="i in 6" :key="i" class="bg-gray-300 border-2 border-black aspect-square flex items-center justify-center">
                <span class="text-black font-bold">卡片 {{ i }}</span>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
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
/* 简单样式 */
</style>
