import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProjectDetailsView from "@/views/ProjectDetailsView.vue";
import ProjectListView from "@/views/ProjectListView.vue";
import StoreView from "@/views/StoreView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/store',
      name: "storeView",
      component: StoreView,
    },
    {
      path: '/projects',
      name: "projectListView",
      component: ProjectListView,
    },
    {
      path: '/projects/:id',
      name: "projectDetails",
      component: ProjectDetailsView,
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue'),
    // },
  ],
})

export default router
