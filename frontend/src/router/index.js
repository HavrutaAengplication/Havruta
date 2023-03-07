import {createRouter, createWebHistory} from "vue-router";
import MainPage from "@/components/MainPage.vue";
import MyPage from "@/components/MyPage.vue";
import Login from "@/components/KaKaoLogin.vue"
import GroupPage from "@/components/GroupPage.vue";
import MyProblems from "@/components/MyProblems.vue"
import CreateProblem from "@/components/CreateProblem.vue";
import ModifyProblem from "@/components/ModifyProblem.vue";
import CategoryView from "@/components/CategoryView.vue"
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
    { path: "/groups/:groupId/problems", component: CreateProblem },
    { path: "/mypage/problems/:problemId", component: ModifyProblem },
    { path: "/categoryproblems", component: CategoryProblems},
    { path: "/groups/:groupId/categories", component: CategoryView},
]

const router = createRouter({
    history: createWebHistory(),
    //요건 원래 구조는 routes : [{path : String, component : 컴포넌트},]로 es6문법에 의해 축약해서 쓴 것(routes : routes)
    routes
})

export default router;