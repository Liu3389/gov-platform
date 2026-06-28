<template>
  <div class="admin-layout">
    <!-- 侧边栏 — fixed -->
    <aside class="sidebar">
      <div class="sb-brand" @click="$router.push('/dashboard')">
        <svg width="22" height="22" viewBox="0 0 36 36" fill="none">
          <rect width="36" height="36" rx="9" fill="#1a56db"/>
          <path d="M8 18l6 6 14-12" stroke="#fff" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        <span>智慧政务</span>
      </div>
      <nav class="sb-nav">
        <div v-for="group in menus" :key="group.label" class="sb-group">
          <div class="sb-group-label">{{ group.label }}</div>
          <router-link
            v-for="item in group.children"
            :key="item.path"
            :to="item.path"
            class="sb-item"
            active-class="sb-item-active"
          >
            <el-icon :size="16"><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </router-link>
        </div>
      </nav>
      <div class="sb-footer">
        <div class="sb-user">
          <span class="sb-avatar">{{ (userStore.realName || 'A').charAt(0) }}</span>
          <div class="sb-user-info">
            <span class="sb-name">{{ userStore.realName }}</span>
            <span class="sb-role">管理员</span>
          </div>
        </div>
      </div>
    </aside>

    <!-- 主区域 -->
    <div class="main">
      <!-- 顶栏 — fixed，页标题唯一来源 -->
      <header class="topbar">
        <div class="topbar-left">
          <template v-for="(c, i) in crumbs" :key="i">
            <span v-if="i > 0" class="crumb-sep">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#bbb" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg>
            </span>
            <span
              :class="['crumb', i < crumbs.length - 1 ? 'crumb-link' : 'crumb-current']"
              @click="i < crumbs.length - 1 && c.path && $router.push(c.path)"
            >{{ c.title }}</span>
          </template>
        </div>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </header>

      <div class="content">
        <router-view v-slot="{ Component }">
          <transition name="page-zoom" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>

    <LogoutDialog ref="logoutDlgRef" />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  HomeFilled, List, FolderOpened, Tickets,
  Setting, User, UserFilled, SwitchButton,
  Postcard, ChatDotSquare, Reading, DataBoard
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import LogoutDialog from '@/components/LogoutDialog.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const logoutDlgRef = ref(null)

const menus = [
  {
    label: '工作',
    children: [
      { label: '工作台', icon: HomeFilled, path: '/dashboard' },
      { label: '事项管理', icon: List, path: '/item/list' },
      { label: '办件中心', icon: FolderOpened, path: '/case/todo' }
    ]
  },
  {
    label: '业务',
    children: [
      { label: '窗口受理', icon: Tickets, path: '/case/reception' },
      { label: '证照管理', icon: Postcard, path: '/license' },
      { label: '政务公开', icon: Reading, path: '/open' },
      { label: '投诉建议', icon: ChatDotSquare, path: '/complaint' }
    ]
  },
  {
    label: '系统',
    children: [
      { label: '用户管理', icon: User, path: '/system/user' },
      { label: '角色管理', icon: UserFilled, path: '/system/role' },
      { label: '部门管理', icon: DataBoard, path: '/system/dept' }
    ]
  }
]

// 面包屑：显示全路径，当前页为主要标题
const crumbs = computed(() =>
  route.matched.filter(m => m.meta?.title).map(m => ({
    title: m.meta.title,
    path: m.meta.breadcrumbPath
  }))
)

async function handleLogout() {
  const ok = await logoutDlgRef.value.show()
  if (ok) { userStore.logout(); router.push('/login') }
}
</script>

<style scoped>
.admin-layout {
  font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  color: #1a1a1a;
  -webkit-font-smoothing: antialiased;
}

/* ====== 侧边栏 ====== */
.sidebar {
  position: fixed; left: 0; top: 0; bottom: 0;
  width: 220px;
  background: #1a1a1e;
  display: flex; flex-direction: column;
  z-index: 200;
}
.sb-brand {
  display: flex; align-items: center; gap: 10px;
  padding: 24px 20px; cursor: pointer; flex-shrink: 0;
}
.sb-brand span { font-size: 16px; font-weight: 600; color: #fff; letter-spacing: -0.01em; }
.sb-nav { flex: 1; padding: 0 10px; overflow-y: auto; }
.sb-group { margin-bottom: 2px; }
.sb-group-label {
  font-size: 10px; font-weight: 600; color: #5a5a5e;
  text-transform: uppercase; letter-spacing: 0.12em; padding: 20px 10px 8px;
}
.sb-item {
  display: flex; align-items: center; gap: 10px;
  padding: 9px 12px; margin: 1px 0; border-radius: 8px;
  font-size: 13px; font-weight: 450; color: #999;
  text-decoration: none; transition: all 0.12s ease;
}
.sb-item:hover { background: rgba(255,255,255,0.06); color: #ddd; }
.sb-item-active { background: rgba(255,255,255,0.12); color: #fff; font-weight: 500; }
.sb-footer { padding: 16px 20px; flex-shrink: 0; border-top: 1px solid #2a2a2e; }
.sb-user { display: flex; align-items: center; gap: 10px; }
.sb-avatar {
  width: 32px; height: 32px; border-radius: 50%;
  background: #1a56db; color: #fff; font-size: 13px;
  font-weight: 600; display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.sb-user-info { line-height: 1.3; }
.sb-name { font-size: 13px; color: #ccc; display: block; }
.sb-role { font-size: 11px; color: #666; }

/* ====== 主区域 ====== */
.main { margin-left: 220px; min-height: 100vh; background: #f5f5f7; }

/* ====== 顶栏 ====== */
.topbar {
  position: fixed; top: 0; left: 220px; right: 0; z-index: 199;
  height: 48px; display: flex; align-items: center; justify-content: space-between;
  padding: 0 32px;
  background: rgba(255,255,255,0.55);
  backdrop-filter: blur(18px) saturate(160%);
  -webkit-backdrop-filter: blur(18px) saturate(160%);
  border-bottom: 1px solid rgba(0,0,0,0.04);
}
.topbar-left { display: flex; align-items: center; gap: 2px; }
.crumb { font-size: 13px; color: #999; }
.crumb-link { cursor: pointer; }
.crumb-link:hover { color: #1a56db; }
.crumb-current { color: #1a1a1a; font-weight: 600; }
.crumb-sep { flex-shrink: 0; display: flex; align-items: center; padding: 0 6px; }
.logout-btn {
  font-size: 12px; font-weight: 500; color: #999; background: none; border: none;
  cursor: pointer; padding: 5px 12px; border-radius: 8px;
  font-family: inherit; transition: all 0.12s;
}
.logout-btn:hover { color: #dc2626; background: rgba(220,38,38,0.06); }

/* ====== 内容 ====== */
.content { padding: 68px 32px 32px; }

/* ====== 页面切换动画 ====== */
.page-zoom-enter-active { transition: all 0.25s cubic-bezier(0.2, 0, 0.1, 1); }
.page-zoom-leave-active { transition: all 0.15s cubic-bezier(0.2, 0, 0.1, 1); }
.page-zoom-enter-from { opacity: 0; transform: scale(0.985) translateY(3px); }
.page-zoom-leave-to { opacity: 0; transform: scale(0.985) translateY(-3px); }
</style>
