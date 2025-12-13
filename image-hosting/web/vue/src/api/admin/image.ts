import request from '@/utils/request'

export interface ImageVO {
  imageId: string
  fileName: string
  thumbnailMinioUrl: string
  originMinioUrl: string
  size: number
  contentType: string
  userId: string
  isPublic: boolean
  createTime: string
  fileHash: string
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
