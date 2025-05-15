import request from '@/utils/frontendRequest.js'
import { ElNotification } from "element-plus";

export const getOrderDetail = (orderId) => {
    return request({
        url: `/rentOrder/detail/${orderId}`,
        method: 'get'
    })
}

// 创建租赁订单
export const createRentOrder = (data) => {
    return request.post('/rentOrder/create', data, {
        headers: {
            'Content-Type': 'application/json' // 明确指定JSON格式
        }
    })
}

// 获取租客的订单
export const getOrdersByTenant = (tenantId, params) => {
    return request.get(`/rentOrder/tenant/${tenantId}`, { params });
};

// 获取某个房源的所有租赁订单
export const getOrdersByHouse = (houseId) => {
    return request.get('/rentOrder/house/' + houseId)
}

// 获取租赁订单的列表
export const getRentOrders = (params) => {
    return request.get('/rentOrder', { params: params })
}

// 更新租赁订单状态（如：已支付、已取消等）
export const updateRentOrderStatus = (orderId, status) => {
    return request.put(`/rentOrder/status/${orderId}`, { status })
}
