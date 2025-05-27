<template>
  <div class="my-nfts">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的NFT</span>
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="owned">我拥有的</el-radio-button>
            <el-radio-button label="created">我创建的</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col v-for="nft in nftList" :key="nft.nftId" :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
          <el-card class="nft-card" :body-style="{ padding: '0px' }">
            <img :src="nft.imageUrl" class="nft-image" />
            <div class="nft-info">
              <h3>{{ nft.description }}</h3>
              <p class="price">价格: {{ formatPrice(nft.price) }} ETH</p>
              <div class="status">
                <el-tag :type="nft.isForSale ? 'success' : 'info'">
                  {{ nft.isForSale ? '在售' : '未售' }}
                </el-tag>
              </div>
              <div class="actions">
                <el-button 
                  type="primary" 
                  size="small"
                  @click="handleSetPrice(nft)"
                  v-if="!nft.isForSale"
                >
                  设置价格
                </el-button>
                <el-button 
                  type="danger" 
                  size="small"
                  @click="handleCancelSale(nft)"
                  v-if="nft.isForSale"
                >
                  取消出售
                </el-button>
              </div>
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

    <!-- 设置价格对话框 -->
    <el-dialog
      v-model="priceDialogVisible"
      title="设置价格"
      width="30%"
    >
      <el-form :model="priceForm" label-width="80px">
        <el-form-item label="价格(ETH)">
          <el-input-number
            v-model="priceForm.price"
            :min="0"
            :precision="4"
            :step="0.1"
            placeholder="请输入价格"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="priceDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmSetPrice">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyNFTs, setNFTPrice, cancelNFTSale } from '@/api/nft'

const viewMode = ref('owned')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const nftList = ref([])
const priceDialogVisible = ref(false)
const selectedNFT = ref(null)

const priceForm = ref({
  price: 0
})

// 获取NFT列表
const fetchNFTList = async () => {
  try {
    const res = await getMyNFTs({
      page: currentPage.value,
      pageSize: pageSize.value,
      mode: viewMode.value
    })
    nftList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    ElMessage.error('获取NFT列表失败')
  }
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

// 处理设置价格
const handleSetPrice = (nft) => {
  selectedNFT.value = nft
  priceForm.value.price = nft.price / 1e18
  priceDialogVisible.value = true
}

// 确认设置价格
const confirmSetPrice = async () => {
  try {
    await setNFTPrice(selectedNFT.value.nftId, priceForm.value.price * 1e18)
    ElMessage.success('设置价格成功')
    priceDialogVisible.value = false
    fetchNFTList()
  } catch (error) {
    ElMessage.error('设置价格失败')
  }
}

// 处理取消出售
const handleCancelSale = async (nft) => {
  try {
    await cancelNFTSale(nft.nftId)
    ElMessage.success('取消出售成功')
    fetchNFTList()
  } catch (error) {
    ElMessage.error('取消出售失败')
  }
}

// 格式化价格
const formatPrice = (price: number) => {
  return (price / 1e18).toFixed(4)
}

// 监听视图模式变化
watch(viewMode, () => {
  currentPage.value = 1
  fetchNFTList()
})

onMounted(() => {
  fetchNFTList()
})
</script>

<style scoped>
.my-nfts {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.status {
  margin: 10px 0;
}

.actions {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 