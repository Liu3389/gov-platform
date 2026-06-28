import request from '@/utils/request'

// ==================== 事项管理（匹配后端 ItemController /item）====================
export function getItemList(params) {
  return request({ url: '/item/list', method: 'get', params })
}

export function getItemDetail(id) {
  return request({ url: `/item/${id}`, method: 'get' })
}

export function addItem(data) {
  return request({ url: '/item', method: 'post', data })
}

export function updateItem(data) {
  return request({ url: '/item', method: 'put', data })
}

export function deleteItem(id) {
  return request({ url: `/item/${id}`, method: 'delete' })
}
