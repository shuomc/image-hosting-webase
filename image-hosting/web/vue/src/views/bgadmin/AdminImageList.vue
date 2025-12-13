<template>
  <div class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Filters -->
    <div class="bg-white dark:bg-slate-800 p-4 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 flex flex-wrap gap-4 items-center justify-between">
      <div class="flex gap-2">
        <button @click="handleFilter('')" :class="{'bg-indigo-50 text-indigo-600 dark:bg-indigo-900/20 dark:text-indigo-400 border-indigo-100 dark:border-indigo-800': !query.type, 'text-slate-600 dark:text-slate-400 border-transparent': query.type}" class="px-4 py-2 rounded-xl text-sm font-medium border transition-colors">全部</button>
        <button @click="handleFilter('public')" :class="{'bg-indigo-50 text-indigo-600 dark:bg-indigo-900/20 dark:text-indigo-400 border-indigo-100 dark:border-indigo-800': query.type === 'public', 'text-slate-600 dark:text-slate-400 border-transparent': query.type !== 'public'}" class="px-4 py-2 rounded-xl text-sm font-medium border transition-colors">公开</button>
        <button @click="handleFilter('private')" :class="{'bg-indigo-50 text-indigo-600 dark:bg-indigo-900/20 dark:text-indigo-400 border-indigo-100 dark:border-indigo-800': query.type === 'private', 'text-slate-600 dark:text-slate-400 border-transparent': query.type !== 'private'}" class="px-4 py-2 rounded-xl text-sm font-medium border transition-colors">私有</button>
      </div>
      <div class="flex gap-2">
        <input 
          v-model="query.keyword"
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="搜索图片ID或名称..." 
          class="w-64 px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all text-sm"
        />
      </div>
    </div>

    <!-- Image Grid -->
    <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
      <div v-for="img in images" :key="img.imageId" class="group relative bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
        <!-- Image Thumbnail -->
        <div class="aspect-[4/3] overflow-hidden bg-slate-100 dark:bg-slate-900 relative">
          <img :src="img.thumbnailMinioUrl || img.originMinioUrl" :alt="img.fileName" class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" loading="lazy" />
          
          <!-- Overlay Actions -->
          <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center gap-2">
            <a :href="img.thumbnailMinioUrl" target="_blank" class="p-2 bg-white/20 backdrop-blur-md rounded-full text-white hover:bg-white/40 transition-colors" title="查看原图">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
            </a>
            <button @click="handleDelete(img)" class="p-2 bg-red-500/80 backdrop-blur-md rounded-full text-white hover:bg-red-600 transition-colors" title="删除">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Info -->
        <div class="p-4">
          <h4 class="text-sm font-medium text-slate-900 dark:text-white truncate" :title="img.fileName">{{ img.fileName }}</h4>
          <div class="flex justify-between items-center mt-2">
            <span class="text-xs text-slate-500 dark:text-slate-400">{{ formatSize(img.size) }}</span>
            <span class="text-xs px-2 py-0.5 rounded-full bg-slate-100 dark:bg-slate-700 text-slate-600 dark:text-slate-300">{{ img.contentType ? img.contentType.split('/')[1].toUpperCase() : 'UNK' }}</span>
          </div>
          <div class="mt-3 pt-3 border-t border-slate-100 dark:border-slate-700 flex items-center gap-2">
            <div class="w-5 h-5 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 text-white flex items-center justify-center text-[10px] font-bold">
              {{ img.userId ? img.userId.charAt(0).toUpperCase() : '?' }}
            </div>
            <span class="text-xs text-slate-500 dark:text-slate-400 truncate">{{ img.userId }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Pagination -->
    <div class="p-4 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between bg-white dark:bg-slate-800 rounded-2xl">
      <span class="text-sm text-slate-500 dark:text-slate-400">
        Showing {{ (query.page - 1) * query.size + 1 }} to {{ Math.min(query.page * query.size, total) }} of {{ total }} entries
      </span>
      <div class="flex gap-2">
        <button 
          @click="changePage(query.page - 1)"
          :disabled="query.page <= 1"
          class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-600 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-50">
          Previous
        </button>
        <button 
          @click="changePage(query.page + 1)"
          :disabled="query.page * query.size >= total"
          class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-600 text-sm text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-50">
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { getImageList, deleteImage, type ImageVO, type ImageListQuery } from '@/api/admin/image';
import { ElMessage, ElMessageBox } from 'element-plus';

const images = ref<ImageVO[]>([]);
const total = ref(0);
const query = reactive<ImageListQuery>({
  page: 1,
  size: 10,
  keyword: '',
  type: ''
});

const loadData = async () => {
  try {
    const res = await getImageList(query);
    if (res.code === 200) {
      images.value = res.data.records;
      total.value = res.data.total;
    }
  } catch (error) {
    console.error('Failed to load images', error);
  }
};

const handleSearch = () => {
  query.page = 1;
  loadData();
};

const handleFilter = (type: string) => {
  query.type = type;
  query.page = 1;
  loadData();
};

const changePage = (newPage: number) => {
  query.page = newPage;
  loadData();
};

const handleDelete = (img: ImageVO) => {
  ElMessageBox.confirm(
    '确定要删除这张图片吗？如果该图片已铸造为NFT，链上信息也将被标记为删除。',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
        const res = await deleteImage(img.imageId);
        if (res.code === 200) {
          ElMessage.success('删除成功');
          loadData();
        } else {
          ElMessage.error(res.message || '删除失败');
        }
      } catch (error) {
        ElMessage.error('删除失败');
      }
    })
    .catch(() => {
      // Cancelled
    });
};

const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

onMounted(() => {
  loadData();
});
</script>
