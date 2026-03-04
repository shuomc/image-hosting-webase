<template>
  <div class="bg-white/80 dark:bg-slate-800/80 backdrop-blur-md rounded-3xl shadow-xl border border-slate-100 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Toolbar -->
    <div class="p-8 border-b border-slate-100 dark:border-slate-700 flex flex-col sm:flex-row sm:items-center justify-between gap-6 bg-gradient-to-r from-slate-50/50 to-transparent dark:from-slate-900/50">
      <div>
        <h2 class="text-2xl font-black text-slate-900 dark:text-white tracking-tight">公告管理</h2>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">发布和维护全站系统通知与公告</p>
      </div>
      <div class="flex gap-3">
        <button 
          @click="openDialog()" 
          class="px-6 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-2xl text-sm font-bold transition-all shadow-lg shadow-indigo-500/25 flex items-center transform hover:-translate-y-0.5 active:translate-y-0"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 4v16m8-8H4" />
          </svg>
          发布新公告
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto p-4">
      <table class="w-full text-left border-separate border-spacing-y-2">
        <thead>
          <tr class="text-slate-400 dark:text-slate-500 text-[10px] uppercase font-black tracking-widest px-6">
            <th class="px-6 py-4">公告标题</th>
            <th class="px-6 py-4">内容预览</th>
            <th class="px-6 py-4 text-center">发布时间</th>
            <th class="px-6 py-4 text-right">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="notices.length === 0">
            <td colspan="4" class="px-6 py-20 text-center">
              <div class="flex flex-col items-center opacity-40">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                </svg>
                <div class="text-lg font-bold">暂无公告数据</div>
              </div>
            </td>
          </tr>
          <tr v-for="notice in notices" :key="notice.noticeId" class="group bg-slate-50/50 dark:bg-slate-900/30 hover:bg-white dark:hover:bg-slate-800 transition-all duration-300 rounded-2xl shadow-sm border border-transparent hover:border-slate-100 dark:hover:border-slate-700">
            <td class="px-6 py-5 rounded-l-2xl">
              <div class="text-sm font-bold text-slate-800 dark:text-white transition-colors group-hover:text-indigo-600 dark:group-hover:text-indigo-400">{{ notice.title }}</div>
            </td>
            <td class="px-6 py-5 max-w-md">
              <div class="text-sm text-slate-500 dark:text-slate-400 line-clamp-1">{{ notice.content }}</div>
            </td>
            <td class="px-6 py-5 text-center">
              <div class="text-[11px] font-medium text-slate-400 dark:text-slate-500 bg-slate-100 dark:bg-slate-800/50 px-3 py-1 rounded-lg inline-block">
                {{ formatDate(notice.createTime) }}
              </div>
            </td>
            <td class="px-6 py-5 text-right rounded-r-2xl">
              <div class="flex items-center justify-end gap-2">
                <button @click="openDialog(notice)" class="p-2 text-indigo-600 hover:bg-indigo-50 dark:text-indigo-400 dark:hover:bg-indigo-900/40 rounded-xl transition-all font-bold text-xs">
                  编辑
                </button>
                <button @click="handleDelete(notice)" class="p-2 text-red-600 hover:bg-red-50 dark:text-red-400 dark:hover:bg-red-900/40 rounded-xl transition-all font-bold text-xs">
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑公告' : '发布公告'"
      width="600px"
      append-to-body
      destroy-on-close
      class="custom-dialog rounded-[2rem] overflow-hidden"
    >
      <el-form :model="form" label-position="top" class="p-2">
        <el-form-item label="公告标题">
          <el-input 
            v-model="form.title" 
            placeholder="请输入引人注目的标题" 
            class="custom-input"
          />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            :rows="8" 
            placeholder="在此输入公告的详细内容..." 
            class="custom-input"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="flex justify-end gap-3 px-2 pb-2">
          <button 
            @click="dialogVisible = false"
            class="px-6 py-2.5 rounded-xl border border-slate-200 dark:border-slate-700 text-sm font-bold text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 transition-all font-sans"
          >
            取消
          </button>
          <button 
            @click="handleSubmit"
            class="px-8 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-bold shadow-lg shadow-indigo-500/25 transition-all transform hover:-translate-y-0.5 active:translate-y-0 font-sans"
          >
            {{ isEdit ? '保存更改' : '立即发布' }}
          </button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

interface Notice {
  noticeId?: string;
  title: string;
  content: string;
  createTime?: string;
}

const notices = ref<Notice[]>([]);
const dialogVisible = ref(false);
const isEdit = ref(false);
const form = reactive<Notice>({
  title: '',
  content: ''
});

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleString();
};

const fetchNotices = async () => {
  try {
    const response: any = await request.get('/api/notices/list');
    if (response.code === 200) {
      notices.value = response.data;
    }
  } catch (error) {
    console.error('Failed to fetch notices:', error);
  }
};

const openDialog = (notice?: Notice) => {
  if (notice) {
    isEdit.value = true;
    form.noticeId = notice.noticeId;
    form.title = notice.title;
    form.content = notice.content;
  } else {
    isEdit.value = false;
    form.noticeId = undefined;
    form.title = '';
    form.content = '';
  }
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!form.title || !form.content) {
    ElMessage.warning('请填写完整信息');
    return;
  }

  try {
    const url = isEdit.value ? '/api/notices/update' : '/api/notices/add';
    const method = isEdit.value ? 'put' : 'post';
    
    const response: any = await request({
      method,
      url,
      data: form
    });

    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '发布成功');
      dialogVisible.value = false;
      fetchNotices();
    } else {
      ElMessage.error(response.msg || '操作失败');
    }
  } catch (error) {
    console.error('Operation failed:', error);
    ElMessage.error('操作失败');
  }
};

const handleDelete = (notice: Notice) => {
  ElMessageBox.confirm('确定要删除这条公告吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      const response: any = await request.delete(`/api/notices/delete/${notice.noticeId}`);
      if (response.code === 200) {
        ElMessage.success('删除成功');
        fetchNotices();
      } else {
        ElMessage.error(response.msg || '删除失败');
      }
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

onMounted(() => {
  fetchNotices();
});
</script>

<style scoped>
:deep(.el-dialog) {
  border-radius: 24px !important;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}

:deep(.el-dialog__header) {
  margin-right: 0;
  padding: 24px 24px 16px;
  background: white;
}

.dark :deep(.el-dialog__header) {
  background: #1e293b; /* slate-800 */
}

:deep(.el-dialog__title) {
  font-weight: 800;
  color: #0f172a; /* slate-900 */
  letter-spacing: -0.025em;
}

.dark :deep(.el-dialog__title) {
  color: white;
}

:deep(.el-dialog__body) {
  padding: 0 24px 24px;
  background: white;
}

.dark :deep(.el-dialog__body) {
  background: #1e293b;
}

:deep(.el-form-item__label) {
  font-weight: 700;
  font-size: 13px;
  color: #64748b; /* slate-500 */
  margin-bottom: 8px !important;
}

:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
  background-color: #f8fafc !important; /* slate-50 */
  border-radius: 12px !important;
  box-shadow: none !important;
  border: 1px solid #e2e8f0 !important; /* slate-200 */
  transition: all 0.3s;
}

.dark :deep(.el-input__wrapper),
.dark :deep(.el-textarea__inner) {
  background-color: #0f172a !important; /* slate-900 */
  border: 1px solid #334155 !important; /* slate-700 */
  color: #f1f5f9 !important;
}

:deep(.el-input__wrapper.is-focus),
:deep(.el-textarea__inner:focus) {
  border-color: #6366f1 !important; /* indigo-500 */
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1) !important;
}

:deep(.el-dialog__footer) {
  padding: 16px 24px 24px;
  background: #f8fafc;
}

.dark :deep(.el-dialog__footer) {
  background: #1e293b;
  border-top: 1px solid #334155;
}
</style>
