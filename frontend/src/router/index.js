import {createRouter, createWebHistory} from "vue-router";
import MainPage from "@/components/MainPage.vue";
import MyPage from "@/components/MyPage.vue";
import Login from "@/components/KaKaoLogin.vue"
import GroupPage from "@/components/GroupPage.vue";
import MyProblems from "@/components/MyProblems.vue"
import CreateProblem from "@/components/CreateProblem.vue";
//import CreateGroup from "@/components/CategoryView.vue"
import CategoryProblems from "@/components/CategoryProblems.vue"
import GroupAdmin from "@/components/GroupAdmin.vue"
import MemberManagement from "@/components/MemberManagement.vue";

const routes = [
    { path: "/home", name: "Home", component: MainPage},
    { path: "/mypage", name: "MyPage", component: MyPage },
    { path: "/login", name: "Login", component: Login},
    { path: '/groups/:groupId', component: GroupPage },
    { path: '/groups/:groupId/categories/:categoryId', component: CategoryProblems},
    { path: '/groups/:groupId/admin', name: 'admin', component: GroupAdmin},
    { path: '/groups/:groupId/members', component: MemberManagement},
    { path: "/problems", name: "MyProblem", component: MyProblems},
    { path: "/groups", name: "GroupPage", component: GroupPage},
    { path: "/createproblem", component: CreateProblem },
    { path: "/categoryproblems", component: CategoryProblems},
]

const router = createRouter({
    history: createWebHistory(),
    //요건 원래 구조는 routes : [{path : String, component : 컴포넌트},]로 es6문법에 의해 축약해서 쓴 것(routes : routes)
    routes
})

export default router;