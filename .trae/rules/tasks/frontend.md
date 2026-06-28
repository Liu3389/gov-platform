---
alwaysApply: false
description: 前端开发规范。成员E新建/修改Vue组件、路由、API调用、状态管理时应用此规则。
---
# 前端开发规则（成员E）

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 框架 | Vue 3 + Composition API | ^3.5 |
| 构建 | Vite | ^6 |
| 语言 | TypeScript | ^5 |
| 状态管理 | Pinia | ^2 |
| 路由 | Vue Router | ^4 |
| UI 组件库 | Element Plus | ^2 |
| HTTP 客户端 | Axios | ^1 |
| E2E 测试 | Playwright（推荐） | latest |

## 项目初始化

```bash
npm create vite@latest gov-frontend -- --template vue-ts
cd gov-frontend
npm install
npm install axios pinia vue-router element-plus
npm run dev
```

访问：http://localhost:5173

## 目录结构

```
gov-frontend/
├── src/
│   ├── api/            # Axios 封装 + 各模块 API
│   │   ├── index.ts    # Axios 实例（baseURL + 拦截器）
│   │   ├── user.ts     # 用户模块 API
│   │   ├── item.ts     # 事项模块 API
│   │   └── ...
│   ├── stores/         # Pinia 状态管理
│   │   ├── auth.ts     # 登录状态 + Token
│   │   └── ...
│   ├── router/         # Vue Router 配置
│   │   └── index.ts    # 路由 + 导航守卫
│   ├── views/          # 页面级组件
│   │   ├── login/      # 登录页
│   │   ├── dashboard/  # 工作台
│   │   └── ...
│   ├── components/     # 通用组件
│   ├── utils/          # 工具函数
│   ├── App.vue
│   └── main.ts
├── public/
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

## Axios 封装

```typescript
// src/api/index.ts
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const api = axios.create({
  baseURL: 'http://localhost:8091/api/v1',
  timeout: 15000
})

// 请求拦截器：自动带 Token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一处理错误
api.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) return data
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message))
  },
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
    }
    return Promise.reject(error)
  }
)

export default api
```

## API 模块示例

```typescript
// src/api/user.ts
import api from './index'

export interface UserVO {
  id: number
  username: string
  realName: string
  phone: string
  email: string
  status: number
  createTime: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  pageNum: number
  pageSize: number
}

// 对应后端 /api/v1/user/list
export function getUserList(params: { pageNum: number; pageSize: number; keyword?: string }) {
  return api.get<PageResult<UserVO>>('/user/list', { params })
}

// 对应后端 /api/v1/user/{id}
export function getUserById(id: number) {
  return api.get<UserVO>(`/user/${id}`)
}
```

## 请求格式对照表

| 后端 | 前端调用 |
|------|---------|
| `Result<PageResult<UserVO>>` | `api.get<PageResult<UserVO>>(...)`, `response.data` 直接就是 `PageResult` |
| `Result<UserVO>` | `api.get<UserVO>(...)`, `response.data` 直接就是 `UserVO` |
| `Result<Void>` | `api.post(...)`, 不关心返回值 |
| `GET /{resource}/list?pageNum=1&pageSize=10` | `api.get('/{resource}/list', { params: { pageNum: 1, pageSize: 10 } })` |
| `POST /{resource}` body: `ItemDTO` | `api.post('/{resource}', { ...dtoFields })` |
| `PUT /{resource}` body: `ItemDTO` | `api.put('/{resource}', { id: 1, ...dtoFields })` |
| `DELETE /{resource}/{id}` | `api.delete('/{resource}/' + id)` |

## Pinia 状态管理

```typescript
// src/stores/auth.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref('')
  const roles = ref<string[]>([])

  function setToken(t: string) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function logout() {
    token.value = ''
    username.value = ''
    roles.value = []
    localStorage.removeItem('token')
  }

  return { token, username, roles, setToken, logout }
})
```

## Vue Router 配置

```typescript
// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('@/views/layout/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        { path: '', redirect: '/dashboard' },
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/DashboardView.vue')
        },
        {
          path: 'user',
          name: 'User',
          component: () => import('@/views/user/UserListView.vue')
        }
      ]
    }
  ]
})

// 导航守卫：未登录跳转登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
```

## 页面开发流程

```
1. 去 Knife4j(http://localhost:8091/doc.html) 找到接口
2. 确认接口可用：点 "发送" 看返回 200
3. 看 Schema 了解 VO/DTO 字段名和类型
4. 在 src/api/xxx.ts 写 API 函数
5. 在 src/views/ 写页面组件
6. 浏览器验证 + Network 面板检查请求
```

## 铁律

1. **禁止直接 fetch/axios 裸调** → 必须使用 `src/api/index.ts` 的封装实例
2. **Token 存 localStorage**，每次请求自动带（拦截器处理）
3. **API 路径必须与后端完全一致** → 对照 Knife4j 写
4. **字段名必须与 VO/DTO 一致** → 看 Knife4j Schema
5. **分页参数用 `pageNum` 和 `pageSize`**，不要自己发明名字
6. **禁止在前端代码中硬编码后端地址、密码、密钥**
7. **组件库统一用 Element Plus**，不混用其他 UI 库
8. **不引入 JWT 解码库** → 后端负责验签，前端只传 Token
9. **不处理敏感数据加密** → 后端已处理 [脱敏] 字段
10. **新增 API 调用前必须在 Knife4j 上验证接口可用**

## 禁止

- ❌ 直接 `fetch()` 调用后端 → 用 `src/api/` 封装
- ❌ 硬编码 `http://localhost:8091` 散落各处 → 统一在 `src/api/index.ts`
- ❌ 不检查 HTTP 200/401 直接使用返回数据
- ❌ 修改后端代码或配置文件 → 前端只改 `gov-frontend/`
- ❌ 混用 Element Plus 和 Ant Design 等 → 统一 Element Plus
