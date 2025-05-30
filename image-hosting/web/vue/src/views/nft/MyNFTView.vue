<template>
  <div class="my-nfts">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的NFT</span>
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="owned" value="owned">我拥有的</el-radio-button>
            <el-radio-button label="created" value="created">我创建的</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-table :data="nftList" style="width: 100%" class="shadow-md rounded-lg overflow-hidden" v-loading="loading"
        element-loading-text="加载中..." empty-text="暂无NFT数据。">
        <el-table-column label="NFT ID" prop="nftId" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.nftId" placement="top">
              <span>{{ shortenAddress(row.nftId, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="描述" prop="description" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.description || '无描述' }}
          </template>
        </el-table-column>

        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            <span class="text-red-500 font-bold">{{ formatPrice(row.price) }} ETH</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isForSale ? 'success' : 'info'">
              {{ row.isForSale ? '在售' : '未售' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="所有者" prop="blockchainAddress" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.blockchainAddress" placement="top">
              <span>{{ shortenAddress(row.blockchainAddress, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="合约地址" prop="contractAddress" width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tooltip :content="row.contractAddress" placement="top">
              <span>{{ shortenAddress(row.contractAddress, 6) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>

        <el-table-column label="Token ID" prop="tokenId" width="100"></el-table-column>

        <el-table-column label="创建时间" prop="createTime" width="180">
          <template #default="{ row }">
            {{ formatTimestamp(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click.stop="handleSetPrice(row)" v-if="!row.isForSale">
              设置价格
            </el-button>
            <el-button type="danger" size="small" @click.stop="handleCancelSale(row)" v-if="row.isForSale">
              取消出售
            </el-button>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="" size="small" @click.stop="handleViewDetail(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" background />
      </div>
    </el-card>

    <el-dialog v-model="priceDialogVisible" title="设置价格" width="30%">
      <el-form :model="priceForm" label-width="80px">
        <el-form-item label="价格(ETH)">
          <el-input-number v-model="priceForm.price" :min="0" :precision="4" :step="0.1" placeholder="请输入价格" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="priceDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmSetPrice">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailDialogVisible"
      title="NFT详情"
      width="50%"
    >
      <nft-detail
        v-if="selectedNFT"
        :nft-id="selectedNFT.nftId"
        @close="detailDialogVisible = false"
        @refresh="fetchNFTList"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyNFTs, setNFTPrice, cancelNFTSale } from '@/api/nft'
import NFTDetail from './NFTDetail.vue'
import { useRouter } from 'vue-router'

// Type definition for NFTInfo based on your provided JSON
interface NFTInfo {
  nftId: string;
  imageId: string;
  ownerId: string;
  tokenId: number;
  contractAddress: string;
  description: string | null;
  price: number;
  isForSale: boolean;
  createTime: string;
  updateTime: string | null;
  isDelete: boolean;
}

const viewMode = ref('owned')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const nftList = ref<NFTInfo[]>([])
const priceDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const selectedNFT = ref<NFTInfo | null>(null)
const loading = ref(false)
const router = useRouter()

const priceForm = ref({
  price: 0
})

// Helper to shorten long addresses for display (e.g., Ethereum addresses, NFT IDs)
const shortenAddress = (address: string, chars = 4) => {
  if (!address || address.length < chars * 2 + 2) {
    return address;
  }
  return `${address.substring(0, chars)}...${address.substring(address.length - chars)}`;
};

// Function to format timestamp for display
const formatTimestamp = (timestamp: string | number | Date) => {
  if (!timestamp) return '';
  const date = new Date(timestamp);
  // Example format: YYYY-MM-DD HH:mm:ss
  // You can adjust the format as needed
  return date.toLocaleString(); // Uses user's locale for date and time
};

// 获取NFT列表
const fetchNFTList = async () => {
  loading.value = true
  try {
    const res = await getMyNFTs({
      page: currentPage.value,
      pageSize: pageSize.value,
      mode: viewMode.value
    })

    // *** CRITICAL: Accessing data based on your highly nested JSON ***
    // Your JSON structure: { msg, code, data: { msg, code, data: { total, list: [...] }}}
    if (res.data && res.data) {
      nftList.value = res.data.list as NFTInfo[];
      total.value = res.data.total;
    } else {
      ElMessage.error('API响应数据结构不正确或数据为空。');
      nftList.value = [];
      total.value = 0;
    }
    console.log('Fetched NFT Data (parsed):', nftList.value);
    console.log('Total NFTs (parsed):', total.value);
    console.log('Full API Response (for debugging):', res.data); // Keep this for debugging

  } catch (error) {
    console.error('获取NFT列表失败:', error);
    ElMessage.error('获取NFT列表失败，请稍后再试。');
    nftList.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
  }
}

// Handle pagination - no changes needed
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1; // Reset to first page on page size change
  fetchNFTList()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchNFTList()
}

// Handle Set Price (remains the same)
const handleSetPrice = (nft: NFTInfo) => {
  selectedNFT.value = nft
  priceForm.value.price = nft.price
  priceDialogVisible.value = true
}

// Confirm Set Price (remains the same)
const confirmSetPrice = async () => {
  if (selectedNFT.value === null) {
    ElMessage.warning('没有选择NFT进行设置价格。')
    return
  }
  if (priceForm.value.price <= 0) {
    ElMessage.warning('价格必须大于0。')
    return
  }

  try {
    // 修改为正确的参数传递方式
    await setNFTPrice(selectedNFT.value.nftId, priceForm.value.price);
    ElMessage.success('设置价格成功！');
    priceDialogVisible.value = false;
    fetchNFTList();
  } catch (error) {
    console.error('设置价格失败:', error)
    ElMessage.error('设置价格失败，请稍后再试。')
  }
}

// Handle Cancel Sale (remains the same)
const handleCancelSale = async (nft: NFTInfo) => {
  try {
    await ElMessageBox.confirm('确定要取消出售此NFT吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });

    await cancelNFTSale(nft.nftId)
    ElMessage.success('取消出售成功！')
    fetchNFTList()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('取消出售失败:', error)
      ElMessage.error('取消出售失败，请稍后再试。')
    }
  }
}

// Format price (remains the same)
const formatPrice = (price: number) => {
  return price.toFixed(4)
}

// Watch viewMode change (remains the same)
watch(viewMode, () => {
  currentPage.value = 1
  fetchNFTList()
})

// OnMounted hook (remains the same)
onMounted(() => {
  fetchNFTList()
})

// 查看详情
const handleViewDetail = (nft: NFTInfo) => {
  router.push(`/nft/detail/${nft.tokenId}`)
}
</script>

---

<style scoped>
.my-nfts {
  padding: 20px;
  margin-top: 50px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Removed nft-card specific styles, as we are now using el-table */

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

/* El-table specific styles (optional, can be customized) */
.el-table {
  /* You can add custom table styles here */
  --el-table-header-bg-color: #f5f7fa;
  --el-table-row-hover-bg-color: #f5f7fa;
  /* Adjust font sizes, padding as needed */
  font-size: 14px;
}

/* Style for action buttons within the table, if needed */
.el-table .el-button {
  padding: 8px 12px;
  /* Adjust button padding for table rows */
  font-size: 12px;
}
</style>
