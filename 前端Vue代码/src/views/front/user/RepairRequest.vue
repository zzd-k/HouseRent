<template>
  <div class="repair-container">
    <el-card class="repair-card">
      <template #header>
        <div class="header">
          <span><strong>提交维修请求</strong></span>
        </div>
      </template>

      <el-form :model="repairForm" :rules="repairRules" ref="repairFormRef" label-width="100px">
        <el-form-item label="房屋" prop="houseId">
          <el-select v-model="repairForm.houseId" placeholder="请选择房屋">
            <el-option
                v-for="house in rentedHouses"
                :key="house.id"
                :label="house.title"
                :value="house.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="问题标题" prop="title">
          <el-input v-model="repairForm.title" placeholder="请输入问题标题"/>
        </el-form-item>

        <el-form-item label="问题描述" prop="description">
          <el-input
              type="textarea"
              v-model="repairForm.description"
              :rows="4"
              placeholder="请详细描述问题情况"
          />
        </el-form-item>

        <el-form-item label="上传图片">
          <el-upload
              ref="uploadRef"
              action="/api/upload"
              name="file"
              :headers="{ Authorization: tokenStore.token }"
              :auto-upload="true"
              :on-success="handleUploadSuccess"
              :on-remove="handleRemove"
              list-type="picture-card"
              :file-list="fileList"
          >
            <el-icon>
              <Plus/>
            </el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitRepair">提交维修请求</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import {Plus} from '@element-plus/icons-vue'
import {useTokenStore} from '@/stores/token.js'
import {houserentListService} from '@/api/rent'
import {submitRepairRequest} from '@/api/repair'

const tokenStore = useTokenStore()

const repairFormRef = ref()
const uploadRef = ref()
const fileList = ref([])

const repairForm = ref({
  houseId: '',
  title: '',
  description: '',
  images: ''
})

const repairRules = {
  houseId: [{required: true, message: '请选择房屋', trigger: 'change'}],
  title: [{required: true, message: '请输入问题标题', trigger: 'blur'}],
  description: [{required: true, message: '请输入问题描述', trigger: 'blur'}]
}

const rentedHouses = ref([])

const fetchRentedHouses = async () => {
  try {
    const res = await houserentListService({state: '已售空'}) // 自行调整状态
    rentedHouses.value = res.data.items
  } catch (err) {
    ElMessage.error('获取房屋列表失败')
  }
}

const handleUploadSuccess = (response, file, fileListParam) => {
  if (response.code ===0) {
    const url = response.data
    if (repairForm.value.images) {
      repairForm.value.images += ',' + url
    } else {
      repairForm.value.images = url
    }
    fileList.value = fileListParam
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const handleRemove = (file, fileListParam) => {
  const url = file.response?.data || file.url
  const updated = repairForm.value.images
      .split(',')
      .filter(item => item !== url)
      .join(',')
  repairForm.value.images = updated
  fileList.value = fileListParam
}

const submitRepair = async () => {
  await repairFormRef.value.validate()

  try {
    await submitRepairRequest(repairForm.value)
    ElMessage.success('请求维修成功')

    // 重置表单和上传组件
    repairForm.value = {
      houseId: '',
      title: '',
      description: '',
      images: ''
    }
    fileList.value = []
  } catch (err) {
    ElMessage.error('提交维修请求失败')
  }
}

onMounted(() => {
  fetchRentedHouses()
})
</script>

<style scoped>
.repair-container {
  padding: 20px;
}

.repair-card {
  max-width: 800px;
  margin: 0 auto;
}
</style>
