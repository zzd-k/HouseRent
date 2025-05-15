<!-- PaymentSuccess.vue -->
<template>
  <div class="success-container">
    <el-result
        icon="success"
        title="支付成功"
        sub-title="您的订单已支付成功，请等待房东确认"
    >
      <template #extra>
        <el-button type="primary" @click="router.push('/')">返回首页</el-button>
        <el-button @click="router.push('/user')">查看订单</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
</script>

<style scoped>
.success-container {
  height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>