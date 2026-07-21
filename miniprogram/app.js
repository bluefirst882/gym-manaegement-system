App({
  globalData: {
    token: '',
    userInfo: null
  },
  onLaunch() {
    // 启动时读取本地登录态
    this.globalData.token = wx.getStorageSync('token') || ''
    this.globalData.userInfo = wx.getStorageSync('userInfo') || null
  }
})
