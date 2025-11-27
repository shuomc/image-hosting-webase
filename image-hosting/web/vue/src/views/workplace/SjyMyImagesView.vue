<template>
  <div class="min-h-screen w-full bg-[#f8fafc] dark:bg-slate-900 relative">
    
    <div class="absolute inset-0 z-0 pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-400/20 blur-[100px]"></div>
      <div class="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-400/20 blur-[100px]"></div>
    </div>

    <div class="relative z-10 container mx-auto px-4 py-8 max-w-7xl">
      
        <div class="flex flex-col sm:flex-row justify-between items-center mb-10 gap-4">
        <div>
          <h1 :class="['text-3xl font-extrabold tracking-tight','bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-500', 'text-slate-800']">我的图库</h1>
          <p :class="['text-sm mt-1', 'dark:text-slate-300 text-slate-500']">管理您上传的所有精彩瞬间</p>
        </div>

        <div class="bg-white p-1 rounded-xl shadow-sm border border-slate-200 flex items-center dark:bg-slate-800 dark:border-slate-700">
          <button @click="setLayout('grid')"
            :class="[
              'flex items-center gap-2 px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200 ease-out',
              currentLayout === 'grid' 
                ? 'bg-indigo-50 text-indigo-600 shadow-sm ring-1 ring-indigo-200' 
                : 'text-slate-500 hover:bg-slate-50'
            ]">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="7" height="7" x="3" y="3" rx="1"/><rect width="7" height="7" x="14" y="3" rx="1"/><rect width="7" height="7" x="14" y="14" rx="1"/><rect width="7" height="7" x="3" y="14" rx="1"/></svg>
            网格
          </button>
          <button @click="setLayout('list')"
            :class="[
              'flex items-center gap-2 px-4 py-2 rounded-xl text-sm font-medium transition-all duration-200 ease-out',
              currentLayout === 'list' 
                ? 'bg-indigo-50 text-indigo-600 shadow-sm ring-1 ring-indigo-200' 
                : 'text-slate-500 hover:bg-slate-50'
            ]">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" x2="21" y1="6" y2="6"/><line x1="8" x2="21" y1="12" y2="12"/><line x1="8" x2="21" y1="18" y2="18"/><line x1="3" x2="3.01" y1="6" y2="6"/><line x1="3" x2="3.01" y1="12" y2="12"/><line x1="3" x2="3.01" y1="18" y2="18"/></svg>
            列表
          </button>
          <div class="border-l border-slate-100 dark:border-slate-700 ml-2 pl-2">
            <button @click="goUpload" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-blue-50 text-blue-700 hover:bg-blue-100 text-sm font-medium transition">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 16.5v2.25A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75V16.5m-13.5-9L12 3m0 0 4.5 4.5M12 3v13.5" />
              </svg>
              上传
            </button>
          </div>
        </div>
      </div>

      <div v-if="loading" class="w-full h-64 flex items-center justify-center">
        <div class="text-slate-400">加载中…</div>
      </div>

      <div v-else>
        <template v-if="currentLayout === 'grid'">
          <div v-if="imageList.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            <div v-for="image in imageList" :key="image.imageId"
              class="group relative bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden transition-all duration-300 -translate-y-0 hover:-translate-y-1 hover:shadow-[0_12px_40px_rgba(99,102,241,0.14),0_0_30px_rgba(139,92,246,0.08)] cursor-pointer flex flex-col dark:bg-slate-800 dark:border-slate-700"
              @click="goToImageDetail(image)">

              <!-- Image area with rounded top corners -->
              <div class="relative w-full aspect-[4/3] bg-slate-100 overflow-hidden rounded-2xl">
                <img :src="image.minioUrl" :alt="image.fileName"
                  class="w-full h-full object-cover transition-transform rounded-t-2xl" loading="lazy" />

                <!-- Info overlay: hidden by default, slides up on hover -->
                <div class="absolute left-0 right-0 bottom-0 transform translate-y-full group-hover:translate-y-0 transition-transform duration-300">
                  <div class="p-4 rounded-2xl backdrop-blur-sm bg-white/95 dark:bg-black/50 text-slate-900 dark:text-white">
                    <div class="flex justify-between items-start mb-2">
                      <div class="font-bold truncate text-base" :title="image.fileName">{{ image.fileName }}</div>
                    </div>
                    <p class="text-xs truncate text-slate-600 dark:text-slate-300 mb-3 h-5">{{ image.description || '暂无描述信息...' }}</p>
                    <div class="flex items-center justify-between">
                      <div class="flex items-center gap-2">
                        <span class="text-[10px] font-medium bg-slate-100 text-slate-500 px-2 py-0.5 rounded-full dark:bg-slate-700 dark:text-slate-200">{{ formatBytes(image.size) }}</span>
                        <div @click.stop title="是否公开">
                          <el-switch v-model="image.isPublic" :loading="image.isToggling" @change="handleTogglePublicStatus(image, $event)" size="small" style="--el-switch-on-color: #6366f1; --el-switch-off-color: #cbd5e1" />
                        </div>
                      </div>

                      <div class="flex items-center gap-2">
                        <button @click.stop="downloadImage(image)" class="p-2 text-slate-700 dark:text-white hover:text-green-600 hover:bg-green-50 rounded-lg transition-colors duration-200" title="下载">
                          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" x2="12" y1="15" y2="3"/></svg>
                        </button>
                        <button @click.stop="deleteImage(image)" :disabled="image.isDeleting" class="p-2 text-slate-700 dark:text-white hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors duration-200 disabled:opacity-50" title="删除">
                          <svg v-if="!image.isDeleting" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
                          <svg v-else class="animate-spin" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>

        <template v-else-if="currentLayout === 'list'">
           <div v-if="imageList.length > 0" class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden dark:bg-slate-800 dark:border-slate-700">
             <el-table :data="imageList" :style="{ width: '100%' }"
              :header-cell-style="headerCellStyle"
              :row-class-name="tableRowClass"
              @row-click="goToImageDetail">
              
              <el-table-column label="预览" width="120">
                <template #default="{ row }">
                  <div class="w-16 h-16 rounded-lg overflow-hidden bg-slate-100 border border-slate-200">
                    <img :src="row.minioUrl" :alt="row.fileName" class="w-full h-full object-cover" />
                  </div>
                </template>
              </el-table-column>
              
              <el-table-column label="文件名" prop="fileName" min-width="200">
                <template #default="{ row }">
                  <span class="font-medium text-slate-700">{{ row.fileName }}</span>
                </template>
              </el-table-column>
              
              <el-table-column label="类型" prop="contentType" width="120">
                 <template #default="{ row }">
                   <span class="text-xs bg-slate-100 text-slate-500 px-2 py-1 rounded-md">{{ row.contentType.split('/')[1] || row.contentType }}</span>
                 </template>
              </el-table-column>
              
              <el-table-column label="大小" width="120">
                <template #default="{ row }">
                  <span class="text-slate-500 font-mono text-sm">{{ formatBytes(row.size) }}</span>
                </template>
              </el-table-column>
              
              <el-table-column label="公开状态" width="100">
                <template #default="{ row }">
                  <div @click.stop>
                    <el-switch v-model="row.isPublic" :loading="row.isToggling"
                      @change="handleTogglePublicStatus(row, $event)" size="small" 
                      style="--el-switch-on-color: #6366f1;" />
                  </div>
                </template>
              </el-table-column>
              
              <el-table-column label="上传时间" width="180">
                <template #default="{ row }">
                   <span class="text-sm text-slate-400">{{ formatTimestamp(row.uploadTime).split(' ')[0] }}</span>
                   <div class="text-xs text-slate-300">{{ formatTimestamp(row.uploadTime).split(' ')[1] }}</div>
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="150" fixed="right">
                <template #default="{ row }">
                  <div class="flex items-center gap-2">
                    <button @click.stop="downloadImage(row)"
                      class="p-1.5 text-slate-400 hover:text-green-600 hover:bg-green-50 rounded-md transition-colors" title="下载">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" x2="12" y1="15" y2="3"/></svg>
                    </button>
                    <button @click.stop="deleteImage(row)" :disabled="row.isDeleting"
                      class="p-1.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-md transition-colors disabled:opacity-50" title="删除">
                      <svg v-if="!row.isDeleting" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
                      <svg v-else class="animate-spin" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                    </button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </template>

        <div v-if="imageList.length === 0 && !loading" class="flex flex-col items-center justify-center py-20 text-center animate-fade-in-up">
           <div class="bg-indigo-50 p-6 rounded-full mb-4">
             <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="text-indigo-400"><rect width="18" height="18" x="3" y="3" rx="2" ry="2"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
           </div>
           <h3 class="text-lg font-semibold text-slate-700">暂无图片数据</h3>
           <p class="text-slate-500 mt-2 max-w-xs">您还没有上传任何图片。点击上方上传按钮开始您的图床之旅。</p>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from 'vue';
import service from '@/utils/request';
import { useUserStore } from '@/stores/user';
import { API_BASE_URL } from '@/config';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
// 引入 Element Plus 样式
import 'element-plus/dist/index.css';

// 定义接口
interface Image {
  imageId: string;
  minioUrl: string;
  fileName: string;
  userId: string;
  contentType: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime?: string;
  isToggling?: boolean;
  isDeleting?: boolean;
}

// 状态
const imageList = ref<Image[]>([]);
const currentLayout = ref('grid');
const loading = ref(false);

const userStore = useUserStore();
const router = useRouter();

// 主题检测（局部同步 Topbar 的 theme/localStorage）
const isDark = ref(false);
let mq: MediaQueryList | null = null;
let mqListener: ((e: MediaQueryListEvent) => void) | null = null;

const updateThemeFromEnvironment = () => {
  try {
    const saved = localStorage.getItem('theme');
    const systemDark = typeof window !== 'undefined' && !!(window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches);
    if (saved === 'dark' || (!saved && systemDark)) isDark.value = true;
    else isDark.value = false;
  } catch {
    isDark.value = document.documentElement.classList.contains('dark');
  }
};

const setupThemeListeners = () => {
  if (typeof window === 'undefined' || !('matchMedia' in window)) return;
  mq = window.matchMedia('(prefers-color-scheme: dark)');
  mqListener = (e: MediaQueryListEvent) => {
    const saved = localStorage.getItem('theme');
    if (saved) return; // 用户已指定主题，忽略系统变化
    isDark.value = e.matches;
  };
  if ('addEventListener' in mq) mq.addEventListener('change', mqListener);
  else (mq as any).addListener && (mq as any).addListener(mqListener);

  // storage 事件用于在多个标签页之间同步主题切换
  window.addEventListener('storage', (ev) => {
    if (ev.key === 'theme') updateThemeFromEnvironment();
  });
};

// 计算表头样式，适配主题
const headerCellStyle = computed(() => {
  return isDark.value
    ? { background: '#0f172a', color: '#cbd5e1', fontWeight: '600' }
    : { background: '#f8fafc', color: '#64748b', fontWeight: '600' };
});

// 表格行 class（用于暗色主题做行样式微调）
const tableRowClass = ({ row, rowIndex }: { row: any; rowIndex: number }) => {
  return isDark.value ? 'my-table-row-dark' : '';
};

// 列表视图不使用 Element Plus 的 Switch，提供简单切换函数
const togglePublic = async (row: Image) => {
  // 调用已有的业务逻辑
  await handleTogglePublicStatus(row, !row.isPublic);
};

// 生命周期：挂载
onMounted(async () => {
  updateThemeFromEnvironment();
  setupThemeListeners();
  loading.value = true;
  if (userStore.hasUserInfo && userStore.userInfo?.userId) {
    const userId = userStore.userInfo.userId;
    const apiUrl = `${API_BASE_URL}/api/images/minio/url/user/${userId}`;

    try {
      const responseData = await service.get(apiUrl);
      if (responseData.code === 200 && Array.isArray(responseData.data)) {
        imageList.value = responseData.data.map((item: any) => ({
          imageId: item.imageId,
          minioUrl: item.minioUrl,
          fileName: item.fileName,
          userId: item.userId,
          contentType: item.contentType,
          description: item.description,
          size: item.size,
          isPublic: item.isPublic,
          uploadTime: item.uploadTime,
          isToggling: false,
          isDeleting: false,
        }));
      } else {
        ElMessage.error(responseData.msg || '获取图片列表失败');
      }
    } catch (error: any) {
      console.error('获取图片列表请求失败:', error);
    } finally {
      loading.value = false;
    }
  } else {
    loading.value = false;
  }
});

onBeforeUnmount(() => {
  if (mq && mqListener) {
    if ('removeEventListener' in mq) mq.removeEventListener('change', mqListener);
    else (mq as any).removeListener && (mq as any).removeListener(mqListener);
  }
});

// 跳转详情
const goToImageDetail = (image: Image) => {
  if (image && image.imageId) {
    userStore.setSelectedImage(image);
    router.push({ name: 'ImageDetail', params: { imageId: image.imageId } });
  } else {
    ElMessage.warning('图片信息不完整');
  }
};

// 切换布局
const setLayout = (layout: 'grid' | 'list') => {
  currentLayout.value = layout;
};

// 前往上传页
const goUpload = () => {
  router.push({ name: 'UploadImage' });
};

// 工具函数：格式化字节
const formatBytes = (bytes: number | undefined, decimals = 2): string => {
  if (bytes === undefined || bytes === null || bytes === 0) return '0 B';
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
};

// 工具函数：格式化时间
const formatTimestamp = (timestamp: string | undefined): string => {
  if (!timestamp) return '未知时间';
  try {
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) return '无效时间';
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  } catch {
    return '格式错误';
  }
};

// 业务逻辑：切换公开状态
const handleTogglePublicStatus = async (image: Image, newStatus: boolean) => {
  const originalStatus = !newStatus;
  // 乐观更新
  image.isPublic = newStatus; 
  image.isToggling = true;

  const apiUrl = `${API_BASE_URL}/api/images/switchPublicStatus`;

  try {
    const responseData = await service.post(apiUrl, null, {
      params: { imageId: image.imageId }
    });

    if (responseData.code === 200) {
      ElMessage.success('状态更新成功');
    } else {
      ElMessage.error(responseData.msg || '状态更新失败');
      image.isPublic = originalStatus; // 回滚
    }
  } catch (error: any) {
    console.error('切换状态请求失败:', error);
    image.isPublic = originalStatus; // 回滚
  } finally {
    image.isToggling = false;
  }
};

// 业务逻辑：下载
const downloadImage = (image: Image) => {
  if (!image || !image.imageId || !image.fileName) {
    ElMessage.warning('无法下载：信息不完整');
    return;
  }
  const downloadUrl = `${API_BASE_URL}/api/images/minio/${image.imageId}`;
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

// 业务逻辑：删除
const deleteImage = async (image: Image) => {
  if (!image || !image.imageId) return;

  try {
    await ElMessageBox.confirm(`确定要删除 "${image.fileName}" 吗？此操作不可恢复。`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger'
    });

    const imageToDelete = imageList.value.find(img => img.imageId === image.imageId);
    if (imageToDelete) imageToDelete.isDeleting = true;

    const deleteUrl = `${API_BASE_URL}/api/images/deleteById/${image.imageId}`;
    const responseData = await service.post(deleteUrl);

    if (responseData.code === 200) {
      ElMessage.success('删除成功');
      const index = imageList.value.findIndex(img => img.imageId === image.imageId);
      if (index !== -1) imageList.value.splice(index, 1);
    } else {
      console.error('删除失败:', responseData.msg);
    }
  } catch (error) {
    if (error !== 'cancel') console.error('删除错误:', error);
  } finally {
    const imageToDelete = imageList.value.find(img => img.imageId === image.imageId);
    if (imageToDelete) imageToDelete.isDeleting = false;
  }
};
</script>

<style scoped>
/* 列表视图暗色行样式 */
.my-table-row-dark td {
  background: #0b1220; /* subtle dark row */
  color: #cbd5e1;
}
.my-table-row-dark td:last-child {
  background: transparent;
}

/* Element Plus table dark-mode overrides
   Element Plus applies its own styles; easiest reliable fix is to increase specificity
   and target the component classes when the page has the `dark` class on <html>.
   We use deep selectors so scoped styles still apply. */
:deep(.dark) .el-table__header th {
  background: #0f172a !important;
  color: #cbd5e1 !important;
  border-bottom: 1px solid rgba(255,255,255,0.04) !important;
}
:deep(.dark) .el-table__body td {
  color: #cbd5e1 !important;
  background: transparent !important;
}
:deep(.dark) .el-table {
  --el-table-border-color: #0f172a;
}

/* Ensure our custom row class applies inside Element table body */
:deep(.dark) .el-table__body .my-table-row-dark td {
  background: #0b1220 !important;
  color: #cbd5e1 !important;
}

/* 定义淡入动画 */
.animate-fade-in-up {
  animation: fadeInUp 0.5s ease-out forwards;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 覆盖 Element Plus 表格的一些默认样式以匹配设计 */
:deep(.el-table) {
  --el-table-border-color: #f1f5f9;
  --el-table-header-bg-color: #f8fafc;
}
:deep(.el-table__inner-wrapper::before) {
  display: none; /* 移除底部默认的灰色边框线 */
}
</style>