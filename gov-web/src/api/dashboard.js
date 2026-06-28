import request from '@/utils/request'

// 统计概览 —— 后端暂无专用 dashboard 端点
// 由前端聚合多个服务数据展示
export function getDashboardStats() {
  return request({ url: '/user/list', method: 'get', params: { pageNum: 1, pageSize: 1 } })
}
