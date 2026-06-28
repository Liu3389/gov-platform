<template>
  <Teleport to="body">
    <Transition name="ld-fade">
      <div v-if="visible" class="ld-overlay" @click.self="cancel">
        <div class="ld-card">
          <div class="ld-icon-wrap">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#dc2626" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
              <polyline points="16 17 21 12 16 7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </div>
          <h3 class="ld-title">退出登录</h3>
          <p class="ld-desc">确定要退出当前账号吗？</p>
          <div class="ld-actions">
            <button class="ld-btn ld-btn-cancel" @click="cancel">取消</button>
            <button class="ld-btn ld-btn-confirm" @click="confirm">确定退出</button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref } from 'vue'

const visible = ref(false)
let resolvePromise = null

function show() {
  visible.value = true
  return new Promise((resolve) => {
    resolvePromise = resolve
  })
}

function confirm() {
  visible.value = false
  resolvePromise?.(true)
}

function cancel() {
  visible.value = false
  resolvePromise?.(false)
}

defineExpose({ show })
</script>

<style scoped>
.ld-overlay {
  position: fixed; inset: 0; z-index: 9999;
  display: flex; align-items: center; justify-content: center;
  background: rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(8px) saturate(120%);
  -webkit-backdrop-filter: blur(8px) saturate(120%);
}

.ld-card {
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 20px;
  padding: 40px 48px 32px;
  text-align: center;
  box-shadow:
    0 1px 2px rgba(0, 0, 0, 0.04),
    0 4px 16px rgba(0, 0, 0, 0.08),
    0 16px 48px rgba(0, 0, 0, 0.12);
  max-width: 360px; width: calc(100vw - 64px);
  animation: ld-in 0.25s cubic-bezier(0.22, 0.61, 0.36, 1);
}

@keyframes ld-in {
  from { opacity: 0; transform: scale(0.92) translateY(8px); }
  to   { opacity: 1; transform: scale(1) translateY(0); }
}

.ld-icon-wrap {
  width: 56px; height: 56px; border-radius: 50%;
  background: linear-gradient(135deg, #fef2f2, #fee2e2);
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 20px;
}

.ld-title {
  font-size: 18px; font-weight: 600; color: #1a1a1a;
  margin: 0 0 6px; letter-spacing: -0.01em;
}

.ld-desc {
  font-size: 14px; color: #888; margin: 0 0 28px;
}

.ld-actions {
  display: flex; gap: 12px;
}

.ld-btn {
  flex: 1; height: 42px; border-radius: 12px;
  font-size: 14px; font-weight: 500; cursor: pointer;
  border: none; font-family: inherit;
  transition: all 0.15s ease;
}

.ld-btn-cancel {
  background: rgba(0, 0, 0, 0.04); color: #555;
}
.ld-btn-cancel:hover {
  background: rgba(0, 0, 0, 0.08); color: #1a1a1a;
}

.ld-btn-confirm {
  background: #1a56db; color: #fff;
  box-shadow: 0 2px 8px rgba(26, 86, 219, 0.25);
}
.ld-btn-confirm:hover {
  background: #1e40af;
  box-shadow: 0 4px 14px rgba(26, 86, 219, 0.35);
  transform: translateY(-0.5px);
}

.ld-fade-enter-active { transition: opacity 0.2s ease; }
.ld-fade-leave-active { transition: opacity 0.15s ease; }
.ld-fade-enter-from,
.ld-fade-leave-to { opacity: 0; }
</style>
