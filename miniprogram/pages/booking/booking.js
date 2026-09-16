const api = require('../../utils/api')
 
Page({
  data: {
    venue: {},
    dates: [],
    dateIndex: 0,
    times: [],
    startIndex: -1,
    endIndex: -1,
    remark: '',
    submitting: false
  },
 
  onLoad(options) {
    this.venueId = options.id
    this.buildDates()
    this.buildTimes()
    this.loadVenue()
  },
 
  buildDates() {
    const arr = []
    const week = ['日', '一', '二', '三', '四', '五', '六']
    for (let i = 0; i < 7; i++) {
      const d = new Date()
      d.setDate(d.getDate() + i)
      const label = i === 0 ? '今天' : i === 1 ? '明天' : '周' + week[d.getDay()]
      arr.push({ label, value: d.toISOString().slice(0, 10), sub: (d.getMonth() + 1) + '/' + d.getDate() })
    }
    this.setData({ dates: arr })
  },
 
  buildTimes() {
    const arr = []
    for (let h = 9; h < 22; h++) arr.push(String(h).padStart(2, '0') + ':00')
    this.setData({ times: arr })
  },
 
  async loadVenue() {
    try {
      const d = await api.getVenue(this.venueId)
      this.setData({ venue: d })
    } catch (e) {}
  },
 
  pickDate(e) { this.setData({ dateIndex: e.currentTarget.dataset.i }) },
  pickStart(e) {
    const i = e.currentTarget.dataset.i
    this.setData({ startIndex: i, endIndex: this.data.endIndex <= i ? -1 : this.data.endIndex })
  },
  pickEnd(e) { this.setData({ endIndex: e.currentTarget.dataset.i }) },
  onRemark(e) { this.setData({ remark: e.detail.value }) },
 
  async submit() {
    const { dates, dateIndex, times, startIndex, endIndex, remark, venue } = this.data
    if (startIndex < 0 || endIndex < 0) {
      wx.showToast({ title: '请选择开始和结束时间', icon: 'none' })
      return
    }
    const userInfo = wx.getStorageSync('userInfo')
    this.setData({ submitting: true })
    try {
      await api.createBooking({
        userId: userInfo.id,
        venueId: venue.id,
        bookingDate: dates[dateIndex].value,
        startTime: times[startIndex] + ':00',
        endTime: times[endIndex] + ':00',
        remark
      })
      wx.showModal({
        title: '预约提交成功',
        content: '请等待管理员审核，可在"我的预约"中查看进度',
        confirmText: '查看预约',
        success: (r) => {
          if (r.confirm) wx.redirectTo({ url: '/pages/my-bookings/my-bookings' })
          else wx.navigateBack()
        }
      })
    } catch (e) {} finally {
      this.setData({ submitting: false })
    }
  }
})
