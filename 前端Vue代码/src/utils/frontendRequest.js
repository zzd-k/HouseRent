import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useTokenStore } from '@/stores/token.js';
import router from '@/router';

const baseURL = '/api';
const frontendInstance = axios.create({ baseURL });

// 请求拦截器
frontendInstance.interceptors.request.use(
    (config) => {
        const tokenStore = useTokenStore();
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
        }
        return config;
    },
    (err) => Promise.reject(err)
);

// 响应拦截器
frontendInstance.interceptors.response.use(
    response => {
        // 修改1: 使用 code 字段，并检查 code === 0
        if (response.data.code === 0) {
            // 修改2: 直接返回整个 data 字段（包含 Token）
            return response.data;
        }
        ElMessage.error(response.data.message || '服务异常');
        return Promise.reject(response.data);
    },
    (error) => {
        const tokenStore = useTokenStore();
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    tokenStore.removeToken();
                    const currentPath = router.currentRoute.value.path;
                    if (currentPath.startsWith('/user') && currentPath !== '/front-login') {
                        ElMessage.error('请先登录');
                        router.push({ path: '/front-login', query: { redirect: currentPath } });
                    }
                    break;
                case 403:
                    ElMessage.error('权限不足，请联系管理员');
                    break;
                case 500:
                    ElMessage.error('服务器错误，请稍后重试');
                    break;
                default:
                    ElMessage.error(error.response.data.message || '服务异常');
            }
        } else {
            ElMessage.error('网络错误，请检查连接后重试');
        }
        return Promise.reject(error);
    }
);

export default frontendInstance;