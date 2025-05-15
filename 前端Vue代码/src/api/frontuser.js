

//导入request.js工具

import request from '@/utils/frontendRequest.js'
import {ElNotification} from "element-plus";


//前台注册
// export const frontRegisterService = (registerData)=>{
//     const params = new URLSearchParams()
//     for(let key in registerData){
//         params.append(key, registerData[key]);
//     }
//     return request.post('/front/register', params)
// }
export const frontRegisterService = async (registerData) => {
    try {
        const params = new URLSearchParams()
        for(let key in registerData){
            params.append(key, registerData[key])
        }
        const response = await request.post('/front/register', params)
        return response
    } catch (error) {
        // 捕获异常并显示通知
        let errorMessage = '注册失败，请稍后再试'

        // 如果有响应数据，尝试获取更详细的错误信息
        if (error.response && error.response.data) {
            errorMessage = error.response.data.message || errorMessage
        } else if (error.message) {
            errorMessage = error.message
        }

        ElNotification({
            title: '错误',
            message: errorMessage,
            type: 'error',
            position: 'bottom-right',
            duration: 5000
        })

        // 重新抛出错误，以便调用者可以进一步处理
        throw error
    }
}
//前台登录
// export const frontLoginService = (loginData)=>{
//     const params = new URLSearchParams()
//     for(let key in loginData){
//         params.append(key, loginData[key]);
//     }
//     return request.post('/front/login', params)
// }
export const frontLoginService = async (loginData) => {
    try {
        const params = new URLSearchParams()
        for(let key in loginData){
            params.append(key, loginData[key])
        }
        const response = await request.post('/front/login', params)
        return response
    } catch (error) {
        // 捕获异常并显示通知
        let errorMessage = '登录失败，请稍后再试'

        // 如果有响应数据，尝试获取更详细的错误信息
        if (error.response && error.response.data) {
            errorMessage = error.response.data.message || errorMessage
        } else if (error.message) {
            errorMessage = error.message
        }

        ElNotification({
            title: '错误',
            message: errorMessage,
            type: 'error',
            position: 'bottom-right',
            duration: 5000
        })

        // 重新抛出错误，以便调用者可以进一步处理
        throw error
    }
}
//前台获取用户信息
export const frontInfoService = ()=>{
    return request.get('/front/userInfo')

}
// 前台更新
export const frontInfoUpdateService = (userInfoData)=>{
    return request.put('/front/update', userInfoData)
}
// 前台更新头像
export const frontAvatarUpdateService = (avatarUrl)=>{
    const params  = new URLSearchParams();
    params.append('avatarUrl', avatarUrl)
    return request.patch('/front/updateAvatar', params)
}

//前台更新密码
export const frontPasswordUpdateService = (passwordData)=>{

    return request.patch('/front/updatePwd', passwordData);
}