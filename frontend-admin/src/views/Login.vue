<template>
  <div class="login-page">
    <div class="login-box">
      <div class="logo">⚡ 极限运动馆</div>
      <h2>运营管理后台</h2>
      <el-form :model="form" @keyup.enter="handleLogin">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-button type="primary" size="large" class="btn" :loading="loading" @click="handleLogin">登 录</el-button>
      </el-form>
      <p class="tip">仅管理员可登录。演示账号：admin / 123456</p>
    </div>
  </div>
</template>
 
<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api'
 
const router = useRouter()
const route = useRoute()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '123456' })

if (route.query.reason === 'forbidden') {
  ElMessage.warning('您没有管理后台访问权限，请使用微信小程序客户端')
}
 
async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const data = await login(form)
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify(data.userInfo))
    ElMessage.success('登录成功')
    router.replace('/dashboard')
  } catch (e) {
    // 错误已在拦截器提示
  } finally {
    loading.value = false
  }
}
</script>
 
<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1f1c2c, #353154);
}
.login-box {
  width: 380px;
  background: #fff;
  border-radius: 12px;
  padding: 36px 32px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}
.logo {
  text-align: center;
  font-size: 26px;
  font-weight: 700;
  color: #ee5253;
}
h2 {
  text-align: center;
  font-size: 16px;
  color: #666;
  font-weight: 400;
  margin: 8px 0 24px;
}
.btn {
  width: 100%;
  margin-top: 6px;
}
.tip {
  margin-top: 16px;
  font-size: 12px;
  color: #999;
  text-align: center;
}
</style>
