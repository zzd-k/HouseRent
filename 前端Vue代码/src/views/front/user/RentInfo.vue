<template>
  <div class="property-detail-container">
    <!-- 头部标题与返回按钮 -->
    <div class="property-header">
      <div class="header-left">
        <h1 class="title">{{ houseDetail.title || '房子测试房子测试房子测试' }}</h1>
        <div class="status-tag">
          <el-tag type="success">{{ houseDetail.state || '在售' }}</el-tag>
          <el-tag>{{ houseDetail.type || '住宅' }}</el-tag>
        </div>
      </div>
      <el-button plain icon="ArrowLeft" @click="router.back">返回列表</el-button>
    </div>

    <!-- 主体内容 -->
    <div class="property-body">
      <!-- 左侧媒体区 -->
      <div class="media-section">
        <div class="main-image">
          <img
              :src="houseDetail.coverImg || 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'"
              alt="主图展示"
              class="property-image"
          />
        </div>
      </div>

      <!-- 右侧信息区 -->
      <div class="info-section">
        <!-- 价格区块 -->
        <div class="price-card">
          <div class="price-block">
            <div class="price-row">
              <span class="label">价格</span>
              <span class="value highlight">{{ houseDetail.price }}元/平</span>
            </div>
            <el-button
                type="primary"
                class="rent-button"
                :disabled="isRented || houseDetail.state === '已售空'"
                :loading="payLoading"
                @click="handleRent">
              {{ houseDetail.state === '已售空' ? '房屋已售空' : (isRented ? '已租赁' : '立即租赁') }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 详情内容 -->
    <div class="property-container">
      <div class="nav-bar">
        <el-menu
            mode="horizontal"
            :default-active="activeNav"
            @select="handleNavSelect"
        >
          <el-menu-item
              v-for="(nav, index) in navItems"
              :key="index"
              :index="nav.value"
              @click="activeNav = nav.value">
            {{ nav.label }}
          </el-menu-item>
        </el-menu>
      </div>
      <div class="content-section" v-html="houseDetail.content"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { houserentDetailService, createRentOrder } from '@/api/rent.js'

const route = useRoute()
const router = useRouter()

const houseDetail = ref({})
const activeNav = ref('layout')
const isRented = ref(false)
const payLoading = ref(false)

const navItems = ref([
  { label: '房屋详情', value: 'layout' },
])

onMounted(async () => {
  try {
    const result = await houserentDetailService(route.params.id)
    houseDetail.value = result.data
  } catch (error) {
    console.error('获取房源详情失败', error)
    ElMessage.error('获取房源详情失败')
  }
})

const handleRent = async () => {
  try {
    payLoading.value = true
    const result = await createRentOrder({
      houseId: Number(route.params.id),
      rentPrice: houseDetail.value.price
    })

    if (result.code === 0) {
      await router.push({
        path: `/order/${result.data.id}`,
        query: {
          houseTitle: houseDetail.value.title,
          price: houseDetail.value.price
        }
      })
    }
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '租赁失败')
  } finally {
    payLoading.value = false
  }
}
</script>

<style scoped>
.property-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 20px 40px;
}

.property-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding-bottom: 20px;
  margin-bottom: 30px;
}

.header-left {
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.status-tag {
  display: flex;
  gap: 10px;
}

.property-body {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.media-section {
  flex: 1;
  max-width: 800px;
}

.main-image {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.property-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.property-image:hover {
  transform: scale(1.02);
}

.info-section {
  flex: 0 0 350px;
}

.price-card {
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.price-block {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.label {
  font-size: 16px;
  color: #666;
}

.value.highlight {
  font-size: 24px;
  color: #f56c6c;
  font-weight: 600;
}

.rent-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
}

.nav-bar {
  margin-top: 20px;
  margin-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.content-section {
  padding: 20px 0;
  line-height: 1.6;
  color: #333;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .property-body {
    flex-direction: column;
  }

  .info-section {
    width: 100%;
  }

  .property-image {
    height: 300px;
  }

  .property-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
}
</style>
