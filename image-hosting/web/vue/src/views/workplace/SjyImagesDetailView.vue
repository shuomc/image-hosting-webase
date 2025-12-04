<template>
  <div class="min-h-screen w-full h-full relative transition-colors">
    <div class="relative z-10 container mx-auto px-4 py-1 max-w-6xl">
      <div class="flex items-center justify-between mb-8 gap-4">
        <div class="flex items-center gap-4">
          <button @click="$router.back()" class="p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-500 dark:text-slate-400 transition-all duration-200 -translate-y-0 hover:-translate-y-1 hover:shadow-md">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M15 18l-6-6 6-6"/></svg>
          </button>
          <div>
            <h1 class="text-3xl font-extrabold tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 to-purple-500">图片详情</h1>
            <p class="text-sm mt-1 dark:text-slate-300 text-slate-500">查看并管理图片的详细信息与链接</p>
          </div>
        </div>
      </div>

      <div v-if="loading" class="w-full h-40 flex items-center justify-center text-slate-400">加载中…</div>
      <div v-else-if="error" class="w-full text-center text-red-500">加载图片详情失败: {{ error.message }}</div>

      <div v-else-if="imageDetail" class="grid grid-cols-1 md:grid-cols-3 gap-6">
        
        <div class="md:col-span-2 bg-slate-100 rounded-2xl shadow-xl border border-slate-100 overflow-hidden dark:bg-slate-800 dark:border-slate-700">
          <div class="relative w-full aspect-[4/3] bg-white overflow-hidden dark:bg-slate-900 dark:border-slate-700">
            <img ref="previewImgRef" :src="imageDetail.thumbnailMinioUrl" :alt="imageDetail.fileName" class="w-full h-full object-contain dark:bg-white/5" />
          </div>
        </div>
        <div class="md:col-span-1 bg-white rounded-2xl shadow-xl border border-slate-100 p-6 flex flex-col justify-between dark:bg-slate-800 dark:border-slate-700">
          <div>
            <div class="flex items-start justify-between gap-2 mb-2">
              <div class="flex-grow min-w-0">
                <input v-if="isEditing" 
                        v-model="editForm.fileName"
                        type="text"
                        class="w-full px-2 py-1 text-lg font-bold border rounded-lg outline-none bg-slate-50 border-slate-300 focus:ring-2 focus:ring-indigo-500 dark:bg-slate-900 dark:border-slate-600 dark:text-white"
                        placeholder="请输入文件名"
                />
                <h2 v-else class="font-bold text-lg text-slate-800 dark:text-white truncate" :title="imageDetail.fileName">
                  {{ imageDetail.fileName }}
                </h2>
              </div>
              <button @click="toggleEditMode" 
                      :disabled="isUpdating"
                      class="flex-shrink-0 p-2 ml-1 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors"
                      :class="isEditing ? 'text-green-600 bg-green-50 dark:bg-green-900/30 dark:text-green-400' : 'text-slate-400 hover:text-indigo-600 dark:text-slate-500 dark:hover:text-indigo-400'">
                <svg v-if="isUpdating" class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <CheckIcon v-else-if="isEditing" class="w-5 h-5" />
                <PencilSquareIcon v-else class="w-5 h-5" />
              </button>
            </div>

            <div class="mb-4">
              <textarea v-if="isEditing"
                         v-model="editForm.description"
                         rows="3"
                         class="w-full px-2 py-2 text-sm border rounded-lg outline-none resize-none bg-slate-50 border-slate-300 focus:ring-2 focus:ring-indigo-500 dark:bg-slate-900 dark:border-slate-600 dark:text-slate-300"
                         placeholder="请输入图片描述..."
              ></textarea>
              <p v-else class="text-sm text-slate-500 dark:text-slate-300 h-auto min-h-[3rem] break-words">
                {{ imageDetail.description || '暂无描述信息' }}
              </p>
            </div>

            <div class="space-y-2 text-sm text-slate-500 dark:text-slate-300 border-t border-slate-100 dark:border-slate-700 pt-4">
              <div class="grid grid-cols-1 gap-y-4">
                <div><span class="font-medium text-slate-700 dark:text-slate-200">图片ID:</span> {{ imageDetail.imageId}}</div>
                <div><span class="font-medium text-slate-700 dark:text-slate-200">上传用户:</span> {{ imageDetail.userId}}</div>
                <div><span class="font-medium text-slate-700 dark:text-slate-200">类型:</span> {{ imageDetail.contentType }}</div>
                <div><span class="font-medium text-slate-700 dark:text-slate-200">大小:</span> {{ formatBytes(imageDetail.size) }}</div>
                <div><span class="font-medium text-slate-700 dark:text-slate-200">尺寸:</span> {{ imageDetail.width }}x{{ imageDetail.height }}</div>
                
                <div class="flex items-center">
                  <span class="font-medium text-slate-700 dark:text-slate-200 mr-2">公开:</span>
                  <div v-if="isEditing" @click.stop>
                       <el-switch 
                          v-model="editForm.isPublic" 
                          size="small"
                          style="--el-switch-on-color: #6366f1; --el-switch-off-color: #94a3b8"
                      />
                       <span class="ml-2 text-xs text-slate-400">{{ editForm.isPublic ? '公开可见' : '私有' }}</span>
                  </div>
                  <span v-else class="ml-1">{{ imageDetail.isPublic ? '是' : '否' }}</span>
                </div>
              </div>
              
              <div v-if="imageDetail.uploadTime"><span class="font-medium text-slate-700 dark:text-slate-200">上传时间:</span> {{ formatTimestamp(imageDetail.uploadTime) }}</div>
            </div>
          </div>

          <div class="mt-6 flex flex-col gap-3">
            
            <button @click="showOriginal" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-green-50 dark:bg-blue-500/10 text-blue-700 dark:text-blue-400 hover:bg-blue-100 dark:hover:bg-blue-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-green-500/20">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 5C5.636 5 2 12 2 12s3.636 7 10 7 10-7 10-7S18.364 5 12 5z"/><circle cx="12" cy="12" r="3"/></svg>
              查看原图
            </button>
            <button @click="downloadImage(imageDetail)" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-green-50 dark:bg-green-500/10 text-green-700 dark:text-green-400 hover:bg-green-100 dark:hover:bg-green-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-green-500/20">
              <ArrowDownTrayIcon class="w-5 h-5" />
              下载原图
            </button>

            <!-- 铸造 NFT 按钮 (逻辑修改) -->
            <button @click="handleShowMintDialog" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-indigo-50 dark:bg-indigo-500/10 text-indigo-700 dark:text-indigo-400 hover:bg-indigo-100 dark:hover:bg-indigo-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-indigo-500/20">
              <SparklesIcon class="w-5 h-5" />
              铸造 NFT
            </button>

            <button @click="deleteImage(imageDetail)" :disabled="isDeleting" class="w-full flex items-center justify-center gap-2 px-4 py-2 rounded-xl bg-red-50 dark:bg-red-500/10 text-red-700 dark:text-red-400 hover:bg-red-100 dark:hover:bg-red-500/20 font-medium transition-all duration-200 -translate-y-0 hover:shadow-md dark:hover:shadow-red-500/20 disabled:opacity-50 disabled:hover:translate-y-0 disabled:hover:shadow-none">
              <TrashIcon class="w-5 h-5" />
              {{ isDeleting ? '删除中...' : '删除图片' }}
            </button>
          </div>
        </div>
      </div>

      <div v-if="imageDetail" class="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="md:col-span-2 bg-white rounded-2xl shadow-xl border border-slate-100 p-6 dark:bg-slate-800 dark:border-slate-700">
          <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-4">扩展元数据</h3>
          
          <div class="flex border-b border-slate-200 dark:border-slate-700 mb-4">
            <button v-for="tab in ['EXIF/相机', 'GPS/位置', '统计/分析']" 
                    :key="tab"
                    @click="activeTab = tab"
                    class="py-2 px-4 text-sm font-medium transition-colors"
                    :class="activeTab === tab 
                        ? 'text-indigo-600 border-b-2 border-indigo-600 dark:text-indigo-400 dark:border-indigo-400'
                        : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'">
              {{ tab }}
            </button>
          </div>

          <div class="space-y-4 text-sm text-slate-600 dark:text-slate-300">
            <div v-if="activeTab === 'EXIF/相机'" class="grid grid-cols-2 gap-4">
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">相机厂商 (Make):</span>
                <span class="text-xs">{{ imageDetail.cameraMake || 'N/A' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">相机型号 (Model):</span>
                <span class="text-xs">{{ imageDetail.cameraModel || 'N/A' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">镜头型号 (Lens):</span>
                <span class="text-xs">{{ imageDetail.lensModel || 'N/A' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">拍摄时间 (Time):</span>
                <span class="text-xs">{{ formatTimestamp(imageDetail.shootTime) }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">焦距 (Focal Length):</span>
                <span class="text-xs">{{ formatFocalLength(imageDetail.focalLength) }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">光圈 (Aperture):</span>
                <span class="text-xs">{{ formatAperture(imageDetail.aperture) }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">快门速度 (Shutter Speed):</span>
                <span class="text-xs">{{ formatShutterSpeed(imageDetail.shutterSpeed) }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">感光度 (ISO):</span>
                <span class="text-xs">{{ imageDetail.iso || 'N/A' }}</span>
              </div>
            </div>

            <div v-else-if="activeTab === 'GPS/位置'" class="grid grid-cols-2 gap-4">
              <div class="flex flex-col col-span-2">
                <span class="font-medium text-slate-700 dark:text-slate-200">位置名称 (Name):</span>
                <span class="text-xs">{{ imageDetail.locationName || '未标记位置信息' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">纬度 (Latitude):</span>
                <span class="text-xs">{{ imageDetail.latitude || 'N/A' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">经度 (Longitude):</span>
                <span class="text-xs">{{ imageDetail.longitude || 'N/A' }}</span>
              </div>
              <div class="col-span-2 pt-2 border-t border-slate-100 dark:border-slate-700">
                  <a v-if="imageDetail.latitude && imageDetail.longitude" 
                    :href="getMapLink(imageDetail.latitude, imageDetail.longitude)" 
                    target="_blank" 
                    class="text-indigo-500 hover:text-indigo-400 font-medium text-xs flex items-center gap-1">
                      在地图上查看位置
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10.828 10l-4.95-4.95a.75.75 0 011.06-1.06l5.5 5.5a.75.75 0 010 1.06l-5.5 5.5a.75.75 0 11-1.06-1.06L10.828 10z" clip-rule="evenodd" /></svg>
                  </a>
              </div>
            </div>

            <div v-else-if="activeTab === '统计/分析'" class="grid grid-cols-2 gap-4">
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">浏览次数 (Views):</span>
                <span class="text-xs">{{ imageDetail.viewCount !== undefined ? imageDetail.viewCount.toLocaleString() : '0' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">下载次数 (Downloads):</span>
                <span class="text-xs">{{ imageDetail.downloadCount !== undefined ? imageDetail.downloadCount.toLocaleString() : '0' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">点赞次数 (Likes):</span>
                <span class="text-xs">{{ imageDetail.likeCount !== undefined ? imageDetail.likeCount.toLocaleString() : '0' }}</span>
              </div>
              <div class="flex flex-col">
                <span class="font-medium text-slate-700 dark:text-slate-200">图片分类 (Category):</span>
                <span class="text-xs">{{ imageDetail.category || '未分类' }}</span>
              </div>
              
              <div class="flex flex-col col-span-2">
                <span class="font-medium text-slate-700 dark:text-slate-200">主色调 (Dominant Color):</span>
                <div class="flex items-center gap-2 mt-1">
                  <div class="w-8 h-4 rounded-sm border border-slate-300 dark:border-slate-600" :style="{ backgroundColor: imageDetail.dominantColor || '#ffffff' }"></div>
                  <span class="text-xs font-mono">{{ imageDetail.dominantColor || 'N/A' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="md:col-span-1 bg-white rounded-2xl shadow-xl border border-slate-100 p-6 dark:bg-slate-800 dark:border-slate-700">
          <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-4">使用链接</h3>
          <div class="space-y-4">
            <div v-for="(label, idx) in ['直链','Markdown','HTML','BBCode','CSS 背景图']" :key="idx">
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">{{ label }}:</label>
              <div class="flex items-center gap-2">
                <input type="text" :value="getLinkByLabel(label)" readonly class="flex-grow rounded-lg px-3 py-2 text-sm bg-slate-100 dark:bg-slate-900 text-slate-700 dark:text-slate-200 truncate border border-slate-200 dark:border-slate-700" />
                <button @click="copyToClipboard(getLinkByLabel(label))" class="px-3 py-2 rounded-lg bg-indigo-500 text-white hover:bg-indigo-600 text-sm duration-200">复制</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <Teleport to="body">
      <Transition
        enter-active-class="transition ease-out duration-300"
        enter-from-class="opacity-0"
        enter-to-class="opacity-100"
        leave-active-class="transition ease-in duration-200"
        leave-from-class="opacity-100"
        leave-to-class="opacity-0"
      >
        <div v-if="modalVisible" 
             class="fixed inset-0 z-[1000] flex items-center justify-center p-4 sm:p-8"
             @click.self="closeModal">
          
          <!-- 模糊背景 -->
          <div class="absolute inset-0 bg-slate-900/95 backdrop-blur-md transition-opacity" @click="closeModal"></div>

          <!-- 关闭按钮 -->
          <button class="absolute top-4 right-4 sm:top-6 sm:right-6 z-[1010] p-2.5 rounded-full bg-white/10 hover:bg-white/20 text-slate-300 hover:text-white border border-white/5 backdrop-blur-sm transition-all duration-200 group" 
                  @click="closeModal"
                  title="关闭预览">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 transition-transform duration-300 group-hover:rotate-90" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>

          <!-- 图片容器 -->
          <div class="relative w-full h-full flex flex-col items-center justify-center pointer-events-none z-10">

            <!-- 加载状态 -->
            <div v-if="originalImageLoading" class="absolute inset-0 flex items-center justify-center z-20">
              <div class="flex flex-col items-center gap-3 p-6 rounded-2xl bg-slate-800/50 backdrop-blur-md border border-white/10 shadow-2xl">
                <svg class="animate-spin h-10 w-10 text-indigo-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <span class="text-sm font-medium text-slate-300 tracking-wide">正在加载原图...</span>
              </div>
            </div>
            
            <!-- 图片本体 -->
            <img 
              v-if="presignedUrl"
              :src="presignedUrl" 
              :alt="imageDetail?.fileName || 'Original Image'" 
              class="pointer-events-auto max-w-full max-h-[85vh] object-contain rounded-lg shadow-2xl ring-1 ring-white/10 transition-all duration-500 select-none"
              :class="originalImageLoading ? 'opacity-0 scale-95' : 'opacity-100 scale-100'"
            />

            <!-- 底部悬浮栏 (文件名 & 下载) -->
            <div v-if="!originalImageLoading && presignedUrl && imageDetail" 
                 class="pointer-events-auto mt-6 px-6 py-3 bg-slate-900/80 backdrop-blur-xl rounded-full border border-white/10 flex items-center gap-4 shadow-2xl transition-all hover:bg-slate-900">
              
              <span class="text-sm font-medium text-slate-200 max-w-[150px] sm:max-w-[300px] truncate" :title="imageDetail.fileName">
                {{ imageDetail.fileName }}
              </span>
              
              <div class="w-px h-4 bg-white/20"></div>
              
              <a :href="presignedUrl" 
                 :download="imageDetail.fileName" 
                 target="_blank"
                 class="flex items-center gap-1.5 text-sm font-medium text-indigo-400 hover:text-indigo-300 transition-colors group">
                 <LinkIcon class="w-4 h-4 transition-transform group-hover:-translate-y-0.5" />
                 访问图片地址
              </a>
            </div>

          </div>
        </div>
      </Transition>
    </Teleport>

    <!-- 模态框 2: 铸造 NFT (新增) -->
    <Teleport to="body">
      <Transition enter-active-class="transition ease-out duration-300" enter-from-class="opacity-0" enter-to-class="opacity-100" leave-active-class="transition ease-in duration-200" leave-from-class="opacity-100" leave-to-class="opacity-0">
        <div v-if="mintDialogVisible" class="fixed inset-0 z-[1000] flex items-center justify-center p-4" @click.self="mintDialogVisible = false">
          
          <!-- 背景遮罩 -->
          <div class="absolute inset-0 bg-slate-900/80 backdrop-blur-sm transition-opacity" @click="mintDialogVisible = false"></div>

          <!-- 弹窗内容 -->
          <div class="relative w-full max-w-md bg-white dark:bg-slate-800 rounded-2xl shadow-2xl overflow-hidden border border-slate-100 dark:border-slate-700 transform transition-all z-10">
            
            <!-- 标题栏 -->
            <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-700 flex items-center justify-between bg-slate-50 dark:bg-slate-800/50">
              <div class="flex items-center gap-2">
                <div class="p-1.5 bg-indigo-100 dark:bg-indigo-900/30 rounded-lg text-indigo-600 dark:text-indigo-400">
                  <SparklesIcon class="w-5 h-5" />
                </div>
                <h3 class="text-lg font-bold text-slate-800 dark:text-white">铸造数字资产 (NFT)</h3>
              </div>
              <button @click="mintDialogVisible = false" class="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 transition">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
              </button>
            </div>

            <!-- 表单内容 -->
            <div class="p-6 space-y-4">
              <div class="bg-indigo-50 dark:bg-indigo-900/20 p-3 rounded-lg text-xs text-indigo-600 dark:text-indigo-300">
                铸造将把此图片的数字指纹写入区块链，确立您的所有权。
              </div>

              <div>
                <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">资产名称</label>
                <input type="text" v-model="mintForm.name" class="w-full px-3 py-2 bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition" placeholder="给您的 NFT 起个名字">
              </div>

              <div>
                <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">描述</label>
                <textarea v-model="mintForm.description" rows="3" class="w-full px-3 py-2 bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition resize-none" placeholder="描述这个资产..."></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">初始价格 (ETH)</label>
                <input type="number" v-model.number="mintForm.price" step="0.0001" min="0" class="w-full px-3 py-2 bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition">
                <p class="text-xs text-slate-400 mt-1">设置为 0 则表示暂不出售，仅铸造到仓库。</p>
              </div>
            </div>

            <!-- 底部按钮 -->
            <div class="px-6 py-4 border-t border-slate-100 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50 flex justify-end gap-3">
              <button @click="mintDialogVisible = false" class="px-4 py-2 rounded-lg text-slate-600 hover:bg-slate-200 dark:text-slate-300 dark:hover:bg-slate-700 transition text-sm font-medium">取消</button>
              <button 
                @click="handleMint" 
                :disabled="minting"
                class="px-4 py-2 rounded-lg bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-md hover:shadow-lg hover:from-indigo-500 hover:to-purple-500 transition-all text-sm font-medium flex items-center gap-2 disabled:opacity-70 disabled:cursor-not-allowed"
              >
                <svg v-if="minting" class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
                {{ minting ? '铸造中...' : '确认铸造' }}
              </button>
            </div>

          </div>
        </div>
      </Transition>
    </Teleport>

  </div>
</template>

<script setup lang="ts">
import { ref, watch, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import service from '@/utils/request';
import { API_BASE_URL } from '@/config';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '@/stores/user';
import { ArrowDownTrayIcon, SparklesIcon, TrashIcon, PencilSquareIcon, CheckIcon, LinkIcon } from '@heroicons/vue/24/outline';
// 引入 NFT 相关 API (请确保这些已在 api/nft.ts 定义)
import { checkRegistrationStatus, mintNFT } from '@/api/nft';

// === 1. 扩展接口定义 (新增所有 EXIF/GPS/Analysis 字段) ===
interface Image {
  // 基础信息
  imageId: string;
  thumbnailMinioUrl: string;
  watermarkMinioUrl: string;
  originMinioUrl: string;
  fileName: string;
  userId: string;
  contentType: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime?: string; // 基础时间

  // 尺寸
  width: number | null;
  height: number | null;

  // EXIF 元数据
  cameraMake: string | null;
  cameraModel: string | null;
  lensModel: string | null;
  focalLength: string | null; // e.g. "50.0 mm"
  aperture: string | null; // e.g. "f/2.8"
  shutterSpeed: string | null; // e.g. "1/100s"
  iso: number | null;
  shootTime: string | null; // 专业的拍摄时间，后端可能是 LocalDateTime/String

  // 地理位置
  locationName: string | null;
  latitude: number | null;
  longitude: number | null;

  // 统计与分类
  viewCount: number | null;
  downloadCount: number | null;
  likeCount: number | null;
  category: string | null;
  dominantColor: string | null; // e.g. "#001B4B"
}

// === 2. 状态定义 ===
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const imageDetail = ref<Image | null>(null);
const loading = ref(true);
const error = ref<Error | null>(null);
const isDeleting = ref(false);
// **新增：原图模态框状态**
const modalVisible = ref(false);
const presignedUrl = ref('');
const originalImageLoading = ref(false);

// 编辑状态
const isEditing = ref(false);
const isUpdating = ref(false); 
const editForm = reactive({
    fileName: '',
    description: '',
    isPublic: false
});

// 新增：元数据 Tab 状态
const activeTab = ref('EXIF/相机');

// === 新增：铸造相关状态 ===
const mintDialogVisible = ref(false);
const minting = ref(false);
const mintForm = reactive({
  name: '',
  description: '',
  price: 0
});

// 定义mint图片的引用
const previewImgRef = ref<HTMLImageElement | null>(null);

// === 3. 核心业务逻辑 (保持不变或微调) ===

const toggleEditMode = async () => {
    if (isEditing.value) {
        await handleUpdateImage();
    } else {
        if (imageDetail.value) {
            editForm.fileName = imageDetail.value.fileName;
            editForm.description = imageDetail.value.description || '';
            editForm.isPublic = imageDetail.value.isPublic;
        }
        isEditing.value = true;
    }
};

const handleUpdateImage = async () => {
    if (!imageDetail.value) return;
    
    if (!editForm.fileName.trim()) {
        ElMessage.warning('文件名不能为空');
        return;
    }

    isUpdating.value = true;

    try {
        const formData = new FormData();
        formData.append('imageId', imageDetail.value.imageId);
        formData.append('fileName', editForm.fileName);
        formData.append('description', editForm.description);
        formData.append('isPublic', editForm.isPublic.toString());

        const updateUrl = `${API_BASE_URL}/api/images/update`;
        
        const responseData = await service.post(updateUrl, formData);

        if (responseData.code === 200) {
            ElMessage.success('图片信息修改成功');
            
            // 更新本地视图数据
            imageDetail.value.fileName = editForm.fileName;
            imageDetail.value.description = editForm.description;
            imageDetail.value.isPublic = editForm.isPublic;
            
            isEditing.value = false;
        } else {
            ElMessage.error(responseData.msg || '修改失败');
        }
    } catch (err: any) {
        console.error('更新图片信息失败:', err);
    } finally {
        isUpdating.value = false;
    }
};

// ... 原有的下载、删除、Mint 逻辑保持不变 ...
const downloadImage = (image: Image) => {
  if (!image || !image.imageId || !image.fileName) {
    ElMessage.warning('图片信息不完整，无法下载。');
    return;
  }
  const downloadUrl = `${API_BASE_URL}/api/images/minio/${image.imageId}`;
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.setAttribute('download', image.fileName);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

const deleteImage = async (image: Image) => {
  if (!image || !image.imageId) {
    ElMessage.warning('图片信息不完整，无法删除。');
    return;
  }

  try {
    await ElMessageBox.confirm(`确定要删除图片 "${image.fileName}" 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });

    isDeleting.value = true;
    const deleteUrl = `${API_BASE_URL}/api/images/deleteById/${image.imageId}`;
    const responseData = await service.post(deleteUrl);

    if (responseData.code === 200) {
      ElMessage.success(responseData.msg || '删除成功');
      router.push({ name: 'MyImages' });
    } else {
       console.error('图片删除业务失败:', responseData.msg);
       ElMessage.error(responseData.msg || '删除失败');
    }

  } catch (error: any) {
    if (error !== 'cancel') {
        console.error('图片删除请求或确认框错误:', error);
    } 
  } finally {
    isDeleting.value = false;
  }
};

// === 新增：显示铸造弹窗前的逻辑 ===
const handleShowMintDialog = async () => {
  if (!imageDetail.value) return;

  // 1. 检查是否已注册区块链账户
  try {
    const res = await checkRegistrationStatus();
    // 假设 API 返回 { code: 200, data: { isRegistered: true/false } }
    const isRegistered = res.data && res.data.isRegistered;

    if (isRegistered) {
      // 已注册 -> 初始化表单并显示弹窗
      mintForm.name = imageDetail.value.fileName || '';
      mintForm.description = imageDetail.value.description || '';
      mintForm.price = 0;
      mintDialogVisible.value = true;
    } else {
      // 未注册 -> 提示跳转
      ElMessageBox.confirm(
        '您尚未激活区块链账户，无法进行铸造操作。激活后即可拥有专属钱包地址。',
        '需要激活账户',
        {
          confirmButtonText: '立即激活',
          cancelButtonText: '稍后再说',
          type: 'info',
          center: true
        }
      ).then(() => {
        // 跳转到 MyNFT 页面 (那里有一键激活功能)
        router.push({ name: 'MyNFT' });
      }).catch(() => {
        // 取消操作
      });
    }
  } catch (err) {
    console.error('检查区块链账户状态失败:', err);
    ElMessage.error('无法连接区块链服务，请稍后重试');
  }
};

// === 执行铸造逻辑 ===
const handleMint = async () => {
  if (!imageDetail.value) return;
  
  // 1. 从 DOM 元素直接获取 src 
  let realUrl = previewImgRef.value?.src;
  
  // 2. 如果 DOM 没取到，再退回到数据对象获取
  if (!realUrl) {
      realUrl = imageDetail.value.thumbnailMinioUrl;
  }
  if (!realUrl) {
      ElMessage.error('无法获取图片链接，请等待图片加载完成或刷新重试');
      return;
  }
  
  if (!mintForm.name.trim()) {
    ElMessage.warning('请输入资产名称');
    return;
  }
  if (mintForm.price < 0) {
    ElMessage.warning('价格不能小于 0');
    return;
  }

  minting.value = true;
  try {
    const res = await mintNFT({
      thumbnailMinioUrl: realUrl, // 使用从 DOM 抓取的真实 URL
      name: mintForm.name,
      description: mintForm.description,
      price: mintForm.price
    });

    if (res.code === 200) {
      ElMessage.success('铸造请求提交成功！请等待区块链确认。');
      mintDialogVisible.value = false;
    } else {
      ElMessage.error(res.msg || '铸造失败');
    }
  } catch (err: any) {
    console.error('Mint error:', err);
    ElMessage.error('铸造请求异常');
  } finally {
    minting.value = false;
  }
};

// === 4. 辅助函数 (新增 GPS 链接) ===
const formatBytes = (bytes: number | undefined, decimals = 2): string => {
  if (bytes === undefined || bytes === null || bytes === 0) return '0 Bytes';
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
};

const formatTimestamp = (timestamp: string | undefined): string => {
  if (!timestamp) return '未知时间';
  try {
    // 尝试解析 ISO 格式的字符串 (LocalDateTime)
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) return '无效时间';
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  } catch (error) {
    return '格式错误';
  }
};

// === 5. EXIF 格式化辅助函数 (新增) ===

/**
 * 通用 EXIF 分数格式化函数
 * 接收 "Numerator/Denominator" 字符串，返回精确的小数值。
 */
const formatExifValue = (value: string | null): number | null => {
    if (!value || typeof value !== 'string') return null;

    // 检查是否包含分数形式
    if (value.includes('/')) {
        const parts = value.split('/').map(p => parseFloat(p.trim()));
        const numerator = parts[0];
        const denominator = parts[1];
        
        // 确保分子和分母都是有效的数字且分母不为零
        if (isFinite(numerator) && isFinite(denominator) && denominator !== 0) {
            return numerator / denominator;
        }
    }
    
    // 如果不是分数形式，尝试直接解析为浮点数
    const num = parseFloat(value.trim());
    return isFinite(num) ? num : null;
};

/**
 * 格式化焦距 (Focal Length)
 * @param value "560/10"
 * @returns "56mm" 或 "N/A"
 */
const formatFocalLength = (value: string | null): string => {
    const num = formatExifValue(value);
    if (num === null) return 'N/A';
    
    // 焦距通常保留整数或一位小数
    return `${num.toFixed(0)}mm`; 
};

/**
 * 格式化光圈 (Aperture)
 * @param value "f/18/10" 或 "1.8" (虽然您的数据是 "18/10"，但需处理前缀)
 * @returns "f/1.8" 或 "N/A"
 */
const formatAperture = (value: string | null): string => {
    let rawValue = value;
    // 移除可能存在的 "f/" 前缀
    if (rawValue && rawValue.toLowerCase().startsWith('f/')) {
        rawValue = rawValue.substring(2);
    }
    
    const num = formatExifValue(rawValue);
    if (num === null) return 'N/A';
    
    // 光圈值通常保留一位小数
    return `f/${num.toFixed(1)}`;
};

/**
 * 格式化快门速度 (Shutter Speed)
 * @param value "9321928/1000000"
 * @returns "1/640s" 或 "N/A"
 */
const formatShutterSpeed = (value: string | null): string => {
    const num = formatExifValue(value);
    if (num === null) return 'N/A';
    
    // 快门速度：
    // 如果值小于 1 秒，表示为分数形式 1/N
    if (num < 1) {
        // 计算分母的倒数，并四舍五入到最近的整百/整十以便美观，例如 0.0015625 -> 1/640
        // 这里采用简单的倒数取整
        const denominator = Math.round(1 / num);
        // 如果倒数结果为 1，则表示 1/1s
        if (denominator === 1) return `1s`;
        
        return `1/${denominator}s`;
    } 
    // 如果值大于等于 1 秒
    else {
        // 保留一位小数并加上 's'
        return `${num.toFixed(1)}s`;
    }
};

const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    ElMessage.success('已复制到剪贴板');
  } catch (err) {
    console.error('复制失败:', err);
    ElMessage.error('复制失败，请手动选择复制');
  }
};

const getLinkByLabel = (label: string) => {
  if (!imageDetail.value) return '';
  const url = imageDetail.value.watermarkMinioUrl || '';
  const fileName = imageDetail.value.fileName || 'image';
  switch (label) {
    case '直链': return url;
    case 'Markdown': return `![${fileName}](${url})`;
    case 'HTML': return `<img src="${url}" alt="${fileName}">`;
    case 'BBCode': return `[img]${url}[/img]`;
    case 'CSS 背景图': return `background-image: url('${url}');`;
    default: return url;
  }
};

/**
 * 生成 Google Maps 链接，方便用户跳转查看 GPS 位置
 */
const getMapLink = (lat: number, lon: number): string => {
    // 使用 Google Maps 链接作为通用链接
    return `https://www.google.com/maps/search/?api=1&query=${lat},${lon}`;
};

const fetchImageDetail = async (imageId: string) => {
  loading.value = true;
  error.value = null;
  imageDetail.value = null;

  const storedImage = userStore.findImageById(imageId);
  if (storedImage) {
    // 需要确保storedImage包含所有扩展字段
    imageDetail.value = storedImage as Image; 
    loading.value = false;
  } else {
    const apiUrl = `${API_BASE_URL}/api/images/${imageId}`;
    try {
      const responseData = await service.get(apiUrl);
      if (responseData.code === 200 && responseData.data) {
        // 确保从后端获取的数据类型正确
        imageDetail.value = responseData.data as Image;
      } else {
        error.value = new Error(responseData.msg || '获取图片详情失败');
        ElMessage.error(error.value.message);
      }
    } catch (err: any) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  }
};

watch(() => route.params.imageId, (newImageId) => {
  if (typeof newImageId === 'string' && newImageId) {
    fetchImageDetail(newImageId);
  } else {
    imageDetail.value = null;
    error.value = new Error('缺少图片 ID');
    loading.value = false;
    ElMessage.error(error.value.message);
  }
}, { immediate: true });

// // NFT Minting logic (保持不变)
// const mintDialogVisible = ref(false);
// const minting = ref(false);
// const mintFormRef = ref();
// const mintForm = ref({ description: '', price: 0 });

// const showMintDialog = () => {
//   mintForm.value = {
//     description: imageDetail.value?.description || '',
//     price: 0
//   };
//   mintDialogVisible.value = true;
// };

// **新增：获取签名 URL 的核心方法**
const fetchPresignedUrl = async (imageId: string): Promise<string> => {
    // 假设您的后端接口是 /api/images/minio/getPresignedUrl
    const apiUrl = `${API_BASE_URL}/api/images/minio/getPresignedUrl/${imageId}`;
    try {
        const responseData = await service.get(apiUrl);
        if (responseData.code === 200 && responseData.data?.presignedUrl) {
            return responseData.data.presignedUrl;
        } else {
            throw new Error(responseData.msg || '无法获取签名链接');
        }
    } catch (err) {
        console.error('获取签名 URL 失败:', err);
        ElMessage.error('获取原图链接失败');
        throw err;
    }
};

// **新增：显示原图模态框**
const showOriginal = async () => {
    if (!imageDetail.value) return;

    // 由图片id查询key
    const imageId = imageDetail.value.imageId;
    if (!imageId) {
        ElMessage.warning('原图 id 获取失败');
        return;
    }

    modalVisible.value = true;
    originalImageLoading.value = true;
    presignedUrl.value = '';

    try {
        const url = await fetchPresignedUrl(imageId);
        presignedUrl.value = url;
    } catch (e) {
        presignedUrl.value = '';
    } finally {
        originalImageLoading.value = false;
    }
};

// **新增：关闭模态框**
const closeModal = () => {
    modalVisible.value = false;
    presignedUrl.value = ''; // 释放链接
};

</script>

<style scoped>

</style>