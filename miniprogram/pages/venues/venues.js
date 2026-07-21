const api = require('../../utils/api')
 
Page({
  data: { venues: [], loading: true },
 
  onShow() { this.load() },
 
  async load() {
    this.setData({ loading: true })
    try {
      const d = await api.listVenues()
      this.setData({ venues: d.list || [] })
    } catch (e) {} finally {
      this.setData({ loading: false })
    }
  },
 
  goDetail(e) {
    wx.navigateTo({ url: '/pages/venue-detail/venue-detail?id=' + e.currentTarget.dataset.id })
  }
})
