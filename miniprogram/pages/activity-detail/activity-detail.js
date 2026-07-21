const api = require('../../utils/api')
 
Page({
  data: { act: {}, types: ['个人参赛', '团队参赛', '个人体验', '观赛'], typeIndex: 0, remark: '', submitting: false },
 
  onLoad(options) {
    this.id = options.id
    this.load()
  },
 
  async load() {
    try {
      const d = await api.getActivity(this.id)
      this.setData({ act: d })
      wx.setNavigationBarTitle({ title: d.title })
    } catch (e) {}
  },
 
  onType(e) { this.setData({ typeIndex: e.detail.value }) },
  onRemark(e) { this.setData({ remark: e.detail.value }) },
 
  async submit() {
    const userInfo = wx.getStorageSync('userInfo')
    if (!userInfo) { wx.redirectTo({ url: '/pages/login/login' }); return }
    this.setData({ submitting: true })
    try {
      await api.registerActivity({
        activityId: this.data.act.id,
        userId: userInfo.id,
        activityType: this.data.types[this.data.typeIndex],
        remark: this.data.remark
      })
      wx.showModal({
        title: '报名成功',
        content: '请等待管理员审核，可在"我的报名"中查看',
        confirmText: '查看报名',
        success: (r) => {
          if (r.confirm) wx.redirectTo({ url: '/pages/my-registrations/my-registrations' })
          else wx.navigateBack()
        }
      })
    } catch (e) {} finally {
      this.setData({ submitting: false })
    }
  }
})
