import request from '@/utils/request'

// ==========================================
// 1. 浏览与查询接口
// ==========================================

/**
 * 获取 NFT 市场列表
 * @param params
 *  - page: 页码
 *  - pageSize: 每页数量
 *  - query: 搜索关键词 (可选)
 *  - category: 分类 (可选)
 *  - sort: 排序 'new' | 'price_asc' | 'price_desc' (可选)
 */
export function getNFTList(params: { 
  page: number; 
  pageSize: number; 
  query?: string; 
  category?: string; 
  sort?: string 
}) {
  return request({
    url: '/nft/list',
    method: 'get',
    params
  })
}

/**
 * 获取我的 NFT
 * @param params
 *  - page: 页码
 *  - pageSize: 每页数量
 *  - mode: 'owned'(持有) | 'created'(铸造) | 'sold'(已卖出)
 */
export function getMyNFTs(params: { 
  page: number; 
  pageSize: number; 
  mode?: 'owned' | 'created' | 'sold' 
}) {
  return request({
    url: '/nft/my',
    method: 'get',
    params
  })
}

/**
 * 获取 NFT 详情
 */
export function getNFTDetail(nftId: string) {
  return request({
    url: `/nft/detail/${nftId}`,
    method: 'get'
  })
}

/**
 * 获取交易历史 (我的交易)
 * @param params
 *  - type: 交易类型筛选 (可选)
 */
export function getNFTTransactions(params: { 
  page: number; 
  pageSize: number; 
  type?: string 
}) {
  return request({
    url: '/nft/transactions',
    method: 'get',
    params
  })
}

/**
 * 获取当前余额
 */
export function getBalance() {
  return request({
    url: '/nft/balance',
    method: 'get'
  })
}

// ==========================================
// 2. 核心交易接口
// ==========================================

/**
 * 铸造 NFT
 * 后端使用 @RequestBody Map，前端使用 data 传 JSON
 */
export function mintNFT(data: { 
  imageId: string;
  fileHash: string;
  thumbnailMinioUrl: string; 
  name: string; 
  description: string; 
  price: number; 
  collectionId?: number 
}) {
  return request({
    url: '/nft/mint',
    method: 'post',
    data
  })
}

/**
 * 购买 NFT
 */
export function buyNFT(nftId: string) {
  return request({
    url: `/nft/buy/${nftId}`,
    method: 'post'
  })
}

/**
 * 赠送/转移 NFT
 */
export function transferNFT(data: { nftId: string; toAddress: string }) {
  return request({
    url: '/nft/transfer',
    method: 'post',
    data
  })
}

// ==========================================
// 3. 商品管理接口
// ==========================================

/**
 * 设置 NFT 价格
 * 后端使用 @RequestParam，前端使用 params 拼接到 URL
 */
export function setNFTPrice(nftId: string, price: number) {
  return request({
    url: `/nft/price/${nftId}`,
    method: 'post',
    params: { price }
  })
}

/**
 * 上架 NFT
 */
export function putOnShelf(nftId: string) {
  return request({
    url: `/nft/shelf/on/${nftId}`,
    method: 'post'
  })
}

/**
 * 下架 NFT (原 cancelNFTSale)
 */
export function offShelf(nftId: string) {
  return request({
    url: `/nft/shelf/off/${nftId}`,
    method: 'post'
  })
}

// ==========================================
// 4. 资金管理接口
// ==========================================

/**
 * 充值
 */
export function deposit(amount: number) {
  return request({
    url: '/nft/deposit',
    method: 'post',
    params: { amount }
  })
}

/**
 * 提现
 */
export function withdraw(amount: number) {
  return request({
    url: '/nft/withdraw',
    method: 'post',
    params: { amount }
  })
}

// ==========================================
// 5. 兼容旧接口
// ==========================================

export function getWebaseBalance() {
  return request({
    url: '/nft/webase/balance',
    method: 'get'
  })
}

export function webaseDeposit(amount: number) {
  return request({
    url: '/nft/webase/deposit',
    method: 'post',
    params: { amount }
  })
}

export function getWebaseNFTInfo(tokenId: string) {
  return request({
    url: `/nft/webase/nft/${tokenId}`,
    method: 'get'
  })
}

export function getWebaseOwnedNFTs() {
  return request({
    url: '/nft/webase/owned',
    method: 'get'
  })
}

// ==========================================
// 6. 用户账户管理 (新增)
// ==========================================

/**
 * 检查当前用户是否已激活区块链账户
 * 对应后端: GET /nft/user/check
 */
export function checkRegistrationStatus() {
  return request({
    url: '/nft/user/check',
    method: 'get'
  })
}

/**
 * 一键激活/注册区块链账户
 * 对应后端: POST /nft/user/register
 */
export function registerBlockchainAccount() {
  return request({
    url: '/nft/user/register',
    method: 'post'
  })
}


// ==========================================
// 类型定义 (VO)
// ==========================================

export interface NFTInfo {
  nftId: string
  tokenId: string
  
  name: string
  description: string
  imageUrl: string     // 预览图地址
  price: number
  isForSale: boolean
  
  ownerId: string
  ownerName: string
  ownerAvatar: string
  ownerAddress: string
  
  creatorName: string
  contractAddress: string
  createTime: string
}

export interface NFTTransaction {
  transactionId: string
  transactionHash: string
  
  nftName: string
  imageUrl: string
  
  fromAddress: string
  toAddress: string
  
  price: number
  status: number 
  type?: string 
  
  createTime: string
}