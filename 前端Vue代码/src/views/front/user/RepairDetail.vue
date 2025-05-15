<template>
  <div class="repair-detail-container">
    <el-card class="detail-card">
      <template #header>
        <div class="header-bar">
          <el-button type="primary" @click="router.push('/user/repair')" plain>返回</el-button>
          <span class="header-title">维修请求详情</span>
          <el-tag :type="getStatusTagType(repairRequest.status)" effect="dark">
            {{ getStatusText(repairRequest.status) }}
          </el-tag>
        </div>
      </template>

      <div class="detail-content">
        <el-descriptions title="维修信息" column="2" border>
          <el-descriptions-item label="房屋名称">{{ houseTitle }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ repairRequest.title }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ repairRequest.createTime }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">{{ getStatusText(repairRequest.status) }}</el-descriptions-item>
        </el-descriptions>

        <el-divider />

        <div>
          <h3>处理进度</h3>
          <el-steps :active="getStepIndex()" align-center finish-status="success">
            <el-step title="待处理" />
            <el-step title="处理中" />
            <el-step title="已完成" />
          </el-steps>
        </div>

        <el-divider />

        <div>
          <h3>问题描述</h3>
          <el-card shadow="never">{{ repairRequest.description }}</el-card>
        </div>

        <div v-if="repairRequest.images" style="margin-top: 20px;">
          <h3>问题图片</h3>
          <div class="image-list">
            <el-image
                v-for="(img, index) in repairRequest.images.split(',')"
                :key="index"
                :src="img"
                :preview-src-list="repairRequest.images.split(',')"
                fit="cover"
                style="width: 100px; height: 100px; border-radius: 6px; margin-right: 10px;"
            />
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getRepairRequestDetail } from '@/api/repair'
import { houserentDetailService } from '@/api/rent'

const route = useRoute()
const router = useRouter()
const repairRequest = ref({})
const houseTitle = ref('')

const fetchRepairDetail = async () => {
  const id = route.params.id
  const res = await getRepairRequestDetail(id)
  repairRequest.value = res.data
  const houseRes = await houserentDetailService(res.data.houseId)
  houseTitle.value = houseRes.data.title
}

const getStatusTagType = (status) => ({
  pending: 'warning',
  processing: 'primary',
  completed: 'success'
}[status] || 'info')

const getStatusText = (status) => ({
  pending: '待处理',
  processing: '处理中',
  completed: '已完成'
}[status] || status)

const getStepIndex = () => ({
  pending: 0,
  processing: 1,
  completed: 2
}[repairRequest.value.status] || 0)

onMounted(fetchRepairDetail)
</script>

<style scoped>
.repair-detail-container {
  padding: 20px;
}
.header-bar {
  display: flex;
  align-items: center;
  gap: 15px;
}
.header-title {
  font-size: 18px;
  font-weight: bold;
  margin-right: auto;
}
.image-list {
  display: flex;
  margin-top: 10px;
}
</style>
