import { createRouter, createWebHistory } from 'vue-router'
import HouseList from '../components/HouseList.vue';



const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "manager/home",  
      
    },
    { path: '/list',
      name: 'HouseList',
      component: HouseList 
    },
   
    
    {
      path: '/manager',
      component: () => import('../views/Manager.vue'),  children:[
        {
          path: 'home',
          name: 'home', meta:{title:'主页'},  component: () => import('../views/Home.vue')},
        { 
          path: 'test',
          name: 'test', meta:{title:'测试页面'}, component: () => import('../views/Test.vue')},
          { 
            path: 'data',
            name: 'data', meta:{title:'数据展示页面'}, component: () => import('../views/Data.vue')},
      ]},
      {
        path: '/404',
        name: 'Not Found',
        meta:{title:'404找不到页面'},
        component: () => import('../views/404.vue')
      },
      {
        path:'/:pathMatch(.*)', redirect:'/404'
      }
  ]
})

 

// 路由守卫 表示跳转之前的一些操作
router.beforeEach((to, from, next) => {
  document.title=to.meta.title
  next()
})

export default router
  