<template>
    <div class="test-page">
      <!-- 页面标题 -->
      <el-header height="60px" class="header">
        <h1>测试页面</h1>
      </el-header>
      
      <!-- 按钮组件 -->
      <div class="button-section">
        <el-button type="primary" @click="handleClick">主要按钮</el-button>
        <el-button type="success">成功按钮</el-button>
        <el-button type="warning">警告按钮</el-button>
        <el-button type="danger">危险按钮</el-button>
        <RouterLink to="home"> <el-button type="danger">←</el-button></RouterLink> 
        
      </div>
  
      <!-- 输入框组件 -->
      <div class="input-section">
        <el-input v-model="inputValue" placeholder="请输入内容"></el-input>
        <p>您输入的内容是：{{ inputValue }}</p>
      </div>
  
      <!-- 日期选择器组件 -->
      <div class="date-picker-section">
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="选择日期"
        ></el-date-picker>
        <p>您选择的日期是：{{ selectedDate }}</p>
      </div>
    </div>
  </template>
  
  <script>
import router from "@/router";
import { reactive } from "vue";

export default {
  setup() {
    const data = reactive({
        id:router.currentRoute.value.query.id,   
      inputValue: "", // 输入框的值
      selectedDate: null, // 日期选择器的值
    });

    const handleClick = () => {
      this.$message({
        message: "按钮被点击了！",
        type: "success",
      });
    };
    console.log('已经获取到id'+data.id)
    return {
      data,
      handleClick,
    };
  },
};
</script>
  
  <style scoped>
  .test-page {
    font-family: Arial, sans-serif;
    padding: 20px;
  }
  
  .header {
    background-color: #409eff;
    color: #fff;
    text-align: center;
    line-height: 60px;
    margin-bottom: 20px;
  }
  
  .button-section {
    margin-bottom: 20px;
  }
  
  .input-section {
    margin-bottom: 20px;
  }
  
  .date-picker-section {
    margin-bottom: 20px;
  }
  </style>
  