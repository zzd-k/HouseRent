<template>
  <el-card class="sign-container">
    <template #header>
      <div class="header">
        <span class="header-title">合同签署</span>
      </div>
    </template>

    <div class="contract-preview">
      <h2 class="contract-title">房屋租赁合同</h2>
      <div class="contract-number">合同编号: {{ contract.contractNumber }}</div>

      <div class="contract-content" v-html="formattedContent"></div>

      <div class="signature-area">
        <div class="signature-box">
          <div class="signature-label">房东签名:</div>
          <div class="signature-placeholder" v-if="!isSigned">
            <el-button type="primary" @click="simulateLandlordSign">
              模拟房东签名
            </el-button>
          </div>
          <div class="signature-value" v-else>
            <img src="@/assets/signature-sample.png" alt="房东签名" class="signature-image">
          </div>
        </div>

        <div class="signature-box">
          <div class="signature-label">租客签名:</div>
          <div class="signature-placeholder" v-if="!isSigned">
            <el-button type="primary" @click="simulateTenantSign">
              模拟租客签名
            </el-button>
          </div>
          <div class="signature-value" v-else>
            <img src="#" alt="租客签名" class="signature-image">
          </div>
        </div>
      </div>
    </div>

    <div class="action-buttons">
      <el-button
          type="success"
          size="large"
          @click="handleSignContract"
          :disabled="!bothSigned || isSigned"
          :loading="signing"
      >
        {{ isSigned ? '合同已签署' : '确认签署合同' }}
      </el-button>
      <el-button size="large" @click="goBack">返回</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getContractDetail, signContract } from '@/api/contract'

const route = useRoute()
const router = useRouter()

const contract = ref({})
const loading = ref(false)
const signing = ref(false)
const landlordSigned = ref(false)
const tenantSigned = ref(false)
const isSigned = computed(() => contract.value.status === 'signed')

const bothSigned = computed(() => landlordSigned.value && tenantSigned.value)

// 格式化合同内容
const formattedContent = computed(() => {
  if (!contract.value.content) return ''
  return contract.value.content.replace(/\n/g, '<br>')
})

// 模拟房东签名
const simulateLandlordSign = () => {
  landlordSigned.value = true
  ElMessage.success('房东已签名')
}

// 模拟租客签名
const simulateTenantSign = () => {
  tenantSigned.value = true
  ElMessage.success('租客已签名')
}

// 签署合同
const handleSignContract = async () => {
  try {
    signing.value = true
    await signContract(contract.value.id)
    ElMessage.success('合同签署成功')
    router.push({ name: 'ContractDetail', params: { id: contract.value.id } })
  } catch (error) {
    console.error('签署失败:', error)
    ElMessage.error('签署失败')
  } finally {
    signing.value = false
  }
}

// 加载合同详情
const loadContractDetail = async () => {
  try {
    loading.value = true
    const res = await getContractDetail(route.params.id)
    contract.value = res.data
  } catch (error) {
    console.error('加载合同详情失败:', error)
    ElMessage.error('加载合同详情失败')
    router.push({ name: 'ContractList' })
  } finally {
    loading.value = false
  }
}

// 返回
const goBack = () => {
  router.go(-1)
}

// 初始化
onMounted(() => {
  loadContractDetail()
})
</script>

<style scoped>
.sign-container {
  max-width: 1000px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.contract-preview {
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fff;
}

.contract-title {
  text-align: center;
  margin-bottom: 10px;
}

.contract-number {
  text-align: right;
  margin-bottom: 20px;
  color: #666;
}

.contract-content {
  line-height: 1.8;
  margin-bottom: 40px;
}

.signature-area {
  display: flex;
  justify-content: space-around;
  margin-top: 60px;
}

.signature-box {
  text-align: center;
}

.signature-label {
  margin-bottom: 10px;
  font-weight: bold;
}

.signature-placeholder {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}

.signature-value {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.signature-image {
  max-height: 80px;
  max-width: 200px;
}

.action-buttons {
  margin-top: 40px;
  text-align: center;
}
</style>