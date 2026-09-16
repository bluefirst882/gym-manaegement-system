const config = require('./config')
const { mockHandle } = require('./mock')
 
// 统一请求封装：成功 code===0，自动带 token，Mock 开关
function request(url, method = 'GET', data = {}) {
  // Mock 模式
  if (config.USE_MOCK) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const res = mockHandle(url, method.toLowerCase(), data)
        if (res.code === 0) resolve(res.data)
        else {
          wx.showToast({ title: res.message || '操作失败', icon: 'none' })
          reject(res)
        }
      }, config.MOCK_DELAY)
    })
  }
  // 真实后端模式
  return new Promise((resolve, reject) => {
    wx.request({
      url: config.BASE_URL + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + (wx.getStorageSync('token') || '')
      },
      success(res) {
        const body = res.data
        if (body && body.code === 0) {
          resolve(body.data)
        } else if (body && body.code === 401) {
          wx.removeStorageSync('token')
          wx.reLaunch({ url: '/pages/login/login' })
          reject(body)
        } else {
          wx.showToast({ title: (body && body.message) || '请求失败', icon: 'none' })
          reject(body)
        }
      },
      fail() {
        wx.showToast({ title: '网络异常', icon: 'none' })
        reject(new Error('network error'))
      }
    })
  })
}
 
module.exports = {
  get: (url, data) => request(url, 'GET', data),
  post: (url, data) => request(url, 'POST', data),
  put: (url, data) => request(url, 'PUT', data),
  del: (url) => request(url, 'DELETE')
}
