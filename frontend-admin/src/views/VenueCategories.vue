<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.categoryName" placeholder="分类名称" clearable style="width:200px" />
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button type="success" :icon="Plus" @click="openEdit()">新增分类</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="categoryCode" label="分类编码" width="120" />
        <el-table-column prop="categoryName" label="分类名称" width="160" />
        <el-table-column prop="categoryDesc" label="分类描述" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
 
    <el-dialog v-model="dialog" :title="form.id ? '编辑分类' : '新增分类'" width="420px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="分类编码"><el-input v-model="form.categoryCode" :disabled="!!form.id" placeholder="如 VC005" /></el-form-item>
        <el-form-item label="分类名称"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="分类描述"><el-input v-model="form.categoryDesc" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
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
import { listVenueCategories, addVenueCategory, updateVenueCategory, deleteVenueCategory } from '@/api'
 
const list = ref([])
const loading = ref(false)
const dialog = ref(false)
const query = reactive({ categoryName: '', pageNumber: 1, pageSize: 10 })
const form = ref({})
 
async function load() {
  loading.value = true
  try { const d = await listVenueCategories(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
function openEdit(row) {
  form.value = row ? { ...row } : { categoryCode: '', categoryName: '', categoryDesc: '', sortOrder: 0 }
  dialog.value = true
}
async function submit() {
  try {
    if (form.value.id) await updateVenueCategory(form.value.id, form.value)
    else await addVenueCategory(form.value)
    ElMessage.success('已保存'); dialog.value = false; load()
  } catch (e) {}
}
async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除分类「${row.categoryName}」？已被场地引用的分类不可删除。`, '提示', { type: 'warning' })
    await deleteVenueCategory(row.id); ElMessage.success('已删除'); load()
  } catch (e) {}
}
onMounted(load)
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; }
</style>
