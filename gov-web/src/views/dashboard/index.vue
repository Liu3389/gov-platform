<template>
  <div class="dashboard">
    <h2 style="margin-bottom: 24px">欢迎使用智慧政务一体化便民服务平台</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalItems }}</div>
          <div class="stat-label">事项总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.pendingTasks }}</div>
          <div class="stat-label">待办任务</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.todayCases }}</div>
          <div class="stat-label">今日办件</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.licensesIssued }}</div>
          <div class="stat-label">证照发放</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalItems: 128,
        pendingTasks: 36,
        todayCases: 89,
        licensesIssued: 256
      }
    }
  }
}
</script>

<style scoped>
.stat-card {
  text-align: center;
  cursor: pointer;
}
.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}
.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
