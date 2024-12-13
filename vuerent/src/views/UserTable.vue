<template>
  <el-container>
    <el-header>
      <h2>用户管理</h2>
    </el-header>
    <el-main>
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="id" label="ID" width="50"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script>
import { ref, onMounted } from 'vue';
import { getUsers } from '../api';

export default {
  name: 'UserTable',
  setup() {
    const users = ref([]);

    const fetchUsers = async () => {
      try {
        const response = await getUsers();
        users.value = response.data;
      } catch (error) {
        console.error('加载用户数据失败:', error);
      }
    };

    onMounted(fetchUsers);

    return {
      users,
    };
  },
};
</script>

<style scoped>
h2 {
  margin: 0;
  padding: 10px 0;
}
</style>
