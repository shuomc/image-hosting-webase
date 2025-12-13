<template>
  <div class="bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Toolbar -->
    <div class="p-6 border-b border-slate-100 dark:border-slate-700 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div class="relative max-w-sm w-full">
        <input 
          v-model="query.keyword"
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="搜索用户..." 
          class="w-full pl-10 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all"
        />
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-slate-400 absolute left-3 top-2.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
      <div class="flex gap-2">
        <button class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-medium transition-colors flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          添加用户
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="bg-slate-50 dark:bg-slate-900/50 text-slate-500 dark:text-slate-400 text-xs uppercase tracking-wider">
            <th class="px-6 py-4 font-semibold">用户</th>
            <th class="px-6 py-4 font-semibold">角色</th>
            <th class="px-6 py-4 font-semibold">状态</th>
            <th class="px-6 py-4 font-semibold">注册时间</th>
            <th class="px-6 py-4 font-semibold text-right">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 dark:divide-slate-700">
          <tr v-for="user in users" :key="user.userId" class="hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors">
            <td class="px-6 py-4">
              <div class="flex items-center">
                <div class="h-10 w-10 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 text-white flex items-center justify-center text-sm font-bold shadow-sm">
                  {{ user.userName ? user.userName.charAt(0).toUpperCase() : '?' }}
                </div>
                <div class="ml-4">
                  <div class="text-sm font-medium text-slate-900 dark:text-white">{{ user.userName }}</div>
                  <div class="text-xs text-slate-500 dark:text-slate-400">{{ user.userEmail }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4">
              <span class="px-2 py-1 text-xs font-semibold rounded-full bg-blue-50 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400 border border-blue-100 dark:border-blue-800">
                {{ user.userRole }}
              </span>
            </td>
            <td class="px-6 py-4">
              <span v-if="user.status === 1" class="px-2 py-1 text-xs font-semibold rounded-full bg-green-50 text-green-600 dark:bg-green-900/20 dark:text-green-400 border border-green-100 dark:border-green-800 flex w-fit items-center gap-1">
                <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span> Active
              </span>
              <span v-else class="px-2 py-1 text-xs font-semibold rounded-full bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-400 border border-slate-200 dark:border-slate-600">
                Inactive
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-slate-500 dark:text-slate-400">
              {{ new Date(user.createTime).toLocaleDateString() }}
            </td>
            <td class="px-6 py-4 text-right">
              <button @click="handleEdit(user)" class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 text-sm font-medium mr-3">编辑</button>
              <button @click="handleDisable(user)" class="text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300 text-sm font-medium">禁用</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="p-4 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between">
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

    <!-- Edit Modal -->
    <div v-if="showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 w-full max-w-md">
        <h3 class="text-lg font-bold mb-4 text-slate-900 dark:text-white">编辑用户</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">角色</label>
            <select v-model="editForm.userRole" class="w-full rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2">
              <option value="user">User</option>
              <option value="admin">Admin</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">状态</label>
            <select v-model="editForm.status" class="w-full rounded-lg border border-slate-300 dark:border-slate-600 bg-white dark:bg-slate-700 px-3 py-2">
              <option :value="1">Active</option>
              <option :value="0">Inactive</option>
            </select>
          </div>
        </div>
        <div class="mt-6 flex justify-end gap-3">
          <button @click="showEditModal = false" class="px-4 py-2 rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">取消</button>
          <button @click="submitEdit" class="px-4 py-2 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { getUserList, updateUser, type UserVO, type UserListQuery, type UserUpdate } from '@/api/admin/user';
import { ElMessage } from 'element-plus';

const users = ref<UserVO[]>([]);
const total = ref(0);
const query = reactive<UserListQuery>({
  page: 1,
  size: 10,
  keyword: ''
});

const showEditModal = ref(false);
const editForm = reactive<UserUpdate>({
  userId: '',
  userRole: '',
  status: 1
});

const loadData = async () => {
  try {
    const res = await getUserList(query);
    if (res.code === 200) {
      users.value = res.data.list;
      total.value = res.data.total;
    }
  } catch (error) {
    console.error('Failed to load users', error);
  }
};

const handleSearch = () => {
  query.page = 1;
  loadData();
};

const changePage = (newPage: number) => {
  query.page = newPage;
  loadData();
};

const handleEdit = (user: UserVO) => {
  editForm.userId = user.userId;
  editForm.userRole = user.userRole;
  editForm.status = user.status;
  showEditModal.value = true;
};

const submitEdit = async () => {
  try {
    const res = await updateUser(editForm);
    if (res.code === 200) {
      ElMessage.success('更新成功');
      showEditModal.value = false;
      loadData();
    } else {
      ElMessage.error(res.message || '更新失败');
    }
  } catch (error) {
    ElMessage.error('更新失败');
  }
};

const handleDisable = async (user: UserVO) => {
  try {
    await updateUser({
      userId: user.userId,
      status: 0
    });
    ElMessage.success('用户已禁用');
    loadData();
  } catch (error) {
    ElMessage.error('操作失败');
  }
};


onMounted(() => {
  loadData();
});
</script>
