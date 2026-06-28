<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="证照编号">
          <el-input v-model="searchForm.licenseNo" placeholder="请输入" clearable size="small" style="width:200px" @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="持证人">
          <el-input v-model="searchForm.holderName" placeholder="请输入" clearable size="small" style="width:140px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable size="small" style="width:110px">
            <el-option label="有效" :value="1" />
            <el-option label="过期" :value="2" />
            <el-option label="注销" :value="3" />
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
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="licenseNo" label="证照编号" min-width="190" show-overflow-tooltip />
        <el-table-column prop="catalogName" label="证照名称" width="140" />
        <el-table-column prop="holderName" label="持证人" width="100" />
        <el-table-column prop="issueDate" label="签发日期" width="120" />
        <el-table-column prop="validUntil" label="有效期至" width="120" />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <span class="status-dot" :class="statusDotCls(row.status)"></span>
            {{ statusLabel(row.status) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <button class="link-btn" @click="handleDetail(row)">详情</button>
            <button class="link-btn" @click="handleVerify(row)">核验</button>
            <button v-if="row.status !== 3" class="link-btn-danger" @click="handleRevoke(row)">注销</button>
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
    <el-dialog v-model="detailVisible" title="证照详情" width="560px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="证照编号">{{ detailData.licenseNo }}</el-descriptions-item>
        <el-descriptions-item label="证照名称">{{ detailData.catalogName }}</el-descriptions-item>
        <el-descriptions-item label="持证人">{{ detailData.holderName }}</el-descriptions-item>
        <el-descriptions-item label="证件号码">{{ detailData.holderIdNo }}</el-descriptions-item>
        <el-descriptions-item label="签发日期">{{ detailData.issueDate }}</el-descriptions-item>
        <el-descriptions-item label="有效期至">{{ detailData.validUntil }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <span class="status-dot" :class="statusDotCls(detailData.status)"></span>
          {{ statusLabel(detailData.status) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 核验弹窗 -->
    <el-dialog v-model="verifyVisible" title="证照核验" width="460px">
      <el-form ref="verifyFormRef" :model="verifyForm" :rules="verifyRules" label-width="100px">
        <el-form-item label="核验码">
          <el-input v-model="verifyForm.verifyCode" placeholder="请输入核验码" />
        </el-form-item>
        <el-descriptions v-if="verifyResult" :column="2" border style="margin-top:16px">
          <el-descriptions-item label="核验结果">
            <el-tag :type="verifyResult.pass ? 'success' : 'danger'" size="small">
              {{ verifyResult.pass ? '通过' : '不通过' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="核验时间">{{ verifyResult.verifyTime }}</el-descriptions-item>
        </el-descriptions>
      </el-form>
      <template #footer>
        <el-button @click="verifyVisible = false">关闭</el-button>
        <el-button type="primary" :loading="verifyLoading" @click="doVerify">核验</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getLicenseList, getLicenseDetail, verifyLicense, revokeLicense } from '@/api/license'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({
  licenseNo: '',
  holderName: '',
  status: null
})

const statusLabel = (st) => ({ 1: '有效', 2: '过期', 3: '注销' }[st] || '-')
const statusDotCls = (st) => ({ 1: 'sd-green', 2: 'sd-red', 3: 'sd-gray' }[st] || '')

const detailVisible = ref(false)
const detailData = reactive({})

const verifyVisible = ref(false)
const verifyLoading = ref(false)
const verifyFormRef = ref(null)
const currentVerifyId = ref(null)
const verifyForm = reactive({ verifyCode: '' })
const verifyRules = {
  verifyCode: [{ required: true, message: '请输入核验码', trigger: 'blur' }]
}
const verifyResult = ref(null)

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
    const res = await getLicenseList(params)
    tableData.value = res.records || res || []
    total.value = res.total || 0
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchList()
}

const handleReset = () => {
  searchForm.licenseNo = ''
  searchForm.holderName = ''
  searchForm.status = null
  handleSearch()
}

const handleDetail = async (row) => {
  try {
    const res = await getLicenseDetail(row.id)
    Object.assign(detailData, res)
  } catch {
    Object.assign(detailData, row)
  }
  detailVisible.value = true
}

const handleVerify = (row) => {
  currentVerifyId.value = row.id
  verifyForm.verifyCode = ''
  verifyResult.value = null
  verifyVisible.value = true
}

const doVerify = async () => {
  if (!verifyFormRef.value) return
  await verifyFormRef.value.validate(async (valid) => {
    if (!valid) return
    verifyLoading.value = true
    try {
      const res = await verifyLicense(currentVerifyId.value, { verifyCode: verifyForm.verifyCode })
      verifyResult.value = res || { pass: true, verifyTime: new Date().toLocaleString() }
      ElMessage.success('核验完成')
    } catch {
      // handled
    } finally {
      verifyLoading.value = false
    }
  })
}

const handleRevoke = (row) => {
  ElMessageBox.confirm(
    `确定注销证照「${row.licenseNo}」吗？注销后不可恢复。`,
    '提示',
    { confirmButtonText: '确定注销', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    try {
      await revokeLicense(row.id)
      ElMessage.success('已注销')
      // 更新本地状态
      const idx = tableData.value.findIndex(r => r.id === row.id)
      if (idx !== -1) tableData.value[idx].status = 3
    } catch {
      // handled
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchList()
})
</script>
