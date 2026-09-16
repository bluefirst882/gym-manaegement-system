const api = require('../../utils/api')
 
Page({
  data: { username: 'zhangxiao', password: '123456', loading: false },
 
  onInput(e) {
    this.setData({ [e.currentTarget.dataset.field]: e.detail.value })
  },
 
  async onLogin() {
    const { username, password } = this.data
    if (!username || !password) {
      wx.showToast({ title: '请输入账号密码', icon: 'none' })
      return
    }
    this.setData({ loading: true })
    try {
      const data = await api.login({ username, password })
      wx.setStorageSync('token', data.token)
      wx.setStorageSync('userInfo', data.userInfo)
      getApp().globalData.token = data.token
      getApp().globalData.userInfo = data.userInfo
      wx.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => wx.switchTab({ url: '/pages/index/index' }), 500)
    } catch (e) {
      // 已提示
    } finally {
      this.setData({ loading: false })
    }
  }
})
