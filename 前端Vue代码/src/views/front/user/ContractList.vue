<template>
  <div class="app-container">
    <div class="header">
      <span><strong>合同管理</strong></span>
    </div>

    <el-form :inline="true" :model="queryParams" class="search-form">
      <el-form-item>
        <el-button
            type="primary"
            class="btn-create"
            @click="$router.push('/create')"
            v-if="userInfoStore.info.role === '房东'">
          拟定合同
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="contractList" stripe border style="width: 100%" class="contract-table">
      <el-table-column prop="contractTitle" label="合同标题" min-width="180" />
      <el-table-column label="合同签署状态" width="110">
        <template #default="{ row }">
          <el-tag :type="row.signStatus === '已签署' ? 'success' : 'info'" class="tag-status">
            {{ row.signStatus === '已签署' ? '已签署' : '未签署' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="signedTime" label="签署时间" min-width="160" />
      <el-table-column prop="createTime" label="创建时间" min-width="160" />
      <el-table-column label="操作" fixed="right" width="140">
        <template #default="{ row }">
          <el-button type="primary" link class="btn-view" @click="viewContract(row)">查看</el-button>
          <el-button type="success" link v-if="canSign(row)" class="btn-sign" @click="signContract(row)">
            签署
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="contractList.length === 0" class="no-data">暂无数据</div>

    <div class="pagination-container">
      <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :current-page="queryParams.page"
          :page-size="queryParams.pageSize"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
          class="pagination"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { getContractList, getContractsByLandlordAndTenant, getContractsByTenantId, signContractById } from '@/api/contract';
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import useUserInfoStore from '@/stores/userInfo';

const userInfoStore = useUserInfoStore();
const userId = userInfoStore.info.id;

onMounted(() => {
  if (!userInfoStore.info) {
    console.error('User info not loaded');
    return;
  }
  fetchContracts();
});

const router = useRouter();
const queryParams = reactive({
  contractTitle: '',
  signStatus: '',
  page: 1,
  pageSize: 10,
  userId: userId,
  role: userInfoStore.info.role
});

const contractList = ref([]);
const total = ref(0);

const fetchContracts = async () => {
  try {
    let res;
    const tenantId = route.query.tenantId;
    if (userInfoStore.info.role === '房东') {
      res = await getContractsByLandlordAndTenant(userId, tenantId ? Number(tenantId) : null);
    } else {
      res = await getContractsByTenantId(userId);
    }
    const data = Array.isArray(res.data) ? res.data : [];
    contractList.value = data;
    total.value = data.length;
  } catch (error) {
    console.error('请求失败:', error);
    ElMessage.error('网络错误，请重试');
  }
};

const resetQuery = () => {
  queryParams.contractTitle = '';
  queryParams.signStatus = '';
  queryParams.page = 1;
  fetchContracts();
};

const handlePageChange = (page) => {
  queryParams.page = page;
  fetchContracts();
};

const handleSizeChange = (size) => {
  queryParams.pageSize = size;
  queryParams.page = 1;
  fetchContracts();
};

const viewContract = (row) => {
  router.push(`/contractview/${row.id}`);
};

const canSign = (row) => {
  return row.landlordSignStatus === '已签署' && row.tenantSignStatus === '已签署' && row.signStatus === '未签署';
};

const signContract = async (row) => {
  if (row.landlordSignStatus === '未签署' || row.tenantSignStatus === '未签署') {
    ElMessage.warning('请确保房东和租客都已签署');
    return;
  }
  try {
    const res = await signContractById(row.id);
    if (res.code === 200) {
      ElMessage.success('合同签署成功');
      row.signStatus = '已签署';
      row.signedTime = new Date().toISOString();
    } else {
      ElMessage.error('签署失败');
    }
  } catch (error) {
    console.error('签署请求失败:', error);
    ElMessage.error('网络错误，请重试');
  }
};

const route = useRoute();
watch(
    () => route.fullPath,
    () => {
      fetchContracts();
    }
);

onMounted(() => {
  fetchContracts();
});
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.header {
  font-size: 24px;
  margin-bottom: 16px;
  color: #333;
}

.search-form {
  margin-bottom: 20px;
}

.btn-create {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
  font-size: 14px;
  padding: 8px 20px;
}

.btn-create:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.contract-table .el-table-column {
  font-size: 14px;
}

.tag-status {
  font-size: 12px;
  padding: 5px;
}

.btn-view, .btn-sign {
  font-size: 14px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.no-data {
  text-align: center;
  color: #999;
  font-size: 16px;
  margin-top: 20px;
}



</style>
