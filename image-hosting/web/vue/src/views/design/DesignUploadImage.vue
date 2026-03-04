<template>
  <div class="w-full h-full overflow-y-auto bg-white pt-4 px-4">
    
    <div class="container mx-auto max-w-4xl pb-10">
      
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-black">上传图片</h1>
        <p class="text-gray-600 mt-2">支持批量拖拽上传，快速分享您的精彩瞬间</p>
      </div>

      <div class="bg-white border-2 border-black p-8">
        
        <div 
          class="relative cursor-pointer"
          @dragover.prevent="isDragOver = true"
          @dragleave.prevent="isDragOver = false"
          @drop.prevent="handleDrop"
          @click="triggerFileInput"
        >
          <input 
            type="file" 
            ref="fileInput" 
            multiple 
            class="hidden" 
            @change="handleFileSelect" 
            accept="image/*"
          />

          <div 
            class="w-full h-48 border-4 border-black flex flex-col items-center justify-center"
            :class="isDragOver ? 'bg-gray-300' : 'bg-gray-200'"
          >
            <div class="mb-4">
              <CloudArrowUpIcon class="w-12 h-12 text-gray-600" />
            </div>
            
            <p class="text-base font-bold text-black">
              点击上传 或将文件拖到此处
            </p>
            <p class="text-sm text-gray-700 mt-2">支持 JPG, PNG, GIF 等常见格式 (批量)</p>
          </div>
        </div>

        <div v-if="selectedFiles.length > 0" class="mt-8 space-y-2">
          <div class="flex justify-between items-center mb-4 pb-2 border-b-2 border-black">
            <h3 class="text-sm font-bold text-black uppercase">
              待上传 ({{ selectedFiles.length }})
            </h3>
            <button @click="clearAllFiles" class="text-xs text-black font-bold border-2 border-black px-2 py-1">清空列表</button>
          </div>

          <ul class="space-y-2">
            <li v-for="(file, index) in selectedFiles" :key="file.name + index"
              class="flex items-center justify-between p-2 bg-white border-2 border-black"
            >
              <div class="flex items-center gap-2 overflow-hidden">
                <div class="w-8 h-8 bg-gray-300 border border-black flex items-center justify-center flex-shrink-0">
                   <PhotoIcon class="w-4 h-4 text-gray-600" />
                </div>
                <div class="flex flex-col min-w-0">
                  <span class="text-sm font-medium text-black truncate">{{ file.name }}</span>
                  <span class="text-xs text-gray-600">{{ formatFileSize(file.size) }}</span>
                </div>
              </div>
              
              <button @click.stop="removeFile(index)" 
                class="p-1 text-black border-2 border-black font-bold text-xs"
                title="移除文件"
              >
                X
              </button>
            </li>
          </ul>
        </div>

        <div class="mt-8 space-y-4">
          <div>
            <label class="block text-sm font-bold text-black mb-2">图片描述 (可选)</label>
            <textarea 
              v-model="description" 
              rows="3"
              class="w-full px-2 py-2 bg-white border-2 border-black text-black placeholder-gray-600 focus:outline-none resize-none"
              placeholder="记录这张图片的背后的故事..."
            ></textarea>
          </div>

          <div class="flex items-center justify-between bg-gray-200 border-2 border-black p-4">
            <div class="flex flex-col">
              <span class="text-sm font-bold text-black">公开可见</span>
              <span class="text-xs text-gray-700 mt-0.5">关闭后仅自己可见 (Private)</span>
            </div>
            <button 
              @click="isPublic = !isPublic"
              class="px-4 py-2 border-2 border-black font-bold text-xs"
              :class="isPublic ? 'bg-black text-white' : 'bg-white text-black'"
            >
              {{ isPublic ? '公开' : '私密' }}
            </button>
          </div>
        </div>

        <div class="mt-10">
          <button 
            @click="uploadBatch"
            :disabled="selectedFiles.length === 0 || isUploading"
            class="w-full py-3 border-2 border-black font-bold text-base flex items-center justify-center gap-2"
            :class="selectedFiles.length === 0 || isUploading
              ? 'bg-gray-300 text-gray-600 cursor-not-allowed'
              : 'bg-black text-white hover:bg-gray-800'
            "
          >
            <span v-if="isUploading">
              正在上传...
            </span>
            <span v-else>
              开始上传 ({{ selectedFiles.length }})
            </span>
          </button>
        </div>

        <div v-if="uploadResult || uploadError" class="mt-6 overflow-hidden">
          
          <div v-if="uploadResult && uploadResult.code === 200" 
               class="bg-gray-100 border-2 border-black p-4">
            <div class="flex items-start gap-3">
              <CheckCircleIcon class="w-6 h-6 text-black mt-0.5 flex-shrink-0" />
              <div>
                <h4 class="font-bold text-black">上传完成</h4>
                <div v-if="uploadResult.data?.length > 0" class="mt-2 text-sm text-black">
                  <p class="font-bold mb-1">成功文件:</p>
                  <ul class="list-disc list-inside space-y-0.5">
                    <li v-for="img in uploadResult.data" :key="img.imageId">{{ img.fileName }}</li>
                  </ul>
                </div>
                <div v-if="uploadResult.failedFiles?.length > 0" class="mt-3 text-sm text-black">
                   <p class="font-bold mb-1">失败文件:</p>
                   <ul class="list-disc list-inside space-y-0.5">
                      <li v-for="fileName in uploadResult.failedFiles" :key="fileName">{{ fileName }}</li>
                   </ul>
                </div>
              </div>
            </div>
          </div>

          <div v-else-if="uploadError || (uploadResult && uploadResult.code !== 200)" 
               class="bg-gray-200 border-2 border-black p-4">
             <div class="flex items-start gap-3">
                <ExclamationTriangleIcon class="w-6 h-6 text-black mt-0.5 flex-shrink-0" />
                <div>
                  <h4 class="font-bold text-black">上传遇到问题</h4>
                  <p class="text-sm text-black mt-1">
                    {{ uploadError?.message || uploadResult?.msg || '未知错误，请重试' }}
                  </p>
                </div>
             </div>
          </div>

        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { 
  CloudArrowUpIcon, 
  PhotoIcon, 
  XMarkIcon, 
  ArrowUpTrayIcon, 
  CheckCircleIcon, 
  ExclamationTriangleIcon 
} from '@heroicons/vue/24/outline';
import service from '@/utils/request';
import { API_BASE_URL } from '@/config';
import { ElMessage } from 'element-plus'; // 仅保留用于全局提示

// 状态管理
const fileInput = ref<HTMLInputElement | null>(null);
const selectedFiles = ref<File[]>([]);
const isDragOver = ref(false);
const description = ref('');
const isPublic = ref(true);
const isUploading = ref(false);
const uploadResult = ref<any>(null);
const uploadError = ref<any>(null);

// 文件处理逻辑
const triggerFileInput = () => {
  fileInput.value?.click();
};

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files) {
    addFiles(Array.from(target.files));
  }
  // 清空 value 允许重复选择同一文件
  if (fileInput.value) fileInput.value.value = '';
};

const handleDrop = (event: DragEvent) => {
  isDragOver.value = false;
  if (event.dataTransfer?.files) {
    addFiles(Array.from(event.dataTransfer.files));
  }
};

const addFiles = (files: File[]) => {
  // 过滤图片类型 (可选)
  const imageFiles = files.filter(file => file.type.startsWith('image/'));
  if (imageFiles.length < files.length) {
    ElMessage.warning('已过滤非图片文件');
  }
  
  // 添加到列表 (去重逻辑可根据需求添加，这里简单追加)
  selectedFiles.value = [...selectedFiles.value, ...imageFiles];
  
  // 重置结果状态
  resetStatus();
};

const removeFile = (index: number) => {
  selectedFiles.value.splice(index, 1);
  if (selectedFiles.value.length === 0) resetStatus();
};

const clearAllFiles = () => {
  selectedFiles.value = [];
  resetStatus();
};

const resetStatus = () => {
  uploadResult.value = null;
  uploadError.value = null;
};

// 工具函数
const formatFileSize = (bytes: number) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

// 上传逻辑
const uploadBatch = async () => {
  if (selectedFiles.value.length === 0) return;

  isUploading.value = true;
  resetStatus();

  const formData = new FormData();
  selectedFiles.value.forEach(file => {
    formData.append('files', file);
  });
  formData.append('description', description.value);
  formData.append('isPublic', String(isPublic.value));

  try {
    const responseData = await service.post(`${API_BASE_URL}/api/images/batch-upload`, formData);
    uploadResult.value = responseData;

    if (responseData.code === 200) {
      // 成功后清空
      selectedFiles.value = [];
      description.value = '';
      // isPublic.value = true; // 可选重置
      ElMessage.success('上传成功！');
    } else {
      ElMessage.warning('上传部分失败，请查看详情');
    }
  } catch (error: any) {
    console.error(error);
    uploadError.value = error;
    ElMessage.error('上传请求失败');
  } finally {
    isUploading.value = false;
  }
};
</script>

<style scoped>
/* 简单样式 */
</style>