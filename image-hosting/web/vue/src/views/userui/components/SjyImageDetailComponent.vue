<template>
  <Teleport to="body">
    <Transition name="modal">
      <div 
        v-if="dialogVisible && displayedImage" 
        class="fixed inset-0 z-[9999] flex items-center justify-center"
      >
        <!-- Backdrop -->
        <div 
          class="absolute inset-0 bg-black/80 backdrop-blur-sm"
          @click="handleClose"
        ></div>

        <!-- Modal Panel -->
        <div class="relative w-full max-w-6xl max-h-[90vh] m-4 bg-white dark:bg-slate-800 rounded-2xl shadow-2xl overflow-hidden flex flex-col">
          
          <!-- Modal Header -->
          <div class="flex items-center justify-between px-6 py-4 border-b border-slate-100 dark:border-slate-700 shrink-0">
            <div class="flex items-center gap-4">
              <div 
                class="flex items-center gap-3 cursor-pointer"
                @click="goToUserProfile(displayedImage.userId)"
              >
                <div 
                  class="w-10 h-10 rounded-full flex items-center justify-center text-white font-bold shadow-md"
                  :style="{ backgroundColor: getRandomColor() }"
                >
                  {{ currentAuthorName.charAt(0).toUpperCase() }}
                </div>
                <div>
                  <div class="font-bold text-slate-900 dark:text-white hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors">
                    {{ currentAuthorName }}
                  </div>
                  <div class="text-xs text-slate-500 dark:text-slate-400">关注 · 捐赠</div>
                </div>
              </div>
            </div>

            <div class="flex items-center gap-3">
              <button 
                @click="toggleFavorite"
                class="p-2 rounded-full transition-colors"
                :class="isFavorited ? 'text-yellow-500 bg-yellow-50 dark:bg-yellow-900/20' : 'text-slate-400 hover:text-yellow-500 hover:bg-yellow-50 dark:hover:bg-yellow-900/20'"
                title="收藏"
              >
                <component :is="isFavorited ? StarIconSolid : StarIcon" class="w-6 h-6" />
              </button>
              <button class="p-2 text-slate-400 hover:text-indigo-600 hover:bg-indigo-50 dark:hover:bg-indigo-900/20 rounded-full transition-colors" title="添加到合集">
                <PlusCircleIcon class="w-6 h-6" />
              </button>
              
              <div class="h-6 w-px bg-slate-200 dark:bg-slate-700 mx-2"></div>

              <button 
                @click="downloadImage(displayedImage)"
                class="flex items-center gap-2 px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-lg transition-colors shadow-lg"
              >
                <span>免费下载</span>
                <ArrowDownTrayIcon class="w-5 h-5" />
              </button>
              
              <button 
                @click="handleClose"
                class="ml-4 p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-700 rounded-full transition-colors"
              >
                <XMarkIcon class="w-6 h-6" />
              </button>
            </div>
          </div>

          <!-- Modal Body -->
          <div class="flex-1 overflow-hidden flex flex-col lg:flex-row min-h-0">
            <!-- Image Area -->
            <div class="flex-1 relative flex items-center justify-center p-4 lg:p-10 bg-slate-100 dark:bg-slate-900 overflow-auto group">
              <!-- Navigation Buttons -->
              <button
                v-if="hasPrevImage"
                class="absolute left-4 z-10 p-3 rounded-full bg-white/20 hover:bg-white/40 text-white backdrop-blur-md transition-all opacity-0 group-hover:opacity-100"
                @click="goToPrevImage"
              >
                <ChevronLeftIcon class="w-6 h-6" />
              </button>

              <img 
                :src="(displayedImage.watermarkMinioUrl || displayedImage.thumbnailMinioUrl) ? ((displayedImage.watermarkMinioUrl || displayedImage.thumbnailMinioUrl)) : ''" 
                :alt="displayedImage.fileName" 
                class="max-w-full max-h-full object-contain shadow-2xl rounded-lg"
              >

              <button
                v-if="hasNextImage"
                class="absolute right-4 z-10 p-3 rounded-full bg-white/20 hover:bg-white/40 text-white backdrop-blur-md transition-all opacity-0 group-hover:opacity-100"
                @click="goToNextImage"
              >
                <ChevronRightIcon class="w-6 h-6" />
              </button>
            </div>

            <!-- Sidebar -->
            <div class="w-full lg:w-96 bg-white dark:bg-slate-800 border-t lg:border-t-0 lg:border-l border-slate-100 dark:border-slate-700 p-6 overflow-y-auto shrink-0">
              <div class="space-y-8">
                <!-- License Info -->
                <div class="p-4 bg-indigo-50 dark:bg-indigo-900/20 rounded-xl border border-indigo-100 dark:border-indigo-900/30">
                  <div class="flex items-start gap-3">
                    <InformationCircleIcon class="w-5 h-5 text-indigo-600 dark:text-indigo-400 mt-0.5 shrink-0" />
                    <div>
                      <h4 class="font-bold text-indigo-900 dark:text-indigo-300 text-sm">商业授权许可</h4>
                      <p class="text-xs text-indigo-700 dark:text-indigo-400 mt-1">此图片为水印预览图，如需原图请前往市场。</p>
                    </div>
                  </div>
                </div>

                <!-- Details -->
                <div>
                  <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">详细信息</h3>
                  <div class="space-y-3">
                    <div class="flex justify-between text-sm">
                      <span class="text-slate-500 dark:text-slate-400">分辨率</span>
                      <span class="font-medium text-slate-900 dark:text-slate-200">
                        {{ displayedImage.width && displayedImage.height ? `${displayedImage.width} x ${displayedImage.height}` : 'Watermark' }}
                      </span>
                    </div>
                    <div class="flex justify-between text-sm">
                      <span class="text-slate-500 dark:text-slate-400">文件大小</span>
                      <span class="font-medium text-slate-900 dark:text-slate-200">{{ formatBytes(displayedImage.size) }}</span>
                    </div>
                    <div class="flex justify-between text-sm">
                      <span class="text-slate-500 dark:text-slate-400">格式</span>
                      <span class="font-medium text-slate-900 dark:text-slate-200">{{ displayedImage.contentType || 'JPG' }}</span>
                    </div>
                    <div class="flex justify-between text-sm">
                      <span class="text-slate-500 dark:text-slate-400">上传时间</span>
                      <span class="font-medium text-slate-900 dark:text-slate-200">{{ formatDate(displayedImage.createTime || displayedImage.uploadTime) }}</span>
                    </div>
                  </div>
                </div>

                <!-- Description -->
                <div v-if="displayedImage.description">
                  <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">描述</h3>
                  <p class="text-sm text-slate-600 dark:text-slate-300 leading-relaxed">{{ displayedImage.description }}</p>
                </div>

                <!-- Tags -->
                <div v-if="displayedImage.description">
                  <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">标签</h3>
                  <div class="flex flex-wrap gap-2">
                    <span 
                      v-for="tag in getTags(displayedImage.description)" 
                      :key="tag"
                      class="px-3 py-1 bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 dark:hover:bg-slate-600 text-slate-600 dark:text-slate-300 text-xs rounded-full cursor-pointer transition-colors"
                    >
                      {{ tag }}
                    </span>
                  </div>
                </div>

                <!-- Actions -->
                <div class="pt-6 border-t border-slate-100 dark:border-slate-700 flex gap-4">
                  <button @click="handleShare" class="flex-1 flex items-center justify-center gap-2 py-2.5 border border-slate-200 dark:border-slate-600 rounded-lg text-sm font-medium text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-slate-900 dark:hover:text-white transition-colors">
                    <ShareIcon class="w-4 h-4" />
                    分享
                  </button>
                  <button @click="handleReport" class="flex-1 flex items-center justify-center gap-2 py-2.5 border border-slate-200 dark:border-slate-600 rounded-lg text-sm font-medium text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 hover:text-slate-900 dark:hover:text-white transition-colors">
                    <FlagIcon class="w-4 h-4" />
                    举报
                  </button>
                </div>

                <!-- Comments Section -->
                <div class="pt-6 border-t border-slate-100 dark:border-slate-700">
                  <h3 class="text-sm font-bold text-slate-900 dark:text-white uppercase tracking-wider mb-4">评论</h3>
                  
                  <!-- Comment Input -->
                  <div class="mb-6">
                    <textarea 
                      v-model="newComment" 
                      placeholder="写下你的评论..." 
                      class="w-full p-3 rounded-lg border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-900/50 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition-all resize-none"
                      rows="3"
                    ></textarea>
                    <div class="flex justify-end mt-2">
                      <button 
                        @click="submitComment" 
                        :disabled="!newComment.trim()"
                        class="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed text-white text-sm font-medium rounded-lg transition-colors"
                      >
                        发布
                      </button>
                    </div>
                  </div>

                  <!-- Comments List -->
                  <div class="space-y-4">
                    <div v-if="comments.length === 0" class="text-center text-slate-500 dark:text-slate-400 text-sm py-4">
                      暂无评论，快来抢沙发吧！
                    </div>
                    <div v-for="comment in comments" :key="comment.commentId" class="flex gap-3">
                      <div 
                        class="w-8 h-8 rounded-full flex items-center justify-center text-white text-xs font-bold shrink-0"
                        :style="{ backgroundColor: getRandomColor() }"
                      >
                        {{ comment.userName ? comment.userName.charAt(0).toUpperCase() : '?' }}
                      </div>
                      <div class="flex-1">
                        <div class="flex justify-between items-start">
                          <span class="text-sm font-medium text-slate-900 dark:text-white">{{ comment.userName }}</span>
                          <span class="text-xs text-slate-400">{{ formatDate(comment.createTime) }}</span>
                        </div>
                        <p class="text-sm text-slate-600 dark:text-slate-300 mt-1">{{ comment.content }}</p>
                        <button 
                          v-if="canDeleteComment(comment)"
                          @click="deleteComment(comment.commentId)"
                          class="text-xs text-red-500 hover:text-red-600 mt-1"
                        >
                          删除
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script lang="ts" setup>
import { ref, computed, watch, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  XMarkIcon,
  ArrowDownTrayIcon,
  StarIcon,
  PlusCircleIcon,
  InformationCircleIcon,
  FlagIcon,
  ShareIcon,
  ChevronLeftIcon,
  ChevronRightIcon
} from '@heroicons/vue/24/outline';
import { StarIcon as StarIconSolid } from '@heroicons/vue/24/solid';

// Interfaces
interface Image {
  imageId: string;
  minioUrl: string;
  watermarkMinioUrl: string | null;
  thumbnailMinioUrl: string | null;
  fileName: string;
  userId: string;
  contentType: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime?: string;
  createTime?: string;
  authorName?: string;
  width?: number;
  height?: number;
}

interface UserInfo {
  userId: string;
  userName: string;
  userEmail: string;
  userRole: string;
}

// Props definition
const props = defineProps<{
  modelValue: boolean; 
  image: Image | null; 
  images: Image[]; 
  initialIndex: number; 
}>();

// Emits definition
const emit = defineEmits(['update:modelValue', 'close', 'navigate']);

const API_BASE_URL = 'http://localhost:8080';
const router = useRouter();

// Cache for user info
const userCache = new Map<string, UserInfo>();

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
  return props.image;
});

// Local author name to handle async fetching
const currentAuthorName = ref('未知作者');
const isFavorited = ref(false);

const checkFavoriteStatus = async () => {
  if (!displayedImage.value?.imageId) return;
  try {
    const response = await request.get(`/api/favorites/check/${displayedImage.value.imageId}`);
    const res = response as any;
    if (res.code === 200) {
      isFavorited.value = res.data;
    }
  } catch (e) {
    console.error(e);
  }
};

const toggleFavorite = async () => {
  if (!displayedImage.value?.imageId) return;
  try {
    if (isFavorited.value) {
      await request.delete(`/api/favorites/${displayedImage.value.imageId}`);
      isFavorited.value = false;
      ElMessage.success('已取消收藏');
    } else {
      await request.post(`/api/favorites/${displayedImage.value.imageId}`);
      isFavorited.value = true;
      ElMessage.success('已收藏');
    }
  } catch (e) {
    ElMessage.error('操作失败');
  }
};

const fetchUserInfo = async (userId: string): Promise<UserInfo | null> => {
  if (userCache.has(userId)) return userCache.get(userId)!;

  try {
    const response = await request.get(`/api/user/getUserById`, {
      params: { userId }
    });
    const res = response as any;
    if (res.code === 200 && res.data) {
      const userInfo: UserInfo = res.data;
      userCache.set(userId, userInfo);
      return userInfo;
    }
    return null;
  } catch (err) {
    console.error(`Error fetching user info:`, err);
    return null;
  }
};

// Watch for image changes to sync author name
watch(() => displayedImage.value, async (newImg) => {
  if (newImg) {
    checkFavoriteStatus();
    if (newImg.authorName) {
      currentAuthorName.value = newImg.authorName;
    } else {
      currentAuthorName.value = '加载中...';
      const userInfo = await fetchUserInfo(newImg.userId);
      if (userInfo) {
        currentAuthorName.value = userInfo.userName;
      } else {
        currentAuthorName.value = '未知作者';
      }
    }
  }
}, { immediate: true });

// Watch for changes in initialIndex to update currentImageIndex
watch(() => props.initialIndex, (newIndex) => {
  currentImageIndex.value = newIndex;
}, { immediate: true });

// Handle body scroll lock
watch(dialogVisible, (visible) => {
  if (visible) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = '';
  }
});

onUnmounted(() => {
  document.body.style.overflow = '';
});

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

const goToUserProfile = (userId: string | undefined) => {
  if (userId) {
    handleClose();
    router.push(`/user/${userId}`);
  }
};

const getTags = (description: string | null) => {
  if (!description) return [];
  return description.split(' ').filter(tag => tag.length > 0);
};

// Function to generate a random color for the avatar background
const getRandomColor = () => {
  const colors = [
    '#6366f1', '#8b5cf6', '#ec4899', '#f43f5e', 
    '#f59e0b', '#10b981', '#06b6d4', '#3b82f6'
  ];
  return colors[Math.floor(Math.random() * colors.length)];
};

// Utility functions for metadata display
const formatDate = (dateString?: string) => {
  if (!dateString) return '未知';
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return '未知';
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric', month: 'long', day: 'numeric'
    });
  } catch (e) {
    return '未知';
  }
};

const formatBytes = (bytes?: number) => {
  if (bytes === undefined || bytes === null || bytes < 0) return '未知';
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

// Action handlers
const downloadImage = (image: Image | null) => {
  if (!image || !image.imageId) {
    ElMessage.warning('图片信息不完整，无法下载。');
    return;
  }

  const downloadUrl = `${API_BASE_URL}/api/images/watermark/${image.imageId}`;

  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName || 'image.jpg');

  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  ElMessage.success('图片已开始下载！');
};

const handleShare = () => {
  ElMessage.info('分享功能正在开发中...');
};

const handleReport = () => {
  ElMessage.info('举报功能正在开发中...');
};

// Comments Logic
interface Comment {
  commentId: string;
  imageId: string;
  userId: string;
  userName: string;
  content: string;
  createTime: string;
}

const comments = ref<Comment[]>([]);
const newComment = ref('');
const currentUser = ref<UserInfo | null>(null); // Need to know current user for delete permission

// Fetch current user info (simplified, ideally from store)
const fetchCurrentUser = async () => {
  // This is a placeholder. In a real app, you'd get this from a Pinia store or a dedicated API
  // For now, we rely on the backend to handle auth, but we need userId for UI logic (e.g. show delete button)
  // Let's try to get it from a profile endpoint if available, or parse token if we had one accessible here easily.
  // Assuming we have a user store:
  // const userStore = useUserStore();
  // currentUser.value = userStore.userInfo;
  
  // Or fetch from an endpoint that returns "me"
  try {
      const response = await request.get('/api/user/current');
      const res = response as any;
      if (res.code === 200) {
          currentUser.value = res.data;
      }
  } catch (e) {
      // Not logged in or error
  }
};

const fetchComments = async () => {
  if (!displayedImage.value?.imageId) return;
  try {
    const response = await request.get(`/api/comments/list/${displayedImage.value.imageId}`);
    const res = response as any;
    if (res.code === 200) {
      comments.value = res.data;
    }
  } catch (e) {
    console.error('Failed to fetch comments', e);
  }
};

const submitComment = async () => {
  if (!newComment.value.trim()) return;
  if (!displayedImage.value?.imageId) return;

  try {
    const response = await request.post('/api/comments/add', {
      imageId: displayedImage.value.imageId,
      content: newComment.value,
      // userName is optional, backend handles it or we send it if we know it
      userName: currentUser.value?.userName || 'User' 
    });
    const res = response as any;
    if (res.code === 200) {
      ElMessage.success('评论发布成功');
      newComment.value = '';
      fetchComments();
    } else {
      ElMessage.error(res.msg || '发布失败');
    }
  } catch (e) {
    ElMessage.error('发布失败，请先登录');
  }
};

const canDeleteComment = (comment: Comment) => {
  // Allow if current user is the comment author or admin (admin check logic might need more info)
  return currentUser.value && (currentUser.value.userId === comment.userId || currentUser.value.userRole === 'admin');
};

const deleteComment = async (commentId: string) => {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      const response = await request.delete(`/api/comments/delete/${commentId}`);
      const res = response as any;
      if (res.code === 200) {
        ElMessage.success('删除成功');
        fetchComments();
      } else {
        ElMessage.error(res.msg || '删除失败');
      }
    } catch (e) {
      ElMessage.error('删除失败');
    }
  });
};

// Watch displayedImage to fetch comments
watch(() => displayedImage.value, (newImg) => {
  if (newImg) {
    fetchComments();
  }
}, { immediate: true });

// Init
fetchCurrentUser();
</script>

<style scoped>
/* Modal Transition */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-active .relative,
.modal-leave-active .relative {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .relative,
.modal-leave-to .relative {
  transform: scale(0.95);
  opacity: 0;
}
</style>
