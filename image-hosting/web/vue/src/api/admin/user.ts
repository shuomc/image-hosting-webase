import service from '@/utils/request';

export interface UserListQuery {
  page: number;
  size: number;
  keyword?: string;
}

export interface UserUpdate {
  userId: string;
  userRole?: string;
  status?: number;
}

export interface UserCreate {
  userName: string;
  password?: string;
  userEmail: string;
  userRole: string;
}

export interface RoleCreate {
  rolesName: string;
  description: string;
}

export interface RoleVO {
  rolesId: number;
  rolesName: string;
  description: string;
}

export interface UserVO {
  userId: string;
  userName: string;
  userEmail: string;
  userRole: string;
  status: number;
  createTime: string;
  avatarUrl: string;
}

export interface UserPageVO {
  total: number;
  list: UserVO[];
}

export interface UserStatsVO {
  userId: string;
  totalUploads: number;
  totalViews: number;
  totalDownloads: number;
  totalLikes: number;
  storageLimit: number;
  storageUsed: number;
  updateTime: string;
}

export function getUserList(data: UserListQuery) {
  return service.post('/api/admin/users/list', data);
}

export function updateUser(data: UserUpdate) {
  return service.put('/api/admin/users', data);
}

export function createUser(data: UserCreate) {
  return service.post('/api/admin/users', data);
}

export function refreshAllUserStats() {
  return service.post('/api/admin/users/stats/refresh');
}

export function getUserStats(userId: string) {
  return service.get(`/api/admin/users/${userId}/stats`);
}

/**
 * 获取角色列表
 */
export function getRolesList() {
  return service.get('/api/admin/roles/list');
}

/**
 * 创建角色
 */
export function createRole(data: RoleCreate) {
  return service.post('/api/admin/roles', data);
}

/**
 * 删除角色
 */
export function deleteRole(rolesId: number) {
  return service.delete(`/api/admin/roles/${rolesId}`);
}
