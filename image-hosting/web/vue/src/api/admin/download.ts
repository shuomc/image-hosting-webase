import request from '@/utils/request'

export interface DownloadVO {
  downloadId: string
  userId: string
  imageId: string
  createTime: string
  updateTime: string
  isDeleted: number
}

export interface DownloadQuery {
  pageNum: number
  pageSize: number
  keyword?: string
}

export function listDownloads(query: DownloadQuery) {
  return request({
    url: '/api/admin/downloads/list',
    method: 'get',
    params: query
  })
}

export function deleteDownload(downloadId: string) {
  return request({
    url: `/api/admin/downloads/${downloadId}`,
    method: 'delete'
  })
}
