<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="appStore.sidebarCollapsed ? '64px' : '220px'" class="sidebar">
      <div class="logo">
        <img src="@/assets/logo.svg" alt="logo" class="logo-img" />
        <span v-show="!appStore.sidebarCollapsed" class="logo-text">智慧政务</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="appStore.sidebarCollapsed"
        :collapse-transition="false"
        router
        background-color="#001529"
        text-color="#ffffffb3"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-sub-menu index="/item">
          <template #title>
            <el-icon><List /></el-icon>
            <span>事项管理</span>
          </template>
          <el-menu-item index="/item/list">事项列表</el-menu-item>
          <el-menu-item index="/item/add">事项录入</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="/case">
          <template #title>
            <el-icon><Folder /></el-icon>
            <span>办件管理</span>
          </template>
          <el-menu-item index="/case/todo">待办任务</el-menu-item>
          <el-menu-item index="/case/done">已办任务</el-menu-item>
          <el-menu-item index="/case/reception">窗口受理</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/license">
          <el-icon><Postcard /></el-icon>
          <span>证照管理</span>
        </el-menu-item>

        <el-menu-item index="/complaint">
          <el-icon><Message /></el-icon>
          <span>投诉建议</span>
        </el-menu-item>

        <el-menu-item index="/open">
          <el-icon><View /></el-icon>
          <span>政务公开</span>
        </el-menu-item>

        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
          <el-menu-item index="/system/dept">部门管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 主区域 -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="appStore.toggleSidebar">
            <Fold v-if="!appStore.sidebarCollapsed" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentTitle">{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-icon><UserFilled /></el-icon>
              <span>管理员</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/store/app'

const route = useRoute()
const router = useRouter()

const appStore = useAppStore()

// 当前激活菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentTitle = computed(() => route.meta.title || '')

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100%;
}

.sidebar {
  background-color: #001529;
  overflow-y: auto;
  overflow-x: hidden;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
}

.logo-img {
  width: 32px;
  height: 32px;
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  margin-left: 10px;
  white-space: nowrap;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.main-content {
  background: #f0f2f5;
  min-height: calc(100vh - 64px);
}
</style>
