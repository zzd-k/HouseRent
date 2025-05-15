<template>
  <div class="money-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>租金收支统计</span>
          <el-button
              type="primary"
              @click="exportExcel"
              :icon="Download"
          >
            导出财务报表
          </el-button>
        </div>
      </template>

      <!-- 统计卡片 -->
      <div class="stats-grid">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">本月收入</div>
            <div class="stat-value">¥ {{ currentMonthIncome }}</div>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">本年收入</div>
            <div class="stat-value">¥ {{ yearlyIncome }}</div>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">出租房源数</div>
            <div class="stat-value">{{ rentedHouses.length }}</div>
          </div>
        </el-card>
      </div>

      <!-- 收入趋势图 -->
      <div class="chart-container">
        <h3>预计12个月收入趋势</h3>
        <div ref="chartEl" style="height: 400px;"></div>
      </div>

      <!-- 房源明细表格 -->
      <div class="table-container">
        <h3>出租房源明细</h3>
        <el-table :data="rentedHouses" border>
          <el-table-column prop="title" label="房源名称" width="180" />
          <el-table-column label="封面图" width="120">
            <template #default="{ row }">
              <img
                  :src="row.coverImg || defaultHouseImg"
                  class="house-thumb"
              />
            </template>
          </el-table-column>
          <el-table-column prop="price" label="月租金(元)" />
          <el-table-column label="年租金(元)">
            <template #default="{ row }">
              {{ row.price * 12 }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="出租日期" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import { Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { houserentListService } from '@/api/rent'
import { exportJsonToExcel } from '@/utils/exportExcel'

const defaultHouseImg = 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'

// 数据状态
const rentedHouses = ref([])
const currentMonthIncome = ref(0)
const yearlyIncome = ref(0)
const chartEl = ref(null)
let chartInstance = null

// 获取已出租房源（state="已售空"）
const fetchRentedHouses = async () => {
  try {
    const res = await houserentListService({
      state: '已售空',
      pageSize: 1000 // 获取所有已出租房源
    })
    rentedHouses.value = res.data.items
    calculateIncome()
    initChart()
  } catch (error) {
    ElMessage.error('获取数据失败')
  }
}

// 计算收入
const calculateIncome = () => {
  currentMonthIncome.value = rentedHouses.value
      .reduce((sum, house) => sum + Number(house.price), 0)

  yearlyIncome.value = currentMonthIncome.value * 12
}

// 初始化图表
const initChart = () => {
  if (!chartEl.value || rentedHouses.value.length === 0) return;

  // 1. 按月份统计租金收入（使用你的 rentedHouses 数据）
  const monthlyIncome = rentedHouses.value.reduce((acc, house) => {
    if (house.createTime) {
      const month = house.createTime.substring(0, 7); // 提取 "YYYY-MM"
      acc[month] = (acc[month] || 0) + Number(house.price);
    }
    return acc;
  }, {});

  // 2. 生成最近12个月的标签（动态适配当前日期）
  const months = Object.keys(monthlyIncome).sort();

  // 3. 映射数据到月份（无数据则为0）
  const incomeData = months.map(month => monthlyIncome[month]);

  // 4. 渲染图表
  chartInstance = echarts.init(chartEl.value);
  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>收入: <b>¥{c}</b>',
    },
    xAxis: {
      type: 'category',
      data: months,
      axisLabel: { rotate: 45 }
    },
    yAxis: { type: 'value', name: '收入 (元)' },
    series: [{
      name: '月租金收入',
      type: 'bar', // 柱状图更直观
      barWidth: '60%',
      data: incomeData,
      itemStyle: {
        color: '#3a4de9',
      },
      emphasis: {
        itemStyle: { color: '#2c3eaa' }
      }
    }]
  };
  chartInstance.setOption(option);
};

// 导出Excel
const exportExcel = () => {
  console.log('当前数据：', rentedHouses.value) // 检查数据是否存在

  const headers = ['房源名称', '月租金(元)', '年租金(元)', '出租日期']
  const data = rentedHouses.value.map(item => [
    item.title,
    item.price,
    item.price * 12,
    item.createTime || '未知日期'
  ])

  console.log('导出数据：', data) // 检查格式化后的数据

  exportJsonToExcel({
    header: headers,
    data,
    filename: `租金报表_${new Date().toLocaleDateString()}`,
    autoWidth: true
  })
}

// 响应式调整图表大小
const handleResize = () => {
  chartInstance?.resize()
}

onMounted(() => {
  fetchRentedHouses()
  window.addEventListener('resize', handleResize)
})

// 销毁图表
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
})
</script>

<style scoped>
.money-container {
  padding: 20px;

}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;

}

.stat-item {
  padding: 20px;
  text-align: center;
}

.stat-label {
  font-size: 16px;
  color: #666;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #3a4de9;
}

.chart-container {
  margin: 40px 0;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.table-container {
  margin-top: 40px;
}

.house-thumb {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}
.el-card  {
  background: #f5f5f5;
}
</style>