import axios from 'axios'
import store from '@/store'
import router from '@/router'
import { Message } from 'element-ui'

const request = axios.create({
  baseURL: '/api/v1',
  timeout: 30000
})

// 请求拦截器：添加 Authorization 头
request.interceptors.request.use(
  config => {
    const token = store.state.token
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：解包 Result<T>，处理 401
request.interceptors.response.use(
  response => {
    const res = response.data
    // 假设后端返回格式为 { code: 200, data: ..., message: ... }
    if (res.code && res.code !== 200) {
      Message.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data !== undefined ? res.data : res
  },
  error => {
    if (error.response) {
      const { status } = error.response
      if (status === 401) {
        store.commit('CLEAR_ALL')
        router.push('/login')
        Message.error('登录已过期，请重新登录')
      } else {
        Message.error(error.response.data?.message || '网络错误')
      }
    } else {
      Message.error('网络连接异常')
    }
    return Promise.reject(error)
  }
)

export default request
