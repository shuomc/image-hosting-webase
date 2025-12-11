import service from '@/utils/request';

export interface AdminDashboardStats {
  totalUsers: number;
  totalImages: number;
  storageUsed: string;
  nftTransactionVolume: number;
}

export function getDashboardStats() {
  return service.get('/api/admin/stats');
}
