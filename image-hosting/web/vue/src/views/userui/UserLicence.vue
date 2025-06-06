<template>
  <div class="license-page">
    <!-- 顶部导航栏 (这里只做占位符，实际应用中应该是一个独立的组件) -->
    <header class="main-header">
      <div class="header-content">
        <div class="logo">
          <!-- 假设 Pexels 的 logo 是一个 SVG 或图片 -->
          <img src="../../assets/logo.png" alt="Pexels Logo" class="pexels-logo">
        </div>
        <div class="search-bar">
          <input type="text" placeholder="搜索免费照片和视频">
          <el-icon><Search /></el-icon> <!-- 使用 Search 图标 -->
        </div>
        <nav class="header-nav">
          <a href="#">探索</a>
          <a href="#">许可证</a>
          <el-button type="primary" class="join-button">加入</el-button>
        </nav>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="license-content">
      <section class="intro-section">
        <h1 class="page-title">许可</h1>
        <p class="page-subtitle">Pexels 上的所有照片和视频均可免费下载和使用。</p>
      </section>

      <!-- 允许的行为 -->
      <section class="allowed-actions">
        <h2 class="section-title">哪些行为是允许的？ <span class="emoji">🤔</span></h2>
        <div class="action-list">
          <div class="action-item">
            <el-icon class="icon-allowed"><Check /></el-icon> <!-- 使用 CheckBold 图标 -->
            <p>Pexels 上的所有照片和视频均可免费使用。</p>
          </div>
          <div class="action-item">
            <el-icon class="icon-allowed"><Check /></el-icon> <!-- 使用 CheckBold 图标 -->
            <p>无需注明图源。注明摄影作者或 Pexels 不是强制的，但这这种行为受赞赏。</p>
          </div>
          <div class="action-item">
            <el-icon class="icon-allowed"><Check /></el-icon> <!-- 使用 CheckBold 图标 -->
            <p>你可以修改 Pexels 上的照片和视频，发挥创造力。随心所欲地编辑照片和视频。</p>
          </div>
        </div>
      </section>

      <!-- 禁止的行为 -->
      <section class="forbidden-actions">
        <h2 class="section-title">哪些行为是禁止的？ <span class="emoji">👎</span></h2>
        <div class="action-list">
          <div class="action-item">
            <el-icon class="icon-forbidden"><CloseBold /></el-icon> <!-- 使用 CloseBold 图标 -->
            <p>身份已识别的人们不可以违法或恶意的方式出现。</p>
          </div>
          <div class="action-item">
            <el-icon class="icon-forbidden"><CloseBold /></el-icon> <!-- 使用 CloseBold 图标 -->
            <p>请勿售卖未经修改的图片副本。例如: 售卖实物海报，印刷在事件中不作任何修改而出售的背景图、印刷品或实物出版。</p>
          </div>
          <div class="action-item">
            <el-icon class="icon-forbidden"><CloseBold /></el-icon> <!-- 使用 CloseBold 图标 -->
            <p>请勿在您照片上描绘出的人们获得了他人的虚假认可。</p>
          </div>
          <div class="action-item">
            <el-icon class="icon-forbidden"><CloseBold /></el-icon> <!-- 使用 CloseBold 图标 -->
            <p>请勿在其他素材照片或壁纸平台上重新发布或销售图片和视频。</p>
          </div>
          <div class="action-item">
            <el-icon class="icon-forbidden"><CloseBold /></el-icon> <!-- 使用 CloseBold 图标 -->
            <p>请勿明示或暗示得到任何商品的商标、设计标志、著作权或服务标记许可。</p>
          </div>
        </div>
      </section>

      <!-- 通过图片讲述你的故事 -->
      <section class="story-section">
        <h2 class="section-title">通过图片讲述你的故事</h2>
        <p class="section-subtitle">Pexels 上的所有照片和视频均可以免费使用。</p>

        <div class="story-grid">
          <div class="story-card large">
            <img src="https://images.pexels.com/photos/3771089/pexels-photo-3771089.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="工作" class="card-image">
            <div class="card-content">
              <h3>在你的网站、博客或应用程序上</h3>
              <p>可以免费使用任何照片和视频。无论您的网站、博客或应用程序是否用于商业目的，都可以在线上、线下、电子书、教科书、广播、电视、电影、商业企业、社交媒体和社交活动中免费使用。</p>
            </div>
          </div>

          <div class="story-card">
            <img src="https://images.pexels.com/photos/6303577/pexels-photo-6303577.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="产品" class="card-image">
            <div class="card-content">
              <h3>推广产品</h3>
              <p>使用 Pexels 上的图片和视频来推广你的产品，例如：在社交媒体上发布，在你的网站上，或在广告中使用。</p>
            </div>
          </div>

          <div class="story-card">
            <img src="https://images.pexels.com/photos/15729790/pexels-photo-15729790/free-photo-of-man-holding-a-skateboard.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="社交媒体" class="card-image">
            <div class="card-content">
              <h3>在社交媒体上分享</h3>
              <p>在 Facebook、Instagram 和 YouTube 等社交媒体上发布引人入胜的照片和视频，以增加你的受众和参与度。</p>
            </div>
          </div>
        </div>
      </section>
    </main>

    <FooterComponent />
  </div>
</template>

<script lang="ts" setup>
import { ElButton, ElIcon } from 'element-plus';
import { Check, Search, CloseBold } from '@element-plus/icons-vue'; // 确保这些图标已导入
import FooterComponent from './components/FooterComponent.vue';

// 注意: 顶部导航栏和底部只是占位符，实际项目中它们应该是全局布局或单独的组件。
// 这里为了演示许可证页面内容而包含在内。
</script>

<style scoped>
.license-page {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5; /* 页面背景色 */
  line-height: 1.6;
}

/* --- 全局头部样式 (Placeholder) --- */
.main-header {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 10px 20px;
  display: flex;
  justify-content: center; /* 使内容居中 */
  align-items: center;
  height: 60px; /* 固定高度 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1400px; /* 限制内容最大宽度 */
}

.pexels-logo {
  height: 30px; /* 调整 logo 大小 */
}

.search-bar {
  display: flex;
  align-items: center;
  background-color: #f0f0f0;
  border-radius: 25px;
  padding: 8px 15px;
  flex-grow: 1; /* 允许搜索栏填充空间 */
  max-width: 500px; /* 限制搜索栏最大宽度 */
  margin: 0 20px;
}

.search-bar input {
  border: none;
  background: transparent;
  outline: none;
  flex-grow: 1;
  font-size: 16px;
  color: #333;
}

.search-bar .el-icon {
  color: #888;
  margin-left: 10px;
}

.header-nav {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-nav a {
  text-decoration: none;
  color: #555;
  font-weight: 500;
  font-size: 15px;
}

.header-nav a:hover {
  color: #000;
}

.join-button {
  background-color: #00b05b;
  color: #fff;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
  font-weight: bold;
}

/* --- 主内容区域 --- */
.license-content {
  max-width: 900px; /* 页面主要内容的中心宽度 */
  margin: 40px auto;
  padding: 0 20px;
}

.intro-section {
  text-align: center;
  margin-bottom: 50px;
}

.page-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.page-subtitle {
  font-size: 18px;
  color: #666;
}

.section-title {
  font-size: 32px;
  font-weight: bold;
  margin-top: 60px;
  margin-bottom: 30px;
  text-align: center;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.emoji {
  font-size: 28px;
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 60px;
}

.action-item {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px 25px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  display: flex;
  align-items: flex-start;
  gap: 15px;
}

.action-item p {
  margin: 0;
  font-size: 16px;
  color: #555;
}

.icon-allowed {
  color: #00b05b; /* 绿色 */
  font-size: 24px;
  flex-shrink: 0;
}

.icon-forbidden {
  color: #ff4d4f; /* 红色 */
  font-size: 24px;
  flex-shrink: 0;
}

/* --- Story Section --- */
.story-section {
  margin-top: 60px;
  padding-bottom: 50px;
}

.story-section .section-title {
  margin-bottom: 10px;
}

.story-section .section-subtitle {
  text-align: center;
  font-size: 16px;
  color: #666;
  margin-bottom: 40px;
}

.story-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 默认两列 */
  gap: 20px;
}

.story-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  overflow: hidden;
  display: flex;
  flex-direction: column; /* 默认图片在上，内容在下 */
}

.story-card.large {
  grid-column: span 2; /* 大卡片占据两列 */
  display: flex;
  flex-direction: row; /* 大卡片图片在左，内容在右 */
  align-items: center; /* 垂直居中 */
}

.story-card .card-image {
  width: 100%;
  height: 250px; /* 默认图片高度 */
  object-fit: cover;
}

.story-card.large .card-image {
  width: 50%; /* 大卡片图片宽度 */
  height: 100%; /* 继承容器高度 */
  min-height: 350px; /* 确保大卡片有足够高度 */
}

.story-card .card-content {
  padding: 25px;
}

.story-card h3 {
  font-size: 24px;
  font-weight: bold;
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
}

.story-card p {
  font-size: 16px;
  color: #555;
  margin: 0;
}

/* --- 全局底部样式 (Placeholder) --- */
.main-footer {
  background-color: #333;
  color: #aaa;
  padding: 30px 20px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.footer-links a {
  color: #aaa;
  text-decoration: none;
  margin: 0 15px;
  font-size: 14px;
}

.footer-links a:hover {
  color: #fff;
}

.social-icons img {
  height: 24px;
  margin: 0 10px;
}

/* --- 响应式设计 --- */
@media (max-width: 768px) {
  .header-content {
    flex-wrap: wrap; /* 小屏幕下允许换行 */
    justify-content: center;
    gap: 10px;
  }
  .search-bar {
    width: 100%;
    margin: 10px 0;
    order: 3; /* 让搜索栏在小屏幕下排在最后 */
  }
  .header-nav {
    gap: 10px;
  }
  .join-button {
    margin-left: auto; /* Push join button to right */
  }

  .page-title {
    font-size: 36px;
  }
  .section-title {
    font-size: 24px;
  }

  .story-grid {
    grid-template-columns: 1fr; /* 小屏幕下变为单列 */
  }
  .story-card.large {
    flex-direction: column; /* 大卡片在小屏幕下也变为垂直布局 */
  }
  .story-card.large .card-image {
    width: 100%;
    height: 250px;
    min-height: auto;
  }
}
</style>
