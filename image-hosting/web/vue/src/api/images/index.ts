import service from '@/utils/request';
import { API_BASE_URL } from '@/config';

// 图片列表接口
export interface ImageListParams {
  page: number;
  pageSize: number;
  query?: string;
}

export interface ImageInfo {
  imageId: string;
  minioUrl: string;
  fileName: string;
  userId: string;
  contentType: string;
  size: number;
  isPublic: boolean;
  description: string | null;
  uploadTime: string;
}

export interface ImageListResponse {
  total: number;
  list: ImageInfo[];
}

// 获取我的图片列表
export function getMyImages(params: ImageListParams) {
  return service.get(`${API_BASE_URL}/images/my`, { params });
}

// 获取公开图片列表
export function getPublicImages(params: ImageListParams) {
  return service.get(`${API_BASE_URL}/images/public`, { params });
}

// 上传图片
export interface UploadImageParams {
  file: File;
  description?: string;
  isPublic?: boolean;
}

export function uploadImage(data: UploadImageParams) {
  const formData = new FormData();
  formData.append('file', data.file);
  if (data.description) {
    formData.append('description', data.description);
  }
  if (data.isPublic !== undefined) {
    formData.append('isPublic', String(data.isPublic));
  }
  return service.post(`${API_BASE_URL}/images/upload`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 删除图片
export function deleteImage(imageId: string) {
  return service.delete(`${API_BASE_URL}/images/${imageId}`);
}

// 更新图片信息
export interface UpdateImageParams {
  imageId: string;
  description?: string;
  isPublic?: boolean;
}

export function updateImage(data: UpdateImageParams) {
  return service.put(`${API_BASE_URL}/images/${data.imageId}`, data);
} 