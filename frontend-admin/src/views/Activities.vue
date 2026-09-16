<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.title" placeholder="活动主题" clearable style="width:180px" />
        <el-select v-model="query.categoryId" placeholder="分类" clearable style="width:150px">
          <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" />
        </el-select>
        <el-select v-model="query.status" placeholder="状态" clearable style="width:130px">
          <el-option label="草稿" value="DRAFT" /><el-option label="未开始" value="NOT_STARTED" /><el-option label="进行中" value="IN_PROGRESS" /><el-option label="已结束" value="ENDED" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button type="success" :icon="Plus" @click="openEdit()">发布活动</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="activityNo" label="编号" width="130" />
        <el-table-column prop="title" label="活动主题" min-width="170" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="location" label="地点" min-width="140" />
        <el-table-column label="时间" width="180">
          <template #default="{ row }">{{ row.startTime.slice(5, 10) }} ~ {{ row.endTime.slice(5, 10) }}</template>
        </el-table-column>
        <el-table-column label="费用" width="90">
          <template #default="{ row }">{{ row.feeType === 'FREE' ? '免费' : '¥' + row.feeAmount }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)">{{ statusText(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
 
    <el-dialog v-model="dialog" :title="form.id ? '编辑活动' : '发布活动'" width="600px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="活动主题"><el-input v-model="form.title" /></el-form-item>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="分类">
            <el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" /></el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="人数上限"><el-input-number v-model="form.maxParticipants" :min="1" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="活动状态">
          <el-select v-model="form.status" style="width:100%">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="未开始（发布）" value="NOT_STARTED" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已结束" value="ENDED" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动地点"><el-input v-model="form.location" /></el-form-item>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="开始时间"><el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束时间"><el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="费用类型">
            <el-select v-model="form.feeType"><el-option label="免费" value="FREE" /><el-option label="收费" value="CHARGE" /></el-select>
          </el-form-item></el-col>
          <el-col :span="12" v-if="form.feeType === 'CHARGE'"><el-form-item label="收费金额"><el-input-number v-model="form.feeAmount" :min="0" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="奖项设置"><el-input v-model="form.awards" /></el-form-item>
        <el-form-item label="活动规则"><el-input v-model="form.rules" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="详细介绍"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="联系方式"><el-input v-model="form.contact" /></el-form-item>
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
import { listActivities, listActivityCategories, addActivity, updateActivity, deleteActivity } from '@/api'
 
const list = ref([])
const categories = ref([])
const loading = ref(false)
const dialog = ref(false)
const query = reactive({ title: '', categoryId: null, status: '', pageNumber: 1, pageSize: 10 })
const form = ref({})
 
const statusText = (s) => ({ DRAFT: '草稿', NOT_STARTED: '未开始', IN_PROGRESS: '进行中', ENDED: '已结束' }[s] || s)
const statusTag = (s) => ({ DRAFT: 'info', NOT_STARTED: 'primary', IN_PROGRESS: 'warning', ENDED: 'success' }[s] || 'info')
 
async function load() {
  loading.value = true
  try { const d = await listActivities(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
async function loadCats() {
  try { const d = await listActivityCategories({ pageNumber: 1, pageSize: 100 }); categories.value = d.list || [] } catch (e) {}
}
function openEdit(row) {
  form.value = row ? { ...row } : { title: '', categoryId: null, location: '', startTime: '', endTime: '', maxParticipants: 50, feeType: 'FREE', feeAmount: 0, awards: '', rules: '', description: '', contact: '', registrationMethod: 'ONLINE', status: 'NOT_STARTED' }
  dialog.value = true
}
async function submit() {
  try {
    if (!form.value.title?.trim()) return ElMessage.warning('请填写活动主题')
    if (!form.value.startTime || !form.value.endTime) return ElMessage.warning('请填写开始和结束时间')
    if (new Date(form.value.startTime) >= new Date(form.value.endTime)) return ElMessage.warning('结束时间必须晚于开始时间')
    const payload = {
      ...form.value,
      title: form.value.title.trim(),
      categoryId: form.value.categoryId || null,
      location: form.value.location || '',
      status: form.value.status || 'NOT_STARTED',
      startTime: String(form.value.startTime).replace('T', ' '),
      endTime: String(form.value.endTime).replace('T', ' ')
    }
    if (form.value.id) await updateActivity(form.value.id, payload)
    else await addActivity(payload)
    ElMessage.success('已保存'); dialog.value = false; load()
  } catch (e) {}
}
async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除活动「${row.title}」？已有报名的活动不可删除。`, '提示', { type: 'warning' })
    await deleteActivity(row.id); ElMessage.success('已删除'); load()
  } catch (e) {}
}
onMounted(() => { loadCats(); load() })
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>
