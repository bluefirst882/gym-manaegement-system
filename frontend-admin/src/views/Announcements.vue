<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.title" placeholder="公告标题" clearable style="width:200px" />
        <el-select v-model="query.status" placeholder="状态" clearable style="width:140px">
          <el-option label="草稿" value="DRAFT" /><el-option label="已发布" value="PUBLISHED" /><el-option label="已下架" value="OFFLINE" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button type="success" :icon="Plus" @click="openEdit()">发布公告</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column label="置顶" width="70">
          <template #default="{ row }"><span v-if="row.isTop" style="color:#ee5253">📌</span></template>
        </el-table-column>
        <el-table-column prop="title" label="公告标题" min-width="200" />
        <el-table-column prop="summary" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : row.status === 'DRAFT' ? 'info' : 'warning'">
              {{ row.status === 'PUBLISHED' ? '已发布' : row.status === 'DRAFT' ? '草稿' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="发布时间" width="160" />
        <el-table-column label="操作" width="270" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" :type="row.isTop ? 'warning' : 'primary'" @click="onToggleTop(row)">{{ row.isTop ? '取消置顶' : '置顶' }}</el-button>
            <el-button v-if="row.status === 'PUBLISHED'" size="small" type="info" @click="onOffline(row)">下架</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
 
    <el-dialog v-model="dialog" :title="form.id ? '编辑公告' : '发布公告'" width="560px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.summary" /></el-form-item>
        <el-form-item label="正文"><el-input v-model="form.content" type="textarea" :rows="5" placeholder="支持富文本 HTML" /></el-form-item>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="置顶"><el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态">
            <el-select v-model="form.status"><el-option label="草稿" value="DRAFT" /><el-option label="发布" value="PUBLISHED" /></el-select>
          </el-form-item></el-col>
        </el-row>
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
import { listAnnouncements, addAnnouncement, updateAnnouncement, deleteAnnouncement, offlineAnnouncement, toggleTopAnnouncement } from '@/api'
 
const list = ref([])
const loading = ref(false)
const dialog = ref(false)
const query = reactive({ title: '', status: '', pageNumber: 1, pageSize: 10 })
const form = ref({})
 
async function load() {
  loading.value = true
  try { const d = await listAnnouncements(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
function openEdit(row) {
  form.value = row ? { ...row } : { title: '', summary: '', content: '', isTop: 0, status: 'PUBLISHED' }
  dialog.value = true
}
async function submit() {
  try {
    if (form.value.id) await updateAnnouncement(form.value.id, form.value)
    else await addAnnouncement(form.value)
    ElMessage.success('已保存'); dialog.value = false; load()
  } catch (e) {}
}
async function onToggleTop(row) { try { await toggleTopAnnouncement(row.id); load() } catch (e) {} }
async function onOffline(row) {
  try { await ElMessageBox.confirm(`确认下架公告「${row.title}」？`, '提示', { type: 'warning' }); await offlineAnnouncement(row.id); ElMessage.success('已下架'); load() } catch (e) {}
}
async function onDelete(row) {
  try { await ElMessageBox.confirm(`确认删除公告「${row.title}」？`, '提示', { type: 'warning' }); await deleteAnnouncement(row.id); ElMessage.success('已删除'); load() } catch (e) {}
}
onMounted(load)
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>

四、微信小程序客户端 · 完整源码
