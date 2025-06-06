<template>
  <div class="challenges-page">
    <h1 class="page-title">过去趋势的挑战赛</h1>

    <div class="challenges-list">
      <el-card v-for="challenge in challenges" :key="challenge.id" class="challenge-card">
        <el-row :gutter="20">
          <el-col :span="12" class="challenge-media">
            <div class="media-grid">
              <div v-for="media in challenge.previewMedia" :key="media.id" class="media-item">
                <img :src="minioBaseUrl + media.minioUrl" :alt="media.alt" class="media-thumbnail">
              </div>
            </div>
          </el-col>
          <el-col :span="12" class="challenge-details">
            <h2 class="challenge-name">{{ challenge.name }}</h2>
            <p class="challenge-description">{{ challenge.description }}</p>
            <div class="challenge-participants" v-if="challenge.participants.length > 0">
              <div class="participant-avatars">
                <img v-for="(participant, pIndex) in challenge.participants.slice(0, 4)"
                     :key="pIndex"
                     :src="participant.avatar"
                     alt="参与者头像"
                     class="participant-avatar"
                     :style="{ zIndex: challenge.participants.length - pIndex }"
                >
                <span v-if="challenge.participants.length > 4" class="more-participants">
                  +{{ challenge.participants.length - 4 }}
                </span>
              </div>
              <span class="participant-count">{{ challenge.participants.length }} 名成员已加入</span>
            </div>
            <el-button type="primary" class="learn-more-button">了解更多</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <p v-if="challenges.length === 0 && !isLoadingChallenges" class="no-challenges">暂无挑战赛数据。</p>
    <p v-if="isLoadingChallenges" class="loading-message">加载挑战赛...</p>
    <p v-if="errorChallenges" class="error-message">{{ errorChallenges }}</p>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { ElCard, ElRow, ElCol, ElButton, ElMessage } from 'element-plus';
import axios from 'axios'; // Import axios

// Interfaces for data structure
interface MediaItem {
  id: string; // Used as key in v-for
  src?: string; // Original src, kept for clarity but minioUrl is used
  alt: string;
  minioUrl: string; // MinIO URL for the image
  imageId: string; // The ID of the image for unique identification
}

interface Participant {
  id: string;
  avatar: string;
  name: string;
}

interface Challenge {
  id: string;
  name: string;
  description: string;
  previewMedia: MediaItem[];
  participants: Participant[];
}

interface ImageData { // Interface matching your backend's ImageData structure
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
}

const API_BASE_URL = 'http://localhost:8080';
const minioBaseUrl = 'http://localhost:9000'; // Base URL for MinIO images

// Reactive data
const challenges = ref<Challenge[]>([]);
const isLoadingChallenges = ref(false);
const errorChallenges = ref<string | null>(null);
const publicImages = ref<ImageData[]>([]); // To store all public images from API

// Function to fetch all public images
const fetchPublicImages = async (): Promise<ImageData[]> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/images/public`);
    if (response.data && response.data.code === 200 && Array.isArray(response.data.data)) {
      return response.data.data;
    } else {
      console.warn('Failed to fetch public images:', response.data.msg);
      ElMessage.warning('未能获取公共图片数据。');
      return [];
    }
  } catch (err) {
    console.error('Error fetching public images:', err);
    ElMessage.error('获取公共图片失败，请检查后端服务。');
    return [];
  }
};

// Simulate fetching challenge data and integrate real images
const fetchChallenges = async () => {
  isLoadingChallenges.value = true;
  errorChallenges.value = null;
  try {
    // First, fetch all public images
    publicImages.value = await fetchPublicImages();

    // Create mock challenge data, picking random images from publicImages
    const mockChallenges: Challenge[] = [
      {
        id: 'challenge-1',
        name: '视频挑战赛：动态影像', // Translated
        description: '提交您最精彩的动态视频！', // Translated
        previewMedia: [], // Will be populated with random images
        participants: [],
      },
      {
        id: 'challenge-2',
        name: '粉色主页挑战赛', // Translated
        description: '提交您最棒的粉色主题照片和视频！', // Translated
        previewMedia: [], // Will be populated with random images
        participants: [
          { id: 'p1', avatar: 'https://randomuser.me/api/portraits/thumb/men/1.jpg', name: 'User 1' },
          { id: 'p2', avatar: 'https://randomuser.me/api/portraits/thumb/women/2.jpg', name: 'User 2' },
          { id: 'p3', avatar: 'https://randomuser.me/api/portraits/thumb/men/3.jpg', name: 'User 3' },
          { id: 'p4', avatar: 'https://randomuser.me/api/portraits/thumb/women/4.jpg', name: 'User 4' },
          { id: 'p5', avatar: 'https://randomuser.me/api/portraits/thumb/men/5.jpg', name: 'User 5' },
        ],
      },
      {
        id: 'challenge-3',
        name: '城市风光与都市景观', // Translated
        description: '捕捉城市生活的精髓，从熙熙攘攘的街道到宁静的天际线。', // Translated
        previewMedia: [],
        participants: [
            { id: 'p6', avatar: 'https://randomuser.me/api/portraits/thumb/men/6.jpg', name: 'User 6' },
            { id: 'p7', avatar: 'https://randomuser.me/api/portraits/thumb/women/7.jpg', name: 'User 7' },
        ],
      },
      {
        id: 'challenge-4',
        name: '近距离欣赏大自然之美', // Translated
        description: '花卉、昆虫或自然纹理的微距或特写镜头。', // Translated
        previewMedia: [],
        participants: [],
      }
    ];

    // Populate previewMedia with random images
    mockChallenges.forEach(challenge => {
      // Shuffle the public images to get random ones
      const shuffledImages = [...publicImages.value].sort(() => 0.5 - Math.random());

      // Get up to 3 random images for preview
      for (let i = 0; i < 3 && i < shuffledImages.length; i++) {
        const imgData = shuffledImages[i];
        challenge.previewMedia.push({
          id: imgData.imageId,
          minioUrl: imgData.minioUrl,
          alt: imgData.description || imgData.fileName,
        });
      }
    });

    challenges.value = mockChallenges;

  } catch (err) {
    errorChallenges.value = '加载挑战赛失败。';
    console.error('Error fetching challenges:', err);
  } finally {
    isLoadingChallenges.value = false;
  }
};

onMounted(() => {
  fetchChallenges();
});
</script>

<style scoped>
.challenges-page {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.challenges-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.challenge-card {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  padding: 20px;
}

.challenge-media {
  display: flex;
  align-items: center;
  justify-content: center;
}

.media-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 2 columns for images */
  gap: 10px;
  max-width: 400px; /* Limit the max width of the media grid */
  width: 100%;
}

.media-item {
  aspect-ratio: 1 / 1; /* Keep images square */
  overflow: hidden;
  border-radius: 8px;
}

.media-item:first-child {
  grid-column: span 2; /* First image spans two columns */
  height: 200px; /* Adjust height for the larger image */
}

.media-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.challenge-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 20px;
}

.challenge-name {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.challenge-description {
  font-size: 16px;
  color: #666;
  margin-bottom: 15px;
  line-height: 1.5;
}

.challenge-participants {
  display: flex;
  align-items: center;
  margin-top: 15px;
  margin-bottom: 20px;
}

.participant-avatars {
  display: flex;
  position: relative;
  margin-right: 10px;
}

.participant-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 2px solid #fff;
  margin-left: -10px; /* Overlap avatars */
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  background-color: #eee; /* Placeholder background */
}

.participant-avatar:first-child {
  margin-left: 0;
}

.more-participants {
  background-color: #00b05b;
  color: #fff;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  font-weight: bold;
  margin-left: -10px;
  border: 2px solid #fff;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.participant-count {
  font-size: 14px;
  color: #909399;
}

.learn-more-button {
  background-color: #00b05b;
  color: #fff;
  border: none;
  padding: 10px 25px;
  border-radius: 5px;
  font-weight: bold;
  align-self: flex-start;
  margin-top: 10px;
}

.learn-more-button:hover {
  background-color: #009e52;
}

.loading-message, .error-message, .no-challenges {
  text-align: center;
  color: #606266;
  margin-top: 20px;
}

.error-message {
  color: #f56c6c;
}

/* Responsive adjustments */
@media (max-width: 992px) {
  .challenge-card .el-row {
    flex-direction: column;
  }
  .challenge-media, .challenge-details {
    width: 100%;
    padding-left: 0;
  }
  .challenge-media {
    margin-bottom: 20px;
  }
  .challenge-details {
    align-items: center;
    text-align: center;
  }
  .learn-more-button {
    align-self: center;
  }
  .media-grid {
    max-width: none;
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 28px;
  }
  .challenge-name {
    font-size: 20px;
  }
  .challenge-description {
    font-size: 14px;
  }
  .media-item:first-child {
    height: 150px;
  }
}
</style>
