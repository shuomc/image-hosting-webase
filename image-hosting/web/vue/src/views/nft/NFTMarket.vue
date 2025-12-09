<template>
  <div class="w-full min-h-screen transition-colors duration-300 pt-2 pb-10">
    
    <!-- 顶部导航/控制栏 -->
    <div class="fixed top-24 right-4 sm:right-8 z-40 flex items-center gap-3 p-1.5 rounded-3xl bg-white/80 dark:bg-slate-900/80 backdrop-blur-md border border-slate-200/60 dark:border-slate-700/60 shadow-xl transition-all duration-300">
      <!-- 视图切换 -->
      <div class="flex bg-slate-100 dark:bg-slate-800 rounded-xl p-1">
        <button @click="currentLayout = 'grid'" :class="layoutBtnClass('grid')" title="网格视图">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="7" height="7" x="3" y="3" rx="1"/><rect width="7" height="7" x="14" y="3" rx="1"/><rect width="7" height="7" x="14" y="14" rx="1"/><rect width="7" height="7" x="3" y="14" rx="1"/></svg>
        </button>
        <button @click="currentLayout = 'list'" :class="layoutBtnClass('list')" title="列表视图">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" x2="21" y1="6" y2="6"/><line x1="8" x2="21" y1="12" y2="12"/><line x1="8" x2="21" y1="18" y2="18"/><line x1="3" x2="3.01" y1="6" y2="6"/><line x1="3" x2="3.01" y1="12" y2="12"/><line x1="3" x2="3.01" y1="18" y2="18"/></svg>
        </button>
      </div>
    </div>

    <div class="container mx-auto px-4 max-w-7xl mt-8">
      
      <!-- 页面标题 -->
      <div class="mb-10 text-center sm:text-left">
        <h1 class="text-4xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-pink-500 via-red-500 to-yellow-500 dark:from-pink-400 dark:via-red-400 dark:to-yellow-400">
          NFT 交易市场
        </h1>
        <p class="mt-3 text-slate-500 dark:text-slate-400 text-lg">
          探索、购买和出售独特的数字资产
        </p>
      </div>

      <!-- 1. 我正在出售的 NFT (My Listings) -->
      <div v-if="mySellingList.length > 0" class="mb-16">
        <div class="flex items-center gap-3 mb-6">
          <div class="p-2 bg-indigo-100 dark:bg-indigo-900/30 rounded-lg text-indigo-600 dark:text-indigo-400">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m12 14 4-4"/><path d="M3.34 19a10 10 0 1 1 17.32 0"/></svg>
          </div>
          <h2 class="text-2xl font-bold text-slate-800 dark:text-white">我的上架商品</h2>
        </div>

        <div :class="gridClass">
          <div v-for="nft in mySellingList" :key="nft.nftId" 
               class="group relative bg-white dark:bg-slate-800 rounded-2xl shadow-sm border-2 border-indigo-100 dark:border-indigo-900/50 overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:shadow-xl cursor-pointer flex flex-col"
               @click="handleViewDetail(nft)">
            
            <!-- 图片 -->
            <div class="relative w-full aspect-square bg-slate-100 dark:bg-slate-900 overflow-hidden">
              <img :src="nft.imageUrl || '/placeholder-nft.png'" :alt="nft.name" class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" loading="lazy" />
              <div class="absolute top-3 right-3">
                <span class="bg-indigo-600 text-white text-[10px] font-bold px-2 py-1 rounded-full shadow-sm">
                  正在出售
                </span>
              </div>
            </div>

            <!-- 信息 -->
            <div class="p-4 flex-1 flex flex-col justify-between">
              <div>
                <div class="flex justify-between items-start mb-1">
                  <h3 class="font-bold text-slate-800 dark:text-white truncate pr-2">{{ nft.name }}</h3>
                  <span class="text-xs font-mono text-slate-400">#{{ nft.tokenId }}</span>
                </div>
              </div>
              <div class="mt-4 pt-3 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between">
                <div class="flex flex-col">
                  <span class="text-[10px] text-slate-400 uppercase tracking-wider">售价</span>
                  <span class="text-sm font-bold text-indigo-600 dark:text-indigo-400">{{ formatPrice(nft.price) }} ETH</span>
                </div>
                <div class="flex gap-1">
                  <button @click.stop="handleSetPrice(nft)" class="p-2 text-blue-600 hover:bg-blue-50 rounded-lg" title="修改价格">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
                  </button>
                  <button @click.stop="handleOffShelf(nft)" class="p-2 text-red-500 hover:bg-red-50 rounded-lg" title="下架">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 2. 市场探索 (Marketplace) -->
      <div>
        <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-6">
          <div class="flex items-center gap-3">
            <div class="p-2 bg-pink-100 dark:bg-pink-900/30 rounded-lg text-pink-600 dark:text-pink-400">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M16 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8"/><path d="M12 18V6"/></svg>
            </div>
            <h2 class="text-2xl font-bold text-slate-800 dark:text-white">市场探索</h2>
          </div>
          
          <!-- 搜索/筛选 -->
          <div class="flex gap-2">
            <div class="relative">
              <input 
                v-model="searchQuery" 
                @keyup.enter="handleSearch"
                type="text" 
                placeholder="搜索 NFT..." 
                class="pl-10 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm focus:ring-2 focus:ring-indigo-500 outline-none w-full sm:w-64"
              />
              <svg xmlns="http://www.w3.org/2000/svg" class="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
            </div>
            <select v-model="sortOption" @change="handleSearch" class="px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm outline-none focus:ring-2 focus:ring-indigo-500">
              <option value="new">最新上架</option>
              <option value="price_asc">价格: 低到高</option>
              <option value="price_desc">价格: 高到低</option>
            </select>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="w-full h-64 flex flex-col items-center justify-center gap-3">
          <div class="w-8 h-8 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
          <div class="text-slate-400 text-sm">加载市场数据...</div>
        </div>

        <div v-else>
          <!-- 空状态 -->
          <div v-if="marketList.length === 0" class="flex flex-col items-center justify-center py-20 text-center">
            <div class="bg-slate-100 dark:bg-slate-800 p-6 rounded-full mb-4">
               <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="text-slate-400"><path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4Z"/><path d="M3 6h18"/><path d="M16 10a4 4 0 0 1-8 0"/></svg>
            </div>
            <h3 class="text-lg font-semibold text-slate-700 dark:text-slate-200">市场空空如也</h3>
            <p class="text-slate-500 dark:text-slate-400 mt-2 text-sm">暂时没有其他用户出售 NFT。</p>
          </div>

          <!-- 列表内容 -->
          <template v-else>
            <!-- Grid View -->
            <div v-if="currentLayout === 'grid'" :class="gridClass">
              <div v-for="nft in marketList" :key="nft.nftId" 
                   class="group relative bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:shadow-xl cursor-pointer flex flex-col"
                   @click="handleViewDetail(nft)">
                
                <div class="relative w-full aspect-square bg-slate-100 dark:bg-slate-900 overflow-hidden">
                  <img :src="nft.imageUrl || '/placeholder-nft.png'" :alt="nft.name" class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" loading="lazy" />
                  
                  <!-- 遮罩 -->
                  <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
                  
                  <!-- 购买按钮悬浮 -->
                  <div class="absolute left-0 right-0 bottom-0 p-4 translate-y-4 group-hover:translate-y-0 transition-transform duration-300 opacity-0 group-hover:opacity-100">
                    <button class="w-full py-2 bg-white text-slate-900 font-bold rounded-lg shadow-lg hover:bg-slate-50 transition">
                      立即购买
                    </button>
                  </div>
                </div>

                <div class="p-4 flex-1 flex flex-col justify-between">
                  <div>
                    <div class="flex justify-between items-start mb-1">
                      <h3 class="font-bold text-slate-800 dark:text-white truncate pr-2">{{ nft.name }}</h3>
                      <span class="text-xs font-mono text-slate-400">#{{ nft.tokenId }}</span>
                    </div>
                    <div class="flex items-center gap-2 mt-2">
                      <div class="w-5 h-5 rounded-full bg-gradient-to-br from-purple-400 to-blue-400 flex items-center justify-center text-[10px] text-white font-bold">
                        {{ nft.ownerName ? nft.ownerName.charAt(0).toUpperCase() : 'U' }}
                      </div>
                      <span class="text-xs text-slate-500 truncate max-w-[100px]">{{ nft.ownerName || shortenAddress(nft.ownerAddress) }}</span>
                    </div>
                  </div>
                  <div class="mt-4 pt-3 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between">
                    <div class="flex flex-col">
                      <span class="text-[10px] text-slate-400 uppercase tracking-wider">价格</span>
                      <span class="text-sm font-bold text-slate-900 dark:text-white">{{ formatPrice(nft.price) }} ETH</span>
                    </div>
                    <span class="text-xs text-slate-400">{{ formatTimestamp(nft.createTime).split(' ')[0] }}</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- List View -->
            <div v-else class="overflow-hidden rounded-2xl border border-slate-200 dark:border-slate-700 shadow-sm bg-white dark:bg-slate-800">
              <div class="overflow-x-auto">
                <table class="w-full text-sm text-left text-slate-500 dark:text-slate-400">
                  <thead class="text-xs text-slate-700 uppercase bg-slate-50 dark:bg-slate-900/50 dark:text-slate-300 border-b border-slate-200 dark:border-slate-700">
                    <tr>
                      <th class="px-6 py-4">资产</th>
                      <th class="px-6 py-4">名称</th>
                      <th class="px-6 py-4">价格</th>
                      <th class="px-6 py-4">持有者</th>
                      <th class="px-6 py-4">上架时间</th>
                      <th class="px-6 py-4 text-right">操作</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y divide-slate-100 dark:divide-slate-700/50">
                    <tr v-for="nft in marketList" :key="nft.nftId" class="bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors cursor-pointer" @click="handleViewDetail(nft)">
                      <td class="px-6 py-3">
                        <div class="w-10 h-10 rounded-lg overflow-hidden bg-slate-100 dark:bg-slate-900">
                          <img :src="nft.imageUrl || '/placeholder-nft.png'" class="w-full h-full object-cover">
                        </div>
                      </td>
                      <td class="px-6 py-3 font-medium text-slate-900 dark:text-white">
                        {{ nft.name }} <span class="text-slate-400 font-normal ml-1">#{{ nft.tokenId }}</span>
                      </td>
                      <td class="px-6 py-3 font-bold text-slate-900 dark:text-white">
                        {{ formatPrice(nft.price) }} ETH
                      </td>
                      <td class="px-6 py-3">
                        <div class="flex items-center gap-2">
                          <span class="text-xs font-mono bg-slate-100 dark:bg-slate-700 px-1.5 py-0.5 rounded">
                            {{ shortenAddress(nft.ownerAddress) }}
                          </span>
                        </div>
                      </td>
                      <td class="px-6 py-3 text-xs">
                        {{ formatTimestamp(nft.createTime) }}
                      </td>
                      <td class="px-6 py-3 text-right">
                        <button class="text-indigo-600 hover:text-indigo-700 font-medium text-xs">购买</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- 分页 -->
            <div class="mt-8 flex justify-end" v-if="total > 0">
              <div class="flex items-center gap-2">
                <button @click="handlePageChange(currentPage - 1)" :disabled="currentPage === 1" class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition">上一页</button>
                <span class="text-sm text-slate-500 dark:text-slate-400">第 {{ currentPage }} 页</span>
                <button @click="handlePageChange(currentPage + 1)" :disabled="marketList.length < pageSize" class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition">下一页</button>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 修改价格弹窗 -->
    <div v-if="priceDialogVisible" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="priceDialogVisible = false"></div>
      <div class="relative bg-white dark:bg-slate-900 rounded-2xl shadow-2xl w-full max-w-md p-6 transform transition-all scale-100">
        <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-2">修改价格</h3>
        <p class="text-sm text-slate-500 mb-6">调整您的 NFT 出售价格。</p>
        <div class="mb-6">
          <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">新价格 (ETH)</label>
          <div class="relative">
            <input type="number" v-model="priceForm.price" step="0.0001" min="0.0001" class="w-full px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500 outline-none transition" />
            <div class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">ETH</div>
          </div>
        </div>
        <div class="flex gap-3">
          <button @click="priceDialogVisible = false" class="flex-1 px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-medium hover:bg-slate-50 dark:hover:bg-slate-800 transition">取消</button>
          <button @click="confirmSetPrice" class="flex-1 px-4 py-3 rounded-xl bg-indigo-600 text-white font-bold hover:bg-indigo-700 shadow-lg shadow-indigo-500/30 transition">确认修改</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNFTList, getMyNFTs, setNFTPrice, offShelf, checkRegistrationStatus } from '@/api/nft'
import type { NFTInfo } from '@/api/nft'

const router = useRouter()

// 状态
const currentUserAddress = ref('')
const mySellingList = ref<NFTInfo[]>([])
const marketList = ref<NFTInfo[]>([])
const loading = ref(false)
const currentLayout = ref<'grid' | 'list'>('grid')

// 筛选与分页
const searchQuery = ref('')
const sortOption = ref('new')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 弹窗
const priceDialogVisible = ref(false)
const selectedNFT = ref<NFTInfo | null>(null)
const priceForm = ref({ price: 0 })

// 样式计算
const gridClass = computed(() => {
  return 'grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6'
})

const layoutBtnClass = (layout: string) => {
  return [
    'flex items-center justify-center w-9 h-8 rounded-xl transition-all duration-200',
    currentLayout.value === layout
      ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-sm'
      : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'
  ]
}

// 初始化
onMounted(async () => {
  await fetchCurrentUser()
  await fetchMySelling()
  await fetchMarket()
})

// 1. 获取当前用户
const fetchCurrentUser = async () => {
  try {
    const res = await checkRegistrationStatus()
    if (res.data && res.data.blockchainAddress) {
      currentUserAddress.value = res.data.blockchainAddress
    }
  } catch (e) {
    console.error('获取用户失败', e)
  }
}

// 2. 获取我正在出售的
const fetchMySelling = async () => {
  try {
    // 获取我持有的所有 NFT
    const res = await getMyNFTs({ page: 1, pageSize: 100, mode: 'owned' })
    if (res.data && res.data.list) {
      // 筛选出 isForSale = true 的
      mySellingList.value = res.data.list.filter((item: NFTInfo) => item.isForSale)
    }
  } catch (e) {
    console.error('获取我的出售列表失败', e)
  }
}

// 3. 获取市场列表 (排除自己)
const fetchMarket = async () => {
  loading.value = true
  try {
    const res = await getNFTList({
      page: currentPage.value,
      pageSize: pageSize.value,
      query: searchQuery.value,
      sort: sortOption.value
    })
    
    if (res.data) {
      const list = res.data.list || []
      total.value = res.data.total || 0
      
      // 客户端过滤掉自己的 (如果后端没过滤)
      // 注意：如果后端分页是包含自己的，这里过滤会导致每页数量不一致，但为了简单起见先这样做
      if (currentUserAddress.value) {
        marketList.value = list.filter((item: NFTInfo) => 
          !item.ownerAddress || item.ownerAddress.toLowerCase() !== currentUserAddress.value.toLowerCase()
        )
      } else {
        marketList.value = list
      }
    }
  } catch (e) {
    console.error('获取市场列表失败', e)
  } finally {
    loading.value = false
  }
}

// 交互
const handleSearch = () => {
  currentPage.value = 1
  fetchMarket()
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchMarket()
}

const handleViewDetail = (nft: NFTInfo) => {
  router.push(`/nft/detail/${nft.nftId}`)
}

// 操作：修改价格
const handleSetPrice = (nft: NFTInfo) => {
  selectedNFT.value = nft
  priceForm.value.price = nft.price
  priceDialogVisible.value = true
}

const confirmSetPrice = async () => {
  if (!selectedNFT.value) return
  try {
    await setNFTPrice(selectedNFT.value.nftId, priceForm.value.price)
    ElMessage.success('价格修改成功')
    priceDialogVisible.value = false
    fetchMySelling() // 刷新我的列表
    fetchMarket()    // 刷新市场列表(价格变动)
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

// 操作：下架
const handleOffShelf = async (nft: NFTInfo) => {
  try {
    await ElMessageBox.confirm('确定要下架此商品吗？', '下架确认', {
      confirmButtonText: '确认下架',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await offShelf(nft.nftId)
    ElMessage.success('下架成功')
    fetchMySelling()
    fetchMarket()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

// 工具
const formatPrice = (price: number) => Number(price).toFixed(4)
const formatTimestamp = (ts: string) => ts ? new Date(ts).toLocaleString() : '--'
const shortenAddress = (addr: string, chars = 4) => {
  if (!addr) return '--'
  if (addr.length < chars * 2 + 2) return addr
  return `${addr.substring(0, chars)}...${addr.substring(addr.length - chars)}`
}
</script>

<style scoped>
/* 滚动条美化 (可选) */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}
::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
.dark ::-webkit-scrollbar-thumb {
  background: #475569;
}
.dark ::-webkit-scrollbar-thumb:hover {
  background: #64748b;
}
</style>
