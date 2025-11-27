<template>
  <div class="w-full max-w-[420px] relative z-10">
    <div class="w-full p-8 bg-white/70 backdrop-blur-xl rounded-2xl shadow-[0_8px_30px_rgb(0,0,0,0.12)] border border-white/50">
      
      <div class="mb-8 text-center">
        <h3 class="text-xl font-bold text-gray-800">欢迎回来</h3>
        <p class="text-sm text-gray-500 mt-2">请输入您的账号信息进行登录</p>
      </div>

      <div class="mb-5 w-full group">
        <label for="userName" class="block text-sm font-semibold text-gray-700 mb-2 ml-1">用户名</label>
        <input 
          id="userName"
          type="text" 
          v-model="loginData.userName"
          class="w-full h-11 px-4 rounded-xl border border-gray-200 bg-white/50 text-gray-700 text-sm
                 transition-all duration-300 ease-in-out outline-none 
                 focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10
                 hover:border-indigo-300 placeholder-gray-400"
          placeholder="请输入用户名或邮箱" 
        />
      </div>

      <div class="mb-5 w-full">
        <div class="flex justify-between items-center mb-2 ml-1">
          <label for="password" class="block text-sm font-semibold text-gray-700">密码</label>
        </div>
        <input 
          id="password"
          type="password" 
          v-model="loginData.password"
          class="w-full h-11 px-4 rounded-xl border border-gray-200 bg-white/50 text-gray-700 text-sm
                 transition-all duration-300 ease-in-out outline-none 
                 focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10
                 hover:border-indigo-300 placeholder-gray-400"
          placeholder="请输入密码" 
        />
      </div>

      <div class="mb-6">
        <label for="captcha" class="block text-sm font-semibold text-gray-700 mb-2 ml-1">验证码</label>
        <div class="flex items-stretch gap-3">
          <input 
            id="captcha"
            type="text" 
            v-model="loginData.captcha"
            class="flex-1 h-11 px-4 rounded-xl border border-gray-200 bg-white/50 text-gray-700 text-sm
                   transition-all duration-300 ease-in-out outline-none 
                   focus:bg-white focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10
                   hover:border-indigo-300 placeholder-gray-400"
            placeholder="验证码" 
            @keyup.enter="handleLogin()"
          />
          
          <div 
            class="h-11 w-52 cursor-pointer overflow-hidden rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-all duration-300 hover:-translate-y-0.5 bg-white flex items-center justify-center"
            @click="getGraghCaptcha()"
            title="点击刷新验证码"
          >
            <img 
              v-if="loginData.captchaImage" 
              :src="loginData.captchaImage" 
              alt="验证码" 
              class="w-full h-full object-cover" 
            />
            <span v-else class="text-xs text-gray-400">加载中...</span>
          </div>
        </div>
      </div>

      <div class="space-y-4">
        <button 
          class="w-full h-11 rounded-xl bg-indigo-600 text-white font-semibold shadow-lg shadow-indigo-500/30 
                 hover:bg-indigo-700 hover:shadow-indigo-500/50 hover:-translate-y-0.5
                 active:translate-y-0 active:shadow-none
                 transition-all duration-300 ease-in-out tracking-wide" 
          @click="handleLogin()"
        >
          登 录
        </button>
        
        <div class="flex justify-center">
           <router-link to="/auth/find-password" class="text-sm text-indigo-600 hover:text-indigo-800 font-medium transition-colors duration-300">
             忘记密码？
           </router-link>
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { onMounted, reactive } from 'vue';
import { useUserStore } from '@/stores/user';
import { API_BASE_URL } from '@/config';
import { ElMessage } from 'element-plus';
import { login } from '@/api/auth/login';
import axios from 'axios';

const router = useRouter();
const loginData = reactive({
  userName: "",
  password: "",
  captcha: "",
  codeKey: "",
  captchaImage: ""
});

const userStore = useUserStore();

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
  try {
    const loginResponse = await login(loginData);
    userStore.setLoginInfo({ token: loginResponse.token });
    await userStore.loadUserInfo();
    router.push('/workplace');
  } catch (error: any) {
    console.error('登录失败:', error);
    getGraghCaptcha(); // 失败后刷新验证码
  }
}

onMounted(() => {
  getGraghCaptcha();
  if (userStore.isLoggedIn) {
    router.push('/workplace');
  }
});
</script>

<style lang="scss" scoped>
/* 保持基本重置，其他都用 Tailwind 处理 */
</style>