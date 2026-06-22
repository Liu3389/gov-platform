import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/item',
    name: 'Item',
    component: () => import('@/views/item/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/case',
    name: 'Case',
    component: () => import('@/views/case/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/license',
    name: 'License',
    component: () => import('@/views/license/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/complaint',
    name: 'Complaint',
    component: () => import('@/views/complaint/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/open',
    name: 'Open',
    component: () => import('@/views/open/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/',
    redirect: '/dashboard'
  }
]

const router = new VueRouter({
  mode: 'hash',
  routes
})

// 导航守卫：检查 token
router.beforeEach((to, from, next) => {
  const token = store.state.token
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
