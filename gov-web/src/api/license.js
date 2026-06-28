import request from '@/utils/request'

// ==================== 证照管理（匹配后端 LicenseController /license）====================
export function getLicenseList(params) {
  return request({ url: '/license/list', method: 'get', params })
}

export function getLicenseDetail(id) {
  return request({ url: `/license/${id}`, method: 'get' })
}

// 核验 POST /license/{id}/verify
export function verifyLicense(id, data) {
  return request({ url: `/license/${id}/verify`, method: 'post', data })
}

// 注销/删除 DELETE /license/{id}
export function revokeLicense(id) {
  return request({ url: `/license/${id}`, method: 'delete' })
}
