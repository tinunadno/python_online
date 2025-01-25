import {createRouter, createWebHistory} from "vue-router";
import Start from "@/components/views/Main.vue";
import Login from "@/components/views/Login.vue";
import Registration from "@/components/views/Registration.vue";
import RegistrationDef from "@/components/views/RegistrationDef.vue";
import Info from "@/components/views/Info.vue";

const routes = [
    { path: '/', name: 'Start', component: Start },
    { path: '/login', name: 'Login', component: Login },
    { path: '/registration', name: 'Registration', component: Registration },
    { path: '/registrationdef', name: 'RegistrationDef', component: RegistrationDef },
    { path: '/info', name: 'Info', component: Info}
]

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;