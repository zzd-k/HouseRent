<script setup>

import FrontNav from "@/components/NavBar.vue";
import Sidebar from "@/components/profile.vue";
</script>

<template>
  <FrontNav />
  <div class="user-layout">
    <Sidebar /> <!-- 固定定位的侧边栏 -->

    <!-- 内容区容器 -->
    <div class="content-main">
      <!-- 路由出口容器 -->
      <div class="content-card">
        <router-view /> <!-- 此处渲染front-avatar组件 -->
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.user-layout {
  position: relative;

  min-height: 100vh;
}

.content-main {
  margin-left: 260px; /* 与侧边栏宽度一致 */
  padding: 24px;
  min-height: calc(100vh - 60px); /* 减去导航栏高度 */
  margin-top: 60px; /* 导航栏高度 */
}


.content-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  max-width: 1200px; /* 适当加大最大宽度 */
  margin: 0 auto;
  padding: 24px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .sidebar {
    position: static;
    width: 100%;
    top: auto;
  }

  .content-main {
    margin-left: 0;
    margin-top: 20px;
    padding: 16px;
  }

  .content-card {
    margin: 0;
    border-radius: 8px;
  }
}
</style>