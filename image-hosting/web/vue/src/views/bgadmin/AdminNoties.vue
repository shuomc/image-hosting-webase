<template>
  <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Toolbar -->
    <div class="p-6 border-b border-slate-100 dark:border-slate-700 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <h2 class="text-lg font-bold text-slate-900 dark:text-white">公告管理</h2>
      <div class="flex gap-2">
        <button @click="openDialog()" class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-medium transition-colors flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          发布公告
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="bg-slate-50 dark:bg-slate-900/50 text-slate-500 dark:text-slate-400 text-xs uppercase tracking-wider">
            <th class="px-6 py-4 font-semibold">标题</th>
            <th class="px-6 py-4 font-semibold">内容摘要</th>
            <th class="px-6 py-4 font-semibold">发布时间</th>
            <th class="px-6 py-4 font-semibold text-right">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 dark:divide-slate-700">
          <tr v-if="notices.length === 0">
            <td colspan="4" class="px-6 py-8 text-center text-slate-500 dark:text-slate-400">暂无公告</td>
          </tr>
          <tr v-for="notice in notices" :key="notice.noticeId" class="hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors">
            <td class="px-6 py-4">
              <div class="text-sm font-medium text-slate-900 dark:text-white">{{ notice.title }}</div>
            </td>
            <td class="px-6 py-4">
              <div class="text-sm text-slate-500 dark:text-slate-400 max-w-xs truncate">{{ notice.content }}</div>
            </td>
            <td class="px-6 py-4">
              <div class="text-sm text-slate-500 dark:text-slate-400">{{ formatDate(notice.createTime) }}</div>
            </td>
            <td class="px-6 py-4 text-right">
              <button @click="openDialog(notice)" class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 font-medium text-sm mr-3">编辑</button>
              <button @click="handleDelete(notice)" class="text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300 font-medium text-sm">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑公告' : '发布公告'"
      width="500px"
      destroy-on-close
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入公告内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
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
