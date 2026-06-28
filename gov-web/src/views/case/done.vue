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
        <el-form-item label="结果">
          <el-select v-model="searchForm.result" placeholder="全部" clearable size="small" style="width:110px">
            <el-option label="通过" :value="1" />
            <el-option label="驳回" :value="2" />
            <el-option label="转办" :value="3" />
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
        <el-table-column prop="taskName" label="任务名称" width="140" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column label="审批结果" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="resultTagType(row.result)" size="small">{{ resultLabel(row.result) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="opinion" label="审批意见" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="审批时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <button class="link-btn" @click="handleView(row)">查看详情</button>
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

    <el-dialog v-model="detailVisible" title="审批详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="办件号">{{ detailData.applyNo }}</el-descriptions-item>
        <el-descriptions-item label="事项名称">{{ detailData.itemName }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ detailData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="审批结果">
          <el-tag :type="resultTagType(detailData.result)" size="small">{{ resultLabel(detailData.result) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2">{{ detailData.opinion }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getDoneList } from '@/api/case'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({ applyNo: '', itemName: '', result: null })

const detailVisible = ref(false)
const detailData = reactive({})

const resultLabel = (v) => ({ 1: '通过', 2: '驳回', 3: '转办' }[v] || '-')
const resultTagType = (v) => ({ 1: 'success', 2: 'danger', 3: 'warning' }[v] || 'info')

const fetchList = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] === null) delete params[k] })
    const res = await getDoneList(params)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch {} finally { loading.value = false }
}

const handleSearch = () => { pageNum.value = 1; fetchList() }
const handleReset = () => { searchForm.applyNo = ''; searchForm.itemName = ''; searchForm.result = null; handleSearch() }

const handleView = (row) => { Object.assign(detailData, row); detailVisible.value = true }

onMounted(() => { fetchList() })
</script>
