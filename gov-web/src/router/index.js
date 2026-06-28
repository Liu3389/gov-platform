import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

// 登录页独立，不包裹在布局里
const loginRoute = {
  path: '/login',
  name: 'Login',
  component: () => import('@/views/login/index.vue')
}

// 所有需登录页面共用布局
const appRoutes = {
  path: '/',
  component: () => import('@/layout/index.vue'),
  children: [
    // ====== 管理员路由 ======
    {
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index.vue'),
      meta: { title: '工作台', role: 'admin' }
    },
    {
      path: 'item',
      name: 'Item',
      component: () => import('@/views/item/index.vue'),
      redirect: '/item/list',
      meta: { title: '事项管理', role: 'admin' },
      children: [
        { path: 'list', name: 'ItemList', component: () => import('@/views/item/list.vue'), meta: { title: '事项列表', role: 'admin' } },
        { path: 'add', name: 'ItemAdd', component: () => import('@/views/item/add.vue'), meta: { title: '事项录入', role: 'admin' } }
      ]
    },
    {
      path: 'case',
      name: 'Case',
      component: () => import('@/views/case/index.vue'),
      redirect: '/case/todo',
      meta: { title: '办件管理', role: 'admin' },
      children: [
        { path: 'todo', name: 'CaseTodo', component: () => import('@/views/case/todo.vue'), meta: { title: '待办任务', role: 'admin' } },
        { path: 'done', name: 'CaseDone', component: () => import('@/views/case/done.vue'), meta: { title: '已办任务', role: 'admin' } },
        { path: 'reception', name: 'CaseReception', component: () => import('@/views/case/reception.vue'), meta: { title: '窗口受理', role: 'admin' } }
      ]
    },
    {
      path: 'system',
      name: 'System',
      redirect: '/system/user',
      meta: { title: '系统管理', role: 'admin' },
      children: [
        { path: 'user', name: 'SystemUser', component: () => import('@/views/system/user.vue'), meta: { title: '用户管理', role: 'admin' } },
        { path: 'role', name: 'SystemRole', component: () => import('@/views/system/role.vue'), meta: { title: '角色管理', role: 'admin' } },
        { path: 'dept', name: 'SystemDept', component: () => import('@/views/system/dept.vue'), meta: { title: '部门管理', role: 'admin' } }
      ]
    },

    // ====== 用户端路由 ======
    {
      path: 'portal',
      name: 'Portal',
      component: () => import('@/views/portal/index.vue'),
      meta: { title: '办事大厅', role: 'user' }
    },
    {
      path: 'my/cases',
      name: 'MyCases',
      component: () => import('@/views/portal/MyCases.vue'),
      meta: { title: '我的办件', role: 'user' }
    },
    {
      path: 'my/licenses',
      name: 'MyLicenses',
      component: () => import('@/views/portal/MyLicenses.vue'),
      meta: { title: '我的证照', role: 'user' }
    },

    // ====== 共用路由 ======
    {
      path: 'license',
      name: 'License',
      component: () => import('@/views/license/index.vue'),
      meta: { title: '证照管理' }
    },
    {
      path: 'complaint',
      name: 'Complaint',
      component: () => import('@/views/complaint/index.vue'),
      meta: { title: '投诉建议' }
    },
    {
      path: 'open',
      name: 'Open',
      component: () => import('@/views/open/index.vue'),
      meta: { title: '政务公开' }
    },
    { path: '', redirect: '/dashboard' }
  ]
}

const routes = [
  loginRoute,
  appRoutes,
  { path: '/404', name: 'NotFound', component: () => import('@/views/error/404.vue') },
  { path: '/:pathMatch(.*)*', redirect: '/404' }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 管理员路由白名单
const adminPaths = ['/dashboard', '/item', '/case', '/system']
// 用户端路由白名单
const userPaths = ['/portal', '/my']

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  const token = userStore.token

  // 未登录 → 只能去登录页
  if (to.path !== '/login' && !token) {
    next('/login')
    return
  }

  // 已登录访问登录页 → 按角色跳转
  if (to.path === '/login' && token) {
    next(userStore.isAdmin ? '/dashboard' : '/portal')
    return
  }

  // 管理员不能访问用户端路由
  if (token && userStore.isAdmin && userPaths.some(p => to.path.startsWith(p))) {
    next('/dashboard')
    return
  }

  // 普通用户不能访问管理端路由
  if (token && userStore.isUser && adminPaths.some(p => to.path.startsWith(p))) {
    next('/portal')
    return
  }

  // 首页重定向
  if (to.path === '/' && token) {
    next(userStore.isAdmin ? '/dashboard' : '/portal')
    return
  }

  next()
})

export default router
