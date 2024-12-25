import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import Start from "@/components/Start.vue";

const routes = [
    {path: '/', name: Start, component: Start}
]

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;