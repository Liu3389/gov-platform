import request from '@/utils/request'

// ==================== 统一受理（匹配后端 RecordController /reception）====================
export function getCaseList(params) {
  return request({ url: '/reception/list', method: 'get', params })
}

export function getCaseDetail(id) {
  return request({ url: `/reception/${id}`, method: 'get' })
}

export function submitApplication(data) {
  return request({ url: '/reception/submit', method: 'post', data })
}

export function acceptApplication(data) {
  return request({ url: '/reception/accept', method: 'post', data })
}

export function rejectApplication(data) {
  return request({ url: '/reception/reject', method: 'post', data })
}

// ==================== 审批流转（匹配后端 WorkflowTaskController /workflow）====================
// 待办：GET /workflow/todo?userId=xxx&pageNum=1&pageSize=10
export function getTodoList(params) {
  return request({ url: '/workflow/todo', method: 'get', params })
}

// 所有任务：GET /workflow/list?pageNum=1&pageSize=10&assignee=xxx&status=xxx
export function getWorkflowList(params) {
  return request({ url: '/workflow/list', method: 'get', params })
}

export function getWorkflowDetail(id) {
  return request({ url: `/workflow/${id}`, method: 'get' })
}

// 审批（通过/驳回/转办统一走 /workflow/approval POST）
export function approveTask(data) {
  return request({ url: '/workflow/approval', method: 'post', data })
}

// 驳回（同审批接口，后端通过 result 区分）
export const rejectTask = approveTask

// 转办（同审批接口，后端通过 result 和 targetUserId 区分）
export const transferTask = approveTask

// 任务详情：GET /workflow/{id}
export const getTaskDetail = getWorkflowDetail

// 已办：GET /workflow/done?userId=xxx&pageNum=1&pageSize=10
export function getDoneList(params) {
  return request({ url: '/workflow/done', method: 'get', params })
}

// 启动流程
export function startProcess(data) {
  return request({ url: '/workflow/process', method: 'post', data })
}
