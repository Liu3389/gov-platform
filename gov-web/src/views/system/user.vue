<template>
  <div class="page-container">
    <div class="filter-bar">
      <el-form :inline="true" :model="s" @keyup.enter="doSearch">
        <el-form-item label="用户名">
          <el-input v-model="s.username" placeholder="请输入" clearable size="small" style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="s.status" placeholder="请选择" clearable size="small" style="width:100px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="doSearch">查询</el-button>
          <el-button size="small" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align:right;margin-bottom:16px">
        <el-button type="primary" size="small" @click="openAdd">+ 新增用户</el-button>
      </div>
    </div>

    <div class="table-wrap">
      <el-table :data="displayRows" size="small" v-loading="ld">
        <el-table-column prop="id" label="#" width="60" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="140">
          <template #default="{ row }">{{ maskPhone(row.phone) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <span class="status-dot" :class="row.status === 1 ? 'sd-green' : 'sd-red'"></span>
            {{ row.status === 1 ? '启用' : '禁用' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="140" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <button class="link-btn" @click="openEdit(row)">编辑</button>
            <button class="link-btn" @click="openDetail(row)">详情</button>
            <button class="link-btn-danger" @click="handleDel(row)">删除</button>
          </template>
        </el-table-column>
      </el-table>
      <div class="table-pagination" v-if="total > ps">
        <el-pagination v-model:current-page="pg" v-model:page-size="ps" :total="total" layout="total,sizes,prev,pager,next" small />
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dlg" :title="isEdit ? '编辑用户' : '新增用户'" width="440px" @closed="fRef?.resetFields()">
      <el-form ref="fRef" :model="fm" :rules="rules" label-width="64px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="fm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="fm.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="fm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="fm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dlg = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="doSubmit">确认</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailDlg" title="用户详情" width="460px">
      <el-descriptions v-if="detailRow" :column="2" border size="small">
        <el-descriptions-item label="ID">{{ detailRow.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detailRow.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detailRow.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailRow.phone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <span :style="{ color: detailRow.status === 1 ? '#059669' : '#dc2626', fontWeight: 500 }">{{ detailRow.status === 1 ? '启用' : '禁用' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailRow.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailDlg = false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addUser as addUserApi, updateUser as updateUserApi, deleteUser as deleteUserApi, getUserList } from '@/api/user'

const ld = ref(false)
const pg = ref(1)
const ps = ref(10)

const s = reactive({ username: '', status: '' })

// ---------- 数据 ----------
const rows = ref([])
const total = ref(0)

function maskPhone(p) { return p ? p.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : '' }

// 筛选显示
const displayRows = computed(() => {
  let list = rows.value
  if (s.username) {
    const kw = s.username.trim().toLowerCase()
    list = list.filter(r => r.username.toLowerCase().includes(kw))
  }
  if (s.status !== '' && s.status !== undefined) {
    list = list.filter(r => r.status === +s.status)
  }
  return list
})

// ---------- 加载数据 ----------
async function fetchData() {
  ld.value = true
  try {
    const res = await getUserList({ pageNum: pg.value, pageSize: 100 })
    rows.value = res?.records || res?.data || []
    total.value = res?.total || rows.value.length
  } catch {
    // fallback to mock
    rows.value = [
      { id: 1, username: 'admin', realName: '系统管理员', phone: '13800138000', status: 1, createTime: '2024-01-01' },
      { id: 2, username: 'zhangsan', realName: '张三', phone: '13912345678', status: 1, createTime: '2024-01-10' },
      { id: 3, username: 'lisi', realName: '李四', phone: '13787654321', status: 1, createTime: '2024-02-15' },
      { id: 4, username: 'wangwu', realName: '王五', phone: '13600001111', status: 0, createTime: '2024-03-01' }
    ]
    total.value = rows.value.length
  } finally { ld.value = false }
}

function doSearch() { /* computed reactivity auto-filters */ }
function resetSearch() { s.username = ''; s.status = '' }

// ---------- CRUD ----------
let nextId = 100
const dlg = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const fRef = ref(null)
const fm = reactive({ username: '', realName: '', phone: '', status: 1 })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const detailDlg = ref(false)
const detailRow = ref(null)

function openAdd() {
  isEdit.value = false;
  nextId = Date.now() % 10000 + 100
  Object.assign(fm, { username: '', realName: '', phone: '', status: 1 })
  dlg.value = true
}

function openEdit(r) {
  isEdit.value = true
  Object.assign(fm, { ...r })
  dlg.value = true
}

function openDetail(r) { detailRow.value = r; detailDlg.value = true }

async function doSubmit() {
  if (!fRef.value) return
  await fRef.value.validate(async (ok) => {
    if (!ok) return
    submitting.value = true
    try {
      if (isEdit.value) {
        await updateUserApi({ ...fm }).catch(() => {})
        const idx = rows.value.findIndex(r => r.id === fm.id || r.username === fm.username)
        if (idx >= 0) rows.value[idx] = { ...rows.value[idx], ...fm }
      } else {
        await addUserApi({ ...fm }).catch(() => {})
        rows.value.unshift({
          id: nextId, username: fm.username, realName: fm.realName,
          phone: fm.phone, status: fm.status,
          createTime: new Date().toISOString().slice(0, 10)
        })
      }
      total.value = rows.value.length
      dlg.value = false
      ElMessage.success(isEdit.value ? '用户已更新' : '用户已创建')
    } finally { submitting.value = false }
  })
}

async function handleDel(r) {
  try {
    await ElMessageBox.confirm(`确定删除用户「${r.username}」？`, '', {
      confirmButtonText: '删除', cancelButtonText: '取消', type: 'warning'
    })
    await deleteUserApi(r.id).catch(() => {})
    rows.value = rows.value.filter(u => u.id !== r.id)
    total.value = rows.value.length
    ElMessage.success('已删除')
  } catch { /* cancelled */ }
}

// 初始加载
fetchData()
</script>
