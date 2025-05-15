import request from '@/utils/frontendRequest.js'
import {ElNotification} from "element-plus";
import useUserInfoStore from "@/stores/userInfo.js";


export const rentListService = ()=>{

    return request.get('/rent')
}

export const rentAddService = (houserentData)=>{
    return request.post('/rent', houserentData)
}



export const rentUpdateService = (houserentData)=>{
    return request.put('/rent', houserentData)
}


export const rentDeleteService = (id)=>{
    return request.delete ('/rent?id='+id)
}


export const houserentListService = (params)=>{
    return request.get('/houserent', {params:params})
}


export const houserentAddService = (houserentData)=>{
    return request.post('/houserent',houserentData)
}


export const houserentUpdateService = (houserentData)=>{
    return request.put('/houserent',houserentData)
}

// export const houserentDeleteService = (id)=>{
//     return request.delete('/houserent?id='+id)
// }



export const houserentDeleteService = async (id) => {
    try {
        const userInfoStore = useUserInfoStore()
        const role = userInfoStore.info.role

        const response = await request.delete('/houserent', {
            params: {
                id,
                role
            }
        })

        return response.data
    } catch (error) {
        // 如果后端返回了结构 {code, message, data}
        if (error.response && error.response.data) {
            const { message, code } = error.response.data

            // 根据返回的 message 弹出通知
            if (message) {
                // 检查错误消息是否包含“维修记录”
                if (message.includes('维修记录')) {
                    ElNotification({
                        message,
                        position: 'bottom-right',
                        type: 'error',
                        duration: 5000
                    })
                } else {
                    ElNotification.error({
                        message,
                        position: 'top-right'
                    })
                }
            }
        } else {
            // 如果没有 error.response，显示网络错误提示
            ElNotification.error({
                message: '该房屋有维修记录或者合同记录，请联系管理员进行相关操作',
                position: 'bottom-right'
            })
        }

        // 抛出错误继续处理
        throw error
    }
}




export const houserentDetailService = (id) => {
    return request.get(`/houserent/${id}`); // 对应后端@GetMapping("/{id}")
}

export const rentHouse = (houseId) => {
    // 添加参数校验
    if (!houseId || isNaN(houseId)) {
        return Promise.reject(new Error("Invalid houseId"))
    }
    return request.post(`/rent/${houseId}`) // 路径必须是数字

}

// 在rent.js中添加模拟支付成功处理
export const createRentOrder = (data) => {
    return request.post('/houserent/order', data, {
        headers: {
            'Content-Type': 'application/json' // 明确指定JSON格式
        }
    })


}

export const updateHouseState = (houseId) => {
    return request.put(`/houserent/state/${houseId}`)
}

export const getRentedHouses = () => {
    return request.get('/houserent/rented')
}
// 根据审核状态查询房源列表
export const getHouseRentListByAuditStatus = (pageNum, pageSize, auditStatus) => {
    return request.get('/houserent/audit-status', {
        params: { pageNum, pageSize, auditStatus }
    })
}

// 审核房源：管理员审核房源并更新审核状态
export const updateHouseRentAuditStatus = (id, auditStatus) => {
    return request.put(`/houserent/audit/${id}`, {
        auditStatus: auditStatus
    })
}


