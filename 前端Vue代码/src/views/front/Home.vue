<template>
  <div class="home-page">
    <!-- 顶部轮播图 -->
    <div class="carousel-wrapper">
      <el-carousel height="360px" interval="4000" arrow="hover">
        <el-carousel-item v-for="(img, index) in carouselImages" :key="index">
          <img :src="img" class="carousel-img" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <main class="main-content">
      <!-- 如何租房 -->
      <section class="how-it-works">
        <h2 class="section-title">如何租房</h2>
        <div class="steps-wrapper">
          <div class="step-item" v-for="(step, index) in rentSteps" :key="index">
            <img :src="step.icon" class="step-icon" />
            <h3>{{ step.title }}</h3>
            <p>{{ step.desc }}</p>
          </div>
        </div>
      </section>

      <!-- 平台保障 -->
      <section class="platform-safety">
        <h2 class="section-title">平台保障</h2>
        <div class="advantage-list">
          <div class="advantage-item">
            <el-icon><House /></el-icon>
            <p>真实房源</p>
          </div>
          <div class="advantage-item">
            <el-icon><Document /></el-icon>
            <p>在线签约</p>
          </div>
          <div class="advantage-item">
            <el-icon><User /></el-icon>
            <p>专属客服</p>
          </div>
          <div class="advantage-item">
            <el-icon><Coin /></el-icon>
            <p>价格透明</p>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { House, Document, User, Coin } from '@element-plus/icons-vue'

// 顶部轮播图图片
const carouselImages = ref([
  'https://s21.ax1x.com/2025/05/08/pELIqTU.jpg',
  'https://s21.ax1x.com/2025/05/08/pELIjfJ.jpg',
  'https://s21.ax1x.com/2025/05/08/pELIzlR.jpg'
])

// 如何租房步骤
const rentSteps = ref([
  {
    icon: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
    title: '查找房源',
    desc: '通过平台查找合适房源'
  },
  {
    icon: 'https://cdn-icons-png.flaticon.com/512/847/847969.png',
    title: '在线支付',
    desc: '选好房源即可支付'
  },
  {
    icon: 'https://cdn-icons-png.flaticon.com/512/1250/1250615.png',
    title: '签订合同',
    desc: '线上线下皆可签订租赁合同'
  },
  {
    icon: 'https://cdn-icons-png.flaticon.com/512/2920/2920322.png',
    title: '拎包入住',
    desc: '完成支付，安心入住'
  }
])
</script>

<style lang="scss" scoped>
.home-page {
  background: #f8f9fa;
  padding-top: 80px;
}

.carousel-wrapper {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 16px;

  .el-carousel {
    border-radius: 12px;
    overflow: hidden;
  }

  .carousel-img {
    width: 100%;
    height: 360px;
    object-fit: cover;
    display: block;
  }
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 50px 20px;
}

.section-title {
  text-align: center;
  font-size: 24px;
  color: #333;
  margin-bottom: 30px;
}

/* 如何租房 */
.how-it-works {
  margin-bottom: 60px;

  .steps-wrapper {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 24px;
  }

  .step-item {
    flex: 1 1 200px;
    text-align: center;
    padding: 20px;

    .step-icon {
      width: 60px;
      height: 60px;
      object-fit: contain;
      margin-bottom: 10px;
    }

    h3 {
      font-size: 18px;
      margin: 10px 0 5px;
    }

    p {
      font-size: 14px;
      color: #666;
    }
  }
}

/* 平台保障 */
.platform-safety {
  .advantage-list {
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
    gap: 24px;

    .advantage-item {
      flex: 1 1 200px;
      text-align: center;
      padding: 20px;

      el-icon {
        font-size: 36px;
        color: var(--el-color-primary);
        margin-bottom: 10px;
      }

      p {
        font-size: 16px;
        margin: 0;
      }
    }
  }
}

/* 响应式支持 */
@media (max-width: 768px) {
  .carousel-wrapper {
    padding: 0 10px;

    .carousel-img {
      height: 220px;
    }
  }

  .steps-wrapper,
  .advantage-list {
    flex-direction: column;
    align-items: center;
  }

  .main-content {
    padding: 30px 10px;
  }
}
</style>
