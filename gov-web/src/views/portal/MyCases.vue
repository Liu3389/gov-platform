<template>
  <div class="page-container">
    <div class="tabs">
      <button v-for="t in tabs" :key="t.k" class="tab" :class="{ active: tab === t.k }" @click="tab = t.k">{{ t.l }}<span class="tc">{{ cnt(t.k) }}</span></button>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <span>加载办件记录中...</span>
    </div>

    <div v-else-if="filtered.length > 0" class="list">
      <div v-for="c in filtered" :key="c.id" class="case">
        <div class="case-main">
          <span class="case-icon" :style="{ background: iconBg(c.status) }">
            <svg v-if="c.status === 3" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
            <svg v-else-if="c.status === 4" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#dc2626" stroke-width="2.5"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>
            <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#1a56db" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
          </span>
          <div class="case-body">
            <div class="case-head">
              <h3>{{ c.itemName }}</h3>
              <span class="status-tag" :class="statusCls(c.status)">{{ statusTxt(c.status) }}</span>
            </div>
            <div class="case-meta">
              <span>{{ c.applyNo }}</span>
              <span class="ms">·</span>
              <span>{{ c.applyTime || c.createTime }}</span>
            </div>
            <div v-if="c.reply" class="reply">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
              <span>{{ c.reply }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="1">
        <rect x="3" y="5" width="18" height="14" rx="2"/>
        <line x1="3" y1="10" x2="21" y2="10"/>
        <line x1="8" y1="14" x2="16" y2="14"/>
      </svg>
      <p>暂无办件记录</p>
      <p class="empty-hint">前往<router-link to="/portal" style="color:#1a56db">办事大厅</router-link>选择服务提交申请</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getCaseList } from '@/api/case'

const tab = ref('all')
const tabs = [{ k: 'all', l: '全部' }, { k: '0', l: '待受理' }, { k: '2', l: '办理中' }, { k: '3', l: '已完成' }]
const loading = ref(true)
const cases = ref([])

const statusTxt = s => ({ 0: '待受理', 1: '已受理', 2: '办理中', 3: '已完成', 4: '已驳回' }[s] || '-')
const statusCls = s => (s === 3 ? 's-success' : s === 4 ? 's-danger' : s === 0 ? 's-warning' : 's-info')
const iconBg = s => ({ 3: '#ecfdf5', 4: '#fef2f2', 0: '#fffbeb' }[s] || '#eff6ff')
const cnt = k => k === 'all' ? cases.value.length : cases.value.filter(c => String(c.status) === k).length
const filtered = computed(() => tab.value === 'all' ? cases.value : cases.value.filter(c => String(c.status) === tab.value))

onMounted(async () => {
  loading.value = true
  try {
    const res = await getCaseList({ pageNum: 1, pageSize: 50 })
    cases.value = res.records || []
  } catch {
    cases.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>

.tabs { display: flex; gap: 0; margin-bottom: 28px; border-bottom: 1.5px solid rgba(0,0,0,0.04); }
.tab {
  font-size: 14px; font-weight: 450; color: #666;
  background: none; border: none; border-bottom: 2px solid transparent;
  padding: 10px 24px; cursor: pointer; font-family: inherit; transition: all 0.15s;
}
.tab:hover { color: #1a1a1a; }
.tab.active { color: #1a56db; border-bottom-color: #1a56db; font-weight: 500; }
.tc { font-size: 11px; opacity: 0.5; margin-left: 4px; }

.loading-state {
  text-align: center; padding: 80px 0;
  display: flex; flex-direction: column; align-items: center; gap: 16px;
  color: #bbb; font-size: 14px;
}
.loading-spinner {
  width: 32px; height: 32px; border: 3px solid rgba(0,0,0,0.06);
  border-top-color: #1a56db; border-radius: 50%;
  animation: spin 0.6s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

.list { display: flex; flex-direction: column; gap: 12px; }

.case {
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04); border-radius: 14px;
  transition: all 0.15s;
}
.case:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.05); }
.case-main { display: flex; gap: 16px; padding: 20px 24px; }
.case-icon {
  width: 40px; height: 40px; border-radius: 10px;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.case-body { flex: 1; min-width: 0; }
.case-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.case-head h3 { font-size: 15px; font-weight: 600; color: #1a1a1a; margin: 0; }
.case-meta { font-size: 13px; color: #999; display: flex; gap: 4px; }
.ms { color: #ddd; }

.reply {
  display: flex; gap: 8px; margin-top: 16px; padding: 12px 14px;
  background: #f0fdf4; border-radius: 10px;
  font-size: 14px; color: #065f46; line-height: 1.6; align-items: flex-start;
}
.reply svg { flex-shrink: 0; margin-top: 2px; }

.empty-state { text-align: center; padding: 80px 0; color: #999; }
.empty-state svg { margin-bottom: 16px; opacity: 0.4; }
.empty-state p { font-size: 15px; margin: 0 0 6px; }
.empty-hint { font-size: 13px !important; color: #bbb !important; }
</style>
