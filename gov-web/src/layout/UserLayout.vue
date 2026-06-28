<template>
  <div class="user-layout">
    <!-- 导航 — fixed -->
    <header class="header">
      <div class="header-inner">
        <div class="header-brand" @click="$router.push('/portal')">
          <svg width="22" height="22" viewBox="0 0 36 36" fill="none">
            <rect width="36" height="36" rx="9" fill="#1a56db"/>
            <path d="M8 18l6 6 14-12" stroke="#fff" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>智慧政务</span>
        </div>
        <nav class="header-nav">
          <router-link v-for="nav in navItems" :key="nav.path" :to="nav.path" class="nav-link" active-class="nav-link-active">
            {{ nav.label }}
          </router-link>
        </nav>
        <div class="header-right">
          <span class="greeting">{{ userStore.realName || userStore.username }}</span>
          <button class="logout" @click="handleLogout">退出</button>
        </div>
      </div>
    </header>

    <main class="main">
      <router-view v-slot="{ Component }">
        <transition name="page-zoom" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <LogoutDialog ref="logoutDlgRef" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import LogoutDialog from '@/components/LogoutDialog.vue'

const router = useRouter()
const userStore = useUserStore()
const logoutDlgRef = ref(null)

const navItems = [
  { label: '办事大厅', path: '/portal' },
  { label: '我的办件', path: '/my/cases' },
  { label: '我的证照', path: '/my/licenses' },
  { label: '政务公开', path: '/open' },
  { label: '投诉建议', path: '/complaint' }
]

async function handleLogout() {
  const ok = await logoutDlgRef.value.show()
  if (ok) { userStore.logout(); router.push('/login') }
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  display: flex; flex-direction: column;
  background:
    radial-gradient(ellipse 70% 50% at 50% 30%, #e8ecf4 0%, transparent 50%),
    radial-gradient(ellipse 50% 70% at 20% 80%, #f0eefa 0%, transparent 50%),
    #f5f5f7;
  font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  color: #1a1a1a;
  -webkit-font-smoothing: antialiased;
}

/* ====== 导航 — fixed ====== */
.header {
  position: fixed; top: 0; left: 0; right: 0; z-index: 199;
  background: rgba(255,255,255,0.55);
  backdrop-filter: blur(22px) saturate(160%);
  -webkit-backdrop-filter: blur(22px) saturate(160%);
  border-bottom: 1px solid rgba(0,0,0,0.04);
}
.header-inner {
  max-width: 1200px; margin: 0 auto;
  display: flex; align-items: center; height: 56px; padding: 0 32px;
}
.header-brand {
  display: flex; align-items: center; gap: 10px;
  font-size: 16px; font-weight: 600; color: #1a1a1a;
  cursor: pointer; margin-right: 48px; letter-spacing: -0.01em;
}
.header-nav { display: flex; align-items: center; gap: 4px; flex: 1; }
.nav-link {
  font-size: 14px; color: #666; text-decoration: none;
  padding: 6px 16px; border-radius: 8px; transition: all 0.15s;
  font-weight: 450;
}
.nav-link:hover { background: rgba(0,0,0,0.04); color: #1a1a1a; }
.nav-link-active { color: #1a56db; font-weight: 500; background: rgba(26,86,219,0.06); }

.header-right { display: flex; align-items: center; gap: 16px; }
.greeting { font-size: 13px; color: #999; }
.logout {
  font-size: 12px; font-weight: 500; color: #999; background: none; border: none;
  cursor: pointer; padding: 5px 12px; border-radius: 8px;
  font-family: inherit; transition: all 0.12s;
}
.logout:hover { color: #dc2626; background: rgba(220,38,38,0.06); }

/* ====== 内容 ====== */
.main { flex: 1; padding: 104px 32px 80px; }

/* ====== 页面切换动画 ====== */
.page-zoom-enter-active { transition: all 0.25s cubic-bezier(0.2, 0, 0.1, 1); }
.page-zoom-leave-active { transition: all 0.15s cubic-bezier(0.2, 0, 0.1, 1); }
.page-zoom-enter-from { opacity: 0; transform: scale(0.985) translateY(3px); }
.page-zoom-leave-to { opacity: 0; transform: scale(0.985) translateY(-3px); }
</style>
