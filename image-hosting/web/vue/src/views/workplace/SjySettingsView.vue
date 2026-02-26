<template>
  <div class="p-6 max-w-4xl mx-auto space-y-6">
    <!-- Header -->
    <div class="flex items-center gap-4 mb-2 animate-in fade-in slide-in-from-top-4 duration-500">
      <div class="p-3 bg-blue-100 dark:bg-blue-900/30 rounded-2xl text-blue-600">
        <el-icon class="text-2xl font-bold"><Setting /></el-icon>
      </div>
      <div>
        <h1 class="text-2xl font-bold text-slate-900 dark:text-white">系统设置</h1>
        <p class="text-sm text-slate-500 dark:text-slate-400">调整系统外观与个性化体验</p>
      </div>
    </div>

    <!-- UI Preferences Card -->
    <div class="bg-white dark:bg-slate-800 rounded-3xl shadow-sm border border-slate-200 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
      <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/10">
        <h2 class="text-base font-bold text-slate-900 dark:text-white">外观与偏好</h2>
      </div>
      <div class="p-6 space-y-6">
        <!-- Theme Setting -->
        <div class="flex items-center justify-between">
          <div class="flex flex-col">
            <span class="text-sm font-bold text-slate-700 dark:text-slate-300">暗黑模式</span>
            <span class="text-xs text-slate-400">切换符合您偏好的界面视觉色调</span>
          </div>
          <el-switch 
            v-model="isDarkMode" 
            @change="toggleDarkMode"
            style="--el-switch-on-color: #4f46e5"
          />
        </div>

        <el-divider class="my-4 opacity-50" />

        <!-- Animation Setting -->
        <div class="flex items-center justify-between">
          <div class="flex flex-col">
            <span class="text-sm font-bold text-slate-700 dark:text-slate-300">界面动画</span>
            <span class="text-xs text-slate-400">开启平滑的视觉过渡动画效果</span>
          </div>
          <el-switch v-model="enableAnimations" style="--el-switch-on-color: #4f46e5" />
        </div>

        <el-divider class="my-4 opacity-50" />

        <!-- Sidebar Setting -->
        <div class="flex items-center justify-between">
          <div class="flex flex-col">
            <span class="text-sm font-bold text-slate-700 dark:text-slate-300">默认折叠侧边栏</span>
            <span class="text-xs text-slate-400">在进入工作台时自动收起左侧导航栏</span>
          </div>
          <el-switch v-model="defaultCollapse" @change="toggleSidebarPref" style="--el-switch-on-color: #4f46e5" />
        </div>
      </div>
    </div>

    <!-- Security & Account Card -->
    <div class="bg-white dark:bg-slate-800 rounded-3xl shadow-sm border border-slate-200 dark:border-slate-700 overflow-hidden animate-in fade-in slide-in-from-bottom-6 duration-600">
      <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-900/10">
        <h2 class="text-base font-bold text-slate-900 dark:text-white">账号与安全</h2>
      </div>
      <div class="p-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
          <div class="p-4 rounded-2xl bg-slate-50 dark:bg-slate-900/50 border border-slate-100 dark:border-slate-700">
            <div class="flex items-center gap-3 mb-2">
              <el-icon class="text-blue-500"><Message /></el-icon>
              <span class="text-xs font-bold text-slate-600 dark:text-slate-400 uppercase">邮箱状态</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium">{{ userInfo?.userEmail || '未绑定' }}</span>
              <el-tag v-if="userInfo?.userEmail" type="success" size="small" round>已验证</el-tag>
            </div>
          </div>

          <div class="p-4 rounded-2xl bg-slate-50 dark:bg-slate-900/50 border border-slate-100 dark:border-slate-700">
            <div class="flex items-center gap-3 mb-2">
              <el-icon class="text-purple-500"><Lock /></el-icon>
              <span class="text-xs font-bold text-slate-600 dark:text-slate-400 uppercase">账号角色</span>
            </div>
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium">{{ userInfo?.userRole === 'admin' ? '系统管理员' : '标准用户' }}</span>
              <span class="text-[10px] text-slate-400 font-mono">ID: {{ userInfo?.userId?.substring(0,8) }}...</span>
            </div>
          </div>
        </div>

        <div class="flex flex-col sm:flex-row gap-3">
          <el-button @click="handleResetPassword" type="primary" plain class="rounded-xl flex-1 h-11">
            修改登录密码
          </el-button>
          <el-button @click="handleLogout" type="danger" plain class="rounded-xl flex-1 h-11">
            退出登录
          </el-button>
        </div>
      </div>
    </div>

    <!-- About Section -->
    <div class="text-center pt-8 opacity-50 hover:opacity-100 transition-opacity">
        <p class="text-xs text-slate-500">Image Hosting Webase v1.2.0-stable</p>
        <p class="text-[10px] text-slate-400 mt-1">Copyright &copy; 2026 Powered by Blockchain Technology</p>
    </div>

    <!-- Password Reset Dialog Mock -->
    <el-dialog v-model="securityDialogVisible" title="安全验证" width="440px" append-to-body class="rounded-3xl">
        <div class="py-4 text-center">
            <el-icon class="text-5xl text-orange-500 mb-4 animate-bounce"><WarningFilled /></el-icon>
            <p class="text-base font-bold text-slate-900 dark:text-white mb-2">即将进行敏感操作</p>
            <p class="text-sm text-slate-500">修改密码需要通过绑定的邮箱验证码进行身份验证。这能确保您的账户始终由您本人掌控。</p>
        </div>
        <template #footer>
          <div class="flex gap-3 px-2 pb-2">
            <el-button @click="securityDialogVisible = false" class="flex-1 h-10 rounded-xl">下次再说</el-button>
            <el-button type="primary" @click="confirmAndGoReset" class="flex-1 h-10 rounded-xl">前往验证</el-button>
          </div>
        </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Setting, 
  Message, 
  Lock, 
  WarningFilled 
} from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()
const userInfo = computed(() => userStore.userInfo)

// UI States
const isDarkMode = ref(document.documentElement.classList.contains('dark'))
const enableAnimations = ref(true)
const defaultCollapse = ref(localStorage.getItem('leftCollapsed') === '1')
const securityDialogVisible = ref(false)

const toggleDarkMode = (val: boolean) => {
  if (val) {
    document.documentElement.classList.add('dark')
    localStorage.setItem('theme', 'dark')
  } else {
    document.documentElement.classList.remove('dark')
    localStorage.setItem('theme', 'light')
  }
}

const toggleSidebarPref = (val: boolean) => {
  localStorage.setItem('leftCollapsed', val ? '1' : '0')
  ElMessage.success(val ? '已开启侧边栏默认折叠' : '已关闭侧边栏默认折叠')
}

const handleResetPassword = () => {
  securityDialogVisible.value = true
}

const confirmAndGoReset = () => {
    securityDialogVisible.value = false
    ElMessage.info('安全模块正在初始化...')
    // 这里未来可以跳转至实际的重置流程
}

const handleLogout = () => {
  ElMessageBox.confirm(
    '您确定要安全退出当前登录会话吗？',
    '确认退出',
    {
      confirmButtonText: '退出登录',
      cancelButtonText: '取消',
      confirmButtonClass: 'el-button--danger',
      type: 'warning',
      center: true
    }
  ).then(() => {
    localStorage.removeItem('token')
    userStore.$reset()
    router.push('/login')
    ElMessage.success('已清空会话并安全退出')
  }).catch(() => {})
}
</script>

<style scoped>
.rounded-3xl {
  border-radius: 1.5rem !important;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideInFromBottom {
  from { transform: translateY(1rem); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes slideInFromTop {
  from { transform: translateY(-1rem); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

.animate-in {
  animation-fill-mode: both;
}

.fade-in { animation: fadeIn 0.5s ease-out; }
.slide-in-from-bottom-4 { animation: slideInFromBottom 0.5s ease-out; }
.slide-in-from-bottom-6 { animation: slideInFromBottom 0.7s ease-out; }
.slide-in-from-top-4 { animation: slideInFromTop 0.5s ease-out; }
</style>
