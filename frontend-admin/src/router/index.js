import { createRouter, createWebHistory } from 'vue-router'
 
const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue'), meta: { title: '后台登录' } },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: '数据看板' } },
      { path: 'users', name: 'Users', component: () => import('@/views/Users.vue'), meta: { title: '用户管理' } },
      { path: 'venue-categories', name: 'VenueCategories', component: () => import('@/views/VenueCategories.vue'), meta: { title: '场地分类' } },
      { path: 'venues', name: 'Venues', component: () => import('@/views/Venues.vue'), meta: { title: '场地管理' } },
      { path: 'bookings', name: 'Bookings', component: () => import('@/views/Bookings.vue'), meta: { title: '场地预约' } },
      { path: 'activities', name: 'Activities', component: () => import('@/views/Activities.vue'), meta: { title: '活动管理' } },
      { path: 'registrations', name: 'Registrations', component: () => import('@/views/Registrations.vue'), meta: { title: '报名审核' } },
      { path: 'equipment', name: 'Equipment', component: () => import('@/views/Equipment.vue'), meta: { title: '设备管理' } },
      { path: 'announcements', name: 'Announcements', component: () => import('@/views/Announcements.vue'), meta: { title: '公告管理' } }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
]
 
const router = createRouter({
  history: createWebHistory(),
  routes
})
 
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  // 当前数据库仅将 ADMIN 角色用于管理端；后端仍会以角色编码做最终校验。
  const isAdmin = userInfo.roleId === 1
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.requiresAuth && !isAdmin) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    next({ path: '/login', query: { reason: 'forbidden' } })
  } else if (to.path === '/login' && token) {
    next(isAdmin ? '/dashboard' : { path: '/login', query: { reason: 'forbidden' } })
  } else {
    next()
  }
})
 
export default router
