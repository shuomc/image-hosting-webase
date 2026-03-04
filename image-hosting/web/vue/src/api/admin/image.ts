import request from '@/utils/request'

export interface ImageVO {
  imageId: string
  fileName: string
  thumbnailMinioUrl: string
  originMinioUrl: string
  watermarkMinioUrl?: string
  size: number
  contentType: string
  userId: string
  isPublic: boolean
  createTime: string
  fileHash: string
  description?: string
  width?: number
  height?: number
  cameraMake?: string
  cameraModel?: string
  lensModel?: string
  focalLength?: string
  aperture?: string
  shutterSpeed?: string
  iso?: number
  shootTime?: string
  locationName?: string
  latitude?: number
  longitude?: number
  viewCount?: number
  downloadCount?: number
  likeCount?: number
  category?: string
  dominantColor?: string
  auditStatus?: number
  auditMsg?: string
  nftId?: string
  tokenId?: string
}

export interface ImageListQuery {
  page: number
  size: number
  keyword?: string
  type?: string // 'public', 'private'
}

export function getImageList(query: ImageListQuery) {
  return request({
    url: '/api/admin/images/list',
    method: 'get',
    params: query
  })
}

export function deleteImage(imageId: string) {
  return request({
    url: `/api/admin/images/${imageId}`,
    method: 'delete'
  })
}
