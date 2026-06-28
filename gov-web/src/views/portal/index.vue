<template>
  <div class="portal page-container">
    <!-- Hero -->
    <section class="hero">
      <div class="hero-badge">
        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
        智慧政务服务
      </div>
      <h1>在线办事大厅</h1>
      <p>一站办理 · 一次不跑 · 一键查询</p>
      <div class="search-wrap">
        <svg class="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#aaa" stroke-width="2" stroke-linecap="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
        <input v-model="keyword" class="search-input" placeholder="搜索您想办理的事项..." />
      </div>
    </section>

    <!-- 分类 -->
    <section class="cats">
      <button v-for="c in categories" :key="c.key" class="cat-btn" :class="{ active: activeCat === c.key }" @click="activeCat = c.key">
        <span class="cat-icon" v-html="c.icon"></span>
        <span>{{ c.label }}</span>
      </button>
    </section>

    <!-- 服务网格 -->
    <section class="services-section">
      <div class="services-header">
        <span v-if="!loading" class="count-label">{{ activeCat ? activeCat + ' · ' : '' }}{{ filtered.length }} 项可办服务</span>
      </div>

      <div v-if="loading" class="loading-state">
        <div class="loading-spinner"></div>
        <span>加载服务列表中...</span>
      </div>

      <div v-else-if="filtered.length > 0" class="services-grid">
        <div v-for="it in filtered" :key="it.id" class="service-card" @click="handleApply(it)">
          <div class="card-icon" :style="{ background: cardColor(it.itemType) + '14', color: cardColor(it.itemType) }">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round">
              <rect x="3" y="3" width="18" height="18" rx="2"/>
              <path d="M9 12l2 2 4-4"/>
            </svg>
          </div>
          <h3>{{ it.itemName }}</h3>
          <span class="card-dept">{{ it.deptName }}</span>
          <div class="card-tags">
            <span class="card-tag" :style="{ background: cardColor(it.itemType) + '12', color: cardColor(it.itemType) }">在线办理</span>
            <span class="card-tag card-tag-green">最多跑一次</span>
          </div>
          <span class="card-time">{{ it.timeLimit || '请咨询' }}</span>
        </div>
      </div>

      <div v-else class="empty-state">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="1">
          <rect x="3" y="3" width="18" height="18" rx="2"/>
          <path d="M9 9h6M9 13h6M9 17h4"/>
        </svg>
        <p>未找到匹配的服务事项</p>
        <p class="empty-hint">{{ keyword ? '请尝试其他关键词' : '请选择上方的分类筛选' }}</p>
        <button v-if="keyword || activeCat" class="link-btn" @click="resetFilter">清除筛选</button>
      </div>
    </section>

    <!-- 申请弹窗 -->
    <el-dialog v-model="dlg" title="提交申请" width="480px">
      <div v-if="currentItem" class="apply-header">
        <span class="apply-icon" :style="{ background: cardColor(currentItem.itemType) + '14', color: cardColor(currentItem.itemType) }">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <rect x="3" y="3" width="18" height="18" rx="2"/><path d="M9 12l2 2 4-4"/>
          </svg>
        </span>
        <strong>{{ currentItem.itemName }}</strong>
        <span class="apply-dept">{{ currentItem.deptName }} · {{ currentItem.timeLimit || '请咨询' }}</span>
      </div>
      <el-form ref="fRef" :model="fm" :rules="frules" label-width="64px" style="margin-top:24px">
        <el-form-item label="申请人" prop="name">
          <el-input v-model="fm.name" placeholder="请填写您的姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="fm.phone" placeholder="请填写联系电话" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="fm.remark" type="textarea" :rows="3" placeholder="补充说明（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dlg = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="doApply">确认提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getItemList } from '@/api/item'
import { submitApplication } from '@/api/case'

const keyword = ref('')
const activeCat = ref('')
const loading = ref(true)
const items = ref([])

const typeColors = { 1: '#1a56db', 2: '#059669', 3: '#d97706' }
function cardColor(itemType) {
  return typeColors[itemType] || '#1a56db'
}

const categories = [
  { key: '', label: '全部', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></svg>' },
  { key: '户籍', label: '户籍', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>' },
  { key: '社保', label: '社保', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>' },
  { key: '住房', label: '住房', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg>' },
  { key: '证照', label: '证照', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="7" width="20" height="10" rx="2"/><line x1="6" y1="12" x2="18" y2="12"/><line x1="9" y1="16" x2="15" y2="16"/></svg>' },
  { key: '综合', label: '综合', icon: '<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/></svg>' }
]

// itemType 映射：数字类型 → 分类 key
const typeToCat = { 1: '户籍', 2: '社保', 3: '住房', 4: '证照', 5: '综合' }

const filtered = computed(() => {
  let list = items.value
  if (activeCat.value) {
    // 筛选中文字符匹配
    list = list.filter(i => {
      const cat = typeToCat[i.itemType] || ''
      return cat === activeCat.value
    })
  }
  if (keyword.value.trim()) {
    const kw = keyword.value.trim().toLowerCase()
    list = list.filter(i =>
      (i.itemName || '').toLowerCase().includes(kw) ||
      (i.deptName || '').toLowerCase().includes(kw)
    )
  }
  return list
})

const dlg = ref(false); const currentItem = ref(null); const submitting = ref(false)
const fRef = ref(null); const fm = reactive({ name: '', phone: '', remark: '' })
const frules = {
  name: [{ required: true, message: '请填写申请人姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请填写联系电话', trigger: 'blur' }]
}

function handleApply(item) { currentItem.value = item; fm.name = ''; fm.phone = ''; fm.remark = ''; dlg.value = true }
function resetFilter() { keyword.value = ''; activeCat.value = '' }

async function doApply() {
  if (!fRef.value) return
  await fRef.value.validate(async (ok) => {
    if (!ok) return
    submitting.value = true
    try {
      await submitApplication({ itemId: currentItem.value.id, itemName: currentItem.value.itemName, applicantName: fm.name, phone: fm.phone, remark: fm.remark })
      ElMessage.success('申请已提交')
      dlg.value = false
    } catch {} finally { submitting.value = false }
  })
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getItemList({ pageNum: 1, pageSize: 100 })
    items.value = res.records || []
  } catch {
    items.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>

/* ====== Hero ====== */
.hero { text-align: center; margin-bottom: 52px; }
.hero-badge {
  display: inline-flex; align-items: center; gap: 6px;
  font-size: 12px; font-weight: 600; color: #1a56db;
  background: #eff6ff; padding: 5px 16px; border-radius: 20px;
  margin-bottom: 20px; letter-spacing: 0.03em;
}
.hero h1 {
  font-size: 36px; font-weight: 700; color: #1a1a1a;
  letter-spacing: -0.03em; margin: 0 0 10px; line-height: 1.2;
}
.hero p { font-size: 16px; color: #777; margin: 0 0 32px; }

.search-wrap { position: relative; max-width: 520px; margin: 0 auto; }
.search-icon { position: absolute; left: 18px; top: 50%; transform: translateY(-50%); pointer-events: none; }
.search-input {
  width: 100%; height: 50px; padding: 0 24px 0 48px; font-size: 15px;
  font-family: inherit; color: #1a1a1a; background: #fff;
  border: 1.5px solid rgba(0,0,0,0.06); border-radius: 14px;
  outline: none; transition: all 0.2s;
}
.search-input::placeholder { color: #bbb; font-size: 14px; }
.search-input:focus { border-color: #1a56db; box-shadow: 0 0 0 4px rgba(26,86,219,0.08); }

/* ====== 分类 ====== */
.cats { display: flex; justify-content: center; gap: 8px; margin-bottom: 40px; flex-wrap: wrap; }
.cat-btn {
  display: flex; align-items: center; gap: 7px;
  padding: 9px 20px; font-size: 14px; font-weight: 450; color: #555;
  background: #fff; border: 1.5px solid rgba(0,0,0,0.06); border-radius: 12px;
  cursor: pointer; transition: all 0.18s; font-family: inherit;
}
.cat-btn:hover { border-color: rgba(0,0,0,0.15); background: #fafafa; }
.cat-btn.active { background: #1a56db; border-color: #1a56db; color: #fff; font-weight: 500; }
.cat-icon { display: flex; align-items: center; }

/* ====== 服务网格 ====== */
.services-section { margin-bottom: 40px; }
.services-header { margin-bottom: 16px; }
.count-label { font-size: 14px; font-weight: 500; color: #999; }

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

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 14px;
}

.service-card {
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04); border-radius: 14px;
  padding: 22px 20px; cursor: pointer; transition: all 0.2s ease;
}
.service-card:hover {
  border-color: rgba(0,0,0,0.1); box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transform: translateY(-2px);
}
.card-icon {
  width: 44px; height: 44px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center; margin-bottom: 14px;
}
.service-card h3 { font-size: 15px; font-weight: 600; color: #1a1a1a; margin: 0 0 4px; }
.card-dept { font-size: 12px; color: #999; display: block; margin-bottom: 12px; }
.card-tags { display: flex; gap: 6px; margin-bottom: 8px; }
.card-tag { font-size: 11px; font-weight: 500; padding: 2px 8px; border-radius: 5px; }
.card-tag-green { background: #ecfdf5; color: #059669; }
.card-time { font-size: 12px; color: #bbb; }

/* ====== 弹窗 ====== */
.apply-header { text-align: center; }
.apply-icon {
  width: 48px; height: 48px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center; margin: 0 auto 12px;
}
.apply-header strong { font-size: 18px; font-weight: 700; color: #1a1a1a; display: block; }
.apply-dept { font-size: 13px; color: #888; margin-top: 4px; display: block; }

.empty-state { text-align: center; padding: 80px 0; color: #999; }
.empty-state svg { margin-bottom: 16px; opacity: 0.4; }
.empty-state p { font-size: 15px; margin: 0 0 6px; }
.empty-hint { font-size: 13px !important; color: #bbb !important; }
</style>
