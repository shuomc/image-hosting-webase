<template>
  <div class="nft-mint">
    <el-form :model="form" label-width="100px">
      <el-form-item label="选择图片">
        <el-select
          v-model="form.imageId"
          placeholder="请选择要铸造NFT的图片"
          style="width: 100%"
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

      <el-form-item label="描述">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入NFT描述"
        />
      </el-form-item>

      <el-form-item label="价格">
        <el-input-number
          v-model="form.price"
          :min="0"
          :precision="8"
          :step="0.1"
          placeholder="请输入NFT价格"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit">铸造</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { mintNFT } from '@/api/nft';
import { getMyImages } from '@/api/images';

const emit = defineEmits<{
  (e: 'success'): void;
  (e: 'close'): void;
}>();

// 状态
const imageList = ref([]);
const form = ref({
  imageId: '',
  description: '',
  price: 0
});

// 加载用户图片列表
const loadImages = async () => {
  try {
    const res = await getMyImages({
      page: 1,
      pageSize: 100
    });
    imageList.value = res.data.list;
  } catch (error) {
    ElMessage.error('加载图片列表失败');
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!form.value.imageId) {
    ElMessage.warning('请选择图片');
    return;
  }
  if (!form.value.description) {
    ElMessage.warning('请输入描述');
    return;
  }
  if (form.value.price <= 0) {
    ElMessage.warning('请输入有效的价格');
    return;
  }

  try {
    await mintNFT({
      imageId: form.value.imageId,
      description: form.value.description,
      price: form.value.price
    });
    ElMessage.success('铸造成功');
    emit('success');
  } catch (error) {
    ElMessage.error('铸造失败');
  }
};

// 取消
const handleCancel = () => {
  emit('close');
};

onMounted(() => {
  loadImages();
});
</script>

<style scoped>
.nft-mint {
  padding: 20px;
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
</style> 