<template>
        <div>
                <!-- 头部 -->
                <div style="height: 60px;background-color: #3c7fff;"></div>


                <!-- 头部结束 -->
                <!-- 左侧 -->
                <div style="display: flex;">
                        <div style="width: 200px;border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
                                <el-menu router :default-active="currentPath" style="border: 0;">
                                        <el-menu-item index="/manager/home">
                                                <el-icon><House /></el-icon>
                                                系统首页
                                        </el-menu-item>
                                  <el-menu-item index="/manager/data">
                                    <el-icon><DataAnalysis /></el-icon>
                                    数据统计
                                  </el-menu-item>
                                        <el-sub-menu index="1">
                                                <template #title>
                                                        <el-icon>
                                                                <User />
                                                        </el-icon>
                                                        <span>用户管理</span>
                                                </template>
                                                <el-menu-item>管理员信息</el-menu-item>
                                                <el-menu-item>房客信息</el-menu-item>
                                        </el-sub-menu>

                                </el-menu>
                        </div>
                        <!-- 左侧end -->
                        <!-- 右侧 ---->
                        <div style="flex: 1;width: 0;background-color: #f5f7ff;padding:10px">
                                <RouterView />
                        </div>
                        <!-- 右侧结束 -->
                </div>
        </div>
</template>


<script>
import { useRoute } from "vue-router";
import router from '@/router';
import { RouterView } from 'vue-router';
export default {
  setup() {
    const route = useRoute();

    // 当前路由的路径
    const currentPath = route.path;

    return {
      currentPath,
    };
  },
};
</script>

<style></style>