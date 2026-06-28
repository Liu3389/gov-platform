<template>
  <div class="page-container">
    <div class="form-box">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px" style="max-width:700px">
        <el-form-item label="事项名称" prop="itemName">
          <el-input v-model="formData.itemName" placeholder="请输入事项名称" />
        </el-form-item>
        <el-form-item label="事项编码" prop="itemCode">
          <el-input v-model="formData.itemCode" placeholder="请输入事项编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="事项类型" prop="itemType">
          <el-select v-model="formData.itemType" placeholder="请选择事项类型" style="width:240px">
            <el-option v-for="(v, k) in ITEM_TYPE" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-tree-select
            v-model="formData.deptId"
            :data="deptTree"
            :props="{ label: 'deptName', value: 'id', children: 'children' }"
            placeholder="请选择部门"
            check-strictly
            clearable
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="办理时限" prop="timeLimit">
          <el-input v-model="formData.timeLimit" placeholder="如：7个工作日" style="width:240px" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">发布</el-radio>
            <el-radio :value="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="办理流程说明" prop="guideContent">
          <el-input
            v-model="formData.guideContent"
            type="textarea"
            :rows="4"
            placeholder="请输入办理流程说明"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
          <el-button @click="$router.push('/item/list')">返回列表</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addItem, updateItem, getItemDetail } from '@/api/item'
import { getDeptList } from '@/api/user'
import { ITEM_TYPE } from '@/utils/constants'

const route = useRoute()
const router = useRouter()

const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const deptTree = ref([])

const formData = reactive({
  id: null,
  itemName: '',
  itemCode: '',
  itemType: null,
  deptId: null,
  timeLimit: '',
  status: 1,
  guideContent: ''
})

const formRules = {
  itemName: [{ required: true, message: '请输入事项名称', trigger: 'blur' }],
  itemCode: [{ required: true, message: '请输入事项编码', trigger: 'blur' }],
  itemType: [{ required: true, message: '请选择事项类型', trigger: 'change' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  timeLimit: [{ required: true, message: '请输入办理时限', trigger: 'blur' }]
}

const loadEditData = async (id) => {
  try {
    const res = await getItemDetail(id)
    Object.assign(formData, {
      id: res.id, itemName: res.itemName, itemCode: res.itemCode,
      itemType: res.itemType, deptId: res.deptId,
      timeLimit: res.timeLimit, status: res.status, guideContent: res.guideContent || ''
    })
  } catch {
    ElMessage.error('获取事项详情失败')
    router.push('/item/list')
  }
}

const loadDeptTree = async () => {
  try {
    const res = await getDeptList({})
    deptTree.value = res.records || res || []
  } catch {}
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) { await updateItem({ ...formData }); ElMessage.success('更新成功') }
      else { await addItem({ ...formData }); ElMessage.success('新增成功') }
      router.push('/item/list')
    } catch {} finally { submitLoading.value = false }
  })
}

onMounted(() => {
  const id = route.query.id
  if (id) { isEdit.value = true; loadEditData(id) }
  loadDeptTree()
})
</script>

<style scoped>
.form-box {
  background: #fff;
  border: 1px solid rgba(0,0,0,0.04);
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.02);
}
</style>
