// repair.js
import request from '@/utils/frontendRequest.js'

// 提交维修请求
export const submitRepairRequest = (repairData) => {
    return request.post('/repair/request', repairData)
}

// 获取用户维修请求列表
export const getUserRepairRequests = () => {
    return request.get('/repair/requests')
}

// 获取维修请求详情
export const getRepairRequestDetail = (id) => {
    return request.get(`/repair/request/${id}`)
}

// 更新维修状态
export function updateRepairStatus(data) {
    return request.post('/repair/updateStatus', data)
}
// 管理员获取所有维修请求
export const getAllRepairRequests = () => {
    return request.get('/repair/requests/all')
}


// 添加维修进度记录
export const addRepairProgress = (progressData) => {
    return request.post('/repair/progress', progressData)
}

// 获取维修进度记录
export const getRepairProgress = (requestId) => {
    return request.get(`/repair/progress/${requestId}`)
}

