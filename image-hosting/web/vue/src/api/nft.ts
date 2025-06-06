import request from '@/utils/request'

// NFT列表查询
export function getNFTList(params: { page: number; pageSize: number; query?: string }) {
  return request({
    url: '/nft/list',
    method: 'get',
    params
  })
}

// 获取我的NFT
export function getMyNFTs(params: { page: number; pageSize: number }) {
  return request({
    url: '/nft/my',
    method: 'get',
    params
  })
}

// 铸造NFT
export function mintNFT(data: { imageId: string; description: string; price: number; minioUrl: string }) {
  return request({
    url: '/nft/mint',
    method: 'post',
    params: {
      imageId: data.imageId,
      description: data.description,
      price: data.price,
      minioUrl: data.minioUrl
    }
  })
}

// 购买NFT
export function buyNFT(nftId: string) {
  return request({
    url: `/nft/buy/${nftId}`,
    method: 'post'
  })
}

// 设置NFT价格
export function setNFTPrice(nftId: string, price: number) {
  return request({
    url: `/nft/price/${nftId}`,
    method: 'post',
    params: { price: price }
  })
}

// 取消NFT销售
export function cancelNFTSale(nftId: string) {
  return request({
    url: `/nft/cancel/${nftId}`,
    method: 'post'
  })
}

// 获取NFT详情
export function getNFTDetail(nftId: string) {
  return request({
    url: `/nft/detail/${nftId}`,
    method: 'get'
  })
}

// 获取NFT交易历史
export function getNFTTransactions() {
  return request({
    url: '/nft/transactions',
    method: 'get'
  })
}

// Webase接口
// 获取余额
export function getWebaseBalance() {
  return request({
    url: '/nft/webase/balance',
    method: 'get'
  })
}

// 充值
export function webaseDeposit(amount: number) {
  return request({
    url: '/nft/webase/deposit',
    method: 'post',
    params: { amount }
  })
}

// 获取NFT信息
export function getWebaseNFTInfo(tokenId: string) {
  return request({
    url: `/nft/webase/nft/${tokenId}`,
    method: 'get'
  })
}

// 获取用户拥有的NFT
export function getWebaseOwnedNFTs() {
  return request({
    url: '/nft/webase/owned',
    method: 'get'
  })
}

// NFT信息类型定义
export interface NFTInfo {
  nftId: string
  imageId: string
  ownerId: string
  tokenId: number
  contractAddress: string
  description: string | null
  price: number
  isForSale: boolean
  createTime: string
  updateTime: string | null
  isDelete: boolean
  minioUrl?: string
  blockchainAddress?: string
}

// NFT交易记录类型定义
export interface NFTTransaction {
  transactionId: string
  nftId: string
  fromUserId: string
  toUserId: string
  price: number
  transactionHash: string
  createTime: string
  isDelete: boolean
} 