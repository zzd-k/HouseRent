<template>
  <div class="repair-detail-container">
    <el-card class="detail-card">
      <template #header>
        <div class="header-bar">
          <el-button type="primary" @click="router.push('/repair/list')" plain>返回</el-button>
          <span class="header-title">维修请求详情（管理员）</span>
          <el-tag :type="getStatusTagType(repairRequest.status)" effect="dark">
            {{ getStatusText(repairRequest.status) }}
          </el-tag>
          <div class="header-actions">
            <el-button
                v-if="repairRequest.status === 'pending'"
                type="warning"
                @click="updateStatus('processing')"
            >开始处理</el-button>
            <el-button
                v-if="repairRequest.status === 'processing'"
                type="success"
                @click="updateStatus('completed')"
            >标记完成</el-button>
          </div>
        </div>
      </template>

      <div class="detail-content">
        <section>
          <el-descriptions title="维修信息" column="2" border>
            <el-descriptions-item label="房屋名称">{{ houseTitle }}</el-descriptions-item>
            <el-descriptions-item label="标题">{{ repairRequest.title }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ repairRequest.createTime }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ getStatusText(repairRequest.status) }}</el-descriptions-item>
          </el-descriptions>

          <el-divider />

          <div>
            <h3>问题描述</h3>
            <el-card shadow="never">{{ repairRequest.description }}</el-card>
          </div>

          <div v-if="repairRequest.images" style="margin-top: 20px;">
            <h3>图片展示</h3>
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
        </section>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getRepairRequestDetail, updateRepairStatus } from '@/api/repair'
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

const updateStatus = async (newStatus) => {
  await updateRepairStatus({ id: repairRequest.value.id, status: newStatus })
  ElMessage.success('状态更新成功')
  repairRequest.value.status = newStatus
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
.header-actions {
  display: flex;
  gap: 10px;
}
.image-list {
  display: flex;
  margin-top: 10px;
}
</style>
