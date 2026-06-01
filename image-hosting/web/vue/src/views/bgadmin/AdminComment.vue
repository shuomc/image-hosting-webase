<template>
  <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Toolbar -->
    <div class="p-6 border-b border-slate-100 dark:border-slate-700 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div class="relative max-w-sm w-full">
        <input 
          v-model="queryParams.keyword"
          @keyup.enter="handleQuery"
          type="text" 
          placeholder="搜索评论内容或用户名..." 
          class="w-full pl-10 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
        />
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-slate-400 absolute left-3 top-2.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
      <div class="flex gap-2">
        <button 
          @click="handleQuery"
          class="px-4 py-2 bg-slate-100 hover:bg-slate-200 dark:bg-slate-700 dark:hover:bg-slate-600 text-slate-700 dark:text-slate-200 rounded-xl text-sm font-medium transition-colors flex items-center"
        >
          刷新
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="bg-slate-50 dark:bg-slate-900/50 text-slate-500 dark:text-slate-400 text-xs uppercase tracking-wider">
            <th class="px-6 py-4 font-semibold">评论用户</th>
            <th class="px-6 py-4 font-semibold">内容</th>
            <th class="px-6 py-4 font-semibold">图片ID</th>
            <th class="px-6 py-4 font-semibold">发布时间</th>
            <th class="px-6 py-4 font-semibold text-right">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 dark:divide-slate-700">
          <tr v-for="comment in commentList" :key="comment.commentId" class="hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors text-sm">
            <td class="px-6 py-4">
              <div class="flex items-center">
                <div class="h-8 w-8 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center text-xs font-bold text-slate-600 dark:text-slate-300">
                  {{ comment.userName.charAt(0).toUpperCase() }}
                </div>
                <div class="ml-3">
                  <div class="font-medium text-slate-900 dark:text-white">{{ comment.userName }}</div>
                  <div class="text-xs text-slate-500 dark:text-slate-400">{{ comment.userId }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 max-w-xs truncate">
              <span class="text-slate-700 dark:text-slate-300">{{ comment.content }}</span>
            </td>
            <td class="px-6 py-4 font-mono text-xs text-slate-500">
              {{ comment.imageId }}
            </td>
            <td class="px-6 py-4 text-slate-500 dark:text-slate-400">
              {{ comment.createTime }}
            </td>
            <td class="px-6 py-4 text-right">
              <div class="flex justify-end gap-2">
                <button 
                  @click="handleUpdate(comment)"
                  class="p-2 text-indigo-600 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 rounded-lg transition-colors"
                  title="修改"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
                <button 
                  @click="handleDelete(comment)"
                  class="p-2 text-rose-600 hover:bg-rose-50 dark:hover:bg-rose-900/20 rounded-lg transition-colors"
                  title="删除"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="commentList.length === 0">
            <td colspan="5" class="px-6 py-10 text-center text-slate-500 dark:text-slate-400">
              暂无评论数据
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="p-6 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between">
      <div class="text-sm text-slate-500 dark:text-slate-400">
        共 {{ total }} 条评论
      </div>
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="getList"
      />
    </div>

    <!-- Edit Dialog -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑评论"
      width="500px"
      class="custom-dialog"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="评论内容">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            rows="4"
            placeholder="请输入评论内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listComments, updateComment, deleteComment, type CommentVO, type CommentQuery } from '@/api/admin/comment'

const commentList = ref<CommentVO[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)

const queryParams = reactive<CommentQuery>({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  commentId: '',
  content: ''
})

const getList = async () => {
  loading.value = true
  try {
    const res: any = await listComments(queryParams)
    if (res.code === 200) {
      commentList.value = res.rows || []
      total.value = res.total || 0
    } else {
      ElMessage.error(res.msg || '获取列表失败')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const handleUpdate = (comment: CommentVO) => {
  form.commentId = comment.commentId
  form.content = comment.content
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!form.content.trim()) {
    return ElMessage.warning('内容不能为空')
  }
  try {
    const res: any = await updateComment(form)
    if (res.code === 200) {
      ElMessage.success('修改成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = (comment: CommentVO) => {
  ElMessageBox.confirm(
    '确定要永久删除这条评论吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      const res: any = await deleteComment(comment.commentId)
      if (res.code === 200) {
        ElMessage.success('操作成功')
        getList()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    } catch (error) {
      console.error(error)
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.custom-dialog :deep(.el-dialog) {
  border-radius: 1rem;
  overflow: hidden;
}

.custom-dialog :deep(.el-dialog__header) {
  margin-right: 0;
  padding: 1.25rem 1.5rem;
  border-bottom: 1px solid #f1f5f9;
}

.custom-dialog :deep(.el-dialog__title) {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1e293b;
}

.custom-dialog :deep(.el-dialog__footer) {
  padding: 1rem 1.5rem 1.25rem;
  border-top: 1px solid #f1f5f9;
}

.dark .custom-dialog :deep(.el-dialog) {
  background-color: #1e293b;
}

.dark .custom-dialog :deep(.el-dialog__header),
.dark .custom-dialog :deep(.el-dialog__footer) {
  border-color: #334155;
}

.dark .custom-dialog :deep(.el-dialog__title) {
  color: #f8fafc;
}
</style>
