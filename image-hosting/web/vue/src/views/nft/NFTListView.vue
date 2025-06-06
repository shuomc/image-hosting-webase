<template>
  <div class="nft-list">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>NFT市场</span>
          <div class="header-actions">
            <el-input
              v-model="searchQuery"
              placeholder="搜索NFT"
              class="search-input"
              @keyup.enter="handleSearch"
            />
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button type="success" @click="handleMint">铸造NFT</el-button>
            <el-switch
              v-model="showOnlyForSale"
              active-text="仅显示在售"
              inactive-text="显示全部"
              @change="handleFilterChange"
              class="ml-3"
            />
          </div>
        </div>
      </template>

      <el-table
        :data="nftList"
        style="width: 100%"
        class="shadow-md rounded-lg overflow-hidden"
        v-loading="loading"
        element-loading-text="加载中..."
        empty-text="暂无NFT数据。"
      >
        <el-table-column label="图片" width="100">
          <template #default="{ row }">
            <img :src="'http://'+row.minioUrl" :alt="row.description || 'NFT Image'" class="h-16 w-16 object-cover rounded-md" />
          </template>
        </el-table-column>

        <el-table-column label="NFT ID" prop="nftId" width="220" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.nftId }}</span>
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

        <el-table-column label="所有者" prop="blockchainAddress" width="220" show-overflow-tooltip>
             <template #default="{ row }">
                    <span>{{ row.blockchainAddress }}</span>
            </template>
        </el-table-column>

        <el-table-column label="合约地址" prop="contractAddress" width="220" show-overflow-tooltip>
             <template #default="{ row }">
                    <span>{{ row.contractAddress }}</span>
            </template>
        </el-table-column>

        <el-table-column label="Token ID" prop="tokenId" width="100"></el-table-column>

        <el-table-column label="创建时间" prop="createTime" width="180">
          <template #default="{ row }">
            {{ formatTimestamp(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click.stop="handleViewDetail(row)">
              查看详情
            </el-button>
            <el-button
              v-if="row.isForSale"
              type="success"
              size="small"
              @click.stop="handleBuy(row)"
            >
              购买
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>
    </el-card>

    <el-dialog
      v-model="detailDialogVisible"
      title="NFT详情"
      width="50%"
    >
      <nft-detail
        v-if="selectedNFT"
        :nft="selectedNFT"
        @close="detailDialogVisible = false"
      />
    </el-dialog>

    <el-dialog
      v-model="mintDialogVisible"
      title="铸造NFT"
      width="50%"
    >
      <nft-mint
        @success="handleMintSuccess"
        @close="mintDialogVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import type { NFTInfo } from '@/api/nft';
import { getNFTList, buyNFT } from '@/api/nft';
import NFTDetail from './components/NFTDetail.vue';
import NFTMint from './components/NFTMint.vue';
import { useRouter } from 'vue-router'

// State
const nftList = ref<NFTInfo[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(12);
const searchQuery = ref('');
const detailDialogVisible = ref(false);
const mintDialogVisible = ref(false);
const selectedNFT = ref<NFTInfo | null>(null);
const loading = ref(false);
const showOnlyForSale = ref(true); // Default to showing only "For Sale" NFTs

// Function to format timestamp for display
const formatTimestamp = (timestamp: string | number | Date) => {
  if (!timestamp) return '';
  const date = new Date(timestamp);
  return date.toLocaleString(); // Uses user's locale for date and time
};

// Helper function to format price
const formatPrice = (price: number) => {
  if (typeof price !== 'number') return 'N/A';
  return price.toFixed(4); // Format to 4 decimal places
};

// Load NFT list
const loadNFTList = async () => {
  loading.value = true;
  try {
    const params: { page: number; pageSize: number; query?: string; isForSale?: boolean } = {
      page: currentPage.value,
      pageSize: pageSize.value,
    };
    if (searchQuery.value) {
      params.query = searchQuery.value;
    }
    if (showOnlyForSale.value) {
      params.isForSale = true;
    }

    const res = await getNFTList(params);

    // IMPORTANT: Adjust data access based on your actual backend response structure.
    // Based on previous conversations, it might be res.data.data.data.list
    if (res.data) { // Deeply nested data
      nftList.value = res.data.list as NFTInfo[];
      total.value = res.data.total;
    } else if (res.data && res.data.list) { // Standard top-level list
      nftList.value = res.data.list as NFTInfo[];
      total.value = res.data.total;
    } else { // Fallback for unexpected structures
      ElMessage.error('API响应数据结构不正确或数据为空。');
      nftList.value = [];
      total.value = 0;
    }

  } catch (error) {
    console.error('加载NFT列表失败:', error);
    ElMessage.error('加载NFT列表失败');
    nftList.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
  }
};

// Search
const handleSearch = () => {
  currentPage.value = 1;
  loadNFTList();
};

// Handle filter switch change
const handleFilterChange = () => {
  currentPage.value = 1;
  loadNFTList();
};

// Pagination
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  loadNFTList();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  loadNFTList();
};

// View Detail
const handleViewDetail = (nft: NFTInfo) => {
  selectedNFT.value = nft;
  detailDialogVisible.value = true;
};

// Buy NFT
const handleBuy = async (nft: NFTInfo) => {
  try {
    await buyNFT(nft.nftId);
    ElMessage.success('购买成功');
    loadNFTList();
  } catch (error) {
    console.error('购买失败:', error);
    ElMessage.error('购买失败');
  }
};

// Mint NFT
const handleMint = () => {
  mintDialogVisible.value = true;
};

// Mint Success
const handleMintSuccess = () => {
  mintDialogVisible.value = false;
  loadNFTList();
};

onMounted(() => {
  loadNFTList();
});

// Watch for changes in showOnlyForSale to trigger a re-fetch
watch(showOnlyForSale, () => {
  handleFilterChange();
});
</script>

---

<style scoped>
.nft-list {
  padding: 20px;
  margin-top: 50px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-table {
  --el-table-header-bg-color: #f5f7fa;
  --el-table-row-hover-bg-color: #f5f7fa;
  font-size: 14px;
}

.el-table .el-button {
  padding: 8px 12px;
  font-size: 12px;
}

.el-empty {
  width: 100%;
  padding: 50px 0;
}

/* Specific styles for table cells to ensure text wrapping/overflow */
.el-table .el-table__cell .cell {
  white-space: nowrap; /* Prevent wrapping by default */
  overflow: hidden; /* Hide overflow */
  text-overflow: ellipsis; /* Show ellipsis for truncated text */
}

/* Adjust width for columns that hold long IDs/addresses to prevent excessive truncation */
/* You might need to fine-tune these widths based on your data and screen size */
.el-table-column[label="NFT ID"] {
  width: 250px; /* Example adjustment */
}
.el-table-column[label="所有者"],
.el-table-column[label="合约地址"] {
  width: 250px; /* Example adjustment */
}
</style>
