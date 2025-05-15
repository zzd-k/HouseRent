import {createRouter, createWebHistory} from 'vue-router'

//导入组件
import LoginVue from '@/views/Login.vue' 
import LayoutVue from '@/views/Layout.vue'

import ArticleCategoryVue from '@/views/article/ArticleCategory.vue'
import ArticleManageVue from '@/views/article/ArticleManage.vue'

import UserAvatarVue from '@/views/user/UserAvatar.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'
import FrontAvatar from "@/views/front/FrontAvatar.vue";
import UserLayout from "@/views/front/UserLayout.vue";
import {ElMessage} from "element-plus";
import {useTokenStore} from "@/stores/token.js";
import RepairDetail from "@/views/front/user/RepairDetail.vue";


//定义路由关系
const routes = [
    //前台路由
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/front/Home.vue'),
        meta: {
            layout: 'FrontendLayout', // 指定布局
            requiresAuth: false       // 无需登录
        }
    },
    {
        path: '/front-login',
        name: 'Login',
        component: () => import('@/views/front/login.vue'),
        meta: {
            layout: 'FrontendLayout',
            requiresAuth: false
        },
    },
    {
        path: '/rent',
        name: 'Rent',
        component: () => import('@/views/front/user/Rent.vue'),
        meta: {
            layout: 'FrontendLayout',
            requiresAuth: true
        },

    },
    {
        path: '/rentinfo/:id?',  // 增加动态参数
        name: 'RentInfo',
        component: () => import('@/views/front/user/RentInfo.vue'),
        meta: {
            layout: 'FrontendLayout',
            requiresAuth: false
        },
    },
    {
        path: '/order/:id',
        name: 'Order',
        component: () => import('@/views/front/user/order.vue'),
        props: true
    },
    {
        path: '/payment-success',
        name: 'PaymentSuccess',
        component: () => import('@/views/front/user/PaymentSuccess.vue'),
        props: true
    },
    {
        path: '/order/detail/:id',
        name: 'OrderDetail',
        component: () => import('@/views/front/user/OrderDetail.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/repair/detail/:id',
        name: 'RepairDetail',
        component: RepairDetail,
        meta: { requiresAuth: true }
    },
    {
        path: '/contractdetail',
        name: 'ContractDetail',
        component: () => import('@/views/front/user/ContractView.vue'),

    },
    {
        path: '/create',
        component: () => import('@/views/front/user/ContractCreate.vue'),
        meta: { title: '创建合同' }
    },
    {
        path: '/contractview/:id',
        name: 'ContractView',
        component: () => import('@/views/front/user/ContractView.vue'),

    },

    {
        path: '/user',
        name: 'User-center',
        component: UserLayout, // 新增布局组件
        children: [
            {
                path: 'profile',
                name: 'profile',
                component: () => import('@/components/profile.vue')
            },
            {
                path: 'front-avatar',
                name: 'front-avatar',
                component: () => import('@/views/front/FrontAvatar.vue')
            },
            {
                path: 'house',
                name: 'house',
                component: () => import('@/views/front/house/House.vue')
            },
            {
                path: 'money',
                name: 'money',
                component: () => import('@/views/front/house/Money.vue')
            },
            {
                path: 'info',
                name: 'info',
                component: () => import('@/views/front/user/Info.vue')
            },
            {
                path: 'maintenance',
                name: 'maintenance',
                component: () => import('@/views/front/user/Maintenance.vue')
            },
            {
                path: 'housing',
                name: 'Housing',
                component: () => import('@/views/front/user/housing.vue'),

            },
            {
                path: 'housingclassify',
                name: 'HousingClassify',
                component: () => import('@/views/front/user/HousingClassify.vue'),

            },
            {
                path: 'frontuserreset',
                name: 'FrontUserReset',
                component: () => import('@/views/front/user/FrontUserReset.vue'),

            },
            {
                path: 'orderlist',
                name: 'OrderList',
                component: () => import('@/views/front/user/OrderList.vue'),

            },
            {
                path: 'repair',
                name: 'RepairList',
                component: () => import('@/views/front/user/RepairList.vue'),
                meta: { requiresAuth: true, role: '租客' }
            },
            {
                path: 'repair/request',
                name: 'RepairRequest',
                component: () => import('@/views/front/user/RepairRequest.vue'),
                meta: { requiresAuth: true, role: '租客' }
            },
            {
                path: 'contractlist',
                name: 'ContractList',
                component: () => import('@/views/front/user/ContractList.vue'),

            },



        ]
    },
    {
        path: '/admin/repair/detail/:id',
        name: 'AdminRepairDetail',
        component: () => import('@/views/user/AdminRepairDetail.vue')
    },
    //后台路由
    {path:'/login', component:LoginVue},
    // 修改后的后台路由配置
    {path:'/admin', component:LayoutVue, redirect:'/article/manage',children:[
            {path:'/article/category', component:ArticleCategoryVue},
            {path:'/article/manage', component:ArticleManageVue},
            {path:'/user/infos', component:UserInfoVue},
            {path:'/user/avatar', component:UserAvatarVue},
            {path:'/user/resetPassword', component:UserResetPasswordVue},
            {
                path: '/repair/list',
                name: 'adminRepairDetail',
                component: () => import('@/views/user/AdminRepairList.vue')
            },
            {
                path: '/house-rent/list',
                name: 'AdminHouseRentList',
                component: () => import('@/views/user/AdminHouseRentList.vue'),  // 假设你有这个组件
                meta: { requiresAuth: true } // 这里可以加上管理员权限的限制
            },
            {
                path: '/house-rent/audit/:id',
                name: 'UpdateHouseRentAuditStatus',
                component: () => import('@/views/user/UpdateHouseRentAuditStatus.vue'),  // 假设你有审核房源的组件
                meta: { requiresAuth: true, role: '管理员' }
            }
        ]}
]

//创建路由器
const router = createRouter({
    history:createWebHistory(),
    routes: routes
})
router.beforeEach((to, from, next) => {
    const tokenStore = useTokenStore();

    if (to.meta.requiresAuth) {
        if (!tokenStore.token) {

            ElMessage({
                type: 'error',
                message: '请先登录',
                offset: 80
            })
            next('/front-login');
        } else {
            next();
        }
    } else {
        next();
    }
});
//导出路由
export default router