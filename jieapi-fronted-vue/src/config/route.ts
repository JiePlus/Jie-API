import LoginPage from "../views/LoginPage.vue";
import Index from "../views/IndexPage.vue";
import Home from "../components/Home.vue";
import AboutPage from "../views/AboutPage.vue";
import UserAdminPage from "../views/admin/UserAdminPage.vue";
import InterfaceInfoAdminPage from "../views/admin/InterfaceInfoAdminPage.vue";
import InterfaceInfoAnalysisPage from "../views/admin/InterfaInfoAnalysisPage.vue";
import InterfaceRunPage from "../views/InterfaceRunPage.vue";
import UserInfoPage from "../views/UserInfoPage.vue";

const routes = [
    {path: '/', component: LoginPage, name: "登录",},
    {path: '/home', component: Home, name: "主页",
    meta: {
        requiresAuth: true,
    },
    children: [
        {
            path: '/index', component: Index, name: "系统首页",
            meta: {
                requiresAuth: true,
            }
        },
        {
            path: '/admin/user', component: UserAdminPage, name: "用户管理",
            meta: {
                requiresAuth: true,
            }
        },
        {
            path: '/admin/interfaceInfo', component: InterfaceInfoAdminPage, name: "接口管理",
            meta: {
                requiresAuth: true,
            }
        },
        {
            path: '/admin/interfaceAnalysis', component: InterfaceInfoAnalysisPage, name: "接口分析",
            meta: {
                requiresAuth: true,
            }
        },
        {
            path: '/about', component: AboutPage, name: "关于",
            meta: {
                requiresAuth: true,
            }
        },
        {path: '/interfaceRun/:id', component: InterfaceRunPage, name: "接口运行",},
        {path: '/userinfo', component: UserInfoPage, name: "个人信息",},
    ]},
]


export default routes;