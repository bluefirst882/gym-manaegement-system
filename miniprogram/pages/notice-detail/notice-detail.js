const api = require('../../utils/api')
 
Page({
  data: { notice: {} },
  onLoad(options) {
    api.getNotice(options.id).then((d) => {
      this.setData({ notice: d })
      wx.setNavigationBarTitle({ title: '公告详情' })
    }).catch(() => {})
  }
})
