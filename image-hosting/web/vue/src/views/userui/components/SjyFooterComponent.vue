<template>
  <footer class="bg-white dark:bg-slate-900 border-t border-slate-100 dark:border-slate-800 pt-16 pb-8 transition-colors duration-300">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      
      <!-- Top Section: CTA Buttons -->
      <div class="flex flex-col sm:flex-row items-center justify-center gap-4 mb-16">
        <button class="px-8 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-full transition-all shadow-lg shadow-indigo-500/25 flex items-center gap-2">
          <span>上传自己的图片</span>
        </button>
        <button class="px-8 py-3 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-300 font-bold rounded-full hover:border-indigo-600 dark:hover:border-indigo-500 hover:text-indigo-600 dark:hover:text-indigo-400 transition-all">
          帮助中心
        </button>
      </div>

      <!-- Main Content Grid -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12 mb-16">
        <!-- Brand Section -->
        <div class="space-y-6">
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 bg-gradient-to-br from-indigo-600 to-violet-600 rounded-lg flex items-center justify-center text-white font-bold text-lg shadow-lg shadow-indigo-500/30">
              IH
            </div>
            <span class="text-xl font-bold text-slate-900 dark:text-white">
              Image-Hosting<span class="text-indigo-600 dark:text-indigo-400">.NFT</span>
            </span>
          </div>
          <p class="text-slate-500 dark:text-slate-400 text-sm leading-relaxed">
            下一代去中心化数字资产平台。发现、存储并铸造属于你的独一无二的数字艺术品。
          </p>
          <div class="flex flex-col gap-3">
            <button class="flex items-center justify-center gap-2 px-4 py-2 bg-slate-900 dark:bg-white text-white dark:text-slate-900 rounded-lg text-sm font-medium hover:opacity-90 transition-opacity">
              下载 iOS 版
            </button>
            <button class="flex items-center justify-center gap-2 px-4 py-2 bg-slate-900 dark:bg-white text-white dark:text-slate-900 rounded-lg text-sm font-medium hover:opacity-90 transition-opacity">
              下载 Android 版
            </button>
          </div>
        </div>

        <!-- Links Column 1 -->
        <div>
          <h4 class="text-slate-900 dark:text-white font-bold mb-6 uppercase tracking-wider text-xs">平台服务</h4>
          <ul class="space-y-4">
            <li v-for="link in platformLinks" :key="link.text">
              <a href="#" class="text-slate-500 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 text-sm transition-colors">{{ link.text }}</a>
            </li>
          </ul>
        </div>

        <!-- Links Column 2 -->
        <div>
          <h4 class="text-slate-900 dark:text-white font-bold mb-6 uppercase tracking-wider text-xs">关于企业</h4>
          <ul class="space-y-4">
            <li v-for="link in companyLinks" :key="link.text">
              <a href="#" class="text-slate-500 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 text-sm transition-colors">{{ link.text }}</a>
            </li>
          </ul>
        </div>

        <!-- Tags Section -->
        <div>
          <h4 class="text-slate-900 dark:text-white font-bold mb-6 uppercase tracking-wider text-xs">热门分类</h4>
          <div class="flex flex-wrap gap-2">
            <a v-for="tag in tags" :key="tag" href="#" class="px-3 py-1.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-md text-xs text-slate-600 dark:text-slate-400 hover:border-indigo-500 hover:text-indigo-600 transition-all">
              {{ tag }}
            </a>
          </div>
        </div>
      </div>

      <!-- Bottom Section -->
      <div class="pt-8 border-t border-slate-100 dark:border-slate-800 flex flex-col md:flex-row justify-between items-center gap-6">
        <div class="flex flex-col md:flex-row items-center gap-4 md:gap-8">
          <span class="text-slate-400 dark:text-slate-500 text-sm">© 2025 Image-Hosting.NFT</span>
          <div class="flex gap-6">
            <a href="#" class="text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-300 text-sm transition-colors">使用条款</a>
            <a href="#" class="text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-300 text-sm transition-colors">隐私政策</a>
            <a href="#" class="text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-300 text-sm transition-colors">许可证</a>
          </div>
        </div>

        <div class="flex items-center gap-4">
          <el-dropdown trigger="click">
            <span class="flex items-center gap-2 text-slate-500 dark:text-slate-400 hover:text-indigo-600 cursor-pointer text-sm font-medium">
              <el-icon><Orange /></el-icon>
              简体中文
              <el-icon class="text-xs"><ArrowUp /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="dark:bg-slate-800 dark:border-slate-700">
                <el-dropdown-item class="dark:text-slate-300 dark:hover:bg-slate-700">English</el-dropdown-item>
                <el-dropdown-item class="dark:text-slate-300 dark:hover:bg-slate-700">繁體中文</el-dropdown-item>
                <el-dropdown-item class="dark:text-slate-300 dark:hover:bg-slate-700">日本語</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import {
  Orange,
  ArrowUp
} from '@element-plus/icons-vue';

const platformLinks = [
  { text: '免费素材图片' },
  { text: '免费视频' },
  { text: '热门搜索' },
  { text: '挑战赛' },
  { text: '热门作者排行榜' },
  { text: '其他插件和应用' }
];

const companyLinks = [
  { text: '关于' },
  { text: '博客' },
  { text: '帮助中心' },
  { text: '举报内容' },
  { text: '图片和视频 API' },
  { text: '注册' }
];

const tags = [
  '黑白摄影', '生日快乐图片', '免费商业视频', '新年快乐图片', 
  '精美高清壁纸', '银河壁纸', '锁屏壁纸', 'iPhone 壁纸', 
  '4K 壁纸', '大自然壁纸', '爱情壁纸', '手机壁纸'
];
</script>

<style scoped>
/* 移除大部分旧样式，仅保留必要的 Element Plus 覆盖 */
:deep(.el-dropdown-menu) {
  @apply dark:bg-slate-800 dark:border-slate-700;
}

:deep(.el-dropdown-menu__item) {
  @apply dark:text-slate-300 dark:hover:bg-slate-700 dark:hover:text-indigo-400;
}
</style>
