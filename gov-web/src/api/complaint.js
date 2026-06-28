import request from '@/utils/request'

// ==================== 投诉工单（匹配后端 WorkController /complaint/work）====================
export function getComplaintList(params) {
  return request({ url: '/complaint/work/list', method: 'get', params })
}

export function getComplaintDetail(id) {
  return request({ url: `/complaint/work/${id}`, method: 'get' })
}

export function submitComplaint(data) {
  return request({ url: '/complaint/work', method: 'post', data })
}

// 处理记录 POST /complaint/handle
export function handleComplaint(data) {
  return request({ url: '/complaint/handle', method: 'post', data })
}

// ==================== 建议征集（匹配后端 SuggestionController /complaint/suggestion）====================
export function getSuggestionList(params) {
  return request({ url: '/complaint/suggestion/list', method: 'get', params })
}

export function submitSuggestion(data) {
  return request({ url: '/complaint/suggestion', method: 'post', data })
}

// ==================== 投诉分类 ====================
export function getComplaintCategoryList(params) {
  return request({ url: '/complaint/category/list', method: 'get', params })
}
