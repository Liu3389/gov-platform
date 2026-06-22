import request from '@/utils/request'

// ==================== 登录相关 ====================
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

// ==================== 事项管理 ====================
export function getItemList(params) {
  return request({
    url: '/item/list',
    method: 'get',
    params
  })
}

export function getItemDetail(id) {
  return request({
    url: `/item/${id}`,
    method: 'get'
  })
}

// ==================== 办件管理 ====================
export function getTodoList(params) {
  return request({
    url: '/workflow/todo',
    method: 'get',
    params
  })
}

export function submitApproval(data) {
  return request({
    url: '/workflow/approve',
    method: 'post',
    data
  })
}

// ==================== 通用 CRUD ====================
export function getList(url, params) {
  return request({
    url,
    method: 'get',
    params
  })
}

export function addItem(url, data) {
  return request({
    url,
    method: 'post',
    data
  })
}

export function updateItem(url, data) {
  return request({
    url,
    method: 'put',
    data
  })
}

export function deleteItem(url, id) {
  return request({
    url: `${url}/${id}`,
    method: 'delete'
  })
}
