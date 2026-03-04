<template>
  <div class="absolute inset-0 w-full h-full overflow-y-auto custom-scrollbar pt-20 px-4 transition-colors duration-300">
    
    <!-- <div class="absolute inset-0 z-0 pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-400/10 dark:bg-indigo-400/5 blur-[100px]"></div>
      <div class="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-400/10 dark:bg-purple-400/5 blur-[100px]"></div>
    </div> -->

    <div class="relative z-10 container mx-auto max-w-4xl pb-10">
      
      <div class="mb-8 text-center sm:text-left">
        <h1 class="text-3xl font-extrabold text-slate-800 dark:text-slate-100 tracking-tight">上传图片</h1>
        <p class="text-slate-500 dark:text-slate-400 mt-2">支持批量拖拽上传，快速分享您的精彩瞬间</p>
      </div>

      <div class="bg-white/70 dark:bg-slate-800/70 backdrop-blur-xl rounded-3xl shadow-xl border border-white/50 dark:border-slate-700 p-6 sm:p-10 transition-all duration-300">
        
        <div 
          class="relative group cursor-pointer"
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
            class="w-full h-64 border-2 border-dashed rounded-2xl flex flex-col items-center justify-center transition-all duration-300 ease-out"
            :class="[
              isDragOver 
                ? 'border-indigo-500 bg-indigo-50/50 dark:bg-indigo-500/10 scale-[1.01]' 
                : 'border-slate-300 dark:border-slate-600 bg-slate-50/50 dark:bg-slate-900/30 hover:border-indigo-400 dark:hover:border-indigo-400 hover:bg-white/50 dark:hover:bg-slate-800/50'
            ]"
          >
            <div class="p-4 rounded-full bg-indigo-100 dark:bg-indigo-900/50 mb-4 transition-transform duration-300 group-hover:scale-110 group-hover:rotate-3">
              <CloudArrowUpIcon class="w-10 h-10 text-indigo-600 dark:text-indigo-400" />
            </div>
            
            <p class="text-lg font-medium text-slate-700 dark:text-slate-200">
              <span class="text-indigo-600 dark:text-indigo-400">点击上传</span> 或将文件拖到此处
            </p>
            <p class="text-sm text-slate-400 dark:text-slate-500 mt-2">支持 JPG, PNG, GIF 等常见格式 (批量)</p>
          </div>
        </div>

        <div v-if="selectedFiles.length > 0" class="mt-8 space-y-3">
          <div class="flex justify-between items-center mb-2">
            <h3 class="text-sm font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
              待上传 ({{ selectedFiles.length }})
            </h3>
            <button @click="clearAllFiles" class="text-xs text-red-500 hover:text-red-600 font-medium">清空列表</button>
          </div>

          <transition-group name="list" tag="ul" class="space-y-2">
            <li v-for="(file, index) in selectedFiles" :key="file.name + index"
              class="flex items-center justify-between p-3 bg-white dark:bg-slate-900 border border-slate-100 dark:border-slate-700 rounded-xl shadow-sm hover:shadow-md transition-all duration-200"
            >
              <div class="flex items-center gap-3 overflow-hidden">
                <div class="w-10 h-10 rounded-lg bg-slate-100 dark:bg-slate-800 flex items-center justify-center flex-shrink-0">
                   <PhotoIcon class="w-5 h-5 text-slate-500 dark:text-slate-400" />
                </div>
                <div class="flex flex-col min-w-0">
                  <span class="text-sm font-medium text-slate-700 dark:text-slate-200 truncate">{{ file.name }}</span>
                  <span class="text-xs text-slate-400">{{ formatFileSize(file.size) }}</span>
                </div>
              </div>
              
              <button @click.stop="removeFile(index)" 
                class="p-2 text-slate-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/30 rounded-lg transition-colors"
                title="移除文件"
              >
                <XMarkIcon class="w-5 h-5" />
              </button>
            </li>
          </transition-group>
        </div>

        <div class="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="md:col-span-2">
            <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2">图片描述 (可选)</label>
            <textarea 
              v-model="description" 
              rows="3"
              class="w-full px-4 py-3 bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-xl text-slate-700 dark:text-slate-200 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all resize-none"
              placeholder="记录这张图片的背后的故事..."
            ></textarea>
          </div>

          <div class="flex items-center justify-between bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 p-4 rounded-xl">
            <div class="flex flex-col">
              <span class="text-sm font-semibold text-slate-700 dark:text-slate-200">公开可见</span>
              <span class="text-xs text-slate-400 mt-0.5">关闭后仅自己可见 (Private)</span>
            </div>
            <button 
              @click="isPublic = !isPublic"
              class="relative w-12 h-7 rounded-full transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-indigo-500/30"
              :class="isPublic ? 'bg-indigo-500' : 'bg-slate-300 dark:bg-slate-600'"
            >
              <span 
                class="absolute top-1 left-1 bg-white w-5 h-5 rounded-full shadow-md transform transition-transform duration-300"
                :class="isPublic ? 'translate-x-5' : 'translate-x-0'"
              ></span>
            </button>
          </div>
        </div>

        <div class="mt-10">
          <button 
            @click="uploadBatch"
            :disabled="selectedFiles.length === 0 || isUploading"
            class="w-full py-3.5 rounded-xl font-bold text-white shadow-lg shadow-indigo-500/30 transition-all duration-300 flex items-center justify-center gap-2"
            :class="[
              selectedFiles.length === 0 || isUploading
                ? 'bg-slate-300 dark:bg-slate-700 cursor-not-allowed opacity-70 shadow-none'
                : 'bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 hover:shadow-indigo-500/50 hover:-translate-y-0.5 active:translate-y-0'
            ]"
          >
            <span v-if="isUploading" class="flex items-center gap-2">
              <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              正在上传...
            </span>
            <span v-else class="flex items-center gap-2">
              <ArrowUpTrayIcon class="w-5 h-5" />
              开始上传 ({{ selectedFiles.length }})
            </span>
          </button>
        </div>

        <transition name="fade">
          <div v-if="uploadResult || uploadError" class="mt-6 rounded-2xl overflow-hidden shadow-sm animate-fade-in-up">
            
            <div v-if="uploadResult && uploadResult.code === 200" 
                 class="bg-green-50 dark:bg-green-900/20 border-green-200 dark:border-green-800 p-4 border-2">
              <div class="flex items-start gap-3">
                <CheckCircleIcon class="w-6 h-6 text-green-500 mt-0.5 flex-shrink-0" />
                <div>
                  <h4 class="font-bold text-green-800 dark:text-green-300">上传完成</h4>
                  <div v-if="uploadResult.data?.length > 0" class="mt-2 text-sm text-green-700 dark:text-green-400">
                    <p class="font-semibold mb-1">成功文件:</p>
                    <ul class="list-disc list-inside space-y-0.5 opacity-80">
                      <li v-for="img in uploadResult.data" :key="img.imageId">{{ img.fileName }}</li>
                    </ul>
                  </div>
                  <div v-if="uploadResult.failedFiles?.length > 0" class="mt-3 text-sm text-red-600 dark:text-red-400">
                     <p class="font-semibold mb-1">失败文件:</p>
                     <ul class="list-disc list-inside space-y-0.5 opacity-80">
                        <li v-for="fileName in uploadResult.failedFiles" :key="fileName">{{ fileName }}</li>
                     </ul>
                  </div>
                </div>
              </div>
            </div>

            <div v-else-if="uploadError || (uploadResult && uploadResult.code !== 200)" 
                 class="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 p-4">
               <div class="flex items-start gap-3">
                  <ExclamationTriangleIcon class="w-6 h-6 text-red-500 mt-0.5 flex-shrink-0" />
                  <div>
                    <h4 class="font-bold text-red-800 dark:text-red-300">上传遇到问题</h4>
                    <p class="text-sm text-red-700 dark:text-red-400 mt-1">
                      {{ uploadError?.message || uploadResult?.msg || '未知错误，请重试' }}
                    </p>
                  </div>
               </div>
            </div>

          </div>
        </transition>

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
/* 列表过渡动画 */
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* 简单的淡入淡出 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 自定义滚动条 */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.5);
  border-radius: 20px;
}
</style>