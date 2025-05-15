<template>
  <!-- 根据当前路由的 meta.layout 动态切换布局 -->
  <component :is="layoutComponent">
    <!-- 路由出口 -->
    <router-view />
  </component>
</template>

<script>
import { defineComponent } from 'vue'
import { RouterView } from 'vue-router'
import FrontendLayout from '@/views/front/FrontendLayout.vue'


export default defineComponent({
  components: {
    RouterView,
    FrontendLayout
  },
  computed: {
    layoutComponent() {
      // 获取当前路由的 meta 中的布局设置
      return this.$route.meta.layout || 'div' // 默认使用 div 作为容器
    }
  },


})
</script>