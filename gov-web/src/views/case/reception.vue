<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="办件号">
          <el-input v-model="searchForm.applyNo" placeholder="请输入" clearable size="small" @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="申请人">
          <el-input v-model="searchForm.applicantName" placeholder="请输入" clearable size="small" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable size="small" style="width:120px">
            <el-option v-for="(v, k) in CASE_STATUS" :key="k" :label="v.label" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" size="small" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" size="small" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-wrap">
      <el-table :data="tableData" size="small" v-loading="loading">
        <el-table-column prop="applyNo" label="办件号" width="170" />
        <el-table-column prop="itemName" label="事项名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="deptName" label="受理窗口" width="140" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="CASE_STATUS[row.status]?.type" size="small">{{ CASE_STATUS[row.status]?.label || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="170" />
        <el-table-column label="操作" width="190" fixed="right">
          <template #default="{ row }">
            <button v-if="row.status === 0" class="link-btn" @click="handleAccept(row)">受理</button>
            <button v-if="row.status === 0" class="link-btn-danger" @click="handleReject(row)">驳回</button>
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

    <el-dialog v-model="acceptVisible" title="受理确认" width="460px">
      <p>确定受理 <strong>{{ currentItem.applyNo }}</strong> 的申请吗？</p>
      <p style="color:#909399">办件号将在受理后自动生成</p>
      <template #footer>
        <el-button @click="acceptVisible = false">取消</el-button>
        <el-button type="primary" :loading="acceptLoading" @click="doAccept">确认受理</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="驳回原因" width="460px">
      <el-form ref="rejectFormRef" :model="rejectForm" :rules="rejectRules" label-width="0">
        <el-form-item prop="reason">
          <el-input v-model="rejectForm.reason" type="textarea" :rows="3" placeholder="请填写驳回原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" :loading="rejectLoading" @click="doReject">确定驳回</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="申请详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="办件号">{{ detailData.applyNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="事项名称">{{ detailData.itemName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="受理窗口">{{ detailData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="CASE_STATUS[detailData.status]?.type" size="small">{{ CASE_STATUS[detailData.status]?.label || '-' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ detailData.applyTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getCaseList, acceptApplication, rejectApplication } from '@/api/case'
import { CASE_STATUS } from '@/utils/constants'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({ applyNo: '', applicantName: '', status: null })

const detailVisible = ref(false)
const detailData = reactive({})

const acceptVisible = ref(false)
const acceptLoading = ref(false)
const currentItem = reactive({})

const rejectVisible = ref(false)
const rejectLoading = ref(false)
const rejectFormRef = ref(null)
const rejectForm = reactive({ reason: '' })
const rejectRules = { reason: [{ required: true, message: '请填写驳回原因', trigger: 'blur' }] }

const fetchList = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] === null) delete params[k] })
    const res = await getCaseList(params)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch {} finally { loading.value = false }
}

const handleSearch = () => { pageNum.value = 1; fetchList() }
const handleReset = () => { searchForm.applyNo = ''; searchForm.applicantName = ''; searchForm.status = null; handleSearch() }

const handleView = (row) => { Object.assign(detailData, row); detailVisible.value = true }
const handleAccept = (row) => { Object.assign(currentItem, row); acceptVisible.value = true }
const handleReject = (row) => { Object.assign(currentItem, row); rejectForm.reason = ''; rejectVisible.value = true }

const doAccept = async () => {
  acceptLoading.value = true
  try { await acceptApplication({ caseId: currentItem.id }); ElMessage.success('受理成功'); acceptVisible.value = false; fetchList() }
  catch {} finally { acceptLoading.value = false }
}

const doReject = async () => {
  if (!rejectFormRef.value) return
  await rejectFormRef.value.validate(async (valid) => {
    if (!valid) return
    rejectLoading.value = true
    try { await rejectApplication({ caseId: currentItem.id, reason: rejectForm.reason }); ElMessage.success('已驳回'); rejectVisible.value = false; fetchList() }
    catch {} finally { rejectLoading.value = false }
  })
}

onMounted(() => { fetchList() })
</script>
