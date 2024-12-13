<template>
    <div class="home-page">
      <!-- 页面标题 -->
      <el-header height="60px" class="header">
        <h1>首页</h1>
      </el-header>
      
      <!-- 搜索框 -->
      <el-main>
        <el-card style="width: 100px" shadow="always">
            <RouterLink to="test">
                <el-link :underline="false">Test页面</el-link>
            </RouterLink>

        </el-card>
        
         
             
 
  
        <!-- 房源列表 -->
        <el-row :gutter="20" class="house-list">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="house in paginatedHouses" :key="house.id">
            <el-card shadow="hover" class="house-card">
              <img :src="house.image" alt="房源图片" class="house-image" />
              <div class="house-info">
                <h3>{{ house.title }}</h3>
                <p>租金：{{ house.price }} 元/月</p>
                <p>地址：{{ house.address }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
  
        <!-- 分页 -->
        <el-pagination
          background
          layout="prev, pager, next"
           v-model:current-page.sync="currentPage"
           v-model:page-size="pageSize"
          :total="filteredHouses.length"
          @current-change="handlePageChange"
          class="pagination"
        ></el-pagination>
      </el-main>
    </div>
  </template>
  
  <script>
import router from '@/router';

  export default {
    data() {
      return {
        searchKeyword: "", // 搜索关键词
        currentPage: 1, // 当前页
        pageSize: 8, // 每页显示的房源数量
        houses: [
          // 房源数据示例
          {
            id: 1,
            title: "舒适单间",
            price: 2000,
            address: "北京市朝阳区",
            image: "https://via.placeholder.com/150",
          },
          {
            id: 2,
            title: "精装两居室",
            price: 3500,
            address: "上海市浦东新区",
            image: "https://via.placeholder.com/150",
          },
          // 添加更多房源...
        ],
      };
    },
    computed: {
      // 根据关键词过滤房源
      filteredHouses() {
        return this.houses.filter((house) =>
          house.title.includes(this.searchKeyword)
        );
      },
      // 分页后的房源
      paginatedHouses() {
        const start = (this.currentPage - 1) * this.pageSize;
        const end = start + this.pageSize;
        return this.filteredHouses.slice(start, end);
      },
    },
    methods: {
      // 搜索功能
      search() {
        this.currentPage = 1; // 搜索后重置到第一页
      },
      // 分页功能
      handlePageChange(page) {
        this.currentPage = page;
      },
    },
  };
  </script>
  
  <style scoped>
  .home-page {
    font-family: Arial, sans-serif;
  }
  
  .header {
    background-color: #409eff;
    color: #fff;
    text-align: center;
    line-height: 60px;
  }
  
  .search-box {
    width: 50%;
    margin: 20px auto;
    display: block;
  }
  
  .house-list {
    margin-top: 20px;
  }
  
  .house-card {
    text-align: center;
    padding: 10px;
  }
  
  .house-image {
    width: 100%;
    height: 150px;
    object-fit: cover;
    margin-bottom: 10px;
  }
  
  .house-info h3 {
    margin: 10px 0;
    font-size: 18px;
  }
  
  .house-info p {
    margin: 5px 0;
    font-size: 14px;
    color: #666;
  }
  
  .pagination {
    margin: 20px auto;
    text-align: center;
  }
  </style>
  