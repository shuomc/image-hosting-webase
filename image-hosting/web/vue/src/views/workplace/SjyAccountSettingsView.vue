<template>
  <div class="p-6 max-w-4xl mx-auto">
    <div class="mb-8">
      <h1 class="text-3xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-500">账户设置</h1>
      <p class="text-sm mt-1 dark:text-slate-300 text-slate-500">管理您的个人信息、安全设置及存储空间</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Left: Profile Settings -->
      <div class="lg:col-span-2 space-y-6">
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700">
          <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-6 flex items-center gap-2">
            <UserIcon class="w-5 h-5 text-indigo-500" />
            个人资料
          </h3>
          
          <div class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="space-y-1">
                <label class="text-sm font-medium text-slate-500 dark:text-slate-400">用户名</label>
                <el-input v-model="profileForm.userName" placeholder="请输入用户名" />
              </div>
              <div class="space-y-1">
                <label class="text-sm font-medium text-slate-500 dark:text-slate-400">电子邮箱</label>
                <el-input v-model="profileForm.userEmail" disabled placeholder="邮箱不可修改" />
              </div>
            </div>

            <div class="space-y-1">
              <label class="text-sm font-medium text-slate-500 dark:text-slate-400">个人简介</label>
              <el-input 
                v-model="profileForm.remark" 
                type="textarea" 
                :rows="3" 
                placeholder="向大家介绍一下你自己吧..." 
              />
            </div>

            <div class="pt-4 flex justify-end">
              <el-button 
                type="primary" 
                :loading="submitting" 
                class="!bg-indigo-600 !border-indigo-600 hover:!bg-indigo-700 rounded-lg px-8 shadow-lg shadow-indigo-500/20"
                @click="handleUpdateProfile"
              >
                保存更改
              </el-button>
            </div>
          </div>
        </div>

        <!-- Security Settings (Mock) -->
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700">
          <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-6 flex items-center gap-2">
            <LockClosedIcon class="w-5 h-5 text-rose-500" />
            安全与隐私
          </h3>
          <div class="divide-y divide-slate-100 dark:divide-slate-700">
            <div class="py-4 flex items-center justify-between">
              <div>
                <p class="font-medium text-slate-700 dark:text-slate-200">修改登录密码</p>
                <p class="text-xs text-slate-500">定期更换密码可以更好地保护您的账户安全</p>
              </div>
              <el-button size="small" class="rounded-lg">立即修改</el-button>
            </div>
            <div class="py-4 flex items-center justify-between">
              <div>
                <p class="font-medium text-slate-700 dark:text-slate-200">两步验证 (2FA)</p>
                <p class="text-xs text-slate-500">为您的账户增加一层额外的安全保护</p>
              </div>
              <el-switch v-model="twoFactorEnabled" style="--el-switch-on-color: #6366f1" />
            </div>
          </div>
        </div>
      </div>

      <!-- Right: Storage & Stats -->
      <div class="space-y-6">
        <!-- Storage Card -->
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden relative">
          <div class="absolute top-0 right-0 p-8 opacity-5">
            <CloudIcon class="w-32 h-32 text-indigo-500" />
          </div>
          
          <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-6 flex items-center gap-2">
            <CloudIcon class="w-5 h-5 text-sky-500" />
            云端存储
          </h3>

          <div v-if="loadingStats" class="space-y-4">
             <div class="h-4 bg-slate-100 dark:bg-slate-700 animate-pulse rounded w-3/4"></div>
             <div class="h-8 bg-slate-100 dark:bg-slate-700 animate-pulse rounded"></div>
             <div class="h-2 bg-slate-100 dark:bg-slate-700 animate-pulse rounded-full"></div>
          </div>
          <div v-else class="space-y-4">
            <div class="flex items-center justify-between">
              <span class="text-sm text-slate-500 dark:text-slate-400">已使用空间</span>
              <span class="text-xs font-bold text-indigo-600 dark:text-indigo-400 bg-indigo-50 dark:bg-indigo-900/30 px-2 py-0.5 rounded-full">
                {{ storagePercent }}%
              </span>
            </div>
            
            <div class="flex items-end gap-1">
              <span class="text-3xl font-extrabold text-slate-800 dark:text-white">
                {{ formatSize(stats?.userStats?.storageUsed || 0).split(' ')[0] }}
              </span>
              <span class="text-sm font-medium text-slate-500 mb-1.5">
                {{ formatSize(stats?.userStats?.storageUsed || 0).split(' ')[1] }}
              </span>
              <span class="text-sm text-slate-400 mb-1.5 mx-1">/</span>
              <span class="text-sm text-slate-500 mb-1.5">
                {{ formatSize(stats?.userStats?.storageLimit || 1073741824) }}
              </span>
            </div>

            <div class="w-full bg-slate-100 dark:bg-slate-700 rounded-full h-2.5 overflow-hidden">
              <div 
                class="h-full rounded-full transition-all duration-1000 ease-out bg-gradient-to-r from-sky-400 to-indigo-500"
                :style="{ width: storagePercent + '%' }"
              ></div>
            </div>

            <p class="text-[11px] text-slate-400 leading-relaxed italic">
              * 存储空间用于存放您的原始高清素材及 NFT 水印副本。
            </p>
          </div>
        </div>

        <!-- Quick Stats -->
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700">
           <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-6 flex items-center gap-2">
            <ChartBarIcon class="w-5 h-5 text-emerald-500" />
            资产概览
          </h3>
          <div class="grid grid-cols-2 gap-4">
            <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-xl border border-slate-100 dark:border-slate-700">
              <div class="text-xs text-slate-500 mb-1">图片总数</div>
              <div class="text-xl font-bold text-slate-800 dark:text-white">{{ stats?.userStats?.totalUploads || 0 }}</div>
            </div>
            <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-xl border border-slate-100 dark:border-slate-700">
              <div class="text-xs text-slate-500 mb-1">持有 NFT</div>
              <div class="text-xl font-bold text-slate-800 dark:text-white">{{ stats?.totalNfts || 0 }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';
import { 
  UserIcon, 
  LockClosedIcon, 
  CloudIcon, 
  ChartBarIcon 
} from '@heroicons/vue/24/outline';

const profileForm = reactive({
  userName: '',
  userEmail: '',
  remark: ''
});

const submitting = ref(false);
const loadingStats = ref(true);
const stats = ref<any>(null);
const twoFactorEnabled = ref(false);

const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

const storagePercent = computed(() => {
  if (!stats.value?.userStats) return 0;
  const used = stats.value.userStats.storageUsed || 0;
  const limit = stats.value.userStats.storageLimit || 1073741824;
  return Math.min(100, Math.round((used / limit) * 100));
});

const fetchUserProfile = async () => {
  try {
    const res = await request.get('/api/user/current');
    if (res.code === 200) {
      profileForm.userName = res.data.userName;
      profileForm.userEmail = res.data.userEmail;
      profileForm.remark = res.data.remark || '';
    }
  } catch (error) {
    console.error('获取用户信息失败', error);
  }
};

const fetchUserStats = async () => {
  try {
    loadingStats.value = true;
    // 关键修复：统一使用 dashboard/stats 接口获取存储使用量数据
    const res = await request.get('/api/user/dashboard/stats');
    if (res.code === 200) {
      stats.value = res.data;
    }
  } catch (error) {
    console.error('获取统计信息失败', error);
  } finally {
    loadingStats.value = false;
  }
};

const handleUpdateProfile = async () => {
  try {
    submitting.value = true;
    const res = await request.post('/api/user/update', {
      userName: profileForm.userName,
      remark: profileForm.remark
    });
    if (res.code === 200) {
      ElMessage.success('个人资料已更新');
    }
  } catch (error) {
    console.error('更新个人资料失败', error);
    ElMessage.error('更新失败，请稍后重试');
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  fetchUserProfile();
  fetchUserStats();
});
</script>

<style scoped>
:deep(.el-input__inner) {
  @apply bg-transparent;
}
</style>
