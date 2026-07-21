// Mock 数据：与接口文档字段一致，code===0 表示成功
function ok(data) { return { code: 0, message: '操作成功', data } }
function page(list) { return { list, pageNumber: 1, pageSize: list.length, totalPage: 1, totalRow: list.length } }
 
const venues = [
  { id: 1, venueCode: 'V20260720001', venueName: '室内攀岩馆-A区', venueAddress: '极限运动馆1层A区', categoryId: 1, categoryName: '攀岩类', capacity: 50, facilities: '攀岩墙、安全绳、防护垫、空调', feeType: 'ONCE', feeAmount: 88.0, coverImage: '', status: 'AVAILABLE' },
  { id: 2, venueCode: 'V20260720002', venueName: '室内攀岩馆-B区', venueAddress: '极限运动馆1层B区', categoryId: 1, categoryName: '攀岩类', capacity: 30, facilities: '攀岩墙、速度赛道、防护垫', feeType: 'ONCE', feeAmount: 68.0, coverImage: '', status: 'AVAILABLE' },
  { id: 3, venueCode: 'V20260720003', venueName: '室外滑板公园', venueAddress: '极限运动馆户外区域', categoryId: 2, categoryName: '滑板类', capacity: 80, facilities: 'U型池、栏杆、台阶、斜坡', feeType: 'PER_HOUR', feeAmount: 35.0, coverImage: '', status: 'AVAILABLE' },
  { id: 4, venueCode: 'V20260720004', venueName: '室内滑板训练场', venueAddress: '极限运动馆2层', categoryId: 2, categoryName: '滑板类', capacity: 40, facilities: '迷你坡道、街式道具、空调', feeType: 'PER_HOUR', feeAmount: 45.0, coverImage: '', status: 'MAINTENANCE' },
  { id: 5, venueCode: 'V20260720005', venueName: '蹦极跳台', venueAddress: '极限运动馆户外高空区', categoryId: 3, categoryName: '蹦极类', capacity: 10, facilities: '跳台、安全绳系统、缓冲气垫', feeType: 'ONCE', feeAmount: 198.0, coverImage: '', status: 'AVAILABLE' },
  { id: 6, venueCode: 'V20260720006', venueName: '高空拓展训练基地', venueAddress: '极限运动馆户外拓展区', categoryId: 4, categoryName: '高空拓展类', capacity: 60, facilities: '高空断桥、攀岩墙、空中抓杠', feeType: 'PER_HOUR', feeAmount: 120.0, coverImage: '', status: 'AVAILABLE' }
]
 
const activities = [
  { id: 1, activityNo: 'A20260720001', title: '第七届极限攀岩挑战赛', categoryName: '赛事活动', location: '极限运动馆1层攀岩区', startTime: '2026-08-15 09:00:00', endTime: '2026-08-20 18:00:00', status: 'NOT_STARTED', maxParticipants: 100, awards: '冠军：5000元+奖杯', rules: '1.年满16周岁；2.自备攀岩鞋和安全装备', description: '年度攀岩赛事，设难度赛/速度赛/抱石赛三个项目，面向全国攀岩爱好者。', contact: '0756-8888100', feeType: 'FREE', feeAmount: 0 },
  { id: 2, activityNo: 'A20260720002', title: '滑板新手体验日', categoryName: '体验活动', location: '极限运动馆2层滑板训练场', startTime: '2026-08-10 14:00:00', endTime: '2026-08-10 18:00:00', status: 'NOT_STARTED', maxParticipants: 30, awards: '参与即赠送滑板护具一套', rules: '1.无需滑板经验；2.场馆提供装备', description: '专业教练指导，零基础学滑板基础技巧。', contact: '0756-8888101', feeType: 'FREE', feeAmount: 0 },
  { id: 3, activityNo: 'A20260720003', title: '高空绳索救援培训课程', categoryName: '培训课程', location: '极限运动馆户外拓展区', startTime: '2026-08-01 09:00:00', endTime: '2026-08-05 17:00:00', status: 'IN_PROGRESS', maxParticipants: 20, awards: '考核通过颁发证书', rules: '1.具备基本攀爬能力；2.课程为期5天', description: '系统学习高空绳索救援技术。', contact: '0756-8888102', feeType: 'CHARGE', feeAmount: 500 },
  { id: 4, activityNo: 'A20260720004', title: '周年庆会员活动', categoryName: '会员活动', location: '极限运动馆全馆', startTime: '2026-09-01 10:00:00', endTime: '2026-09-03 22:00:00', status: 'NOT_STARTED', maxParticipants: 200, awards: '现场抽奖，大奖为全年免费畅玩卡', rules: '1.仅限会员；2.可携带一位亲友', description: '周年庆狂欢，全场项目免费体验。', contact: '0756-8888103', feeType: 'FREE', feeAmount: 0 }
]
 
const notices = [
  { id: 1, announcementNo: 'N20260720001', title: '暑期营业时间调整通知', summary: '自7月15日起营业时间调整为每日9:00-22:00', content: '为满足暑期高峰需求，自2026年7月15日起，本馆营业时间调整为每日9:00-22:00，请各位合理安排运动时间。', isTop: 1, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' },
  { id: 2, announcementNo: 'N20260720002', title: '第七届攀岩挑战赛报名开启', summary: '年度重磅赛事报名正式启动，冠军奖金5000元', content: '第七届极限攀岩挑战赛将于8月15-20日举行，现已开放报名，设难度赛、速度赛、抱石赛三个项目。', isTop: 1, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' },
  { id: 3, announcementNo: 'N20260720003', title: '场馆设备维护升级通知', summary: '7月22-24日部分区域暂停开放', content: '7月22-24日对攀岩B区、户外滑板公园进行设备维护升级，其他区域正常营业。', isTop: 0, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' },
  { id: 4, announcementNo: 'N20260720004', title: '新会员注册优惠活动', summary: '新注册会员享首次场地预约8折优惠', content: '即日起至9月30日，新注册会员首次场地预约享8折优惠。', isTop: 0, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' }
]
 
let myBookings = [
  { id: 1, bookingNo: 'B20260720001', venueName: '室内攀岩馆-A区', venueAddress: '极限运动馆1层A区', bookingDate: '2026-07-18', startTime: '10:00:00', endTime: '12:00:00', status: 'COMPLETED', feeAmount: 88.0, paymentStatus: 'PAID' },
  { id: 3, bookingNo: 'B20260720003', venueName: '室内攀岩馆-A区', venueAddress: '极限运动馆1层A区', bookingDate: '2026-07-20', startTime: '09:00:00', endTime: '11:00:00', status: 'APPROVED', feeAmount: 88.0, paymentStatus: 'UNPAID' },
  { id: 5, bookingNo: 'B20260720005', venueName: '高空拓展训练基地', venueAddress: '极限运动馆户外拓展区', bookingDate: '2026-07-22', startTime: '09:00:00', endTime: '12:00:00', status: 'PENDING', feeAmount: 360.0, paymentStatus: 'UNPAID' }
]
 
let myRegistrations = [
  { id: 1, registrationNo: 'REG20260720001', activityTitle: '第七届极限攀岩挑战赛', activityType: '个人参赛', auditStatus: 'APPROVED', createdAt: '2026-07-20 12:00:00' },
  { id: 4, registrationNo: 'REG20260720004', activityTitle: '高空绳索救援培训课程', activityType: '培训学员', auditStatus: 'APPROVED', createdAt: '2026-07-20 11:00:00' }
]
 
let _id = 100
 
function mockHandle(url, method, body = {}) {
  const seg = url.split('?')[0]
 
  if (seg === '/auth/login') {
    if (body.password === '123456') {
      return ok({ token: 'mock-jwt-' + Date.now(), userInfo: { id: 3, username: body.username, realName: '张晓', phone: '13912345678', gender: '女', roleId: 2, roleName: '普通客户' } })
    }
    return { code: 400, message: '用户名或密码错误', data: null }
  }
 
  if (seg === '/venues') return ok(page(venues))
  if (/^\/venue\/\d+$/.test(seg)) return ok(venues.find((v) => v.id === Number(seg.split('/')[2])) || venues[0])
 
  if (seg === '/booking' && method === 'post') {
    const v = venues.find((x) => x.id === body.venueId)
    const item = { id: ++_id, bookingNo: 'B' + Date.now(), venueName: v ? v.venueName : '', venueAddress: v ? v.venueAddress : '', bookingDate: body.bookingDate, startTime: body.startTime, endTime: body.endTime, status: 'PENDING', feeAmount: v ? v.feeAmount : 0, paymentStatus: 'UNPAID' }
    myBookings.unshift(item)
    return ok({ id: item.id, bookingNo: item.bookingNo, status: 'PENDING', feeAmount: item.feeAmount })
  }
  if (seg === '/bookings') return ok(page(myBookings))
  if (/^\/booking\/\d+\/cancel$/.test(seg)) { const b = myBookings.find((x) => x.id === Number(seg.split('/')[2])); if (b) b.status = 'CANCELLED'; return ok(true) }
  if (/^\/booking\/\d+\/pay$/.test(seg)) { const b = myBookings.find((x) => x.id === Number(seg.split('/')[2])); if (b) { b.paymentStatus = 'PAID'; b.status = 'APPROVED' } return ok(true) }
  if (/^\/booking\/\d+$/.test(seg) && method === 'delete') { myBookings = myBookings.filter((x) => x.id !== Number(seg.split('/')[2])); return ok(true) }
 
  if (seg === '/activities') return ok(page(activities))
  if (/^\/activity\/\d+$/.test(seg)) return ok(activities.find((a) => a.id === Number(seg.split('/')[2])) || activities[0])
  if (seg === '/activity-registration' && method === 'post') {
    const a = activities.find((x) => x.id === body.activityId)
    const item = { id: ++_id, registrationNo: 'REG' + Date.now(), activityTitle: a ? a.title : '', activityType: body.activityType || '个人参赛', auditStatus: 'PENDING', createdAt: '2026-07-21 10:00:00' }
    myRegistrations.unshift(item)
    return ok({ id: item.id, registrationNo: item.registrationNo, auditStatus: 'PENDING' })
  }
  if (seg === '/activity-registrations') return ok(page(myRegistrations))
  if (/^\/activity-registration\/\d+\/cancel$/.test(seg)) { const r = myRegistrations.find((x) => x.id === Number(seg.split('/')[2])); if (r) r.auditStatus = 'CANCELLED'; return ok(true) }
 
  if (seg === '/announcements') return ok(page(notices.filter((n) => n.status === 'PUBLISHED')))
  if (/^\/announcement\/\d+$/.test(seg)) return ok(notices.find((n) => n.id === Number(seg.split('/')[2])) || notices[0])
 
  return ok({})
}
 
module.exports = { mockHandle }
