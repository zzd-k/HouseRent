<template>
  <div class="order-detail-container">
    <el-card class="detail-card" shadow="hover">
      <template #header>
        <div class="header">
          <span class="title">租赁订单详情</span>
          <el-button type="primary" @click="router.push('/user/orderlist')" plain round>
            返回列表
          </el-button>
        </div>
      </template>

      <div class="detail-body">
        <!-- 房屋信息 -->
        <div class="section">
          <h3 class="section-title">房屋信息</h3>
          <div class="house-info">
            <img
                :src="order.coverImg || defaultImg"
                class="house-cover"
            />
            <div class="house-meta">
              <h2>{{ order.title }}</h2>
              <p class="price">{{ order.price }} 元/月</p>
              <el-tag :type="order.state === '在售' ? 'success' : 'info'" size="small">
                {{ order.state === '在售' ? '租赁中' : '已完成' }}
              </el-tag>
            </div>
          </div>
        </div>

        <!-- 订单信息 -->
        <div class="section">
          <h3 class="section-title">订单信息</h3>
          <el-descriptions border column="2">
            <el-descriptions-item label="订单编号">{{ order.id }}</el-descriptions-item>
            <el-descriptions-item label="租赁状态">
              <el-tag :type="order.state === '在售' ? 'success' : 'info'" size="small">
                {{ order.state === '在售' ? '租赁中' : '已完成' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="租金（月）">{{ order.price }} 元</el-descriptions-item>
            <el-descriptions-item label="支付总额">{{ order.price * 12 }} 元（年付）</el-descriptions-item>
            <el-descriptions-item label="租赁起始时间">{{ order.startDate }}</el-descriptions-item>
            <el-descriptions-item label="租赁结束时间">{{ order.endDate }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script setup>
import { useRoute, useRouter } from 'vue-router';
import { getOrderDetail } from '@/api/rentOrder';
import { ElMessage } from "element-plus";
import { onMounted, ref } from "vue";

const route = useRoute();
const router = useRouter();
const order = ref({});
const defaultImg = 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg';

onMounted(async () => {
  try {
    const res = await getOrderDetail(route.params.id);
    order.value = {
      id: res.data.id,
      title: res.data.title,
      coverImg: res.data.coverImg,
      price: res.data.totalPrice,
      state: res.data.orderStatus === '已支付' ? '在售' : '已售空',
      startDate: res.data.startDate,
      endDate: res.data.endDate
    };
  } catch (error) {
    ElMessage.error('获取订单详情失败');
  }
});
</script>


<style>
.order-detail-container {
  max-width: 900px;
  margin: 40px auto;
  padding: 0 20px;
}

.detail-card {
  border-radius: 12px;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.section {
  margin-top: 30px;
}

.section-title {
  font-size: 18px;
  margin-bottom: 15px;
  font-weight: 500;
  color: #333;
}

.house-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.house-cover {
  width: 200px;
  height: 150px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.house-meta h2 {
  margin: 0 0 10px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  margin: 10px 0;
  font-size: 16px;
}

</style>