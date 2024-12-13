import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from '@/api/request.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import   '../public/css/global.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
app.use(router).use(ElementPlus,{ locale: zhCn,}).mount('#app')
app.config.globalProperties.$axios=axios;