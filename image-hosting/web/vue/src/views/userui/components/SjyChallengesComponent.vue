<template>
  <div class="py-12 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto transition-colors duration-300">
    <!-- Header Section -->
    <div class="text-center mb-16">
      <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-amber-50 dark:bg-amber-900/30 border border-amber-100 dark:border-amber-800 text-amber-600 dark:text-amber-400 text-xs font-semibold mb-4">
        <TrophyIcon class="w-4 h-4" />
        社区挑战赛
      </div>
      <h1 class="text-4xl md:text-5xl font-bold text-slate-900 dark:text-white mb-4 tracking-tight">
        探索热门挑战
      </h1>
      <p class="text-lg text-slate-600 dark:text-slate-400 max-w-2xl mx-auto leading-relaxed">
        参与社区发起的各类主题挑战，展示您的创意，赢取专属勋章与奖励。
      </p>
    </div>

    <!-- Challenges List -->
    <div v-if="challenges.length > 0" class="grid grid-cols-1 gap-12">
      <div 
        v-for="challenge in challenges" 
        :key="challenge.id" 
        class="bg-white dark:bg-slate-800 rounded-3xl overflow-hidden border border-slate-100 dark:border-slate-700 shadow-sm hover:shadow-xl transition-all duration-300 group"
      >
        <div class="flex flex-col lg:flex-row">
          <!-- Left: Media Grid -->
          <div class="lg:w-1/2 p-6">
            <div class="grid grid-cols-2 grid-rows-2 gap-3 h-[300px] md:h-[400px]">
              <div 
                v-if="challenge.previewMedia[0]"
                class="col-span-2 row-span-1 rounded-2xl overflow-hidden bg-slate-100 dark:bg-slate-900"
              >
                <img 
                  :src="challenge.previewMedia[0].minioUrl" 
                  :alt="challenge.previewMedia[0].alt" 
                  class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                >
              </div>
              <div 
                v-if="challenge.previewMedia[1]"
                class="col-span-1 row-span-1 rounded-2xl overflow-hidden bg-slate-100 dark:bg-slate-900"
              >
                <img 
                  :src="challenge.previewMedia[1].minioUrl" 
                  :alt="challenge.previewMedia[1].alt" 
                  class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                >
              </div>
              <div 
                v-if="challenge.previewMedia[2]"
                class="col-span-1 row-span-1 rounded-2xl overflow-hidden bg-slate-100 dark:bg-slate-900"
              >
                <img 
                  :src="challenge.previewMedia[2].minioUrl" 
                  :alt="challenge.previewMedia[2].alt" 
                  class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                >
              </div>
              <!-- Placeholder if less than 3 images -->
              <div 
                v-if="challenge.previewMedia.length < 3"
                class="col-span-1 row-span-1 rounded-2xl bg-slate-50 dark:bg-slate-900/50 border-2 border-dashed border-slate-200 dark:border-slate-700 flex items-center justify-center"
              >
                <PhotoIcon class="w-8 h-8 text-slate-300 dark:text-slate-600" />
              </div>
            </div>
          </div>

          <!-- Right: Details -->
          <div class="lg:w-1/2 p-8 md:p-12 flex flex-col justify-center">
            <div class="flex items-center gap-2 text-indigo-600 dark:text-indigo-400 text-sm font-bold mb-4 uppercase tracking-widest">
              <span class="w-8 h-px bg-indigo-600 dark:bg-indigo-400"></span>
              进行中
            </div>
            <h2 class="text-3xl font-bold text-slate-900 dark:text-white mb-4 group-hover:text-indigo-600 dark:group-hover:text-indigo-400 transition-colors">
              {{ challenge.name }}
            </h2>
            <p class="text-slate-600 dark:text-slate-400 text-lg leading-relaxed mb-8">
              {{ challenge.description }}
            </p>

            <!-- Participants -->
            <div v-if="challenge.participants.length > 0" class="flex items-center gap-4 mb-10">
              <div class="flex -space-x-3 overflow-hidden">
                <div 
                  v-for="(participant, pIndex) in challenge.participants.slice(0, 4)"
                  :key="pIndex"
                  class="inline-flex items-center justify-center h-10 w-10 rounded-full ring-2 ring-white dark:ring-slate-800 text-white text-xs font-bold uppercase"
                  :class="getAvatarBg(participant.name)"
                  :title="participant.name"
                >
                  {{ participant.name.charAt(0) }}
                </div>
                <div 
                  v-if="challenge.participants.length > 4"
                  class="flex items-center justify-center h-10 w-10 rounded-full bg-slate-100 dark:bg-slate-700 ring-2 ring-white dark:ring-slate-800 text-xs font-bold text-slate-600 dark:text-slate-300"
                >
                  +{{ challenge.participants.length - 4 }}
                </div>
              </div>
              <div class="text-sm text-slate-500 dark:text-slate-400">
                <span class="font-bold text-slate-900 dark:text-white">{{ challenge.participants.length }}</span> 名成员已加入
              </div>
            </div>

            <div class="flex flex-wrap gap-4">
              <button class="px-8 py-3 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-xl transition-all shadow-lg shadow-indigo-500/25 flex items-center gap-2">
                立即参加
                <ArrowRightIcon class="w-5 h-5" />
              </button>
              <button class="px-8 py-3 bg-white dark:bg-slate-700 border border-slate-200 dark:border-slate-600 text-slate-700 dark:text-slate-200 font-bold rounded-xl hover:bg-slate-50 dark:hover:bg-slate-600 transition-all">
                了解更多
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- States -->
    <div v-if="isLoadingChallenges" class="py-32 flex flex-col items-center justify-center">
      <div class="animate-spin rounded-full h-12 w-12 border-4 border-indigo-100 border-t-indigo-600 mb-4"></div>
      <p class="text-slate-500 dark:text-slate-400 animate-pulse">加载挑战赛数据...</p>
    </div>

    <div v-else-if="errorChallenges" class="py-20 text-center">
      <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-red-50 dark:bg-red-900/20 text-red-500 mb-4">
        <InformationCircleIcon class="w-8 h-8" />
      </div>
      <h3 class="text-lg font-bold text-slate-900 dark:text-white">出错了</h3>
      <p class="text-slate-500 dark:text-slate-400 mt-2">{{ errorChallenges }}</p>
      <button @click="fetchChallenges" class="mt-6 px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors">重试</button>
    </div>

    <div v-else-if="challenges.length === 0" class="py-32 text-center">
      <div class="inline-flex items-center justify-center w-20 h-20 rounded-full bg-slate-100 dark:bg-slate-800 mb-6">
        <TrophyIcon class="w-10 h-10 text-slate-400" />
      </div>
      <h3 class="text-xl font-bold text-slate-900 dark:text-white">暂无挑战赛</h3>
      <p class="text-slate-500 dark:text-slate-400 mt-2">新的挑战即将开启，敬请期待！</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { 
  TrophyIcon, 
  PhotoIcon, 
  ArrowRightIcon, 
  InformationCircleIcon 
} from '@heroicons/vue/24/outline';

interface Participant {
  name: string;
}

interface PreviewMedia {
  id: string;
  minioUrl: string;
  alt: string;
}

interface Challenge {
  id: string;
  name: string;
  description: string;
  participants: Participant[];
  previewMedia: PreviewMedia[];
}

const challenges = ref<Challenge[]>([]);
const isLoadingChallenges = ref(true);
const errorChallenges = ref<string | null>(null);

const avatarBgs = [
  'bg-pink-500', 'bg-purple-500', 'bg-indigo-500', 
  'bg-blue-500', 'bg-cyan-500', 'bg-teal-500', 
  'bg-emerald-500', 'bg-amber-500', 'bg-orange-500'
];

const getAvatarBg = (name: string) => {
  const index = name.length % avatarBgs.length;
  return avatarBgs[index];
};

const fetchChallenges = async () => {
  isLoadingChallenges.value = true;
  errorChallenges.value = null;
  
  try {
    // Fetch public images to use as preview media for challenges
    const response = await axios.get('http://localhost:8080/api/images/public');
    const publicImages = response.data.data || [];

    // Mock challenges using real image data
    const mockChallenges: Challenge[] = [
      {
        id: 'challenge-1',
        name: "城市光影：夜色中的霓虹",
        description: "捕捉城市夜晚最迷人的瞬间。无论是繁华商业街的霓虹灯火，还是寂静小巷的昏黄路灯，分享你眼中的城市夜色。",
        participants: [
          { name: "Alex" }, { name: "Sarah" }, { name: "Mike" }, 
          { name: "Elena" }, { name: "David" }, { name: "Luna" }
        ],
        previewMedia: publicImages.slice(0, 3).map((img: any) => ({
          id: img.imageId,
          minioUrl: img.watermarkMinioUrl || img.minioUrl,
          alt: img.description || img.fileName
        }))
      },
      {
        id: 'challenge-2',
        name: "极简主义：少即是多",
        description: "通过构图、线条和色彩的极致简化，表达深刻的意境。挑战用最简单的元素讲述最动人的故事。",
        participants: [
          { name: "Kevin" }, { name: "Julia" }, { name: "Tom" }, { name: "Anna" }
        ],
        previewMedia: publicImages.slice(3, 6).map((img: any) => ({
          id: img.imageId,
          minioUrl: img.watermarkMinioUrl || img.minioUrl,
          alt: img.description || img.fileName
        }))
      },
      {
        id: 'challenge-3',
        name: "自然微距：微观世界",
        description: "探索大自然中那些容易被忽视的细节。从花瓣的纹理到昆虫的复眼，带我们进入奇妙的微观世界。",
        participants: [
          { name: "Sophie" }, { name: "Ryan" }, { name: "Chloe" }
        ],
        previewMedia: publicImages.slice(6, 9).map((img: any) => ({
          id: img.imageId,
          minioUrl: img.watermarkMinioUrl || img.minioUrl,
          alt: img.description || img.fileName
        }))
      }
    ];

    challenges.value = mockChallenges;
  } catch (err) {
    console.error('Failed to fetch challenges:', err);
    errorChallenges.value = '无法加载挑战赛数据，请稍后再试。';
  } finally {
    isLoadingChallenges.value = false;
  }
};

onMounted(() => {
  fetchChallenges();
});
</script>

<style scoped>
/* No extra styles needed as we use Tailwind */
</style>
