<template>
  <div class="nft-mint">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>铸造NFT</span>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="mint-form"
      >
        <el-form-item label="选择图片" prop="imageId">
          <el-select
            v-model="form.imageId"
            placeholder="请选择要铸造的图片"
            class="image-select"
            @change="handleImageSelect"
          >
            <el-option
              v-for="image in imageList"
              :key="image.imageId"
              :label="image.fileName"
              :value="image.imageId"
            >
              <div class="image-option">
                <img :src="image.minioUrl" class="option-image" />
                <span>{{ image.fileName }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="图片预览">
          <div class="image-preview" v-if="selectedImage">
            <img :src="selectedImage.minioUrl" class="preview-image" />
          </div>
          <div class="no-image" v-else>
            请选择一张图片
          </div>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入NFT描述"
          />
        </el-form-item>

        <el-form-item label="价格(ETH)" prop="price">
          <el-input-number
            v-model="form.price"
            :min="0"
            :precision="4"
            :step="0.1"
            placeholder="请输入价格"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleMint" :loading="minting">
            铸造NFT
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserImages } from '@/api/image'
import { mintNFT } from '@/api/nft'

const formRef = ref()
const imageList = ref([])
const selectedImage = ref(null)
const minting = ref(false)

const form = ref({
  imageId: '',
  description: '',
  price: 0
})

const rules = {
  imageId: [
    { required: true, message: '请选择要铸造的图片', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入NFT描述', trigger: 'blur' },
    { min: 1, max: 200, message: '描述长度在1到200个字符之间', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ]
}

// 获取用户图片列表
const fetchUserImages = async () => {
  try {
    const res = await getUserImages()
    imageList.value = res.data
  } catch (error) {
    ElMessage.error('获取图片列表失败')
  }
}

// 处理图片选择
const handleImageSelect = (imageId) => {
  selectedImage.value = imageList.value.find(img => img.imageId === imageId)
}

// 处理铸造
const handleMint = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        minting.value = true
        await mintNFT({
          ...form.value,
          price: form.value.price * 1e18 // 转换为wei
        })
        ElMessage.success('NFT铸造成功')
        // 重置表单
        formRef.value.resetFields()
        selectedImage.value = null
      } catch (error) {
        ElMessage.error('NFT铸造失败')
      } finally {
        minting.value = false
      }
    }
  })
}

onMounted(() => {
  fetchUserImages()
})
</script>

<style scoped>
.nft-mint {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mint-form {
  max-width: 600px;
  margin: 0 auto;
}

.image-select {
  width: 100%;
}

.image-option {
  display: flex;
  align-items: center;
  gap: 10px;
}

.option-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.image-preview {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.preview-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.no-image {
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-radius: 8px;
  color: #909399;
}
</style> 