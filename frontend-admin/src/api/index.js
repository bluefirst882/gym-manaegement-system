import request from './request'
 
// ===== 登录 =====
export function login(data) {
  return request.post('/auth/admin-login', data)
}
 
// ===== 用户管理 =====
export const listUsers = (p) => request.post('/users', p)
export const addUser = (p) => request.post('/user', p)
export const updateUser = (id, p) => request.put(`/user/${id}`, p)
export const deleteUser = (id) => request.delete(`/user/${id}`)
export const resetPassword = (id) => request.put(`/user/${id}/reset-password`)
 
// ===== 场地分类 =====
export const listVenueCategories = (p) => request.post('/venue-categories', p)
export const addVenueCategory = (p) => request.post('/venue-category', p)
export const updateVenueCategory = (id, p) => request.put(`/venue-category/${id}`, p)
export const deleteVenueCategory = (id) => request.delete(`/venue-category/${id}`)
 
// ===== 场地 =====
export const listVenues = (p) => request.post('/venues', p)
export const addVenue = (p) => request.post('/venue', p)
export const updateVenue = (id, p) => request.put(`/venue/${id}`, p)
export const deleteVenue = (id) => request.delete(`/venue/${id}`)
export const getVenue = (id) => request.get(`/venue/${id}`)
 
// ===== 场地预约 =====
export const listBookings = (p) => request.post('/bookings', p)
export const createBooking = (p) => request.post('/booking', p)
export const cancelBooking = (id) => request.put(`/booking/${id}/cancel`)
export const auditBooking = (id, p) => request.put(`/booking/${id}/audit`, p)
export const deleteBooking = (id) => request.delete(`/booking/${id}`)
export const checkinBooking = (id, p) => request.put(`/booking/${id}/checkin`, p)
export const checkoutBooking = (id, p) => request.put(`/booking/${id}/checkout`, p)
export const payBooking = (id, p) => request.put(`/booking/${id}/pay`, p)
export const setPaymentStatus = (id, p) => request.put(`/booking/${id}/payment-status`, p)
 
// ===== 活动分类 / 活动 =====
export const listActivityCategories = (p) => request.post('/activity-categories', p)
export const listActivities = (p) => request.post('/activities', p)
export const addActivity = (p) => request.post('/activity', p)
export const updateActivity = (id, p) => request.put(`/activity/${id}`, p)
export const deleteActivity = (id) => request.delete(`/activity/${id}`)
 
// ===== 活动报名 =====
export const listRegistrations = (p) => request.post('/activity-registrations', p)
export const auditRegistration = (id, p) => request.put(`/activity-registration/${id}/audit`, p)
 
// ===== 设备 =====
export const listEquipmentCategories = (p) => request.post('/equipment-categories', p)
export const listEquipment = (p) => request.post('/equipments', p)
export const addEquipment = (p) => request.post('/equipment', p)
export const updateEquipment = (id, p) => request.put(`/equipment/${id}`, p)
export const deleteEquipment = (id) => request.delete(`/equipment/${id}`)
export const listPurchases = (p) => request.post('/equipment-purchases', p)
export const addPurchase = (p) => request.post('/equipment-purchase', p)
export const listRentals = (p) => request.post('/equipment-rentals', p)
export const addRental = (p) => request.post('/equipment-rental', p)
export const returnRental = (id, p) => request.put(`/equipment-rental/${id}/return`, p)
export const listMaintenances = (p) => request.post('/equipment-maintenances', p)
export const addMaintenance = (p) => request.post('/equipment-maintenance', p)
 
// ===== 公告 =====
export const listAnnouncements = (p) => request.post('/announcements', p)
export const addAnnouncement = (p) => request.post('/announcement', p)
export const updateAnnouncement = (id, p) => request.put(`/announcement/${id}`, p)
export const deleteAnnouncement = (id) => request.delete(`/announcement/${id}`)
export const offlineAnnouncement = (id) => request.put(`/announcement/${id}/offline`)
export const toggleTopAnnouncement = (id) => request.put(`/announcement/${id}/toggle-top`)
 
// ===== 统计 =====
export const statVenueUtilization = (p) => request.post('/statistics/venue-usage', p)
export const statActivityRegistration = (p) => request.post('/statistics/activity', p)
