<template>
  <div class="min-h-screen py-8 transition-colors duration-300 bg-slate-50 dark:bg-slate-950">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Header -->
      <div class="mb-8 flex flex-col sm:flex-row sm:items-center justify-between gap-4 animate-in fade-in slide-in-from-top-4 duration-500">
        <div>
          <h1 class="text-3xl font-extrabold text-slate-900 dark:text-white tracking-tight">全网交易记录</h1>
          <p class="mt-2 text-sm text-slate-500 dark:text-slate-400 flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 6a1 1 0 011-1h6a1 1 0 110 2H7a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H7z" clip-rule="evenodd" />
            </svg>
            实时监控链上每一笔 NFT 交易动态
          </p>
        </div>
        <button 
          @click="$router.back()" 
          class="inline-flex items-center justify-center px-5 py-2.5 border border-slate-200 dark:border-slate-700 rounded-xl shadow-sm text-sm font-medium text-slate-700 dark:text-slate-200 bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all transform hover:-translate-y-0.5"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
          返回
        </button>
      </div>

      <!-- Info/Notes Card -->
      <div class="mb-8 bg-indigo-50 dark:bg-indigo-900/20 border border-indigo-100 dark:border-indigo-800/50 rounded-2xl p-6 animate-in fade-in slide-in-from-bottom-2 duration-700">
        <h3 class="text-lg font-bold text-indigo-900 dark:text-indigo-300 mb-4 flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
          </svg>
          交易须知
        </h3>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="flex items-start">
            <div class="flex-shrink-0 h-8 w-8 rounded-full bg-indigo-100 dark:bg-indigo-800 flex items-center justify-center text-indigo-600 dark:text-indigo-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="ml-3">
              <h4 class="text-sm font-bold text-slate-800 dark:text-slate-200">不可篡改</h4>
              <p class="mt-1 text-xs text-slate-600 dark:text-slate-400">所有交易记录均已上链，数据真实可靠，无法被篡改或撤销。</p>
            </div>
          </div>
          <div class="flex items-start">
            <div class="flex-shrink-0 h-8 w-8 rounded-full bg-purple-100 dark:bg-purple-800 flex items-center justify-center text-purple-600 dark:text-purple-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
            </div>
            <div class="ml-3">
              <h4 class="text-sm font-bold text-slate-800 dark:text-slate-200">透明公开</h4>
              <p class="mt-1 text-xs text-slate-600 dark:text-slate-400">点击交易哈希可复制完整代码，点击图片可预览 NFT。</p>
            </div>
          </div>
          <div class="flex items-start">
            <div class="flex-shrink-0 h-8 w-8 rounded-full bg-green-100 dark:bg-green-800 flex items-center justify-center text-green-600 dark:text-green-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="ml-3">
              <h4 class="text-sm font-bold text-slate-800 dark:text-slate-200">实时更新</h4>
              <p class="mt-1 text-xs text-slate-600 dark:text-slate-400">列表数据实时同步区块链网络，确保您获取最新的市场动态。</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Transaction Stats Chart -->
      <div class="mb-8 bg-white dark:bg-slate-900 shadow-xl rounded-3xl overflow-hidden border border-slate-100 dark:border-slate-800 animate-in fade-in slide-in-from-bottom-4 duration-800">
        <div class="p-6 border-b border-slate-100 dark:border-slate-800 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <h3 class="text-lg font-bold text-slate-900 dark:text-white flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" />
            </svg>
            交易趋势分析
          </h3>
          <div class="flex bg-slate-100 dark:bg-slate-800 rounded-lg p-1">
            <button 
              v-for="t in ['day', 'week', 'month']" 
              :key="t"
              @click="changeStatsType(t)"
              class="px-4 py-1.5 text-sm font-medium rounded-md transition-all"
              :class="statsType === t ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-sm' : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'"
            >
              {{ t === 'day' ? '日' : t === 'week' ? '周' : '月' }}
            </button>
          </div>
        </div>
        <div class="p-6">
           <div ref="chartRef" class="w-full h-80"></div>
        </div>
      </div>

      <!-- Transactions Table -->
      <div class="bg-white dark:bg-slate-900 shadow-xl rounded-3xl overflow-hidden border border-slate-100 dark:border-slate-800 animate-in fade-in slide-in-from-bottom-6 duration-1000">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-slate-100 dark:divide-slate-800">
            <thead class="bg-slate-50 dark:bg-slate-800/50">
              <tr>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  NFT 信息
                </th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  交易哈希
                </th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  发送方
                </th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  接收方
                </th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  价格
                </th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  类型
                </th>
                <th scope="col" class="px-6 py-4 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider whitespace-nowrap">
                  时间
                </th>
              </tr>
            </thead>
            <tbody class="bg-white dark:bg-slate-900 divide-y divide-slate-100 dark:divide-slate-800" v-loading="loading">
              <tr v-for="tx in transactionList" :key="tx.transactionId" class="hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors duration-150">
                <!-- NFT Info -->
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-12 w-12 group relative">
                      <el-image 
                        class="h-12 w-12 rounded-xl object-cover border border-slate-200 dark:border-slate-700 shadow-sm cursor-pointer transition-transform transform group-hover:scale-105"
                        :src="tx.imageUrl" 
                        :preview-src-list="[tx.imageUrl]"
                        :initial-index="0"
                        fit="cover"
                        preview-teleported
                        hide-on-click-modal
                      >
                        <template #error>
                          <div class="flex justify-center items-center w-full h-full bg-slate-100 dark:bg-slate-800 text-slate-400 rounded-xl">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
                          </div>
                        </template>
                      </el-image>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-bold text-slate-900 dark:text-white">{{ tx.nftName || '未命名' }}</div>
                      <div class="text-xs text-slate-500 dark:text-slate-400 font-mono mt-1">ID: {{ tx.nftId }}</div>
                    </div>
                  </div>
                </td>

                <!-- Transaction Hash -->
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center space-x-2 group">
                    <span class="text-xs text-slate-600 dark:text-slate-400 font-mono bg-slate-100 dark:bg-slate-800 px-2 py-1 rounded-md select-all border border-slate-200 dark:border-slate-700 max-w-[140px] truncate hover:max-w-none transition-all duration-300">{{ tx.transactionHash }}</span>
                    <button 
                      @click="copyToClipboard(tx.transactionHash)"
                      class="text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors p-1.5 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 opacity-0 group-hover:opacity-100"
                      title="复制完整哈希"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                      </svg>
                    </button>
                  </div>
                </td>

                <!-- From -->
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                     <span class="text-xs font-mono text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-slate-800/80 border border-slate-200 dark:border-slate-700 px-2 py-1 rounded-md select-all">
                       {{ tx.fromAddress }}
                     </span>
                  </div>
                </td>

                <!-- To -->
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                     <span class="text-xs font-mono text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-slate-800/80 border border-slate-200 dark:border-slate-700 px-2 py-1 rounded-md select-all">
                       {{ tx.toAddress }}
                     </span>
                  </div>
                </td>

                <!-- Price -->
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-3 py-1 inline-flex text-sm leading-5 font-bold rounded-full bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-100 dark:border-green-900/30">
                    {{ formatPrice(tx.price) }} ETH
                  </span>
                </td>

                <!-- Type -->
                <td class="px-6 py-4 whitespace-nowrap">
                   <span 
                    class="px-2.5 py-1 text-xs font-bold rounded-lg border"
                    :class="{
                      'bg-blue-50 text-blue-700 border-blue-100 dark:bg-blue-900/20 dark:text-blue-400 dark:border-blue-900/30': tx.type === 'MINT',
                      'bg-purple-50 text-purple-700 border-purple-100 dark:bg-purple-900/20 dark:text-purple-400 dark:border-purple-900/30': tx.type === 'BUY',
                      'bg-slate-50 text-slate-700 border-slate-100 dark:bg-slate-800 dark:text-slate-400 dark:border-slate-700': !tx.type || (tx.type !== 'MINT' && tx.type !== 'BUY')
                    }"
                   >
                     {{ tx.type || 'TRANSFER' }}
                   </span>
                </td>

                <!-- Time -->
                <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-500 dark:text-slate-400">
                  {{ formatTimestamp(tx.createTime) }}
                </td>
              </tr>
              <tr v-if="transactionList.length === 0 && !loading">
                <td colspan="7" class="px-6 py-16 text-center text-slate-500 dark:text-slate-400">
                  <div class="flex flex-col items-center justify-center">
                    <div class="bg-slate-50 dark:bg-slate-800 p-4 rounded-full mb-4">
                        <svg class="w-10 h-10 text-slate-300 dark:text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path></svg>
                    </div>
                    <p class="text-lg font-medium">暂无交易记录</p>
                    <p class="text-sm mt-1 opacity-70">区块链网络上暂时没有查询到相关数据</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- Pagination -->
        <div class="bg-white dark:bg-slate-900 px-4 py-4 flex items-center justify-between border-t border-slate-100 dark:border-slate-800 sm:px-6" v-if="total > 0">
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-slate-700 dark:text-slate-300">
                显示第 <span class="font-bold">{{ (page - 1) * pageSize + 1 }}</span> 到 <span class="font-bold">{{ Math.min(page * pageSize, total) }}</span> 条，共 <span class="font-bold">{{ total }}</span> 条
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-xl shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="handlePageChange(page - 1)"
                  :disabled="page === 1"
                  class="relative inline-flex items-center px-3 py-2 rounded-l-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-medium text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  <span class="sr-only">上一页</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <button
                  @click="handlePageChange(page + 1)"
                  :disabled="page * pageSize >= total"
                  class="relative inline-flex items-center px-3 py-2 rounded-r-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-medium text-slate-500 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  <span class="sr-only">下一页</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAllNFTTransactions, getTransactionStats } from '@/api/nft'
import * as echarts from 'echarts'

const loading = ref(false)
const transactionList = ref<any[]>([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// Chart
const chartRef = ref<HTMLElement | null>(null)
const statsType = ref('day')
let chartInstance: echarts.ECharts | null = null

// 格式化价格
const formatPrice = (price: number) => {
  return price ? price.toFixed(4) : '0.0000'
}

// 格式化时间戳
const formatTimestamp = (timestamp: string) => {
  if (!timestamp) return ''
  return new Date(timestamp).toLocaleString()
}

// 复制到剪贴板
const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success('已复制到剪贴板')
  } catch (err) {
    ElMessage.error('复制失败')
  }
}

// 获取交易历史
const fetchTransactions = async () => {
  loading.value = true
  try {
    const res = await getAllNFTTransactions({ 
      page: page.value, 
      pageSize: pageSize.value 
    })
    if (res.data) {
      transactionList.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error('获取交易历史失败:', error)
    ElMessage.error('获取交易历史失败')
  } finally {
    loading.value = false
  }
}

const handlePageChange = (newPage: number) => {
  page.value = newPage
  fetchTransactions()
}

// Chart Logic
const initChart = () => {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  updateChart()
  window.addEventListener('resize', () => chartInstance?.resize())
}

const updateChart = async () => {
  if (!chartInstance) return
  chartInstance.showLoading()
  try {
    const res = await getTransactionStats(statsType.value as any)
    const data = res.data || []
    
    const dates = data.map((item: any) => item.date)
    const counts = data.map((item: any) => item.count)
    const volumes = data.map((item: any) => item.volume)

    const isDark = document.documentElement.classList.contains('dark')
    const textColor = isDark ? '#94a3b8' : '#64748b'
    const axisLineColor = isDark ? '#334155' : '#e2e8f0'
    const splitLineColor = isDark ? '#1e293b' : '#f1f5f9'

    const option = {
      backgroundColor: 'transparent',
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'cross' },
        backgroundColor: isDark ? 'rgba(15, 23, 42, 0.9)' : 'rgba(255, 255, 255, 0.9)',
        borderColor: isDark ? '#334155' : '#e2e8f0',
        textStyle: { color: textColor }
      },
      legend: {
        data: ['交易量 (笔)', '交易金额 (ETH)'],
        textStyle: { color: textColor },
        bottom: 0
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '10%',
        top: '10%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: dates,
        axisLine: { lineStyle: { color: axisLineColor } },
        axisLabel: { color: textColor }
      },
      yAxis: [
        {
          type: 'value',
          name: '交易量',
          position: 'left',
          axisLine: { show: true, lineStyle: { color: axisLineColor } },
          axisLabel: { color: textColor },
          splitLine: { lineStyle: { color: splitLineColor, type: 'dashed' } }
        },
        {
          type: 'value',
          name: '金额',
          position: 'right',
          axisLine: { show: true, lineStyle: { color: axisLineColor } },
          axisLabel: { color: textColor },
          splitLine: { show: false }
        }
      ],
      series: [
        {
          name: '交易量 (笔)',
          type: 'line',
          smooth: true,
          data: counts,
          itemStyle: { color: '#6366f1' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(99, 102, 241, 0.5)' },
              { offset: 1, color: 'rgba(99, 102, 241, 0.0)' }
            ])
          }
        },
        {
          name: '交易金额 (ETH)',
          type: 'line',
          yAxisIndex: 1,
          smooth: true,
          data: volumes,
          itemStyle: { color: '#10b981' },
           areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(16, 185, 129, 0.5)' },
              { offset: 1, color: 'rgba(16, 185, 129, 0.0)' }
            ])
          }
        }
      ]
    }
    chartInstance.setOption(option)
  } catch (e) {
    console.error(e)
  } finally {
    chartInstance.hideLoading()
  }
}

const changeStatsType = (type: string) => {
  statsType.value = type
  updateChart()
}

const observer = new MutationObserver(() => {
    if(chartInstance) {
        chartInstance.dispose()
        initChart()
    }
})

onMounted(() => {
  fetchTransactions()
  initChart()
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
})

onUnmounted(() => {
    observer.disconnect()
    window.removeEventListener('resize', () => chartInstance?.resize())
    chartInstance?.dispose()
})
</script>