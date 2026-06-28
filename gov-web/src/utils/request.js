import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import router from '@/router'

const request = axios.create({
  baseURL: '/api/v1',
  timeout: 30000
})

request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    const token = userStore.token
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => {
    const res = response.data
    // 业务错误
    if (res.code && res.code !== 200) {
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data !== undefined ? res.data : res
  },
  error => {
    if (error.response) {
      const { status, config } = error.response
      if (status === 401) {
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
        ElMessage.error('登录已过期，请重新登录')
        return Promise.reject(error)
      }
      // 非 401 静默 reject，控制台可见调试
      console.error(`[request] ${status} ${config?.url}`, error.response.data)
    } else {
      // 网络错误（后端没启动/连接拒绝）
      console.error(`[request] NETWORK ERROR: ${error.config?.url} — 后端可能未启动`, error.message)
    }
    return Promise.reject(error)
  }
)

export default request
