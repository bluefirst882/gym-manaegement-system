<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.activityTitle" placeholder="活动主题" clearable style="width:180px" />
        <el-select v-model="query.auditStatus" placeholder="审核状态" clearable style="width:150px">
          <el-option label="待审核" value="PENDING" /><el-option label="已通过" value="APPROVED" /><el-option label="已拒绝" value="REJECTED" /><el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
      </div>
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="registrationNo" label="报名编号" width="150" />
        <el-table-column prop="activityTitle" label="活动" min-width="170" />
        <el-table-column prop="customerName" label="客户" width="100" />
        <el-table-column prop="activityType" label="报名类型" width="110" />
        <el-table-column prop="remark" label="备注" min-width="130" />
        <el-table-column label="审核状态" width="100">
          <template #default="{ row }"><el-tag :type="tag(row.auditStatus)">{{ text(row.auditStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="170" fixed="right">
          <template #default="{ row }">
            <template v-if="row.auditStatus === 'PENDING'">
              <el-button size="small" type="success" @click="onAudit(row, 'APPROVED')">通过</el-button>
              <el-button size="small" type="danger" @click="onAudit(row, 'REJECTED')">拒绝</el-button>
            </template>
            <span v-else style="color:#999;font-size:12px">{{ row.auditComment || '已处理' }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
 
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { listRegistrations, auditRegistration } from '@/api'
 
const list = ref([])
const loading = ref(false)
const query = reactive({ activityTitle: '', auditStatus: '', pageNumber: 1, pageSize: 10 })
 
const text = (s) => ({ PENDING: '待审核', APPROVED: '已通过', REJECTED: '已拒绝', CANCELLED: '已取消' }[s] || s)
const tag = (s) => ({ PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger', CANCELLED: 'info' }[s] || 'info')
 
async function load() {
  loading.value = true
  try { const d = await listRegistrations(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
async function onAudit(row, status) {
  try {
    const { value } = await ElMessageBox.prompt(status === 'APPROVED' ? '审核意见（可选）' : '拒绝原因', status === 'APPROVED' ? '通过报名' : '拒绝报名', { inputPlaceholder: '请输入', inputValue: status === 'APPROVED' ? '欢迎参加活动！' : '' })
    await auditRegistration(row.id, { auditStatus: status, auditComment: value })
    ElMessage.success('审核完成'); load()
  } catch (e) {}
}
onMounted(load)
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>
