<template>
    <div class="container">
      <!-- 左侧：房屋列表 -->
      <div class="left-panel">
        <h1>房屋列表</h1>
        <ul>
          <li v-for="house in houses" :key="house.id" class="house-item">
            <h2>{{ house.title }}</h2>
            <p>地址: {{ house.address }}</p>
            <p>价格: {{ house.price }} 元</p>
            <p>面积: {{ house.area }} 平方米</p>
            <p>描述: {{ house.description }}</p>
            <p>联系人: {{ house.contact }} ({{ house.phone }})</p>
            <p>状态: {{ house.status === 0 ? '未出租' : '已出租' }}</p>
          </li>
        </ul>
      </div>
  
      <!-- 右侧：添加房屋表单 -->
      <div class="right-panel">
        <h2>添加房屋信息</h2>
        <form @submit.prevent="addHouse">
          <label for="title">标题:</label>
          <input v-model="newHouse.title" type="text" id="title" required />
  
          <label for="address">地址:</label>
          <input v-model="newHouse.address" type="text" id="address" required />
  
          <label for="price">价格:</label>
          <input v-model="newHouse.price" type="number" id="price" required />
  
          <label for="area">面积:</label>
          <input v-model="newHouse.area" type="number" id="area" required />
  
          <label for="description">描述:</label>
          <textarea v-model="newHouse.description" id="description" required></textarea>
  
          <label for="contact">联系人:</label>
          <input v-model="newHouse.contact" type="text" id="contact" required />
  
          <label for="phone">联系电话:</label>
          <input v-model="newHouse.phone" type="text" id="phone" required />
  
          <label for="status">状态:</label>
          <select v-model="newHouse.status" id="status" required>
            <option value="0">未出租</option>
            <option value="1">已出租</option>
          </select>
  
          <button type="submit">提交</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    name: "HouseManager",
    data() {
      return {
        houses: [], // 用于存储房屋数据
        newHouse: {
          title: "",
          address: "",
          price: null,
          area: null,
          description: "",
          contact: "",
          phone: "",
          status: "0",
        },
      };
    },
    created() {
      // 加载房屋列表数据
      this.fetchHouses();
    },
    methods: {
      // 获取房屋列表数据
      fetchHouses() {
        axios
          .get("http://localhost:8081/house/list")
          .then((response) => {
            this.houses = response.data;
          })
          .catch((error) => {
            console.error("获取房屋数据失败：", error);
          });
      },
      // 添加房屋信息
      addHouse() {
        console.log("即将发送的数据：", this.newHouse); // 打印数据到控制台
        axios
          .post("http://localhost:8081/house/add", this.newHouse, {
            headers: {
              "Content-Type": "application/json", // 指定为 JSON 格式
            },
          })
          .then(() => {
            alert("房屋添加成功！");
            this.fetchHouses(); // 重新加载房屋列表
            // 重置表单
            this.newHouse = {
              title: "",
              address: "",
              price: null,
              area: null,
              description: "",
              contact: "",
              phone: "",
              status: "0",
            };
          })
          .catch((error) => {
            console.error("添加房屋数据失败：", error);
            alert(`添加失败: ${error.response?.data || error.message}`);
          });
      },
    },
  };
  </script>
  
  <style scoped>
  /* 样式保持不变 */
  .container {
    display: flex;
    gap: 20px;
  }
  .left-panel {
    flex: 1;
    background-color: #f9f9f9;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
  }
  .right-panel {
    flex: 1;
    background-color: #ffffff;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
  }
  .house-item {
    margin-bottom: 20px;
    padding: 10px;
    border-bottom: 1px solid #ddd;
  }
  form label {
    display: block;
    margin-top: 10px;
  }
  form input,
  form textarea,
  form select {
    width: 100%;
    margin-top: 5px;
    padding: 5px;
    box-sizing: border-box;
  }
  form button {
    margin-top: 15px;
    padding: 10px 15px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  form button:hover {
    background-color: #0056b3;
  }
  </style>
  