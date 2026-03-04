<template>
  <div class="w-full min-h-screen bg-white pt-4 pb-10">
    <div class="container mx-auto px-4 max-w-6xl">
      
      <!-- 主要内容区域 -->
      <div class="grid grid-cols-2 gap-8">
        
        <!-- 左侧：图片区域 -->
        <div>
          <!-- 图片容器 -->
          <div class="relative border-2 border-black bg-gray-300 h-80 flex items-center justify-center mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" class="text-gray-500">
              <rect width="18" height="18" x="3" y="3"/>
              <circle cx="9" cy="9" r="2"/>
              <path d="m21 15-3.086-3.086a2 2 0 0 0-2.828 0L6 21"/>
            </svg>
            <!-- 状态标签 -->
            <div class="absolute top-4 right-4 border-2 border-black bg-green-300 px-3 py-1 text-xs font-bold text-black">正在出售</div>
          </div>

          <!-- NFT 数据信息 -->
          <div class="border-2 border-black bg-white p-4">
            <div class="flex items-center gap-2 mb-3">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="currentColor" class="text-black">
                <circle cx="12" cy="12" r="1"/>
              </svg>
              <span class="font-bold text-black text-sm">铸造数据</span>
            </div>

            <div class="space-y-3">
              <div class="flex justify-between text-sm">
                <span class="text-gray-600">Token ID</span>
                <span class="font-bold text-black">#11</span>
              </div>
              <div class="border-b-2 border-black"></div>

              <div class="flex justify-between text-sm">
                <span class="text-gray-600">合约地址</span>
                <span class="font-mono text-xs text-black">0xa670...c427d</span>
              </div>
              <div class="border-b-2 border-black"></div>

              <div class="flex justify-between text-sm">
                <span class="text-gray-600">文件 Hash</span>
                <span class="font-mono text-xs text-black">126f0...ba655c</span>
              </div>
              <div class="border-b-2 border-black"></div>

              <div class="flex justify-between text-sm">
                <span class="text-gray-600">铸造时间</span>
                <span class="text-black">2025/12/9 14:14:05</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：信息区域 -->
        <div>
          <!-- 集合标签 -->
          <div class="text-xs font-bold text-black mb-2">IMAGENFT COLLECTION</div>

          <!-- 标题 -->
          <h1 class="text-3xl font-bold text-black mb-6">IMG_3656.JPG</h1>

          <!-- 创建者信息 -->
          <div class="flex gap-4 mb-6 pb-6 border-b-2 border-black">
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 border-2 border-black bg-gray-200 flex items-center justify-center text-xs font-bold">S</div>
              <div>
                <div class="text-xs text-gray-600">持有者</div>
                <div class="text-xs font-bold text-black font-mono">shuomc (0x63f5...66e432)</div>
              </div>
            </div>
            <div class="flex items-center gap-2">
              <div class="w-8 h-8 border-2 border-black bg-gray-200 flex items-center justify-center text-xs font-bold">T</div>
              <div>
                <div class="text-xs text-gray-600">创作者</div>
                <div class="text-xs font-bold text-black">test</div>
              </div>
            </div>
          </div>

          <!-- 价格区域 -->
          <div class="mb-6 p-4 border-2 border-black bg-gray-100">
            <div class="text-sm text-gray-600 font-bold mb-2">当前价格</div>
            <div class="text-2xl font-bold text-black mb-4">11.0000 ETH</div>
            
            <!-- 购买按钮 -->
            <button class="w-full px-4 py-3 border-2 border-black bg-blue-400 text-black font-bold text-sm flex items-center justify-center gap-2 hover:bg-blue-500">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="1"/>
              </svg>
              立即购买
            </button>
          </div>

          <!-- 描述 -->
          <div class="mb-6">
            <h3 class="text-sm font-bold text-black mb-2">描述</h3>
            <div class="text-xs text-gray-600 p-3 border-2 border-black bg-white">暂无描述信息</div>
          </div>

          <!-- 详细信息 -->
          <div>
            <h3 class="text-sm font-bold text-black mb-3">详细信息</h3>
            
            <div class="border-2 border-black bg-white overflow-hidden">
              <div class="border-b-2 border-black px-4 py-2 flex justify-between">
                <span class="text-xs font-bold text-black">NFT ID (System)</span>
                <span class="text-xs text-black">19982749373041868882</span>
              </div>
              <div class="border-b-2 border-black px-4 py-2 flex justify-between">
                <span class="text-xs font-bold text-black">Metadata URI</span>
                <span class="text-xs text-blue-600 font-bold cursor-pointer">View Source</span>
              </div>
              <div class="px-4 py-2 flex justify-between">
                <span class="text-xs font-bold text-black">区块链网络</span>
                <span class="text-xs text-black">FISCO BCOS</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// Mock 数据
const nftDetail = ref({
  name: 'IMG_3656.JPG',
  collection: 'IMAGENFT COLLECTION',
  price: '11.0000 ETH',
  status: '正在出售',
  holder: {
    name: 'shuomc',
    address: '0x63f5dde2ebb8ef76ddf5a8313bd76552eee66e432'
  },
  creator: {
    name: 'test'
  },
  tokenId: '#11',
  contractAddress: '0xa670...c427d',
  fileHash: '126f0...ba655c',
  mintTime: '2025/12/9 14:14:05',
  nftIdSystem: '19982749373041868882',
  network: 'FISCO BCOS',
  description: '暂无描述信息'
})

const handleBuy = () => {
  // 购买逻辑
}
</script>

<style scoped>
</style>
