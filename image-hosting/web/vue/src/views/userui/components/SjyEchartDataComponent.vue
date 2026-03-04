<template>
  <div class="echart-data-component">
    <div class="statistics-header">
      <div class="header-item">
        <span class="header-label">实时浏览量</span>
        <span class="header-value">{{ formatNumber(statistics.totalViews) }}</span>
      </div>
    </div>

    <div class="overview-cards">
      <div class="card-item">
        <span class="card-label">浏览量</span>
        <span class="card-value">{{ formatNumber(statistics.currentViews) }}</span>
      </div>
      <div class="card-item">
        <span class="card-label">下载数</span>
        <span class="card-value">{{ formatNumber(statistics.downloads) }}</span>
      </div>
      <div class="card-item">
        <span class="card-label">点赞数</span>
        <span class="card-value">{{ formatNumber(statistics.likes) }}</span>
      </div>
      <div class="card-item">
        <span class="card-label">关注者</span>
        <span class="card-value">{{ formatNumber(statistics.followers) }}</span>
      </div>
    </div>

    <div class="chart-container">
      <div ref="echartRef" class="echart-instance"></div>
    </div>

    <div class="daily-stats-section">
      <h3>每日上传的统计数据</h3>
      <div class="section-content">
        <p v-if="uploadedImagesStats.length === 0">暂无数据</p>
        <div v-else class="uploaded-images-grid">
          <div v-for="image in uploadedImagesStats" :key="image.imageId" class="uploaded-image-card">
            <h4>{{ image.fileName }}</h4>
            <p>浏览量: {{ formatNumber(image.views) }}</p>
            <p>下载量: {{ formatNumber(image.downloads) }}</p>
            <p>点赞数: {{ formatNumber(image.likes) }}</p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isLoading" class="loading-overlay">加载数据中...</div>
    <div v-if="error" class="error-overlay">{{ error }}</div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, watch } from 'vue';
import * as echarts from 'echarts'; // 导入 ECharts
import axios from 'axios'; // 导入 axios
import type { ECharts, ECUnitOption } from 'echarts'; // 导入类型

// 定义数据接口
interface Statistics {
  totalViews: number;
  currentViews: number;
  downloads: number;
  likes: number;
  followers: number;
  viewsOverTime: { date: string; views: number; }[];
}

interface UploadedImageStat {
  imageId: string;
  fileName: string;
  views: number;
  downloads: number;
  likes: number;
}

// 响应式数据
const echartRef = ref<HTMLElement | null>(null); // ECharts 容器的引用
let myChart: ECharts | null = null; // ECharts 实例
const isLoading = ref(true);
const error = ref<string | null>(null);
const statistics = ref<Statistics>({ // 初始化数据结构
  totalViews: 0,
  currentViews: 0,
  downloads: 0,
  likes: 0,
  followers: 0,
  viewsOverTime: []
});
const uploadedImagesStats = ref<UploadedImageStat[]>([]);

const JSON_SERVER_BASE_URL = 'http://localhost:3001'; // JSON Server 地址

// 获取数据
const fetchData = async () => {
  isLoading.value = true;
  error.value = null;
  try {
    const statsResponse = await axios.get<Statistics>(`${JSON_SERVER_BASE_URL}/statistics`);
    statistics.value = statsResponse.data;

    const uploadedStatsResponse = await axios.get<UploadedImageStat[]>(`${JSON_SERVER_BASE_URL}/uploadedImagesStats`);
    uploadedImagesStats.value = uploadedStatsResponse.data;

    initChart(); // 数据获取后初始化图表

  } catch (err) {
    console.error('Failed to fetch ECharts data:', err);
    error.value = '无法加载统计数据。请检查JSON Server是否运行。';
  } finally {
    isLoading.value = false;
  }
};

// 初始化 ECharts 图表
const initChart = () => {
  if (echartRef.value && statistics.value.viewsOverTime.length > 0) {
    if (myChart) {
      myChart.dispose(); // 销毁旧实例，防止重复初始化
    }
    myChart = echarts.init(echartRef.value);

    const dates = statistics.value.viewsOverTime.map(item => item.date);
    const views = statistics.value.viewsOverTime.map(item => item.views);

    const option: ECUnitOption = {
      tooltip: {
        trigger: 'axis',
        formatter: '{b}<br/>浏览量: {c}'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: dates,
        axisLabel: {
            formatter: (value: string) => {
                // 只显示月-日
                const date = new Date(value);
                return `${date.getMonth() + 1}-${date.getDate()}`;
            }
        },
        axisLine: {
            lineStyle: {
                color: '#ccc' // 坐标轴线颜色
            }
        },
        axisTick: {
            show: false // 不显示刻度线
        }
      },
      yAxis: {
        type: 'value',
        name: '浏览量',
        axisLine: {
            show: false // 不显示Y轴线
        },
        axisTick: {
            show: false // 不显示Y轴刻度线
        },
        splitLine: {
            lineStyle: {
                color: '#eee', // 分割线颜色
                type: 'dashed' // 分割线样式
            }
        }
      },
      series: [
        {
          name: '浏览量',
          type: 'line',
          smooth: true, // 平滑曲线
          lineStyle: {
            color: '#409eff', // 线的颜色
            width: 2 // 线的宽度
          },
          areaStyle: { // 区域填充样式
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64,158,255,0.3)' }, // 渐变色
              { offset: 1, color: 'rgba(64,158,255,0)' }
            ])
          },
          itemStyle: {
            color: '#409eff' // 数据点颜色
          },
          data: views
        }
      ]
    };
    myChart.setOption(option);

    // 监听窗口大小变化，调整图表尺寸
    window.addEventListener('resize', resizeChart);
  }
};

const resizeChart = () => {
  myChart?.resize();
};

// 数字格式化函数，例如 1626156 -> 1.6 百万, 824100 -> 824.1 千
const formatNumber = (num: number | undefined): string => {
  if (num === undefined || num === null) return 'N/A';
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + ' 百万';
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + ' 千';
  }
  return num.toString();
};


onMounted(() => {
  fetchData();
});

onUnmounted(() => {
  if (myChart) {
    myChart.dispose(); // 组件销毁时销毁图表实例
    window.removeEventListener('resize', resizeChart); // 移除监听器
  }
});

// 如果父组件传入的用户ID变化，重新加载数据（如果该组件是接收用户ID的）
watch(() => props.userId, (newUserId) => {
  if (newUserId) {
    fetchData();
  }
});
</script>

<style scoped>
.echart-data-component {
  padding: 20px;
  background-color: #f1f2f4; /* 页面背景色 */
  min-height: calc(100vh - 60px - 80px); /* 减去顶部导航和底部留白 */
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center; /* 让整个组件的内容（包括卡片）水平居中 */
}

.statistics-header {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  display: flex;
  justify-content: center; /* 居中其子项 */
  align-items: center;
  width: 100%; /* 允许它占据可用宽度 */
  max-width: 300px; /* 限制最大宽度，使其看起来像一个独立的卡片 */
  margin: 0 auto; /* 辅助居中 */
  box-sizing: border-box; /* 确保 padding 包含在 width 内 */
}

.header-item {
  display: flex;
  flex-direction: column;
  align-items: center; /* 内部文本居中 */
  text-align: center; /* 确保文本本身居中 */
  /* 移除 flex-start，改为 center */
}

.header-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.header-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.overview-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 20px;
  width: 100%; /* 确保卡片网格占据全宽，以便在父级居中时也居中 */
  max-width: 1200px; /* 限制最大宽度，与主页内容对齐 */
  margin: 0 auto; /* 辅助居中 */
}

.card-item {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.card-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.chart-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%; /* 确保图表容器占据全宽 */
  max-width: 1200px; /* 限制最大宽度，与主页内容对齐 */
  margin: 0 auto; /* 辅助居中 */
}

.echart-instance {
  width: 100%;
  height: 100%;
}

.daily-stats-section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.daily-stats-section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  width: 100%; /* 确保该部分占据全宽 */
  max-width: 1200px; /* 限制最大宽度，与主页内容对齐 */
  margin: 0 auto; /* 辅助居中 */
}

.uploaded-images-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.uploaded-image-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  background-color: #f9f9f9;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.uploaded-image-card h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #333;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.uploaded-image-card p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.loading-overlay, .error-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  color: #333;
  z-index: 100;
}
</style>
