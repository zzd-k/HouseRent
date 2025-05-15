<script setup>
import {
  Edit,
  Delete
} from '@element-plus/icons-vue'
import {ref} from 'vue'

// 数据列表（保持原始数据不变）
const categorys = ref([
  {
    "id": 3,
    "rentName": "美食",
    "rentAlias": "ms",
    "createTime": "2023-09-02 12:06:59",
    "updateTime": "2023-09-02 12:06:59"
  },
  {
    "id": 4,
    "rentName": "娱乐",
    "rentAlias": "yl",
    "createTime": "2023-09-02 12:08:16",
    "updateTime": "2023-09-02 12:08:16"
  },
  {
    "id": 5,
    "rentName": "军事",
    "rentAlias": "js",
    "createTime": "2023-09-02 12:08:33",
    "updateTime": "2023-09-02 12:08:33"
  }
])


import {
  rentListService ,
  rentAddService ,
  rentUpdateService ,
  rentDeleteService
} from '@/api/rent.js' // 路径修改为rent.js

// 获取分类列表
const articleCategoryList = async () => {
  let result = await rentListService()
  categorys.value = result.data
}
articleCategoryList()

const dialogVisible = ref(false)

// 添加分类数据模型
const categoryModel = ref({
  rentName: '',
  rentAlias: ''
})

// 表单校验规则（保持不变）
const rules = {
  rentName: [
    {required: true, message: '请输入分类名称', trigger: 'blur'}
  ],
  rentAlias: [
    {required: true, message: '请输入分类别名', trigger: 'blur'}
  ]
}

// 添加分类操作
import {ElMessage} from 'element-plus'

const addCategory = async () => {
  let result = await rentAddService(categoryModel.value)
  // 添加offset参数调整位置（单位：像素）
  ElMessage({
    type: 'success',
    message: result.msg || '添加成功',
    offset: 80  // 默认是20，这里设置为60px距离顶部
  })
  await articleCategoryList()
  dialogVisible.value = false
}

const title = ref('')

// 显示编辑弹窗（保持不变）
const showDialog = (row) => {
  dialogVisible.value = true
  title.value = '编辑分类'
  categoryModel.value.rentName = row.rentName
  categoryModel.value.rentAlias = row.rentAlias
  categoryModel.value.id = row.id
}

// 修改分类操作
const updateCategory = async () => {
  let result = await rentUpdateService(categoryModel.value)
  ElMessage.success({
    type: 'success',
    message: result.msg ?result.msg : '修改成功',
    offset: 80  // 默认是20，这里设置为60px距离顶部
  })
  articleCategoryList()
  dialogVisible.value = false
}

// 清空表单数据（保持不变）
const clearData = () => {
  categoryModel.value.rentAlias = ''
  categoryModel.value.rentName = ''
}

// 删除分类操作
import {ElMessageBox} from 'element-plus'

const deleteCategory = (row) => {
  ElMessageBox.confirm(
      '确认删除此房屋分类？',
      'Warning',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
  )
      .then(async () => {
        await rentDeleteService(row.id)

        ElMessage({
          type: 'error',
          message: '删除成功',
          offset: 80
        })
        articleCategoryList()
      })
      .catch(() => {
        ElMessage({type: 'info', message: '取消删除',offset: 80})
      })
}
</script>

<template>
  <el-card class="page-container"> 
    <template #header>
      <div class="header">
        <span><strong>房屋分类</strong></span>
        <div class="extra">
          <el-button type="primary" @click="dialogVisible=true;title='添加分类';clearData()">添加分类</el-button>
        </div>
      </div>
    </template>

    <el-table :data="categorys" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"></el-table-column>
      <el-table-column label="分类名称" prop="rentName"></el-table-column>
      <el-table-column label="分类别名" prop="rentAlias"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据"/>
      </template>
    </el-table>

    <!-- 弹窗组件（保持不变） -->
    <el-dialog v-model="dialogVisible" :title="title" width="30%">
      <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
        <el-form-item label="分类名称" prop="rentName">
          <el-input v-model="categoryModel.rentName" minlength="1" maxlength="10"></el-input>
        </el-form-item>
        <el-form-item label="分类别名" prop="rentAlias">
          <el-input v-model="categoryModel.rentAlias" minlength="1" maxlength="15"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="title=='添加分类' ? addCategory() : updateCategory()">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>