import Vue from 'vue'
import Vuex from 'vuex'
import { login as loginApi, getUserInfo as getUserInfoApi } from '@/api/user'

Vue.use(Vuex)

const state = {
  token: localStorage.getItem('token') || '',
  userInfo: {},
  roles: []
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    if (token) {
      localStorage.setItem('token', token)
    } else {
      localStorage.removeItem('token')
    }
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
  },
  SET_ROLES(state, roles) {
    state.roles = roles
  },
  CLEAR_ALL(state) {
    state.token = ''
    state.userInfo = {}
    state.roles = []
    localStorage.removeItem('token')
  }
}

const actions = {
  async login({ commit }, { username, password }) {
    const data = await loginApi({ username, password })
    commit('SET_TOKEN', data.token)
    return data
  },
  async logout({ commit }) {
    commit('CLEAR_ALL')
  },
  async getUserInfo({ commit }) {
    const data = await getUserInfoApi()
    commit('SET_USER_INFO', data.userInfo || data)
    commit('SET_ROLES', data.roles || [])
    return data
  }
}

export default new Vuex.Store({
  namespaced: false,
  state,
  mutations,
  actions
})
