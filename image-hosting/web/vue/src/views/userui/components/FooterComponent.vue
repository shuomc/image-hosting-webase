<template>
  <el-footer class="app-footer">
    <div class="footer-top-buttons pt-10">
      <el-button type="success" size="large">上传自己的图片</el-button>
      <el-button size="large">帮助中心</el-button>
    </div>

    <div class="footer-main-content">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="footer-section story-hub">
            <h3>故事聚集地。</h3>
            <div class="app-downloads">
              <el-button size="medium">下载iOS版</el-button>
              <el-button size="medium">下载Android版</el-button>
            </div>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="footer-section">
            <h4>PEXELS</h4>
            <ul>
              <li><a href="#">免费素材图片</a></li>
              <li><a href="#">免费视频</a></li>
              <li><a href="#">热门搜索</a></li>
              <li><a href="#">收藏夹</a></li>
              <li><a href="#">挑战赛</a></li>
              <li><a href="#">热门作者排行榜</a></li>
              <li><a href="#">其他插件和应用</a></li>
            </ul>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="footer-section">
            <h4>企业</h4>
            <ul>
              <li><a href="#">关于</a></li>
              <li><a href="#">博客</a></li>
              <li><a href="#">帮助中心</a></li>
              <li><a href="#">举报内容</a></li>
              <li><a href="#">成为英雄</a></li>
              <li><a href="#">与 Pexels 合作</a></li>
              <li><a href="#">图片和视频 API</a></li>
              <li><a href="#">注册</a></li>
            </ul>
          </div>
        </el-col>

        <el-col :span="6">
          <div class="footer-section">
            <h4>免费素材图片</h4>
            <div class="tag-list">
              <a href="#" class="tag-button">黑白摄影</a>
              <a href="#" class="tag-button">生日快乐图片</a>
              <a href="#" class="tag-button">免费商业视频</a>
              <a href="#" class="tag-button">新年快乐图片</a>
              <a href="#" class="tag-button">精美高清壁纸</a>
              <a href="#" class="tag-button">银河壁纸</a>
              <a href="#" class="tag-button">锁屏壁纸</a>
              <a href="#" class="tag-button">iPhone 壁纸</a>
              <a href="#" class="tag-button">4K 壁纸</a>
              <a href="#" class="tag-button">大自然壁纸</a>
              <a href="#" class="tag-button">爱情壁纸</a>
              <a href="#" class="tag-button">手机壁纸</a>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="footer-bottom">
      <div class="copyright">
        © 2025 Pexels
      </div>
      <div class="legal-links">
        <a href="#">使用条款</a>
        <a href="#">隐私政策</a>
        <a href="#">许可证</a>
        <a href="#">Cookie政策</a>
      </div>
      <div class="language-selector">
        <el-dropdown trigger="click">
          <span class="el-dropdown-link">
            <el-icon><Orange /></el-icon> 简体中文 <el-icon class="el-icon--right"><arrow-up /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>English</el-dropdown-item>
              <el-dropdown-item>繁體中文</el-dropdown-item>
              <el-dropdown-item>日本語</el-dropdown-item>
              </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-footer>
</template>

<script setup lang="ts">
import {
  Orange, // For language icon
  ArrowUp // For dropdown arrow
} from '@element-plus/icons-vue';
</script>

<style scoped>
.app-footer {
  background-color: #f8f8f8; /* Light grey background */
  padding: 40px 20px 20px; /* Padding top, sides, bottom */
  border-top: 1px solid #eee;
  color: #606266;
  font-size: 14px;
}

/* Top buttons section */
.footer-top-buttons {
  text-align: center;
  margin-bottom: 50px; /* Space between buttons and main content */
}

.footer-top-buttons .el-button {
  margin: 0 10px;
  padding: 12px 25px; /* Adjust button padding */
  border-radius: 5px;
  font-size: 16px;
}

.footer-top-buttons .el-button--success {
  background-color: #00b07e;
  border-color: #00b07e;
  color: #fff;
}

.footer-top-buttons .el-button--success:hover {
  background-color: #00996c;
  border-color: #00996c;
}

/* Main footer content grid */
.footer-main-content {
  max-width: 1200px; /* Match main content width */
  margin: 0 auto;
  margin-bottom: 50px; /* Space between main content and bottom section */
}

.footer-section h3 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.footer-section h4 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section ul li a {
  color: #606266;
  text-decoration: none;
}

.footer-section ul li a:hover {
  color: #409eff; /* Element Plus primary color */
}

/* Social Icons */
.social-icons {
  margin-top: 20px;
  margin-bottom: 20px;
}

.social-icons a {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  margin-right: 15px;
  color: #606266;
  text-decoration: none;
  font-size: 22px; /* Adjust icon size */
  border-radius: 50%;
  transition: color 0.3s;
}

.social-icons a:hover {
  color: #409eff; /* Element Plus primary color */
}

/* App Download Buttons */
.app-downloads .el-button {
  margin-right: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  padding: 8px 15px;
}

/* Tag List (Free Stock Photos Categories) */
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px; /* Space between tags */
}

.tag-button {
  display: inline-block;
  padding: 8px 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  text-decoration: none;
  color: #606266;
  background-color: #fff;
  transition: all 0.2s ease-in-out;
  white-space: nowrap; /* Prevent tags from breaking text */
}

.tag-button:hover {
  border-color: #409eff;
  color: #409eff;
}

/* Bottom footer section */
.footer-bottom {
  border-top: 1px solid #eee;
  padding-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap; /* Allow wrapping on smaller screens */
  max-width: 1200px;
  margin: 0 auto;
}

.footer-bottom .copyright {
  margin-right: 20px;
  margin-bottom: 10px;
}

.footer-bottom .legal-links {
  display: flex;
  flex-wrap: wrap;
  gap: 0 20px; /* Horizontal gap, no vertical gap */
  margin-bottom: 10px;
}

.footer-bottom .legal-links a {
  color: #606266;
  text-decoration: none;
  white-space: nowrap; /* Keep links from breaking */
}

.footer-bottom .legal-links a:hover {
  color: #409eff;
}

.language-selector .el-dropdown-link {
  cursor: pointer;
  color: #606266;
  display: flex;
  align-items: center;
  font-size: 14px;
  margin-bottom: 10px;
}

.language-selector .el-dropdown-link .el-icon {
  margin-right: 5px;
}

/* Responsive adjustments (basic) */
@media (max-width: 768px) {
  .footer-main-content .el-col {
    flex: 0 0 100%; /* Stack columns on small screens */
    max-width: 100%;
    margin-bottom: 30px; /* Add space between stacked columns */
    text-align: center; /* Center content in stacked columns */
  }

  .footer-main-content .footer-section h4 {
      text-align: center;
  }

  .footer-main-content .footer-section ul {
      text-align: center; /* Center list items as well */
  }

  .social-icons {
    justify-content: center; /* Center social icons */
  }

  .app-downloads {
    display: flex;
    justify-content: center; /* Center app download buttons */
    flex-wrap: wrap;
  }

  .footer-bottom {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .footer-bottom .copyright,
  .footer-bottom .legal-links,
  .footer-bottom .language-selector {
    margin-bottom: 15px;
  }

  .footer-bottom .legal-links {
    justify-content: center;
  }
}
</style>
