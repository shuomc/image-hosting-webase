<template>
  <div class="nft-detail">
    <el-row :gutter="20">
      <el-col :span="12">
        <img :src="nft.minioUrl" class="nft-image" />
      </el-col>
      <el-col :span="12">
        <div class="nft-info">
          <h2>{{ nft.description }}</h2>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="Token ID">{{ nft.tokenId }}</el-descriptions-item>
            <el-descriptions-item label="合约地址">{{ nft.contractAddress }}</el-descriptions-item>
            <el-descriptions-item label="价格">{{ nft.price }} ETH</el-descriptions-item>
            <el-descriptions-item label="状态">{{ nft.isForSale ? '在售' : '未售' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ nft.createTime }}</el-descriptions-item>
          </el-descriptions>

          <div class="nft-actions">
            <el-button 
              v-if="nft.isForSale" 
              type="success" 
              @click="handleBuy"
            >购买</el-button>
            <el-button 
              v-if="isOwner" 
              type="primary" 
              @click="handleSetPrice"
            >设置价格</el-button>
            <el-button 
              v-if="isOwner" 
              type="warning" 
              @click="handleToggleSale"
            >{{ nft.isForSale ? '取消销售' : '上架销售' }}</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 交易历史 -->
    <div class="transaction-history">
      <h3>交易历史</h3>
      <el-table :data="transactions" style="width: 100%">
        <el-table-column prop="transactionHash" label="交易哈希" />
        <el-table-column prop="fromUserId" label="卖方" />
        <el-table-column prop="toUserId" label="买方" />
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="createTime" label="交易时间" />
      </el-table>
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 设置价格对话框 -->
    <el-dialog
      v-model="priceDialogVisible"
      title="设置价格"
      width="30%"
    >
      <el-form :model="priceForm" label-width="80px">
        <el-form-item label="价格">
          <el-input-number v-model="priceForm.price" :min="0" :precision="8" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="priceDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePriceSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import type { NFTInfo, NFTTransaction } from '@/api/nft';
import { 
  getNFTTransactions, 
  buyNFT, 
  setNFTPrice, 
  cancelNFTSale 
} from '@/api/nft';
import { useUserStore } from '@/stores/user';

const props = defineProps<{
  nft: NFTInfo;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
}>();

const userStore = useUserStore();

// 状态
const transactions = ref<NFTTransaction[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const priceDialogVisible = ref(false);
const priceForm = ref({
  price: 0
});

// 计算属性
const isOwner = computed(() => {
  return userStore.userInfo?.userId === props.nft.ownerId;
});

// 加载交易历史
const loadTransactions = async () => {
  try {
    const res = await getNFTTransactions({
      nftId: props.nft.nftId,
      page: currentPage.value,
      pageSize: pageSize.value
    });
    transactions.value = res.data.list;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error('加载交易历史失败');
  }
};

// 分页
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  loadTransactions();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  loadTransactions();
};

// 购买NFT
const handleBuy = async () => {
  try {
    await buyNFT(props.nft.nftId);
    ElMessage.success('购买成功');
    emit('close');
  } catch (error) {
    ElMessage.error('购买失败');
  }
};

// 设置价格
const handleSetPrice = () => {
  priceForm.value.price = props.nft.price;
  priceDialogVisible.value = true;
};

const handlePriceSubmit = async () => {
  try {
    await setNFTPrice({
      nftId: props.nft.nftId,
      price: priceForm.value.price
    });
    ElMessage.success('设置价格成功');
    priceDialogVisible.value = false;
    emit('close');
  } catch (error) {
    ElMessage.error('设置价格失败');
  }
};

// 切换销售状态
const handleToggleSale = async () => {
  try {
    if (props.nft.isForSale) {
      await cancelNFTSale(props.nft.nftId);
      ElMessage.success('取消销售成功');
    } else {
      await setNFTPrice({
        nftId: props.nft.nftId,
        price: props.nft.price
      });
      ElMessage.success('上架销售成功');
    }
    emit('close');
  } catch (error) {
    ElMessage.error(props.nft.isForSale ? '取消销售失败' : '上架销售失败');
  }
};

onMounted(() => {
  loadTransactions();
});
</script>

<style scoped>
.nft-detail {
  padding: 20px;
}

.nft-image {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
}

.nft-info {
  padding: 20px;
}

.nft-info h2 {
  margin-top: 0;
  margin-bottom: 20px;
}

.nft-actions {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.transaction-history {
  margin-top: 40px;
}

.transaction-history h3 {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 