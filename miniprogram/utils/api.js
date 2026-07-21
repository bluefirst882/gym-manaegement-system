const { get, post, put, del } = require('./request')
 
module.exports = {
  // 登录
  login: (data) => post('/auth/login', data),
 
  // 场地
  listVenues: (p) => post('/venues', Object.assign({ pageNumber: 1, pageSize: 50 }, p)),
  getVenue: (id) => get(`/venue/${id}`),
 
  // 场地预约
  createBooking: (p) => post('/booking', p),
  myBookings: (p) => post('/bookings', Object.assign({ pageNumber: 1, pageSize: 50, userId: (wx.getStorageSync('userInfo') || {}).id }, p)),
  cancelBooking: (id) => put(`/booking/${id}/cancel`),
  payBooking: (id, p) => put(`/booking/${id}/pay`, p),
  deleteBooking: (id) => del(`/booking/${id}`),
 
  // 活动
  listActivities: (p) => post('/activities', Object.assign({ pageNumber: 1, pageSize: 50 }, p)),
  getActivity: (id) => get(`/activity/${id}`),
  registerActivity: (p) => post('/activity-registration', p),
  myRegistrations: (p) => post('/activity-registrations', Object.assign({ pageNumber: 1, pageSize: 50, userId: (wx.getStorageSync('userInfo') || {}).id }, p)),
  cancelRegistration: (id) => put(`/activity-registration/${id}/cancel`),
 
  // 公告
  listNotices: (p) => post('/announcements', Object.assign({ pageNumber: 1, pageSize: 50, status: 'PUBLISHED' }, p)),
  getNotice: (id) => get(`/announcement/${id}`)
}
