import { defineStore } from 'pinia'
import { login as loginApi } from '@/api/user'
import request from '@/utils/request'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null'),
    currentRole: localStorage.getItem('currentRole') || ''
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.username || '',
    realName: (state) => state.userInfo?.realName || state.userInfo?.username || '',
    userId: (state) => state.userInfo?.userId || state.userInfo?.id || '',
    isAdmin: (state) => state.currentRole === 'admin',
    isUser: (state) => state.currentRole === 'user',
    welcomeName: (state) => state.userInfo?.realName || state.userInfo?.username || '用户'
  },

  actions: {
    async login(loginForm) {
      const res = await loginApi(loginForm)
      if (res.token) {
        this.setSession(res, 'user')
        return true
      }
      return false
    },

    async devLogin(role = 'admin') {
      const uid = role === 'admin' ? 1 : 2
      const username = role === 'admin' ? 'admin' : 'user'
      const res = await request({ url: '/user/dev-token', method: 'get', params: { role, username, userId: uid } })
      const token = typeof res === 'string' ? res : res.token || res
      if (token) {
        const displayRole = role === 'user' ? 'user' : 'admin'
        const displayName = role === 'admin' ? '系统管理员' : '普通用户'
        this.setSession({ token, username, userId: uid, realName: displayName }, displayRole)
        return true
      }
      return false
    },

    setSession(userData, role) {
      this.token = userData.token
      this.currentRole = role
      this.userInfo = {
        userId: userData.userId || userData.id || 1,
        username: userData.username,
        realName: userData.realName || userData.username,
        phone: userData.phone || ''
      }
      localStorage.setItem('token', this.token)
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
      localStorage.setItem('currentRole', this.currentRole)
    },

    logout() {
      this.token = ''
      this.userInfo = null
      this.currentRole = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('currentRole')
    }
  }
})
