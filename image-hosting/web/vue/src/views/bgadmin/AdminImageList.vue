<template>
  <div class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
    <!-- Header & Statistics -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-2">
      <div>
        <h2 class="text-2xl font-bold text-slate-800 dark:text-white tracking-tight">图片库管理</h2>
        <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">监控和维护系统内所有已上传的图片资源</p>
      </div>
      <div class="flex items-center gap-3">
        <div class="px-5 py-2.5 bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm flex items-center gap-3">
          <div class="p-2 bg-indigo-50 dark:bg-indigo-900/30 rounded-lg text-indigo-600 dark:text-indigo-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
          </div>
          <div>
            <span class="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">图片总量</span>
            <span class="text-lg font-black text-slate-800 dark:text-white leading-none">{{ total }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Filters Section -->
    <div class="bg-white/80 dark:bg-slate-800/80 backdrop-blur-md p-4 rounded-3xl shadow-xl border border-slate-100 dark:border-slate-700 flex flex-col lg:flex-row gap-4 lg:items-center justify-between transition-all duration-300">
      <div class="flex flex-wrap gap-2 p-1 bg-slate-100 dark:bg-slate-900 rounded-2xl w-fit">
        <button 
          v-for="f in [{label: '全部图片', value: ''}, {label: '公开展示', value: 'public'}, {label: '私有存储', value: 'private'}]"
          :key="f.value"
          @click="handleFilter(f.value)"
          :class="query.type === f.value 
            ? 'bg-white dark:bg-slate-700 text-indigo-600 dark:text-indigo-400 shadow-md ring-1 ring-slate-200/50 dark:ring-slate-600/50' 
            : 'text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200'"
          class="px-6 py-2 rounded-xl text-sm font-bold transition-all duration-200"
        >
          {{ f.label }}
        </button>
      </div>

      <div class="relative group">
        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none">
          <svg class="h-4 w-4 text-slate-400 group-focus-within:text-indigo-500 transition-colors" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>
        <input 
          v-model="query.keyword"
          @keyup.enter="handleSearch"
          type="text" 
          placeholder="搜索图片ID或名称..." 
          class="w-full lg:w-80 pl-11 pr-20 py-2.5 rounded-2xl border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-900 focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-sm dark:text-white"
        />
        <button 
          @click="handleSearch"
          class="absolute right-2 top-1.5 px-3 py-1 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold rounded-lg transition-colors shadow-sm"
        >
          搜索
        </button>
      </div>
    </div>

    <!-- Image Grid & Empty State -->
    <div v-if="images.length > 0" class="grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-8">
      <div v-for="img in images" :key="img.imageId" class="group relative bg-white/70 dark:bg-slate-800/70 backdrop-blur-sm rounded-[2rem] shadow-lg border border-white/50 dark:border-slate-700/50 overflow-hidden hover:shadow-2xl hover:shadow-indigo-500/10 transition-all duration-500 hover:-translate-y-2">
        <!-- Image Thumbnail -->
        <div class="aspect-[4/3] overflow-hidden bg-slate-100 dark:bg-slate-900 relative">
          <img 
            :src="img.thumbnailMinioUrl || img.originMinioUrl" 
            :alt="img.fileName" 
            class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" 
            loading="lazy" 
          />
          
          <!-- Status Badge -->
          <div class="absolute top-4 left-4 flex gap-2">
            <span v-if="img.isPublic" class="px-3 py-1 bg-emerald-500/90 backdrop-blur-md text-[10px] font-bold text-white rounded-full shadow-lg transition-transform duration-300 group-hover:scale-105">
              公开
            </span>
            <span v-else class="px-3 py-1 bg-amber-500/90 backdrop-blur-md text-[10px] font-bold text-white rounded-full shadow-lg transition-transform duration-300 group-hover:scale-105">
              私有
            </span>
          </div>

          <!-- Overlay Actions -->
          <div class="absolute inset-0 bg-slate-900/40 opacity-0 group-hover:opacity-100 transition-all duration-300 flex items-center justify-center gap-4">
            <button @click="showDetail(img)" class="p-3 bg-white/20 backdrop-blur-xl rounded-2xl text-white hover:bg-white/40 hover:scale-110 active:scale-95 transition-all shadow-xl" title="查看详细信息">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </button>
            <a :href="img.thumbnailMinioUrl || img.originMinioUrl" target="_blank" class="p-3 bg-white/20 backdrop-blur-xl rounded-2xl text-white hover:bg-white/40 hover:scale-110 active:scale-95 transition-all shadow-xl" title="查看缩略图">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
            </a>
            <button @click="handleDelete(img)" class="p-3 bg-red-500/80 backdrop-blur-xl rounded-2xl text-white hover:bg-red-600 hover:scale-110 active:scale-95 transition-all shadow-xl" title="删除">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Info Card -->
        <div class="p-5">
          <div class="flex items-center justify-between gap-3 mb-2">
            <h4 class="text-sm font-bold text-slate-800 dark:text-white truncate flex-1" :title="img.fileName">
              {{ img.fileName }}
            </h4>
            <span class="text-[10px] font-black px-2 py-0.5 rounded-lg bg-slate-100 dark:bg-slate-700 text-slate-500 dark:text-slate-400 uppercase tracking-tighter">
              {{ img.contentType ? img.contentType.split('/')[1] : 'UNK' }}
            </span>
          </div>
          
          <div class="flex items-center gap-2 text-[11px] text-slate-500 dark:text-slate-400 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3.5 w-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            {{ new Date(img.createTime).toLocaleDateString() }}
            <span class="mx-1">•</span>
            {{ formatSize(img.size) }}
          </div>
          
          <div class="pt-4 border-t border-slate-100 dark:border-slate-700/50 flex items-center justify-between">
            <div class="flex items-center gap-2 overflow-hidden">
              <div class="w-7 h-7 flex-shrink-0 rounded-xl bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-500 p-[1.5px]">
                <div class="w-full h-full rounded-[10px] bg-white dark:bg-slate-800 flex items-center justify-center">
                  <span class="text-[10px] font-bold bg-gradient-to-br from-indigo-500 to-purple-600 bg-clip-text text-transparent">
                    {{ img.userId ? img.userId.charAt(0).toUpperCase() : 'U' }}
                  </span>
                </div>
              </div>
              <span class="text-xs font-medium text-slate-600 dark:text-slate-300 truncate" :title="'ID: ' + img.userId">
                {{ img.userId }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="flex flex-col items-center justify-center py-24 px-4 bg-white/50 dark:bg-slate-800/50 backdrop-blur-md rounded-[3rem] border border-slate-100 dark:border-slate-700 shadow-xl">
      <div class="relative mb-8">
        <div class="absolute inset-0 bg-indigo-500 blur-3xl opacity-20"></div>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-32 w-32 text-slate-200 dark:text-slate-700 relative" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.581-1.581a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
        </svg>
      </div>
      <h3 class="text-2xl font-black text-slate-900 dark:text-white mb-2">暂无图片数据</h3>
      <p class="text-slate-500 dark:text-slate-400 text-center max-w-sm">
        当前的筛选条件下没有找到相关图片，请尝试更改关键词或切换分类。
      </p>
    </div>

    <!-- Image Detail Dialog -->
    <el-dialog
      v-model="detailVisible"
      title="图片资源全域视图"
      width="900px"
      append-to-body
      class="detail-dialog rounded-[2.5rem] overflow-hidden"
    >
      <div v-if="selectedImage" class="flex flex-col lg:flex-row gap-8 p-1">
        <!-- Asset Preview Side -->
        <div class="lg:w-1/3 space-y-4">
          <div class="aspect-square rounded-[2rem] overflow-hidden border border-slate-100 dark:border-slate-700 shadow-inner bg-slate-50 dark:bg-slate-900 flex items-center justify-center group/preview relative">
            <img :src="selectedImage.thumbnailMinioUrl || selectedImage.originMinioUrl" class="max-w-full max-h-full object-contain transition-transform duration-500 group-hover/preview:scale-105" aria-label="资源预览" />
            <div class="absolute bottom-4 right-4 px-3 py-1 bg-black/50 backdrop-blur-md text-white text-[10px] font-bold rounded-lg opacity-0 group-hover/preview:opacity-100 transition-opacity">
              缩略图预览
            </div>
          </div>
          
          <div class="p-5 bg-slate-50 dark:bg-slate-900/50 rounded-[1.5rem] border border-slate-100 dark:border-slate-800">
            <h4 class="text-xs font-black text-slate-400 uppercase tracking-widest mb-4">核心资产状态</h4>
            <div class="space-y-3">
              <div class="flex items-center justify-between">
                <span class="text-xs text-slate-500">数字资产化</span>
                <span class="px-2 py-0.5 rounded-lg text-[10px] font-bold" 
                      :class="selectedImage.nftId ? 'bg-indigo-100 text-indigo-600' : 'bg-slate-100 text-slate-400'">
                  {{ selectedImage.nftId ? '已上链 (NFT)' : '未上链' }}
                </span>
              </div>
              <div class="flex items-center justify-between">
                <span class="text-xs text-slate-500">资源分类</span>
                <span class="text-xs font-bold text-slate-700 dark:text-slate-200">{{ selectedImage.category || '未分类' }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Meta Data Tabs Side -->
        <div class="lg:w-2/3 flex flex-col h-full">
          <div class="mb-4 space-y-1">
            <h3 class="text-2xl font-black text-slate-900 dark:text-white truncate" :title="selectedImage.fileName">
              {{ selectedImage.fileName }}
            </h3>
            <div class="flex items-center gap-2">
              <span class="text-xs font-mono text-slate-400">UID: {{ selectedImage.imageId }}</span>
              <span v-if="selectedImage.isPublic" class="flex-shrink-0 w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
            </div>
          </div>

          <!-- Tabs -->
          <div class="flex gap-1 p-1 bg-slate-100 dark:bg-slate-900 rounded-2xl w-fit mb-6">
            <button 
              v-for="tab in ['基础信息', '相机/EXIF', '拍摄位置', '哈希/数字资产']"
              :key="tab"
              @click="activeTab = tab"
              :class="activeTab === tab ? 'bg-white dark:bg-slate-800 text-indigo-600 dark:text-indigo-400 shadow-sm' : 'text-slate-500 hover:text-slate-700 dark:hover:text-slate-200'"
              class="px-4 py-2 rounded-xl text-xs font-bold transition-all"
            >
              {{ tab }}
            </button>
          </div>

          <!-- Tab Content -->
          <div class="flex-1 overflow-y-auto max-h-[400px] pr-2 custom-scrollbar">
            <!-- Basic Info -->
            <div v-if="activeTab === '基础信息'" class="grid grid-cols-2 gap-4 animate-in fade-in duration-300">
              <div class="p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                <span class="text-[10px] font-bold text-slate-400 uppercase block mb-1">原始大小</span>
                <span class="text-sm font-bold text-slate-800 dark:text-slate-200">{{ formatSize(selectedImage.size) }}</span>
              </div>
              <div class="p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                <span class="text-[10px] font-bold text-slate-400 uppercase block mb-1">MIME 类型</span>
                <span class="text-sm font-bold text-slate-800 dark:text-slate-200 font-mono">{{ selectedImage.contentType }}</span>
              </div>
              <div class="p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                <span class="text-[10px] font-bold text-slate-400 uppercase block mb-1">图片尺度</span>
                <span class="text-sm font-bold text-slate-800 dark:text-slate-200">{{ selectedImage.width || '?' }} x {{ selectedImage.height || '?' }}</span>
              </div>
              <div class="p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                <span class="text-[10px] font-bold text-slate-400 uppercase block mb-1">主色调</span>
                <div class="flex items-center gap-2">
                  <div class="w-4 h-4 rounded shadow-sm border border-black/10" :style="{ backgroundColor: selectedImage.dominantColor || '#eee' }"></div>
                  <span class="text-sm font-bold text-slate-800 dark:text-slate-200 uppercase">{{ selectedImage.dominantColor || 'N/A' }}</span>
                </div>
              </div>
              <div class="col-span-2 p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                <span class="text-[10px] font-bold text-slate-400 uppercase block mb-1">图片描述</span>
                <p class="text-xs text-slate-600 dark:text-slate-300">{{ selectedImage.description || '无描述' }}</p>
              </div>
            </div>

            <!-- EXIF Info -->
            <div v-if="activeTab === '相机/EXIF'" class="grid grid-cols-2 gap-4 animate-in fade-in duration-300">
               <div v-for="(val, key) in {
                 '相机厂商': selectedImage.cameraMake,
                 '相机型号': selectedImage.cameraModel,
                 '镜头型号': selectedImage.lensModel,
                 '拍摄时间': selectedImage.shootTime ? new Date(selectedImage.shootTime).toLocaleString() : null,
                 '焦距': selectedImage.focalLength,
                 '光圈': selectedImage.aperture,
                 '快门周期': selectedImage.shutterSpeed,
                 'ISO 感光度': selectedImage.iso
               }" :key="key" class="p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                 <span class="text-[10px] font-bold text-slate-400 uppercase block mb-1">{{ key }}</span>
                 <span class="text-sm font-bold text-slate-800 dark:text-white">{{ val || 'N/A' }}</span>
               </div>
            </div>

            <!-- Position & Stats -->
            <div v-if="activeTab === '拍摄位置'" class="space-y-4 animate-in fade-in duration-300">
               <div class="p-4 bg-slate-50/50 dark:bg-slate-900/30 rounded-2xl border border-slate-100 dark:border-slate-800/50">
                  <span class="text-[10px] font-bold text-slate-400 uppercase block mb-2">拍摄地点</span>
                  <div class="flex items-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                    </svg>
                    <span class="text-sm font-bold">{{ selectedImage.locationName || '未知地理元数据' }}</span>
                  </div>
                  <div v-if="selectedImage.latitude" class="mt-2 text-[10px] font-mono text-slate-400">
                    GPS: {{ selectedImage.latitude.toFixed(6) }}, {{ selectedImage.longitude?.toFixed(6) }}
                  </div>
               </div>
            </div>

            <!-- Hash & Blockchain -->
            <div v-if="activeTab === '哈希/数字资产'" class="space-y-4 animate-in fade-in duration-300">
               <div class="p-5 bg-indigo-50/30 dark:bg-indigo-900/10 rounded-2xl border border-indigo-100/50 dark:border-indigo-800/20">
                  <span class="text-[10px] font-black text-indigo-400 uppercase tracking-widest block mb-2">文件 SHA-256 哈希</span>
                  <code class="text-[10px] text-indigo-600 dark:text-indigo-300 break-all font-mono leading-relaxed bg-white/50 dark:bg-slate-900/50 p-3 rounded-xl block border border-indigo-100 dark:border-indigo-800/30">
                    {{ selectedImage.fileHash }}
                  </code>
               </div>
               <div v-if="selectedImage.nftId" class="grid grid-cols-2 gap-4">
                  <div class="p-4 bg-purple-50/30 dark:bg-purple-900/10 rounded-2xl border border-purple-100/50 dark:border-purple-800/20">
                    <span class="text-[10px] font-bold text-purple-400 uppercase block mb-1">On-Chain NFT ID</span>
                    <span class="text-xs font-bold text-purple-700 dark:text-purple-300 font-mono">{{ selectedImage.nftId }}</span>
                  </div>
                  <div class="p-4 bg-cyan-50/30 dark:bg-cyan-900/10 rounded-2xl border border-cyan-100/50 dark:border-cyan-800/20">
                    <span class="text-[10px] font-bold text-cyan-400 uppercase block mb-1">Token Identifier</span>
                    <span class="text-xs font-bold text-cyan-700 dark:text-cyan-300 font-mono">{{ selectedImage.tokenId || '-' }}</span>
                  </div>
               </div>
               <div v-else class="p-8 text-center border-2 border-dashed border-slate-100 dark:border-slate-800 rounded-3xl">
                  <span class="text-xs text-slate-400">该资产尚未完成链上映射，暂时无法获取区块链关联信息。</span>
               </div>
            </div>
          </div>
          
          <!-- Bottom Metadata -->
          <div class="mt-auto pt-6 border-t border-slate-100 dark:border-slate-800/50 flex items-center justify-between opacity-60">
            <div class="flex flex-col">
              <span class="text-[9px] font-bold text-slate-400 uppercase">录入节点</span>
              <span class="text-[10px] font-mono font-bold text-slate-500">{{ selectedImage.createTime ? new Date(selectedImage.createTime).toLocaleString() : 'N/A' }}</span>
            </div>
            <div class="flex flex-col items-end">
              <span class="text-[9px] font-bold text-slate-400 uppercase">归属持有人</span>
              <span class="text-[10px] font-bold text-slate-500">{{ selectedImage.userId }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- Pagination -->
    <div class="mt-8 p-6 bg-white/80 dark:bg-slate-800/80 backdrop-blur-md rounded-3xl shadow-xl border border-slate-100 dark:border-slate-700 flex items-center justify-between transition-all duration-300">
      <div class="hidden sm:block">
        <span class="text-sm text-slate-500 dark:text-slate-400">
          显示第 <span class="font-bold text-slate-900 dark:text-white">{{ (query.page - 1) * query.size + 1 }}</span> 
          到 <span class="font-bold text-slate-900 dark:text-white">{{ Math.min(query.page * query.size, total) }}</span> 
          条数据，共 <span class="font-bold text-slate-900 dark:text-white">{{ total }}</span> 条
        </span>
      </div>
      <div class="flex items-center gap-3">
        <button 
          @click="changePage(query.page - 1)"
          :disabled="query.page <= 1"
          class="inline-flex items-center px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-bold text-slate-700 dark:text-slate-200 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-40 disabled:cursor-not-allowed transition-all shadow-sm"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
          上一页
        </button>
        
        <!-- Mobile Page Indicator -->
        <span class="sm:hidden text-xs font-bold text-indigo-600 px-3 py-1 bg-indigo-50 dark:bg-indigo-900/30 rounded-lg">
          {{ query.page }}
        </span>

        <button 
          @click="changePage(query.page + 1)"
          :disabled="query.page * query.size >= total"
          class="inline-flex items-center px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm font-bold text-slate-700 dark:text-slate-200 hover:bg-slate-50 dark:hover:bg-slate-700 disabled:opacity-40 disabled:cursor-not-allowed transition-all shadow-sm"
        >
          下一页
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { getImageList, deleteImage, type ImageVO, type ImageListQuery } from '@/api/admin/image';
import { ElMessage, ElMessageBox } from 'element-plus';

const images = ref<ImageVO[]>([]);
const total = ref(0);
const detailVisible = ref(false);
const selectedImage = ref<ImageVO | null>(null);
const activeTab = ref('基础信息');

const query = reactive<ImageListQuery>({
  page: 1,
  size: 10,
  keyword: '',
  type: ''
});

const loadData = async () => {
  try {
    const res = await getImageList(query);
    if (res.code === 200) {
      images.value = res.data.records;
      total.value = res.data.total;
    }
  } catch (error) {
    console.error('Failed to load images', error);
  }
};

const showDetail = (img: ImageVO) => {
  selectedImage.value = img;
  detailVisible.value = true;
};

const handleSearch = () => {
  query.page = 1;
  loadData();
};

const handleFilter = (type: string) => {
  query.type = type;
  query.page = 1;
  loadData();
};

const changePage = (newPage: number) => {
  query.page = newPage;
  loadData();
};

const handleDelete = (img: ImageVO) => {
  ElMessageBox.confirm(
    '确定要删除这张图片吗？如果该图片已铸造为NFT，链上信息也将被标记为删除。',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
        const res = await deleteImage(img.imageId);
        if (res.code === 200) {
          ElMessage.success('删除成功');
          loadData();
        } else {
          ElMessage.error(res.message || '删除失败');
        }
      } catch (error) {
        ElMessage.error('删除失败');
      }
    })
    .catch(() => {
      // Cancelled
    });
};

const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

onMounted(() => {
  loadData();
});
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #e2e8f0;
  border-radius: 10px;
}
.dark .custom-scrollbar::-webkit-scrollbar-thumb {
  background: #334155;
}
</style>
