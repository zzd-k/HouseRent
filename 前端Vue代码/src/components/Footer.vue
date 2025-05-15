<template>
  <footer class="footer">
    <!-- 页脚内容 -->
    <div class="footer-content">
      <!-- 三个信息区块 -->
      <div class="footer-section">
        <h3 class="footer-title">快速链接</h3>
        <ul class="footer-links">
          <li><a href="/listings">房源浏览</a></li>
          <li><a href="/how-it-works">租房指南</a></li>
          <li><a href="/agents">经纪人服务</a></li>
        </ul>
      </div>

      <!-- 支持服务区块 -->
      <div class="footer-section">
        <h3 class="footer-title">支持服务</h3>
        <ul class="footer-links">
          <li><a href="/faq">常见问题</a></li>
          <li><a href="/safety">安全指南</a></li>
          <li><a href="/contact">24小时支持</a></li>
        </ul>
      </div>

      <!-- 法律条款区块 -->
      <div class="footer-section">
        <h3 class="footer-title">法律条款</h3>
        <ul class="footer-links">
          <li><a href="/privacy">隐私政策</a></li>
          <li><a href="/terms">用户协议</a></li>
          <li><a href="/disclaimer">免责声明</a></li>
        </ul>
      </div>
    </div>

    <!-- 版权信息 -->
    <div class="copyright">
      © 2025 安居客 By ZhangYingTao
      <div class="certification">安全租房首选平台 | 认证房源</div>
    </div>

    <!-- 返回顶部按钮 -->
    <div
        class="back-to-top"
        :class="{ show: showBackToTop }"
        @click="scrollToTop"
    >
      <svg viewBox="0 0 24 24">
        <path d="M12 4l-6 6h4v6h4v-6h4l-6-6z"/>
      </svg>
    </div>
  </footer>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const showBackToTop = ref(false)

// 滚动监听逻辑
const handleScroll = () => {
  const scrollY = window.scrollY || document.documentElement.scrollTop
  const windowHeight = window.innerHeight
  const bodyHeight = document.body.clientHeight

  showBackToTop.value = scrollY > 200 ||
      (bodyHeight - scrollY - windowHeight) < 200
}

// 平滑滚动实现
const scrollToTop = () => {
  const start = window.scrollY
  const startTime = performance.now()

  const animateScroll = (timestamp) => {
    const elapsed = timestamp - startTime
    const progress = Math.min(elapsed / 500, 1)

    window.scrollTo(0, start * (1 - progress))

    if (progress < 1) {
      requestAnimationFrame(animateScroll)
    }
  }

  requestAnimationFrame(animateScroll)
}

// 生命周期钩子
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* 页脚基础样式 */
.footer {
  background-color: #f8f9fa;
  padding: 40px 20px;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  position: relative;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.footer-section {
  flex: 1;
  min-width: 250px;
  margin: 15px;
}

.footer-title {
  color: #2c3e50;
  font-size: 16px;
  margin-bottom: 15px;
  font-weight: 600;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin: 8px 0;
}

.footer-links a {
  color: #7f8c8d;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #3498db;
}

.copyright {
  text-align: center;
  margin-top: 40px;
  color: #95a5a6;
  font-size: 14px;
  border-top: 1px solid #ecf0f1;
  padding-top: 20px;
}

.certification {
  margin-top: 10px;
}

/* 返回顶部按钮 */
.back-to-top {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 40px;
  height: 40px;
  background: rgba(52, 152, 219, 0.9);
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.back-to-top.show {
  opacity: 1;
  visibility: visible;
  animation: breath 2s infinite;
}

.back-to-top svg {
  width: 20px;
  height: 20px;
  fill: white;
}

/* 呼吸动画 */
@keyframes breath {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    text-align: center;
  }

  .back-to-top {
    bottom: 20px;
    right: 20px;
    width: 36px;
    height: 36px;
  }
}
</style>