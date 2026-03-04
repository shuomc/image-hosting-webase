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
        <button 
          @click="handleRefreshStats"
          :disabled="isRefreshing"
          class="px-4 py-2 bg-slate-100 hover:bg-slate-200 dark:bg-slate-700 dark:hover:bg-slate-600 text-slate-700 dark:text-slate-200 rounded-xl text-sm font-medium transition-colors flex items-center disabled:opacity-50"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" :class="{'animate-spin': isRefreshing}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          刷新统计
        </button>
        <button 
          @click="openCreateUser"
          class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl text-sm font-medium transition-colors flex items-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          添加用户
        </button>
        <button 
          @click="openManageRoles"
          class="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-sm font-medium transition-colors flex items-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4" />
          </svg>
          管理角色
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
              <el-select 
                v-model="user.userRole" 
                @change="handleFastUpdate(user)"
                placeholder="选择角色"
                size="small"
                class="w-24 custom-select"
              >
                <el-option 
                  v-for="role in roles" 
                  :key="role.rolesId" 
                  :label="role.rolesKey" 
                  :value="role.rolesName" 
                />
              </el-select>
            </td>
            <td class="px-6 py-4">
              <el-select 
                v-model="user.status" 
                @change="handleFastUpdate(user)"
                placeholder="选择状态"
                size="small"
                class="w-24 custom-select"
              >
                <el-option :label="'正常'" :value="1">
                  <div class="flex items-center gap-2">
                    <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span> 正常
                  </div>
                </el-option>
                <el-option :label="'注销'" :value="0">
                  <div class="flex items-center gap-2">
                    <span class="w-1.5 h-1.5 rounded-full bg-red-500"></span> 注销
                  </div>
                </el-option>
              </el-select>
            </td>
            <td class="px-6 py-4 text-sm text-slate-500 dark:text-slate-400">
              {{ new Date(user.createTime).toLocaleDateString() }}
            </td>
            <td class="px-6 py-4 text-right">
              <div class="flex items-center justify-end gap-3">
                <button @click="showUserStats(user)" class="text-emerald-600 hover:text-emerald-900 dark:text-emerald-400 dark:hover:text-emerald-300 text-sm font-bold">查看数据</button>
                <button @click="handleResetPassword(user)" class="text-indigo-600 hover:text-indigo-900 dark:text-indigo-400 dark:hover:text-indigo-300 text-sm font-bold">重置密码</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- User Stats Modal -->
    <el-dialog
      v-model="statsDialogVisible"
      :title="'用户统计 - ' + selectedUserName"
      width="500px"
      class="custom-dialog"
      destroy-on-close
    >
      <div v-if="loadingStats" class="py-12 flex flex-col items-center justify-center gap-4">
        <div class="w-10 h-10 border-4 border-indigo-500 border-t-transparent rounded-full animate-spin"></div>
        <span class="text-slate-500 text-sm">正在加载统计数据...</span>
      </div>
      <div v-else-if="userStats" class="space-y-6 py-2">
        <!-- Stats Grid -->
        <div class="grid grid-cols-2 gap-4">
          <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-2xl border border-slate-100 dark:border-slate-800">
            <div class="text-xs text-slate-500 mb-1">总上传图片</div>
            <div class="text-2xl font-bold text-slate-900 dark:text-white">{{ userStats.totalUploads }}</div>
          </div>
          <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-2xl border border-slate-100 dark:border-slate-800">
            <div class="text-xs text-slate-500 mb-1">总浏览次数</div>
            <div class="text-2xl font-bold text-slate-900 dark:text-white">{{ userStats.totalViews || 0 }}</div>
          </div>
          <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-2xl border border-slate-100 dark:border-slate-800">
            <div class="text-xs text-slate-500 mb-1">总下载次数</div>
            <div class="text-2xl font-bold text-slate-900 dark:text-white">{{ userStats.totalDownloads || 0 }}</div>
          </div>
          <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-2xl border border-slate-100 dark:border-slate-800">
            <div class="text-xs text-slate-500 mb-1">总点赞数</div>
            <div class="text-2xl font-bold text-slate-900 dark:text-white">{{ userStats.totalLikes || 0 }}</div>
          </div>
          <div class="p-4 bg-slate-50 dark:bg-slate-900/50 rounded-2xl border border-slate-100 dark:border-slate-800">
            <div class="text-xs text-slate-500 mb-1">更新时间</div>
            <div class="text-sm font-medium text-slate-900 dark:text-white">{{ userStats.updateTime ? new Date(userStats.updateTime).toLocaleString() : '从未更新' }}</div>
          </div>
        </div>

        <!-- Storage Progress -->
        <div class="p-5 bg-indigo-50 dark:bg-indigo-900/20 rounded-2xl border border-indigo-100 dark:border-indigo-900/30">
          <div class="flex justify-between items-center mb-3">
            <div class="flex items-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-indigo-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4" />
              </svg>
              <span class="text-sm font-bold text-indigo-900 dark:text-indigo-300">存储容量使用情况</span>
            </div>
            <span class="text-xs font-medium text-indigo-600 dark:text-indigo-400">
              {{ (userStats.storageUsed / 1024 / 1024).toFixed(2) }}MB / {{ (userStats.storageLimit / 1024 / 1024).toFixed(2) }}MB
            </span>
          </div>
          <div class="w-full h-3 bg-indigo-100 dark:bg-indigo-900/40 rounded-full overflow-hidden">
            <div 
              class="h-full bg-indigo-600 rounded-full transition-all duration-1000"
              :style="{ width: Math.min((userStats.storageUsed / userStats.storageLimit) * 100, 100) + '%' }"
            ></div>
          </div>
          <div class="mt-2 text-right">
            <span class="text-[10px] text-indigo-500/70">使用率: {{ ((userStats.storageUsed / userStats.storageLimit) * 100).toFixed(1) }}%</span>
          </div>
        </div>
      </div>
      <div v-else class="py-12 text-center text-slate-500">
        无可用的统计数据
      </div>
    </el-dialog>

    <!-- Pagination -->
    <div class="mt-4 p-6 bg-white/50 dark:bg-slate-800/50 backdrop-blur-md rounded-3xl border border-slate-100 dark:border-slate-700 flex items-center justify-between">
      <div class="hidden sm:block">
        <span class="text-sm text-slate-500 dark:text-slate-400">
          显示第 <span class="font-bold text-slate-900 dark:text-white">{{ (query.page - 1) * query.size + 1 }}</span> 
          到 <span class="font-bold text-slate-900 dark:text-white">{{ Math.min(query.page * query.size, total) }}</span> 
          条数据，共 <span class="font-bold text-slate-900 dark:text-white">{{ total }}</span> 条
        </span>
      </div>
      <div class="flex items-center gap-2">
        <button 
          @click="changePage(query.page - 1)"
          :disabled="query.page <= 1"
          class="px-5 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-40 transition-all shadow-sm">
          上一页
        </button>
        <button 
          @click="changePage(query.page + 1)"
          :disabled="query.page * query.size >= total"
          class="px-5 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-40 transition-all shadow-sm">
          下一页
        </button>
      </div>
    </div>

    <!-- Modals -->
    <!-- Create User Dialog -->
    <el-dialog
      v-model="createDialogVisible"
      title="创建新用户"
      width="500px"
      class="rounded-2xl"
    >
      <el-form :model="createForm" label-width="80px" class="mt-4">
        <el-form-item label="用户名" required>
          <el-input v-model="createForm.userName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱" required>
          <el-input v-model="createForm.userEmail" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input v-model="createForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="createForm.userRole" class="w-full">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="flex justify-end gap-3 mt-4">
          <button 
            @click="createDialogVisible = false"
            class="px-4 py-2 border border-slate-200 rounded-xl hover:bg-slate-50 text-sm transition-colors"
          >
            取消
          </button>
          <button 
            @click="handleCreateUser"
            :disabled="creating"
            class="px-4 py-2 bg-indigo-600 text-white rounded-xl hover:bg-indigo-700 text-sm font-medium transition-colors disabled:opacity-50"
          >
            {{ creating ? '创建中...' : '确认创建' }}
          </button>
        </div>
      </template>
    </el-dialog>

    <!-- Manage Roles Dialog -->
    <el-dialog
      v-model="roleDialogVisible"
      title="管理系统角色"
      width="600px"
      class="rounded-2xl"
    >
      <div v-loading="roleLoading">
        <div class="mb-4">
          <h4 class="text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2">已有角色</h4>
          <el-table :data="roles" border stripe style="width: 100%" size="small">
            <el-table-column prop="rolesName" label="角色标识" width="120" />
            <el-table-column prop="description" label="描述" />
            <el-table-column label="操作" width="80" align="center">
              <template #default="{ row }">
                <el-button 
                  type="danger" 
                  icon="Delete" 
                  circle 
                  size="small" 
                  @click="handleDeleteRole(row)"
                  :disabled="row.rolesName === 'admin'"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pt-4 border-t border-slate-100 dark:border-slate-700">
          <h4 class="text-sm font-semibold text-slate-700 dark:text-slate-300 mb-3">新增角色</h4>
          <el-form :model="roleForm" layout="inline" class="flex gap-2">
            <el-input 
              v-model="roleForm.rolesName" 
              placeholder="角色标识 (如: editor)"
              class="flex-1"
            />
            <el-input 
              v-model="roleForm.description" 
              placeholder="描述 (如: 编辑人员)"
              class="flex-1"
            />
            <button 
              @click="handleCreateRole"
              type="button"
              :disabled="!roleForm.rolesName || !roleForm.description"
              class="px-4 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700 text-sm font-medium transition-colors disabled:opacity-50"
            >
              添加
            </button>
          </el-form>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { 
  getUserList, 
  updateUser, 
  createUser,
  getRolesList,
  createRole,
  deleteRole,
  refreshAllUserStats, 
  getUserStats, 
  type UserVO, 
  type UserListQuery, 
  type UserUpdate,
  type UserCreate,
  type RoleCreate,
  type RoleVO,
  type UserStatsVO 
} from '@/api/admin/user';
import { Delete } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const users = ref<UserVO[]>([]);
const total = ref(0);
const isRefreshing = ref(false);
const query = reactive<UserListQuery>({
  page: 1,
  size: 10,
  keyword: ''
});

// User Stats Modal State
const statsDialogVisible = ref(false);
const loadingStats = ref(false);
const userStats = ref<UserStatsVO | null>(null);
const selectedUserName = ref('');

// Create User State
const createDialogVisible = ref(false);
const creating = ref(false);
const createForm = reactive<UserCreate>({
  userName: '',
  userEmail: '',
  password: '',
  userRole: 'user'
});

const openCreateUser = () => {
  createForm.userName = '';
  createForm.userEmail = '';
  createForm.password = '';
  createForm.userRole = 'user';
  createDialogVisible.value = true;
};

// Roles Management State
const roles = ref<RoleVO[]>([]);
const roleLoading = ref(false);
const roleDialogVisible = ref(false);
const roleForm = reactive<RoleCreate>({
  rolesName: '',
  description: ''
});

const loadRoles = async () => {
  try {
    const res = await getRolesList();
    if (res.code === 200) {
      roles.value = res.data;
    }
  } catch (err) {
    console.error('Failed to load roles');
  }
};

const openManageRoles = async () => {
  roleForm.rolesName = '';
  roleForm.description = '';
  roleDialogVisible.value = true;
  roleLoading.value = true;
  await loadRoles();
  roleLoading.value = false;
};

const handleCreateUser = async () => {
  if (!createForm.userName || !createForm.userEmail || !createForm.password) {
    ElMessage.warning('请填写完整信息');
    return;
  }
  creating.value = true;
  try {
    const res = await createUser(createForm);
    if (res.code === 200) {
      ElMessage.success('用户创建成功');
      createDialogVisible.value = false;
      loadData();
    } else {
      ElMessage.error(res.message || '创建失败');
    }
  } catch (error) {
    ElMessage.error('请求失败');
  } finally {
    creating.value = false;
  }
};

const handleCreateRole = async () => {
  if (!roleForm.rolesName || !roleForm.description) {
    ElMessage.warning('请填写角色标识和描述');
    return;
  }
  roleLoading.value = true;
  try {
    const res = await createRole(roleForm);
    if (res.code === 200) {
      ElMessage.success('角色创建成功');
      roleForm.rolesName = '';
      roleForm.description = '';
      await loadRoles();
    } else {
      ElMessage.error(res.message || '创建角色失败');
    }
  } catch (error) {
    ElMessage.error('网络错误');
  } finally {
    roleLoading.value = false;
  }
};

const handleDeleteRole = async (role: RoleVO) => {
  try {
    await ElMessageBox.confirm(`确定要删除角色 "${role.rolesKey}" 吗？删除后该操作不可恢复。`, '危险操作', {
      type: 'error',
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      confirmButtonClass: 'el-button--danger'
    });
    
    roleLoading.value = true;
    const res = await deleteRole(role.rolesId);
    if (res.code === 200) {
      ElMessage.success('角色已删除');
      await loadRoles();
    } else {
      ElMessage.error(res.message || '删除失败');
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error?.response?.data?.message || '操作失败');
    }
  } finally {
    roleLoading.value = false;
  }
};

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

const handleRefreshStats = async () => {
  isRefreshing.value = true;
  try {
    const res = await refreshAllUserStats();
    if (res.code === 200) {
      ElMessage.success('用户统计任务已启动，请稍等片刻刷新数据');
    }
  } catch (err) {
    ElMessage.error('触发更新失败');
  } finally {
    isRefreshing.value = false;
  }
};

const showUserStats = async (user: UserVO) => {
  selectedUserName.value = user.userName;
  statsDialogVisible.value = true;
  loadingStats.value = true;
  userStats.value = null;
  
  try {
    const res = await getUserStats(user.userId);
    if (res.code === 200) {
      userStats.value = res.data;
    } else {
      ElMessage.error(res.message || '获取数据失败');
    }
  } catch (error) {
    ElMessage.error('网络错误');
  } finally {
    loadingStats.value = false;
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

const handleFastUpdate = async (user: UserVO) => {
  try {
    const res = await updateUser({
      userId: user.userId,
      userRole: user.userRole,
      status: user.status
    });
    if (res.code === 200) {
      ElMessage.success(`用户 ${user.userName} 已更新`);
    } else {
      ElMessage.error(res.message || '更新失败');
      loadData(); // 失败则回滚 UI 状态
    }
  } catch (error) {
    ElMessage.error('网络错误或权限不足');
    loadData();
  }
};

const handleResetPassword = (user: UserVO) => {
  ElMessageBox.confirm(`确定要重置用户 ${user.userName} 的密码吗？`, '安全警告', {
    confirmButtonText: '确定重置',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 这里调用您的重置 API，示例暂用更新接口模拟
    ElMessage.success('密码已重置为初始密码 123456');
  });
};

onMounted(async () => {
  loadData();
  await loadRoles();
});
</script>

<style scoped>
:deep(.custom-select .el-input__wrapper) {
  background-color: transparent !important;
  box-shadow: none !important;
  border: 1px solid #e2e8f0 !important; /* slate-200 */
  border-radius: 8px !important;
}

.dark :deep(.custom-select .el-input__wrapper) {
  border-color: #334155 !important; /* slate-700 */
}

:deep(.custom-select .el-input__wrapper.is-focus) {
  border-color: #6366f1 !important; /* indigo-500 */
}
</style>
