<template>
  <div class="min-h-screen flex items-center justify-center bg-slate-100 dark:bg-slate-900 relative overflow-hidden">
    <!-- Background Decorations -->
    <div class="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none z-0">
      <div class="absolute top-[-10%] right-[-10%] w-[50%] h-[50%] rounded-full bg-blue-500/10 blur-3xl animate-pulse"></div>
      <div class="absolute bottom-[-10%] left-[-10%] w-[50%] h-[50%] rounded-full bg-indigo-500/10 blur-3xl animate-pulse delay-1000"></div>
    </div>

    <div class="w-full max-w-[520px] relative z-10 px-4">
      <div class="w-full p-8 bg-white/80 dark:bg-slate-800/80 backdrop-blur-xl rounded-2xl shadow-2xl border border-white/50 dark:border-slate-700/50">
        
        <div class="mb-8 text-center">
          <h3 class="text-2xl font-bold text-slate-800 dark:text-white">管理员登录</h3>
          <p class="text-sm text-slate-500 dark:text-slate-400 mt-2">Image Hosting System Admin Panel</p>
        </div>

        <div class="mb-5 w-full group">
          <label for="userName" class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1">账号</label>
          <input 
            id="userName"
            type="text" 
            v-model="loginData.userName"
            class="w-full h-11 px-4 rounded-xl border border-slate-200 dark:border-slate-600 bg-white/50 dark:bg-slate-900/50 text-slate-700 dark:text-slate-200 text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all hover:border-blue-300 placeholder-slate-400"
            placeholder="请输入管理员账号" 
          />
        </div>

        <div class="mb-5 w-full">
          <div class="flex justify-between items-center mb-2 ml-1">
            <label for="password" class="block text-sm font-semibold text-slate-700 dark:text-slate-300">密码</label>
          </div>
          <input 
            id="password"
            type="password" 
            v-model="loginData.password"
            class="w-full h-11 px-4 rounded-xl border border-slate-200 dark:border-slate-600 bg-white/50 dark:bg-slate-900/50 text-slate-700 dark:text-slate-200 text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all hover:border-blue-300 placeholder-slate-400"
            placeholder="请输入密码" 
            @keyup.enter="handleLogin()"
          />
        </div>

        <div class="mb-6">
          <label for="captcha" class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1">验证码</label>
          <div class="flex items-stretch gap-3">
            <input 
              id="captcha"
              type="text" 
              v-model="loginData.captcha"
              class="flex-1 h-11 px-4 rounded-xl border border-slate-200 dark:border-slate-600 bg-white/50 dark:bg-slate-900/50 text-slate-700 dark:text-slate-200 text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all hover:border-blue-300 placeholder-slate-400"
              placeholder="验证码" 
              @keyup.enter="handleLogin()"
            />
            
            <div 
              class="w-36 h-11 rounded-xl overflow-hidden cursor-pointer border border-slate-200 dark:border-slate-600 shadow-sm hover:shadow-md transition-all"
              @click="getGraghCaptcha"
              title="点击刷新验证码"
            >
              <img 
                v-if="loginData.captchaImage" 
                :src="loginData.captchaImage" 
                alt="验证码" 
                class="w-full h-full object-cover"
              />
              <div v-else class="w-full h-full bg-slate-100 dark:bg-slate-700 flex items-center justify-center text-xs text-slate-400">
                加载中...
              </div>
            </div>
          </div>
        </div>

        <div class="space-y-4">
          <button 
            class="w-full h-11 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-500 hover:to-indigo-500 text-white font-bold rounded-xl shadow-lg shadow-blue-500/30 transition-all transform hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-70 disabled:cursor-not-allowed"
            :disabled="loading"
            @click="handleLogin()"
          >
            <span v-if="loading" class="flex items-center justify-center">
              <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              登录中...
            </span>
            <span v-else>立即登录</span>
          </button>
          
          <div class="flex justify-center">
             <router-link to="/login" class="text-sm text-slate-500 hover:text-blue-600 transition-colors">
               返回普通用户登录
             </router-link>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { onMounted, reactive, ref } from 'vue';
import { useUserStore } from '@/stores/user';
import { API_BASE_URL } from '@/config';
import { ElMessage } from 'element-plus';
import { adminLogin } from '@/api/auth/login';
import axios from 'axios';

const router = useRouter();
const userStore = useUserStore();
const loading = ref(false);

const loginData = reactive({
  userName: "",
  password: "",
  captcha: "",
  codeKey: "",
  captchaImage: ""
});

async function getGraghCaptcha() {
  const apiUrl = `${API_BASE_URL}/auth/getValidateCode`;
  try {
    const response = await axios.get(apiUrl);
    if (response.data.code === 200) {
      loginData.codeKey = response.data.data.codeKey;
      loginData.captchaImage = response.data.data.codeValue;
    }
  } catch (error) {
    console.error("获取验证码失败:", error);
    ElMessage.error("获取验证码失败");
  }
}

async function handleLogin() {
  if (!loginData.userName || !loginData.password || !loginData.captcha) {
    ElMessage.warning('请填写完整的登录信息');
    return;
  }
  
  loading.value = true;
  try {
    const loginResponse = await adminLogin(loginData);
    userStore.setLoginInfo({ token: loginResponse.token });
    await userStore.loadUserInfo();
    
    // Check role again on frontend just in case, though backend enforces it
    if (userStore.userInfo?.userRole !== 'admin') {
       ElMessage.error('非管理员账号');
       userStore.clearLoginInfo();
       return;
    }

    ElMessage.success('管理员登录成功');
    router.push('/admin/dashboard');
  } catch (error: any) {
    console.error('登录失败:', error);
    // Error message is usually handled by interceptor or the api function wrapper, 
    // but if it bubbles up:
    if (error.message) ElMessage.error(error.message);
    getGraghCaptcha(); // Refresh captcha
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  getGraghCaptcha();
});
</script>
