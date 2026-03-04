import service from '@/utils/request';

export interface AdminDashboardStats {
  totalUsers: number;
  totalImages: number;
  storageUsed: string;
  nftTransactionVolume: number;
  uploadTrendDates: string[];
  uploadTrendCounts: number[];
  imageTypes: string[];
  imageTypeCounts: number[];
  mintedImageCount: number;
  publicImageCount: number;
  privateImageCount: number;
}

export function getDashboardStats() {
  return service.get('/api/admin/stats');
}
