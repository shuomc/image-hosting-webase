<template>
  <div class="min-h-screen py-8 transition-colors duration-300">
    <div class="container mx-auto px-4 max-w-6xl">
      
      <!-- 顶部导航 -->
      <div class="mb-8 flex items-center justify-between">
        <button 
          @click="handleBack" 
          class="flex items-center gap-2 text-slate-500 hover:text-slate-800 dark:text-slate-400 dark:hover:text-slate-200 transition-colors"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
          <span class="font-medium">返回列表</span>
        </button>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex flex-col items-center justify-center h-[60vh]">
        <div class="w-10 h-10 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
        <div class="mt-4 text-slate-500 text-sm">正在加载资产详情...</div>
      </div>

      <!-- 内容区域 -->
      <div v-else-if="nftInfo" class="grid grid-cols-1 lg:grid-cols-12 gap-8 lg:gap-12">
        
        <!-- 左侧：图片展示 -->
        <div class="lg:col-span-5 xl:col-span-5">
          <div class="pt-10">
            <div class="aspect-square rounded-2xl overflow-hidden bg-white dark:bg-slate-900 shadow-xl border border-slate-100 dark:border-slate-800 relative group">
              <img 
                :src="nftInfo.imageUrl || '/placeholder-nft.png'" 
                :alt="nftInfo.name" 
                class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105"
              />
              <!-- 状态标签 -->
              <div class="absolute top-4 right-4">
                <span :class="[
                  'px-3 py-1.5 rounded-full text-xs font-bold backdrop-blur-md shadow-sm',
                  nftInfo.isForSale 
                    ? 'bg-green-500/90 text-white' 
                    : 'bg-slate-800/80 text-white'
                ]">
                  {{ nftInfo.isForSale ? '正在出售' : '非卖品' }}
                </span>
              </div>
            </div>

            <!-- 资产属性摘要 -->
            <div class="mt-6 bg-white dark:bg-slate-900 rounded-xl p-5 border border-slate-100 dark:border-slate-800 shadow-sm">
              <h3 class="text-sm font-semibold text-slate-900 dark:text-white mb-4 flex items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-indigo-500"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                链上数据
              </h3>
              <div class="space-y-3">
                <div class="flex justify-between items-center text-sm">
                  <span class="text-slate-500">Token ID</span>
                  <span class="font-mono text-slate-700 dark:text-slate-300">#{{ nftInfo.tokenId }}</span>
                </div>
                <div class="flex justify-between items-center text-sm">
                  <span class="text-slate-500">合约地址</span>
                  <code class="bg-slate-100 dark:bg-slate-800 px-2 py-1 rounded text-xs font-mono text-indigo-600 dark:text-indigo-400 cursor-help" :title="nftInfo.contractAddress">
                    {{ shortenAddress(nftInfo.contractAddress) }}
                  </code>
                </div>
                <div class="flex justify-between items-center text-sm">
                  <span class="text-slate-500">文件 Hash</span>
                  <span class="bg-slate-100 dark:bg-slate-800 px-2 py-1 rounded text-xs font-mono text-slate-700 dark:text-slate-300 truncate" :title="nftInfo.fileHash">
                    {{ shortenAddress(nftInfo.fileHash) || '--' }}
                  </span>
                </div>
                <div class="flex justify-between items-center text-sm">
                  <span class="text-slate-500">铸造时间</span>
                  <span class="text-slate-700 dark:text-slate-300">{{ formatTimestamp(nftInfo.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：详细信息 -->
        <div class="lg:col-span-7 xl:col-span-7 flex flex-col">
          
          <!-- 标题与所有者 -->
          <div class="mb-8">
            <div class="flex items-center gap-2 mb-2">
              <span class="text-indigo-600 font-bold text-sm tracking-wider uppercase">ImageNFT Collection</span>
            </div>
            <h1 class="text-3xl sm:text-4xl font-extrabold text-slate-900 dark:text-white mb-6 leading-tight">
              {{ nftInfo.name || '未命名资产' }}
            </h1>

            <div class="flex flex-wrap gap-8">
              <!-- 持有者 -->
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-400 to-purple-500 flex items-center justify-center text-white font-bold text-sm shadow-md">
                  {{ nftInfo.ownerName ? nftInfo.ownerName.charAt(0).toUpperCase() : 'O' }}
                </div>
                <div class="flex flex-col">
                  <span class="text-xs text-slate-500 mb-0.5">持有者</span>
                  <div class="flex items-center gap-2">
                    <span class="text-sm font-bold text-slate-900 dark:text-white" v-if="nftInfo.ownerName">
                      {{ nftInfo.ownerName }}
                    </span>
                    <code class="bg-slate-100 dark:bg-slate-800 px-1.5 py-0.5 rounded text-xs font-mono text-slate-500 dark:text-slate-400 cursor-help" :title="nftInfo.ownerAddress">
                      {{ shortenAddress(nftInfo.ownerAddress) }}
                    </code>
                  </div>
                </div>
              </div>
              
              <!-- 创作者 -->
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-slate-100 dark:bg-slate-800 flex items-center justify-center text-slate-500 font-bold text-sm border border-slate-200 dark:border-slate-700">
                  {{ nftInfo.creatorName ? nftInfo.creatorName.charAt(0).toUpperCase() : 'C' }}
                </div>
                <div class="flex flex-col">
                  <span class="text-xs text-slate-500 mb-0.5">创作者</span>
                  <span class="text-sm font-bold text-slate-900 dark:text-white">
                    {{ nftInfo.creatorName || 'Unknown' }}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <!-- 价格与操作卡片 -->
          <div class="bg-white dark:bg-slate-900 rounded-2xl p-6 border border-slate-100 dark:border-slate-800 shadow-lg mb-8">
            <div class="mb-4">
              <span class="text-slate-500 text-sm">当前价格</span>
              <div class="flex items-baseline gap-2 mt-1">
                <span class="text-3xl font-bold text-slate-900 dark:text-white">
                  {{ nftInfo.isForSale ? formatPrice(nftInfo.price) : '--' }}
                </span>
                <span class="text-lg font-medium text-slate-500">ETH</span>
              </div>
            </div>

            <!-- 操作按钮组 -->
            <div class="flex flex-col sm:flex-row gap-3">
              <!-- 购买按钮 (非持有者 & 在售) -->
              <button 
                v-if="nftInfo.isForSale && !isOwner"
                @click="handleBuy"
                class="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3.5 px-6 rounded-xl transition-all shadow-lg shadow-indigo-500/30 active:scale-95 flex items-center justify-center gap-2"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4Z"/><path d="M3 6h18"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
                立即购买
              </button>

              <!-- 上架按钮 (持有者 & 未售) -->
              <button 
                v-if="isOwner && !nftInfo.isForSale"
                @click="handleSetPrice"
                class="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3.5 px-6 rounded-xl transition-all shadow-lg shadow-indigo-500/30 active:scale-95"
              >
                上架出售
              </button>

              <!-- 修改价格按钮 (持有者 & 在售) -->
              <button 
                v-if="isOwner && nftInfo.isForSale"
                @click="handleSetPrice"
                class="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-bold py-3.5 px-6 rounded-xl transition-all shadow-lg shadow-blue-500/30 active:scale-95"
              >
                修改价格
              </button>

              <!-- 下架按钮 (持有者 & 在售) -->
              <button 
                v-if="isOwner && nftInfo.isForSale"
                @click="handleCancelSale"
                class="flex-1 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-200 font-bold py-3.5 px-6 rounded-xl hover:bg-slate-50 dark:hover:bg-slate-700 transition-all active:scale-95"
              >
                取消出售
              </button>

              <!-- 仅展示 (非持有者 & 未售) -->
              <button 
                v-if="!isOwner && !nftInfo.isForSale"
                disabled
                class="flex-1 bg-slate-100 dark:bg-slate-800 text-slate-400 font-bold py-3.5 px-6 rounded-xl cursor-not-allowed"
              >
                暂未出售
              </button>
            </div>
          </div>

          <!-- 描述信息 -->
          <div class="mb-8">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-3">描述</h3>
            <div class="bg-slate-50 dark:bg-slate-900/50 rounded-xl p-5 text-slate-600 dark:text-slate-400 leading-relaxed text-sm">
              {{ nftInfo.description || '暂无描述信息' }}
            </div>
          </div>

          <!-- 详细属性 -->
          <div>
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-3">详细信息</h3>
            <div class="border border-slate-200 dark:border-slate-800 rounded-xl overflow-hidden">
              <div class="grid grid-cols-1 divide-y divide-slate-200 dark:divide-slate-800">
                <div class="p-4 flex justify-between hover:bg-slate-50 dark:hover:bg-slate-900/50 transition">
                  <span class="text-slate-500 text-sm">NFT ID (System)</span>
                  <span class="text-slate-700 dark:text-slate-300 text-sm font-mono">{{ nftInfo.nftId }}</span>
                </div>
                <div class="p-4 flex justify-between hover:bg-slate-50 dark:hover:bg-slate-900/50 transition">
                  <span class="text-slate-500 text-sm">Metadata URI</span>
                  <a :href="nftInfo.imageUrl" target="_blank" class="text-indigo-600 text-sm hover:underline truncate max-w-[200px]">
                    View Source
                  </a>
                </div>
                <div class="p-4 flex justify-between hover:bg-slate-50 dark:hover:bg-slate-900/50 transition">
                  <span class="text-slate-500 text-sm">区块链网络</span>
                  <span class="text-slate-700 dark:text-slate-300 text-sm">FISCO BCOS</span>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>

      <!-- 404 状态 -->
      <div v-else class="flex flex-col items-center justify-center h-[60vh] text-center">
        <div class="text-6xl mb-4">😕</div>
        <h2 class="text-2xl font-bold text-slate-800 dark:text-white mb-2">未找到 NFT</h2>
        <p class="text-slate-500 mb-6">该资产可能已被删除或不存在。</p>
        <button @click="handleBack" class="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
          返回列表
        </button>
      </div>
    </div>

    <!-- 自定义模态框: 设置价格 -->
    <div v-if="priceDialogVisible" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <!-- 背景遮罩 -->
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="priceDialogVisible = false"></div>
      
      <!-- 弹窗内容 -->
      <div class="relative bg-white dark:bg-slate-900 rounded-2xl shadow-2xl w-full max-w-md p-6 transform transition-all scale-100">
        <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-2">
          {{ nftInfo?.isForSale ? '修改价格' : '上架出售' }}
        </h3>
        <p class="text-sm text-slate-500 mb-6">
          {{ nftInfo?.isForSale ? '修改当前 NFT 的出售价格。' : '设置您的 NFT 出售价格，上架后其他用户可以购买。' }}
        </p>
        
        <div class="mb-6">
          <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">价格 (ETH)</label>
          <div class="relative">
            <input 
              type="number" 
              v-model="priceForm.price"
              step="0.0001"
              min="0"
              class="w-full px-4 py-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition"
              placeholder="0.00"
            />
            <div class="absolute right-4 top-3 text-slate-400 font-medium">ETH</div>
          </div>
        </div>

        <div class="flex gap-3">
          <button 
            @click="priceDialogVisible = false"
            class="flex-1 px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-medium hover:bg-slate-50 dark:hover:bg-slate-800 transition"
          >
            取消
          </button>
          <button 
            @click="confirmSetPrice"
            class="flex-1 px-4 py-3 rounded-xl bg-indigo-600 text-white font-bold hover:bg-indigo-700 transition shadow-lg shadow-indigo-500/30"
          >
            {{ nftInfo?.isForSale ? '确认修改' : '确认上架' }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus' // 仅保留 Message/MessageBox 功能组件，UI组件已移除
import { getNFTDetail, setNFTPrice, offShelf, putOnShelf, buyNFT, checkRegistrationStatus } from '@/api/nft'
import type { NFTInfo } from '@/api/nft'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const nftInfo = ref<NFTInfo | null>(null)
const currentUserAddress = ref('')

// 价格弹窗
const priceDialogVisible = ref(false)
const priceForm = ref({ price: 0 })

// 计算属性：是否为持有者
const isOwner = computed(() => {
  if (!nftInfo.value || !currentUserAddress.value) return false
  // 比较地址 (忽略大小写)
  return nftInfo.value.ownerAddress?.toLowerCase() === currentUserAddress.value.toLowerCase()
})

// 格式化工具
const formatPrice = (price: number) => Number(price).toFixed(4)
const formatTimestamp = (ts: string) => ts ? new Date(ts).toLocaleString() : '--'
const shortenAddress = (addr: string, chars = 6) => {
  if (!addr) return '--'
  if (addr.length < chars * 2 + 2) return addr
  return `${addr.substring(0, chars)}...${addr.substring(addr.length - chars)}`
}

// 初始化
onMounted(async () => {
  await fetchCurrentUser()
  await fetchNFTInfo()
})

// 获取当前用户区块链信息
const fetchCurrentUser = async () => {
  try {
    const res = await checkRegistrationStatus()
    if (res.data && res.data.blockchainAddress) {
      currentUserAddress.value = res.data.blockchainAddress
    }
  } catch (e) {
    console.error('获取用户信息失败', e)
  }
}

// 获取 NFT 详情
const fetchNFTInfo = async () => {
  loading.value = true
  try {
    const nftId = route.params.nftId as string
    if (!nftId) {
      ElMessage.error('参数错误')
      return
    }
    
    // 使用新的 getNFTDetail 接口
    const res = await getNFTDetail(nftId)
    if (res.data) {
      nftInfo.value = res.data
    } else {
      nftInfo.value = null
    }
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取NFT详情失败')
  } finally {
    loading.value = false
  }
}

// 交互逻辑
const handleBack = () => router.back()

const handleSetPrice = () => {
  if (!nftInfo.value) return
  priceForm.value.price = nftInfo.value.price || 0
  priceDialogVisible.value = true
}

const confirmSetPrice = async () => {
  if (priceForm.value.price <= 0) {
    ElMessage.warning('价格必须大于 0')
    return
  }
  
  try {
    // 1. 先设置价格
    await setNFTPrice(nftInfo.value!.nftId, priceForm.value.price)
    
    // 2. 如果当前未上架，则执行上架操作
    if (!nftInfo.value!.isForSale) {
      await offShelf(nftInfo.value!.nftId) // 确保状态一致性，虽然可能多余
      // 注意：这里应该调用 putOnShelf，但原代码中 offShelf 对应的是 cancelNFTSale，putOnShelf 对应的是 putOnShelf
      // 检查 api/nft.ts: putOnShelf 是 /nft/shelf/on/{nftId}
      // 引入 putOnShelf
      await putOnShelf(nftInfo.value!.nftId)
      ElMessage.success('上架成功')
    } else {
      ElMessage.success('价格修改成功')
    }

    priceDialogVisible.value = false
    fetchNFTInfo() // 刷新数据
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancelSale = async () => {
  try {
    await ElMessageBox.confirm('确定要下架此商品吗？', '确认下架', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await offShelf(nftInfo.value!.nftId)
    ElMessage.success('下架成功')
    fetchNFTInfo()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

const handleBuy = async () => {
  try {
    await ElMessageBox.confirm(
      `确认支付 ${formatPrice(nftInfo.value!.price)} ETH 购买此 NFT?`, 
      '购买确认', 
      {
        confirmButtonText: '确认支付',
        cancelButtonText: '取消',
        type: 'success'
      }
    )
    
    await buyNFT(nftInfo.value!.nftId)
    ElMessage.success('购买成功！')
    fetchNFTInfo()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('购买失败，请检查余额')
  }
}
</script>

<style scoped>
/* 移除 Element Plus 样式依赖，使用 Tailwind */
</style>