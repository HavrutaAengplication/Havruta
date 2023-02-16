import {createRouter, createWebHistory} from "vue-router";
import MainPage from "@/components/MainPage.vue";
import MyPage from "@/components/MyPage.vue";
import Login from "@/components/KaKaoLogin.vue"

const routes = [
    { path: "/home", name: "Home", component: MainPage},
    { path: "/mypage", name: "MyPage", component: MyPage },
    { path: "/login", name: "Login", component: Login},
]

const router = createRouter({
    history: createWebHistory(),
    //요건 원래 구조는 routes : [{path : String, component : 컴포넌트},]로 es6문법에 의해 축약해서 쓴 것(routes : routes)
    routes
})

export default router;