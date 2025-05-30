<template>
  <div class="nft-balance">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>我的余额</span>
          <el-button type="primary" @click="handleDeposit">充值</el-button>
        </div>
      </template>

      <div class="balance-content">
        <div class="balance-info">
          <h2>当前余额</h2>
          <div class="balance-amount">
            <span class="amount">{{ formatBalance(balance) }}</span>
            <span class="unit">ETH</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 充值对话框 -->
    <el-dialog
      v-model="depositDialogVisible"
      title="充值"
      width="30%"
    >
      <el-form :model="depositForm" label-width="80px">
        <el-form-item label="充值金额">
          <el-input-number
            v-model="depositForm.amount"
            :min="0"
            :precision="4"
            :step="0.1"
            placeholder="请输入充值金额"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="depositDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmDeposit">确认充值</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getWebaseBalance, webaseDeposit } from '@/api/nft'

const balance = ref(0)
const depositDialogVisible = ref(false)
const depositForm = ref({
  amount: 0
})

// 格式化余额显示
const formatBalance = (value: number) => {
  return value.toFixed(4)
}

// 获取余额
const fetchBalance = async () => {
  try {
    const res = await getWebaseBalance()
    if (res.data) {
      balance.value = res.data.balance
    }
  } catch (error) {
    console.error('获取余额失败:', error)
    ElMessage.error('获取余额失败')
  }
}

// 打开充值对话框
const handleDeposit = () => {
  depositForm.value.amount = 0
  depositDialogVisible.value = true
}

// 确认充值
const confirmDeposit = async () => {
  if (depositForm.value.amount <= 0) {
    ElMessage.warning('充值金额必须大于0')
    return
  }

  try {
    await webaseDeposit(depositForm.value.amount)
    ElMessage.success('充值成功')
    depositDialogVisible.value = false
    fetchBalance() // 刷新余额
  } catch (error) {
    console.error('充值失败:', error)
    ElMessage.error('充值失败')
  }
}

onMounted(() => {
  fetchBalance()
})
</script>

<style scoped>
.nft-balance {
  padding: 20px;
  margin-top: 50px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.balance-content {
  padding: 20px;
  text-align: center;
}

.balance-info {
  margin-bottom: 20px;
}

.balance-amount {
  margin-top: 20px;
}

.amount {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
}

.unit {
  font-size: 20px;
  margin-left: 10px;
  color: #606266;
}
</style> 