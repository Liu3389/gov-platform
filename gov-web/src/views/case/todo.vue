<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="办件号">
          <el-input v-model="searchForm.applyNo" placeholder="请输入" clearable size="small" @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="事项名称">
          <el-input v-model="searchForm.itemName" placeholder="请输入" clearable size="small" />
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input v-model="searchForm.taskName" placeholder="请输入" clearable size="small" />
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
        <el-table-column prop="taskName" label="当前任务" width="140" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="CASE_STATUS[row.status]?.type" size="small">{{ CASE_STATUS[row.status]?.label || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <button class="link-btn" @click="handleApprove(row)">审批</button>
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

    <el-dialog v-model="approveVisible" title="任务审批" width="560px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small" style="margin-bottom:20px">
        <el-descriptions-item label="办件号">{{ currentTask.applyNo }}</el-descriptions-item>
        <el-descriptions-item label="事项名称">{{ currentTask.itemName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ currentTask.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="当前任务">{{ currentTask.taskName }}</el-descriptions-item>
      </el-descriptions>
      <el-form ref="approveFormRef" :model="approveForm" :rules="approveRules" label-width="100px">
        <el-form-item label="审批结果" prop="result">
          <el-radio-group v-model="approveForm.result">
            <el-radio :value="1">通过</el-radio>
            <el-radio :value="2">驳回</el-radio>
            <el-radio :value="3">转办</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="approveForm.result === 3" label="转办人" prop="targetUserId">
          <el-select v-model="approveForm.targetUserId" placeholder="请选择转办人" style="width:100%">
            <el-option v-for="u in userList" :key="u.id" :label="`${u.realName}（${u.deptName}）`" :value="u.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批意见" prop="opinion">
          <el-input v-model="approveForm.opinion" type="textarea" :rows="3" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" :loading="approveLoading" @click="handleSubmitApprove">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="办件详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="办件号">{{ detailData.applyNo }}</el-descriptions-item>
        <el-descriptions-item label="事项名称">{{ detailData.itemName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="当前任务">{{ detailData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="CASE_STATUS[detailData.status]?.type" size="small">{{ CASE_STATUS[detailData.status]?.label || '-' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getTodoList, getTaskDetail, approveTask, rejectTask, transferTask } from '@/api/case'
import { CASE_STATUS } from '@/utils/constants'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({ applyNo: '', itemName: '', taskName: '' })

const detailVisible = ref(false)
const detailData = reactive({})

const approveVisible = ref(false)
const approveLoading = ref(false)
const approveFormRef = ref(null)
const currentTask = reactive({})
const userList = ref([])

const approveForm = reactive({ result: 1, opinion: '', targetUserId: null })
const approveRules = {
  result: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
  targetUserId: [{ required: true, message: '请选择转办人', trigger: 'change' }],
  opinion: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] === null) delete params[k] })
    const res = await getTodoList(params)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch {} finally { loading.value = false }
}

const handleSearch = () => { pageNum.value = 1; fetchList() }
const handleReset = () => { searchForm.applyNo = ''; searchForm.itemName = ''; searchForm.taskName = ''; handleSearch() }

const handleView = (row) => { Object.assign(detailData, row); detailVisible.value = true }

const handleApprove = (row) => {
  Object.assign(currentTask, row)
  approveForm.result = 1; approveForm.opinion = ''; approveForm.targetUserId = null
  approveVisible.value = true
}

const handleSubmitApprove = async () => {
  if (!approveFormRef.value) return
  await approveFormRef.value.validate(async (valid) => {
    if (!valid) return
    approveLoading.value = true
    try {
      const data = { taskId: currentTask.taskId, opinion: approveForm.opinion }
      if (approveForm.result === 1) { await approveTask(data); ElMessage.success('审批通过') }
      else if (approveForm.result === 2) { await rejectTask(data); ElMessage.success('已驳回') }
      else { await transferTask({ ...data, targetUserId: approveForm.targetUserId }); ElMessage.success('已转办') }
      approveVisible.value = false; fetchList()
    } catch {} finally { approveLoading.value = false }
  })
}

onMounted(() => { fetchList() })
</script>
