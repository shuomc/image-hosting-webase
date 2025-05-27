// API基础URL
export const API_BASE_URL = 'http://localhost:8080/api';

// 上传文件大小限制（单位：MB）
export const UPLOAD_FILE_SIZE_LIMIT = 10;

// 支持的图片类型
export const SUPPORTED_IMAGE_TYPES = [
  'image/jpeg',
  'image/png',
  'image/gif',
  'image/webp'
];

// 分页配置
export const DEFAULT_PAGE_SIZE = 12;
export const PAGE_SIZES = [12, 24, 36, 48];

// 路由配置
export const ROUTES = {
  LOGIN: '/login',
  REGISTER: '/register',
  HOME: '/',
  NFT_LIST: '/nft/list',
  NFT_DETAIL: '/nft/detail',
  NFT_MINT: '/nft/mint',
  MY_NFTS: '/nft/my',
  MY_IMAGES: '/images/my',
  UPLOAD: '/images/upload'
}; 