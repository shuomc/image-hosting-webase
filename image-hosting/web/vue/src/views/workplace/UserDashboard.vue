<template>
  <div class="min-h-screen w-full h-full relative transition-colors p-6">
    <div class="mb-8">
      <h1 class="text-3xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-500">数据总览</h1>
      <p class="text-sm mt-1 dark:text-slate-300 text-slate-500">查看您的个人统计数据与资产分布</p>
    </div>

    <div v-if="loading" class="w-full h-40 flex items-center justify-center text-slate-400">加载中…</div>
    <div v-else-if="data" class="space-y-6">
      
      <!-- General Stats Row -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-indigo-50 dark:bg-indigo-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">总上传图片</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ data.userStats?.totalUploads || 0 }}</div>
        </div>

        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-emerald-50 dark:bg-emerald-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-emerald-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">总浏览量</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ data.userStats?.totalViews || 0 }}</div>
        </div>

        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-sky-50 dark:bg-sky-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-sky-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">总下载量</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ data.userStats?.totalDownloads || 0 }}</div>
        </div>

        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-rose-50 dark:bg-rose-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">总点赞数</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ data.userStats?.totalLikes || 0 }}</div>
        </div>

        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-blue-50 dark:bg-blue-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">交易记录</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ data.transactionCount || 0 }} <span class="text-xs font-normal text-slate-400">条</span></div>
        </div>
      </div>

      <!-- Assets & Storage Row -->
      <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
        <div class="xl:col-span-2 bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700">
          <div class="flex items-center justify-between mb-4">
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">存储空间使用情况</div>
            <span class="text-xs font-semibold px-2 py-1 bg-slate-100 dark:bg-slate-700 rounded-full text-slate-600 dark:text-slate-300">
              {{ Math.round(((data.userStats?.storageUsed || 0) / (data.userStats?.storageLimit || 1073741824)) * 100) }}%
            </span>
          </div>
          <div class="flex justify-between items-end mb-2">
            <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ formatSize(data.userStats?.storageUsed || 0) }}</div>
            <div class="text-sm text-slate-500">/ {{ formatSize(data.userStats?.storageLimit || 1073741824) }}</div>
          </div>
          <div class="w-full bg-slate-100 dark:bg-slate-700 rounded-full h-3 overflow-hidden">
            <div class="bg-gradient-to-r from-indigo-500 to-purple-500 h-full rounded-full transition-all duration-500" 
                 :style="{ width: Math.min(100, ((data.userStats?.storageUsed || 0) / (data.userStats?.storageLimit || 1073741824)) * 100) + '%' }"></div>
          </div>
        </div>

        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-purple-50 dark:bg-purple-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-purple-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.384-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">持有 NFT</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white">{{ data.totalNfts || 0 }}</div>
        </div>

        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
          <div class="flex items-center gap-3 mb-2">
            <div class="p-2 bg-yellow-50 dark:bg-yellow-900/30 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="text-sm font-medium text-slate-500 dark:text-slate-400">钱包余额</div>
          </div>
          <div class="text-2xl font-bold text-slate-800 dark:text-white truncate">{{ data.walletBalance || '0' }} <span class="text-xs font-normal text-slate-400 ml-1">ETH</span></div>
        </div>
      </div>

      <!-- Charts -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Minted Ratio -->
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700">
          <div class="flex items-center gap-2 mb-4">
            <div class="w-1.5 h-6 bg-violet-500 rounded-full"></div>
            <h3 class="text-lg font-bold text-slate-800 dark:text-white">资产铸造比例</h3>
          </div>
          <div v-if="data.totalImages > 0" ref="mintedChartRef" class="w-full h-64"></div>
          <div v-else class="w-full h-64 flex flex-col items-center justify-center text-slate-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mb-2 opacity-20" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <span class="text-sm">暂无数据</span>
          </div>
        </div>
        
        <!-- Public Ratio -->
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700">
          <div class="flex items-center gap-2 mb-4">
            <div class="w-1.5 h-6 bg-emerald-500 rounded-full"></div>
            <h3 class="text-lg font-bold text-slate-800 dark:text-white">公开/私有比例</h3>
          </div>
          <div v-if="data.totalImages > 0" ref="publicChartRef" class="w-full h-64"></div>
          <div v-else class="w-full h-64 flex flex-col items-center justify-center text-slate-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mb-2 opacity-20" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <span class="text-sm">暂无数据</span>
          </div>
        </div>

        <!-- Transaction Trend -->
        <div class="bg-white dark:bg-slate-800 rounded-2xl p-6 shadow-sm border border-slate-100 dark:border-slate-700 lg:col-span-2">
          <div class="flex items-center gap-2 mb-4">
            <div class="w-1.5 h-6 bg-amber-500 rounded-full"></div>
            <h3 class="text-lg font-bold text-slate-800 dark:text-white">交易量趋势 (近7天)</h3>
          </div>
          <div v-if="data.transactionDates && data.transactionDates.length > 0" ref="transChartRef" class="w-full h-80"></div>
          <div v-else class="w-full h-80 flex flex-col items-center justify-center text-slate-400 bg-slate-50/50 dark:bg-slate-900/50 rounded-xl">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mb-2 opacity-20" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" />
            </svg>
            <span class="text-sm">暂无交易数据</span>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import * as echarts from 'echarts';
import request from '@/utils/request';

const loading = ref(true);
const data = ref<any>(null);

const mintedChartRef = ref<HTMLElement | null>(null);
const publicChartRef = ref<HTMLElement | null>(null);
const transChartRef = ref<HTMLElement | null>(null);

let mintedChart: echarts.ECharts | null = null;
let publicChart: echarts.ECharts | null = null;
let transChart: echarts.ECharts | null = null;

const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

const initCharts = () => {
  if (!data.value) return;

  // Minted Chart
  if (mintedChartRef.value) {
    if (mintedChart) mintedChart.dispose();
    mintedChart = echarts.init(mintedChartRef.value);
    mintedChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: '0%', left: 'center' },
      color: ['#8b5cf6', '#cbd5e1'],
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false, position: 'center' },
        emphasis: { label: { show: true, fontSize: 20, fontWeight: 'bold' } },
        labelLine: { show: false },
        data: [
          { value: data.value.mintedCount || 0, name: '已铸造' },
          { value: data.value.unmintedCount || 0, name: '未铸造' }
        ]
      }]
    });
  }

  // Public Chart
  if (publicChartRef.value) {
    if (publicChart) publicChart.dispose();
    publicChart = echarts.init(publicChartRef.value);
    publicChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { bottom: '0%', left: 'center' },
      color: ['#10b981', '#f43f5e'],
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false, position: 'center' },
        emphasis: { label: { show: true, fontSize: 20, fontWeight: 'bold' } },
        labelLine: { show: false },
        data: [
          { value: data.value.publicCount || 0, name: '公开' },
          { value: data.value.privateCount || 0, name: '私有' }
        ]
      }]
    });
  }

  // Transaction Trend Chart
  if (transChartRef.value && data.value.transactionDates && data.value.transactionDates.length > 0) {
    if (transChart) transChart.dispose();
    transChart = echarts.init(transChartRef.value);
    transChart.setOption({
      tooltip: { 
        trigger: 'axis',
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        borderColor: '#f59e0b',
        borderWidth: 1,
        textStyle: { color: '#334155' }
      },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: data.value.transactionDates,
        axisLine: { lineStyle: { color: '#e2e8f0' } },
        axisLabel: { color: '#64748b' }
      },
      yAxis: { 
        type: 'value',
        splitLine: { lineStyle: { type: 'dashed', color: '#f1f5f9' } },
        axisLabel: { color: '#64748b' }
      },
      series: [{
        name: '交易量',
        type: 'line',
        smooth: true,
        data: data.value.transactionAmounts,
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: { color: '#f59e0b' },
        lineStyle: { width: 4, color: '#f59e0b' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(245, 158, 11, 0.4)' },
            { offset: 1, color: 'rgba(245, 158, 11, 0)' }
          ])
        }
      }]
    });
  }
};

const handleResize = () => {
  mintedChart?.resize();
  publicChart?.resize();
  transChart?.resize();
};

const loadData = async () => {
  try {
    loading.value = true;
    const res = await request.get('/api/user/dashboard/stats');
    if (res.code === 200) {
      data.value = res.data;
    }
  } catch (error) {
    console.error('Failed to load user dashboard stats', error);
  } finally {
    loading.value = false;
    nextTick(() => {
      initCharts();
    });
  }
};

onMounted(() => {
  loadData();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  mintedChart?.dispose();
  publicChart?.dispose();
  transChart?.dispose();
});
</script>
