<template>
  <div class="page-container">
    <!-- 概览数字 -->
    <div class="stats-row">
      <div v-for="s in stats" :key="s.key" class="stat-item" @click="s.link && $router.push(s.link)">
        <div class="stat-number">{{ s.loading ? '—' : s.value }}</div>
        <div class="stat-label">{{ s.label }}</div>
        <div v-if="s.change" class="stat-change" :class="s.changeDown ? 'down' : 'up'">{{ s.change }}</div>
      </div>
    </div>

    <!-- 待办 + 快捷入口 -->
    <div class="grid-2col">
      <div class="block">
        <div class="block-header">
          <h3>待办审批</h3>
          <a class="view-all" @click="$router.push('/case/todo')">查看全部 →</a>
        </div>
        <div class="block-card">
          <div v-if="todoLoading" class="block-loading">加载中...</div>
          <div v-else-if="todos.length > 0" class="todo-list">
            <div v-for="t in todos" :key="t.id" class="todo-row" @click="$router.push('/case/todo')">
              <div class="todo-left">
                <span class="todo-icon">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#1a56db" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                </span>
                <div class="todo-info">
                  <span class="todo-applicant">{{ t.applicantName || t.applicant }}</span>
                  <span class="todo-sep">·</span>
                  <span class="todo-item">{{ t.itemName }}</span>
                </div>
              </div>
              <span class="todo-time">{{ formatTime(t.createTime || t.time) }}</span>
            </div>
          </div>
          <div v-else class="block-empty">暂无待办事项</div>
        </div>
      </div>

      <div class="block">
        <div class="block-header">
          <h3>快捷入口</h3>
        </div>
        <div class="block-card">
          <a v-for="q in quickLinks" :key="q.path" class="quick-link" @click="$router.push(q.path)">
            <span>{{ q.label }}</span>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="2" stroke-linecap="round"><polyline points="9 18 15 12 9 6"/></svg>
          </a>
        </div>
      </div>
    </div>

    <!-- 最近动态 -->
    <div class="block">
      <div class="block-header">
        <h3>最近动态</h3>
      </div>
      <div class="block-card timeline-card">
        <div v-if="activities.length > 0" class="timeline">
          <div v-for="(a, i) in activities" :key="i" class="tl-item">
            <span class="tl-dot" :style="{ background: a.color }"></span>
            <div class="tl-body">
              <p>{{ a.content }}</p>
              <span class="tl-time">{{ a.time }}</span>
            </div>
          </div>
        </div>
        <div v-else class="block-empty">暂无动态</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getTodoList } from '@/api/case'
import { getItemList } from '@/api/item'
import { getLicenseList } from '@/api/license'
import { getComplaintList } from '@/api/complaint'

const router = useRouter()
const userStore = useUserStore()

const dateStr = computed(() => {
  const d = new Date()
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
})

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 9) return '早上好'
  if (h < 12) return '上午好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

function formatTime(v) {
  if (!v) return ''
  if (typeof v === 'string' && v.includes('前')) return v
  return v
}

// 概览数据
const stats = ref([
  { key: 'items', label: '事项总数', value: '—', change: '', link: '/item/list', loading: true },
  { key: 'todo', label: '待办任务', value: '—', change: '', link: '/case/todo', loading: true },
  { key: 'licenses', label: '证照发放', value: '—', change: '', link: '/license', loading: true },
  { key: 'complaints', label: '投诉工单', value: '—', change: '', link: '/complaint', loading: true }
])

// 待办列表
const todos = ref([])
const todoLoading = ref(true)

// 动态
const activities = ref([])

const quickLinks = [
  { label: '事项管理', path: '/item/list' },
  { label: '用户管理', path: '/system/user' },
  { label: '角色管理', path: '/system/role' },
  { label: '部门管理', path: '/system/dept' }
]

async function loadStats() {
  // 并行拉取各项总数
  const tasks = [
    getItemList({ pageNum: 1, pageSize: 1 }).then(r => { stats.value[0].value = String(r.total || 0); stats.value[0].loading = false }).catch(() => { stats.value[0].loading = false }),
    getTodoList({ pageNum: 1, pageSize: 1 }).then(r => { stats.value[1].value = String(r.total || 0); stats.value[1].loading = false }).catch(() => { stats.value[1].loading = false }),
    getLicenseList({ pageNum: 1, pageSize: 1 }).then(r => { stats.value[2].value = String(r.total || 0); stats.value[2].loading = false }).catch(() => { stats.value[2].loading = false }),
    getComplaintList({ pageNum: 1, pageSize: 1 }).then(r => { stats.value[3].value = String(r.total || 0); stats.value[3].loading = false }).catch(() => { stats.value[3].loading = false })
  ]
  await Promise.allSettled(tasks)
}

async function loadTodos() {
  todoLoading.value = true
  try {
    const res = await getTodoList({ pageNum: 1, pageSize: 5 })
    todos.value = (res.records || []).slice(0, 5)
  } catch {
    todos.value = []
  } finally {
    todoLoading.value = false
  }
}

function loadActivities() {
  // 动态时间线：基于时间生成
  const now = new Date()
  const items = [
    { content: '系统运行正常，各项服务稳定', time: '刚刚', color: '#059669' },
    { content: '今日共有 ' + stats.value[1].value + ' 个待办任务需要处理', time: '自动刷新', color: '#1a56db' },
    { content: '共有 ' + stats.value[0].value + ' 个政务服务事项在线', time: '自动刷新', color: '#1a56db' }
  ]
  activities.value = items
}

onMounted(async () => {
  await loadStats()
  await loadTodos()
  loadActivities()
})
</script>

<style scoped>
/* ====== 概览数字 ====== */
.stats-row {
  display: flex; gap: 48px; margin-bottom: 32px; padding: 0;
}
.stat-item {
  cursor: pointer; transition: opacity 0.15s;
}
.stat-item:hover { opacity: 0.75; }
.stat-number {
  font-size: 36px; font-weight: 700; color: #1a1a1a;
  letter-spacing: -0.03em; line-height: 1.1;
}
.stat-label {
  font-size: 13px; color: #555; margin-top: 2px;
}
.stat-change {
  font-size: 12px; color: #059669; margin-top: 4px; font-weight: 500;
}
.stat-change.down { color: #dc2626; }

/* ====== 双栏 ====== */
.grid-2col {
  display: grid; grid-template-columns: 1fr 1fr; gap: 32px; margin-bottom: 32px;
}

/* ====== 区块 ====== */
.block { margin-bottom: 32px; }
.block-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 12px;
}
.block-header h3 {
  font-size: 17px; font-weight: 600; color: #1a1a1a; margin: 0;
}
.view-all {
  font-size: 13px; color: #1a56db; cursor: pointer; text-decoration: none;
}
.view-all:hover { text-decoration: underline; }

.block-card {
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04);
  border-radius: 14px; overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.02);
}
.block-empty {
  text-align: center; padding: 40px 0; color: #bbb; font-size: 14px;
}
.block-loading {
  text-align: center; padding: 40px 0; color: #bbb; font-size: 14px;
}

/* ====== 待办列表 ====== */
.todo-list {}
.todo-row {
  display: flex; align-items: center; justify-content: space-between;
  padding: 12px 18px; cursor: pointer;
  border-bottom: 1px solid rgba(0,0,0,0.03);
  transition: background 0.12s;
}
.todo-row:last-child { border-bottom: none; }
.todo-row:hover { background: rgba(0,0,0,0.015); }
.todo-left { display: flex; align-items: center; gap: 10px; }
.todo-icon {
  width: 28px; height: 28px; border-radius: 8px;
  background: #eff6ff; display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.todo-info { font-size: 14px; color: #1a1a1a; display: flex; align-items: center; gap: 4px; }
.todo-applicant { font-weight: 500; }
.todo-sep { color: #ccc; }
.todo-item { color: #555; }
.todo-time { font-size: 12px; color: #bbb; }

/* ====== 快捷入口 ====== */
.quick-link {
  display: flex; align-items: center; justify-content: space-between;
  padding: 13px 18px; font-size: 14px; color: #1a1a1a;
  cursor: pointer; text-decoration: none;
  border-bottom: 1px solid rgba(0,0,0,0.03);
  transition: all 0.12s;
}
.quick-link:last-child { border-bottom: none; }
.quick-link:hover { color: #1a56db; background: rgba(26,86,219,0.03); }

/* ====== 动态时间线 ====== */
.timeline-card { padding: 16px 0; }
.timeline { padding-left: 24px; border-left: 2px solid #f0f0f0; margin-left: 18px; }
.tl-item { display: flex; gap: 12px; padding: 6px 0; position: relative; }
.tl-dot {
  width: 8px; height: 8px; border-radius: 50%; margin-top: 6px;
  flex-shrink: 0; margin-left: -28px;
}
.tl-body { min-width: 0; }
.tl-body p { font-size: 14px; color: #1a1a1a; margin: 0; line-height: 1.5; }
.tl-time { font-size: 12px; color: #999; display: block; margin-top: 2px; }
</style>
