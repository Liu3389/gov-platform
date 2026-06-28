<template>
  <div class="login">
    <!-- 背景光晕层 -->
    <div class="bg-glow glow-1"></div>
    <div class="bg-glow glow-2"></div>
    <div class="bg-glow glow-3"></div>

    <!-- 主内容 -->
    <div class="login-content">
      <!-- 左：品牌 -->
      <div class="panel-left">
        <div class="brand-inner">
          <div class="brand-mark">
            <svg width="40" height="40" viewBox="0 0 36 36" fill="none">
              <rect width="36" height="36" rx="9" fill="#1a56db"/>
              <path d="M8 18l6 6 14-12" stroke="#fff" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <div class="brand-text">
            <h1>智慧政务</h1>
            <p>一体化便民服务平台</p>
          </div>
          <div class="brand-foot">
            <span>安全 · 高效 · 便民</span>
          </div>
        </div>
      </div>

      <!-- 右：登录面板（毛玻璃） -->
      <div class="panel-right">
        <div class="frost-card">
          <div class="frost-card-inner">
            <h2>登录</h2>
            <el-form ref="formRef" :model="form" :rules="rules" @keyup.enter="handleLogin">
              <div class="field">
                <label>用户名</label>
                <el-input v-model="form.username" placeholder="请输入用户名" />
              </div>
              <div class="field">
                <label>密码</label>
                <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
              </div>
              <el-button type="primary" class="submit-btn" :loading="loading" @click="handleLogin">登录</el-button>
            </el-form>

            <div class="dev-wrap">
              <div class="dev-divider"><span>开发环境</span></div>
              <div class="dev-actions">
                <button :disabled="devLoading" @click="handleDev('admin')">管理员</button>
                <span class="dev-sep">·</span>
                <button :disabled="devLoading" @click="handleDev('user')">用户</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const devLoading = ref(false)

const form = reactive({ username: 'test01', password: '123456' })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 4, message: '密码不少于4位', trigger: 'blur' }]
}

async function handleLogin() {
  if (!formRef.value) return
  await formRef.value.validate(async (ok) => {
    if (!ok) return
    loading.value = true
    try {
      const r = await userStore.login({ username: form.username, password: form.password })
      if (r) {
        ElMessage.success('登录成功')
        router.push(userStore.isAdmin ? '/dashboard' : '/portal')
      } else {
        ElMessage.error('用户名或密码错误')
      }
    } catch {
      ElMessage.error('服务异常，请稍后重试')
    } finally { loading.value = false }
  })
}
async function handleDev(role) {
  devLoading.value = true
  console.log('[devLogin] 开始请求, role=', role)
  try {
    const ok = await userStore.devLogin(role)
    console.log('[devLogin] 结果:', ok)
    if (ok) {
      ElMessage.success('已登录')
      router.push(role === 'admin' ? '/dashboard' : '/portal')
    } else {
      ElMessage.error('获取Token失败，请确认后端是否启动')
    }
  } catch (e) {
    console.error('[devLogin] 异常:', e)
    ElMessage.error('登录失败：' + (e.message || '网络不通，检查后端'))
  } finally { devLoading.value = false }
}
</script>

<style scoped>
.login {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  background: #f2f2f7;
  font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  -webkit-font-smoothing: antialiased;
}

/* ====== 背景光晕 ====== */
.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.5;
  pointer-events: none;
}
.glow-1 { width: 500px; height: 500px; background: rgba(26,86,219,0.12); top: -15%; left: -10%; }
.glow-2 { width: 400px; height: 400px; background: rgba(5,150,105,0.07); bottom: -10%; right: -5%; }
.glow-3 { width: 350px; height: 350px; background: rgba(217,119,6,0.05); top: 50%; left: 50%; transform: translate(-50%, -50%); }

/* ====== 主容器 ====== */
.login-content {
  position: relative;
  z-index: 1;
  display: flex;
  min-height: 100vh;
}

/* ====== 左侧品牌 ====== */
.panel-left {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
}

.brand-inner { text-align: center; }
.brand-mark { margin-bottom: 24px; }
.brand-text h1 {
  font-size: 32px; font-weight: 700; color: #1a1a1a;
  letter-spacing: -0.03em; margin: 0;
}
.brand-text p {
  font-size: 16px; color: #555; margin: 8px 0 0;
  font-weight: 450;
}
.brand-foot { margin-top: 48px; font-size: 12px; color: #999; letter-spacing: 0.05em; }

/* ====== 右侧面板 ====== */
.panel-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
}

/* ====== 毛玻璃卡片 ====== */
.frost-card {
  width: 380px;
  background: rgba(255,255,255,0.72);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border: 1px solid rgba(255,255,255,0.6);
  border-radius: 20px;
  box-shadow:
    0 1px 2px rgba(0,0,0,0.04),
    0 4px 16px rgba(0,0,0,0.04),
    0 8px 32px rgba(0,0,0,0.06);
}

.frost-card-inner { padding: 36px 32px; }
.frost-card-inner h2 {
  font-size: 24px; font-weight: 700; color: #1a1a1a;
  margin: 0 0 28px; letter-spacing: -0.02em;
}

.field { margin-bottom: 20px; }
.field label {
  display: block; font-size: 13px; font-weight: 600;
  color: #1a1a1a; margin-bottom: 6px;
}
.submit-btn {
  width: 100%; height: 42px; border-radius: 10px;
  font-size: 15px; font-weight: 600; margin-top: 8px;
  background: #1a56db; border-color: #1a56db;
  transition: all 0.2s ease;
}
.submit-btn:hover {
  background: #1e40af; border-color: #1e40af;
  box-shadow: 0 2px 12px rgba(26,86,219,0.3);
  transform: translateY(-1px);
}

/* ====== 开发入口 ====== */
.dev-wrap { margin-top: 28px; }
.dev-divider {
  display: flex; align-items: center; gap: 12px; margin-bottom: 12px;
}
.dev-divider::before, .dev-divider::after {
  content: ''; flex: 1; height: 1px; background: rgba(0,0,0,0.06);
}
.dev-divider span { font-size: 11px; color: #bbb; font-weight: 500; text-transform: uppercase; letter-spacing: 0.05em; }
.dev-actions { display: flex; justify-content: center; align-items: center; gap: 6px; }
.dev-actions button {
  font-size: 12px; color: #aaa; background: none; border: none;
  cursor: pointer; padding: 6px 14px; border-radius: 8px;
  font-family: inherit; transition: all 0.15s;
}
.dev-actions button:hover:not(:disabled) { color: #1a56db; background: rgba(26,86,219,0.08); }
.dev-actions button:disabled { opacity: 0.4; cursor: not-allowed; }
.dev-sep { color: #ddd; font-size: 12px; }
</style>
