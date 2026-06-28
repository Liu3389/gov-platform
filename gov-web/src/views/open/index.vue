<template>
  <div class="page-container">
    <div class="tabs">
      <button v-for="t in tabs" :key="t.k" class="tab" :class="{ active: tab === t.k }" @click="switchTab(t.k)">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="15" height="15" v-html="t.icon"></svg>
        {{ t.l }}
      </button>
    </div>

    <!-- 通知公告 / 政策法规 -->
    <div v-if="tab !== 'apply'">
      <div v-if="loading" class="loading-state">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>

      <div v-else-if="currentList.length > 0" class="card-list">
        <div v-for="n in currentList" :key="n.id" class="card-item" @click="handleView(n)">
          <span class="card-dot" :style="{ background: tab === 'notice' ? '#1a56db' : '#d97706' }"></span>
          <div class="card-item-body">
            <h3>{{ n.title }}</h3>
            <p>{{ n.summary || n.content?.substring(0, 80) }}</p>
            <span class="card-item-date">{{ n.publishDate || n.createTime || n.date }}</span>
          </div>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg>
        </div>
      </div>

      <div v-else class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="1">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
          <polyline points="14 2 14 8 20 8"/>
          <line x1="16" y1="13" x2="8" y2="13"/>
          <line x1="16" y1="17" x2="8" y2="17"/>
        </svg>
        <p>暂无{{ tab === 'notice' ? '通知公告' : '政策法规' }}</p>
        <p class="empty-hint">最新内容将在发布后实时显示</p>
      </div>
    </div>

    <!-- 依申请公开 -->
    <div v-if="tab === 'apply'" class="apply-block">
      <div class="apply-intro">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#1a56db" stroke-width="1.8"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/></svg>
        <span>根据《中华人民共和国政府信息公开条例》，公民、法人或其他组织可向行政机关申请获取相关政府信息。</span>
      </div>
      <el-form :model="fm" label-width="80px" style="max-width:540px;margin-top:28px">
        <el-form-item label="申请人">
          <el-input v-model="fm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="fm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="申请内容">
          <el-input v-model="fm.content" type="textarea" :rows="4" placeholder="请描述您需要获取的信息内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleApply">提交申请</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getNoticeList, getPolicyList, submitOpenApply } from '@/api/open'

const tab = ref('notice')
const loading = ref(false)
const notices = ref([])
const policies = ref([])

const currentList = computed(() => tab.value === 'notice' ? notices.value : policies.value)

const tabs = [
  { k: 'notice', l: '通知公告', icon: '<circle cx="12" cy="12" r="10"/><path d="M12 16v-4M12 8h.01"/>' },
  { k: 'policy', l: '政策法规', icon: '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/>' },
  { k: 'apply', l: '依申请公开', icon: '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/>' }
]

const fm = reactive({ name: '', idCard: '', content: '' })
const submitting = ref(false)

function handleView(item) { ElMessage.info('查看详情：' + item.title) }

async function handleApply() {
  if (!fm.name || !fm.content) { ElMessage.warning('请填写必填项'); return }
  submitting.value = true
  try {
    await submitOpenApply({ applicantName: fm.name, idCard: fm.idCard, content: fm.content })
    ElMessage.success('申请已提交，我们将在3个工作日内回复')
    fm.name = ''; fm.idCard = ''; fm.content = ''
  } catch {
    // handled
  } finally {
    submitting.value = false
  }
}

function switchTab(k) {
  tab.value = k
  if (k === 'notice' && notices.value.length === 0) fetchNotices()
  if (k === 'policy' && policies.value.length === 0) fetchPolicies()
}

async function fetchNotices() {
  loading.value = true
  try {
    const res = await getNoticeList({ pageNum: 1, pageSize: 20 })
    notices.value = res.records || []
  } catch { notices.value = [] }
  finally { loading.value = false }
}

async function fetchPolicies() {
  loading.value = true
  try {
    const res = await getPolicyList({ pageNum: 1, pageSize: 20 })
    policies.value = res.records || []
  } catch { policies.value = [] }
  finally { loading.value = false }
}

onMounted(() => { fetchNotices() })
</script>

<style scoped>

.tabs {
  display: flex; gap: 2px; margin-bottom: 28px;
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04);
  border-radius: 14px; padding: 4px;
}
.tab {
  display: flex; align-items: center; gap: 7px; flex: 1; justify-content: center;
  font-size: 14px; font-weight: 450; color: #666;
  background: none; border: none; padding: 10px 16px; border-radius: 10px;
  cursor: pointer; font-family: inherit; transition: all 0.15s;
}
.tab:hover { color: #1a1a1a; }
.tab.active { background: #1a1a1a; color: #fff; font-weight: 500; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }

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

.card-list { display: flex; flex-direction: column; gap: 2px; }
.card-item {
  display: flex; align-items: flex-start; gap: 16px;
  padding: 20px; cursor: pointer; transition: all 0.15s;
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04); border-radius: 14px;
}
.card-item:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.05); }
.card-dot { width: 8px; height: 8px; border-radius: 50%; margin-top: 8px; flex-shrink: 0; }
.card-item-body { flex: 1; min-width: 0; }
.card-item-body h3 { font-size: 15px; font-weight: 600; color: #1a1a1a; margin: 0 0 4px; line-height: 1.4; }
.card-item-body p { font-size: 14px; color: #666; margin: 0 0 8px; line-height: 1.5; }
.card-item-date { font-size: 12px; color: #bbb; }
.card-item > svg { flex-shrink: 0; margin-top: 6px; }

.apply-block { background: #fff; border: 1.5px solid rgba(0,0,0,0.04); border-radius: 16px; padding: 28px 32px; }
.apply-intro { display: flex; gap: 12px; align-items: flex-start; font-size: 14px; color: #666; line-height: 1.7; }
.apply-intro svg { flex-shrink: 0; margin-top: 2px; }

.empty-state { text-align: center; padding: 80px 0; color: #999; }
.empty-state svg { margin-bottom: 16px; opacity: 0.4; }
.empty-state p { font-size: 15px; margin: 0 0 6px; }
.empty-hint { font-size: 13px !important; color: #bbb !important; }
</style>
