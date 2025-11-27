<template>
  <div class="min-h-screen w-full h-full relative transition-colors">

    <!-- <div class="absolute inset-0 z-0 pointer-events-none overflow-hidden">
      <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-400/20 blur-[100px]"></div>
      <div class="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-400/20 blur-[100px]"></div>
    </div> -->

    <div class="relative z-10 container mx-auto px-4 py-1 max-w-6xl">
      <div class="flex items-center justify-between mb-8 gap-4">
        <div class="flex items-center gap-4">
          <button @click="$router.back()" class="p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-500 dark:text-slate-400 transition-all duration-200 -translate-y-0 hover:-translate-y-1 hover:shadow-md">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M15 18l-6-6 6-6"/></svg>
          </button>
          <div>
            <h1 class="text-3xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-500">图片详情</h1>
            <p class="text-sm mt-1 dark:text-slate-300 text-slate-500">查看并管理图片的详细信息与链接</p>
          </div>
        </div>
      </div>

      <div v-if="loading" class="w-full h-40 flex items-center justify-center text-slate-400">加载中…</div>
      <div v-else-if="error" class="w-full text-center text-red-500">加载图片详情失败: {{ error.message }}</div>

      <div v-else-if="imageDetail" class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="md:col-span-2 bg-slate-100 rounded-2xl shadow-sm border border-slate-100 overflow-hidden dark:bg-slate-800 dark:border-slate-700">
          <div class="relative w-full aspect-[4/3] bg-white overflow-hidden dark:bg-slate-900 dark:border-slate-700">
            <img :src="imageDetail.minioUrl" :alt="imageDetail.fileName" class="w-full h-full object-contain dark:bg-white/5" />
          </div>
        </div>

        <div class="md:col-span-1 bg-white rounded-2xl shadow-sm border border-slate-100 p-6 flex flex-col justify-between dark:bg-slate-800 dark:border-slate-700">
          <div>
            <div class="flex items-start justify-between gap-2 mb-2">
              <div class="flex-grow min-w-0">
                <input v-if="isEditing" 
                       v-model="editForm.fileName"
                       type="text"
                       class="w-full px-2 py-1 text-lg font-bold border rounded-lg outline-none bg-slate-50 border-slate-300 focus:ring-2 focus:ring-indigo-500 dark:bg-slate-900 dark:border-slate-600 dark:text-white"
                       placeholder="请输入文件名"
                />
                <h2 v-else class="font-bold text-lg text-slate-800 dark:text-white truncate" :title="imageDetail.fileName">
                  {{ imageDetail.fileName }}
                </h2>
              </div>

              <button @click="toggleEditMode" 
                      :disabled="isUpdating"
                      class="flex-shrink-0 p-2 ml-1 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors"
                      :class="isEditing ? 'text-green-600 bg-green-50 dark:bg-green-900/30 dark:text-green-400' : 'text-slate-400 hover:text-indigo-600 dark:text-slate-500 dark:hover:text-indigo-400'">
                <svg v-if="isUpdating" class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <CheckIcon v-else-if="isEditing" class="w-5 h-5" />
                <PencilSquareIcon v-else class="w-5 h-5" />
              </button>
            </div>

            <div class="mb-4">
              <textarea v-if="isEditing"
                        v-model="editForm.description"
                        rows="3"
                        class="w-full px-2 py-2 text-sm border rounded-lg outline-none resize-none bg-slate-50 border-slate-300 focus:ring-2 focus:ring-indigo-500 dark:bg-slate-900 dark:border-slate-600 dark:text-slate-300"
                        placeholder="请输入图片描述..."
              ></textarea>
              <p v-else class="text-sm text-slate-500 dark:text-slate-300 h-auto min-h-[3rem] break-words">
                {{ imageDetail.description || '暂无描述信息' }}
              </p>
            </div>

            <div class="space-y-2 text-sm text-slate-500 dark:text-slate-300 border-t border-slate-100 dark:border-slate-700 pt-4">
              <div><span class="font-medium text-slate-700 dark:text-slate-200">图片ID:</span> {{ imageDetail.imageId }}</div>
              <div><span class="font-medium text-slate-700 dark:text-slate-200">类型:</span> {{ imageDetail.contentType }}</div>
              <div><span class="font-medium text-slate-700 dark:text-slate-200">大小:</span> {{ formatBytes(imageDetail.size) }}</div>
              <div><span class="font-medium text-slate-700 dark:text-slate-200">上传用户:</span> {{ imageDetail.userId }}</div>
              
              <div class="flex items-center h-6">
                <span class="font-medium text-slate-700 dark:text-slate-200 mr-2">公开:</span>
                
                <div v-if="isEditing" @click.stop>
                   <el-switch 
                      v-model="editForm.isPublic" 
                      size="small"
                      style="--el-switch-on-color: #6366f1; --el-switch-off-color: #94a3b8"
                   />
                   <span class="ml-2 text-xs text-slate-400">{{ editForm.isPublic ? '公开可见' : '私有' }}</span>
                </div>
                
                <span v-else class="ml-1">{{ imageDetail.isPublic ? '是' : '否' }}</span>
              </div>
              
              <div v-if="imageDetail.uploadTime"><span class="font-medium text-slate-700 dark:text-slate-200">上传时间:</span> {{ formatTimestamp(imageDetail.uploadTime) }}</div>
            </div>
            </div>

          <div class="mt-6 flex flex-col gap-3">
            <button @click="downloadImage(imageDetail)" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-green-50 dark:bg-green-500/10 text-green-700 dark:text-green-400 hover:bg-green-100 dark:hover:bg-green-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-green-500/20">
              <ArrowDownTrayIcon class="w-5 h-5" />
              下载图片
            </button>

            <button @click="showMintDialog" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-indigo-50 dark:bg-indigo-500/10 text-indigo-700 dark:text-indigo-400 hover:bg-indigo-100 dark:hover:bg-indigo-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-indigo-500/20">
              <SparklesIcon class="w-5 h-5" />
              铸造 NFT
            </button>

            <button @click="deleteImage(imageDetail)" :disabled="isDeleting" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-red-50 dark:bg-red-500/10 text-red-700 dark:text-red-400 hover:bg-red-100 dark:hover:bg-red-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-red-500/20 disabled:opacity-50 disabled:hover:translate-y-0 disabled:hover:shadow-none">
              <TrashIcon class="w-5 h-5" />
              {{ isDeleting ? '删除中...' : '删除图片' }}
            </button>
          </div>
        </div>
      </div>

      <div v-if="imageDetail" class="mt-8 bg-white rounded-2xl shadow-sm border border-slate-100 p-6 dark:bg-slate-800 dark:border-slate-700">
        <h3 class="text-lg font-semibold text-slate-800 dark:text-white mb-4">使用链接</h3>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div v-for="(label, idx) in ['直链','Markdown','HTML','BBCode','CSS 背景图']" :key="idx">
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">{{ label }}:</label>
            <div class="flex items-center gap-2">
              <input type="text" :value="getLinkByLabel(label)" readonly class="flex-grow rounded-lg px-3 py-2 text-sm bg-slate-100 dark:bg-slate-900 text-slate-700 dark:text-slate-200 truncate border border-slate-200 dark:border-slate-700" />
              <button @click="copyToClipboard(getLinkByLabel(label))" class="px-3 py-2 rounded-lg bg-indigo-500 text-white hover:bg-indigo-600 text-sm">复制</button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, reactive } from 'vue'; // 引入 reactive
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import service from '@/utils/request';
import { API_BASE_URL } from '@/config';
import { ElMessage } from 'element-plus';
import { ElMessageBox } from 'element-plus';
import { useUserStore } from '@/stores/user';
import { mintNFT } from '@/api/nft';
// === 引入 Heroicons 的 PencilSquareIcon 和 CheckIcon ===
import { ArrowDownTrayIcon, SparklesIcon, TrashIcon, PencilSquareIcon, CheckIcon } from '@heroicons/vue/24/outline';

// 接口定义
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
}

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const imageDetail = ref<Image | null>(null);
const loading = ref(true);
const error = ref<Error | null>(null);
const isDeleting = ref(false);

// === ✨ 新增状态：编辑相关 ===
const isEditing = ref(false);
const isUpdating = ref(false); // 保存操作的 loading 状态
// 编辑表单数据
const editForm = reactive({
    fileName: '',
    description: '',
    isPublic: false
});

// === ✨ 业务逻辑：切换编辑模式/保存 ===
const toggleEditMode = async () => {
    // 1. 如果当前是编辑模式，点击则意味着“保存”
    if (isEditing.value) {
        await handleUpdateImage();
    } else {
        // 2. 如果当前是查看模式，点击则进入“编辑”
        // 初始化表单数据
        if (imageDetail.value) {
            editForm.fileName = imageDetail.value.fileName;
            editForm.description = imageDetail.value.description || '';
            editForm.isPublic = imageDetail.value.isPublic;
        }
        isEditing.value = true;
    }
};

// === ✨ 业务逻辑：提交更新到 API ===
const handleUpdateImage = async () => {
    if (!imageDetail.value) return;
    
    // 简单校验
    if (!editForm.fileName.trim()) {
        ElMessage.warning('文件名不能为空');
        return;
    }

    isUpdating.value = true;

    try {
        // 构建 FormData，适配后端的 @ModelAttribute
        const formData = new FormData();
        formData.append('imageId', imageDetail.value.imageId);
        formData.append('fileName', editForm.fileName);
        formData.append('description', editForm.description);
        formData.append('isPublic', editForm.isPublic.toString());
        // 如果需要其他字段，继续 append

        const updateUrl = `${API_BASE_URL}/api/images/update`;
        
        // 发送 POST 请求
        const responseData = await service.post(updateUrl, formData);

        if (responseData.code === 200) {
            ElMessage.success('图片信息修改成功');
            
            // 更新本地视图数据
            imageDetail.value.fileName = editForm.fileName;
            imageDetail.value.description = editForm.description;
            imageDetail.value.isPublic = editForm.isPublic;
            
            // 退出编辑模式
            isEditing.value = false;
        } else {
            ElMessage.error(responseData.msg || '修改失败');
        }
    } catch (err: any) {
        console.error('更新图片信息失败:', err);
        // service 拦截器通常会处理错误弹窗，这里可省略
    } finally {
        isUpdating.value = false;
    }
};

// ... 原有的下载、删除、复制、Mint 逻辑保持不变 ...

const downloadImage = (image: Image) => {
  if (!image || !image.imageId || !image.fileName) {
    ElMessage.warning('图片信息不完整，无法下载。');
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

const deleteImage = async (image: Image) => {
  if (!image || !image.imageId) {
    ElMessage.warning('图片信息不完整，无法删除。');
    return;
  }

  try {
    await ElMessageBox.confirm(`确定要删除图片 "${image.fileName}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });

    isDeleting.value = true;
    const deleteUrl = `${API_BASE_URL}/api/images/deleteById/${image.imageId}`;
    const responseData = await service.post(deleteUrl);

    if (responseData.code === 200) {
      ElMessage.success(responseData.msg || '删除成功');
      router.push({ name: 'MyImages' });
    } else {
       console.error('图片删除业务失败:', responseData.msg);
       ElMessage.error(responseData.msg || '删除失败');
    }

  } catch (error: any) {
    if (error !== 'cancel') {
       console.error('图片删除请求或确认框错误:', error);
    } 
  } finally {
    isDeleting.value = false;
  }
};

// ... Helper functions ...
const formatBytes = (bytes: number | undefined, decimals = 2): string => {
  if (bytes === undefined || bytes === null || bytes === 0) return '0 Bytes';
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
};

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
  } catch (error) {
    return '格式错误';
  }
};

const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    ElMessage.success('已复制到剪贴板');
  } catch (err) {
    console.error('复制失败:', err);
    ElMessage.error('复制失败，请手动选择复制');
  }
};

const getLinkByLabel = (label: string) => {
  if (!imageDetail.value) return '';
  const url = imageDetail.value.minioUrl || '';
  const fileName = imageDetail.value.fileName || 'image';
  switch (label) {
    case '直链': return url;
    case 'Markdown': return `![${fileName}](${url})`;
    case 'HTML': return `<img src="${url}" alt="${fileName}">`;
    case 'BBCode': return `[img]${url}[/img]`;
    case 'CSS 背景图': return `background-image: url('${url}');`;
    default: return url;
  }
};

const fetchImageDetail = async (imageId: string) => {
  loading.value = true;
  error.value = null;
  imageDetail.value = null;

  const storedImage = userStore.findImageById(imageId);
  if (storedImage) {
    imageDetail.value = storedImage;
    loading.value = false;
  } else {
    const apiUrl = `${API_BASE_URL}/api/images/${imageId}`;
    try {
      const responseData = await service.get(apiUrl);
      if (responseData.code === 200 && responseData.data) {
        imageDetail.value = responseData.data as Image;
      } else {
        error.value = new Error(responseData.msg || '获取图片详情失败');
        ElMessage.error(error.value.message);
      }
    } catch (err: any) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  }
};

watch(() => route.params.imageId, (newImageId) => {
  if (typeof newImageId === 'string' && newImageId) {
    fetchImageDetail(newImageId);
  } else {
    imageDetail.value = null;
    error.value = new Error('缺少图片 ID');
    loading.value = false;
    ElMessage.error(error.value.message);
  }
}, { immediate: true });

// NFT Minting logic
const mintDialogVisible = ref(false);
const minting = ref(false);
const mintFormRef = ref();
const mintForm = ref({ description: '', price: 0 });

const showMintDialog = () => {
  mintForm.value = {
    description: imageDetail.value?.description || '',
    price: 0
  };
  mintDialogVisible.value = true;
};

// ... NFT minting logic omitted for brevity as it was not changed ...
</script>

<style scoped>
/* 保持原有样式 */
</style>