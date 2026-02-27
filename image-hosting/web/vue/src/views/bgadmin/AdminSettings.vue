<template>
  <div class="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
    
    <!-- General Settings -->
    <div class="bg-white/80 dark:bg-slate-800/80 backdrop-blur-md rounded-[2.5rem] shadow-xl border border-white dark:border-slate-700 overflow-hidden transition-all duration-300">
      <div class="p-8 border-b border-slate-100 dark:border-slate-700 bg-gradient-to-br from-indigo-50/50 to-transparent dark:from-indigo-900/20">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-indigo-600 rounded-2xl text-white shadow-lg shadow-indigo-200 dark:shadow-none">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          </div>
          <div>
            <h3 class="text-xl font-black text-slate-900 dark:text-white">基础设置</h3>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-0.5">管理网站的基本身份标识与全局运行策略</p>
          </div>
        </div>
      </div>
      <div class="p-8 space-y-8">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">网站名称</label>
            <input type="text" v-model="settings.siteName" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" placeholder="例如：NFT 影像中心" />
          </div>
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">网站描述</label>
            <input type="text" v-model="settings.siteDesc" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" placeholder="简单描述您的站点功能" />
          </div>
        </div>
        
        <div class="flex items-center justify-between p-6 bg-slate-50/50 dark:bg-slate-900/30 rounded-3xl border border-slate-100 dark:border-slate-800 transition-colors hover:bg-slate-100/50 dark:hover:bg-slate-900/50">
          <div class="flex items-center gap-4">
            <div class="w-12 h-12 rounded-2xl bg-emerald-100 dark:bg-emerald-900/30 flex items-center justify-center text-emerald-600 dark:text-emerald-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            <div>
              <h4 class="text-sm font-bold text-slate-900 dark:text-white">开放全站注册</h4>
              <p class="text-xs text-slate-500 dark:text-slate-400 mt-1">控制是否允许新用户通过注册页面创建账号</p>
            </div>
          </div>
          <label class="relative inline-flex items-center cursor-pointer group">
            <input type="checkbox" v-model="settings.allowRegister" class="sr-only peer">
            <div class="w-14 h-8 bg-slate-200 peer-focus:outline-none rounded-full peer dark:bg-slate-700 peer-checked:after:translate-x-6 peer-checked:after:border-white after:content-[''] after:absolute after:top-[4px] after:left-[4px] after:bg-white after:border-slate-300 after:border after:rounded-full after:h-6 after:w-6 after:transition-all dark:border-slate-600 peer-checked:bg-indigo-600 transition-all"></div>
          </label>
        </div>
      </div>
    </div>

    <!-- Storage Settings -->
    <div class="bg-white/80 dark:bg-slate-800/80 backdrop-blur-md rounded-[2.5rem] shadow-xl border border-white dark:border-slate-700 overflow-hidden transition-all duration-300">
      <div class="p-8 border-b border-slate-100 dark:border-slate-700 bg-gradient-to-br from-blue-50/50 to-transparent dark:from-blue-900/20">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-blue-600 rounded-2xl text-white shadow-lg shadow-blue-200 dark:shadow-none">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4" />
            </svg>
          </div>
          <div>
            <h3 class="text-xl font-black text-slate-900 dark:text-white">存储设置</h3>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-0.5">配置图片存储后端 (MinIO / S3)</p>
          </div>
        </div>
      </div>
      <div class="p-8 space-y-8">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div class="space-y-3">
             <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">API Endpoint</label>
            <input type="text" v-model="settings.storage.endpoint" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
          </div>
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">管理后台地址</label>
            <div class="flex gap-3">
                <input type="text" v-model="settings.storage.consoleUrl" class="flex-1 px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
                <a :href="settings.storage.consoleUrl" target="_blank" class="px-5 py-3 bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 dark:hover:bg-slate-600 rounded-2xl transition-all flex items-center shadow-sm">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-external-link"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/><polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/></svg>
                </a>
            </div>
          </div>
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">存储桶 (Bucket)</label>
            <input type="text" v-model="settings.storage.bucket" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
          </div>
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">Access Key</label>
            <input type="password" v-model="settings.storage.accessKey" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
          </div>
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">Secret Key</label>
            <input type="password" v-model="settings.storage.secretKey" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
          </div>
        </div>
      </div>
    </div>

    <!-- Blockchain Settings -->
    <div class="bg-white/80 dark:bg-slate-800/80 backdrop-blur-md rounded-[2.5rem] shadow-xl border border-white dark:border-slate-700 overflow-hidden transition-all duration-300">
      <div class="p-8 border-b border-slate-100 dark:border-slate-700 bg-gradient-to-br from-emerald-50/50 to-transparent dark:from-emerald-900/20">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-emerald-600 rounded-2xl text-white shadow-lg shadow-emerald-200 dark:shadow-none">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
            </svg>
          </div>
          <div>
            <h3 class="text-xl font-black text-slate-900 dark:text-white">区块链设置</h3>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-0.5">配置 WeBASE 和区块链服务连接</p>
          </div>
        </div>
      </div>
      <div class="p-8 space-y-8">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div class="space-y-3">
            <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">WeBASE 管理台地址</label>
            <div class="flex gap-3">
                <input type="text" v-model="settings.blockchain.webaseUrl" class="flex-1 px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-emerald-500/10 focus:border-emerald-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
                <a :href="settings.blockchain.webaseUrl" target="_blank" class="px-5 py-3 bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 dark:hover:bg-slate-600 rounded-2xl transition-all flex items-center shadow-sm">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-external-link"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/><polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/></svg>
                </a>
            </div>
          </div>
          <div class="space-y-3">
             <label class="text-xs font-black text-slate-400 dark:text-slate-500 uppercase tracking-widest px-1">区块链服务 API 地址</label>
            <input type="text" v-model="settings.blockchain.apiUrl" class="w-full px-5 py-3.5 rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/50 focus:ring-4 focus:ring-emerald-500/10 focus:border-emerald-500 outline-none transition-all font-medium text-slate-700 dark:text-slate-200" />
          </div>
        </div>
      </div>
      <div class="px-8 py-6 bg-slate-50/80 dark:bg-slate-900/50 border-t border-slate-100 dark:border-slate-700 flex justify-end">
        <button @click="saveSettings" :disabled="loading" class="px-10 py-3 bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl font-black text-sm transition-all shadow-xl shadow-indigo-500/25 disabled:opacity-50 disabled:cursor-not-allowed transform hover:-translate-y-0.5 active:translate-y-0 flex items-center gap-2">
            <svg v-if="loading" class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            {{ loading ? '保存中...' : '提交配置更新' }}
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
