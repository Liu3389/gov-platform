<template>
  <div class="page-container">
    <el-card header="事项列表" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="事项名称">
            <el-input v-model="searchForm.itemName" placeholder="请输入事项名称" clearable />
          </el-form-item>
          <el-form-item label="事项类型">
            <el-select v-model="searchForm.itemType" placeholder="请选择" clearable>
              <el-option label="行政许可" :value="1" />
              <el-option label="公共服务" :value="2" />
              <el-option label="行政确认" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search">查询</el-button>
            <el-button icon="Refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="[]" border stripe v-loading="false">
        <el-table-column prop="itemCode" label="事项编码" width="120" />
        <el-table-column prop="itemName" label="事项名称" min-width="180" />
        <el-table-column prop="deptName" label="所属部门" width="150" />
        <el-table-column prop="itemType" label="事项类型" width="120" />
        <el-table-column prop="timeLimit" label="办理时限" width="120" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default>
            <el-button type="primary" link>编辑</el-button>
            <el-button type="primary" link>查看</el-button>
            <el-button type="danger" link>下架</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="0"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const searchForm = reactive({
  itemName: '',
  itemType: null,
})

const pageNum = ref(1)
const pageSize = ref(10)
</script>

<style scoped>
.page-container {
  padding: 0;
}
.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
