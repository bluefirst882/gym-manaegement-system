const api = require('../../utils/api')
 
Page({
  data: { list: [], loading: true },
  onShow() { this.load() },
  async load() {
    this.setData({ loading: true })
    try {
      const d = await api.myRegistrations()
      this.setData({ list: d.list || [] })
    } catch (e) {} finally { this.setData({ loading: false }) }
  },
  onCancel(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '取消报名',
      content: '确认取消该报名？',
      success: async (r) => {
        if (r.confirm) {
          try {
            await api.cancelRegistration(id)
            wx.showToast({ title: '已取消', icon: 'success' })
            this.load()
          } catch (err) {}
        }
      }
    })
  }
})
