<!-- RepairList.vue -->
<template>
  <div class="repair-list-container">
    <el-card class="page-container">
      <template #header>
        <div class="header">
          <!-- 管理员 -->
          <span v-if="userInfoStore.info.role === '管理员'">
      <strong>管理员维修管理</strong>
    </span>

          <!-- 房东 -->
          <span v-else-if="userInfoStore.info.role === '房东'">
      <strong>房东维修管理</strong>
    </span>
          <span v-else>
      <strong>我的维修请求</strong>
    </span>
        </div>
      </template>

      <el-table :data="repairRequests" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="房屋">
          <template #default="{ row }">
            {{ getHouseTitle(row.houseId) }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="问题标题" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewDetails(row.id)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {getAllRepairRequests, getUserRepairRequests} from '@/api/repair'
import { houserentListService } from '@/api/rent'
import useUserInfoStore  from '@/stores/userInfo.js';
import {useTokenStore} from '@/stores/token.js'
const tokenStore= useTokenStore();
const userInfoStore = useUserInfoStore();
const router = useRouter()
const repairRequests = ref([])
const loading = ref(false)
const houses = ref([])
const props = defineProps({
  basePath: {
    type: String,
    default: '/repair'
  },
  isAdmin: {
    type: Boolean,
    default: false
  }
})
// 获取维修请求列表
const fetchRepairRequests = async () => {
  try {
    loading.value = true
    const res =  props.isAdmin
        ? await getAllRepairRequests()
        : await getUserRepairRequests()
    repairRequests.value = res.data
  } catch (error) {
    ElMessage.error('获取维修请求失败')
  } finally {
    loading.value = false
  }
}

// 获取房屋列表
const fetchHouses = async () => {
  try {
    const res = await houserentListService()
    houses.value = res.data.items
  } catch (error) {
    ElMessage.error('获取房屋列表失败')
  }
}

// 获取房屋标题
const getHouseTitle = (houseId) => {
  const house = houses.value.find(h => h.id === houseId)
  return house ? house.title : '未知房屋'
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  switch (status) {
    case 'pending': return 'warning'
    case 'processing': return 'primary'
    case 'completed': return 'success'
    default: return 'info'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'pending': return '待处理'
    case 'processing': return '处理中'
    case 'completed': return '已完成'
    default: return status
  }
}

// 查看详情
const viewDetails = (id) => {
  router.push(`${props.basePath}/detail/${id}`)
}

onMounted(() => {
  fetchRepairRequests()
  fetchHouses()
})
</script>

<style scoped>
.repair-list-container {
  padding: 20px;
}
</style>