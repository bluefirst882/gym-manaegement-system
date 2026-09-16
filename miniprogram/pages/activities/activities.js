const api = require('../../utils/api')
 
Page({
  data: { list: [], loading: true },
  onShow() { this.load() },
  async load() {
    this.setData({ loading: true })
    try {
      const d = await api.listActivities()
      this.setData({ list: d.list || [] })
    } catch (e) {} finally { this.setData({ loading: false }) }
  },
  goDetail(e) {
    wx.navigateTo({ url: '/pages/activity-detail/activity-detail?id=' + e.currentTarget.dataset.id })
  }
})
