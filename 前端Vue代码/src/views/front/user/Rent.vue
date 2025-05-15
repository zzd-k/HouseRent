<template>
  <div class="home-page">
    <main class="main-content">
      <!-- 搜索区 -->
      <section class="search-section">
        <div class="search-wrapper">
          <el-input
              v-model="searchKeyword"
              placeholder="输入小区名或地址搜索房源"
              class="search-input"
              size="large"
              @keyup.enter="search"
          >
            <template #append>
              <el-button
                  type="primary"
                  @click="search"
                  size="large"
                  class="search-btn"
              >
                <el-icon><Search /></el-icon>
                立即搜索
              </el-button>
            </template>
          </el-input>
        </div>
      </section>

      <!-- 筛选条件 -->
      <div class="filter-container">
        <div class="filter-row">
          <div class="filter-group">
            <span class="filter-label">租赁方式：</span>
            <el-radio-group v-model="rentType">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="whole">可租</el-radio-button>
              <el-radio-button label="shared">已租</el-radio-button>
            </el-radio-group>
          </div>


        </div>

        <div class="filter-row">
          <div class="filter-group">
            <span class="filter-label">房屋分类：</span>
            <el-scrollbar class="category-scrollbar" v-if="categories.length > 11">
              <el-checkbox-group v-model="selectedCategories" class="checkbox-flex">
                <el-checkbox
                    v-for="c in categories"
                    :key="c.id"
                    :label="c.id"
                >
                  {{ c.rentName }}
                </el-checkbox>
              </el-checkbox-group>
            </el-scrollbar>
            <template v-else>
              <el-checkbox-group v-model="selectedCategories" class="checkbox-flex">
                <el-checkbox
                    v-for="c in categories"
                    :key="c.id"
                    :label="c.id"
                >
                  {{ c.rentName }}
                </el-checkbox>
              </el-checkbox-group>
            </template>
          </div>
        </div>
        <div class="filter-footer">
          <div class="sort-group">
            <span class="sort-label">排序：</span>
            <el-radio-group v-model="sortBy" size="large">
              <el-radio-button label="default">综合排序</el-radio-button>
              <el-radio-button label="latest">最新上架</el-radio-button>
              <el-radio-button label="price">价格从低到高</el-radio-button>
            </el-radio-group>
          </div>
          <span class="total-text">找到 {{ filteredProperties.length }} 套房源</span>
        </div>
      </div>

      <!-- 房源列表 -->
      <section class="featured-section">
        <div class="property-list">
          <el-card
              v-for="(item, index) in filteredProperties"
              :key="index"
              shadow="hover"
              class="property-card"
              @click="gotoDetail(item.id)"
          >
            <div class="card-content">
              <img :src="item.image" class="property-image" />
              <div class="property-info">
                <div class="price-header">
                  <h3 class="price">{{ item.price }}元/月</h3>

                </div>
                <p class="title">{{ item.title }}</p>

                <!-- 移除不存在的area/layout字段 -->
                <div class="meta-info">
                  <span class="date">{{ formatDate(item.date) }}</span>
                </div>

                <!-- 通过分类ID显示分类名称 -->
                <div class="tags">
                  <el-tag type="info" size="small">
                    {{ getCategoryName(item.houserentId) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import {ref, computed, watch} from 'vue'
import {
  ElRadioGroup,
  ElRadioButton,
  ElSelect,
  ElOption,
  ElCascader,
  ElCheckboxGroup,
  ElCheckbox,
  ElTag,
  ElCard,
  ElInput,
  ElButton,
  ElIcon, dayjs, ElMessage
} from 'element-plus'
import {Search} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import { useTokenStore } from '@/stores/token';
const tokenStore = useTokenStore();
const router = useRouter()
// 原有import保持不变
import {onMounted} from 'vue'
// 新增分类数据
const categories = ref([])
const selectedCategories = ref([]) // 修改变量名

// 获取分类数据
const getCategories = async () => {

  try {
    const result = await rentListService();
    console.log('分类数据:', result.data);
    categories.value = result.data;
  } catch (error) {
    console.error('获取分类失败:', error);
  }
};


// 筛选条件
const rentType = ref('all')
const selectedLayouts = ref([])
const selectedRegions = ref([])
const selectedFeatures = ref([])
const sortBy = ref('default')
const searchKeyword = ref('')
import {
  rentListService, rentAddService, rentUpdateService, rentDeleteService, houserentListService
  , houserentAddService, houserentUpdateService, houserentDeleteService
} from '@/api/rent'

const renList = async () => {
  let result = await rentListService();
  console.log(result.data);
}
renList();
const dialogVisible = ref(false);
//添加分类数据模型
const rentModel = ref({
  rentName: '',
  categoryAlias: ''
})


// 模拟数据
const featuredProperties = ref([])

const getHouses = async () => {
  const result = await houserentListService({
    pageNum: 1,
    pageSize: 100 // 按需调整分页参数
  })
  featuredProperties.value = result.data.items.map(item => ({
    id: item.id,
    houserentId: item.houserentId,
    image: item.coverImg,       // 使用封面图字段
    title: item.title,          // 直接映射标题
    price: item.price,          // 直接映射价格
    date: item.createTime,      // 创建时间作为日期
    state: item.state,          // 状态字段
    content: item.content       // 保留详情内容
  }))
}


// 过滤和排序逻辑
const filteredProperties = computed(() => {
  return featuredProperties.value.filter(property => {

    // 分类筛选
    if (selectedCategories.value.length > 0 &&
        !selectedCategories.value.includes(property.houserentId)) {
      return false
    }

    // 状态筛选代替原租赁方式
    if (rentType.value !== 'all') {
      const targetState = rentType.value === 'whole' ? '在售' : '已售空'
      if (property.state !== targetState) return false
    }
    const keyword = searchKeyword.value.toLowerCase()
    if (keyword &&
        !property.title.toLowerCase().includes(keyword) &&
        !property.content.toLowerCase().includes(keyword)
    ) return false

    return true
  }).sort((a, b) => {
    if (sortBy.value === 'price') return a.price - b.price
    if (sortBy.value === 'latest') return new Date(b.date) - new Date(a.date)
    return 0
  })
})


// 添加分类名称映射
const getCategoryName = (id) => {
  const category = categories.value.find(c => c.id === id);

  return category ? category.rentName : '其他';
};
// return categories.value.find(c => c.id === id)?.rentName || '其他'
// 日期格式化方法
const formatDate = (dateStr) => {
  return dayjs(dateStr).format('YYYY-MM-DD')
}
// 添加跳转方法
const gotoDetail = (id) => {
  // 确保传递的是有效的数字ID
  if (!id || typeof id !== 'number') {
    ElMessage.error('无效的房源信息')
    return
  }

  router.push(`/rentinfo/${id}`)
}
const search = () => {
  console.log('搜索关键词:', searchKeyword.value)
}

onMounted(() => {
  getCategories()
  getHouses() // 新增房源数据获取
})
// 监听token变化
watch(() => tokenStore.token, (newVal) => {
  if (newVal) {
    getCategories();  // 重新获取分类
    getHouses();      // 重新获取房源
  }
}, { immediate: true }); // 立即执行
</script>

<style lang="scss" scoped>
.home-page {
  background: #f8f9fa;
  min-height: 100vh;

}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.search-section {
  margin-top: 80px;
  margin-bottom: 30px;


  .search-wrapper {
    max-width: 800px;
    margin: 0 auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }

  .search-btn {
    padding: 0 32px;
    font-weight: 500;
  }

  :deep(.el-input__wrapper) {
    padding: 0 20px;
    height: 56px;
  }

  :deep(.el-input-group__append) {
    padding: 0;
    border: none;
  }
}

.filter-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 30px;
}

.filter-row {
  display: flex;
  gap: 40px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;

  &:last-child {
    border-bottom: none;
    margin-bottom: 0;
    padding-bottom: 0;
  }
}

.el-tag {
  margin-left: 8px;
  vertical-align: middle;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 12px;

  .filter-label {
    color: #666;
    font-size: 14px;
    white-space: nowrap;
  }
}

.filter-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;

  .sort-group {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .sort-label {
    color: #666;
    font-size: 14px;
  }

  .total-text {
    color: var(--el-color-primary);
    font-weight: 500;
  }
}

.el-select,
.el-cascader {
  width: 280px;
}

.el-checkbox-group {
  display: flex;
  gap: 20px;
}

.featured-section {
  .property-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 30px;
  }

  .property-card {
    border-radius: 12px;
    transition: transform 0.3s ease;
    cursor: pointer;

    &:hover {
      transform: translateY(-3px);
    }

    .property-card:hover {
      transform: translateY(-3px);
    }

    :deep(.el-card__body) {
      padding: 0;
    }
  }

  .card-content {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  .property-image {
    height: 200px;
    object-fit: cover;
  }

  .property-info {
    padding: 20px;
    flex-grow: 1;

    .price-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 12px;
    }

    .price {
      color: var(--el-color-primary);
      font-size: 24px;
      margin: 0;
      font-weight: 600;
    }

    .title {
      font-size: 16px;
      color: #333;
      margin: 0 0 8px;
      line-height: 1.4;
    }

    .meta-info {
      color: #666;
      font-size: 14px;
      margin-bottom: 12px;

      .divider {
        margin: 0 8px;
        color: #ddd;
      }
    }

    .meta-info .date {
      color: #666;
      font-size: 12px;
    }

    .tags {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }

    .property-tag {
      border-radius: 4px;
      background: #f3f4f6;
      color: #666;
    }
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 30px 15px;
  }

  .filter-row {
    flex-direction: column;
    gap: 15px;
  }

  .filter-group {
    flex-wrap: wrap;

    .el-select,
    .el-cascader {
      width: 100%;
    }
  }

  .filter-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .property-image {
    height: 180px !important;
  }
}

.category-scrollbar {
  max-height: 120px;
  overflow: hidden;
  transition: all 0.3s ease;
  padding-right: 4px;

  &:hover {
    overflow-y: auto;
  }

  ::-webkit-scrollbar {
    width: 6px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 4px;
  }
}

.checkbox-flex {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

</style>