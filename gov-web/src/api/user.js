import request from '@/utils/request'

// ==================== 登录（匹配后端 UserController）====================
export function login(data) {
  return request({ url: '/user/login', method: 'post', data })
}

// ==================== 开发环境Token ====================
export function devToken(params) {
  return request({ url: '/user/dev-token', method: 'get', params })
}

// ==================== 用户管理 ====================
export function getUserList(params) {
  return request({ url: '/user/list', method: 'get', params })
}

export function getUserById(id) {
  return request({ url: `/user/${id}`, method: 'get' })
}

export function addUser(data) {
  return request({ url: '/user', method: 'post', data })
}

export function updateUser(data) {
  return request({ url: '/user', method: 'put', data })
}

export function deleteUser(id) {
  return request({ url: `/user/${id}`, method: 'delete' })
}

// ==================== 角色管理 ====================
export function getRoleList() {
  return request({ url: '/role/list', method: 'get' })
}

export function getRolePage(params) {
  return request({ url: '/role/page', method: 'get', params })
}

export function addRole(data) {
  return request({ url: '/role', method: 'post', data })
}

export function updateRole(data) {
  return request({ url: '/role', method: 'put', data })
}

export function deleteRole(id) {
  return request({ url: `/role/${id}`, method: 'delete' })
}

// ==================== 部门管理 ====================
export function getDeptList() {
  return request({ url: '/dept/list', method: 'get' })
}

export function getDeptPage(params) {
  return request({ url: '/dept/page', method: 'get', params })
}

export function addDept(data) {
  return request({ url: '/dept', method: 'post', data })
}

export function updateDept(data) {
  return request({ url: '/dept', method: 'put', data })
}

export function deleteDept(id) {
  return request({ url: `/dept/${id}`, method: 'delete' })
}
