import request from '@/utils/request'

// ==================== 通知公告（匹配后端 NoticeController /open/notice）====================
export function getNoticeList(params) {
  return request({ url: '/open/notice/list', method: 'get', params })
}

export function getNoticeDetail(id) {
  return request({ url: `/open/notice/${id}`, method: 'get' })
}

// ==================== 政策法规（匹配后端 PolicyController /open/policy）====================
export function getPolicyList(params) {
  return request({ url: '/open/policy/list', method: 'get', params })
}

export function getPolicyDetail(id) {
  return request({ url: `/open/policy/${id}`, method: 'get' })
}

// ==================== 依申请公开（匹配后端 ApplyController /open/apply）====================
// 提交申请 POST /open/apply/submit
export function submitOpenApply(data) {
  return request({ url: '/open/apply/submit', method: 'post', data })
}

// 申请人查看自己的申请列表 GET /open/apply/list?pageNum=1&pageSize=10
export function getMyApplyList(params) {
  return request({ url: '/open/apply/list', method: 'get', params })
}

// ==================== 公开目录 ====================
export function getOpenCatalogTree(type) {
  return request({ url: '/open/catalog/tree', method: 'get', params: { catalogType: type } })
}
