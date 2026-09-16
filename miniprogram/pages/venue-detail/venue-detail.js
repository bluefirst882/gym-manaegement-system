const api = require('../../utils/api')
 
Page({
  data: { venue: {}, id: null },
 
  onLoad(options) {
    this.setData({ id: options.id })
    this.load()
  },
 
  async load() {
    try {
      const d = await api.getVenue(this.data.id)
      this.setData({ venue: d })
      wx.setNavigationBarTitle({ title: d.venueName })
    } catch (e) {}
  },
 
  goBooking() {
    if (this.data.venue.status !== 'AVAILABLE') {
      wx.showToast({ title: '该场地维护中', icon: 'none' })
      return
    }
    wx.navigateTo({ url: '/pages/booking/booking?id=' + this.data.id })
  }
})
