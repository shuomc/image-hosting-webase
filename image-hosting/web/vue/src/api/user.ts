import request from '@/utils/request'

export function getUserInfo() {
  return request({
    url: '/api/user/current',
    method: 'get'
  })
}

export function updateUserInfo(data: any) {
  return request({
    url: '/api/user/update',
    method: 'post',
    data
  })
}
