<template>
  <div class="min-h-screen bg-slate-50 dark:bg-slate-900/30 overflow-hidden rounded-3xl border border-slate-300/80 dark:border-slate-700/30">
    <!-- Hero Section -->
    <div class="relative py-20 px-6 lg:px-8">
      <div class="absolute inset-0 z-0">
        <div class="absolute top-0 -left-4 w-72 h-72 bg-blue-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob"></div>
        <div class="absolute top-0 -right-4 w-72 h-72 bg-purple-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000"></div>
        <div class="absolute -bottom-8 left-20 w-72 h-72 bg-pink-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-4000"></div>
      </div>

      <div class="relative z-10 max-w-4xl mx-auto text-center">
        <h1 class="text-4xl md:text-6xl font-extrabold text-slate-900 dark:text-white mb-6 tracking-tight">
          Image Hosting <span class="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-purple-600">Webase</span>
        </h1>
        <p class="text-xl text-slate-600 dark:text-slate-400 mb-10 leading-relaxed">
          一个融合了区块链技术的现代图床解决方案。我们不仅提供高效的图片托管服务，更通过区块链确权为每一张灵感助力。
        </p>
        <div class="flex justify-center gap-4">
          <el-button type="primary" size="large" round class="px-8 shadow-lg shadow-blue-500/30" @click="$router.push('/workplace/recommended')">
            立即开始
          </el-button>
          <el-button size="large" round class="px-8 border-slate-300 dark:border-slate-700 dark:bg-slate-800">
            了解更多
          </el-button>
        </div>
      </div>
    </div>

    <!-- Core Features -->
    <div class="max-w-7xl mx-auto px-6 lg:px-8 py-16">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <!-- Feature 1 -->
        <div class="bg-white dark:bg-slate-800 p-8 rounded-3xl shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-xl transition-shadow duration-300">
          <div class="w-12 h-12 bg-blue-100 dark:bg-blue-900/30 rounded-2xl flex items-center justify-center mb-6">
            <el-icon class="text-2xl text-blue-600"><Picture /></el-icon>
          </div>
          <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-3">极致托管</h3>
          <p class="text-slate-600 dark:text-slate-400">
            基于 MinIO 的高性能对象存储，支持多格式图片上传、自动缩略图生成以及极速的分发体验。
          </p>
        </div>

        <!-- Feature 2 -->
        <div class="bg-white dark:bg-slate-800 p-8 rounded-3xl shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-xl transition-shadow duration-300">
          <div class="w-12 h-12 bg-purple-100 dark:bg-purple-900/30 rounded-2xl flex items-center justify-center mb-6">
            <el-icon class="text-2xl text-purple-600"><Cpu /></el-icon>
          </div>
          <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-3">区块链确权</h3>
          <p class="text-slate-600 dark:text-slate-400">
            集成 FISCO BCOS 区块链，一键将图片铸造为 NFT。通过智能合约记录图片所有权，让原创更有价值。
          </p>
        </div>

        <!-- Feature 3 -->
        <div class="bg-white dark:bg-slate-800 p-8 rounded-3xl shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-xl transition-shadow duration-300">
          <div class="w-12 h-12 bg-pink-100 dark:bg-pink-900/30 rounded-2xl flex items-center justify-center mb-6">
            <el-icon class="text-2xl text-pink-600"><Lock /></el-icon>
          </div>
          <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-3">安全合规</h3>
          <p class="text-slate-600 dark:text-slate-400">
            完善的角色权限控制 (Sa-Token) 与操作审计记录，为您提供安全、私密且可追溯的图片管理环境。
          </p>
        </div>
      </div>
    </div>

    <!-- Tech Stack Section -->
    <div class="py-20 bg-slate-100/10 dark:bg-slate-800/20 rounded-3xl border border-slate-200/50 dark:border-slate-700/50">
      <div class="max-w-7xl mx-auto px-6 lg:px-8 text-center">
        <h2 class="text-3xl font-bold text-slate-900 dark:text-white mb-16 underline decoration-blue-500/30 underline-offset-8">驱动我们的技术</h2>
        <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-6">
          <!-- Tech Tile 1 (Spring Boot) -->
          <div class="group relative p-6 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 transition-all duration-300 hover:-translate-y-2 hover:shadow-xl hover:border-green-500/50 dark:hover:border-green-500/50 cursor-default overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-green-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-2xl"></div>
            <span class="relative z-10 text-lg font-black tracking-tight text-slate-600 dark:text-slate-400 group-hover:text-green-600 dark:group-hover:text-green-400 transition-colors">Spring Boot</span>
          </div>

          <!-- Tech Tile 2 (Vue 3) -->
          <div class="group relative p-6 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 transition-all duration-300 hover:-translate-y-2 hover:shadow-xl hover:border-emerald-400/50 dark:hover:border-emerald-400/50 cursor-default overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-emerald-400/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-2xl"></div>
            <span class="relative z-10 text-lg font-black tracking-tight text-slate-600 dark:text-slate-400 group-hover:text-emerald-500 dark:group-hover:text-emerald-400 transition-colors">Vue 3 + Vite</span>
          </div>

          <!-- Tech Tile 3 (FISCO BCOS) -->
          <div class="group relative p-6 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 transition-all duration-300 hover:-translate-y-2 hover:shadow-xl hover:border-blue-500/50 dark:hover:border-blue-500/50 cursor-default overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-blue-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-2xl"></div>
            <span class="relative z-10 text-lg font-black tracking-tight text-slate-600 dark:text-slate-400 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">FISCO BCOS</span>
          </div>

          <!-- Tech Tile 4 (PostgreSQL) -->
          <div class="group relative p-6 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 transition-all duration-300 hover:-translate-y-2 hover:shadow-xl hover:border-indigo-500/50 dark:hover:border-indigo-500/50 cursor-default overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-indigo-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-2xl"></div>
            <span class="relative z-10 text-lg font-black tracking-tight text-slate-600 dark:text-slate-400 group-hover:text-indigo-600 dark:group-hover:text-indigo-400 transition-colors">PostgreSQL</span>
          </div>

          <!-- Tech Tile 5 (Redis) -->
          <div class="group relative p-6 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 transition-all duration-300 hover:-translate-y-2 hover:shadow-xl hover:border-red-500/50 dark:hover:border-red-500/50 cursor-default overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-red-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-2xl"></div>
            <span class="relative z-10 text-lg font-black tracking-tight text-slate-600 dark:text-slate-400 group-hover:text-red-600 dark:group-hover:text-red-400 transition-colors">Redis</span>
          </div>

          <!-- Tech Tile 6 (MinIO) -->
          <div class="group relative p-6 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 transition-all duration-300 hover:-translate-y-2 hover:shadow-xl hover:border-orange-500/50 dark:hover:border-orange-500/50 cursor-default overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-orange-500/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity rounded-2xl"></div>
            <span class="relative z-10 text-lg font-black tracking-tight text-slate-600 dark:text-slate-400 group-hover:text-orange-600 dark:group-hover:text-orange-400 transition-colors">MinIO</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Footer-like Contact -->
    <div class="py-16 text-center">
      <p class="text-slate-500 dark:text-slate-500 text-sm">
         &copy; 2026 Image Hosting Webase. 由区块链技术与激情驱动。
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Picture, Cpu, Lock } from '@element-plus/icons-vue'
</script>

<style scoped>
.about {
  padding: 0;
}

@keyframes blob {
  0% { transform: translate(0px, 0px) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
  100% { transform: translate(0px, 0px) scale(1); }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}

.animation-delay-4000 {
  animation-delay: 4s;
}
</style>

