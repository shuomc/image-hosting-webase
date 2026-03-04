<template>
  <div class="min-h-screen w-full bg-white">
    <div class="container mx-auto px-4 py-4 max-w-6xl">
      <div class="flex items-center gap-4 mb-8">
        <button @click="$router.back()" class="p-2 border-2 border-black bg-white">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M15 18l-6-6 6-6"/></svg>
        </button>
        <div>
          <h1 class="text-3xl font-bold text-black">图片详情</h1>
          <p class="text-sm mt-1 text-gray-600">查看并管理图片信息</p>
        </div>
      </div>

      <div v-if="loading" class="w-full h-40 flex items-center justify-center bg-gray-100 border-2 border-black text-gray-600">加载中…</div>
      <div v-else-if="error" class="w-full text-center text-black bg-gray-200 p-4 border-2 border-black">加载图片详情失败</div>

      <div v-else-if="imageDetail" class="grid grid-cols-1 md:grid-cols-3 gap-4">
        
        <div class="md:col-span-2 bg-gray-200 border-2 border-black h-96 flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect width="18" height="18" x="3" y="3"/><circle cx="9" cy="9" r="2"/><path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/></svg>
        </div>
        <div class="md:col-span-1 bg-white border-2 border-black p-4 flex flex-col justify-between">
          <div>
            <div class="mb-4">
              <h2 class="font-bold text-lg text-black break-words">{{ imageDetail.fileName }}</h2>
            </div>

            <div class="mb-4">
              <p class="text-sm text-gray-600 break-words min-h-12">
                {{ imageDetail.description || '暂无描述信息' }}
              </p>
            </div>

            <div class="space-y-2 text-sm text-gray-600 border-t-2 border-black pt-4">
              <div><span class="font-bold text-black">ID:</span> {{ imageDetail.imageId }}</div>
              <div><span class="font-bold text-black">持有者:</span> {{ imageDetail.userId }}</div>
              <div><span class="font-bold text-black">类型:</span> {{ imageDetail.contentType }}</div>
              <div><span class="font-bold text-black">大小:</span> {{ formatBytes(imageDetail.size) }}</div>
              <div><span class="font-bold text-black">尺寸:</span> {{ imageDetail.width || 'N/A' }} x {{ imageDetail.height || 'N/A' }}</div>
              <div><span class="font-bold text-black">公开:</span> {{ imageDetail.isPublic ? '是' : '否' }}</div>
              <div v-if="imageDetail.uploadTime"><span class="font-bold text-black">上传:</span> {{ formatTimestamp(imageDetail.uploadTime) }}</div>
            </div>
          </div>

          <div class="mt-6 flex flex-col gap-2">
            <button class="w-full px-4 py-2 border-2 border-black bg-white text-black text-sm font-bold">查看原图</button>
            <button class="w-full px-4 py-2 border-2 border-black bg-white text-black text-sm font-bold">下载</button>
            <button class="w-full px-4 py-2 border-2 border-black bg-white text-black text-sm font-bold">铸造 NFT</button>
            <button class="w-full px-4 py-2 border-2 border-black bg-gray-300 text-black text-sm font-bold">删除</button>
          </div>
        </div>
      </div>

      <div v-if="imageDetail" class="mt-4 grid grid-cols-2 gap-4">
        <div class="bg-white border-2 border-black p-4">
          <h3 class="text-lg font-bold text-black mb-4">扩展元数据</h3>
          
          <div class="flex border-b-2 border-black mb-4 overflow-x-auto">
            <button v-for="tab in ['EXIF', 'GPS', '统计', '安全']" 
                    :key="tab"
                    @click="activeTab = tab"
                    class="py-2 px-4 text-sm font-bold border-b-2"
                    :class="activeTab === tab ? 'border-b-2 border-black bg-gray-300 text-black' : 'border-b-2 border-white text-gray-600'">
              {{ tab }}
            </button>
          </div>

          <div class="space-y-3 text-sm text-gray-600">
            <div v-if="activeTab === 'EXIF'" class="grid grid-cols-2 gap-4">
              <div><span class="font-bold text-black">相机:</span> {{ imageDetail.cameraMake || 'N/A' }}</div>
              <div><span class="font-bold text-black">型号:</span> {{ imageDetail.cameraModel || 'N/A' }}</div>
              <div><span class="font-bold text-black">镜头:</span> {{ imageDetail.lensModel || 'N/A' }}</div>
              <div><span class="font-bold text-black">焦距:</span> {{ imageDetail.focalLength || 'N/A' }}</div>
              <div><span class="font-bold text-black">光圈:</span> {{ imageDetail.aperture || 'N/A' }}</div>
              <div><span class="font-bold text-black">ISO:</span> {{ imageDetail.iso || 'N/A' }}</div>
            </div>

            <div v-else-if="activeTab === 'GPS'" class="space-y-3">
              <div><span class="font-bold text-black">位置:</span> {{ imageDetail.locationName || '无' }}</div>
              <div><span class="font-bold text-black">纬度:</span> {{ imageDetail.latitude || 'N/A' }}</div>
              <div><span class="font-bold text-black">经度:</span> {{ imageDetail.longitude || 'N/A' }}</div>
            </div>

            <div v-else-if="activeTab === '统计'" class="grid grid-cols-2 gap-4">
              <div><span class="font-bold text-black">浏览:</span> {{ imageDetail.viewCount || '0' }}</div>
              <div><span class="font-bold text-black">下载:</span> {{ imageDetail.downloadCount || '0' }}</div>
              <div><span class="font-bold text-black">点赞:</span> {{ imageDetail.likeCount || '0' }}</div>
              <div><span class="font-bold text-black">分类:</span> {{ imageDetail.category || '未分类' }}</div>
            </div>

            <div v-else-if="activeTab === '安全'" class="space-y-3">
              <div>
                <span class="font-bold text-black">SHA-256:</span>
                <div class="bg-gray-100 border border-gray-300 p-2 mt-1 text-xs break-all">{{ imageDetail.fileHash || '未计算' }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white border-2 border-black p-4">
          <h3 class="text-lg font-bold text-black mb-4">使用链接</h3>
          <div class="space-y-3">
            <div v-for="(label, idx) in ['直链','Markdown','HTML','BBCode']" :key="idx">
              <label class="block text-sm font-bold text-black mb-1">{{ label }}:</label>
              <div class="flex items-center gap-2">
                <input type="text" value="https://example.com/image.jpg" readonly class="flex-grow px-3 py-2 text-sm bg-gray-100 border-2 border-black text-gray-600 truncate" />
                <button class="px-3 py-2 border-2 border-black bg-white text-black text-sm font-bold">复制</button>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

interface Image {
  imageId: string;
  thumbnailMinioUrl?: string;
  watermarkMinioUrl?: string | null;
  originMinioUrl?: string;
  fileName: string;
  userId: string;
  contentType: string;
  fileHash: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime?: string;
  width: number | null;
  height: number | null;
  cameraMake: string | null;
  cameraModel: string | null;
  lensModel: string | null;
  focalLength: string | null;
  aperture?: string | null;
  shutterSpeed?: string | null;
  iso?: number | null;
  shootTime?: string | null;
  locationName?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  viewCount?: number | null;
  downloadCount?: number | null;
  likeCount?: number | null;
  category?: string | null;
  dominantColor?: string | null;
  nftId?: string;
}

const imageDetail = ref<Image | null>(null);
const loading = ref(true);
const error = ref<Error | null>(null);
const activeTab = ref('EXIF');

// 模拟数据加载
onMounted(() => {
  loading.value = true;
  setTimeout(() => {
    imageDetail.value = {
      imageId: 'img-001',
      thumbnailMinioUrl: '',
      watermarkMinioUrl: null,
      originMinioUrl: '',
      fileName: 'sample-image-001.jpg',
      userId: 'user-123',
      contentType: 'image/jpeg',
      fileHash: 'abc123def456ghi789jkl',
      size: 2560000,
      isPublic: true,
      description: '这是一张样例图片的描述信息',
      uploadTime: new Date().toISOString(),
      width: 1920,
      height: 1080,
      cameraMake: 'Canon',
      cameraModel: 'EOS 5D Mark IV',
      lensModel: 'EF 24-70mm',
      focalLength: '50mm',
      aperture: 'f/2.8',
      shutterSpeed: '1/125',
      iso: 400,
      shootTime: new Date().toISOString(),
      locationName: 'Beijing, China',
      latitude: 39.9,
      longitude: 116.4,
      viewCount: 1024,
      downloadCount: 256,
      likeCount: 128,
      category: '风景',
      dominantColor: '#87CEEB',
    };
    loading.value = false;
  }, 500);
});

const formatBytes = (bytes: number | undefined, decimals = 2): string => {
  if (bytes === undefined || bytes === null || bytes === 0) return '0 B';
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
};

const formatTimestamp = (timestamp: string | undefined): string => {
  if (!timestamp) return '未知时间';
  try {
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) return '无效时间';
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  } catch {
    return '格式错误';
  }
};
</script>

<style scoped>
/* 确保滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}
.dark ::-webkit-scrollbar-thumb {
  background: #475569;
}
::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>