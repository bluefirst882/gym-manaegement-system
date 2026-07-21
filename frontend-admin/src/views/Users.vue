<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.username" placeholder="用户名" clearable style="width:160px" />
        <el-input v-model="query.realName" placeholder="真实姓名" clearable style="width:160px" />
        <el-select v-model="query.roleId" placeholder="角色" clearable style="width:140px">
          <el-option label="系统管理员" :value="1" /><el-option label="普通客户" :value="2" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button type="success" :icon="Plus" @click="openEdit()">新增用户</el-button>
      </div>
 
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="110" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="gender" label="性别" width="70" />
        <el-table-column prop="roleName" label="角色" width="110" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginAt" label="最后登录" width="160" />
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="warning" @click="onReset(row)">重置密码</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
 
    <el-dialog v-model="dialog" :title="form.id ? '编辑用户' : '新增用户'" width="440px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="真实姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender"><el-radio value="男">男</el-radio><el-radio value="女">女</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.roleId"><el-option label="系统管理员" :value="1" /><el-option label="普通客户" :value="2" /></el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="正常" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
 
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import { listUsers, addUser, updateUser, deleteUser, resetPassword } from '@/api'
 
const list = ref([])
const loading = ref(false)
const dialog = ref(false)
const query = reactive({ username: '', realName: '', roleId: null, pageNumber: 1, pageSize: 10 })
const form = ref({})
 
async function load() {
  loading.value = true
  try { const d = await listUsers(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
function openEdit(row) {
  form.value = row ? { ...row } : { username: '', realName: '', phone: '', gender: '男', roleId: 2, status: 1 }
  dialog.value = true
}
async function submit() {
  try {
    if (form.value.id) await updateUser(form.value.id, form.value)
    else await addUser(form.value)
    ElMessage.success('已保存'); dialog.value = false; load()
  } catch (e) {}
}
async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除用户「${row.realName}」？`, '提示', { type: 'warning' })
    await deleteUser(row.id); ElMessage.success('已删除'); load()
  } catch (e) {}
}
async function onReset(row) {
  try {
    await ElMessageBox.confirm(`将「${row.realName}」密码重置为 123456？`, '提示', { type: 'warning' })
    await resetPassword(row.id); ElMessage.success('密码已重置为 123456')
  } catch (e) {}
}
onMounted(load)
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>
