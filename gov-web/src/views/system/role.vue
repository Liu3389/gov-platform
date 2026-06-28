<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.roleName" placeholder="请输入" clearable size="small" @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable size="small" style="width:110px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" size="small" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" size="small" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:right;margin-bottom:16px">
        <el-button type="primary" :icon="Plus" size="small" @click="handleAdd">新增角色</el-button>
      </div>
    </div>

    <div class="table-wrap">
      <el-table :data="tableData" size="small" v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="roleName" label="角色名称" min-width="140" />
        <el-table-column prop="roleCode" label="角色编码" width="140" />
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <span class="status-dot" :class="row.status === 1 ? 'sd-green' : 'sd-gray'"></span>
            {{ row.status === 1 ? '启用' : '禁用' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <button class="link-btn" @click="handleEdit(row)">编辑</button>
            <button class="link-btn" @click="handleAssignPerm(row)">分配权限</button>
            <button class="link-btn-danger" @click="handleDelete(row)">删除</button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" :close-on-click-modal="false" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="formData.roleCode" placeholder="请输入角色编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="permVisible" title="分配权限" width="420px">
      <el-tree
        ref="permTreeRef"
        :data="menuTree"
        show-checkbox
        node-key="id"
        :props="{ label: 'menuName', children: 'children' }"
        :default-checked-keys="checkedMenuIds"
        default-expand-all
      />
      <template #footer>
        <el-button @click="permVisible = false">取消</el-button>
        <el-button type="primary" :loading="permLoading" @click="handleSavePerm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/user'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({ roleName: '', status: null })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const formData = reactive({ id: null, roleName: '', roleCode: '', remark: '', status: 1 })
const formRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const permVisible = ref(false)
const permLoading = ref(false)
const permTreeRef = ref(null)
const currentRoleId = ref(null)
const menuTree = ref([])
const checkedMenuIds = ref([])

const fetchList = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm }
    Object.keys(params).forEach(k => { if (params[k] === '' || params[k] === null) delete params[k] })
    const res = await getRoleList(params)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch {} finally { loading.value = false }
}

const handleSearch = () => { pageNum.value = 1; fetchList() }
const handleReset = () => { searchForm.roleName = ''; searchForm.status = null; handleSearch() }

const resetForm = () => { formData.id = null; formData.roleName = ''; formData.roleCode = ''; formData.remark = ''; formData.status = 1 }

const handleAdd = () => {
  isEdit.value = false; dialogTitle.value = '新增角色'
  resetForm(); formRef.value?.resetFields(); dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true; dialogTitle.value = '编辑角色'
  formRef.value?.resetFields()
  nextTick(() => Object.assign(formData, { id: row.id, roleName: row.roleName, roleCode: row.roleCode, remark: row.remark || '', status: row.status }))
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除角色「${row.roleName}」吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => { await deleteRole(row.id); ElMessage.success('删除成功'); fetchList() })
    .catch(() => {})
}

const handleDialogClose = () => { resetForm() }

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) { await updateRole({ ...formData }); ElMessage.success('更新成功') }
      else { await addRole({ ...formData }); ElMessage.success('新增成功') }
      dialogVisible.value = false; fetchList()
    } catch {} finally { submitLoading.value = false }
  })
}

const handleAssignPerm = (row) => { currentRoleId.value = row.id; permVisible.value = true }
const handleSavePerm = () => { ElMessage.info('权限分配功能将在后端接口就绪后启用'); permVisible.value = false }

onMounted(() => { fetchList() })
</script>
