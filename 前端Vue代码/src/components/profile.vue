<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import {ref} from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()

import { useRouter } from 'vue-router'
const router = useRouter()
import useUserInfoStore  from '@/stores/userInfo.js';
import {useTokenStore} from '@/stores/token.js'
import { frontAvatarUpdateService } from '@/api/frontuser.js';
import { ElMessage } from 'element-plus';
const tokenStore= useTokenStore();
const userInfoStore = useUserInfoStore();
const imgUrl= ref(userInfoStore.info.userPic)
//用户头像地址

//图片上传成功的回调函数
const uploadSuccess = (result)=>{
  imgUrl.value = result.data;
}

const updateAvatar = async()=>{
  //调用接口
  let result = await frontAvatarUpdateService(imgUrl.value);

  ElMessage.success(result.msg ? result.msg : '修改成功')

  //修改pinia中的数据
  userInfoStore.info.userPic = imgUrl.value;

}
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

<template>

    <!-- 左侧导航栏 -->
    <div class="sidebar">
      <div class="user-panel">
        <el-avatar :src="userInfoStore.info.userPic || avatar" :size="80" class="sidebar-avatar"/>

        <div class="user-info-group">
          <div class="user-name">
            {{ userInfoStore.info.username }}
            <el-tooltip content="登录名" placement="top">
              <el-icon><User /></el-icon>
            </el-tooltip>
          </div>
          <div class="user-name">
            <span class="role-badge">{{ userInfoStore.info.role }}</span>
            <el-tooltip content="用户身份" placement="top">
              <el-icon><Lock /></el-icon>
            </el-tooltip>
          </div>
        </div>
      </div>
      <el-menu
          :default-active="router.currentRoute.value.path"
          router
          class="nav-menu"
      >


        <el-menu-item index="/user/front-avatar">
          <el-icon><Picture /></el-icon>
          <span>头像设置</span>
        </el-menu-item>

        <el-menu-item index="/user/money" v-if="userInfoStore.info.role === '房东'">
          <el-icon><Money /></el-icon>
          <span>财务</span>
        </el-menu-item>
        <el-menu-item index="/user/info">
          <el-icon><User /></el-icon>
          <span>用户信息</span>
        </el-menu-item>
        <el-menu-item index="/user/repair">
          <el-icon><Warning /></el-icon>
          <span>维修服务</span>
        </el-menu-item>
        <el-menu-item index="/user/repair/request">
          <el-icon><Warning /></el-icon>
          <span>维修服务订单申请</span>
        </el-menu-item>

        <el-menu-item index="/user/housing" v-if="userInfoStore.info.role === '房东'">
          <el-icon><House /></el-icon>
          <span>房源发布</span>
        </el-menu-item>
<!--        <el-menu-item index="/user/housingclassify" v-if="userInfoStore.info.role === '房东'">-->
<!--        <el-icon><Discount /></el-icon>-->
<!--          <span>房源类别</span>-->
<!--        </el-menu-item>-->

        <el-menu-item index="/user/frontuserreset" >
          <el-icon><Discount /></el-icon>
          <span>修改密码</span>
        </el-menu-item>


        <el-menu-item index="/user/orderList" >
          <el-icon><Notification /></el-icon>
          <span>租赁管理</span>
        </el-menu-item>

        <el-menu-item index="/user/contractlist" >
          <el-icon><Notification /></el-icon>
          <span>合同管理</span>
        </el-menu-item>


      </el-menu>

    </div>



</template>

<style lang="scss" scoped>


/* 侧边栏固定定位 */
.sidebar[data-v-25f2fec7] {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  width: 260px;
  z-index: 100;
  background: #f8f9fa;
  box-shadow: 2px 0 8px rgba(0,0,0,0.05);
}

/* 内容区自适应 */
.main-content[data-v-25f2fec7] {
  position: relative;
  z-index: 50;
  padding: 30px;
  margin-left: 0; /* 清除旧代码的margin */
}


.user-panel {
  padding: 30px 20px;
  text-align: center;
  border-bottom: 1px solid #e6ebf5;
  background: linear-gradient(145deg, #f8f9fa 0%, #ffffff 100%);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
  margin: 10px;
  border-radius: 12px;

  .user-info-group {
    margin-top: 15px;
    padding: 12px;
    background: rgba(245, 247, 250, 0.8);
    border-radius: 8px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    }
  }

  .user-name {
    font-size: 16px;
    color: #303133;
    font-weight: 600;
    margin: 8px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;

    &::before {
      content: '•';
      color: #409EFF;
      font-size: 24px;
      line-height: 1;
    }
  }

  .role-badge {
    display: inline-block;
    padding: 4px 12px;
    background: #ecf5ff;
    color: #409EFF;
    border-radius: 15px;
    font-size: 14px;
    font-weight: 500;
    border: 1px solid rgba(64, 158, 255, 0.2);
    transition: all 0.3s;
  }
}

    .nav-menu {
      border-right: none;
      padding: 10px 0;

      .el-menu-item {
        height: 50px;
        line-height: 50px;
        margin: 8px 10px;
        border-radius: 6px;
        transition: all 0.3s;

        &:hover {
          background-color: #f0f7ff;
        }

        &.is-active {
          background-color: #ecf5ff;
          color: #409EFF;

          .el-icon {
            color: #409EFF;
          }
        }

        .el-icon {
          font-size: 18px;
          margin-right: 8px;
        }
      }
    }



</style>