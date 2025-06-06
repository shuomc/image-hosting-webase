<template>
  <div class="nft-detail">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>NFT详情</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>

      <div class="detail-content" v-loading="loading">
        <div class="nft-image" v-if="nftInfo">
          <img :src="'http://'+nftInfo.minioUrl" :alt="nftInfo.description || 'NFT Image'" class="nft-image-content" />
        </div>

        <div class="nft-info" v-if="nftInfo">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="Token ID">
              {{ nftInfo.tokenId }}
            </el-descriptions-item>
            <el-descriptions-item label="NFT ID">
              {{ nftInfo.nftId }}
            </el-descriptions-item>
            <el-descriptions-item label="描述">
              {{ nftInfo.description || '无描述' }}
            </el-descriptions-item>
            <el-descriptions-item label="价格">
              <span class="price">{{ formatPrice(nftInfo.price) }} ETH</span>
            </el-descriptions-item>
            <el-descriptions-item label="URI">
              {{ "nft:"+nftInfo.minioUrl }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatTimestamp(nftInfo.createTime) }}
            </el-descriptions-item>
          </el-descriptions>

          <div class="action-buttons" v-if="nftInfo">
            <el-button 
              type="primary" 
              @click="handleSetPrice" 
            >
              设置价格
            </el-button>
            <el-button 
              type="danger" 
              @click="handleCancelSale" 
            >
              取消出售
            </el-button>
            <el-button 
              type="success" 
              @click="handleBuy" 
            >
              购买
            </el-button>
          </div>
        </div>
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getWebaseNFTInfo, setNFTPrice, cancelNFTSale, buyNFT } from '@/api/nft'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const nftInfo = ref<any>(null)
const priceDialogVisible = ref(false)
const priceForm = ref({
  price: 0
})

// 格式化价格
const formatPrice = (price: number) => {
  return price.toFixed(4)
}

// 格式化时间戳
const formatTimestamp = (timestamp: string) => {
  if (!timestamp) return ''
  return new Date(timestamp).toLocaleString()
}

// 获取NFT详情
const fetchNFTInfo = async () => {
  loading.value = true
  try {
    const nftId = route.params.nftId
    if (!nftId) {
      ElMessage.error('NFT ID不存在')
      return
    }
    const res = await getWebaseNFTInfo(nftId as string)
    if (res.data) {
      nftInfo.value = res.data
    }
  } catch (error) {
    console.error('获取NFT详情失败:', error)
    ElMessage.error('获取NFT详情失败')
  } finally {
    loading.value = false
  }
}

// 设置价格
const handleSetPrice = () => {
  priceForm.value.price = nftInfo.value.price
  priceDialogVisible.value = true
}

// 确认设置价格
const confirmSetPrice = async () => {
  if (priceForm.value.price <= 0) {
    ElMessage.warning('价格必须大于0')
    return
  }

  try {
    const nftId = route.params.nftId
    await setNFTPrice(nftId as string, priceForm.value.price)
    ElMessage.success('设置价格成功')
    priceDialogVisible.value = false
    fetchNFTInfo()
  } catch (error) {
    console.error('设置价格失败:', error)
    ElMessage.error('设置价格失败')
  }
}

// 取消出售
const handleCancelSale = async () => {
  try {
    await ElMessageBox.confirm('确定要取消出售此NFT吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const nftId = route.params.nftId
    await cancelNFTSale(nftId as string)
    ElMessage.success('取消出售成功')
    fetchNFTInfo()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('取消出售失败:', error)
      ElMessage.error('取消出售失败')
    }
  }
}

// 购买NFT
const handleBuy = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要购买此NFT吗？价格：${formatPrice(nftInfo.value.price)} ETH`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const nftId = route.params.nftId
    await buyNFT(nftId as string)
    ElMessage.success('购买成功')
    fetchNFTInfo()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('购买失败:', error)
      ElMessage.error('购买失败')
    }
  }
}

// 返回上一页
const handleBack = () => {
  router.back()
}

onMounted(() => {
  fetchNFTInfo()
})
</script>

<style scoped>
.nft-detail {
  padding: 20px;
  margin-top: 50px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-content {
  display: flex;
  gap: 20px;
}

.nft-image {
  flex: 0 0 300px;
}

.nft-image-content {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
}

.nft-info {
  flex: 1;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
</style> 