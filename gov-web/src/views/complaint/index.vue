<template>
  <div class="page-container">
    <div class="tab-row">
      <button v-for="t in tabs" :key="t.k" class="c-tab" :class="{ active: currentTab === t.k }" @click="switchTab(t.k)">{{ t.l }}</button>
    </div>

    <!-- 我的工单 -->
    <div v-if="currentTab === 'my'">
      <div class="filter-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="类型">
            <el-select v-model="searchForm.type" placeholder="全部" clearable size="small" style="width:110px">
              <el-option label="投诉" value="complaint" />
              <el-option label="建议" value="suggestion" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="全部" clearable size="small" style="width:110px">
              <el-option label="待处理" :value="0" />
              <el-option label="处理中" :value="1" />
              <el-option label="已办结" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" :icon="Search" @click="handleSearch">查询</el-button>
            <el-button size="small" :icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="table-wrap">
        <el-table :data="tableData" size="small" v-loading="loading">
          <el-table-column prop="subject" label="主题" min-width="240" show-overflow-tooltip />
          <el-table-column label="类型" width="80" align="center">
            <template #default="{ row }">
              <span class="status-tag" :class="row.sourceType === 'suggestion' ? 's-info' : 's-danger'">
                {{ row.sourceType === 'suggestion' ? '建议' : '投诉' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="100" align="center">
            <template #default="{ row }">
              <span class="status-dot" :class="statusDotCls(row.status)"></span>
              {{ statusLabel(row.status) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="提交时间" width="170" />
          <el-table-column label="操作" width="100" align="center">
            <template #default="{ row }">
              <button class="link-btn" @click="handleView(row)">查看</button>
            </template>
          </el-table-column>
        </el-table>
        <div class="table-pagination">
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50]"
            layout="total,sizes,prev,pager,next"
            small
            @size-change="fetchList"
            @current-change="fetchList"
          />
        </div>
      </div>
    </div>

    <!-- 提交投诉/建议 -->
    <div v-else class="form-block">
      <el-form ref="fRef" :model="fm" :rules="rules" label-width="80px" style="max-width:540px">
        <el-form-item label="类型" prop="submitType">
          <el-radio-group v-model="fm.submitType">
            <el-radio value="complaint">投诉</el-radio>
            <el-radio value="suggestion">建议</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="主题" prop="subject">
          <el-input v-model="fm.subject" placeholder="请简要描述主题" />
        </el-form-item>
        <el-form-item label="详细内容" prop="content">
          <el-input v-model="fm.content" type="textarea" :rows="5" placeholder="请详细描述您的问题或建议..." />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="doSubmit">确认提交</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="工单详情" width="560px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="主题">{{ detailData.subject }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <span class="status-tag" :class="detailData.sourceType === 'suggestion' ? 's-info' : 's-danger'">
            {{ detailData.sourceType === 'suggestion' ? '建议' : '投诉' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <span class="status-dot" :class="statusDotCls(detailData.status)"></span>
          {{ statusLabel(detailData.status) }}
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="详细内容" :span="2">{{ detailData.content }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.replyContent" label="处理回复" :span="2">{{ detailData.replyContent }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getComplaintList, getSuggestionList, submitComplaint, submitSuggestion, getComplaintDetail } from '@/api/complaint'

const currentTab = ref('my')
const tabs = [{ k: 'my', l: '我的工单' }, { k: 'submit', l: '提交意见' }]

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({
  type: null,
  status: null
})

const statusLabel = (s) => ({ 0: '待处理', 1: '处理中', 2: '已办结' }[s] || '-')
const statusDotCls = (s) => ({ 0: 'sd-orange', 1: 'sd-green', 2: 'sd-gray' }[s] || '')

const detailVisible = ref(false)
const detailData = reactive({})

const fRef = ref(null)
const submitting = ref(false)
const fm = reactive({ submitType: 'complaint', subject: '', content: '' })
const rules = {
  subject: [{ required: true, message: '请输入主题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入详细内容', trigger: 'blur' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchForm.status !== null && searchForm.status !== '') params.status = searchForm.status

    let complaintRes = { records: [], total: 0 }
    let suggestionRes = { records: [], total: 0 }

    try { complaintRes = await getComplaintList(params) } catch {}
    try { suggestionRes = await getSuggestionList(params) } catch {}

    // 合并并标记来源
    let list = [
      ...(complaintRes.records || []).map(r => ({ ...r, sourceType: 'complaint' })),
      ...(suggestionRes.records || []).map(r => ({ ...r, sourceType: 'suggestion' }))
    ]

    if (searchForm.type === 'complaint') list = list.filter(r => r.sourceType === 'complaint')
    if (searchForm.type === 'suggestion') list = list.filter(r => r.sourceType === 'suggestion')

    tableData.value = list
    total.value = (complaintRes.total || 0) + (suggestionRes.total || 0)
  } catch {
    // handled
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { pageNum.value = 1; fetchList() }
const handleReset = () => { searchForm.type = null; searchForm.status = null; handleSearch() }

const switchTab = (k) => {
  currentTab.value = k
  if (k === 'my') fetchList()
}

const handleView = async (row) => {
  try {
    const endpoint = row.sourceType === 'suggestion'
      ? { getDetail: getComplaintDetail }
      : { getDetail: getComplaintDetail }
    const res = await getComplaintDetail(row.id)
    Object.assign(detailData, res)
  } catch {
    Object.assign(detailData, row)
  }
  detailVisible.value = true
}

const doSubmit = async () => {
  if (!fRef.value) return
  await fRef.value.validate(async (ok) => {
    if (!ok) return
    submitting.value = true
    try {
      const data = { subject: fm.subject, content: fm.content }
      if (fm.submitType === 'complaint') {
        await submitComplaint(data)
      } else {
        await submitSuggestion(data)
      }
      ElMessage.success('提交成功')
      fm.subject = ''; fm.content = ''
      currentTab.value = 'my'
      fetchList()
    } catch {
      // handled
    } finally {
      submitting.value = false
    }
  })
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.tab-row {
  display: flex; gap: 2px; margin-bottom: 24px;
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04);
  border-radius: 14px; padding: 4px; max-width: 240px;
}
.c-tab {
  flex: 1; font-size: 14px; font-weight: 450; color: #666;
  background: none; border: none; padding: 8px 16px; border-radius: 10px;
  cursor: pointer; font-family: inherit; transition: all 0.15s;
}
.c-tab:hover { color: #1a1a1a; }
.c-tab.active { background: #1a1a1a; color: #fff; font-weight: 500; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }

.form-block {
  background: #fff; border: 1.5px solid rgba(0,0,0,0.04);
  border-radius: 16px; padding: 28px 32px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.02);
}
</style>
