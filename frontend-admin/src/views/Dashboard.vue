<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="c in kpis" :key="c.label">
        <el-card class="kpi">
          <div class="kpi-icon" :style="{ background: c.color }">{{ c.icon }}</div>
          <div>
            <div class="kpi-val">{{ c.value }}</div>
            <div class="kpi-label">{{ c.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
 
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card>
          <template #header>场地利用率</template>
          <div ref="venueChart" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>活动报名统计</template>
          <div ref="activityChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
 
<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { statVenueUtilization, statActivityRegistration } from '@/api'
 
const kpis = ref([
  { label: '场地总数', value: '-', icon: '🏟️', color: '#5470c6' },
  { label: '活动总数', value: '-', icon: '🚩', color: '#91cc75' },
  { label: '平均利用率', value: '-', icon: '📈', color: '#fac858' },
  { label: '累计报名', value: '-', icon: '✍️', color: '#ee6666' }
])
 
const venueChart = ref(null)
const activityChart = ref(null)
 
onMounted(async () => {
  const [v, a] = await Promise.all([
    statVenueUtilization({}),
    statActivityRegistration({})
  ])
  // 后端统计接口直接返回数组，而不是 { venueList/activityList } 包装对象。
  const vl = Array.isArray(v) ? v : (v.venueList || [])
  const al = Array.isArray(a) ? a : (a.activityList || [])
 
  kpis.value[0].value = vl.length
  kpis.value[1].value = al.length
  const rateOf = (x) => {
    const total = Number(x.totalBookings || 0)
    return total ? Math.round(Number(x.completedBookings || 0) * 100 / total) : 0
  }
  const avg = vl.length ? Math.round(vl.reduce((s, x) => s + rateOf(x), 0) / vl.length) : 0
  kpis.value[2].value = avg + '%'
  kpis.value[3].value = al.reduce((s, x) => s + x.totalRegistrations, 0)
 
  echarts.init(venueChart.value).setOption({
    tooltip: {},
    xAxis: { type: 'category', data: vl.map((x) => x.venueName), axisLabel: { rotate: 20 } },
    yAxis: { type: 'value', name: '利用率(%)' },
    series: [{ type: 'bar', data: vl.map(rateOf), itemStyle: { color: '#5470c6', borderRadius: [6, 6, 0, 0] } }]
  })
 
  echarts.init(activityChart.value).setOption({
    tooltip: {},
    xAxis: { type: 'category', data: al.map((x) => x.activityTitle), axisLabel: { rotate: 20 } },
    yAxis: { type: 'value' },
    legend: { bottom: 0 },
    series: [
      { name: '报名总数', type: 'bar', data: al.map((x) => x.totalRegistrations), itemStyle: { color: '#91cc75', borderRadius: [6, 6, 0, 0] } },
      { name: '审核通过', type: 'bar', data: al.map((x) => x.approvedCount), itemStyle: { color: '#ee6666', borderRadius: [6, 6, 0, 0] } }
    ]
  })
})
</script>
 
<style scoped>
.kpi { display: flex; align-items: center; }
.kpi :deep(.el-card__body) { display: flex; align-items: center; gap: 14px; width: 100%; }
.kpi-icon { width: 52px; height: 52px; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 26px; }
.kpi-val { font-size: 24px; font-weight: 700; }
.kpi-label { font-size: 13px; color: #999; }
.chart { height: 320px; }
</style>
