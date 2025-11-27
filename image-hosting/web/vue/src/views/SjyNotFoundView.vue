<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1 class="status-code">404</h1>
      <h2 class="message">抱歉，页面未找到。</h2>
      <p class="description">您访问的页面可能已被删除、名称已更改或暂时不可用。</p>
      <el-button type="primary" @click="goToHome" class="home-button">
        返回主页
      </el-button>
      <div class="animation-placeholder">
        <img src="https://via.placeholder.com/200x200?text=Page+Not+Found" alt="Page Not Found" class="not-found-image">
        <p class="tip">或者您可以尝试搜索您感兴趣的内容。</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { ElButton } from 'element-plus';

const router = useRouter();

const goToHome = () => {
  router.push('/'); // 返回主页
};
</script>

<style scoped>
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px); /* 假设头部高度为60px，减去它，让页面居中 */
  background-color: #f8f8f8;
  color: #333;
  text-align: center;
  padding: 20px;
  box-sizing: border-box;
}

.not-found-content {
  background-color: #fff;
  padding: 50px;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
}

.status-code {
  font-size: 120px;
  font-weight: bold;
  color: #00b05b; /* 使用您的品牌色 */
  margin-bottom: 20px;
  text-shadow: 4px 4px 10px rgba(0, 0, 0, 0.1);
}

.message {
  font-size: 32px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.description {
  font-size: 18px;
  color: #666;
  margin-bottom: 40px;
  line-height: 1.6;
}

.home-button {
  background-color: #00b05b;
  border-color: #00b05b;
  color: #fff;
  font-size: 18px;
  padding: 12px 30px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.home-button:hover {
  background-color: #009e52;
  border-color: #009e52;
}

.animation-placeholder {
  margin-top: 40px;
}

.not-found-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  opacity: 0.8;
}

.tip {
  font-size: 16px;
  color: #999;
  margin-top: 20px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .not-found-content {
    padding: 30px;
  }
  .status-code {
    font-size: 80px;
  }
  .message {
    font-size: 24px;
  }
  .description {
    font-size: 16px;
  }
  .home-button {
    font-size: 16px;
    padding: 10px 25px;
  }
}

@media (max-width: 480px) {
  .not-found-content {
    padding: 20px;
  }
  .status-code {
    font-size: 60px;
  }
  .message {
    font-size: 20px;
  }
  .description {
    font-size: 14px;
  }
  .tip {
    font-size: 14px;
  }
}
</style>
