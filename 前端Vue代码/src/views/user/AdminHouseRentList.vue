<template>
  <div class="audit-container">
    <!-- 标题和统计 -->
    <div class="header">
      <h2>房源审核管理</h2>
      <div class="statistics">
        <el-statistic title="待审核" :value="pendingCount" />
        <el-statistic title="已通过" :value="approvedCount" />
        <el-statistic title="已拒绝" :value="rejectedCount" />
      </div>
    </div>

    <!-- 筛选条件 -->
    <div class="filter-bar">
      <el-select v-model="filterStatus" placeholder="筛选状态" @change="loadHouseRentList">

        <el-option label="待审核" value="pending" />
        <el-option label="已通过" value="approved" />
        <el-option label="已拒绝" value="rejected" />
      </el-select>


    </div>

    <!-- 分页控制 -->
    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadHouseRentList"
          @current-change="loadHouseRentList"
      />
    </div>

    <!-- 房源列表 -->
    <div v-if="loading" class="loading">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else>
      <div v-if="houseRentList.length === 0" class="no-data">
        <el-empty description="暂无相关房源" />
      </div>

      <div
          v-for="house in houseRentList"
          :key="house.id"
          class="house-card"
          :class="{
          'approved': house.auditStatus === '已通过',
          'rejected': house.auditStatus === '已拒绝'
        }"
      >
        <div class="house-header">
          <h3>{{ house.title }}</h3>
          <div>
            <span class="price">¥{{ house.price }}/月</span>
            <el-tag :type="getStatusTagType(house.auditStatus)" size="small">
              {{ house.auditStatus }}
            </el-tag>
          </div>
        </div>

        <div class="house-content">
          <el-image
              v-if="house.coverImg"
              :src="house.coverImg"
              class="cover-img"
              :preview-src-list="[house.coverImg]"
              fit="cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>

          <div class="house-info">
            <p><el-icon><User /></el-icon> 房东: {{ house.landlordName || house.landlordId }}</p>

            <p><el-icon><Calendar /></el-icon> 提交时间: {{ formatTime(house.createTime) }}</p>
          </div>
        </div>

        <div class="audit-actions">
          <div class="audit-info" v-if="house.auditStatus !== '待审核'">
            <p>审核人: {{ userInfoStore.info.nickname|| '系统' }}</p>

          </div>

          <div class="action-buttons" v-if="house.auditStatus === '待审核'">
            <el-button
                type="success"
                @click="handleAudit(house.id, 'approved')"
                :loading="auditLoading[house.id]?.approved"
            >
              <el-icon><CircleCheck /></el-icon> 通过
            </el-button>

            <el-button
                type="danger"
                @click="handleReject(house.id)"
                :loading="auditLoading[house.id]?.rejected"
            >
              <el-icon><CircleClose /></el-icon> 拒绝
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部分页 -->
    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadHouseRentList"
          @current-change="loadHouseRentList"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import {
  getHouseRentListByAuditStatus,
  updateHouseRentAuditStatus
} from '@/api/rent.js';
import {
  User,
  Location,
  House,
  Calendar,
  CircleCheck,
  CircleClose,
  Picture,
  Search
} from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {useTokenStore} from "@/stores/token.js";
import useUserInfoStore from "@/stores/userInfo.js";
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
// 数据状态
const houseRentList = ref([]);
const loading = ref(false);
const filterStatus = ref('pending'); // 默认显示待审核
const searchKeyword = ref(''); // 搜索关键词
const auditLoading = ref({}); // 记录每个房源的审核按钮loading状态

// 分页配置
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 加载房源列表
const loadHouseRentList = async () => {
  try {
    loading.value = true;
    const statusMap = {
      'pending': '待审核',
      'approved': '已通过',
      'rejected': '已拒绝'
    };

    const status = statusMap[filterStatus.value];
    if (!status) {
      throw new Error('缺少有效的审核状态');
    }

    const response = await getHouseRentListByAuditStatus(
        pagination.value.currentPage,
        pagination.value.pageSize,
        status,
        searchKeyword.value
    );

    houseRentList.value = response.data?.items || [];
    pagination.value.total = response.data?.total || 0;
  } catch (error) {
    ElMessage.error('加载房源列表失败: ' + error.message);
  } finally {
    loading.value = false;
  }
};


// 处理审核通过操作
const handleAudit = async (houseId, action) => {
  try {
    // 设置按钮loading状态
    auditLoading.value = {
      ...auditLoading.value,
      [houseId]: { [action]: true }
    };

    await ElMessageBox.confirm('确定通过此房源审核吗？', '审核确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    // 调用API更新状态
    await updateHouseRentAuditStatus(houseId, '已通过');

    ElMessage.success('审核已通过');
    await loadHouseRentList();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败: ' + error.message);
    }
  } finally {
    // 清除loading状态
    auditLoading.value = {
      ...auditLoading.value,
      [houseId]: { [action]: false }
    };
  }
};

// 处理审核拒绝操作
const handleReject = async (houseId) => {
  try {
    auditLoading.value = {
      ...auditLoading.value,
      [houseId]: { rejected: true }
    };

    await ElMessageBox.confirm('确定拒绝此房源申请吗？', '审核确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    // 调用API更新状态
    await updateHouseRentAuditStatus(houseId, '已拒绝');

    ElMessage.success('已拒绝该房源');
    await loadHouseRentList();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败: ' + error.message);
    }
  } finally {
    auditLoading.value = {
      ...auditLoading.value,
      [houseId]: { rejected: false }
    };
  }
};

// 计算统计数量
const pendingCount = computed(() => {
  return houseRentList.value.filter(h => h.auditStatus === '待审核').length;
});

const approvedCount = computed(() => {
  return houseRentList.value.filter(h => h.auditStatus === '已通过').length;
});

const rejectedCount = computed(() => {
  return houseRentList.value.filter(h => h.auditStatus === '已拒绝').length;
});

// 辅助函数
const getStatusTagType = (status) => {
  const map = {
    '待审核': 'warning',
    '已通过': 'success',
    '已拒绝': 'danger'
  };
  return map[status] || '';
};

const formatTime = (timeString) => {
  if (!timeString) return '--';
  return new Date(timeString).toLocaleString();
};

// 初始化加载
onMounted(() => {
  loadHouseRentList();
});
</script>

<style scoped>
.audit-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.statistics {
  display: flex;
  gap: 20px;
}

.filter-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.house-card {
  border: 1px solid #eaeaea;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s;
  background-color: #fff;
}

.house-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.house-card.approved {
  border-left: 4px solid var(--el-color-success);
}

.house-card.rejected {
  border-left: 4px solid var(--el-color-danger);
}

.house-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.house-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.house-header .price {
  color: var(--el-color-primary);
  font-weight: bold;
  font-size: 16px;
  margin-right: 10px;
}

.house-content {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.cover-img {
  width: 200px;
  height: 150px;
  border-radius: 4px;
  object-fit: cover;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  color: #999;
}

.house-info {
  flex: 1;
}

.house-info p {
  margin: 8px 0;
  color: #666;
  display: flex;
  align-items: center;
}

.house-info .el-icon {
  margin-right: 8px;
  color: #999;
}

.audit-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px dashed #eee;
  padding-top: 15px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.audit-info {
  color: #666;
  font-size: 13px;
}

.audit-time {
  color: #999;
  margin: 5px 0;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>





<style scoped>
.audit-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.statistics {
  display: flex;
  gap: 20px;
}

.filter-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.pagination {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.house-card {
  border: 1px solid #eaeaea;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s;
  background-color: #fff;
}

.house-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.house-card.approved {
  border-left: 4px solid var(--el-color-success);
}

.house-card.rejected {
  border-left: 4px solid var(--el-color-danger);
}

.house-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.house-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.house-header .price {
  color: var(--el-color-primary);
  font-weight: bold;
  font-size: 16px;
  margin-right: 10px;
}

.house-content {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.cover-img {
  width: 200px;
  height: 150px;
  border-radius: 4px;
  object-fit: cover;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  color: #999;
}

.house-info {
  flex: 1;
}

.house-info p {
  margin: 8px 0;
  color: #666;
  display: flex;
  align-items: center;
}

.house-info .el-icon {
  margin-right: 8px;
  color: #999;
}

.audit-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px dashed #eee;
  padding-top: 15px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.audit-info {
  color: #666;
  font-size: 13px;
}

.audit-time {
  color: #999;
  margin: 5px 0;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>