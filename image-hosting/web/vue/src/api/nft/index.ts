import service from '@/utils/request';
import { API_BASE_URL } from '@/config';

// NFT列表接口
export interface NFTListParams {
  page: number;
  pageSize: number;
  query?: string;
}

export interface NFTInfo {
  nftId: string;
  imageId: string;
  ownerId: string;
  tokenId: string;
  contractAddress: string;
  price: number;
  isForSale: boolean;
  createTime: string;
  updateTime: string;
  image?: {
    minioUrl: string;
    fileName: string;
    description: string | null;
  };
}

export interface NFTListResponse {
  total: number;
  list: NFTInfo[];
}

// NFT交易记录接口
export interface NFTTransaction {
  transactionId: string;
  nftId: string;
  fromUserId: string;
  toUserId: string;
  price: number;
  transactionHash: string;
  createTime: string;
}

export interface NFTTransactionListResponse {
  total: number;
  list: NFTTransaction[];
}

// 获取NFT列表
export function getNFTList(params: NFTListParams) {
  return service.get(`${API_BASE_URL}/nft/list`, { params });
}

// 获取我的NFT列表
export function getMyNFTs(params: NFTListParams) {
  return service.get(`${API_BASE_URL}/nft/my`, { params });
}

// 铸造NFT
export interface MintNFTParams {
  imageId: string;
  description: string;
  price: number;
  minioUrl: string;
}

export function mintNFT(data: MintNFTParams) {
  return service.post(`${API_BASE_URL}/nft/mint`, null, {
    params: {
      imageId: data.imageId,
      description: data.description,
      price: data.price,
      minioUrl: data.minioUrl
    }
  });
}

// 购买NFT
export function buyNFT(nftId: string) {
  return service.post(`${API_BASE_URL}/nft/buy/${nftId}`);
}

export interface SetNFTPriceParams {
  nftId: string;
  price: number;
}

export function setNFTPrice(data: SetNFTPriceParams) {
  const { nftId, price } = data;
  return service.post(`/nft/price/${nftId}?price=${price}`);
}

// 取消NFT销售
export function cancelNFTSale(nftId: string) {
  return service.post(`${API_BASE_URL}/nft/cancel/${nftId}`);
}

// 获取NFT详情
export function getNFTDetail(nftId: string) {
  return service.get(`${API_BASE_URL}/nft/detail/${nftId}`);
}

// 获取NFT交易记录
export function getNFTTransactions(nftId: string, params: NFTListParams) {
  return service.get(`${API_BASE_URL}/nft/transactions/${nftId}`, { params });
}

// 获取用户余额
export function getBalance() {
  return service.get(`${API_BASE_URL}/nft/balance`);
}

// 充值
export interface DepositParams {
  amount: number;
}

export function deposit(data: DepositParams) {
  return service.post(`${API_BASE_URL}/nft/deposit`, data);
}
