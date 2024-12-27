import {createRouter, createWebHistory} from "vue-router";
import Start from "@/components/views/Start.vue";
import Login from "@/components/views/Login.vue";
import Registration from "@/components/views/Registration.vue";

const routes = [
    { path: '/', name: 'Start', component: Start },
    { path: '/login', name: 'Login', component: Login },
    { path: '/registration', name: 'Registration', component: Registration },
]

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;