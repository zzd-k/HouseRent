
import axios from 'axios';

// const baseURL = 'http://localhost:8081';  

const baseURL = '/api';
const instance = axios.create({baseURL})
 
//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        return result.data;
    },
    err=>{
        alert('服务异常');
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        //判断业务状态码
        if(result.data.code === 0){
            return result.data;
        }
        //操作失败
        alert(result.data.message ? result.data.message : '服务异常')
        //异步操作的状态转换为失败
        return Promise.reject(result.data);
    },
    err=>{
        alert('服务异常');
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

//暴露
export default instance;