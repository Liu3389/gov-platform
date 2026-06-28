<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="事项名称">
          <el-input v-model="searchForm.itemName" placeholder="请输入" clearable size="small" style="width:180px" @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.itemType" placeholder="请选择" clearable size="small" style="width:140px">
            <el-option v-for="(v, k) in ITEM_TYPE" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable size="small" style="width:110px">
            <el-option label="发布" :value="1" />
            <el-option label="草稿" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" size="small" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" size="small" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:right;margin-bottom:16px">
        <el-button type="primary" :icon="Plus" size="small" @click="$router.push('/item/add')">新增事项</el-button>
      </div>
    </div>

    <div class="table-wrap">
      <el-table :data="tableData" size="small" v-loading="loading">
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="itemName" label="事项名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="itemCode" label="事项编码" width="150" />
        <el-table-column prop="deptName" label="所属部门" width="140" />
        <el-table-column label="类型" width="110" align="center">
          <template #default="{ row }">
            <span class="status-tag" :class="typeTagCls(row.itemType)">{{ ITEM_TYPE[row.itemType] || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="timeLimit" label="办理时限" width="120" />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <span class="status-dot" :class="row.status === 1 ? 'sd-green' : 'sd-gray'"></span>
            {{ row.status === 1 ? '发布' : '草稿' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <button class="link-btn" @click="handleEdit(row)">编辑</button>
            <button class="link-btn" @click="handleView(row)">查看</button>
            <button class="link-btn-danger" @click="handleDel(row)">删除</button>
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

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="事项详情" width="560px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="事项名称">{{ detailData.itemName }}</el-descriptions-item>
        <el-descriptions-item label="事项编码">{{ detailData.itemCode }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <span class="status-tag" :class="typeTagCls(detailData.itemType)">{{ ITEM_TYPE[detailData.itemType] || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="所属部门">{{ detailData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="办理时限">{{ detailData.timeLimit }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <span class="status-dot" :class="detailData.status === 1 ? 'sd-green' : 'sd-gray'"></span>
          {{ detailData.status === 1 ? '发布' : '草稿' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.guideContent" label="办理流程" :span="2">
          {{ detailData.guideContent }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { getItemList, deleteItem } from '@/api/item'
import { ITEM_TYPE } from '@/utils/constants'

const router = useRouter()

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({
  itemName: '',
  itemType: null,
  status: null
})

const detailVisible = ref(false)
const detailData = reactive({})

const typeTagCls = (t) => {
  const map = { 1: 's-info', 2: 's-success', 3: 's-warning' }
  return map[t] || 's-info'
}

const fetchList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    }
    Object.keys(params).forEach(k => {
      if (params[k] === '' || params[k] === null || params[k] === undefined) delete params[k]
    })
    const res = await getItemList(params)
    tableData.value = res.records || res || []
    total.value = res.total || 0
  } catch {
    // API 请求失败时由拦截器统一处理
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchList()
}

const handleReset = () => {
  searchForm.itemName = ''
  searchForm.itemType = null
  searchForm.status = null
  handleSearch()
}

const handleView = (row) => {
  Object.assign(detailData, row)
  detailVisible.value = true
}

const handleEdit = (row) => {
  router.push({ path: '/item/add', query: { id: row.id } })
}

const handleDel = (row) => {
  ElMessageBox.confirm(`确定删除事项「${row.itemName}」吗？`, '提示', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteItem(row.id)
      ElMessage.success('已删除')
      tableData.value = tableData.value.filter(r => r.id !== row.id)
      total.value -= 1
    } catch {
      // handled by interceptor
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchList()
})
</script>
