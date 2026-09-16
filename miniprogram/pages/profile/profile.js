Page({
  data: { userInfo: {}, avatarText: '客', displayName: '未登录', displayPhone: '' },
  onShow() {
    const userInfo = wx.getStorageSync('userInfo') || {}
    const displayName = userInfo.realName || userInfo.username || '未登录'
    this.setData({
      userInfo,
      avatarText: displayName.charAt(0) || '客',
      displayName,
      displayPhone: userInfo.phone || ''
    })
  },
  goMyBookings() { wx.navigateTo({ url: '/pages/my-bookings/my-bookings' }) },
  goMyRegistrations() { wx.navigateTo({ url: '/pages/my-registrations/my-registrations' }) },
  goNotices() { wx.navigateTo({ url: '/pages/notices/notices' }) },
  onAbout() {
    wx.showModal({ title: '关于', content: '极限运动馆管理系统小程序 v1.0\nSpringBoot + 微信小程序\n课设演示版（Mock 数据）', showCancel: false })
  },
  onLogout() {
    wx.showModal({
      title: '退出登录',
      content: '确认退出当前账号？',
      success: (r) => {
        if (r.confirm) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          wx.reLaunch({ url: '/pages/login/login' })
        }
      }
    })
  }
})
