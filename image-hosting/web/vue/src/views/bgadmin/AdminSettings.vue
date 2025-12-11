<template>
  <div class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
    
    <!-- General Settings -->
    <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden">
      <div class="p-6 border-b border-slate-100 dark:border-slate-700">
        <h3 class="text-lg font-semibold text-slate-900 dark:text-white">基础设置</h3>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">管理网站的基本信息和全局配置</p>
      </div>
      <div class="p-6 space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">网站名称</label>
            <input type="text" v-model="settings.siteName" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">网站描述</label>
            <input type="text" v-model="settings.siteDesc" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
        </div>
        
        <div class="flex items-center justify-between p-4 bg-slate-50 dark:bg-slate-900/50 rounded-xl">
          <div>
            <h4 class="text-sm font-medium text-slate-900 dark:text-white">开放注册</h4>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-1">允许新用户注册账号</p>
          </div>
          <label class="relative inline-flex items-center cursor-pointer">
            <input type="checkbox" v-model="settings.allowRegister" class="sr-only peer">
            <div class="w-11 h-6 bg-slate-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-indigo-300 dark:peer-focus:ring-indigo-800 rounded-full peer dark:bg-slate-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-indigo-600"></div>
          </label>
        </div>
      </div>
    </div>

    <!-- Storage Settings -->
    <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden">
      <div class="p-6 border-b border-slate-100 dark:border-slate-700">
        <h3 class="text-lg font-semibold text-slate-900 dark:text-white">存储设置</h3>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">配置图片存储后端 (MinIO/S3)</p>
      </div>
      <div class="p-6 space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">Endpoint</label>
            <input type="text" v-model="settings.storage.endpoint" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">Bucket Name</label>
            <input type="text" v-model="settings.storage.bucket" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">Access Key</label>
            <input type="password" v-model="settings.storage.accessKey" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">Secret Key</label>
            <input type="password" v-model="settings.storage.secretKey" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
        </div>
      </div>
      <div class="px-6 py-4 bg-slate-50 dark:bg-slate-900/30 border-t border-slate-100 dark:border-slate-700 flex justify-end">
        <button class="px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-medium transition-colors shadow-lg shadow-indigo-500/30">保存更改</button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const settings = ref({
  siteName: 'ImageHost Pro',
  siteDesc: 'Secure & Fast Image Hosting with Blockchain',
  allowRegister: true,
  storage: {
    endpoint: 'http://minio:9000',
    bucket: 'images',
    accessKey: 'minioadmin',
    secretKey: 'minioadmin'
  }
});
</script>
