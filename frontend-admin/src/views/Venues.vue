<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.venueName" placeholder="场地名称" clearable style="width:180px" />
        <el-select v-model="query.categoryId" placeholder="分类" clearable style="width:150px">
          <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" />
        </el-select>
        <el-select v-model="query.status" placeholder="状态" clearable style="width:130px">
          <el-option label="可用" value="AVAILABLE" /><el-option label="维护中" value="MAINTENANCE" /><el-option label="已停用" value="DISABLED" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
        <el-button type="success" :icon="Plus" @click="openEdit()">新增场地</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="venueCode" label="编码" width="130" />
        <el-table-column prop="venueName" label="场地名称" min-width="150" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="capacity" label="容量" width="70" />
        <el-table-column label="收费" width="130">
          <template #default="{ row }">¥{{ row.feeAmount }}{{ row.feeType === 'ONCE' ? '/次' : '/小时' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'AVAILABLE' ? 'success' : row.status === 'MAINTENANCE' ? 'warning' : 'info'">
              {{ row.status === 'AVAILABLE' ? '可用' : row.status === 'MAINTENANCE' ? '维护中' : '已停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
 
    <el-dialog v-model="dialog" :title="form.id ? '编辑场地' : '新增场地'" width="560px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="场地名称"><el-input v-model="form.venueName" /></el-form-item>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="分类">
            <el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" /></el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="容量"><el-input-number v-model="form.capacity" :min="1" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="场地地址"><el-input v-model="form.venueAddress" /></el-form-item>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="收费方式">
            <el-select v-model="form.feeType"><el-option label="按次" value="ONCE" /><el-option label="按小时" value="PER_HOUR" /></el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="收费金额"><el-input-number v-model="form.feeAmount" :min="0" :step="10" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio value="AVAILABLE">可用</el-radio><el-radio value="MAINTENANCE">维护中</el-radio><el-radio value="DISABLED">已停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="设施设备"><el-input v-model="form.facilities" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
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
import { listVenues, listVenueCategories, addVenue, updateVenue, deleteVenue } from '@/api'
 
const list = ref([])
const categories = ref([])
const loading = ref(false)
const dialog = ref(false)
const query = reactive({ venueName: '', categoryId: null, status: '', pageNumber: 1, pageSize: 10 })
const form = ref({})
 
async function load() {
  loading.value = true
  try { const d = await listVenues(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
async function loadCats() {
  try { const d = await listVenueCategories({ pageNumber: 1, pageSize: 100 }); categories.value = d.list || [] } catch (e) {}
}
function openEdit(row) {
  form.value = row ? { ...row } : { venueName: '', categoryId: null, capacity: 10, feeType: 'ONCE', feeAmount: 50, status: 'AVAILABLE', venueAddress: '', facilities: '', remark: '' }
  dialog.value = true
}
async function submit() {
  try {
    if (form.value.id) await updateVenue(form.value.id, form.value)
    else await addVenue(form.value)
    ElMessage.success('已保存'); dialog.value = false; load()
  } catch (e) {}
}
async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除场地「${row.venueName}」？已有预约记录的场地不可删除。`, '提示', { type: 'warning' })
    await deleteVenue(row.id); ElMessage.success('已删除'); load()
  } catch (e) {}
}
onMounted(() => { loadCats(); load() })
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>
