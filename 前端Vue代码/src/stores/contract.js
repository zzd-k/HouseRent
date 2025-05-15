import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
    getContractList,
    getContractsByHouse,
    getContractsByTenant
} from '@/api/contract'

export const useContractStore = defineStore('contract', () => {
    const contracts = ref([])
    const currentContract = ref(null)
    const loading = ref(false)
    const error = ref(null)

    // 获取合同列表
    const fetchContracts = async (params) => {
        loading.value = true
        try {
            const res = await getContractList(params)
            contracts.value = res.data.items
        } catch (err) {
            error.value = err.message
        } finally {
            loading.value = false
        }
    }

    // 根据房源获取合同
    const fetchByHouse = async (houseId) => {
        loading.value = true
        try {
            const res = await getContractsByHouse(houseId)
            contracts.value = res.data
        } catch (err) {
            error.value = err.message
        } finally {
            loading.value = false
        }
    }

    // 根据租客获取合同
    const fetchByTenant = async (tenantId) => {
        loading.value = true
        try {
            const res = await getContractsByTenant(tenantId)
            contracts.value = res.data
        } catch (err) {
            error.value = err.message
        } finally {
            loading.value = false
        }
    }

    return {
        contracts,
        currentContract,
        loading,
        error,
        fetchContracts,
        fetchByHouse,
        fetchByTenant
    }
})