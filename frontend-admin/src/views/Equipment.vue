<template>
  <div>
    <el-card>
      <el-tabs v-model="tab">
        <!-- 设备清单 -->
        <el-tab-pane label="设备清单" name="list">
          <div class="toolbar">
            <el-input v-model="qList.name" placeholder="设备名称" clearable style="width:180px" />
            <el-select v-model="qList.categoryId" placeholder="分类" clearable style="width:150px">
              <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" />
            </el-select>
            <el-button type="primary" :icon="Search" @click="loadList">查询</el-button>
            <el-button type="success" :icon="Plus" @click="openEdit()">新增设备</el-button>
          </div>
          <el-table :data="list" border stripe v-loading="loading">
            <el-table-column prop="equipmentNo" label="编号" width="130" />
            <el-table-column prop="name" label="设备名称" min-width="170" />
            <el-table-column prop="categoryName" label="分类" width="100" />
            <el-table-column prop="brand" label="品牌" width="120" />
            <el-table-column prop="location" label="存放位置" width="140" />
            <el-table-column prop="quantity" label="总库存" width="80" />
            <el-table-column prop="availableQuantity" label="可用" width="70" />
            <el-table-column label="租用中" width="80">
              <template #default="{ row }">{{ Math.max(0, row.quantity - row.availableQuantity) }}</template>
            </el-table-column>
            <el-table-column label="状态" width="90">
              <template #default="{ row }"><el-tag :type="equipmentStatusTag(row.status)">{{ equipmentStatusText(row.status) }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="openEdit(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
 
        <!-- 购买记录 -->
        <el-tab-pane label="购买记录" name="purchase">
          <div class="toolbar">
            <el-button type="success" :icon="Plus" @click="openPurchase()">新增购买</el-button>
          </div>
          <el-table :data="purchases" border stripe v-loading="loading">
            <el-table-column prop="purchaseNo" label="购买编号" width="140" />
            <el-table-column prop="equipmentName" label="设备名称" min-width="170" />
            <el-table-column prop="quantity" label="数量" width="80" />
            <el-table-column prop="purchasePrice" label="金额(元)" width="110" />
            <el-table-column prop="purchaseDate" label="购买日期" width="120" />
            <el-table-column prop="supplier" label="供应商" min-width="160" />
          </el-table>
        </el-tab-pane>
 
        <!-- 租用记录 -->
        <el-tab-pane label="租用记录" name="rental">
          <div class="toolbar">
            <el-input v-model="qRental.borrower" placeholder="租借人" clearable style="width:150px" />
            <el-select v-model="qRental.status" placeholder="状态" clearable style="width:140px">
              <el-option label="借用中" value="RENTED" /><el-option label="已归还" value="RETURNED" /><el-option label="逾期未还" value="OVERDUE" />
            </el-select>
            <el-button type="primary" :icon="Search" @click="loadRentals">查询</el-button>
            <el-button type="success" :icon="Plus" @click="openRental()">新增租用</el-button>
          </div>
          <el-table :data="rentals" border stripe v-loading="loading">
            <el-table-column prop="rentalNo" label="租用编号" width="140" />
            <el-table-column prop="equipmentName" label="设备" min-width="160" />
            <el-table-column prop="quantity" label="数量" width="70" />
            <el-table-column prop="borrower" label="租借人" width="90" />
            <el-table-column label="租用时间" width="200">
              <template #default="{ row }">{{ row.startTime.slice(5, 16) }} ~ {{ row.endTime.slice(5, 16) }}</template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }"><el-tag :type="row.status === 'RETURNED' ? 'success' : row.status === 'RENTED' ? 'warning' : 'danger'">{{ row.status === 'RETURNED' ? '已归还' : row.status === 'RENTED' ? '借用中' : '逾期' }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="110" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.status === 'RENTED'" size="small" type="success" @click="onReturn(row)">归还</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
 
        <!-- 维护记录 -->
        <el-tab-pane label="维护记录" name="maintenance">
          <div class="toolbar">
            <el-button type="success" :icon="Plus" @click="openMaintenance()">新增维护</el-button>
          </div>
          <el-table :data="maintenances" border stripe v-loading="loading">
            <el-table-column prop="maintenanceNo" label="维护编号" width="140" />
            <el-table-column prop="title" label="维护标题" min-width="150" />
            <el-table-column prop="equipmentName" label="设备" min-width="160" />
            <el-table-column prop="maintenanceTime" label="维护时间" width="160" />
            <el-table-column prop="personnel" label="维护人员" width="110" />
            <el-table-column label="设备情况" width="100">
              <template #default="{ row }"><el-tag :type="conditionTag(row.equipmentCondition)">{{ conditionText(row.equipmentCondition) }}</el-tag></template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
 
    <!-- 设备编辑 -->
    <el-dialog v-model="dialog" :title="form.id ? '编辑设备' : '新增设备'" width="520px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="设备名称"><el-input v-model="form.name" /></el-form-item>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="分类">
            <el-select v-model="form.categoryId"><el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id" /></el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="数量"><el-input-number v-model="form.quantity" :min="1" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12"><el-form-item label="品牌"><el-input v-model="form.brand" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="型号"><el-input v-model="form.model" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="序列号"><el-input v-model="form.serialNumber" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="存放位置"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="责任人"><el-input v-model="form.responsiblePerson" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
 
    <!-- 购买 -->
    <el-dialog v-model="purchaseDialog" title="新增购买记录" width="480px">
      <el-form :model="purchaseForm" label-width="90px">
        <el-form-item label="关联设备">
          <el-select v-model="purchaseForm.equipmentId" @change="onPickEquipment">
            <el-option v-for="e in list" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量"><el-input-number v-model="purchaseForm.quantity" :min="1" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="purchaseForm.purchasePrice" :min="0" /></el-form-item>
        <el-form-item label="购买日期"><el-date-picker v-model="purchaseForm.purchaseDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="供应商"><el-input v-model="purchaseForm.supplier" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="purchaseDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPurchase">保存</el-button>
      </template>
    </el-dialog>
 
    <!-- 租用 -->
    <el-dialog v-model="rentalDialog" title="新增租用记录" width="480px">
      <el-form :model="rentalForm" label-width="90px">
        <el-form-item label="设备">
          <el-select v-model="rentalForm.equipmentId"><el-option v-for="e in list" :key="e.id" :label="`${e.name}(可租${e.availableQuantity})`" :value="e.id" /></el-select>
        </el-form-item>
        <el-form-item label="数量"><el-input-number v-model="rentalForm.quantity" :min="1" /></el-form-item>
        <el-form-item label="租借人"><el-input v-model="rentalForm.borrower" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="rentalForm.contactPhone" /></el-form-item>
        <el-form-item label="租借价格"><el-input-number v-model="rentalForm.rentalPrice" :min="0" /></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="rentalForm.startTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="rentalForm.endTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rentalDialog = false">取消</el-button>
        <el-button type="primary" @click="submitRental">保存</el-button>
      </template>
    </el-dialog>
 
    <!-- 维护 -->
    <el-dialog v-model="maintenanceDialog" title="新增维护记录" width="480px">
      <el-form :model="maintenanceForm" label-width="90px">
        <el-form-item label="维护标题"><el-input v-model="maintenanceForm.title" /></el-form-item>
        <el-form-item label="设备">
          <el-select v-model="maintenanceForm.equipmentId"><el-option v-for="e in list" :key="e.id" :label="e.name" :value="e.id" /></el-select>
        </el-form-item>
        <el-form-item label="维护时间"><el-date-picker v-model="maintenanceForm.maintenanceTime" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="维护内容"><el-input v-model="maintenanceForm.content" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="维护人员"><el-input v-model="maintenanceForm.personnel" /></el-form-item>
        <el-form-item label="设备情况">
          <el-select v-model="maintenanceForm.equipmentCondition" style="width:100%">
            <el-option label="正常" value="NORMAL" />
            <el-option label="维修中" value="REPAIRING" />
            <el-option label="报废" value="SCRAPPED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="maintenanceDialog = false">取消</el-button>
        <el-button type="primary" @click="submitMaintenance">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
 
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import { listEquipmentCategories, listEquipment, addEquipment, updateEquipment, deleteEquipment, listPurchases, addPurchase, listRentals, addRental, returnRental, listMaintenances, addMaintenance } from '@/api'
 
const tab = ref('list')
const loading = ref(false)
const categories = ref([])
const list = ref([])
const purchases = ref([])
const rentals = ref([])
const maintenances = ref([])
 
const dialog = ref(false)
const purchaseDialog = ref(false)
const rentalDialog = ref(false)
const maintenanceDialog = ref(false)
 
const qList = reactive({ name: '', categoryId: null, pageNumber: 1, pageSize: 50 })
const qRental = reactive({ borrower: '', status: '', pageNumber: 1, pageSize: 50 })
const form = ref({})
const purchaseForm = ref({})
const rentalForm = ref({})
const maintenanceForm = ref({})

const equipmentStatusText = (status) => ({ NORMAL: '正常', FAULT: '故障/维修', SCRAPPED: '报废', RENTED: '已借出' }[status] || status)
const equipmentStatusTag = (status) => ({ NORMAL: 'success', FAULT: 'warning', SCRAPPED: 'danger', RENTED: 'info' }[status] || 'info')
const conditionText = (condition) => ({ NORMAL: '正常', REPAIRING: '维修中', SCRAPPED: '报废' }[condition] || condition)
const conditionTag = (condition) => ({ NORMAL: 'success', REPAIRING: 'warning', SCRAPPED: 'danger' }[condition] || 'info')
 
async function loadCats() { try { const d = await listEquipmentCategories({ pageNumber: 1, pageSize: 100 }); categories.value = d.list || [] } catch (e) {} }
async function loadList() { loading.value = true; try { const d = await listEquipment(qList); list.value = d.list || [] } catch (e) {} finally { loading.value = false } }
async function loadPurchases() { loading.value = true; try { const d = await listPurchases({ pageNumber: 1, pageSize: 50 }); purchases.value = d.list || [] } catch (e) {} finally { loading.value = false } }
async function loadRentals() { loading.value = true; try { const d = await listRentals(qRental); rentals.value = d.list || [] } catch (e) {} finally { loading.value = false } }
async function loadMaintenances() { loading.value = true; try { const d = await listMaintenances({ pageNumber: 1, pageSize: 50 }); maintenances.value = d.list || [] } catch (e) {} finally { loading.value = false } }
 
function openEdit(row) {
  form.value = row ? { ...row } : { name: '', categoryId: null, quantity: 1, brand: '', model: '', serialNumber: '', location: '', responsiblePerson: '管理员' }
  dialog.value = true
}
async function submit() {
  try {
    if (form.value.id) await updateEquipment(form.value.id, form.value)
    else await addEquipment(form.value)
    ElMessage.success('已保存'); dialog.value = false; loadList()
  } catch (e) {}
}
async function onDelete(row) {
  try {
    await ElMessageBox.confirm(`确认删除设备「${row.name}」？`, '提示', { type: 'warning' })
    await deleteEquipment(row.id); ElMessage.success('已删除'); loadList()
  } catch (e) {}
}
 
function openPurchase() { purchaseForm.value = { equipmentId: null, quantity: 1, purchasePrice: 0, purchaseDate: '', supplier: '' }; purchaseDialog.value = true }
function onPickEquipment(id) { const e = list.value.find((x) => x.id === id); if (e) purchaseForm.value.equipmentName = e.name }
async function submitPurchase() { try { await addPurchase(purchaseForm.value); ElMessage.success('购买入库成功'); purchaseDialog.value = false; loadPurchases(); loadList() } catch (e) {} }
 
function openRental() { rentalForm.value = { equipmentId: null, quantity: 1, borrower: '', contactPhone: '', rentalPrice: 0, startTime: '', endTime: '' }; rentalDialog.value = true }
async function submitRental() { try { await addRental(rentalForm.value); ElMessage.success('租用成功'); rentalDialog.value = false; loadRentals(); loadList() } catch (e) {} }
async function onReturn(row) {
  try { await ElMessageBox.confirm(`确认设备「${row.equipmentName}」已归还？`, '归还登记', { type: 'warning' }); await returnRental(row.id, {}); ElMessage.success('已归还'); loadRentals(); loadList() } catch (e) {}
}
 
function openMaintenance() { maintenanceForm.value = { title: '', equipmentId: null, maintenanceTime: '', content: '', personnel: '设备维护部', equipmentCondition: 'NORMAL' }; maintenanceDialog.value = true }
async function submitMaintenance() { try { await addMaintenance(maintenanceForm.value); ElMessage.success('维护记录已保存'); maintenanceDialog.value = false; loadMaintenances(); loadList() } catch (e) {} }
 
onMounted(() => { loadCats(); loadList(); loadPurchases(); loadRentals(); loadMaintenances() })
</script>
 
<style scoped>
.toolbar { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
</style>
