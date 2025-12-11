<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-900 flex">
    <!-- Sidebar -->
    <AdminSidebar :is-collapsed="isSidebarCollapsed" @toggle="toggleSidebar" />

    <!-- Main Content Wrapper -->
    <div class="flex-1 flex flex-col min-w-0 transition-all duration-300" :class="{ 'lg:ml-64': !isSidebarCollapsed, 'lg:ml-20': isSidebarCollapsed , 'lg:mr-64': !isSidebarCollapsed, 'lg:mr-20': isSidebarCollapsed }">
      
      <!-- Topbar -->
      <AdminTopbar @toggle-sidebar="toggleSidebar" />

      <!-- Page Content -->
      <main class="flex-1 p-4 md:p-6 lg:p-8 overflow-y-auto overflow-x-hidden">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
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
