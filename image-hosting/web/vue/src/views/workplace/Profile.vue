<template>
  <div class="min-h-screen py-12 transition-colors duration-500 bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-950 dark:to-slate-900 relative overflow-hidden">
    <!-- Background Decorations -->
    <div class="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none z-0">
      <div class="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-400/10 blur-3xl animate-pulse"></div>
      <div class="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-400/10 blur-3xl animate-pulse delay-1000"></div>
    </div>

    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
      <!-- Header -->
      <div class="mb-10 flex flex-col sm:flex-row sm:items-center justify-between gap-6 animate-in fade-in slide-in-from-top-4 duration-700">
        <div>
          <h1 class="text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600 dark:from-indigo-400 dark:to-purple-400 tracking-tight">
            个人资料
          </h1>
          <p class="mt-3 text-base text-slate-600 dark:text-slate-400 flex items-center gap-2">
            管理您的个人信息与账户设置
          </p>
        </div>
        <button 
          @click="saveProfile" 
          :disabled="loading"
          class="group relative inline-flex items-center justify-center px-8 py-3 border border-transparent rounded-2xl shadow-lg shadow-indigo-500/30 text-sm font-bold text-white bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-300 transform hover:-translate-y-1 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
        >
          <span class="absolute inset-0 rounded-2xl bg-white/20 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></span>
          <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          <span v-else class="relative flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
            </svg>
            保存修改
          </span>
        </button>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
        <!-- Left Column: Avatar & Status -->
        <div class="lg:col-span-4 space-y-8 animate-in fade-in slide-in-from-bottom-6 duration-700 delay-100">
          <!-- Avatar Card -->
          <div class="backdrop-blur-xl bg-white/80 dark:bg-slate-900/80 shadow-2xl shadow-slate-200/50 dark:shadow-slate-900/50 rounded-3xl overflow-hidden border border-white/20 dark:border-slate-700/50 p-8 text-center relative group hover:shadow-indigo-500/10 transition-all duration-500">
             <div class="absolute top-0 left-0 w-full h-32 bg-gradient-to-br from-indigo-500/20 to-purple-600/20 opacity-50 group-hover:opacity-70 transition-opacity duration-500"></div>
             <div class="relative z-10 mt-12">
                <div class="relative inline-block">
                  <div class="absolute inset-0 bg-gradient-to-r from-indigo-500 to-purple-600 rounded-full blur opacity-40 group-hover:opacity-60 transition-opacity duration-500 animate-pulse"></div>
                  <!-- <img 
                    :src="form.avatarUrl || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" 
                    class="relative h-36 w-36 rounded-full object-cover border-4 border-white dark:border-slate-800 shadow-xl mx-auto transition-transform duration-500 transform group-hover:scale-105"
                    alt="Avatar"
                  /> -->
                    <div class="relative h-36 w-36 rounded-full object-cover border-1 border-white dark:border-slate-800 shadow-xl mx-auto transition-transform duration-500 transform group-hover:scale-105 from-indigo-500 to-purple-500 text-white flex items-center justify-center text-6xl font-bold shadow-indigo-500/20 ring-2 ring-white dark:ring-slate-800">
                    {{ form.userName ? form.userName.charAt(0).toUpperCase() : '?' }}
          </div>
                  <!-- Removed Edit Icon as requested -->
                </div>
                <h2 class="mt-6 text-2xl font-bold text-slate-900 dark:text-white tracking-tight">{{ form.nickname || form.userName }}</h2>
                <p class="text-sm font-medium text-slate-500 dark:text-slate-400 mt-1">{{ form.userEmail }}</p>
                
                <div class="mt-6 flex justify-center gap-3">
                  <span class="px-4 py-1.5 rounded-full text-xs font-bold bg-indigo-50 text-indigo-600 dark:bg-indigo-900/30 dark:text-indigo-300 border border-indigo-100 dark:border-indigo-800 uppercase tracking-wider">
                    {{ form.userRole || 'USER' }}
                  </span>
                  <span v-if="form.status === 1" class="px-4 py-1.5 rounded-full text-xs font-bold bg-emerald-50 text-emerald-600 dark:bg-emerald-900/30 dark:text-emerald-300 border border-emerald-100 dark:border-emerald-800 flex items-center gap-1">
                    <span class="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>
                    Active
                  </span>
                </div>
             </div>
          </div>

          <!-- Storage Card -->
          <div class="backdrop-blur-xl bg-white/80 dark:bg-slate-900/80 shadow-xl shadow-slate-200/50 dark:shadow-slate-900/50 rounded-3xl overflow-hidden border border-white/20 dark:border-slate-700/50 p-8 hover:transform hover:-translate-y-1 transition-all duration-300">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-6 flex items-center gap-2">
              <div class="p-2 rounded-lg bg-indigo-50 dark:bg-indigo-900/30 text-indigo-600 dark:text-indigo-400">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4" />
                </svg>
              </div>
              存储空间
            </h3>
            <div class="space-y-5">
              <div class="flex justify-between text-sm font-medium">
                <span class="text-slate-500 dark:text-slate-400">已用空间</span>
                <span class="font-mono text-slate-700 dark:text-slate-200">{{ formatSize(form.storageUsed) }} / {{ formatSize(form.storageLimit) }}</span>
              </div>
              <div class="relative w-full bg-slate-100 dark:bg-slate-800 rounded-full h-3 overflow-hidden shadow-inner">
                <div class="absolute top-0 left-0 h-full bg-gradient-to-r from-indigo-500 to-purple-600 rounded-full transition-all duration-1000 ease-out shadow-[0_0_10px_rgba(99,102,241,0.5)]" :style="{ width: storagePercentage + '%' }"></div>
              </div>
              <p class="text-xs text-slate-400 dark:text-slate-500 text-center font-medium">
                您已使用 {{ storagePercentage }}% 的存储配额
              </p>
            </div>
          </div>
        </div>

        <!-- Right Column: Edit Form -->
        <div class="lg:col-span-8 animate-in fade-in slide-in-from-bottom-8 duration-1000 delay-200">
          <div class="backdrop-blur-xl bg-white/80 dark:bg-slate-900/80 shadow-xl shadow-slate-200/50 dark:shadow-slate-900/50 rounded-3xl overflow-hidden border border-white/20 dark:border-slate-700/50 p-8 sm:p-10">
            <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-8 border-b border-slate-100 dark:border-slate-800 pb-4 flex items-center gap-2">
              <span class="w-1 h-6 bg-indigo-500 rounded-full"></span>
              基本信息
            </h3>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
              <!-- Username (Readonly) -->
              <div class="group">
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1">用户名</label>
                <div class="relative">
                  <input 
                    type="text" 
                    v-model="form.userName" 
                    disabled
                    class="w-full px-5 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-800/50 text-slate-500 dark:text-slate-400 cursor-not-allowed font-mono text-sm"
                  />
                  <div class="absolute inset-y-0 right-4 flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                    </svg>
                  </div>
                </div>
              </div>

              <!-- Email (Readonly) -->
              <div class="group">
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1">邮箱地址</label>
                <div class="relative">
                  <input 
                    type="email" 
                    v-model="form.userEmail" 
                    disabled
                    class="w-full px-5 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50/50 dark:bg-slate-800/50 text-slate-500 dark:text-slate-400 cursor-not-allowed font-mono text-sm"
                  />
                  <div class="absolute inset-y-0 right-4 flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                    </svg>
                  </div>
                </div>
              </div>

              <!-- Nickname -->
              <div class="group">
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1 group-focus-within:text-indigo-600 dark:group-focus-within:text-indigo-400 transition-colors">显示昵称</label>
                <input 
                  type="text" 
                  v-model="form.nickname" 
                  class="w-full px-5 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all shadow-sm hover:border-indigo-300 dark:hover:border-indigo-700"
                  placeholder="设置一个好听的昵称"
                />
              </div>

              <!-- Phone -->
              <div class="group">
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1 group-focus-within:text-indigo-600 dark:group-focus-within:text-indigo-400 transition-colors">手机号码</label>
                <input 
                  type="tel" 
                  v-model="form.phoneNumber" 
                  class="w-full px-5 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all shadow-sm hover:border-indigo-300 dark:hover:border-indigo-700"
                  placeholder="绑定手机号"
                />
              </div>

              <!-- Website -->
              <div class="md:col-span-2 group">
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1 group-focus-within:text-indigo-600 dark:group-focus-within:text-indigo-400 transition-colors">个人网站 / 作品集</label>
                <div class="relative">
                  <div class="absolute inset-y-0 left-0 pl-5 flex items-center pointer-events-none">
                    <span class="text-slate-400 font-mono text-sm">https://</span>
                  </div>
                  <input 
                    type="text" 
                    v-model="form.websiteUrl" 
                    class="w-full pl-20 pr-5 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all shadow-sm hover:border-indigo-300 dark:hover:border-indigo-700"
                    placeholder="www.example.com"
                  />
                </div>
              </div>

              <!-- Bio -->
              <div class="md:col-span-2 group">
                <label class="block text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 ml-1 group-focus-within:text-indigo-600 dark:group-focus-within:text-indigo-400 transition-colors">个人简介</label>
                <textarea 
                  v-model="form.bio" 
                  rows="4"
                  class="w-full px-5 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 outline-none transition-all shadow-sm hover:border-indigo-300 dark:hover:border-indigo-700 resize-none"
                  placeholder="介绍一下你自己..."
                ></textarea>
              </div>
            </div>

            <!-- Web3 Section -->
            <div class="mt-10 pt-8 border-t border-slate-100 dark:border-slate-800">
              <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-6 flex items-center gap-2">
                <div class="p-2 rounded-lg bg-indigo-50 dark:bg-indigo-900/30 text-indigo-600 dark:text-indigo-400">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.384-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
                  </svg>
                </div>
                Web3 绑定
              </h3>
              <div class="bg-slate-50 dark:bg-slate-800/30 rounded-2xl p-6 border border-slate-100 dark:border-slate-800/50 hover:border-indigo-200 dark:hover:border-indigo-800 transition-colors duration-300">
                <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div>
                    <p class="text-sm font-bold text-slate-700 dark:text-slate-300">区块链钱包地址</p>
                    <p class="text-xs text-slate-500 mt-1.5" v-if="form.blockchainAddress">
                      <span class="inline-block w-2 h-2 rounded-full bg-green-500 mr-1"></span>
                      已绑定至 FISCO BCOS 网络
                    </p>
                    <p class="text-xs text-slate-500 mt-1.5" v-else>
                      <span class="inline-block w-2 h-2 rounded-full bg-slate-400 mr-1"></span>
                      暂未绑定区块链账户
                    </p>
                  </div>
                  <div v-if="form.blockchainAddress" class="flex items-center bg-white dark:bg-slate-900 px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 shadow-sm">
                    <span class="font-mono text-xs text-slate-600 dark:text-slate-400 mr-3">{{ form.blockchainAddress }}</span>
                    <button @click="copyAddress" class="p-1.5 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 text-indigo-500 hover:text-indigo-600 transition-colors" title="复制地址">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                      </svg>
                    </button>
                  </div>
                  <button v-else class="inline-flex items-center px-4 py-2 rounded-xl text-sm font-bold text-indigo-600 bg-indigo-50 hover:bg-indigo-100 dark:bg-indigo-900/20 dark:text-indigo-400 dark:hover:bg-indigo-900/40 transition-colors">
                    立即激活
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUserInfo } from '@/api/user'

const loading = ref(false)
const form = ref<any>({
  userId: '',
  userName: '',
  userEmail: '',
  nickname: '',
  bio: '',
  websiteUrl: '',
  phoneNumber: '',
  avatarUrl: '',
  userRole: '',
  storageLimit: 0,
  storageUsed: 0,
  blockchainAddress: '',
  status: 1
})

const storagePercentage = computed(() => {
  if (!form.value.storageLimit) return 0
  const pct = (form.value.storageUsed / form.value.storageLimit) * 100
  return Math.min(pct, 100).toFixed(1)
})

const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const shortenAddress = (addr: string) => {
  if (!addr) return ''
  return `${addr.substring(0, 6)}...${addr.substring(addr.length - 4)}`
}

const copyAddress = async () => {
  if (!form.value.blockchainAddress) return
  try {
    await navigator.clipboard.writeText(form.value.blockchainAddress)
    ElMessage.success('地址已复制')
  } catch (e) {
    ElMessage.error('复制失败')
  }
}

const fetchUser = async () => {
  try {
    const res = await getUserInfo()
    if (res.data) {
      form.value = { ...res.data }
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('获取用户信息失败')
  }
}

const saveProfile = async () => {
  loading.value = true
  try {
    // Filter out readonly fields if needed, but backend handles it too
    const payload = {
      nickname: form.value.nickname,
      bio: form.value.bio,
      websiteUrl: form.value.websiteUrl,
      phoneNumber: form.value.phoneNumber,
      avatarUrl: form.value.avatarUrl
    }
    await updateUserInfo(payload)
    ElMessage.success('个人资料已更新')
    await fetchUser() // Refresh
  } catch (e) {
    console.error(e)
    ElMessage.error('更新失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUser()
})
</script>
