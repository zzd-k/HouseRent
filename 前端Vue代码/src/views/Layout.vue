<script setup>
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom
} from '@element-plus/icons-vue';

import avatar from '@/assets/default.png';
import { userInfoService } from '@/api/user.js';
import useUserInfoStore from '@/stores/userInfo.js';
import { useTokenStore } from '@/stores/token';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';

const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
const router = useRouter();

// 获取用户信息
const getUserInfo = async () => {
  try {
    let result = await userInfoService();
    userInfoStore.setInfo(result.data);
  } catch (error) {
    console.error("获取用户信息失败:", error);
  }
};
getUserInfo();

// 处理用户操作命令
const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确认退出登录', '提示', {
      confirmButtonText: '退出',
      cancelButtonText: '取消',
      type: 'warning',
    })
        .then(async () => {
          tokenStore.removeToken();
          userInfoStore.removeInfo();
          router.push('/login');

          ElMessage({ type: 'success', message: '退出成功' });
        })
        .catch(() => {
          ElMessage({ type: 'info', message: '取消退出' });
        });
  } else {
    router.push(`/user/${command}`);
  }
};
</script>

<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside class="sidebar">
      <el-menu active-text-color="#ffd04b" background-color="rgba(30, 30, 30, 0.9)" text-color="#fff" router>
        <el-menu-item index="/article/category">
          <el-icon><Management /></el-icon>
          <span>房屋分类</span>
        </el-menu-item>
        <el-menu-item index="/article/manage">
          <el-icon><Promotion /></el-icon>
          <span>房屋管理</span>
        </el-menu-item>
        <el-sub-menu>
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>个人中心</span>
          </template>
          <el-menu-item index="/user/infos">
            <el-icon><User /></el-icon>
            <span>基本资料</span>
          </el-menu-item>
          <el-menu-item index="/user/avatar">
            <el-icon><Crop /></el-icon>
            <span>更换头像</span>
          </el-menu-item>
          <el-menu-item index="/user/resetPassword">
            <el-icon><EditPen /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/repair/list">
          <el-icon><Warning /></el-icon>
          <span>维修服务</span>
        </el-menu-item>
        <el-menu-item index="/house-rent/list">
          <el-icon><HomeFilled /></el-icon> <!-- 换个更合适的图标，比如房子 -->
          <span>房源审核管理</span>
        </el-menu-item>
      </el-menu>

    </el-aside>

    <!-- 右侧主区域 -->
    <el-container class="main-container">
      <!-- 头部 -->
      <el-header class="header">
        <div class="header__title">管理员:{{ userInfoStore.info.nickname ? userInfoStore.info.nickname : userInfoStore.info.username }}</div>
        <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="header__dropdown">
                        <el-avatar :src="userInfoStore.info.userPic || avatar" class="header__avatar"/>
                        <span class="header__username">
                            {{ userInfoStore.info.nickname || userInfoStore.info.username }}
                        </span>
                        <el-icon><CaretBottom /></el-icon>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
              <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
              <el-dropdown-item command="resetPassword" :icon="EditPen">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <!-- 主内容 -->
      <el-main class="content">
        <router-view></router-view>
      </el-main>

      <!-- 底部 -->
      <el-footer class="footer">2025 By ZhangYingTao</el-footer>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
/* 让整体布局铺满整个页面 */
.layout-container {
  height: 100vh;
  display: flex;
}

/* 侧边栏 */
.sidebar {
  width: 220px;
  background-color: rgba(30, 30, 30, 0.9);
  backdrop-filter: blur(10px);
  height: 100vh; /* 让侧边栏填满整个页面 */

  .el-menu {
    border-right: none;
    height: 100%;
  }

  .el-menu-item, .el-sub-menu__title {
    font-size: 16px;
    transition: background 0.3s;
  }

  .el-menu-item:hover, .el-sub-menu__title:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
}

/* 右侧主区域 */
.main-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  height: 100vh; /* 让内容区也撑满整个页面 */
}

/* 头部 */
.header {
  height: 64px;
  background: linear-gradient(90deg, #4a90e2, #007aff);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  color: white;
  font-size: 18px;
  font-weight: bold;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);

  &__title {
    font-size: 20px;
    font-weight: 600;
  }

  &__dropdown {
    display: flex;
    align-items: center;
    cursor: pointer;

    .el-icon {
      color: #fff;
      margin-left: 10px;
      transition: color 0.3s;
    }

    &:hover .el-icon {
      color: #f1f1f1;
    }
  }

  &__avatar {
    width: 35px;
    height: 35px;
    margin-right: 10px;
  }

  &__username {
    font-size: 16px;
    font-weight: 500;
    color: white;
  }
}

/* 主内容 */
.content {
  flex: 1; /* 让内容区自动填充剩余高度 */
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  margin: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  overflow-y: auto; /* 确保内容区不会撑出页面 */
}

/* 底部 */
.footer {
  height: 50px;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #666;
}
</style>
