import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

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
    meta: { requiresAuth: true, title: '首页' }
  },
  {
    path: '/item',
    name: 'Item',
    component: () => import('@/views/item/index.vue'),
    meta: { requiresAuth: true, title: '事项管理' }
  },
  {
    path: '/case',
    name: 'Case',
    component: () => import('@/views/case/index.vue'),
    meta: { requiresAuth: true, title: '办件管理' }
  },
  {
    path: '/license',
    name: 'License',
    component: () => import('@/views/license/index.vue'),
    meta: { requiresAuth: true, title: '证照管理' }
  },
  {
    path: '/complaint',
    name: 'Complaint',
    component: () => import('@/views/complaint/index.vue'),
    meta: { requiresAuth: true, title: '投诉建议' }
  },
  {
    path: '/open',
    name: 'Open',
    component: () => import('@/views/open/index.vue'),
    meta: { requiresAuth: true, title: '政务公开' }
  },
  {
    path: '/',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
