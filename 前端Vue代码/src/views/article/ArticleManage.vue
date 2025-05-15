<script setup>
import {
  Edit,
  Delete,
  Plus,
  Money
} from '@element-plus/icons-vue'
import {ref} from 'vue'
import {useTokenStore} from '@/stores/token.js';
import {ElMessage, ElMessageBox} from 'element-plus';
import {QuillEditor} from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import {
  rentListService,
  houserentListService,
  houserentAddService,
  houserentUpdateService,
  houserentDeleteService
} from '@/api/rent.js'
import { useHouseStore } from '@/stores/house'
const houseStore = useHouseStore()
// 房屋分类数据
const categorys = ref([
  {
    "id": 3,
    "rentName": "美食",
    "categoryAlias": "my",
    "createTime": "2023-09-02 12:06:59",
    "updateTime": "2023-09-02 12:06:59"
  },
  {
    "id": 4,
    "rentName": "娱乐",
    "categoryAlias": "yl",
    "createTime": "2023-09-02 12:08:16",
    "updateTime": "2023-09-02 12:08:16"
  },
  {
    "id": 5,
    "rentName": "军事",
    "categoryAlias": "js",
    "createTime": "2023-09-02 12:08:33",
    "updateTime": "2023-09-02 12:08:33"
  }
])

// 房屋列表数据
const articles = ref([])
const houserentId = ref('')
const state = ref('')

// 分页数据
const pageNum = ref(1)
const total = ref(0)
const pageSize = ref(3)

// 表单相关
const visibleDrawer = ref(false)
const title = ref('')
const articleModel = ref({
  title: '',
  price: '', //null
  houserentId: '',
  coverImg: '',
  content: '',
  state: ''
})
const tokenStore = useTokenStore()
const formRef = ref()

// 验证规则
const rules = {
  title: [{required: true, message: '请输入标题', trigger: 'blur'}],
  price: [
    {required: true, message: '请输入价格', trigger: 'blur'},
    {
      validator: (_, value, callback) => {
        if (value < 100) callback(new Error('价格不能低于100元'))
        else callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  houserentId: [{required: true, message: '请选择分类', trigger: 'change'}],
  content: [{required: true, message: '请输入内容', trigger: 'blur'}]
}

// 分页处理
const onSizeChange = (size) => {
  pageSize.value = size
  articleList()
}

const onCurrentChange = (num) => {
  pageNum.value = num
  articleList()
}

// 获取分类列表
const articleCategoryList = async () => {
  const result = await rentListService()
  categorys.value = result.data
}

// 获取房屋列表
const articleList = async () => {
  const params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    houserentId: houserentId.value || null,
    state: state.value || null
  }

  const result = await houserentListService(params)
  articles.value = result.data.items.map(item => {
    const category = categorys.value.find(c => c.id === item.houserentId)
    return {...item, rentName: category?.rentName || ''}
  })
  total.value = result.data.total
}

// 图片上传
const uploadSuccess = (result) => {
  articleModel.value.coverImg = result.data
}

// 表单操作
const addArticle = async (clickState) => {
  await formRef.value.validate()
  articleModel.value.state = clickState
  await houserentAddService(articleModel.value)
  await houseStore.refreshHouses() // 新增同步操作
  ElMessage.success('添加成功')
  visibleDrawer.value = false
  articleList()
}

const updateArticle = async (clickState) => {
  await formRef.value.validate()
  articleModel.value.state = clickState
  await houserentUpdateService(articleModel.value)
  await houseStore.refreshHouses() // 新增同步操作
  ElMessage.success('修改成功')
  visibleDrawer.value = false
  articleList()
}

const showDrawer = (row) => {
  visibleDrawer.value = true
  title.value = '编辑房屋'
  articleModel.value = {...row}
}

const deleteArticle = (row) => {
  console.log('准备删除房屋:', row);
  ElMessageBox.confirm('确认删除该房屋？', '警告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await houserentDeleteService(row.id)
    await houseStore.refreshHouses() // 新增同步操作
    ElMessage.success('删除成功')
    articleList()
  })
}

const clearData = () => {
  articleModel.value = {
    title: '',
    price: null,
    houserentId: '',
    coverImg: '',
    content: '',
    state: ''
  }
}

const resetSearch = () => {
  houserentId.value = ''
  state.value = ''
  pageNum.value = 1
  pageSize.value = 3
  articleList()
}

// 初始化
articleCategoryList()
articleList()

// 价格输入处理
const handlePriceInput = (value) => {
  const numValue = parseInt(value.replace(/[^\d]/g, '')) || 0
  articleModel.value.price = numValue
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span class="header-title">房源列表</span>
<!--        <el-button type="primary" size="small" @click="visibleDrawer=true; title='添加房屋'; clearData()">-->
<!--          添加房屋-->
<!--        </el-button>-->
      </div>
    </template>

    <!-- 搜索区域 -->
    <el-form inline class="search-form">
      <el-form-item label="房屋分类：" class="compact-item">
        <el-select
            v-model="houserentId"
            placeholder="全部分类"
            clearable
            style="width: 160px"
        >
          <el-option
              v-for="c in categorys"
              :key="c.id"
              :label="c.rentName"
              :value="c.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="发布状态：" class="compact-item">
        <el-select
            v-model="state"
            placeholder="全部状态"
            clearable
            style="width: 120px"
        >
          <el-option label="在售" value="在售"/>
          <el-option label="已售空" value="已售空"/>
        </el-select>
      </el-form-item>

      <el-form-item class="form-btns">
        <el-button type="primary" size="small" @click="articleList">搜索</el-button>
        <el-button size="small" @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
        :data="articles"
        class="compact-table"
        stripe
        v-loading="false"
        style="width: 100%"
    >
      <el-table-column prop="title" label="房屋标题"/>
      <el-table-column label="价格" width="140">
        <template #default="{ row }">
          <div class="price-display">
            <el-icon :size="16" color="#67C23A">
              <Money/>
            </el-icon>
            <span class="price-number">¥{{ row.price }}</span>
            <span class="price-unit">/月</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="封面" width="100">
        <template #default="{ row }">
          <img :src="row.coverImg" class="cover-image">
        </template>
      </el-table-column>
      <el-table-column prop="rentName" label="分类" width="120"/>
      <el-table-column prop="createTime" label="发布时间" width="160"/>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag
              :type="row.state === '在售' ? 'success' : 'info'"
              size="small"
              effect="light"
              round
          >
            {{ row.state }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
              :icon="Edit"
              circle
              plain
              type="primary"
              size="small"
              @click="showDrawer(row)"
          />
          <el-button
              :icon="Delete"
              circle
              plain
              type="danger"
              size="small"
              @click="deleteArticle(row)"
          />
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="暂无数据" :image-size="80"/>
      </template>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[3, 5, 10]"
        :total="total"
        background
        layout="prev, pager, next, sizes, jumper"
        small
        class="custom-pagination"
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
    />

    <!-- 编辑抽屉 -->
    <el-drawer
        v-model="visibleDrawer"
        :title="title"
        direction="rtl"
        size="50%"
        destroy-on-close
    >
      <el-form
          ref="formRef"
          :model="articleModel"
          :rules="rules"
          label-width="100px"
      >
        <el-form-item label="房屋标题" prop="title">
          <el-input
              v-model="articleModel.title"
              placeholder="请输入标题"
              clearable
          />
        </el-form-item>

        <el-form-item label="房屋价格" prop="price">
          <el-input
              v-model="articleModel.price"
              placeholder="请输入价格"
              class="price-input"
              type="text"
              @input="handlePriceInput"
              clearable
          >
            <template #append>元/月</template>
          </el-input>
        </el-form-item>

        <el-form-item label="房屋分类" prop="houserentId">
          <el-select
              v-model="articleModel.houserentId"
              placeholder="请选择分类"
              clearable
          >
            <el-option
                v-for="c in categorys"
                :key="c.id"
                :label="c.rentName"
                :value="c.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="房屋封面">
          <el-upload
              class="avatar-uploader"
              :auto-upload="true"
              :show-file-list="false"
              action="/api/upload"
              name="file"
              :headers="{ Authorization: tokenStore.token }"
              :on-success="uploadSuccess"
          >
            <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar">
            <el-icon v-else class="avatar-uploader-icon">
              <Plus/>
            </el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="房屋详情" prop="content">
          <div class="editor">
            <quill-editor
                v-model:content="articleModel.content"
                theme="snow"
                contentType="html"
            />
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="title === '添加房屋' ? addArticle('在售') : updateArticle('在售')">
            在售
          </el-button>
          <el-button @click="title === '添加房屋' ? addArticle('已售空') : updateArticle('已售空')">
            已售空
          </el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </el-card>
</template>

<style lang="scss" scoped>
/* 整体卡片容器 */
.page-container {
  min-height: 100%;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  background: #ffffff;
  padding: 30px;
  margin: 30px;
  transition: background 0.3s ease;
}

/* 卡片头部 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  background: linear-gradient(135deg, #eef2f7, #dde6ef);
  border-radius: 12px;
  margin-bottom: 30px;

  .header-title {
    font-size: 24px;
    font-weight: 700;
    color: #333;
    margin: 0;
  }

  .el-button {
    font-size: 16px;
    border-radius: 8px;
    padding: 8px 20px;
  }
}

/* 搜索区域 */
.search-form {
  margin-bottom: 30px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;

  .compact-item {
    margin-right: 20px;
    margin-bottom: 15px;

    :deep(.el-form-item__label) {
      font-size: 14px;
      color: #555;
    }
  }

  .form-btns {
    display: flex;
    align-items: center;

    .el-button {
      margin-right: 10px;
      padding: 6px 18px;
      font-size: 14px;
      border-radius: 8px;
    }
  }
}

/* 表格样式 */
.compact-table {
  :deep(th.el-table__cell) {
    background: #f0f5f9 !important;
    padding: 14px 10px;
    font-weight: 600;
    font-size: 14px;
    color: #333;
  }

  :deep(td.el-table__cell) {
    padding: 12px 10px;
    font-size: 14px;
    color: #555;
  }

  .cover-image {
    width: 60px;
    height: 60px;
    border-radius: 8px;
    object-fit: cover;
    border: 1px solid #dfe6ec;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    cursor: pointer;

    &:hover {
      transform: scale(1.5);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      z-index: 10;
    }
  }
}

/* 价格显示样式 */
.price-display {
  display: flex;
  align-items: center;

  .price-number {
    color: #e67e22;
    margin-left: 6px;
    font-size: 16px;
    font-weight: 600;
  }

  .price-unit {
    color: #7f8c8d;
    font-size: 12px;
    margin-left: 4px;
  }
}

/* 分页 */
.custom-pagination {
  margin-top: 30px;
  text-align: right;

  :deep(.btn-prev),
  :deep(.btn-next),
  :deep(.number) {
    min-width: 36px;
    height: 36px;
    border-radius: 8px;
  }
}

/* 编辑器样式 */
.editor {
  border: 1px solid #dfe6ec;
  border-radius: 8px;
  overflow: hidden;

  :deep(.ql-toolbar) {
    border: none;
    border-bottom: 1px solid #dfe6ec;
    background: #f8fafc;
  }

  :deep(.ql-container) {
    border: none;
    min-height: 220px;
  }
}

/* 头像上传器 */
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed #c8d4e3;
    border-radius: 12px;
    cursor: pointer;
    transition: border-color 0.3s;

    &:hover {
      border-color: #409eff;
    }
  }

  .avatar {
    width: 160px;
    height: 160px;
    object-fit: cover;
    border-radius: 12px;
  }

  .avatar-uploader-icon {
    width: 160px;
    height: 160px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #8c939d;
    font-size: 32px;
    background: #f8fafc;
  }
}

/* 价格输入框 */
.price-input {
  width: 220px;

  :deep(input::-webkit-outer-spin-button),
  :deep(input::-webkit-inner-spin-button) {
    -webkit-appearance: none;
    margin: 0;
  }

  :deep(input[type="number"]) {
    -moz-appearance: textfield;
  }
}

/* 抽屉内按钮组 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;

  .el-button {
    margin-left: 12px;
    padding: 10px 24px;
    font-size: 14px;
    border-radius: 8px;
  }
}
</style>
