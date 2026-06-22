<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background-color: #001529">
      <div class="logo">
        <span>智慧政务平台</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        background-color="#001529"
        text-color="#ffffff90"
        active-text-color="#ffffff"
        router
        style="border-right: none"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/item">
          <i class="el-icon-s-order"></i>
          <span>事项管理</span>
        </el-menu-item>
        <el-menu-item index="/case">
          <i class="el-icon-s-claim"></i>
          <span>办件管理</span>
        </el-menu-item>
        <el-menu-item index="/license">
          <i class="el-icon-s-check"></i>
          <span>证照管理</span>
        </el-menu-item>
        <el-menu-item index="/complaint">
          <i class="el-icon-s-comment"></i>
          <span>投诉建议</span>
        </el-menu-item>
        <el-menu-item index="/open">
          <i class="el-icon-s-data"></i>
          <span>政务公开</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="height: 60px; background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: flex-end; padding: 0 20px">
        <span style="margin-right: 16px">{{ userInfo.nickname || '管理员' }}</span>
        <el-button type="text" @click="handleLogout">退出登录</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'App',
  computed: {
    ...mapState(['userInfo']),
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #002140;
}
</style>
