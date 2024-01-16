import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import * as VueRouter from 'vue-router'
import routes from "./config/route.ts";
import 'element-plus/dist/index.css'
import {getLoginUser} from "./api/user.ts";
import {ElMessage} from "element-plus";
import store from "./store";

const app = createApp(App)

const router = VueRouter.createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHistory(),
    routes, // `routes: routes` 的缩写
})

router.beforeEach(async (to, from) => {
    // 1.获取当前登录信息
    const res = await getLoginUser()
    const isAuthenticated = res.code === 0
    // console.log(res.code)
    if (
        // 检查用户是否已登录
        !isAuthenticated &&
        // ❗️ 避免无限重定向
        to.path !== '/'
    ) {
        ElMessage.error('请先登录')
        // 将用户重定向到登录页面
        return {path: '/'}
    }
})

//整个应用支持路由。
app.use(router)
app.use(store)

app.mount('#app')
