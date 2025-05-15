<template>
  <div class="order-list-container">
    <el-card class="page-container">
      <template #header>
        <div class="header">
          <span><strong>我的订单</strong></span>
        </div>
      </template>

      <el-table :data="orders" style="width: 100%" v-loading="loading">
        <el-table-column label="房屋封面" width="120">
          <template #default="{ row }">
            <img
                :src="row.coverImg || 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'"
                class="order-cover"
            />
          </template>
        </el-table-column>
        <el-table-column label="房屋名称" prop="title" />
        <el-table-column label="价格" width="150">
          <template #default="{ row }">
            <span class="price">{{ row.price }}元/月</span>
          </template>
        </el-table-column>
        <el-table-column label="订单状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.state === '在售' ? 'success' : 'info'">
              {{ row.state === '在售' ? '租赁中' : '已完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewDetail(row.id)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getOrdersByTenant } from '@/api/rentOrder';
import useUserInfoStore from '@/stores/userInfo'; // 引入用户信息store

const router = useRouter();
const userInfoStore = useUserInfoStore(); // 获取用户信息store实例
const orders = ref([]);
const loading = ref(false);

// 动态获取当前用户的tenantId
const tenantId = userInfoStore.info.id; // 从store中获取用户ID

const fetchOrders = async () => {
  try {
    loading.value = true;
    const res = await getOrdersByTenant(tenantId, { pageNum: 1, pageSize: 100 });

    orders.value = res.data.list.map(order => ({
      id: order.id,
      title: order.title || '未知房源', // 直接从VO获取
      coverImg: order.coverImg || '',
      price: order.totalPrice || 0,
      state: order.orderStatus === '已支付' ? '租赁中' : '已完成' // 根据订单状态显示
    }));
  } catch (error) {
    console.error('获取订单错误:', error);
    ElMessage.error('获取订单失败: ' + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false;
  }
};

const viewDetail = (orderId) => {
  router.push(`/order/detail/${orderId}`);
};

onMounted(() => {
  // 确保用户已登录
  if (!tenantId) {
    ElMessage.warning('请先登录');
    router.push('/login');
    return;
  }
  fetchOrders();
});
</script>

<style scoped>
.order-list-container {
  padding: 20px;
}

.order-cover {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}
</style>
