<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userInfo.js';
const userInfoStore = useUserInfoStore();
const userInfo = ref({...userInfoStore.info})

const rules = {
  nickname: [
    { required: true, amessage: '请输入用户昵称', trigger: 'blur' },
    {
      pattern: /^\S{2,16}$/,
      message: '昵称必须是2-16位的非空字符串',
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入用户邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '必须选择用户身份', trigger: 'change' }
  ]
}


import { frontInfoUpdateService } from '@/api/frontuser.js';
import { ElMessage } from 'element-plus';
const updateUserInfo = async () => {
  try {
    // 调用接口
    let result = await frontInfoUpdateService(userInfo.value);

    // 显示带延时的成功提示（5000毫秒）
    const msg = result.msg || '修改成功';
    ElMessage.success({
      message: msg,
      duration: 5000,
      offset:80,
      grouping: true,
    });

    // 修改pinia中的信息
    userInfoStore.setInfo(userInfo.value);

  } catch (error) {
    // 建议添加错误处理
    ElMessage.error({
      message: '修改失败，请重试',
      duration: 5000,
      offset:80,
    });
  }
}

</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span><strong>基本资料</strong></span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
          <el-form-item label="登录名称">
            <el-input v-model="userInfo.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="userInfo.nickname"></el-input>
          </el-form-item>
          <el-form-item label="用户邮箱" prop="email">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="用户身份" prop="role">
            <el-radio-group v-model="userInfo.role">
              <el-radio-button label="租客">租客</el-radio-button>
              <el-radio-button label="房东">房东</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :plain="true" @click="updateUserInfo()">提交修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>