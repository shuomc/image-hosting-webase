<template>
  <div class="w-full transition-colors duration-300 pt-2 pb-10">

    <div class="fixed top-20 right-4 sm:right-8 z-40 flex items-center gap-2 p-1.5 rounded-xl 
                bg-white/70 dark:bg-slate-900/70 backdrop-blur-md 
                border border-slate-200/50 dark:border-slate-700/50 shadow-lg transition-all duration-300">
      
      <div class="flex bg-slate-100 dark:bg-slate-800 rounded-xl p-1">
        <button @click="setLayout('grid')"
          :class="[
            'flex items-center justify-center w-9 h-8 rounded-xl transition-all duration-200',
            currentLayout === 'grid' 
              ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-sm' 
              : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'
          ]"
          title="网格视图">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="7" height="7" x="3" y="3" rx="1"/><rect width="7" height="7" x="14" y="3" rx="1"/><rect width="7" height="7" x="14" y="14" rx="1"/><rect width="7" height="7" x="3" y="14" rx="1"/></svg>
        </button>
        <button @click="setLayout('list')"
          :class="[
            'flex items-center justify-center w-9 h-8 rounded-xl transition-all duration-200',
            currentLayout === 'list' 
               ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-sm' 
              : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'
          ]"
          title="列表视图">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" x2="21" y1="6" y2="6"/><line x1="8" x2="21" y1="12" y2="12"/><line x1="8" x2="21" y1="18" y2="18"/><line x1="3" x2="3.01" y1="6" y2="6"/><line x1="3" x2="3.01" y1="12" y2="12"/><line x1="3" x2="3.01" y1="18" y2="18"/></svg>
        </button>
      </div>

      <div class="w-px h-6 bg-slate-200 dark:bg-slate-700 mx-1"></div>

      <button @click="goUpload" 
        class="flex items-center gap-2 px-4 py-1.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white shadow-md shadow-indigo-500/20 transition-all active:scale-95 text-sm font-medium">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path stroke-linecap="round" stroke-linejoin="round" d="M3 16.5v2.25A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75V16.5m-13.5-9L12 3m0 0 4.5 4.5M12 3v13.5" />
        </svg>
        <span>上传</span>
      </button>
    </div>

    <div class="container mx-auto px-4 max-w-7xl">
      
      <div class="mb-8">
        <h1 class="text-3xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600 dark:from-indigo-400 dark:to-purple-400">
          我的图库
        </h1>
        <p class="text-sm mt-2 text-slate-500 dark:text-slate-400 max-w-lg">
          管理您上传的所有精彩瞬间。支持批量管理、预览及快速分享。
        </p>
      </div>

      <div v-if="loading" class="w-full h-64 flex flex-col items-center justify-center gap-3">
        <div class="w-8 h-8 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
        <div class="text-slate-400 text-sm">加载资源中...</div>
      </div>

      <div v-else>
        <div v-if="imageList.length === 0" class="flex flex-col items-center justify-center py-20 text-center">
          <div class="bg-slate-100 dark:bg-slate-800 p-6 rounded-full mb-4">
             <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="text-slate-400"><rect width="18" height="18" x="3" y="3" rx="2" ry="2"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
          </div>
          <h3 class="text-lg font-semibold text-slate-700 dark:text-slate-200">暂无图片</h3>
          <p class="text-slate-500 dark:text-slate-400 mt-2 text-sm">点击右上角的上传按钮添加您的第一张图片。</p>
        </div>

        <template v-else>
          <div v-if="currentLayout === 'grid'" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
             <div v-for="image in imageList" :key="image.imageId"
               class="group relative bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden transition-all duration-300 -translate-y-0 hover:-translate-y-1 hover:shadow-[0_12px_40px_rgba(99,102,241,0.45),0_0_30px_rgba(139,92,246,0.42)] cursor-pointer flex flex-col dark:bg-slate-800 dark:border-slate-700"
               @click="goToImageDetail(image)">

               <div class="relative w-full aspect-[4/3] bg-slate-100 dark:bg-slate-900 overflow-hidden">
                 <img :src="image.originalMinioUrl" :alt="image.fileName"
                   class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" loading="lazy" />
                 
                 <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>

                 <div class="absolute left-0 right-0 bottom-0 p-4 translate-y-4 group-hover:translate-y-0 transition-transform duration-300 opacity-0 group-hover:opacity-100">
                    <p class="text-white font-medium truncate text-sm">{{ image.fileName }}</p>
                    <div class="flex items-center justify-between mt-2">
                       <span class="text-[10px] bg-white/20 backdrop-blur-md text-white px-2 py-0.5 rounded-full">
                         {{ formatBytes(image.size) }}
                       </span>
                       <div class="flex gap-2">
                          <button @click.stop="downloadImage(image)" class="p-1.5 bg-white/20 hover:bg-white/40 backdrop-blur-md rounded-lg text-white transition">
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" x2="12" y1="15" y2="3"/></svg>
                          </button>
                          <button @click.stop="deleteImage(image)" :disabled="image.isDeleting" class="p-1.5 bg-white/20 hover:bg-red-500/80 backdrop-blur-md rounded-lg text-white transition disabled:opacity-50">
                             <svg v-if="!image.isDeleting" xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
                             <svg v-else class="animate-spin" xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                          </button>
                       </div>
                    </div>
                 </div>
               </div>
               
               <div class="p-3 bg-white dark:bg-slate-800 flex justify-between items-center border-t border-slate-100 dark:border-slate-700">
                  <div class="flex items-center gap-2">
                     <span class="w-2 h-2 rounded-full" :class="image.isPublic ? 'bg-green-500' : 'bg-slate-300'"></span>
                     <span class="text-xs text-slate-500 dark:text-slate-400">{{ image.isPublic ? '公开' : '私有' }}</span>
                  </div>
                  <div @click.stop>
                     <el-switch v-model="image.isPublic" :loading="image.isToggling" @change="handleTogglePublicStatus(image, $event)" size="small" style="--el-switch-on-color: #6366f1;" />
                  </div>
               </div>
             </div>
          </div>

          <div v-else-if="currentLayout === 'list'" class="overflow-hidden rounded-2xl border border-slate-200 dark:border-slate-700 shadow-sm bg-white dark:bg-slate-800">
             <div class="overflow-x-auto">
               <table class="w-full text-sm text-left text-slate-500 dark:text-slate-400">
                 <thead class="text-xs text-slate-700 uppercase bg-slate-50 dark:bg-slate-900/50 dark:text-slate-300 border-b border-slate-200 dark:border-slate-700">
                   <tr>
                     <th scope="col" class="px-6 py-4 font-semibold">预览</th>
                     <th scope="col" class="px-6 py-4 font-semibold">文件名</th>
                     <th scope="col" class="px-6 py-4 font-semibold">类型</th>
                     <th scope="col" class="px-6 py-4 font-semibold">大小</th>
                     <th scope="col" class="px-6 py-4 font-semibold">状态</th>
                     <th scope="col" class="px-6 py-4 font-semibold">上传时间</th>
                     <th scope="col" class="px-6 py-4 font-semibold text-right">操作</th>
                   </tr>
                 </thead>
                 <tbody class="divide-y divide-slate-100 dark:divide-slate-700/50">
                   <tr v-for="image in imageList" :key="image.imageId" 
                       class="bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors cursor-pointer"
                       @click="goToImageDetail(image)">
                     
                     <td class="px-6 py-3">
                       <div class="w-12 h-12 rounded-lg overflow-hidden bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-700">
                         <img :src="image.originalMinioUrl" class="w-full h-full object-cover" loading="lazy">
                       </div>
                     </td>
                     
                     <td class="px-6 py-3 font-medium text-slate-900 dark:text-white max-w-[200px] truncate">
                       {{ image.fileName }}
                     </td>
                     
                     <td class="px-6 py-3">
                       <span class="bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-300 text-xs font-medium px-2 py-0.5 rounded">
                         {{ image.contentType.split('/')[1]?.toUpperCase() || 'UNK' }}
                       </span>
                     </td>
                     
                     <td class="px-6 py-3 font-mono text-xs">
                       {{ formatBytes(image.size) }}
                     </td>
                     
                     <td class="px-6 py-3" @click.stop>
                        <div class="flex items-center gap-2">
                           <el-switch v-model="image.isPublic" :loading="image.isToggling" @change="handleTogglePublicStatus(image, $event)" size="small" style="--el-switch-on-color: #6366f1;" />
                           <span class="text-xs">{{ image.isPublic ? 'Public' : 'Private' }}</span>
                        </div>
                     </td>
                     
                     <td class="px-6 py-3 text-xs">
                       <div class="flex flex-col">
                         <span>{{ formatTimestamp(image.uploadTime).split(' ')[0] }}</span>
                         <span class="text-slate-400">{{ formatTimestamp(image.uploadTime).split(' ')[1] }}</span>
                       </div>
                     </td>
                     
                     <td class="px-6 py-3 text-right">
                       <div class="flex items-center justify-end gap-2">
                         <button @click.stop="downloadImage(image)" class="p-2 text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 hover:bg-indigo-50 dark:hover:bg-indigo-900/30 rounded-lg transition">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" x2="12" y1="15" y2="3"/></svg>
                         </button>
                         <button @click.stop="deleteImage(image)" :disabled="image.isDeleting" class="p-2 text-slate-400 hover:text-red-600 dark:hover:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/30 rounded-lg transition disabled:opacity-50">
                            <svg v-if="!image.isDeleting" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/><path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/><line x1="10" x2="10" y1="11" y2="17"/><line x1="14" x2="14" y1="11" y2="17"/></svg>
                            <svg v-else class="animate-spin" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12a9 9 0 1 1-6.219-8.56"/></svg>
                         </button>
                       </div>
                     </td>

                   </tr>
                 </tbody>
               </table>
             </div>
          </div>
        </template>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import service from '@/utils/request';
import { useUserStore } from '@/stores/user';
import { API_BASE_URL } from '@/config';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
// 引入 Element Plus 样式 (仅用于 Message 和 Box)
import 'element-plus/dist/index.css';

// 定义接口
interface Image {
  imageId: string;
  originalMinioUrl: string;
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

// ... 其他函数和状态保持不变 ...

// 生命周期：挂载
onMounted(async () => {
  loading.value = true;
  if (userStore.hasUserInfo && userStore.userInfo?.userId) {
    const userId = userStore.userInfo.userId;
    const apiUrl = `${API_BASE_URL}/api/images/minio/url/user/${userId}`;

    try {
      const responseData = await service.get(apiUrl);
      if (responseData.code === 200 && Array.isArray(responseData.data)) {
        imageList.value = responseData.data.map((item: any) => ({
          imageId: item.imageId,
          originalMinioUrl: item.originalMinioUrl,
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

// ⭐ 主要修改点：只进行路由跳转，不再依赖本地缓存。
// 跳转详情
const goToImageDetail = (image: Image) => {
  if (image && image.imageId) {
    // 移除 useUserStore().setSelectedImage(image) 依赖
    router.push({ name: 'ImageDetail', params: { imageId: image.imageId } });
  } else {
    ElMessage.warning('图片信息不完整');
  }
};

// 切换布局
const setLayout = (layout: 'grid' | 'list') => {
// ... 保持不变 ...
  currentLayout.value = layout;
};

// 前往上传页
const goUpload = () => {
// ... 保持不变 ...
  router.push({ name: 'UploadImage' });
};

// 工具函数：格式化字节
const formatBytes = (bytes: number | undefined, decimals = 2): string => {
// ... 保持不变 ...
  if (bytes === undefined || bytes === null || bytes === 0) return '0 B';
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
};

// 工具函数：格式化时间
const formatTimestamp = (timestamp: string | undefined): string => {
// ... 保持不变 ...
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
// ... 保持不变 ...
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
// ... 保持不变 ...
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
// ... 保持不变 ...
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
/* 可以在这里添加一些特定的动画或样式，但大部分样式已通过 Tailwind 类实现 */
</style>