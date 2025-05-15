<script setup>
import { User, Lock, House } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import {frontRegisterService, frontLoginService, frontInfoService} from '@/api/frontuser.js'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token.js'
import useUserInfoStore from "@/stores/userInfo.js";
const userInfoStore = useUserInfoStore();
const isRegister = ref(false)
const router = useRouter()
const tokenStore = useTokenStore()

const registerData = ref({
  username: '',
  password: '',
  rePassword: ''
})

const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== registerData.value.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}





const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 6, max: 16, message: '请输入6-16位非空字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '请输入6-16位非空字符', trigger: 'blur' }
  ],
  rePassword: [
    { validator: checkRePassword, trigger: 'blur' }
  ]
}
// 获取用户信息
const getUserInfo = async () => {
  try {
    let result = await frontInfoService();
    userInfoStore.setInfo(result.data);
  } catch (error) {
    console.error("获取用户信息失败:", error);
  }
};
getUserInfo();

const register = async () => {
  const result = await frontRegisterService(registerData.value)
  ElMessage.success(result.msg || '注册成功')
  isRegister.value = false
  clearRegisterData()
}

const login = async () => {
  const result = await frontLoginService(registerData.value)
  ElMessage.success(result.msg || '登录成功')
  tokenStore.setToken(result.data)

    await getUserInfo() // 获取用户信息
   await router.push('/')
}

const clearRegisterData = () => {
  registerData.value = {
    username: '',
    password: '',
    rePassword: ''
  }
}
</script>

<template>
  <div class="login-container">
    <h1 class="welcome-title">欢迎登录安居屋</h1>

    <transition name="fade-slide" mode="out-in">
      <el-card class="form-box">
        <el-button
            class="home-btn"
            type="primary"
            :icon="House"
            circle
            @click="router.push('/')"
        />

        <!-- 注册表单 -->
        <el-form
            v-if="isRegister"
            key="register"
            size="large"
            :model="registerData"
            :rules="rules"
        >
          <h1 class="form-title">用户注册</h1>
          <el-form-item prop="username">
            <el-input
                v-model="registerData.username"
                :prefix-icon="User"
                placeholder="请输入用户名"
                clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="registerData.password"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                show-password
            />
          </el-form-item>
          <el-form-item prop="rePassword">
            <el-input
                v-model="registerData.rePassword"
                :prefix-icon="Lock"
                type="password"
                placeholder="确认密码"
                show-password
            />
          </el-form-item>
          <el-button
              type="primary"
              class="submit-btn"
              @click="register"
          >
            立即注册
          </el-button>
          <div class="form-footer">
            <el-link type="info" @click="isRegister = false; clearRegisterData()">
              ← 返回登录
            </el-link>
          </div>
        </el-form>

        <!-- 登录表单 -->
        <el-form
            v-else
            key="login"
            size="large"
            :model="registerData"
            :rules="rules"
        >
          <h1 class="form-title">用户登录</h1>
          <el-form-item prop="username">
            <el-input
                v-model="registerData.username"
                :prefix-icon="User"
                placeholder="请输入用户名"
                clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="registerData.password"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                show-password
            />
          </el-form-item>
          <div class="form-options">
            <el-checkbox>记住密码</el-checkbox>
            <el-link type="primary">忘记密码？</el-link>
          </div>
          <el-button
              type="primary"
              class="submit-btn"
              @click="login"
          >
            立即登录
          </el-button>
          <div class="form-footer">
            <span>没有账号？</span>
            <el-link type="primary" @click="isRegister = true; clearRegisterData()">
              立即注册 →
            </el-link>
          </div>
        </el-form>
      </el-card>
    </transition>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
  overflow: hidden;

  .welcome-title {
    color: #fff;
    font-size: 2.5rem;
    margin-bottom: 2rem;
    text-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    position: relative;
    &::after {
      content: '';
      position: absolute;
      bottom: -10px;
      left: 50%;
      transform: translateX(-50%);
      width: 80px;
      height: 3px;
      background: var(--el-color-primary);
      border-radius: 2px;
    }
  }

  .home-card {
    position: absolute;
    top: 20px;
    right: 20px;
    padding: 12px 20px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
    background: rgba(255, 255, 255, 0.1);
    border: none;
    color: #fff;
    display: flex;
    align-items: center;

    &:hover {
      background: var(--el-color-primary);
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
    }

    .el-icon {
      font-size: 18px;
      margin-right: 8px;
    }

    .text {
      font-size: 14px;
    }

    @media (max-width: 768px) {
      padding: 10px;
      .text {
        display: none;
      }
      .el-icon {
        margin-right: 0;
      }
    }
  }

  .form-box {
    width: 100%;
    max-width: 450px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.2);
    padding: 40px 30px;

    .form-title {
      text-align: center;
      margin-bottom: 2rem;
      color: #2c3e50;
      font-size: 24px;
      font-weight: 600;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        bottom: -12px;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 3px;
        background: var(--el-color-primary);
        border-radius: 2px;
      }
    }

    .el-form-item {
      margin-bottom: 22px;
    }

    .el-input {
      :deep(.el-input__wrapper) {
        border-radius: 8px;
        padding: 12px 16px;
        background: rgba(245, 247, 250, 0.8);
        transition: all 0.3s ease;

        &:hover {
          box-shadow: 0 0 0 1px var(--el-color-primary) inset;
        }
      }
    }

    .submit-btn {
      width: 100%;
      height: 48px;
      font-size: 16px;
      letter-spacing: 2px;
      border-radius: 8px;
      margin-top: 10px;
      transition: all 0.3s ease;
      background: linear-gradient(135deg, var(--el-color-primary), #409EFF);
      border: none;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
      }
    }

    .form-options {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 12px 0;
    }

    .form-footer {
      margin-top: 24px;
      text-align: center;
      color: #666;

      .el-link {
        margin-left: 8px;
      }
    }
  }
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

@media (max-width: 768px) {
  .login-container {
    padding: 20px;

    .welcome-title {
      font-size: 1.8rem;
      text-align: center;
      padding: 0 20px;
    }

    .form-box {
      padding: 30px 20px;

      .form-title {
        font-size: 20px;
      }

      .el-input {
        :deep(.el-input__wrapper) {
          padding: 10px 14px;
        }
      }

      .submit-btn {
        height: 44px;
        font-size: 15px;
      }
    }
  }
}


.home-btn {
  position: fixed;
  top: 20px;
  right: 20px;
  width: 40px;
  height: 40px;
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.2);
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 16px rgba(52, 152, 219, 0.3);
  }

  @media (max-width: 768px) {
    width: 36px;
    height: 36px;
    top: 15px;
    right: 15px;

    :deep(.el-icon) {
      font-size: 18px;
    }
  }
}
</style>