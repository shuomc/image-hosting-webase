<template>
  <div class="h-screen bg-slate-50 dark:bg-slate-950 flex overflow-hidden">
    <!-- Sidebar -->
    <AdminSidebar 
      class="flex-shrink-0" 
      :collapsed="isSidebarCollapsed" 
      @toggle-collapse="toggleSidebar" 
    />

    <!-- Main Content Wrapper -->
    <div class="flex-1 flex flex-col min-w-0 h-full overflow-hidden transition-all duration-300">
      
      <!-- Topbar -->
      <AdminTopbar 
        class="flex-shrink-0" 
        @toggle-sidebar="toggleSidebar" 
      />

      <!-- Page Content -->
      <main class="flex-1 overflow-y-auto overflow-x-hidden p-4 md:p-6 lg:p-10 bg-slate-50 dark:bg-slate-950">
        <div class="max-w-[1600px] mx-auto">
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
import { ref } from 'vue';
import AdminSidebar from '@/components/admin/AdminSidebar.vue';
import AdminTopbar from '@/components/admin/AdminTopbar.vue';

const isSidebarCollapsed = ref(false);

const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};
</script>

<style scoped>
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
</style>
