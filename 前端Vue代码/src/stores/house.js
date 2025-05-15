// stores/house.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import {houserentListService, rentListService} from '@/api/rent'

export const useHouseStore = defineStore('house', () => {
    const houses = ref([])
    const loading = ref(false)
    const categories = ref([])
    const refreshHouses = async () => {
        loading.value = true
        try {
            const res = await houserentListService({pageSize: 100})
            houses.value = res.data.items
        } finally {
            loading.value = false
        }
    }
// 更新单个房屋数据
    function updateHouse(houseData) {
        houses.value[houseData.id] = houseData
    }

    // 获取单个房屋数据
    function getHouse(id) {
        return houses.value[id] || null
    }

    const listeners = ref(new Map())

    // 添加监听
    function subscribe(houseId, callback) {
        if (!listeners.value.has(houseId)) {
            listeners.value.set(houseId, [])
        }
        listeners.value.get(houseId).push(callback)
    }

    // 触发更新
    function notifyUpdate(houseId, newData) {
        const callbacks = listeners.value.get(houseId) || []
        callbacks.forEach(cb => cb(newData))
    }
    // 新增方法：加载分类数据
    const loadCategories = async () => {
        try {
            const res = await rentListService()
            categories.value = res.data
        } catch (error) {
            console.error('分类加载失败:', error)
        }
    }
    // 新增方法：根据ID获取分类名称
    const getCategoryName = (id) => {
        const category = categories.value.find(c => c.id === id)
        return category?.rentName || '未知分类'
    }

    return { houses, refreshHouses,updateHouse,getHouse,subscribe, notifyUpdate,loadCategories,getCategoryName  }
})