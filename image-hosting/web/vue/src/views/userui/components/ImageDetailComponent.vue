<template>
  <el-dialog
    v-model="dialogVisible"
    fullscreen
    :show-close="false"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    class="image-detail-dialog"
  >
    <template #header>
      <div class="dialog-header">
        <div class="header-left">
          <el-button link class="close-button" @click="handleClose">
            <el-icon><CloseBold /></el-icon>
          </el-button>
          <div class="author-info">
            <div class="author-avatar-circle" :style="{ backgroundColor: getRandomColor() }">
              <span>{{ displayedImage?.authorName?.charAt(0).toUpperCase() || '?' }}</span>
            </div>
            <div class="author-text">
              <span class="author-name">{{ displayedImage?.authorName || '未知作者' }}</span>
              <el-button type="primary" size="small" class="follow-button">关注</el-button>
            </div>
          </div>
        </div>
        <div class="header-right">
          <el-button link class="action-btn">
            <el-icon><Star /></el-icon> 收藏
          </el-button>
          <el-button link class="action-btn">
            <el-icon><MagicStick /></el-icon> 在Canvas中编辑
          </el-button>
          <el-button type="success" @click="downloadImage(displayedImage)" class="download-button">
            免费下载 <el-icon><ArrowDownBold /></el-icon>
          </el-button>
        </div>
      </div>
    </template>

    <div class="dialog-content">
      <div class="image-display-area">
        <el-button
          v-if="hasPrevImage"
          class="nav-button nav-left"
          circle
          @click="goToPrevImage"
        >
          <el-icon><ArrowLeftBold /></el-icon>
        </el-button>

        <div class="main-image-container">
          <img :src="minioBaseUrl + displayedImage.minioUrl" :alt="displayedImage.fileName" class="main-image">
        </div>

        <el-button
          v-if="hasNextImage"
          class="nav-button nav-right"
          circle
          @click="goToNextImage"
        >
          <el-icon><ArrowRightBold /></el-icon>
        </el-button>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <div class="footer-left">
          <div class="meta-item-group"> <div class="meta-item">
              <strong>信息</strong>
              <p>上传时间: {{ formatDate(displayedImage?.createTime) }}</p>
              <p>图片尺寸: {{ displayedImage?.width || 'N/A' }}x{{ displayedImage?.height || 'N/A' }}</p>
              <p>图片类型: {{ displayedImage?.contentType || 'N/A' }}</p>
              <p>文件大小: {{ formatBytes(displayedImage?.size) }}</p>
            </div>
            <div class="meta-item">
              <strong>地点</strong>
              <p>{{ displayedImage?.description || '未知地点' }}</p>
            </div>
            <div class="meta-item">
              <strong>浏览量 & 下载量</strong>
              <p>浏览量: 0</p>
              <p>下载量: 0</p>
            </div>
          </div>
        </div>
        <div class="footer-right">
          <el-button link class="action-btn" @click="handleMoreOptions">
            <el-icon><MoreFilled /></el-icon>
          </el-button>
          <el-button link class="action-btn" @click="handleShare">
            <el-icon><Share /></el-icon> 分享
          </el-button>
          <el-button link class="action-btn" @click="handleReport">
            <el-icon><WarningFilled /></el-icon> 举报
          </el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue';
import { ElDialog, ElButton, ElIcon, ElMessage, ElMessageBox } from 'element-plus';
import {
  CloseBold, Star, MagicStick, ArrowDownBold, ArrowLeftBold, ArrowRightBold, MoreFilled, Share, WarningFilled
} from '@element-plus/icons-vue';

// Interfaces (Should be consistent across components)
interface Image {
  imageId: string;
  minioUrl: string;
  fileName: string;
  userId: string;
  contentType: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime?: string;
  createTime?: string;
  authorName?: string;
  width?: number; // Add width and height for image info
  height?: number;
}

// Props definition
const props = defineProps<{
  modelValue: boolean; // Controls dialog visibility (used with v-model)
  image: Image | null; // The specific image to display initially
  images: Image[]; // All images in the current view (for navigation)
  initialIndex: number; // The index of 'image' within 'images'
}>();

// Emits definition
const emit = defineEmits(['update:modelValue', 'close', 'navigate']);

const API_BASE_URL = 'http://localhost:8080'; // 后端API基础URL
const minioBaseUrl = 'http://localhost:9000'; // MinIO服务器基础URL

// Reactive state for dialog visibility
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
});

// Current index for navigation
const currentImageIndex = ref(props.initialIndex);

// Computed property for the currently displayed image
const displayedImage = computed<Image | null>(() => {
  if (props.images && props.images.length > 0 && currentImageIndex.value >= 0 && currentImageIndex.value < props.images.length) {
    return props.images[currentImageIndex.value];
  }
  return props.image; // Fallback to initial image if images array is not available or index is off
});

// Watch for changes in initialIndex to update currentImageIndex
watch(() => props.initialIndex, (newIndex) => {
  currentImageIndex.value = newIndex;
}, { immediate: true });

// Navigation controls
const hasPrevImage = computed(() => currentImageIndex.value > 0);
const hasNextImage = computed(() => currentImageIndex.value < props.images.length - 1);

const goToPrevImage = () => {
  if (hasPrevImage.value) {
    currentImageIndex.value--;
    emit('navigate', currentImageIndex.value);
  }
};

const goToNextImage = () => {
  if (hasNextImage.value) {
    currentImageIndex.value++;
    emit('navigate', currentImageIndex.value);
  }
};

// Handle closing the dialog
const handleClose = () => {
  dialogVisible.value = false;
  emit('close');
};

// Function to generate a random color for the avatar background
const getRandomColor = () => {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
};

// Utility functions for metadata display
const formatDate = (dateString?: string) => {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return 'N/A'; // Check for invalid date
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric', month: 'long', day: 'numeric'
    });
  } catch (e) {
    return 'N/A';
  }
};


const formatBytes = (bytes?: number) => {
  if (bytes === undefined || bytes === null || bytes < 0) return 'N/A';
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

// Action handlers
// Updated download function
const downloadImage = (image: Image | null) => {
  if (!image || !image.imageId || !image.fileName) {
    ElMessage.warning('图片信息不完整，无法下载。');
    return;
  }

  const downloadUrl = `${API_BASE_URL}/api/images/minio/${image.imageId}`;

  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName); // 使用 image.fileName 作为下载文件名

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  ElMessage.success('图片已开始下载！'); // 添加下载成功的提示
};

const handleShare = () => {
  ElMessageBox.alert('分享功能正在开发中...', '分享图片', {
    confirmButtonText: '好的'
  });
};

const handleReport = () => {
  ElMessageBox.alert('举报功能正在开发中...', '举报图片', {
    confirmButtonText: '好的'
  });
};

const handleMoreOptions = () => {
  ElMessage.info('更多选项功能正在开发中...');
};
</script>

<style>
/* El-Dialog overrides for fullscreen and background */
.image-detail-dialog.el-dialog {
  --el-dialog-bg-color: rgba(0, 0, 0, 0.9); /* Dark overlay background */
  --el-dialog-box-shadow: none;
  --el-dialog-border-radius: 0;
  --el-dialog-padding-primary: 0; /* Remove default padding */
  overflow: hidden; /* Prevent internal scrollbar */
  display: flex; /* Flex container for custom layout */
  flex-direction: column;
}

.image-detail-dialog .el-dialog__header {
  padding: 0; /* Custom header padding */
  margin-right: 0; /* Remove default right margin from close button */
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 10; /* Ensure header is on top */
  /* 修改这里：替换为白色半透明和模糊效果 */
  background-color: rgba(255, 255, 255, 0.6); /* 白色背景，20% 不透明度 */
  backdrop-filter: blur(5px); /* 模糊效果 */
  -webkit-backdrop-filter: blur(5px); /* Safari 兼容性 */
}

.image-detail-dialog .el-dialog__body {
  flex-grow: 1; /* Allow content to take available space */
  padding: 0; /* Custom body padding */
  display: flex;
  align-items: center; /* Center image vertically */
  justify-content: center; /* Center image horizontally */
}

.image-detail-dialog .el-dialog__footer {
  padding: 0; /* Custom footer padding */
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 10; /* Ensure footer is on top */
  background: linear-gradient(to top, rgba(0,0,0,0.7), rgba(0,0,0,0)); /* Gradient background for visibility */
}

/* Custom styles for the dialog content */
.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 25px;
  color: #fff;
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.close-button {
  font-size: 24px;
  color: #fff;
}
.close-button:hover {
  color: #ddd;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.author-avatar-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  border: 2px solid rgba(255, 255, 255, 0.5);
  flex-shrink: 0; /* Prevent shrinking */
}

.author-text {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: bold;
  font-size: 16px;
  white-space: nowrap; /* Prevent line break */
}

.follow-button {
  background-color: #00b05b;
  color: #fff;
  border: none;
  padding: 2px 8px;
  height: auto; /* Allow auto height */
  font-size: 12px;
  line-height: 1; /* Adjust line height for better alignment */
  align-self: flex-start; /* Align to the start of the flex container (column) */
  margin-top: 2px;
}
.follow-button:hover {
  background-color: #009e52;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.action-btn {
  color: #fff;
  font-size: 16px;
  padding: 8px 12px;
}
.action-btn:hover {
  color: #ddd;
  background-color: rgba(255, 255, 255, 0.1);
}

.download-button {
  background-color: #00b05b;
  color: #fff;
  border: none;
  padding: 10px 20px;
  font-weight: bold;
  font-size: 16px;
}
.download-button:hover {
  background-color: #009e52;
}

.dialog-content {
  flex-grow: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* Ensure image doesn't overflow */
  position: relative; /* For navigation buttons */
  width: 100%;
  height: 100%;
}

.image-display-area {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  position: relative;
}

.main-image-container {
  max-width: 90%; /* Limit image size within dialog */
  max-height: 90vh; /* Limit image height to viewport */
  display: flex;
  justify-content: center;
  align-items: center;
}

.main-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain; /* Ensure entire image is visible */
  border-radius: 8px;
}

.nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: none;
  width: 50px;
  height: 50px;
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 5; /* Above the image */
}
.nav-button:hover {
  background-color: rgba(255, 255, 255, 0.4);
}
.nav-left {
  left: 20px;
}
.nav-right {
  right: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end; /* Align items to the bottom */
  padding: 15px 25px;
  color: #fff;
  width: 100%;
}

/* New styles for footer-left content */
.footer-left {
  display: flex;
  flex-direction: column; /* Changed to column to stack meta-item-group */
  gap: 15px; /* Space between meta-item-group and other potential footer-left items */
}

.meta-item-group { /* New class for the backgrounded group */
  background-color: rgba(255, 255, 255, 0.2); /* White background with 20% opacity */
  border-radius: 8px; /* Rounded corners */
  padding: 15px 20px; /* Padding inside the box */
  backdrop-filter: blur(5px); /* Optional: add a slight blur to the background */
  -webkit-backdrop-filter: blur(5px); /* For Safari */
  display: flex;
  gap: 40px; /* Space between metadata columns */
}

.meta-item {
  display: flex;
  flex-direction: column;
}
.meta-item strong {
  font-size: 16px;
  margin-bottom: 5px;
}
.meta-item p {
  font-size: 14px;
  margin: 2px 0;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

</style>
