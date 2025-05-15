<template>
  <div class="order-container">
    <!-- 主内容卡片 -->
    <div class="order-card">
      <h2 class="order-title">订单支付</h2>
      <el-button
          class="home-btn"
          type="primary"
          :icon="House"
          circle
          @click="router.push('/')"
      />

      <!-- 订单概要 -->
      <div class="order-summary">
        <div class="summary-item">
          <span class="label">订单编号：</span>
          <span class="value">{{ orderId }}</span>
        </div>
        <div class="summary-item">
          <span class="label">租赁房源：</span>
          <span class="value">{{ houseTitle }}</span>
        </div>
      </div>

      <!-- 支付方式 -->
      <div class="payment-methods">
        <h3 class="section-title">选择支付方式</h3>
        <el-radio-group v-model="paymentMethod">
          <el-radio label="alipay">
            <img src="https://s21.ax1x.com/2025/05/11/pEOTBmq.png" class="payment-icon" />
            支付宝支付
          </el-radio>
          <el-radio label="wechat">
            <img src="https://s21.ax1x.com/2025/05/11/pEOTd6s.png" class="payment-icon" />
            微信支付
          </el-radio>
        </el-radio-group>
      </div>

      <!-- 价格明细 -->
      <div class="price-summary">
        <div class="price-item">
          <span>租金总额：</span>
          <span class="amount">¥{{ totalPrice }}</span>
        </div>
        <div class="price-item">
          <span>押金：</span>
          <span class="amount">¥{{ deposit }}</span>
        </div>
        <div class="divider"></div>
        <div class="total-price">
          <span>应付总额：</span>
          <span class="amount">¥{{ totalPrice + deposit }}</span>
        </div>
      </div>

      <!-- 支付按钮 -->
      <div class="action-buttons">
        <el-button
            type="primary"
            class="pay-button"
            @click="handlePayment"
            :loading="payLoading"
        >
          立即支付
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-btn {
  position: fixed;
  top: 30px;
  right: 420px;
  width: 40px;
  height: 40px;
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.2);
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 16px rgba(52, 152, 219, 0.3);
  }
  }
.order-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.order-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  max-width: 1000px;
  margin: 0 auto;
}

.order-title {
  color: var(--el-color-primary);
  margin-bottom: 25px;
  font-size: 24px;
}

.section-title {
  color: var(--el-text-color-primary);
  margin: 20px 0;
  font-size: 18px;
}

/* 订单概要 */
.order-summary {
  border-bottom: 1px solid var(--el-border-color);
  padding-bottom: 15px;
}

.summary-item {
  margin: 10px 0;
  font-size: 14px;
}

.label {
  color: var(--el-text-color-secondary);
}

.value {
  color: var(--el-text-color-primary);
  margin-left: 10px;
}

/* 支付方式 */
.payment-methods {
  margin-top: 25px;
}

.el-radio {
  margin-right: 30px;
}

.payment-icon {
  width: 30px;
  vertical-align: middle;
  margin-right: 10px;
}

/* 订单详情 */
.order-details {
  display: flex;
  gap: 30px;
  margin-top: 25px;
}

.detail-left {
  flex: 1;
}

.detail-item {
  margin: 15px 0;
  color: var(--el-text-color-regular);
}

/* 价格明细 */
.price-summary {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 20px;
  width: 300px;
}

.price-item {
  display: flex;
  justify-content: space-between;
  margin: 12px 0;
}

.amount {
  color: var(--el-color-primary);
  font-weight: 500;
}

.divider {
  border-top: 1px dashed var(--el-border-color);
  margin: 15px 0;
}

.total-price {
  display: flex;
  justify-content: space-between;
  font-size: 16px;
  font-weight: 600;
}

/* 支付按钮 */
.action-buttons {
  margin-top: 30px;
  text-align: center;
}

.pay-button {
  width: 200px;
  height: 45px;
  font-size: 16px;
}
</style>
<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { House } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();

// 订单数据
const orderId = ref(route.params.id); // 从路由参数获取订单ID
const houseTitle = ref(route.query.houseTitle || '未知房源');
const price = ref(Number(route.query.price) || 0);

// 支付相关状态
const paymentMethod = ref('alipay');
const payLoading = ref(false);
const totalPrice = ref(price.value * 12); // 假设租期12个月
const deposit = ref(price.value * 2);     // 押金为2个月租金

// 处理支付
const handlePayment = async () => {
  try {
    payLoading.value = true;
    // 模拟支付API请求（实际项目中替换为真实接口）
    await new Promise(resolve => setTimeout(resolve, 1500));

    ElMessage.success('支付成功！');
    // 支付成功后跳转到订单完成页（可选）
    router.push('/payment-success');
  } catch (error) {
    ElMessage.error('支付失败，请重试');
  } finally {
    payLoading.value = false;
  }
};
</script>