<template>
  <!-- 前台头部导航 -->
  <header class="front-header">
    <div class="container">
      <!-- 品牌LOGO -->
      <router-link to="/" class="brand">
        <el-icon class="logo"><House /></el-icon>
        <span class="name">安居客</span>
      </router-link>

      <!-- 导航菜单 -->
      <nav class="nav-menu">
        <router-link to="/rent" class="nav-item">
          <el-icon><PriceTag /></el-icon>
          <span>租房</span>
        </router-link>
        <router-link to="/login" class="nav-item">
          <el-icon><Search /></el-icon>
          <span>管理员中心</span>
        </router-link>



        <router-link v-if="!isLogin" to="/front-login" class="nav-item" >
          <el-icon><User /></el-icon>
          <span>登录/注册</span>
        </router-link>

        <!-- 登录后显示 -->
        <el-dropdown v-else trigger="hover" popper-class="user-dropdown">
          <div class="user-panel">
            <el-avatar :src="userInfoStore.info.userPic || avatar" class="header__avatar"/>
            <span class="username">  {{ userInfoStore.info.nickname || userInfoStore.info.username }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="$router.push('/user')">
                <el-icon><User /></el-icon>个人中心
              </el-dropdown-item>
              <el-dropdown-item @click="$router.push('/user/housing')" v-if="userInfoStore.info.role==='房东'">
                <el-icon><CirclePlus /></el-icon>发布房源
              </el-dropdown-item>
              <el-dropdown-item divided @click="logout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </nav>
    </div>
  </header>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {useTokenStore} from '@/stores/token'
import {frontInfoService} from "@/api/frontuser.js";
import useUserInfoStore from "@/stores/userInfo.js";
import {ElMessage} from "element-plus";
import avatar from "@/assets/default.png";
const userInfoStore = useUserInfoStore();


const router = useRouter()
const tokenStore = useTokenStore()






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

const isLogin = computed(() => tokenStore.token)

const logout = () => {
  // 1. 清除token
  tokenStore.removeToken()
  userInfoStore.removeInfo()
  // 2. 显示成功提示
  ElMessage({
    message: '退出成功',  // 提示内容
    type: 'success',     // 成功样式（绿色图标）
    duration: 2000       // 2秒后自动消失
  })

  // 3. 跳转到首页
  router.push('/')
}
</script>

<style lang="scss" scoped>

/* 新增下拉菜单样式 */
.user-dropdown {
  .el-dropdown-menu__item {
    padding: 12px 20px;
    display: flex;
    align-items: center;
    gap: 8px;

    .el-icon {
      font-size: 16px;
    }
  }

  .el-dropdown-menu__item:hover {
    color: var(--el-color-primary);
    background-color: #f5f7fa;
  }
}


.front-header {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 9999;

  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .brand {
    display: flex;
    align-items: center;
    text-decoration: none;

    .logo {
      font-size: 32px;
      color: var(--el-color-primary);
      margin-right: 8px;
    }

    .name {
      font-size: 20px;
      font-weight: 600;
      color: #333;
    }
  }

  .nav-menu {
    display: flex;
    align-items: center;
    gap: 30px;

    .nav-item {
      display: flex;
      align-items: center;
      text-decoration: none;
      color: #666;
      transition: all 0.3s;
      gap: 6px;

      &:hover {
        color: var(--el-color-primary);
        transform: translateY(-2px);
      }

      &.router-link-exact-active {
        color: var(--el-color-primary);
        font-weight: 500;
      }

      .el-icon {
        font-size: 18px;
      }
    }

    .user-panel {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 8px 12px;
      border-radius: 6px;
      transition: background 0.3s;

      &:hover {
        background: #f5f7fa;
      }

      .username {
        font-size: 14px;
        color: #606266;
      }
    }
  }
}

@media (max-width: 768px) {
  .front-header {
    .container {
      padding: 0 10px;
    }

    .brand .name {
      display: none;
    }

    .nav-menu {
      gap: 15px;

      .nav-item span {
        display: none;
      }

      .user-panel .username {
        display: none;
      }
    }
  }
}
</style>