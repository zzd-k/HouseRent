<template>
  <div class="contract-create-container">
    <el-page-header :icon="ArrowLeft" @back="router.back()" title="返回合同列表">
      <template #content>
        <h2 class="page-title">创建新合同</h2>
      </template>
    </el-page-header>

    <el-card class="form-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">合同信息填写</span>
          <el-text type="info">请填写完整的合同信息</el-text>
        </div>
      </template>

      <el-form :model="contractForm" label-width="120px" label-position="top">
        <el-row :gutter="30">
          <el-col :span="24">
            <el-form-item label="合同标题" required>
              <el-input
                  v-model="contractForm.contractTitle"
                  placeholder="例如：XX小区2023年租赁合同"
                  clearable
              />
            </el-form-item>
          </el-col>

          <el-col :md="12" :sm="24">
            <el-form-item label="选择房源" required>
              <el-select
                  v-model="contractForm.houseId"
                  placeholder="请选择要出租的房源"
                  clearable
                  filterable
                  :loading="loading"
                  style="width: 100%"
              >
                <el-option
                    v-for="house in houseList"
                    :key="house.id"
                    :label="`${house.title} (${house.price}元/月)`"
                    :value="house.id"
                >
                  <div class="house-option">

                    <div class="house-info">
                      <div class="house-title">{{ house.title }}</div>
                      <div class="house-price">{{ house.price }}元/月</div>
                    </div>
                  </div>
                </el-option>
                <template #prefix>
                  <el-icon><House /></el-icon>
                </template>
                <template #empty>
                  <el-empty description="暂无可用房源" :image-size="60" />
                </template>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :md="12" :sm="24">
            <el-form-item label="选择租客" required>
              <el-select
                  v-model="contractForm.tenantId"
                  placeholder="请选择租客"
                  :loading="tenantLoading"
                  :disabled="!contractForm.houseId"
                  style="width: 100%"
              >
                <el-option
                    v-for="tenant in tenantList"
                    :key="tenant.id"
                    :label="tenant.username"
                    :value="tenant.id"
                >
                  <div class="tenant-option">
                    <el-avatar :size="30" :src="tenant.avatar" />
                    <span class="tenant-name">{{ tenant.username }}</span>
                    <el-tag v-if="tenant.phone" size="small">{{ tenant.phone }}</el-tag>
                  </div>
                </el-option>
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
                <template #empty>
                  <el-empty description="请先选择房源" :image-size="60" />
                </template>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="合同内容" required>
              <el-input
                  type="textarea"
                  v-model="contractForm.content"
                  placeholder="请输入详细的合同条款内容"
                  :rows="10"
                  resize="none"
                  show-word-limit
                  maxlength="5000"
              />
              <div class="form-tip">
                <el-text type="info">提示：可参考标准租赁合同模板，包含租金、租期、付款方式等条款</el-text>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-actions">
          <el-button type="primary" size="large" @click="submitContract" :loading="submitting">
            <template #icon>
              <el-icon><DocumentAdd /></el-icon>
            </template>
            生成合同
          </el-button>
          <el-button size="large" @click="resetForm">
            <template #icon>
              <el-icon><Refresh /></el-icon>
            </template>
            重置表单
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, House, User, DocumentAdd, Refresh } from '@element-plus/icons-vue'
import useUserInfoStore from '@/stores/userInfo'
import { houserentListService } from '@/api/rent'
import { createContractService, getTenantListByHouseIdService } from '@/api/contract'

const userInfoStore = useUserInfoStore()
const router = useRouter()

const loading = ref(false)
const tenantLoading = ref(false)
const submitting = ref(false)

// 表单数据
const contractForm = reactive({
  contractTitle: '',
  houseId: null,
  tenantId: null,
  content: ''
})

// 房源与租客数据
const houseList = ref([])
const tenantList = ref([])
const signedTenants = ref([])

// 加载房源（根据房东ID）
const loadHouses = async () => {
  loading.value = true
  try {
    const landlordId = userInfoStore.info.id
    const res = await houserentListService({
      createUser: landlordId,
      state: '已售空',
      pageSize: 1000
    })
    houseList.value = res.data?.items || []
  } catch (err) {
    ElMessage.error('加载房源失败')
  } finally {
    loading.value = false
  }
}

// 加载租客
const loadTenants = async (houseId) => {
  if (!houseId) return tenantList.value = []
  tenantLoading.value = true
  try {
    const res = await getTenantListByHouseIdService(houseId)
    tenantList.value = res.data.filter(tenant => !signedTenants.value.includes(tenant.id))
  } catch (err) {
    ElMessage.error('加载租客失败')
    console.error('加载租客失败:', err)
  } finally {
    tenantLoading.value = false
  }
}

// 监听房源变化
watch(() => contractForm.houseId, (newVal) => {
  contractForm.tenantId = null
  loadTenants(newVal)
})

// 提交合同
const submitContract = async () => {
  if (!contractForm.contractTitle) {
    return ElMessage.warning('请输入合同标题')
  }
  if (!contractForm.houseId) {
    return ElMessage.warning('请选择房源')
  }
  if (!contractForm.tenantId) {
    return ElMessage.warning('请选择租客')
  }
  if (!contractForm.content) {
    return ElMessage.warning('请输入合同内容')
  }

  submitting.value = true
  try {
    contractForm.landlordId = userInfoStore.info.id
    const res = await createContractService(contractForm)
    if (res.code === 0) {
      ElMessage.success({
        message: '合同生成成功',
        duration: 2000,
        onClose: () => {
          router.push('/user/contractlist')
        }
      })
    } else {
      ElMessage.error(res.msg || '合同生成失败')
    }
  } catch (err) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  contractForm.contractTitle = ''
  contractForm.houseId = null
  contractForm.tenantId = null
  contractForm.content = ''
}

onMounted(() => {
  loadHouses()
})
</script>

<style scoped>
.contract-create-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.form-card {
  margin-top: 30px;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.card-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.house-option {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.house-image {
  width: 60px;
  height: 40px;
  border-radius: 4px;
  margin-right: 10px;
}

.house-info {
  display: flex;
  flex-direction: column;
}

.house-title {
  font-size: 14px;
  margin-bottom: 4px;
}

.house-price {
  font-size: 12px;
  color: #f56c6c;
}

.tenant-option {
  display: flex;
  align-items: center;
  gap: 10px;
}

.tenant-name {
  margin-right: 10px;
}

.form-tip {
  margin-top: 8px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  margin-bottom: 8px;
}
</style>