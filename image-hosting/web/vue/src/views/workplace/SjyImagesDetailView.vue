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
            <img ref="previewImgRef" :src="imageDetail.thumbnailMinioUrl" :alt="imageDetail.fileName" class="w-full h-full object-contain dark:bg-white/5" />
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

      <!-- 扩展元数据区域 -->
      <div v-if="imageDetail" class="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="md:col-span-2 bg-white rounded-2xl shadow-xl border border-slate-100 p-6 dark:bg-slate-800 dark:border-slate-700">
          <h3 class="text-lg font-bold text-slate-800 dark:text-white mb-4">扩展元数据</h3>
          
          <div class="flex border-b border-slate-200 dark:border-slate-700 mb-4 overflow-x-auto">
            <button v-for="tab in ['EXIF/相机', 'GPS/位置', '统计/分析', '安全/哈希']" 
                    :key="tab"
                    @click="activeTab = tab"
                    class="py-2 px-4 text-sm font-medium transition-colors whitespace-nowrap"
                    :class="activeTab === tab 
                        ? 'text-indigo-600 border-b-2 border-indigo-600 dark:text-indigo-400 dark:border-indigo-400'
                        : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'">
              {{ tab }}
            </button>
          </div>

          <div class="space-y-4 text-sm text-slate-600 dark:text-slate-300">
            <!-- EXIF Tab -->
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

            <!-- GPS Tab -->
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

            <!-- 统计 Tab -->
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
            
            <!-- 安全/哈希 Tab -->
            <div v-else-if="activeTab === '安全/哈希'" class="space-y-4">
                <div class="flex flex-col">
                    <span class="font-medium text-slate-700 dark:text-slate-200 mb-1">文件指纹 (SHA-256):</span>
                    <div class="flex items-center gap-2 bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-700 p-2 rounded-lg group relative">
                        <code class="text-xs font-mono text-slate-600 dark:text-slate-300 break-all select-all">
                            {{ imageDetail.fileHash || '未计算' }}
                        </code>
                        <button v-if="imageDetail.fileHash" 
                                @click="copyToClipboard(imageDetail.fileHash)" 
                                class="p-1 rounded bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-600 hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-500" 
                                title="复制哈希值">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                            </svg>
                        </button>
                    </div>
                </div>
                <div class="text-xs text-slate-400 dark:text-slate-400 leading-relaxed">
                    <p>此哈希值是文件的数字指纹，唯一对应原始图片文件内容。</p>
                    <p class="mt-1">当您将此图片铸造为 NFT 时，该值将被写入区块链，作为不可篡改的存证凭据，用于验证版权和文件完整性。</p>
                    <p class="mt-1">请妥善保管此哈希值，以便未来进行版权声明或纠纷解决时使用。</p>
                </div>
            </div>

          </div>
        </div>

        <div class="md:col-span-1 bg-white rounded-2xl shadow-xl border border-slate-100 p-6 dark:bg-slate-800 dark:border-slate-700 relative overflow-hidden">
          
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-bold text-slate-800 dark:text-white">使用链接</h3>
            <!-- 手动刷新按钮 -->
            <button @click="refreshImageData" :disabled="loading" 
                    class="p-1.5 rounded-full hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-400 hover:text-indigo-500 transition-colors"
                    title="刷新链接状态">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" :class="{'animate-spin': loading}">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
            </button>
          </div>

          <!-- 链接内容区域 (有水印则正常显示，无水印则模糊) -->
          <div :class="{'blur-sm pointer-events-none select-none opacity-60': !imageDetail.watermarkMinioUrl, 'space-y-4': true}">
            <div v-for="(label, idx) in ['直链','Markdown','HTML','BBCode','CSS 背景图']" :key="idx">
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">{{ label }}:</label>
              <div class="flex items-center gap-2">
                <input type="text" :value="getLinkByLabel(label)" readonly class="flex-grow rounded-lg px-3 py-2 text-sm bg-slate-100 dark:bg-slate-900 text-slate-700 dark:text-slate-200 truncate border border-slate-200 dark:border-slate-700" />
                <button @click="copyToClipboard(getLinkByLabel(label))" class="px-3 py-2 rounded-lg bg-indigo-500 text-white hover:bg-indigo-600 text-sm duration-200">复制</button>
              </div>
            </div>
            <!-- <button @click="showSamplePreview" 
                     class="px-4 py-2 bg-white dark:bg-slate-700 hover:bg-slate-50 dark:hover:bg-slate-600 text-indigo-600 dark:text-indigo-400 text-xs font-bold rounded-full shadow-md border border-slate-100 dark:border-slate-600 transition-all flex items-center gap-1.5 duration-200">
                 <svg xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                   <circle cx="11" cy="11" r="8"></circle>
                   <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                 </svg>
                 查看预览
             </button> -->
          </div>

          <!-- 遮罩层 (无水印时显示) -->
          <div v-if="!imageDetail.watermarkMinioUrl" class="absolute inset-0 z-10 flex flex-col items-center justify-center p-6 text-center dark:bg-slate-800/50 bg-white/70 backdrop-blur-sm">
             <div class="w-16 h-16 rounded-full bg-slate-100 dark:bg-slate-700/80 backdrop-blur-sm flex items-center justify-center mb-4 shadow-lg border border-slate-200 dark:border-slate-600">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-slate-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                   <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                   <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                </svg>
             </div>
             <h4 class="text-base font-bold text-slate-800 dark:text-white mb-2">铸造 NFT 后使用水印图</h4>
             <p class="text-xs text-slate-500 dark:text-slate-300 mb-5 max-w-[200px]">只有完成数字资产确权后，才能获取并使用带有版权保护水印的链接。</p>
             <button @click="showSamplePreview" 
                     class="px-4 py-2 bg-white dark:bg-slate-700 hover:bg-slate-50 dark:hover:bg-slate-600 text-indigo-600 dark:text-indigo-400 text-xs font-bold rounded-full shadow-md border border-slate-100 dark:border-slate-600 transition-all flex items-center gap-1.5 duration-200">
                 <svg xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                   <circle cx="11" cy="11" r="8"></circle>
                   <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                 </svg>
                 查看预览
             </button>
          </div>

        </div>
      </div>
      
      <div v-if="imageDetail" class="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="md:col-span-2 bg-white rounded-2xl shadow-xl border border-slate-100 p-6 dark:bg-slate-800 dark:border-slate-700">
                <div class="text-xs text-slate-400 dark:text-slate-400 leading-relaxed">
                    <p>注意：上方为图片缩略图，非源文件。</p>
                    <p class="mt-1">当您点击查看原图时会获取预签名URL，用于安全访问原始图片文件。预签名URL具有时效性，请勿长期保存或公开分享。</p>
                    <p class="mt-1">如需设置为公开或使用图片，请完成图片上链操作，使用含版权信息的水印图。</p>
                </div>
        </div>
      </div>

    </div>

    <!-- 模态框 1: 原图/样例预览 -->
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
          <div class="absolute inset-0 bg-slate-900/95 backdrop-blur-md transition-opacity" @click="closeModal"></div>
          <button class="absolute top-4 right-4 sm:top-6 sm:right-6 z-[1010] p-2.5 rounded-full bg-white/10 hover:bg-white/20 text-slate-300 hover:text-white border border-white/5 backdrop-blur-sm transition-all duration-200 group" 
                  @click="closeModal"
                  title="关闭预览">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 transition-transform duration-300 group-hover:rotate-90" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
          <div class="relative w-full h-full flex flex-col items-center justify-center pointer-events-none z-10">
            
            <!-- 加载状态 -->
            <div v-if="originalImageLoading" class="absolute inset-0 flex items-center justify-center z-20">
              <div class="flex flex-col items-center gap-3 p-6 rounded-2xl bg-slate-800/50 backdrop-blur-md border border-white/10 shadow-2xl">
                <svg class="animate-spin h-10 w-10 text-indigo-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                <span class="text-sm font-medium text-slate-300 tracking-wide">正在加载...</span>
              </div>
            </div>
            
            <!-- 图片本体 (可能是原图 URL 或 样例图片) -->
            <img 
              v-if="presignedUrl"
              :src="presignedUrl" 
              :alt="isPreviewSample ? 'Watermark Sample' : (imageDetail?.fileName || 'Image')" 
              class="pointer-events-auto max-w-full max-h-[85vh] object-contain rounded-lg shadow-2xl ring-1 ring-white/10 transition-all duration-500 select-none"
              :class="originalImageLoading ? 'opacity-0 scale-95' : 'opacity-100 scale-100'"
              @load="originalImageLoading = false"
            />
            
            <!-- 底部悬浮栏 -->
            <div v-if="!originalImageLoading && presignedUrl && !isPreviewSample && imageDetail" 
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
            
            <!-- 样例预览时的底部提示 -->
             <div v-if="!originalImageLoading && isPreviewSample" 
                 class="pointer-events-auto mt-6 px-6 py-3 bg-slate-900/80 backdrop-blur-xl rounded-full border border-white/10 shadow-2xl">
              <span class="text-sm font-medium text-slate-300">
                效果预览：铸造 NFT 后将自动生成此类带版权信息的水印图
              </span>
            </div>

          </div>
        </div>
      </Transition>
    </Teleport>

    <!-- 模态框 2: 铸造 NFT -->
    <Teleport to="body">
      <Transition enter-active-class="transition ease-out duration-300" enter-from-class="opacity-0" enter-to-class="opacity-100" leave-active-class="transition ease-in duration-200" leave-from-class="opacity-100" leave-to-class="opacity-0">
        <div v-if="mintDialogVisible" class="fixed inset-0 z-[1000] flex items-center justify-center p-4" @click.self="mintDialogVisible = false">
          <div class="absolute inset-0 bg-slate-900/80 backdrop-blur-sm transition-opacity" @click="mintDialogVisible = false"></div>
          <div class="relative w-full max-w-md bg-white dark:bg-slate-800 rounded-2xl shadow-2xl overflow-hidden border border-slate-100 dark:border-slate-700 transform transition-all z-10">
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

    <!-- 模态框 3: 激活账户弹窗 -->
    <Teleport to="body">
      <Transition enter-active-class="transition ease-out duration-300" enter-from-class="opacity-0" enter-to-class="opacity-100" leave-active-class="transition ease-in duration-200" leave-from-class="opacity-100" leave-to-class="opacity-0">
        <div v-if="activationDialogVisible" class="fixed inset-0 z-[1000] flex items-center justify-center p-4" @click.self="activationDialogVisible = false">
            <div class="absolute inset-0 bg-slate-900/80 backdrop-blur-sm transition-opacity" @click="activationDialogVisible = false"></div>
            <div class="relative w-full max-w-sm bg-white dark:bg-slate-800 rounded-2xl shadow-2xl overflow-hidden border border-slate-100 dark:border-slate-700 transform transition-all z-10">
                <div class="p-6 text-center">
                    <div class="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-indigo-100 dark:bg-indigo-900/30 mb-4">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-600 dark:text-indigo-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-bold text-slate-800 dark:text-white mb-2">需要激活账户</h3>
                    <p class="text-sm text-slate-500 dark:text-slate-400 leading-relaxed mb-6">
                        您尚未激活区块链账户，无法进行 NFT 铸造。激活后您将获得专属的区块链钱包地址。
                    </p>
                    <div class="flex gap-3">
                        <button @click="activationDialogVisible = false" class="flex-1 px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700 font-medium transition-colors">
                            稍后再说
                        </button>
                        <button @click="goToActivation" class="flex-1 px-4 py-2.5 rounded-xl bg-indigo-600 text-white hover:bg-indigo-700 font-medium shadow-md shadow-indigo-200 dark:shadow-none transition-all">
                            立即激活
                        </button>
                    </div>
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
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/user';
import { ArrowDownTrayIcon, SparklesIcon, TrashIcon, PencilSquareIcon, CheckIcon, LinkIcon } from '@heroicons/vue/24/outline';
import { checkRegistrationStatus, mintNFT } from '@/api/nft';
import samplePreviewImage from '@/assets/sample_nft.jpg'; // 样例图片路径

// === 1. 扩展接口定义 ===
interface Image {
  imageId: string;
  thumbnailMinioUrl: string;
  watermarkMinioUrl: string | null; // 允许为null
  originMinioUrl: string;
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
  aperture: string | null;
  shutterSpeed: string | null;
  iso: number | null;
  shootTime: string | null;
  locationName: string | null;
  latitude: number | null;
  longitude: number | null;
  viewCount: number | null;
  downloadCount: number | null;
  likeCount: number | null;
  category: string | null;
  dominantColor: string | null;
}

// === 2. 状态定义 ===
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const imageDetail = ref<Image | null>(null);
const loading = ref(true);
const error = ref<Error | null>(null);
const isDeleting = ref(false);
const modalVisible = ref(false);
const presignedUrl = ref('');
const originalImageLoading = ref(false);
const isPreviewSample = ref(false); // 新增：是否为样例预览

const isEditing = ref(false);
const isUpdating = ref(false); 
const editForm = reactive({ fileName: '', description: '', isPublic: false });
const activeTab = ref('EXIF/相机');

// Mint 相关状态
const mintDialogVisible = ref(false);
const minting = ref(false);
const mintForm = reactive({ name: '', description: '', price: 0 });

const activationDialogVisible = ref(false);
const previewImgRef = ref<HTMLImageElement | null>(null);

// === 3. 业务逻辑 ===

// ... (toggleEditMode, handleUpdateImage, downloadImage, deleteImage 保持不变)
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
    // 逻辑保持不变...
    // 省略具体代码以节省篇幅，请保持原有的 deleteImage 逻辑
      if (!image || !image.imageId) {
    ElMessage.warning('图片信息不完整，无法删除。');
    return;
  }
  try {
     // ...
    isDeleting.value = true;
    const deleteUrl = `${API_BASE_URL}/api/images/deleteById/${image.imageId}`;
    const responseData = await service.post(deleteUrl);
    if (responseData.code === 200) {
      ElMessage.success(responseData.msg || '删除成功');
      router.push({ name: 'MyImages' });
    } else {
       ElMessage.error(responseData.msg || '删除失败');
    }
  } catch (error: any) {
  } finally {
    isDeleting.value = false;
  }
};


const handleShowMintDialog = async () => {
  if (!imageDetail.value) return;
  try {
    const res = await checkRegistrationStatus();
    const isRegistered = res.data && res.data.isRegistered;
    if (isRegistered) {
      mintForm.name = imageDetail.value.fileName || '';
      mintForm.description = imageDetail.value.description || '';
      mintForm.price = 0;
      mintDialogVisible.value = true;
    } else {
      activationDialogVisible.value = true;
    }
  } catch (err) {
    console.error('检查区块链账户状态失败:', err);
    ElMessage.error('无法连接区块链服务，请稍后重试');
  }
};

const goToActivation = () => {
    activationDialogVisible.value = false;
    router.push({ name: 'MyNFT' });
};

// === 执行铸造逻辑 (添加自动刷新) ===
const handleMint = async () => {
  if (!imageDetail.value) return;
  let realUrl = previewImgRef.value?.src;
  if (!realUrl) realUrl = imageDetail.value.thumbnailMinioUrl;
  if (!realUrl) {
      ElMessage.error('无法获取图片链接');
      return;
  }
  if (!mintForm.name.trim()) {
    ElMessage.warning('请输入资产名称');
    return;
  }

  minting.value = true;
  try {
    const res = await mintNFT({
      imageId: imageDetail.value.imageId,
      thumbnailMinioUrl: realUrl,
      name: mintForm.name,
      fileHash: imageDetail.value.fileHash,
      description: mintForm.description,
      price: mintForm.price
    });

    if (res.code === 200) {
      ElMessage.success('铸造请求提交成功！正在等待水印生成...');
      mintDialogVisible.value = false;
      
      // === 新增：2秒后自动刷新数据 ===
      setTimeout(() => {
          refreshImageData();
      }, 2000);

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

// === 新增：手动刷新数据方法 ===
const refreshImageData = async () => {
    if (!imageDetail.value) return;
    loading.value = true; // 可选：让用户感知到正在刷新，或者只用按钮的 spin 动画
    try {
        const apiUrl = `${API_BASE_URL}/api/images/${imageDetail.value.imageId}`;
        const responseData = await service.get(apiUrl);
        if (responseData.code === 200 && responseData.data) {
            imageDetail.value = responseData.data as Image;
            ElMessage.success('数据已更新');
        }
    } catch (e) {
        console.error("刷新失败", e);
    } finally {
        loading.value = false;
    }
}

// ... (formatBytes, formatTimestamp, EXIF 格式化函数等保持不变)
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
const formatExifValue = (value: string | null): number | null => {
    if (!value || typeof value !== 'string') return null;
    if (value.includes('/')) {
        const parts = value.split('/').map(p => parseFloat(p.trim()));
        const numerator = parts[0];
        const denominator = parts[1];
        if (isFinite(numerator) && isFinite(denominator) && denominator !== 0) {
            return numerator / denominator;
        }
    }
    const num = parseFloat(value.trim());
    return isFinite(num) ? num : null;
};
const formatFocalLength = (value: string | null): string => {
    const num = formatExifValue(value);
    if (num === null) return 'N/A';
    return `${num.toFixed(0)}mm`; 
};
const formatAperture = (value: string | null): string => {
    let rawValue = value;
    if (rawValue && rawValue.toLowerCase().startsWith('f/')) {
        rawValue = rawValue.substring(2);
    }
    const num = formatExifValue(rawValue);
    if (num === null) return 'N/A';
    return `f/${num.toFixed(1)}`;
};
const formatShutterSpeed = (value: string | null): string => {
    const num = formatExifValue(value);
    if (num === null) return 'N/A';
    if (num < 1) {
        const denominator = Math.round(1 / num);
        if (denominator === 1) return `1s`;
        return `1/${denominator}s`;
    } 
    else {
        return `${num.toFixed(1)}s`;
    }
};

const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    ElMessage.success('已复制到剪贴板');
  } catch (err) {
    ElMessage.error('复制失败，请手动选择复制');
  }
};

// 修改：getLinkByLabel 使用 watermarkMinioUrl
const getLinkByLabel = (label: string) => {
  if (!imageDetail.value) return '';
  // 如果没有水印URL，也返回空或者提示，但通常界面已被遮罩
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

const getMapLink = (lat: number, lon: number): string => {
    return `https://www.google.com/maps/search/?api=1&query=${lat},${lon}`;
};

const fetchImageDetail = async (imageId: string) => {
  loading.value = true;
  error.value = null;
  imageDetail.value = null;
  // 简化逻辑：每次都重新请求，确保水印状态最新，或者保留 store 缓存逻辑并在 mint 后更新 store
  const apiUrl = `${API_BASE_URL}/api/images/${imageId}`;
  try {
    const responseData = await service.get(apiUrl);
    if (responseData.code === 200 && responseData.data) {
      imageDetail.value = responseData.data as Image;
    } else {
      error.value = new Error(responseData.msg || '获取图片详情失败');
    }
  } catch (err: any) {
    error.value = err;
  } finally {
    loading.value = false;
  }
};

watch(() => route.params.imageId, (newImageId) => {
  if (typeof newImageId === 'string' && newImageId) {
    fetchImageDetail(newImageId);
  } else {
    imageDetail.value = null;
    error.value = new Error('缺少图片 ID');
    loading.value = false;
  }
}, { immediate: true });

const fetchPresignedUrl = async (imageId: string): Promise<string> => {
    const apiUrl = `${API_BASE_URL}/api/images/minio/getPresignedUrl/${imageId}`;
    try {
        const responseData = await service.get(apiUrl);
        if (responseData.code === 200 && responseData.data?.presignedUrl) {
            return responseData.data.presignedUrl;
        } else {
            throw new Error(responseData.msg || '无法获取签名链接');
        }
    } catch (err) {
        ElMessage.error('获取原图链接失败');
        throw err;
    }
};

const showOriginal = async () => {
    if (!imageDetail.value) return;
    const imageId = imageDetail.value.imageId;
    if (!imageId) return;
    
    modalVisible.value = true;
    originalImageLoading.value = true;
    presignedUrl.value = '';
    isPreviewSample.value = false; // 标记为真实原图

    try {
        const url = await fetchPresignedUrl(imageId);
        presignedUrl.value = url;
    } catch (e) {
        presignedUrl.value = '';
    } finally {
        // 图片加载完成事件会在 img @load 中处理 loading 状态，这里只处理请求结束
    }
};

// === 新增：显示样例预览 ===
const showSamplePreview = () => {
    modalVisible.value = true;
    originalImageLoading.value = false; // 本地图片无需 loading 状态
    presignedUrl.value = samplePreviewImage; // 使用导入的样例图片
    isPreviewSample.value = true;
}

const closeModal = () => {
    modalVisible.value = false;
    presignedUrl.value = ''; 
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