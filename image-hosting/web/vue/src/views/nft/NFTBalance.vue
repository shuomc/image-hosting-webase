<template>
  <div class="min-h-screen py-10 transition-colors duration-300 bg-slate-50 dark:bg-slate-950">
    <div class="container mx-auto px-4 max-w-4xl">
      
      <!-- 标题 -->
      <div class="mb-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
        <h1 class="text-3xl font-extrabold text-slate-900 dark:text-white">钱包余额</h1>
        <p class="mt-2 text-slate-500 dark:text-slate-400">管理您的链上资产与交易记录</p>
      </div>

      <!-- 余额卡片 -->
      <div class="bg-white dark:bg-slate-900 rounded-3xl shadow-xl overflow-hidden mb-10 border border-slate-100 dark:border-slate-800 relative animate-in fade-in slide-in-from-bottom-6 duration-700">
        <!-- 背景装饰 -->
        <div class="absolute top-0 right-0 -mt-10 -mr-10 w-64 h-64 bg-gradient-to-br from-indigo-500/20 to-purple-500/20 rounded-full blur-3xl pointer-events-none"></div>
        
        <div class="p-10 relative z-10 flex flex-col items-center justify-center text-center">
          <div class="text-sm font-medium text-slate-500 dark:text-slate-400 uppercase tracking-wider mb-4">当前可用余额</div>
          
          <div class="flex items-baseline gap-3 mb-10">
            <span class="text-7xl sm:text-8xl font-black text-indigo-600 dark:text-indigo-400 tracking-tighter drop-shadow-sm">{{ formatBalance(balance) }}</span>
            <span class="text-2xl font-bold text-slate-400 dark:text-slate-500">ETH</span>
          </div>
          
          <div class="flex gap-4 w-full max-w-md justify-center">
            <button @click="openDeposit" class="flex-1 px-8 py-4 bg-indigo-600 hover:bg-indigo-700 text-white text-lg font-bold rounded-2xl shadow-xl shadow-indigo-500/30 transition-all transform hover:-translate-y-1 active:translate-y-0">
              充值
            </button>
            <button @click="openWithdraw" class="flex-1 px-8 py-4 bg-white dark:bg-slate-800 border-2 border-slate-100 dark:border-slate-700 text-slate-700 dark:text-slate-200 text-lg font-bold rounded-2xl hover:bg-slate-50 dark:hover:bg-slate-700 transition-all transform hover:-translate-y-1 active:translate-y-0">
              提现
            </button>
          </div>
        </div>
      </div>

      <!-- 交易记录 -->
      <div class="bg-white dark:bg-slate-900 rounded-2xl shadow-sm border border-slate-200 dark:border-slate-800 overflow-hidden animate-in fade-in slide-in-from-bottom-8 duration-1000">
        <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-800 flex justify-between items-center">
          <h3 class="font-bold text-lg text-slate-800 dark:text-white">交易历史</h3>
          <button @click="fetchTransactions" class="p-2 text-slate-400 hover:text-indigo-600 transition-colors rounded-lg hover:bg-slate-50 dark:hover:bg-slate-800" title="刷新">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12a9 9 0 0 0-9-9 9.75 9.75 0 0 0-6.74 2.74L3 8"/><path d="M3 3v5h5"/><path d="M3 12a9 9 0 0 0 9 9 9.75 9.75 0 0 0 6.74-2.74L21 16"/><path d="M16 21h5v-5"/></svg>
          </button>
        </div>

        <div v-if="loadingTransactions" class="p-10 flex justify-center">
          <div class="w-8 h-8 border-4 border-indigo-200 border-t-indigo-600 rounded-full animate-spin"></div>
        </div>

        <div v-else-if="transactions.length === 0" class="p-10 text-center text-slate-500">
          暂无交易记录
        </div>

        <div v-else class="overflow-x-auto">
          <table class="w-full text-sm text-left">
            <thead class="text-xs text-slate-500 uppercase bg-slate-50 dark:bg-slate-800/50 border-b border-slate-100 dark:border-slate-800">
              <tr>
                <th class="px-6 py-3">类型</th>
                <th class="px-6 py-3">金额 (ETH)</th>
                <th class="px-6 py-3">关联地址</th>
                <th class="px-6 py-3">时间</th>
                <th class="px-6 py-3">状态</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
              <tr v-for="tx in transactions" :key="tx.transactionId" class="hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors">
                <td class="px-6 py-4">
                  <span :class="getTypeClass(determineType(tx))">{{ determineType(tx) }}</span>
                </td>
                <td class="px-6 py-4 font-medium font-mono text-slate-700 dark:text-slate-300">
                  {{ formatBalance(tx.price) }}
                </td>
                <td class="px-6 py-4 font-mono text-xs text-slate-500">
                  {{ getRelatedAddress(tx) }}
                </td>
                <td class="px-6 py-4 text-slate-500">
                  {{ formatTime(tx.createTime) }}
                </td>
                <td class="px-6 py-4">
                  <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400">
                    成功
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="px-6 py-4 border-t border-slate-100 dark:border-slate-800 flex justify-end gap-2" v-if="total > pageSize">
           <button @click="changePage(page - 1)" :disabled="page === 1" class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition">上一页</button>
           <button @click="changePage(page + 1)" :disabled="transactions.length < pageSize" class="px-3 py-1 rounded-lg border border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed transition">下一页</button>
        </div>
      </div>
    </div>

    <!-- 充值弹窗 -->
    <div v-if="showDepositModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="showDepositModal = false"></div>
      <div class="relative bg-white dark:bg-slate-900 rounded-2xl shadow-2xl w-full max-w-md p-6 transform transition-all scale-100">
        <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-4">充值 ETH</h3>
        <div class="mb-6">
          <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">金额</label>
          <div class="relative">
            <input type="number" v-model="amountForm.amount" class="w-full px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-indigo-500 transition" placeholder="0.00" />
            <div class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">ETH</div>
          </div>
        </div>
        <div class="flex gap-3">
          <button @click="showDepositModal = false" class="flex-1 px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-medium hover:bg-slate-50 dark:hover:bg-slate-800 transition">取消</button>
          <button @click="handleConfirmDeposit" class="flex-1 px-4 py-3 rounded-xl bg-indigo-600 text-white font-bold hover:bg-indigo-700 shadow-lg shadow-indigo-500/30 transition">确认充值</button>
        </div>
      </div>
    </div>

    <!-- 提现弹窗 -->
    <div v-if="showWithdrawModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="showWithdrawModal = false"></div>
      <div class="relative bg-white dark:bg-slate-900 rounded-2xl shadow-2xl w-full max-w-md p-6 transform transition-all scale-100">
        <h3 class="text-xl font-bold text-slate-900 dark:text-white mb-4">提现 ETH</h3>
        <div class="mb-6">
          <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">金额</label>
          <div class="relative">
            <input type="number" v-model="amountForm.amount" class="w-full px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-900 dark:text-white outline-none focus:ring-2 focus:ring-indigo-500 transition" placeholder="0.00" />
            <div class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">ETH</div>
          </div>
          <p class="text-xs text-slate-500 mt-2 flex justify-between">
            <span>可提现余额:</span>
            <span class="font-mono font-medium">{{ formatBalance(balance) }} ETH</span>
          </p>
        </div>
        <div class="flex gap-3">
          <button @click="showWithdrawModal = false" class="flex-1 px-4 py-3 rounded-xl border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300 font-medium hover:bg-slate-50 dark:hover:bg-slate-800 transition">取消</button>
          <button @click="handleConfirmWithdraw" class="flex-1 px-4 py-3 rounded-xl bg-indigo-600 text-white font-bold hover:bg-indigo-700 shadow-lg shadow-indigo-500/30 transition">确认提现</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getBalance, deposit, withdraw, getNFTTransactions, checkRegistrationStatus } from '@/api/nft'
import { ElMessage } from 'element-plus'

// State
const balance = ref(0)
const transactions = ref<any[]>([])
const loadingTransactions = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const currentUserAddress = ref('')

// Modals
const showDepositModal = ref(false)
const showWithdrawModal = ref(false)
const amountForm = ref({ amount: 0 })

// Init
onMounted(async () => {
  await fetchCurrentUser()
  fetchBalance()
  fetchTransactions()
})

// Actions
const fetchCurrentUser = async () => {
  try {
    const res = await checkRegistrationStatus()
    if (res.data && res.data.blockchainAddress) {
      currentUserAddress.value = res.data.blockchainAddress
    }
  } catch (e) {
    console.error('获取用户地址失败', e)
  }
}

const fetchBalance = async () => {
  try {
    const res = await getBalance()
    if (res.data !== undefined) {
      balance.value = Number(res.data)
    }
  } catch (e) {
    console.error('获取余额失败', e)
  }
}

const fetchTransactions = async () => {
  loadingTransactions.value = true
  try {
    const res = await getNFTTransactions({ page: page.value, pageSize: pageSize.value })
    if (res.data) {
      transactions.value = res.data.list || []
      total.value = res.data.total || 0
    }
  } catch (e) {
    console.error('获取交易记录失败', e)
  } finally {
    loadingTransactions.value = false
  }
}

const openDeposit = () => {
  amountForm.value.amount = 0
  showDepositModal.value = true
}

const openWithdraw = () => {
  amountForm.value.amount = 0
  showWithdrawModal.value = true
}

const handleConfirmDeposit = async () => {
  if (amountForm.value.amount <= 0) return ElMessage.warning('请输入有效金额')
  try {
    await deposit(amountForm.value.amount)
    ElMessage.success('充值成功')
    showDepositModal.value = false
    fetchBalance()
    fetchTransactions()
  } catch (e) {
    ElMessage.error('充值失败')
  }
}

const handleConfirmWithdraw = async () => {
  if (amountForm.value.amount <= 0) return ElMessage.warning('请输入有效金额')
  if (amountForm.value.amount > balance.value) return ElMessage.warning('余额不足')
  try {
    await withdraw(amountForm.value.amount)
    ElMessage.success('提现申请已提交')
    showWithdrawModal.value = false
    fetchBalance()
    fetchTransactions()
  } catch (e) {
    ElMessage.error('提现失败')
  }
}

const changePage = (newPage: number) => {
  page.value = newPage
  fetchTransactions()
}

// Helpers
const formatBalance = (val: number) => Number(val).toFixed(4)
const formatTime = (ts: string) => ts ? new Date(ts).toLocaleString() : '--'

const shortenAddress = (addr: string) => {
  if (!addr) return '--'
  return `${addr.substring(0, 6)}...${addr.substring(addr.length - 4)}`
}

const getRelatedAddress = (tx: any) => {
  if (tx.toAddress && tx.toAddress !== '0x0000000000000000000000000000000000000000') return shortenAddress(tx.toAddress)
  if (tx.fromAddress) return shortenAddress(tx.fromAddress)
  return '--'
}

const determineType = (tx: any) => {
  const zeroAddr = '0x0000000000000000000000000000000000000000'
  const from = tx.fromAddress ? tx.fromAddress.toLowerCase() : ''
  const me = currentUserAddress.value ? currentUserAddress.value.toLowerCase() : ''
  
  // 优先使用用户指定的规则
  if (from === zeroAddr) return '铸造'
  if (from && from !== me) return '买入'
  if (from && from === me) return '卖出'
  
  // 回退到后端类型
  return formatType(tx.type)
}

const formatType = (type: string) => {
  const map: Record<string, string> = {
    'MINT': '铸造',
    'BUY': '购买',
    'SELL': '出售',
    'DEPOSIT': '充值',
    'WITHDRAW': '提现',
    'TRANSFER': '转账'
  }
  return map[type] || type || '未知'
}

const getTypeClass = (typeLabel: string) => {
  const base = "px-2 py-1 rounded text-xs font-bold "
  if (typeLabel === '铸造') return base + "bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-400"
  if (typeLabel === '买入') return base + "bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400"
  if (typeLabel === '卖出') return base + "bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400"
  
  // 兼容其他
  if (typeLabel === '充值') return base + "bg-green-100 text-green-700"
  if (typeLabel === '提现') return base + "bg-orange-100 text-orange-700"
  
  return base + "bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300"
}
</script>

<style scoped>
/* 动画类 */
.animate-in {
  animation-fill-mode: both;
}
</style> 