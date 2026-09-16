const api = require('../../utils/api')
 
Page({
  data: { list: [], loading: true },
 
  onShow() { this.load() },
 
  async load() {
    this.setData({ loading: true })
    try {
      const d = await api.myBookings()
      this.setData({ list: d.list || [] })
    } catch (e) {} finally {
      this.setData({ loading: false })
    }
  },
 
  statusText(s) { return { PENDING: '待审核', APPROVED: '已通过', REJECTED: '已拒绝', CANCELLED: '已取消', COMPLETED: '已完成' }[s] || s },
 
  onCancel(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '取消预约',
      content: '确认取消该预约？场地资源将被释放',
      success: async (r) => {
        if (r.confirm) {
          try {
            await api.cancelBooking(id)
            wx.showToast({ title: '已取消', icon: 'success' })
            this.load()
          } catch (err) {}
        }
      }
    })
  },
 
  onPay(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '模拟支付',
      content: '确认支付该预约费用？',
      success: async (r) => {
        if (r.confirm) {
          try {
            await api.payBooking(id, { paymentMethod: '微信支付' })
            wx.showToast({ title: '支付成功', icon: 'success' })
            this.load()
          } catch (err) {}
        }
      }
    })
  }
})
