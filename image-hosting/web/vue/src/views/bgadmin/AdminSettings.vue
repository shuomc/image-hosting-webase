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
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">API Endpoint</label>
            <input type="text" v-model="settings.storage.endpoint" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">Console URL</label>
            <div class="flex gap-2">
                <input type="text" v-model="settings.storage.consoleUrl" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
                <a :href="settings.storage.consoleUrl" target="_blank" class="px-4 py-2 bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 dark:hover:bg-slate-600 rounded-xl transition-colors flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-external-link"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/><polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/></svg>
                </a>
            </div>
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
    </div>

    <!-- Blockchain Settings -->
    <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden">
      <div class="p-6 border-b border-slate-100 dark:border-slate-700">
        <h3 class="text-lg font-semibold text-slate-900 dark:text-white">区块链设置</h3>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">配置 WeBASE 和区块链服务连接</p>
      </div>
      <div class="p-6 space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">WeBASE 管理台地址</label>
            <div class="flex gap-2">
                <input type="text" v-model="settings.blockchain.webaseUrl" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
                <a :href="settings.blockchain.webaseUrl" target="_blank" class="px-4 py-2 bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 dark:hover:bg-slate-600 rounded-xl transition-colors flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-external-link"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/><polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/></svg>
                </a>
            </div>
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">区块链服务 API 地址</label>
            <input type="text" v-model="settings.blockchain.apiUrl" class="w-full px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all" />
          </div>
        </div>
      </div>
      <div class="px-6 py-4 bg-slate-50 dark:bg-slate-900/30 border-t border-slate-100 dark:border-slate-700 flex justify-end">
        <button @click="saveSettings" :disabled="loading" class="px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-medium transition-colors shadow-lg shadow-indigo-500/30 disabled:opacity-50 disabled:cursor-not-allowed">
            {{ loading ? '保存中...' : '保存更改' }}
        </button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus';

const loading = ref(false);

const settings = ref({
  siteName: 'ImageHosting',
  siteDesc: '基于区块链的原创图片保护与分享网站',
  allowRegister: true,
  storage: {
    endpoint: 'http://localhost:19000',
    bucket: 'images',
    accessKey: 'minioadmin',
    secretKey: 'minioadmin',
    consoleUrl: 'http://localhost:19090'
  },
  blockchain: {
    webaseUrl: 'http://192.168.196.128:5000',
    apiUrl: 'http://localhost:8081'
  }
});

const loadSettings = async () => {
  try {
    const res = await request.get('/api/admin/config');
    if (res.data) {
      const data = res.data;
      if (data['site.name']) settings.value.siteName = data['site.name'];
      if (data['site.desc']) settings.value.siteDesc = data['site.desc'];
      if (data['site.allowRegister']) settings.value.allowRegister = data['site.allowRegister'] === 'true';
      
      if (data['storage.endpoint']) settings.value.storage.endpoint = data['storage.endpoint'];
      if (data['storage.bucket']) settings.value.storage.bucket = data['storage.bucket'];
      if (data['storage.accessKey']) settings.value.storage.accessKey = data['storage.accessKey'];
      if (data['storage.secretKey']) settings.value.storage.secretKey = data['storage.secretKey'];
      if (data['storage.consoleUrl']) settings.value.storage.consoleUrl = data['storage.consoleUrl'];

      if (data['blockchain.webaseUrl']) settings.value.blockchain.webaseUrl = data['blockchain.webaseUrl'];
      if (data['blockchain.apiUrl']) settings.value.blockchain.apiUrl = data['blockchain.apiUrl'];
    }
  } catch (error) {
    console.error('Failed to load settings', error);
  }
};

const saveSettings = async () => {
  loading.value = true;
  try {
    const data = {
      'site.name': settings.value.siteName,
      'site.desc': settings.value.siteDesc,
      'site.allowRegister': String(settings.value.allowRegister),
      
      'storage.endpoint': settings.value.storage.endpoint,
      'storage.bucket': settings.value.storage.bucket,
      'storage.accessKey': settings.value.storage.accessKey,
      'storage.secretKey': settings.value.storage.secretKey,
      'storage.consoleUrl': settings.value.storage.consoleUrl,

      'blockchain.webaseUrl': settings.value.blockchain.webaseUrl,
      'blockchain.apiUrl': settings.value.blockchain.apiUrl
    };
    
    await request.post('/api/admin/config', data);
    ElMessage.success('设置已保存');
  } catch (error) {
    console.error(error);
    ElMessage.error('保存失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadSettings();
});
</script>
