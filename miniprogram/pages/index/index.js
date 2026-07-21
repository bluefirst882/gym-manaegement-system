const api = require('../../utils/api')
 
Page({
  data: { notices: [], venues: [], userInfo: null },
 
  onShow() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.setData({ userInfo: wx.getStorageSync('userInfo') })
    this.loadData()
  },
 
  async loadData() {
    try {
      const [n, v] = await Promise.all([api.listNotices(), api.listVenues()])
      this.setData({ notices: n.list || [], venues: (v.list || []).slice(0, 4) })
    } catch (e) {}
  },
 
  goVenues() { wx.switchTab({ url: '/pages/venues/venues' }) },
  goActivities() { wx.switchTab({ url: '/pages/activities/activities' }) },
  goNotices() { wx.navigateTo({ url: '/pages/notices/notices' }) },
  goMyBookings() { wx.navigateTo({ url: '/pages/my-bookings/my-bookings' }) },
  goVenue(e) { wx.navigateTo({ url: '/pages/venue-detail/venue-detail?id=' + e.currentTarget.dataset.id }) },
  goNotice(e) { wx.navigateTo({ url: '/pages/notice-detail/notice-detail?id=' + e.currentTarget.dataset.id }) }
})
