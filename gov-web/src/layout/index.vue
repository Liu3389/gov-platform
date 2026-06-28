<template>
  <!-- 布局分发：管理员 vs 用户 vs 未登录 -->
  <AdminLayout v-if="userStore.isAdmin" />
  <UserLayout v-else-if="userStore.isLoggedIn" />
  <router-view v-else />
</template>

<script setup>
import { useUserStore } from '@/store/user'
import AdminLayout from './AdminLayout.vue'
import UserLayout from './UserLayout.vue'

const userStore = useUserStore()
</script>
