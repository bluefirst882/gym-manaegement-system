<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input v-model="query.customerName" placeholder="客户姓名" clearable style="width:150px" />
        <el-input v-model="query.venueName" placeholder="场地名称" clearable style="width:150px" />
        <el-select v-model="query.status" placeholder="预约状态" clearable style="width:140px">
          <el-option v-for="s in statusOptions" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
        <el-select v-model="query.paymentStatus" placeholder="缴费状态" clearable style="width:130px">
          <el-option label="待支付" value="UNPAID" /><el-option label="已支付" value="PAID" /><el-option label="已退款" value="REFUNDED" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="load">查询</el-button>
      </div>
 
      <el-table :data="list" border stripe v-loading="loading">
        <el-table-column prop="bookingNo" label="预约编号" width="140" />
        <el-table-column prop="customerName" label="客户" width="90" />
        <el-table-column prop="venueName" label="场地" min-width="140" />
        <el-table-column label="预约时间" width="200">
          <template #default="{ row }">{{ row.bookingDate }} {{ row.startTime }}-{{ row.endTime }}</template>
        </el-table-column>
        <el-table-column label="预约状态" width="100">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)">{{ statusText(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="缴费" width="90">
          <template #default="{ row }">
            <el-tag :type="row.paymentStatus === 'PAID' ? 'success' : row.paymentStatus === 'REFUNDED' ? 'info' : 'warning'">
              {{ row.paymentStatus === 'PAID' ? '已支付' : row.paymentStatus === 'REFUNDED' ? '已退款' : '待支付' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="feeAmount" label="金额" width="90">
          <template #default="{ row }">¥{{ row.feeAmount }}</template>
        </el-table-column>
        <el-table-column label="操作" width="390" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING'">
              <el-button size="small" type="success" @click="onAudit(row, 'APPROVED')">通过</el-button>
              <el-button size="small" type="warning" @click="onAudit(row, 'REJECTED')">拒绝</el-button>
            </template>
            <el-button v-if="row.status === 'APPROVED' && !row.checkinTime" size="small" type="primary" @click="onCheckin(row)">到店登记</el-button>
            <el-button v-if="row.checkinTime && !row.checkoutTime" size="small" type="success" @click="onCheckout(row)">离店结算</el-button>
            <el-button size="small" @click="openPayment(row)">缴费</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
 
    <!-- 缴费状态 -->
    <el-dialog v-model="payDialog" title="修改缴费状态" width="380px">
      <el-form label-width="80px">
        <el-form-item label="订单号"><span>{{ current.bookingNo }}</span></el-form-item>
        <el-form-item label="缴费状态">
          <el-select v-model="payForm.paymentStatus">
            <el-option label="待支付" value="UNPAID" /><el-option label="已支付" value="PAID" /><el-option label="已退款" value="REFUNDED" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式" v-if="payForm.paymentStatus === 'PAID'">
          <el-select v-model="payForm.paymentMethod"><el-option label="微信支付" value="微信支付" /><el-option label="现金" value="现金" /><el-option label="刷卡" value="刷卡" /></el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="payDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPayment">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
 
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { listBookings, deleteBooking, auditBooking, checkinBooking, checkoutBooking, setPaymentStatus } from '@/api'
 
const list = ref([])
const loading = ref(false)
const payDialog = ref(false)
const current = ref({})
const payForm = reactive({ paymentStatus: 'PAID', paymentMethod: '微信支付' })
const query = reactive({ customerName: '', venueName: '', status: '', paymentStatus: '', pageNumber: 1, pageSize: 10 })
 
const statusOptions = [
  { value: 'PENDING', label: '待审核' }, { value: 'APPROVED', label: '已通过' },
  { value: 'REJECTED', label: '已拒绝' }, { value: 'CANCELLED', label: '已取消' }, { value: 'COMPLETED', label: '已完成' }
]
const statusText = (s) => statusOptions.find((x) => x.value === s)?.label || s
const statusTag = (s) => ({ PENDING: 'warning', APPROVED: 'primary', COMPLETED: 'success', REJECTED: 'danger', CANCELLED: 'info' }[s] || 'info')
 
async function load() {
  loading.value = true
  try { const d = await listBookings(query); list.value = d.list || [] } catch (e) {} finally { loading.value = false }
}
async function onCheckin(row) {
  try {
    const { value } = await ElMessageBox.prompt('到场人数', '到店登记', { inputValue: row.attendeeCount || 1, inputPattern: /^\d+$/, inputErrorMessage: '请输入数字' })
    await checkinBooking(row.id, { attendeeCount: Number(value) })
    ElMessage.success('到店登记成功'); load()
  } catch (e) {}
}
async function onAudit(row, status) {
  const action = status === 'APPROVED' ? '通过' : '拒绝'
  try {
    await ElMessageBox.confirm(`确认${action}预约「${row.bookingNo}」？`, '预约审核', { type: 'warning' })
    await auditBooking(row.id, { status })
    ElMessage.success(`已${action}`); load()
  } catch (e) {}
}
async function onCheckout(row) {
  try {
    await ElMessageBox.confirm('确认客户离店并结算费用？', '离店结算', { type: 'warning' })
    const d = await checkoutBooking(row.id, {})
    ElMessage.success(`结算成功，费用 ¥${d.feeAmount}`); load()
  } catch (e) {}
}
function openPayment(row) {
  current.value = row
  payForm.paymentStatus = row.paymentStatus
  payForm.paymentMethod = row.paymentMethod || '微信支付'
  payDialog.value = true
}
async function submitPayment() {
  try {
    await setPaymentStatus(current.value.id, { ...payForm, amount: current.value.feeAmount })
    ElMessage.success('缴费状态已更新'); payDialog.value = false; load()
  } catch (e) {}
}
async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除预约「${row.bookingNo}」？`, '提示', { type: 'warning' })
    await deleteBooking(row.id); ElMessage.success('已删除'); load()
  } catch (e) {}
}
onMounted(load)
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>
