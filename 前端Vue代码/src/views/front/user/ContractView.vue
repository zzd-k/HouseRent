<template>
  <div class="contract-view-container">
    <!-- 顶部导航栏 -->
    <div class="contract-header">
      <el-page-header :icon="ArrowLeft" @back="router.back()" title="返回">
        <template #content>
          <h2 class="contract-title">{{ contract.contractTitle }}</h2>
        </template>
      </el-page-header>
    </div>

    <div class="contract-content-wrapper">
      <!-- 合同基本信息卡片 -->
      <el-card class="contract-info-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="card-title">合同基本信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>

          <el-descriptions-item label="合同状态" label-class-name="description-label">
            <el-tag :type="contract.status === '已签署' ? 'success' : 'warning'">
              {{ contract.status || '待签署' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" label-class-name="description-label">{{ contract.createTime || '--' }}</el-descriptions-item>
          <el-descriptions-item label="签署时间" label-class-name="description-label">{{ contract.signedTime || '--' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 房源信息卡片 -->
      <el-card class="house-info-card" shadow="never" v-if="houseInfo.title">
        <template #header>
          <div class="card-header">
            <span class="card-title">房源信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="房东" label-class-name="description-label">
            <div class="user-info">
              <el-avatar :size="30" :src="landlordInfo.avatar" />
              <span class="user-name">{{ landlordInfo.nickname || '未知' }}</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="租客" label-class-name="description-label">
            <div class="user-info">
              <el-avatar :size="30" :src="tenantInfo.avatar" />
              <span class="user-name">{{ tenantInfo.nickname || '未知' }}</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="房源名称" label-class-name="description-label">{{ houseInfo.title }}</el-descriptions-item>
          <el-descriptions-item label="租赁价格" label-class-name="description-label">
            <span class="price">{{ houseInfo.price }}</span> 元/月
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 合同正文区域 -->
      <el-card class="contract-content-card" shadow="never">
        <template #header>
          <div class="card-header">
            <span class="card-title">合同正文</span>
          </div>
        </template>
        <div class="contract-content">
          <h3 class="contract-title">房屋租赁合同</h3>
          <div class="contract-html-content" v-html="contract.content"></div>
        </div>
      </el-card>

      <!-- 签署区域 -->
      <div class="sign-area">
        <el-card shadow="never">
          <div class="sign-status">
            <div class="sign-item">
              <el-tag :type="contract.landlordSigned ? 'success' : 'info'" size="large">
                <el-icon v-if="contract.landlordSigned"><Check /></el-icon>
                房东签署状态: {{ contract.landlordSigned ? '已签署' : '待签署' }}
              </el-tag>
            </div>
            <div class="sign-item">
              <el-tag :type="contract.tenantSigned ? 'success' : 'info'" size="large">
                <el-icon v-if="contract.tenantSigned"><Check /></el-icon>
                租客签署状态: {{ contract.tenantSigned ? '已签署' : '待签署' }}
              </el-tag>
            </div>
          </div>

          <div class="sign-buttons">
            <el-button
                v-if="userInfo.role === '房东'"
                type="primary"
                size="large"
                @click="handleSign('landlord')"
                :loading="signing"
                :disabled="contract.landlordSigned"
                class="sign-button"
            >
              <template #icon>
                <el-icon><Edit /></el-icon>
              </template>
              {{ contract.landlordSigned ? '房东已签署' : '房东签署' }}
            </el-button>

            <el-button
                v-if="userInfo.role === '租客'"
                type="primary"
                size="large"
                @click="handleSign('tenant')"
                :loading="signing"
                :disabled="contract.tenantSigned"
                class="sign-button"
            >
              <template #icon>
                <el-icon><Edit /></el-icon>
              </template>
              {{ contract.tenantSigned ? '租客已签署' : '租客签署' }}
            </el-button>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {getContractDetail, signContractById} from '@/api/contract'
import {houserentDetailService} from '@/api/rent'
import {ElMessage} from 'element-plus'
import useUserInfoStore from '@/stores/userInfo.js'
import {getFrontUserById} from '@/api/user'
import { ArrowLeft, Check, Edit } from '@element-plus/icons-vue'

const userInfoStore = useUserInfoStore()
const userInfo = ref({
  ...userInfoStore.info,
  role: userInfoStore.info.role || '租客' // 默认值
})

const route = useRoute()
const router = useRouter()
const contractId = route.params.id

const contract = ref({}) // 这里是合同数据
const houseInfo = ref({
  title: '',
  price: 0,
  state: 0
})
const tenantInfo = ref({})
const landlordInfo = ref({})
const signing = ref(false)

const fetchContractDetail = async () => {
  try {
    const res = await getContractDetail(contractId)
    if (res.code === 0) {
      contract.value = res.data
      console.log('合同详情:', contract.value)

      // 获取房源信息
      if (contract.value.houseId) {
        await fetchHouseDetail(contract.value.houseId)
      }

      // 确保获取到房东ID - 优先从合同获取，其次从房源获取
      const landlordIdToFetch = contract.value.landlordId ||
          (houseInfo.value && houseInfo.value.landlordId) ||
          (contract.value.houseInfo && contract.value.houseInfo.landlordId)

      // 确保获取到租客ID
      const tenantIdToFetch = contract.value.tenantId || userInfo.value.id

      // 根据当前用户角色处理显示
      if (userInfo.value.role === '房东') {
        landlordInfo.value = userInfo.value
        if (tenantIdToFetch) {
          await fetchTenantInfo(tenantIdToFetch)
        }
      } else {
        tenantInfo.value = userInfo.value
        if (landlordIdToFetch) {
          await fetchLandlordInfo(landlordIdToFetch)
        } else {
          console.error('未获取到房东ID')
          landlordInfo.value = {nickname: '未知房东'}
        }
      }
    }
  } catch (error) {
    console.error('获取合同详情出错:', error)
    ElMessage.error('网络错误，请重试')
  }
}

const fetchHouseDetail = async (houseId) => {
  try {
    const res = await houserentDetailService(houseId)
    if (res.code === 0) {
      houseInfo.value = res.data
    }
  } catch (error) {
    console.error('获取房源详情失败', error)
  }
}

const fetchLandlordInfo = async (landlordId) => {
  try {
    const res = await getFrontUserById(landlordId)
    if (res.code === 0) {
      landlordInfo.value = res.data
    }
  } catch (error) {
    console.error('获取房东信息失败', error)
  }
}

const fetchTenantInfo = async (tenantId) => {
  try {
    const res = await getFrontUserById(tenantId)
    if (res.code === 0) {
      tenantInfo.value = res.data
    }
  } catch (error) {
    console.error('获取租客信息失败', error)
  }
}

const handleSign = async (role) => {
  signing.value = true
  try {
    const res = await signContractById(contractId, role)
    if (res.code === 200 || res.code === 0) {
      ElMessage.success(`${role === 'tenant' ? '租客' : '房东'}签署成功`)
      await fetchContractDetail() // 重新获取合同详情
      router.back()
    } else {
      ElMessage.error(res.msg || '签署失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请重试')
  } finally {
    signing.value = false
  }
}

onMounted(() => {
  fetchContractDetail()
})
</script>

<style scoped>
.contract-view-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.contract-header {
  margin-bottom: 30px;
}

.contract-title {
  color: #303133;
  font-weight: 500;
}

.contract-content-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

:deep(.description-label) {
  width: 120px;
  font-weight: 500;
  color: #606266;
}

.contract-info-card,
.house-info-card,
.contract-content-card {
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.contract-content {
  padding: 15px;
  line-height: 1.8;
  color: #606266;
}

.contract-html-content {
  font-size: 15px;
  line-height: 1.8;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  font-weight: 500;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}

.sign-area {
  margin-top: 30px;
}

.sign-status {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 20px;
}

.sign-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.sign-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.sign-button {
  min-width: 150px;
  padding: 15px 25px;
  font-size: 16px;
}
</style>