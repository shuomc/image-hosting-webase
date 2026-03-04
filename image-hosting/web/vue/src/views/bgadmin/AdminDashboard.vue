<template>
  <div class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div v-for="(stat, index) in stats" :key="index" class="bg-white dark:bg-slate-800 p-6 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 hover:shadow-md transition-shadow">
        <div class="flex justify-between items-start">
          <div>
            <p class="text-sm font-medium text-slate-500 dark:text-slate-400">{{ stat.title }}</p>
            <h3 class="text-2xl font-bold text-slate-900 dark:text-white mt-2">{{ stat.value }}</h3>
          </div>
          <div :class="`p-3 rounded-xl ${stat.iconBg} ${stat.iconColor}`">
            <component :is="stat.icon" class="w-6 h-6" />
          </div>
        </div>
        <div class="mt-4 flex items-center text-sm">
          <span :class="stat.trend > 0 ? 'text-green-500' : 'text-red-500'" class="font-medium flex items-center">
            <svg v-if="stat.trend > 0" class="w-4 h-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
            <svg v-else class="w-4 h-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 17h8m0 0V9m0 8l-8-8-4 4-6-6" />
            </svg>
            {{ Math.abs(stat.trend) }}%
          </span>
          <span class="text-slate-400 ml-2">较上月</span>
        </div>
      </div>
    </div>

    <!-- Charts Section -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Upload Trend Chart -->
      <div class="bg-white dark:bg-slate-800 p-6 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700">
        <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-6">上传趋势</h3>
        <div ref="uploadChartRef" class="w-full h-80"></div>
      </div>

      <!-- Image Distribution Charts -->
      <div class="bg-white dark:bg-slate-800 p-6 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700">
        <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-6">图片分布统计</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="h-80">
            <h4 class="text-center text-sm font-medium text-slate-500 mb-2">上传 / 铸造</h4>
            <div ref="mintedChartRef" class="w-full h-full"></div>
          </div>
          <div class="h-80">
            <h4 class="text-center text-sm font-medium text-slate-500 mb-2">公开 / 私有</h4>
            <div ref="publicChartRef" class="w-full h-full"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { UsersIcon, PhotoIcon, ServerIcon, CurrencyYenIcon } from '@heroicons/vue/24/outline';
import { getDashboardStats } from '@/api/admin/dashboard';

// Stats Data
const stats = ref([
  { title: '总用户数', value: 'Loading...', trend: 0, icon: UsersIcon, iconBg: 'bg-blue-50 dark:bg-blue-900/20', iconColor: 'text-blue-600 dark:text-blue-400' },
  { title: '图片总数', value: 'Loading...', trend: 0, icon: PhotoIcon, iconBg: 'bg-indigo-50 dark:bg-indigo-900/20', iconColor: 'text-indigo-600 dark:text-indigo-400' },
  { title: '存储占用', value: 'Loading...', trend: 0, icon: ServerIcon, iconBg: 'bg-purple-50 dark:bg-purple-900/20', iconColor: 'text-purple-600 dark:text-purple-400' },
  { title: 'NFT 交易量', value: 'Loading...', trend: 0, icon: CurrencyYenIcon, iconBg: 'bg-emerald-50 dark:bg-emerald-900/20', iconColor: 'text-emerald-600 dark:text-emerald-400' },
]);

const loadStats = async () => {
  try {
    const res = await getDashboardStats();
    if (res.code === 200) {
      const data = res.data;
      stats.value[0].value = data.totalUsers.toLocaleString();
      stats.value[1].value = data.totalImages.toLocaleString();
      stats.value[2].value = data.storageUsed;
      stats.value[3].value = data.nftTransactionVolume.toLocaleString();

      // Update Charts
      if (uploadChart) {
        uploadChart.setOption({
          xAxis: { data: data.uploadTrendDates },
          series: [{ data: data.uploadTrendCounts }]
        });
      }

      if (mintedChart) {
        const unminted = data.totalImages - data.mintedImageCount;
        mintedChart.setOption({
          series: [{
            data: [
              { value: data.mintedImageCount, name: '已铸造' },
              { value: unminted, name: '未铸造' }
            ]
          }]
        });
      }

      if (publicChart) {
        publicChart.setOption({
          series: [{
            data: [
              { value: data.publicImageCount, name: '公开' },
              { value: data.privateImageCount, name: '私有' }
            ]
          }]
        });
      }
    }
  } catch (error) {
    console.error('Failed to load dashboard stats', error);
  }
};

const uploadChartRef = ref<HTMLElement | null>(null);
const mintedChartRef = ref<HTMLElement | null>(null);
const publicChartRef = ref<HTMLElement | null>(null);
let uploadChart: echarts.ECharts | null = null;
let mintedChart: echarts.ECharts | null = null;
let publicChart: echarts.ECharts | null = null;

const initCharts = () => {
  if (uploadChartRef.value) {
    uploadChart = echarts.init(uploadChartRef.value);
    uploadChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', boundaryGap: false, data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'] },
      yAxis: { type: 'value' },
      series: [{
        name: 'Uploads',
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.3, color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#6366f1' }, { offset: 1, color: '#e0e7ff' }]) },
        itemStyle: { color: '#6366f1' },
        data: [0, 0, 0, 0, 0, 0, 0]
      }]
    });
  }

  const pieOption = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '0%' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false, position: 'center' },
      emphasis: { label: { show: true, fontSize: '20', fontWeight: 'bold' } },
      data: []
    }]
  };

  if (mintedChartRef.value) {
    mintedChart = echarts.init(mintedChartRef.value);
    mintedChart.setOption({
      ...pieOption,
      series: [{
        ...pieOption.series[0],
        name: 'Mint Status',
        data: [
          { value: 0, name: '已铸造' },
          { value: 0, name: '未铸造' }
        ]
      }]
    });
  }

  if (publicChartRef.value) {
    publicChart = echarts.init(publicChartRef.value);
    publicChart.setOption({
      ...pieOption,
      series: [{
        ...pieOption.series[0],
        name: 'Public Status',
        data: [
          { value: 0, name: '公开' },
          { value: 0, name: '私有' }
        ]
      }]
    });
  }
};

const handleResize = () => {
  uploadChart?.resize();
  mintedChart?.resize();
  publicChart?.resize();
};

onMounted(() => {
  loadStats();
  initCharts();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  uploadChart?.dispose();
  mintedChart?.dispose();
  publicChart?.dispose();
});
</script>
