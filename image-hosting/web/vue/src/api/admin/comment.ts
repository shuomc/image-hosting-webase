import request from '@/utils/request'

export interface CommentVO {
  commentId: string
  imageId: string
  userId: string
  userName: string
  content: string
  createTime: string
  updateTime: string
  isDelete: boolean
}

export interface CommentQuery {
  pageNum: number
  pageSize: number
  keyword?: string
}

export function listComments(query: CommentQuery) {
  return request({
    url: '/api/admin/comments/list',
    method: 'get',
    params: query
  })
}

export function updateComment(comment: Partial<CommentVO>) {
  return request({
    url: '/api/admin/comments/update',
    method: 'put',
    data: comment
  })
}

export function deleteComment(commentId: string) {
  return request({
    url: `/api/admin/comments/delete/${commentId}`,
    method: 'delete'
  })
}
