/*
 * @Author: 思俊宇
 */

import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
// 导入 pinia-plugin-persistedstate
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 不需要在这里useUserStore来手动恢复状态

import App from './App.vue'
import router from './router'
import './utils/request' // 导入请求拦截器

const app = createApp(App)

const pinia = createPinia();

pinia.use(piniaPluginPersistedstate);
// =============================

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 将配置好的 Pinia 实例挂载到 App
app.use(pinia)

// 挂载路由器
app.use(router)

// 挂载 Element Plus
app.use(ElementPlus)


// 全局错误处理
app.config.errorHandler = (err, instance, info) => {
  console.error('全局错误:', err)
  console.log('错误信息:', info)
}


app.mount('#app')
