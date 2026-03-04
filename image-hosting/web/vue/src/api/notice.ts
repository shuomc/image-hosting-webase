import request from '@/utils/request';

export function getNoticeList() {
  return request({
    url: '/api/notices/list',
    method: 'get'
  });
}
