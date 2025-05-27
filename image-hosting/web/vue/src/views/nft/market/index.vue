<template>
  <div class="nft-market">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>NFT市场</span>
          <el-input
            v-model="searchQuery"
            placeholder="搜索NFT"
            class="search-input"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col v-for="nft in nftList" :key="nft.nftId" :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
          <el-card class="nft-card" :body-style="{ padding: '0px' }">
            <img :src="nft.imageUrl" class="nft-image" />
            <div class="nft-info">
              <h3>{{ nft.description }}</h3>
              <p class="price">价格: {{ formatPrice(nft.price) }} ETH</p>
              <div class="owner">
                <span>所有者: {{ formatAddress(nft.ownerAddress) }}</span>
              </div>
              <el-button 
                type="primary" 
                :disabled="!nft.isForSale"
                @click="handleBuy(nft)"
              >
                {{ nft.isForSale ? '购买' : '已售出' }}
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 购买确认对话框 -->
    <el-dialog
      v-model="buyDialogVisible"
      title="确认购买"
      width="30%"
    >
      <div class="buy-dialog-content">
        <p>您确定要购买这个NFT吗？</p>
        <p>价格: {{ formatPrice(selectedNFT?.price) }} ETH</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="buyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmBuy">确认购买</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getNFTList, buyNFT } from '@/api/nft'

const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const nftList = ref([])
const buyDialogVisible = ref(false)
const selectedNFT = ref(null)

// 获取NFT列表
const fetchNFTList = async () => {
  try {
    const res = await getNFTList({
      page: currentPage.value,
      pageSize: pageSize.value,
      query: searchQuery.value
    })
    nftList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取NFT列表失败')
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchNFTList()
}

// 处理分页
const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchNFTList()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchNFTList()
}

// 处理购买
const handleBuy = (nft) => {
  selectedNFT.value = nft
  buyDialogVisible.value = true
}

// 确认购买
const confirmBuy = async () => {
  try {
    await buyNFT(selectedNFT.value.nftId)
    ElMessage.success('购买成功')
    buyDialogVisible.value = false
    fetchNFTList()
  } catch (error) {
    ElMessage.error('购买失败')
  }
}

// 格式化价格
const formatPrice = (price: number) => {
  return (price / 1e18).toFixed(4)
}

// 格式化地址
const formatAddress = (address: string) => {
  return `${address.slice(0, 6)}...${address.slice(-4)}`
}

onMounted(() => {
  fetchNFTList()
})
</script>

<style scoped>
.nft-market {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  width: 300px;
}

.nft-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.nft-card:hover {
  transform: translateY(-5px);
}

.nft-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.nft-info {
  padding: 14px;
}

.nft-info h3 {
  margin: 0;
  font-size: 16px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  margin: 10px 0;
}

.owner {
  font-size: 12px;
  color: #909399;
  margin-bottom: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.buy-dialog-content {
  text-align: center;
}
</style> 