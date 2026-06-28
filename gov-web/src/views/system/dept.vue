<template>
  <div class="page-container">
    <div class="filter-bar dept-filter-bar">
      <div class="dept-toolbar">
        <el-button type="primary" :icon="Plus" size="small" @click="handleAdd">新增部门</el-button>
        <el-button :icon="RefreshRight" size="small" @click="fetchDeptTree">刷新</el-button>
      </div>
      <span class="dept-count">{{ flatCount }} 个部门</span>
    </div>

    <div class="table-wrap">
      <TransitionGroup name="tree-node" tag="div" class="dept-table-body">
        <template v-for="row in deptTree" :key="row.id">
          <div class="dept-row" :style="{ paddingLeft: (row._level || 0) * 24 + 'px' }">
            <span class="dept-expand" :class="{ expanded: row._expanded }" @click="toggleExpand(row)" v-if="row.children?.length">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="9 18 15 12 9 6"/></svg>
            </span>
            <span v-else class="dept-expand-placeholder"></span>
            <span class="dept-cell-name">{{ row.deptName }}</span>
            <span class="dept-cell-code">{{ row.deptCode }}</span>
            <span class="dept-cell-leader">{{ row.leaderName || '-' }}</span>
            <span class="dept-cell-phone">{{ row.phone || '-' }}</span>
            <span class="dept-cell-sort">{{ row.sort }}</span>
            <span class="dept-cell-status">
              <span class="status-dot" :class="row.status === 1 ? 'sd-green' : 'sd-gray'"></span>
              {{ row.status === 1 ? '启用' : '禁用' }}
            </span>
            <span class="dept-cell-time">{{ row.createTime || '-' }}</span>
            <span class="dept-cell-actions">
              <button class="link-btn" @click="handleAddSub(row)">添加子部门</button>
              <button class="link-btn" @click="handleEdit(row)">编辑</button>
              <button class="link-btn-danger" @click="handleDelete(row)">删除</button>
            </span>
          </div>
          <template v-if="row._expanded && row.children">
            <div v-for="child in flattenChildren(row)" :key="child.id" class="dept-row child-row" :style="{ paddingLeft: (child._level || 1) * 24 + 'px' }">
              <span class="dept-expand-placeholder"></span>
              <span class="dept-cell-name" :class="{ 'has-parent': true }">{{ child.deptName }}</span>
              <span class="dept-cell-code">{{ child.deptCode }}</span>
              <span class="dept-cell-leader">{{ child.leaderName || '-' }}</span>
              <span class="dept-cell-phone">{{ child.phone || '-' }}</span>
              <span class="dept-cell-sort">{{ child.sort }}</span>
              <span class="dept-cell-status">
                <span class="status-dot" :class="child.status === 1 ? 'sd-green' : 'sd-gray'"></span>
                {{ child.status === 1 ? '启用' : '禁用' }}
              </span>
              <span class="dept-cell-time">{{ child.createTime || '-' }}</span>
              <span class="dept-cell-actions">
                <button class="link-btn" @click="handleAddSub(child)">添加子部门</button>
                <button class="link-btn" @click="handleEdit(child)">编辑</button>
                <button class="link-btn-danger" @click="handleDelete(child)">删除</button>
              </span>
            </div>
          </template>
        </template>
      </TransitionGroup>
      <div v-if="deptTree.length === 0 && !loading" class="empty-state">暂无部门数据</div>
    </div>

    <!-- 弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="520px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级部门">
          <el-tree-select
            v-model="formData.parentId"
            :data="deptTreeForSelect"
            :props="{ label: 'deptName', value: 'id', children: 'children' }"
            placeholder="留空为顶级部门"
            check-strictly
            clearable
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="formData.deptCode" placeholder="请输入部门编码" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="formData.leaderName" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" :max="999" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, RefreshRight } from '@element-plus/icons-vue'
import { getDeptList, addDept, updateDept, deleteDept } from '@/api/user'

const loading = ref(false)
const deptTree = ref([])

const flatCount = computed(() => {
  let n = 0
  const walk = list => list.forEach(d => { n++; d.children && walk(d.children) })
  walk(deptTree.value)
  return n
})

// 递归展平子节点
function flattenChildren(parent) {
  const result = []
  if (!parent.children) return result
  for (const c of parent.children) {
    result.push(c)
    if (c._expanded && c.children) {
      result.push(...flattenChildren(c))
    }
  }
  return result
}

function annotateLevel(list, level = 0) {
  list.forEach(d => {
    d._level = level
    d._expanded = d._expanded !== false
    if (d.children?.length) annotateLevel(d.children, level + 1)
  })
}

const deptTreeForSelect = computed(() => deptTree.value)

function toggleExpand(row) {
  row._expanded = !row._expanded
}

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  parentId: null,
  deptName: '',
  deptCode: '',
  leaderName: '',
  phone: '',
  sort: 0,
  status: 1
})

const formRules = {
  deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }]
}

const fetchDeptTree = async () => {
  loading.value = true
  try {
    const res = await getDeptList({})
    const list = res.records || res || []
    annotateLevel(list)
    deptTree.value = list
  } catch {
    // handled
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  formData.id = null
  formData.parentId = null
  formData.deptName = ''
  formData.deptCode = ''
  formData.leaderName = ''
  formData.phone = ''
  formData.sort = 0
  formData.status = 1
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增部门'
  resetForm()
  formRef.value?.resetFields()
  dialogVisible.value = true
}

const handleAddSub = (row) => {
  isEdit.value = false
  dialogTitle.value = `新增「${row.deptName}」子部门`
  resetForm()
  formData.parentId = row.id
  formRef.value?.resetFields()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑部门'
  formRef.value?.resetFields()
  nextTick(() => {
    Object.assign(formData, {
      id: row.id,
      parentId: row.parentId || null,
      deptName: row.deptName,
      deptCode: row.deptCode,
      leaderName: row.leaderName || '',
      phone: row.phone || '',
      sort: row.sort || 0,
      status: row.status
    })
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  const hasChildren = deptTree.value.some(d => d.parentId === row.id) ||
    (row.children && row.children.length > 0)

  if (hasChildren) {
    ElMessage.warning(`部门「${row.deptName}」下有子部门，无法删除。请先删除子部门。`)
    return
  }

  ElMessageBox.confirm(`确定删除部门「${row.deptName}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteDept(row.id)
    ElMessage.success('删除成功')
    fetchDeptTree()
  }).catch(() => {})
}

const handleDialogClose = () => {
  resetForm()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateDept({ ...formData })
        ElMessage.success('更新成功')
      } else {
        await addDept({ ...formData })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchDeptTree()
    } catch {
      // handled
    } finally {
      submitLoading.value = false
    }
  })
}

onMounted(() => {
  fetchDeptTree()
})
</script>

<style scoped>
.dept-toolbar { display: flex; gap: 8px; }
.dept-count { font-size: 13px; color: #999; font-weight: 450; }

.dept-filter-bar {
  display: flex; justify-content: space-between; align-items: center;
  padding-bottom: 12px !important;
}

/* ====== 树表格 ====== */
.dept-table-body { min-height: 200px; }

.dept-row {
  display: flex; align-items: center; gap: 12px;
  padding: 11px 16px;
  border-bottom: 1px solid rgba(0,0,0,0.03);
  transition: background 0.12s;
}
.dept-row:hover { background: rgba(0,0,0,0.015); }
.dept-row:last-child { border-bottom: none; }

.dept-expand {
  width: 20px; height: 20px; flex-shrink: 0;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; color: #999; transition: all 0.2s ease;
  border-radius: 4px;
}
.dept-expand:hover { background: rgba(0,0,0,0.04); color: #555; }
.dept-expand.expanded svg { transform: rotate(90deg); }
.dept-expand svg { transition: transform 0.2s ease; }
.dept-expand-placeholder { width: 20px; flex-shrink: 0; }

.dept-cell-name { flex: 1; min-width: 140px; font-size: 14px; font-weight: 500; color: #1a1a1a; }
.dept-cell-name.has-parent { font-weight: 450; }
.dept-cell-code { width: 130px; font-size: 13px; color: #999; font-family: 'SF Mono', 'Menlo', monospace; }
.dept-cell-leader { width: 90px; font-size: 13px; color: #555; }
.dept-cell-phone { width: 120px; font-size: 13px; color: #555; }
.dept-cell-sort { width: 50px; text-align: center; font-size: 13px; color: #999; }
.dept-cell-status { width: 70px; text-align: center; font-size: 12px; color: #555; }
.dept-cell-time { width: 150px; font-size: 12px; color: #bbb; }
.dept-cell-actions { width: 190px; text-align: right; flex-shrink: 0; white-space: nowrap; }

.empty-state { text-align: center; padding: 60px 0; color: #bbb; font-size: 14px; }

/* 表头 */
.dept-row-header {
  display: flex; align-items: center; gap: 12px;
  padding: 10px 16px;
  background: #fafafa;
  border-bottom: 1px solid rgba(0,0,0,0.05);
  font-size: 12px; font-weight: 600; color: #555;
  text-transform: uppercase; letter-spacing: 0.04em;
}

/* 子行淡入动效 */
.child-row {
  animation: row-in 0.25s ease;
}
@keyframes row-in {
  from { opacity: 0; transform: translateY(-4px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* TransitionGroup 动画 */
.tree-node-enter-active { transition: all 0.25s ease; }
.tree-node-leave-active { transition: all 0.2s ease; }
.tree-node-enter-from { opacity: 0; transform: translateY(-6px); }
.tree-node-leave-to { opacity: 0; transform: translateY(-6px); }
.tree-node-move { transition: transform 0.25s ease; }
</style>
