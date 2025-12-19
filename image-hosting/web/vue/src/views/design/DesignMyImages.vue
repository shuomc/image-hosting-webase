<template>
  <div class="w-full pt-4 pb-10 bg-white">

    <div class="fixed top-20 right-4 z-40 flex items-center gap-2 p-2 bg-gray-100 border-2 border-black">
      <button @click="setLayout('grid')" :class="['w-8 h-8 border-2 border-black', currentLayout === 'grid' ? 'bg-gray-400' : 'bg-white']" title="网格视图">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="7" height="7" x="3" y="3"/><rect width="7" height="7" x="14" y="3"/><rect width="7" height="7" x="14" y="14"/><rect width="7" height="7" x="3" y="14"/></svg>
      </button>
      <button @click="setLayout('list')" :class="['w-8 h-8 border-2 border-black', currentLayout === 'list' ? 'bg-gray-400' : 'bg-white']" title="列表视图">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" x2="21" y1="6" y2="6"/><line x1="8" x2="21" y1="12" y2="12"/><line x1="8" x2="21" y1="18" y2="18"/><line x1="3" x2="3.01" y1="6" y2="6"/><line x1="3" x2="3.01" y1="12" y2="12"/><line x1="3" x2="3.01" y1="18" y2="18"/></svg>
      </button>
      <button @click="goUpload" class="px-4 py-1 border-2 border-black text-black bg-white text-sm font-medium">上传</button>
    </div>

    <div class="container mx-auto px-4 max-w-7xl">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-black">我的图库</h1>
        <p class="text-sm mt-2 text-gray-600">管理您上传的所有精彩瞬间。</p>
      </div>

      <div v-if="loading" class="w-full h-48 flex flex-col items-center justify-center gap-3 bg-gray-100 border-2 border-black">
        <div class="text-gray-600 text-sm">加载资源中...</div>
      </div>

      <div v-else>
        <div v-if="imageList.length === 0" class="flex flex-col items-center justify-center py-20 text-center">
          <div class="bg-gray-200 p-6 mb-4 border-2 border-black w-20 h-20 flex items-center justify-center">
             <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="18" height="18" x="3" y="3"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
          </div>
          <h3 class="text-lg font-bold text-black">暂无图片</h3>
          <p class="text-gray-600 mt-2 text-sm">点击右上角的上传按钮添加您的第一张图片。</p>
        </div>

        <template v-else>
          <div v-if="currentLayout === 'grid'" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-3">
             <div v-for="image in imageList" :key="image.imageId"
               class="bg-white border-2 border-black flex flex-col cursor-pointer"
               @click="goToImageDetail(image)">
               <div class="w-full h-56 bg-gray-200 border-b-2 border-black flex items-center justify-center">
                 <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="18" height="18" x="3" y="3"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
               </div>
               <div class="p-3 border-b-2 border-black">
                  <p class="text-sm font-bold text-black truncate">{{ image.fileName }}</p>
                  <p class="text-xs text-gray-600 mt-1">{{ formatBytes(image.size) }}</p>
               </div>
               <div class="p-3 flex items-center justify-between">
                  <span class="text-xs text-gray-600">{{ image.isPublic ? '公开' : '私有' }}</span>
                  <div class="flex gap-2" @click.stop>
                     <button @click.stop="downloadImage(image)" class="px-2 py-1 border-2 border-black bg-white text-black text-xs">下载</button>
                     <button @click.stop="deleteImage(image)" :disabled="image.isDeleting" class="px-2 py-1 border-2 border-black text-black bg-white text-xs">删除</button>
                  </div>
               </div>
             </div>
          </div>

          <div v-else-if="currentLayout === 'list'" class="bg-white border-2 border-black overflow-x-auto">
             <table class="w-full text-sm">
               <thead class="bg-gray-200 border-b-2 border-black">
                 <tr>
                   <th class="px-4 py-3 text-left font-bold text-black">预览</th>
                   <th class="px-4 py-3 text-left font-bold text-black">文件名</th>
                   <th class="px-4 py-3 text-left font-bold text-black">类型</th>
                   <th class="px-4 py-3 text-left font-bold text-black">大小</th>
                   <th class="px-4 py-3 text-left font-bold text-black">状态</th>
                   <th class="px-4 py-3 text-left font-bold text-black">上传时间</th>
                   <th class="px-4 py-3 text-right font-bold text-black">操作</th>
                 </tr>
               </thead>
               <tbody class="divide-y-2 divide-black">
                 <tr v-for="image in imageList" :key="image.imageId" 
                     class="bg-white cursor-pointer"
                     @click="goToImageDetail(image)">
                   <td class="px-4 py-3">
                     <div class="w-10 h-10 bg-gray-200 border-2 border-black flex items-center justify-center">
                       <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="18" height="18" x="3" y="3"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
                     </div>
                   </td>
                   <td class="px-4 py-3 font-bold text-black truncate max-w-[200px]">{{ image.fileName }}</td>
                   <td class="px-4 py-3 text-gray-600">{{ image.contentType.split('/')[1]?.toUpperCase() || 'UNK' }}</td>
                   <td class="px-4 py-3 text-gray-600">{{ formatBytes(image.size) }}</td>
                   <td class="px-4 py-3 text-gray-600">{{ image.isPublic ? '公开' : '私有' }}</td>
                   <td class="px-4 py-3 text-xs text-gray-600">{{ formatTimestamp(image.uploadTime) }}</td>
                   <td class="px-4 py-3 text-right" @click.stop>
                     <div class="flex justify-end gap-2">
                       <button @click.stop="downloadImage(image)" class="px-3 py-1 border-2 border-black text-black bg-white text-xs">下载</button>
                       <button @click.stop="deleteImage(image)" :disabled="image.isDeleting" class="px-3 py-1 border-2 text-black border-black bg-white text-xs">删除</button>
                     </div>
                   </td>
                 </tr>
               </tbody>
             </table>
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
  thumbnailMinioUrl: string;
  watermarkMinioUrl: string;
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
onMounted(() => {
  loading.value = true;
  // 模拟20张图片数据
  const mockImages: Image[] = Array.from({ length: 20 }, (_, index) => ({
    imageId: `img-${index + 1}`,
    thumbnailMinioUrl: '',
    watermarkMinioUrl: '',
    fileName: `image-${String(index + 1).padStart(2, '0')}.jpg`,
    userId: 'user-1',
    contentType: 'image/jpeg',
    description: null,
    size: Math.floor(Math.random() * 5000000) + 500000,
    isPublic: Math.random() > 0.5,
    uploadTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toISOString(),
    isToggling: false,
    isDeleting: false,
  }));
  
  imageList.value = mockImages;
  loading.value = false;
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
</style>