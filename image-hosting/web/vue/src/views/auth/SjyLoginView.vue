<template>
  <div class="relative min-h-screen w-full flex flex-col overflow-x-hidden font-sans">
    
    <div class="fixed inset-0 -z-10 w-full h-full bg-white">
      <div class="absolute inset-0 bg-[radial-gradient(125%_125%_at_50%_10%,#fff_40%,#6366f1_100%)] opacity-80"></div>
      </div>

    <header class="w-full px-6 py-6 flex justify-between items-center">
        <div class="text-xl font-bold text-indigo-600 tracking-tight select-none">Image Hosting</div>
        <nav>
          <a href="https://github.com/anoixa/image-hosting" target="_blank" rel="noopener noreferrer"
            class="p-2 rounded-full hover:bg-gray-100/50 transition-colors duration-300 group inline-flex">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
              stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"
              class="text-gray-700 group-hover:text-indigo-600 transition-colors">
              <path d="M15 22v-4a4.8 4.8 0 0 0-1-3.5c3 0 6-2 6-5.5.08-1.25-.27-2.48-1-3.5.28-1.15.28-2.35 0-3.5 0 0-1 0-3 1.5-2.64-.5-5.36-.5-8 0C6 2 5 2 5 2c-.3 1.15-.3 2.35 0 3.5A5.403 5.403 0 0 0 4 9c0 3.5 3 5.5 6 5.5-.39.49-.68 1.05-.85 1.65-.17.6-.22 1.23-.15 1.85v4"></path>
              <path d="M9 18c-4.51 2-5-2-7-2"></path>
            </svg>
          </a>
        </nav>
    </header>

    <main class="flex-grow flex flex-col items-center justify-center px-4 py-10 w-full max-w-7xl mx-auto">
      
      <div class="text-center mb-10 max-w-2xl mx-auto animate-fade-in-up">
        <h2 class="text-4xl sm:text-5xl font-extrabold text-gray-900 tracking-tight mb-4 drop-shadow-sm">
          Image-Hosting <span class="text-indigo-600">在线图床</span>
        </h2>
        <p class="text-lg text-gray-600 font-medium">
          快捷部署，一次上传，永久保存
        </p>
      </div>

      <div class="w-full flex justify-center mb-8 animate-fade-in-up delay-100">
        <LoginComponent />
      </div>

      <div class="flex flex-col items-center gap-4 animate-fade-in-up delay-200">
        <div class="text-sm font-medium text-gray-500">还没有账户？</div>
        <router-link to="/register" class="group">
          <button
            class="relative overflow-hidden rounded-xl bg-white px-8 py-2.5 text-sm font-semibold text-indigo-600 shadow-sm ring-1 ring-inset ring-indigo-200 
                   hover:bg-indigo-50 hover:ring-indigo-300 transition-all duration-300 ease-out hover:-translate-y-0.5">
            <span class="flex items-center gap-2">
              立即注册体验
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                class="transition-transform group-hover:translate-x-1">
                <path d="M5 12h14"></path>
                <path d="m12 5 7 7-7 7"></path>
              </svg>
            </span>
          </button>
        </router-link>
      </div>

    </main>

    <footer class="w-full py-6 mt-auto">
      <div class="flex flex-col items-center justify-center gap-4 text-center">
        <div class="flex space-x-6 text-sm text-gray-500">
          <a href="#" class="hover:text-indigo-600 transition-colors">关于我们</a>
          <a href="#" class="hover:text-indigo-600 transition-colors">服务条款</a>
          <a href="#" class="hover:text-indigo-600 transition-colors">隐私政策</a>
          <a href="#" class="hover:text-indigo-600 transition-colors">帮助中心</a>
        </div>
        <p class="text-xs text-gray-400">© 2024 Image-Hosting. All rights reserved.</p>
      </div>
    </footer>

  </div>
</template>

<script lang="ts" setup>
import LoginComponent from '@/components/LoginComponent.vue'
</script>

<style lang="scss" scoped>
/* 简单的淡入上浮动画 */
.animate-fade-in-up {
  animation: fadeInUp 0.8s ease-out forwards;
  opacity: 0;
  transform: translateY(20px);
}

.delay-100 { animation-delay: 0.1s; }
.delay-200 { animation-delay: 0.2s; }

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>