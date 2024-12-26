import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import Start from "@/components/views/Start.vue";

const routes = [
    {path: '/', name: Start, component: Start}
]

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;