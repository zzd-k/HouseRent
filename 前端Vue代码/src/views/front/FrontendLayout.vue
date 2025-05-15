<template>
  <div class="frontend-layout">
    <!-- 前台专属导航栏 -->
    <FrontNav />
    <!-- 内容区域 -->
    <main class="frontend-main">
      <router-view />
    </main>
    <!-- 前台页脚 -->
    <FrontFooter />
  </div>
</template>

<script setup>
import FrontNav from '@/components/NavBar.vue'
import FrontFooter from '@/components/Footer.vue'
</script>

<style scoped>
.frontend-layout {
  min-height: 100vh;
  display: flex;
  z-index: 9999;
  flex-direction: column;
}


</style>