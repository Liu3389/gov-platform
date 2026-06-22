import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo as getUserInfoApi } from '@/api'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.username || '',
    roles: (state) => state.userInfo?.roles || [],
  },

  actions: {
    // 登录
    async login(loginForm) {
      const res = await loginApi(loginForm)
      if (res.code === 200) {
        this.token = res.data.token
        localStorage.setItem('token', res.data.token)
        return true
      }
      return false
    },

    // 获取用户信息
    async getUserInfo() {
      const res = await getUserInfoApi()
      if (res.code === 200) {
        this.userInfo = res.data
      }
    },

    // 登出
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    }
  }
})
