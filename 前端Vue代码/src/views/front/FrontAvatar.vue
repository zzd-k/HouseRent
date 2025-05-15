<script setup>

import { Plus, Upload } from '@element-plus/icons-vue'
import {ref} from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()

import useUserInfoStore  from '@/stores/userInfo';
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
</script>

<template>

    <el-card class="page-container">
      <template #header>
        <div class="header">
          <span><strong>更换头像</strong></span>
          <div class="tip-text">支持 JPG、PNG 格式，大小不超过 2MB 的图片</div>
        </div>
      </template>
      <el-row justify="center">
        <el-col :xs="24" :sm="18" :md="16" :lg="12">
          <div class="upload-wrapper">
            <el-upload
                ref="uploadRef"
                class="avatar-uploader"
                :show-file-list="false"
                :auto-upload="true"
                action="/api/upload"
                name="file"
                :headers="{'Authorization':tokenStore.token}"
                :on-success="uploadSuccess"
            >
              <div class="avatar-container">
                <img v-if="imgUrl" :src="imgUrl" class="avatar"  alt=""/>
                <img v-else :src="avatar" class="default-avatar"  alt=""/>
                <div class="hover-mask">
                  <el-icon :size="40" color="#fff"><Plus /></el-icon>
                </div>
              </div>
            </el-upload>

            <div class="button-group">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-button
                      type="primary"
                      :icon="Plus"
                      size="large"
                      @click="uploadRef.$el.querySelector('input').click()"
                      class="action-btn"
                      plain
                  >
                    选择图片
                  </el-button>
                </el-col>
                <el-col :span="12">
                  <el-button
                      type="success"
                      :icon="Upload"
                      size="large"
                      @click="updateAvatar()"
                      class="action-btn"
                      plain
                  >
                    上传头像
                  </el-button>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

</template>

<style scoped lang="scss">
.main-content {
  flex: 1;
  padding: 30px;

  .page-container {
    max-width: 800px;
    border: none;
    box-shadow: 0 2px 12px rgba(0,0,0,0.05);

    :deep(.el-card__header) {
      border-bottom: 1px solid #ebeef5;
      background: linear-gradient(45deg, #fafcff, #f6f8fb);
      span {
        font-size: 16px;
        color: #303133;
      }
    }
  }
}


// 完全保留并优化原有样式
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
      border-radius: 8px;
      border: 2px dashed #e0e3e8;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      }
    }

    .el-upload {
      border: 2px dashed var(--el-border-color);
      border-radius: 8px;
      transition: all 0.3s;

      &:hover {
        border-color: var(--el-color-primary);
        box-shadow: 0 2px 8px rgba(64,158,255,0.2);
      }
    }
  }
}

.action-btn {
  margin-top: 25px;
  padding: 12px 28px;
  font-size: 14px;
  letter-spacing: 0.5px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }

  & + & {
    margin-left: 20px;
  }
}
</style>