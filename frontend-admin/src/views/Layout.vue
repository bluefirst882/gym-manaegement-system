<template>
  <el-container class="admin-layout">
    <el-aside :width="collapse ? '64px' : '220px'" class="aside">
      <div class="logo">{{ collapse ? '⚡' : '⚡ 极限运动馆' }}</div>
      <el-menu :default-active="activeMenu" :collapse="collapse" router background-color="#001529" text-color="#bfcbd9" active-text-color="#ee5253">
        <el-menu-item index="/dashboard"><el-icon><DataAnalysis /></el-icon><template #title>数据看板</template></el-menu-item>
        <el-menu-item index="/bookings"><el-icon><Calendar /></el-icon><template #title>场地预约</template></el-menu-item>
        <el-menu-item index="/venues"><el-icon><OfficeBuilding /></el-icon><template #title>场地管理</template></el-menu-item>
        <el-menu-item index="/venue-categories"><el-icon><Collection /></el-icon><template #title>场地分类</template></el-menu-item>
        <el-menu-item index="/activities"><el-icon><Flag /></el-icon><template #title>活动管理</template></el-menu-item>
        <el-menu-item index="/registrations"><el-icon><Checked /></el-icon><template #title>报名审核</template></el-menu-item>
        <el-menu-item index="/equipment"><el-icon><Box /></el-icon><template #title>设备管理</template></el-menu-item>
        <el-menu-item index="/announcements"><el-icon><Bell /></el-icon><template #title>公告管理</template></el-menu-item>
        <el-menu-item index="/users"><el-icon><User /></el-icon><template #title>用户管理</template></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="left">
          <el-icon class="fold" @click="collapse = !collapse"><Fold v-if="!collapse" /><Expand v-else /></el-icon>
          <span class="title">{{ route.meta.title || '运营管理后台' }}</span>
        </div>
        <el-dropdown @command="onCommand">
          <span class="user">
            <el-icon><Avatar /></el-icon> {{ realName }} <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
 
<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { DataAnalysis, Calendar, OfficeBuilding, Collection, Flag, Checked, Box, Bell, User, Fold, Expand, Avatar, ArrowDown } from '@element-plus/icons-vue'
 
const route = useRoute()
const router = useRouter()
const collapse = ref(false)
const activeMenu = computed(() => route.path)
const realName = computed(() => JSON.parse(localStorage.getItem('userInfo') || '{}').realName || '管理员')
 
function onCommand(cmd) {
  if (cmd === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.replace('/login')
  }
}
</script>
 
<style scoped>
.admin-layout { height: 100vh; }
.aside { background: #001529; transition: width 0.2s; overflow: hidden; }
.logo { height: 60px; line-height: 60px; text-align: center; font-weight: 700; color: #ee5253; background: #000c17; white-space: nowrap; }
.aside .el-menu { border-right: none; }
.header { display: flex; align-items: center; justify-content: space-between; background: #fff; border-bottom: 1px solid #eee; }
.left { display: flex; align-items: center; gap: 12px; }
.fold { font-size: 20px; cursor: pointer; }
.title { font-size: 16px; font-weight: 600; }
.user { display: flex; align-items: center; gap: 6px; cursor: pointer; color: #333; }
.main { background: #f5f6fa; }
</style>
