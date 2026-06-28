<template>
  <div class="page-container">
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <span>加载证照信息中...</span>
    </div>

    <div v-else-if="licenses.length > 0" class="grid">
      <div v-for="l in licenses" :key="l.id" class="card" @click="handleView(l)">
        <div class="card-icon" :class="l.status === 1 ? 'valid' : 'expired'">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="5" width="18" height="14" rx="2"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
        </div>
        <div class="card-body">
          <h3>{{ l.catalogName }}</h3>
          <p class="card-no">{{ l.licenseNo }}</p>
          <p class="card-date">有效期至 {{ l.validUntil }}</p>
        </div>
        <span class="card-badge" :class="l.status === 1 ? 'badge-valid' : 'badge-expired'">{{ l.status === 1 ? '有效' : '已过期' }}</span>
      </div>
    </div>

    <div v-else class="empty-state">
      <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#ddd" stroke-width="1">
        <rect x="3" y="5" width="18" height="14" rx="2"/>
        <line x1="3" y1="10" x2="21" y2="10"/>
      </svg>
      <p>暂无电子证照</p>
      <p class="empty-hint">办理完成后证照将自动同步至此处</p>
    </div>

    <el-dialog v-model="dlg" title="证照详情" width="480px">
      <div v-if="detail" class="detail-top">
        <div class="detail-icon" :class="detail.status === 1 ? 'valid' : 'expired'">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="5" width="18" height="14" rx="2"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
        </div>
        <span class="detail-name">{{ detail.catalogName }}</span>
        <span class="detail-no">{{ detail.licenseNo }}</span>
      </div>
      <el-descriptions v-if="detail" :column="2" border size="small" style="margin-top:20px">
        <el-descriptions-item label="持证人">{{ detail.holderName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <span :style="{ color: detail.status === 1 ? '#059669' : '#dc2626', fontWeight: 600 }">{{ detail.status === 1 ? '有效' : '已过期' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="签发日期">{{ detail.issueDate }}</el-descriptions-item>
        <el-descriptions-item label="有效期至">{{ detail.validUntil }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="dlg = false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getLicenseList } from '@/api/license'

const loading = ref(true)
const licenses = ref([])

const dlg = ref(false); const detail = ref(null)
function handleView(l) { detail.value = l; dlg.value = true }

onMounted(async () => {
  loading.value = true
  try {
    const res = await getLicenseList({ pageNum: 1, pageSize: 50 })
    licenses.value = res.records || []
  } catch {
    licenses.value = []
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>

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

.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 16px; }

.card {
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04); border-radius: 14px;
  padding: 24px; cursor: pointer; transition: all 0.2s; position: relative;
}
.card:hover { box-shadow: 0 2px 14px rgba(0,0,0,0.06); transform: translateY(-2px); }
.card-icon { width: 44px; height: 44px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-bottom: 16px; }
.card-icon.valid { background: #eff6ff; color: #1a56db; }
.card-icon.expired { background: #fef2f2; color: #dc2626; }
.card-body h3 { font-size: 15px; font-weight: 600; color: #1a1a1a; margin: 0 0 4px; }
.card-no { font-size: 12px; color: #999; margin: 0 0 6px; word-break: break-all; }
.card-date { font-size: 12px; color: #bbb; margin: 0; }
.card-badge { position: absolute; top: 16px; right: 16px; font-size: 11px; font-weight: 600; padding: 3px 10px; border-radius: 6px; }
.badge-valid { background: #ecfdf5; color: #059669; }
.badge-expired { background: #fef2f2; color: #dc2626; }

.detail-top { text-align: center; padding: 20px 0; }
.detail-icon { width: 52px; height: 52px; border-radius: 14px; display: flex; align-items: center; justify-content: center; margin: 0 auto 14px; }
.detail-icon.valid { background: #eff6ff; color: #1a56db; }
.detail-icon.expired { background: #fef2f2; color: #dc2626; }
.detail-name { font-size: 20px; font-weight: 700; color: #1a1a1a; display: block; }
.detail-no { font-size: 13px; color: #999; margin-top: 4px; display: block; }

.empty-state { text-align: center; padding: 80px 0; color: #999; }
.empty-state svg { margin-bottom: 16px; opacity: 0.4; }
.empty-state p { font-size: 15px; margin: 0 0 6px; }
.empty-hint { font-size: 13px !important; color: #bbb !important; }
</style>
