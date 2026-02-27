<template>
  <div class="w-full min-h-screen transition-colors duration-300 pt-2 pb-10">

    <!-- 全局加载状态 (检查注册中) -->
    <div v-if="checkingStatus" class="flex flex-col items-center justify-center h-[80vh]">
      <div class="w-10 h-10 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
      <div class="mt-4 text-slate-500 text-sm font-medium">正在连接区块链网络...</div>
    </div>

    <!-- 状态 1: 未注册/未激活 (引导页) -->
    <div v-else-if="!isRegistered" class="flex flex-col items-center justify-center min-h-[80vh] px-4">
      <div class="max-w-md w-full bg-white dark:bg-slate-900 rounded-3xl shadow-xl border border-slate-100 dark:border-slate-800 p-8 text-center relative overflow-hidden">
        <!-- 背景装饰 -->
        <div class="absolute top-0 left-0 w-full h-2 bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"></div>
        
        <div class="w-20 h-20 mx-auto bg-indigo-50 dark:bg-indigo-900/30 rounded-full flex items-center justify-center mb-6">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-indigo-600 dark:text-indigo-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.384-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
          </svg>
        </div>

        <h2 class="text-2xl font-bold text-slate-800 dark:text-white mb-3">开启 Web3 之旅</h2>
        <p class="text-slate-500 dark:text-slate-400 mb-8 leading-relaxed">
          检测到您尚未激活区块链账户。激活后，您将获得一个专属的区块链钱包地址，用于存储和交易 NFT 数字资产。
        </p>

        <button 
          @click="handleRegister" 
          :disabled="registering"
          class="w-full py-3.5 px-6 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 text-white font-semibold shadow-lg shadow-indigo-500/30 transition-all transform active:scale-95 flex items-center justify-center gap-2"
        >
          <svg v-if="registering" class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          <span>{{ registering ? '正在链上注册...' : '一键激活账户' }}</span>
        </button>
        
        <p class="mt-4 text-xs text-slate-400">
          此操作将在联盟链上为您生成密钥对，过程安全且不可逆。
        </p>
      </div>
    </div>

    <!-- 状态 2: 已注册 (NFT 列表) -->
    <div v-else class="relative">
      
      <!-- 顶部悬浮控制栏 -->
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

        <!-- 分割线 -->
        <div class="w-px h-6 bg-slate-200 dark:bg-slate-700"></div>

        <!-- 模式切换 (持有/创建) -->
        <div class="flex bg-slate-100 dark:bg-slate-800 rounded-xl p-1 text-xs font-medium">
           <button 
             v-for="mode in ['owned', 'created']" 
             :key="mode"
             @click="handleModeChange(mode)"
             :class="[
               'px-3 py-1.5 rounded-xl transition-all duration-200',
               viewMode === mode 
                 ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-sm' 
                 : 'text-slate-500 hover:text-slate-700'
             ]"
           >
             {{ mode === 'owned' ? '我持有的' : '我铸造的' }}
           </button>
        </div>
      </div>

      <div class="container mx-auto px-4 max-w-7xl mt-8">
        
        <!-- 标题区 -->
        <div class="mb-10 flex justify-between items-end">
          <div>
            <h1 class="text-3xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-indigo-600 to-purple-600 dark:from-indigo-400 dark:to-purple-400">
              我的数字资产
            </h1>
            <p class="text-sm mt-2 text-slate-500 dark:text-slate-400 flex items-center gap-2">
              <span>钱包地址:</span>
              <span class="font-mono bg-slate-100 dark:bg-slate-800 px-2 py-0.5 rounded text-slate-600 dark:text-slate-300 select-all">
                {{ blockchainAddress }}
              </span>
            </p>
          </div>

          <!-- 注销按钮 -->
          <button 
            @click="handleDeregister" 
            class="text-xs text-slate-400 hover:text-red-500 transition-colors flex items-center gap-1 group"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
            </svg>
            <span class="opacity-0 group-hover:opacity-100 transition-opacity">注销区块链账户</span>
          </button>
        </div>

        <!-- 列表加载中 -->
        <div v-if="loading" class="w-full h-64 flex flex-col items-center justify-center gap-3">
          <div class="w-8 h-8 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
          <div class="text-slate-400 text-sm">加载资产中...</div>
        </div>

        <!-- 内容区 -->
        <div v-else>
          <!-- 空状态 -->
          <div v-if="nftList.length === 0" class="flex flex-col items-center justify-center py-20 text-center">
            <div class="bg-slate-100 dark:bg-slate-800 p-6 rounded-full mb-4">
               <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="text-slate-400"><rect width="18" height="18" x="3" y="3" rx="2" ry="2"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
            </div>
            <h3 class="text-lg font-semibold text-slate-700 dark:text-slate-200">暂无 NFT</h3>
            <p class="text-slate-500 dark:text-slate-400 mt-2 text-sm">您还没有{{ viewMode === 'owned' ? '持有' : '铸造' }}任何 NFT。</p>
          </div>

          <template v-else>
            <!-- 网格视图 Grid View -->
            <div v-if="currentLayout === 'grid'" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
               <div v-for="nft in nftList" :key="nft.nftId"
                 class="group relative bg-white dark:bg-slate-800 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:shadow-xl cursor-pointer flex flex-col"
                 @click="handleViewDetail(nft)">

                 <!-- 图片区域 -->
                 <div class="relative w-full aspect-square bg-slate-100 dark:bg-slate-900 overflow-hidden">
                   <!-- 这里的 imageUrl 是后端 VO 返回的预览图 -->
                   <img :src="nft.imageUrl || '/placeholder-nft.png'" :alt="nft.name"
                     class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" loading="lazy" />
                   
                   <!-- 遮罩层 -->
                   <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
                   
                   <!-- 悬浮状态标签 -->
                   <div class="absolute top-3 right-3">
                      <span :class="[
                        'text-[10px] font-bold px-2 py-1 rounded-full backdrop-blur-md shadow-sm',
                        nft.isForSale ? 'bg-green-500/90 text-white' : 'bg-slate-500/80 text-white'
                      ]">
                        {{ nft.isForSale ? '在售' : '未售' }}
                      </span>
                   </div>

                   <!-- 悬浮底部信息 -->
                   <div class="absolute left-0 right-0 bottom-0 p-4 translate-y-4 group-hover:translate-y-0 transition-transform duration-300 opacity-0 group-hover:opacity-100">
                      <div class="flex gap-2 justify-center">
                        <!-- 详情按钮 -->
                        <button class="px-3 py-1.5 bg-white/20 hover:bg-white/40 backdrop-blur-md rounded-lg text-white text-xs transition font-medium">
                          查看详情
                        </button>
                      </div>
                   </div>
                 </div>
                 
                 <!-- 卡片信息 -->
                 <div class="p-4 flex-1 flex flex-col justify-between">
                    <div>
                      <div class="flex justify-between items-start mb-1">
                        <h3 class="font-bold text-slate-800 dark:text-white truncate pr-2" :title="nft.name">{{ nft.name || '未命名 NFT' }}</h3>
                        <span class="text-xs font-mono text-slate-400">#{{ nft.tokenId }}</span>
                      </div>
                      <p class="text-xs text-slate-500 dark:text-slate-400 line-clamp-2 min-h-[2.5em]">{{ nft.description || '暂无描述' }}</p>
                    </div>

                    <div class="mt-4 pt-3 border-t border-slate-100 dark:border-slate-700 flex items-center justify-between">
                       <div class="flex flex-col">
                         <span class="text-[10px] text-slate-400 uppercase tracking-wider">当前价格</span>
                         <span class="text-sm font-bold text-indigo-600 dark:text-indigo-400">
                           {{ nft.isForSale ? formatPrice(nft.price) + ' ETH' : '--' }}
                         </span>
                       </div>
                       
                       <!-- 快捷操作菜单 -->
                       <div class="flex gap-1">
                          <button v-if="!nft.isForSale" @click.stop="handleSetPrice(nft)" class="p-2 text-indigo-600 hover:bg-indigo-50 rounded-lg" title="上架出售">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2v20"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>
                          </button>
                          <button v-else @click.stop="handleCancelSale(nft)" class="p-2 text-red-500 hover:bg-red-50 rounded-lg" title="取消出售">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" x2="9" y1="9" y2="15"/><line x1="9" x2="15" y1="9" y2="15"/></svg>
                          </button>
                       </div>
                    </div>
                 </div>
               </div>
            </div>

            <!-- 列表视图 List View -->
            <div v-else-if="currentLayout === 'list'" class="overflow-hidden rounded-2xl border border-slate-200 dark:border-slate-700 shadow-sm bg-white dark:bg-slate-800">
               <div class="overflow-x-auto">
                 <table class="w-full text-sm text-left text-slate-500 dark:text-slate-400">
                   <thead class="text-xs text-slate-700 uppercase bg-slate-50 dark:bg-slate-900/50 dark:text-slate-300 border-b border-slate-200 dark:border-slate-700">
                     <tr>
                       <th scope="col" class="px-6 py-4 font-semibold">资产预览</th>
                       <th scope="col" class="px-6 py-4 font-semibold">名称 / ID</th>
                       <th scope="col" class="px-6 py-4 font-semibold">价格</th>
                       <th scope="col" class="px-6 py-4 font-semibold">状态</th>
                       <th scope="col" class="px-6 py-4 font-semibold">合约地址</th>
                       <th scope="col" class="px-6 py-4 font-semibold">创建时间</th>
                       <th scope="col" class="px-6 py-4 font-semibold text-right">操作</th>
                     </tr>
                   </thead>
                   <tbody class="divide-y divide-slate-100 dark:divide-slate-700/50">
                     <tr v-for="nft in nftList" :key="nft.nftId" 
                         class="bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700/30 transition-colors cursor-pointer"
                         @click="handleViewDetail(nft)">
                       
                       <td class="px-6 py-3">
                         <div class="w-12 h-12 rounded-lg overflow-hidden bg-slate-100 dark:bg-slate-900 border border-slate-200 dark:border-slate-700">
                           <img :src="nft.imageUrl || '/placeholder-nft.png'" class="w-full h-full object-cover" loading="lazy">
                         </div>
                       </td>
                       
                       <td class="px-6 py-3">
                         <div class="flex flex-col">
                           <span class="font-medium text-slate-900 dark:text-white">{{ nft.name || '未命名' }}</span>
                           <span class="text-xs text-slate-400 font-mono">Token ID: #{{ nft.tokenId }}</span>
                         </div>
                       </td>
                       
                       <td class="px-6 py-3 font-bold text-indigo-600 dark:text-indigo-400">
                         {{ nft.isForSale ? formatPrice(nft.price) + ' ETH' : '--' }}
                       </td>
                       
                       <td class="px-6 py-3">
                         <span :class="[
                           'px-2 py-1 rounded text-xs font-medium',
                           nft.isForSale ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400' : 'bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-300'
                         ]">
                           {{ nft.isForSale ? '在售' : '仓库中' }}
                         </span>
                       </td>

                       <td class="px-6 py-3 text-xs font-mono text-slate-500">
                          {{ shortenAddress(nft.contractAddress, 6) }}
                       </td>
                       
                       <td class="px-6 py-3 text-xs">
                         {{ formatTimestamp(nft.createTime) }}
                       </td>
                       
                       <td class="px-6 py-3 text-right">
                         <div class="flex items-center justify-end gap-2">
                           <button v-if="!nft.isForSale" @click.stop="handleSetPrice(nft)" class="px-3 py-1.5 bg-indigo-50 hover:bg-indigo-100 text-indigo-600 rounded-lg text-xs font-medium transition">
                              上架
                           </button>
                           <button v-else @click.stop="handleCancelSale(nft)" class="px-3 py-1.5 bg-red-50 hover:bg-red-100 text-red-600 rounded-lg text-xs font-medium transition">
                              下架
                           </button>
                           <button @click.stop="handleViewDetail(nft)" class="p-2 text-slate-400 hover:text-slate-600 rounded-lg">
                              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="1"/><circle cx="19" cy="12" r="1"/><circle cx="5" cy="12" r="1"/></svg>
                           </button>
                         </div>
                       </td>

                     </tr>
                   </tbody>
                 </table>
               </div>
            </div>
            
            <!-- 分页 -->
            <div class="mt-8 flex justify-end" v-if="total > 0">
              <div class="flex items-center gap-2">
                <button 
                  @click="handleCurrentChange(currentPage - 1)" 
                  :disabled="currentPage === 1"
                  class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition"
                >
                  上一页
                </button>
                <span class="text-sm text-slate-500 dark:text-slate-400">
                  第 {{ currentPage }} 页 / 共 {{ Math.ceil(total / pageSize) }} 页
                </span>
                <button 
                  @click="handleCurrentChange(currentPage + 1)" 
                  :disabled="currentPage * pageSize >= total"
                  class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition"
                >
                  下一页
                </button>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 弹窗: 设置价格 -->
    <div v-if="priceDialogVisible" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <!-- 背景遮罩 -->
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="priceDialogVisible = false"></div>
      
      <!-- 弹窗内容 -->
      <div class="relative bg-white dark:bg-slate-900 rounded-2xl shadow-2xl w-full max-w-md p-6 transform transition-all scale-100">
        <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-2">设置出售价格</h3>
        <p class="text-sm text-slate-500 mb-6">上架后，其他用户可以在市场中购买您的 NFT。</p>
        
        <div class="mb-6">
          <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">价格 (ETH)</label>
          <div class="relative">
            <input 
              type="number" 
              v-model="priceForm.price" 
              step="0.0001"
              min="0.0001"
              class="w-full px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition"
              placeholder="0.00"
            />
            <div class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">ETH</div>
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
            class="flex-1 px-4 py-3 rounded-xl bg-indigo-600 text-white font-bold hover:bg-indigo-700 shadow-lg shadow-indigo-500/30 transition"
          >
            确认上架
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
// 引入之前定义的 API，假设您已在 api/nft.ts 中定义了这些方法
import { getMyNFTs, setNFTPrice, offShelf, putOnShelf } from '@/api/nft' 
import request from '@/utils/request' // 用于调用 check 和 register
import { useRouter } from 'vue-router'

// ---------------- 类型定义 ----------------
interface NFTInfo {
  nftId: string;
  tokenId: string;
  name: string;
  description: string | null;
  imageUrl: string;
  price: number;
  isForSale: boolean;
  ownerAddress: string;
  creatorAddress: string;
  fileHash: string;
  contractAddress: string;
  createTime: string;
}

// ---------------- 状态变量 ----------------
const router = useRouter()

// 页面状态
const checkingStatus = ref(true)     // 是否正在检查注册状态
const isRegistered = ref(false)      // 是否已注册
const registering = ref(false)       // 注册按钮 Loading
const blockchainAddress = ref('')    // 用户钱包地址

// NFT 列表相关
const loading = ref(false)
const nftList = ref<NFTInfo[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const viewMode = ref<'owned' | 'created'>('owned')
const currentLayout = ref<'grid' | 'list'>('grid')

// 操作相关
const priceDialogVisible = ref(false)
const selectedNFT = ref<NFTInfo | null>(null)
const priceForm = ref({ price: 0 })

// ---------------- 生命周期 ----------------

onMounted(async () => {
  await checkRegistrationStatus()
})

// ---------------- 核心逻辑 ----------------

// 1. 检查注册状态
const checkRegistrationStatus = async () => {
  checkingStatus.value = true
  try {
    // 调用后端 check 接口
    const res = await request({
      url: '/nft/user/check',
      method: 'get'
    })
    
    if (res.data && res.data.isRegistered) {
      isRegistered.value = true
      blockchainAddress.value = res.data.blockchainAddress
      // 已注册，则加载 NFT 列表
      fetchNFTList()
    } else {
      isRegistered.value = false
    }
  } catch (error) {
    console.error('检查注册状态失败', error)
    ElMessage.error('连接区块链服务失败')
  } finally {
    checkingStatus.value = false
  }
}

// 2. 注册/激活账户
const handleRegister = async () => {
  registering.value = true
  try {
    // 调用后端一键注册接口
    const res = await request({
      url: '/nft/user/register',
      method: 'post'
    })
    
    if (res.code === 200) {
      ElMessage.success('区块链账户激活成功！')
      isRegistered.value = true
      // 刷新页面或重新获取列表
      if (res.data && res.data.blockchainAddress) {
        blockchainAddress.value = res.data.blockchainAddress
      }
      fetchNFTList()
    } else {
      ElMessage.error(res.msg || '激活失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('激活请求异常，请稍后再试')
  } finally {
    registering.value = false
  }
}

// 3. 注销区块链账户
const handleDeregister = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要注销区块链账户吗？注销后您将无法查看和交易您的数字资产，直到重新激活。',
      '注销确认',
      {
        confirmButtonText: '确认注销',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const res = await request({
      url: '/nft/user/deregister',
      method: 'post'
    })

    if (res.code === 200) {
      ElMessage.success('注销成功')
      isRegistered.value = false
      blockchainAddress.value = ''
      nftList.value = []
    } else {
      ElMessage.error(res.msg || '注销失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
      ElMessage.error('注销操作异常')
    }
  }
}

// 4. 获取 NFT 列表
const fetchNFTList = async () => {
  loading.value = true
  try {
    const res = await getMyNFTs({
      page: currentPage.value,
      pageSize: pageSize.value,
      mode: viewMode.value
    })

    if (res.data) {
      nftList.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    console.error('获取NFT列表失败:', error)
    nftList.value = []
  } finally {
    loading.value = false
  }
}

// ---------------- 交互事件 ----------------

const handleModeChange = (mode: 'owned' | 'created') => {
  viewMode.value = mode
  currentPage.value = 1
  fetchNFTList()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
  fetchNFTList()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchNFTList()
}

const handleViewDetail = (nft: NFTInfo) => {
  router.push(`/nft/detail/${nft.nftId}`)
}

// 设置价格
const handleSetPrice = (nft: NFTInfo) => {
  selectedNFT.value = nft
  priceForm.value.price = nft.price || 0
  priceDialogVisible.value = true
}

const confirmSetPrice = async () => {
  if (!selectedNFT.value) return
  if (priceForm.value.price <= 0) {
    ElMessage.warning('价格必须大于 0')
    return
  }

  try {
    // 1. 设置价格
    await setNFTPrice(selectedNFT.value.nftId, priceForm.value.price)
    
    // 2. 如果当前未上架，则执行上架操作
    if (!selectedNFT.value.isForSale) {
      await putOnShelf(selectedNFT.value.nftId)
    }
    
    ElMessage.success('上架成功')
    priceDialogVisible.value = false
    fetchNFTList()
  } catch (error) {
    console.error(error)
    ElMessage.error('上架失败')
  }
}

// 下架 (取消出售)
const handleCancelSale = async (nft: NFTInfo) => {
  try {
    await ElMessageBox.confirm('确定要下架此 NFT 吗？下架后其他用户将无法购买。', '确认下架', {
      confirmButtonText: '确认下架',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    await offShelf(nft.nftId)
    ElMessage.success('下架成功')
    fetchNFTList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('下架失败')
  }
}

// ---------------- 工具函数 ----------------

const shortenAddress = (address: string, chars = 4) => {
  if (!address) return '--'
  if (address.length < chars * 2 + 2) return address
  return `${address.substring(0, chars)}...${address.substring(address.length - chars)}`
}

const formatTimestamp = (timestamp: string | number) => {
  if (!timestamp) return '--'
  return new Date(timestamp).toLocaleString()
}

const formatPrice = (price: number) => {
  return Number(price).toFixed(4)
}

const layoutBtnClass = (layout: string) => {
  return [
    'flex items-center justify-center w-9 h-8 rounded-xl transition-all duration-200',
    currentLayout.value === layout
      ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-sm'
      : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'
  ]
}

</script>

<style scoped>
/* 简单的动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>