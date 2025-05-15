<template>
  <el-card class="form-container">
    <template #header>
      <div class="header">
        <span class="header-title">{{ isEdit ? '编辑合同' : '创建合同' }}</span>
      </div>
    </template>

    <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
    >
      <el-form-item label="房源ID" prop="houseId">
        <el-input-number
            v-model="formData.houseId"
            :min="1"
            controls-position="right"
        />
      </el-form-item>

      <el-form-item label="房东ID" prop="landlordId">
        <el-input-number
            v-model="formData.landlordId"
            :min="1"
            controls-position="right"
        />
      </el-form-item>

      <el-form-item label="租客ID" prop="tenantId">
        <el-input-number
            v-model="formData.tenantId"
            :min="1"
            controls-position="right"
        />
      </el-form-item>

      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker
            v-model="formData.startDate"
            type="datetime"
            placeholder="选择开始日期"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker
            v-model="formData.endDate"
            type="datetime"
            placeholder="选择结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="租金金额" prop="rentAmount">
        <el-input-number
            v-model="formData.rentAmount"
            :min="0"
            :precision="2"
            controls-position="right"
        />
      </el-form-item>

      <el-form-item label="支付方式" prop="paymentMethod">
        <el-select v-model="formData.paymentMethod" placeholder="请选择支付方式">
          <el-option label="月付" value="monthly"></el-option>
          <el-option label="季付" value="quarterly"></el-option>
          <el-option label="半年付" value="half-yearly"></el-option>
          <el-option label="年付" value="yearly"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="合同内容" prop="content">
        <el-input
            v-model="formData.content"
            type="textarea"
            :rows="10"
            placeholder="请输入合同内容"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="goBack">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addContract, updateContract, getContractDetail } from '@/api/contract'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const isEdit = ref(false)

// 表单数据
const formData = ref({
  houseId: null,
  landlordId: null,
  tenantId: null,
  startDate: '',
  endDate: '',
  rentAmount: null,
  paymentMethod: '',
  content: ''
})

// 验证规则
const rules = {
  houseId: [{ required: true, message: '请输入房源ID', trigger: 'blur' }],
  landlordId: [{ required: true, message: '请输入房东ID', trigger: 'blur' }],
  tenantId: [{ required: true, message: '请输入租客ID', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  rentAmount: [{ required: true, message: '请输入租金金额', trigger: 'blur' }],
  paymentMethod: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
  content: [{ required: true, message: '请输入合同内容', trigger: 'blur' }]
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    if (isEdit.value) {
      await updateContract(formData.value)
      ElMessage.success('合同更新成功')
    } else {
      await addContract(formData.value)
      ElMessage.success('合同创建成功')
    }

    router.push({ name: 'ContractList' })
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请检查表单')
  }
}

// 重置表单
const resetForm = () => {
  formRef.value.resetFields()
}

// 返回列表
const goBack = () => {
  router.push({ name: 'ContractList' })
}

// 加载合同详情（编辑时）
const loadContractDetail = async (id) => {
  try {
    const res = await getContractDetail(id)
    formData.value = res.data
  } catch (error) {
    console.error('加载合同详情失败:', error)
    ElMessage.error('加载合同详情失败')
    router.push({ name: 'ContractList' })
  }
}

// 初始化
onMounted(() => {
  if (route.params.id) {
    isEdit.value = true
    loadContractDetail(route.params.id)
  }
})
</script>

<style scoped>
.form-container {
  max-width: 1000px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-input-number,
.el-select,
.el-date-editor {
  width: 100%;
}
</style>