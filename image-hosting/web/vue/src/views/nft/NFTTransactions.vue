<template>
  <div class="nft-transactions">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>交易历史</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-table
        :data="transactionList"
        style="width: 100%"
        class="shadow-md rounded-lg overflow-hidden"
        v-loading="loading"
        element-loading-text="加载中..."
        empty-text="暂无交易记录"
      >
        <el-table-column label="交易ID" prop="transactionId" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.transactionId" placement="top">
              <span>{{ shortenAddress(row.transactionId, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="NFT ID" prop="nftId" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.nftId" placement="top">
              <span>{{ shortenAddress(row.nftId, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="卖方" prop="fromUserId" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.fromUserId" placement="top">
              <span>{{ shortenAddress(row.fromUserId, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="买方" prop="toUserId" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.toUserId" placement="top">
              <span>{{ shortenAddress(row.toUserId, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            <span class="text-red-500 font-bold">{{ formatPrice(row.price) }} ETH</span>
          </template>
        </el-table-column>

        <el-table-column label="交易哈希" prop="transactionHash" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.transactionHash" placement="top">
              <span>{{ shortenAddress(row.transactionHash, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="交易时间" prop="createTime" width="180">
          <template #default="{ row }">
            {{ formatTimestamp(row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getNFTTransactions } from '@/api/nft'

const route = useRoute()
const loading = ref(false)
const transactionList = ref<any[]>([])

// 缩短地址显示
const shortenAddress = (address: string, chars = 4) => {
  if (!address || address.length < chars * 2 + 2) {
    return address
  }
  return `${address.substring(0, chars)}...${address.substring(address.length - chars)}`
}

// 格式化价格
const formatPrice = (price: number) => {
  return price.toFixed(4)
}

// 格式化时间戳
const formatTimestamp = (timestamp: string) => {
  if (!timestamp) return ''
  return new Date(timestamp).toLocaleString()
}

// 获取交易历史
const fetchTransactions = async () => {
  loading.value = true
  try {
    const res = await getNFTTransactions()
    if (res.data) {
      transactionList.value = res.data.list || []
    }
  } catch (error) {
    console.error('获取交易历史失败:', error)
    ElMessage.error('获取交易历史失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchTransactions()
})
</script>

<style scoped>
.nft-transactions {
  padding: 20px;
  margin-top: 50px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-table {
  --el-table-header-bg-color: #f5f7fa;
  --el-table-row-hover-bg-color: #f5f7fa;
  font-size: 14px;
}
</style>
