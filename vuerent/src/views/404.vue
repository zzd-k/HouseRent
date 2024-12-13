<template>
    <div class="not-found">
      <h1 class="error-code">404</h1>
      <p class="error-message">页面未找到</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </template>
  
  <script>
  import { useRouter } from "vue-router";
  
  export default {
    setup() {
      const router = useRouter();
  
      const goHome = () => {
        router.push("/");
      };
  
      return {
        goHome,
      };
    },
  };
  </script>
  
  <style scoped>
  .not-found {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
    text-align: center;
    background-color: #f5f5f5;
  }
  
  .error-code {
    font-size: 10rem;
    font-weight: bold;
    color: #409eff;
    margin: 0;
  }
  
  .error-message {
    font-size: 1.5rem;
    color: #606266;
    margin: 1rem 0 2rem;
  }
  
  .el-button {
    font-size: 1rem;
  }
  </style>
  