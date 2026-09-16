// 前端 Mock 数据：后端未就绪时，拦截请求返回假数据
// 后端就绪后，把 request.js 里的 USE_MOCK 改为 false 即可切换真实接口
// 数据结构与《极限运动馆管理系统接口文档》保持一致：成功 code===0，分页返回 list/pageNumber/totalRow
 
function ok(data) {
  return { code: 0, message: '操作成功', data }
}
function page(list) {
  return { list, pageNumber: 1, pageSize: list.length, totalPage: 1, totalRow: list.length }
}
 
// 简单内存"数据库"（支持增删改）
let _uid = 100
const db = {
  users: [
    { id: 1, username: 'admin', realName: '系统管理员', phone: '13800000001', gender: '男', birthday: '1990-01-01', roleId: 1, roleName: '系统管理员', status: 1, registeredAt: '2026-07-20 10:00:00', lastLoginAt: '2026-07-20 14:30:00' },
    { id: 2, username: 'manager01', realName: '张三', phone: '13800000002', gender: '男', birthday: '1992-05-15', roleId: 1, roleName: '系统管理员', status: 1, registeredAt: '2026-07-20 10:00:00', lastLoginAt: '2026-07-20 09:00:00' },
    { id: 3, username: 'zhangxiao', realName: '张晓', phone: '13912345678', gender: '女', birthday: '1998-08-20', roleId: 2, roleName: '普通客户', status: 1, registeredAt: '2026-07-20 10:00:00', lastLoginAt: '2026-07-21 08:00:00' },
    { id: 4, username: 'liwei', realName: '李伟', phone: '13987654321', gender: '男', birthday: '2000-03-12', roleId: 2, roleName: '普通客户', status: 1, registeredAt: '2026-07-20 10:00:00', lastLoginAt: null },
    { id: 5, username: 'wangfang', realName: '王芳', phone: '13799887766', gender: '女', birthday: '2001-11-05', roleId: 2, roleName: '普通客户', status: 0, registeredAt: '2026-07-20 10:00:00', lastLoginAt: null }
  ],
  venueCategories: [
    { id: 1, categoryCode: 'VC001', categoryName: '攀岩类', categoryDesc: '攀岩运动相关场地，按次收费', sortOrder: 1 },
    { id: 2, categoryCode: 'VC002', categoryName: '滑板类', categoryDesc: '滑板运动相关场地，按小时收费', sortOrder: 2 },
    { id: 3, categoryCode: 'VC003', categoryName: '蹦极类', categoryDesc: '蹦极运动相关场地', sortOrder: 3 },
    { id: 4, categoryCode: 'VC004', categoryName: '高空拓展类', categoryDesc: '高空拓展训练相关场地', sortOrder: 4 }
  ],
  venues: [
    { id: 1, venueCode: 'V20260720001', venueName: '室内攀岩馆-A区', venueAddress: '极限运动馆1层A区', contactPhone: '0756-8888001', categoryId: 1, categoryName: '攀岩类', dimensions: '30m×20m×15m', material: '人工岩壁/高强度树脂', capacity: 50, facilities: '攀岩墙、安全绳、防护垫、空调', feeType: 'ONCE', feeAmount: 88.0, coverImage: '', status: 'AVAILABLE', remark: null },
    { id: 2, venueCode: 'V20260720002', venueName: '室内攀岩馆-B区', venueAddress: '极限运动馆1层B区', contactPhone: '0756-8888001', categoryId: 1, categoryName: '攀岩类', dimensions: '25m×18m×12m', material: '人工岩壁', capacity: 30, facilities: '攀岩墙、速度赛道、防护垫', feeType: 'ONCE', feeAmount: 68.0, coverImage: '', status: 'AVAILABLE', remark: null },
    { id: 3, venueCode: 'V20260720003', venueName: '室外滑板公园', venueAddress: '极限运动馆户外区域', contactPhone: '0756-8888002', categoryId: 2, categoryName: '滑板类', dimensions: '50m×30m×0m', material: '高强度水泥', capacity: 80, facilities: 'U型池、栏杆、台阶、斜坡', feeType: 'PER_HOUR', feeAmount: 35.0, coverImage: '', status: 'AVAILABLE', remark: null },
    { id: 4, venueCode: 'V20260720004', venueName: '室内滑板训练场', venueAddress: '极限运动馆2层', contactPhone: '0756-8888002', categoryId: 2, categoryName: '滑板类', dimensions: '40m×25m×8m', material: '木质地板', capacity: 40, facilities: '迷你坡道、街式道具、空调', feeType: 'PER_HOUR', feeAmount: 45.0, coverImage: '', status: 'MAINTENANCE', remark: null },
    { id: 5, venueCode: 'V20260720005', venueName: '蹦极跳台', venueAddress: '极限运动馆户外高空区', contactPhone: '0756-8888003', categoryId: 3, categoryName: '蹦极类', dimensions: '10m×10m×50m', material: '钢结构/高强度绳索', capacity: 10, facilities: '跳台、安全绳系统、缓冲气垫', feeType: 'ONCE', feeAmount: 198.0, coverImage: '', status: 'AVAILABLE', remark: null },
    { id: 6, venueCode: 'V20260720006', venueName: '高空拓展训练基地', venueAddress: '极限运动馆户外拓展区', contactPhone: '0756-8888004', categoryId: 4, categoryName: '高空拓展类', dimensions: '60m×40m×12m', material: '钢结构/钢缆/木质平台', capacity: 60, facilities: '高空断桥、攀岩墙、空中抓杠', feeType: 'PER_HOUR', feeAmount: 120.0, coverImage: '', status: 'AVAILABLE', remark: null }
  ],
  bookings: [
    { id: 1, bookingNo: 'B20260720001', userId: 3, customerName: '张晓', customerPhone: '13912345678', venueId: 1, venueName: '室内攀岩馆-A区', venueAddress: '极限运动馆1层A区', bookingDate: '2026-07-18', startTime: '10:00:00', endTime: '12:00:00', durationHours: 2.0, remark: null, status: 'COMPLETED', source: 'MINI_PROGRAM', checkinTime: '2026-07-18 10:05:00', checkoutTime: '2026-07-18 12:00:00', attendeeCount: 1, feeAmount: 88.0, paymentStatus: 'PAID', paymentMethod: '微信支付', createdAt: '2026-07-17 15:30:00' },
    { id: 2, bookingNo: 'B20260720002', userId: 3, customerName: '张晓', customerPhone: '13912345678', venueId: 4, venueName: '室内滑板训练场', venueAddress: '极限运动馆2层', bookingDate: '2026-07-19', startTime: '14:00:00', endTime: '16:00:00', durationHours: 2.0, remark: null, status: 'COMPLETED', source: 'MINI_PROGRAM', checkinTime: '2026-07-19 14:02:00', checkoutTime: '2026-07-19 16:00:00', attendeeCount: 2, feeAmount: 90.0, paymentStatus: 'PAID', paymentMethod: '微信支付', createdAt: '2026-07-18 09:00:00' },
    { id: 3, bookingNo: 'B20260720003', userId: 4, customerName: '李伟', customerPhone: '13987654321', venueId: 1, venueName: '室内攀岩馆-A区', venueAddress: '极限运动馆1层A区', bookingDate: '2026-07-20', startTime: '09:00:00', endTime: '11:00:00', durationHours: 2.0, remark: '需要安全指导', status: 'APPROVED', source: 'MINI_PROGRAM', checkinTime: null, checkoutTime: null, attendeeCount: 1, feeAmount: 88.0, paymentStatus: 'UNPAID', paymentMethod: null, createdAt: '2026-07-19 11:00:00' },
    { id: 4, bookingNo: 'B20260720004', userId: 5, customerName: '王芳', customerPhone: '13799887766', venueId: 3, venueName: '室外滑板公园', venueAddress: '极限运动馆户外区域', bookingDate: '2026-07-21', startTime: '15:00:00', endTime: '17:00:00', durationHours: 2.0, remark: null, status: 'PENDING', source: 'MINI_PROGRAM', checkinTime: null, checkoutTime: null, attendeeCount: 1, feeAmount: 70.0, paymentStatus: 'UNPAID', paymentMethod: null, createdAt: '2026-07-20 16:00:00' },
    { id: 5, bookingNo: 'B20260720005', userId: 3, customerName: '张晓', customerPhone: '13912345678', venueId: 6, venueName: '高空拓展训练基地', venueAddress: '极限运动馆户外拓展区', bookingDate: '2026-07-22', startTime: '09:00:00', endTime: '12:00:00', durationHours: 3.0, remark: '团队建设', status: 'PENDING', source: 'BACKEND', checkinTime: null, checkoutTime: null, attendeeCount: 10, feeAmount: 360.0, paymentStatus: 'UNPAID', paymentMethod: null, createdAt: '2026-07-20 18:00:00' },
    { id: 6, bookingNo: 'B20260720006', userId: 4, customerName: '李伟', customerPhone: '13987654321', venueId: 2, venueName: '室内攀岩馆-B区', venueAddress: '极限运动馆1层B区', bookingDate: '2026-07-16', startTime: '14:00:00', endTime: '16:00:00', durationHours: 2.0, remark: null, status: 'CANCELLED', source: 'MINI_PROGRAM', checkinTime: null, checkoutTime: null, attendeeCount: 1, feeAmount: 0.0, paymentStatus: 'UNPAID', paymentMethod: null, createdAt: '2026-07-15 10:00:00' },
    { id: 7, bookingNo: 'B20260720007', userId: 5, customerName: '王芳', customerPhone: '13799887766', venueId: 5, venueName: '蹦极跳台', venueAddress: '极限运动馆户外高空区', bookingDate: '2026-07-17', startTime: '10:00:00', endTime: '10:30:00', durationHours: 0.5, remark: null, status: 'REJECTED', source: 'MINI_PROGRAM', checkinTime: null, checkoutTime: null, attendeeCount: 1, feeAmount: 0.0, paymentStatus: 'UNPAID', paymentMethod: null, createdAt: '2026-07-16 13:00:00' }
  ],
  activityCategories: [
    { id: 1, categoryCode: 'AC001', categoryName: '赛事活动', categoryDesc: '各类极限运动比赛和竞技活动', sortOrder: 1 },
    { id: 2, categoryCode: 'AC002', categoryName: '体验活动', categoryDesc: '面向初学者的体验活动', sortOrder: 2 },
    { id: 3, categoryCode: 'AC003', categoryName: '培训课程', categoryDesc: '极限运动技能培训课程', sortOrder: 3 },
    { id: 4, categoryCode: 'AC004', categoryName: '会员活动', categoryDesc: '面向会员的专属活动', sortOrder: 4 }
  ],
  activities: [
    { id: 1, activityNo: 'A20260720001', title: '第七届极限攀岩挑战赛', coverImage: '', categoryId: 1, categoryName: '赛事活动', location: '极限运动馆1层攀岩区', startTime: '2026-08-15 09:00:00', endTime: '2026-08-20 18:00:00', status: 'NOT_STARTED', registrationMethod: 'ONLINE', maxParticipants: 100, awards: '冠军：5000元+奖杯', rules: '1.年满16周岁；2.自备攀岩鞋和安全装备', description: '年度攀岩赛事，设难度赛/速度赛/抱石赛。', contact: '0756-8888100', feeType: 'FREE', feeAmount: 0.0, createdAt: '2026-07-20 10:00:00' },
    { id: 2, activityNo: 'A20260720002', title: '滑板新手体验日', coverImage: '', categoryId: 2, categoryName: '体验活动', location: '极限运动馆2层滑板训练场', startTime: '2026-08-10 14:00:00', endTime: '2026-08-10 18:00:00', status: 'NOT_STARTED', registrationMethod: 'ONLINE', maxParticipants: 30, awards: '参与即赠送滑板护具一套', rules: '1.无需滑板经验；2.场馆提供装备', description: '专业教练指导，零基础学滑板。', contact: '0756-8888101', feeType: 'FREE', feeAmount: 0.0, createdAt: '2026-07-20 10:00:00' },
    { id: 3, activityNo: 'A20260720003', title: '高空绳索救援培训课程', coverImage: '', categoryId: 3, categoryName: '培训课程', location: '极限运动馆户外拓展区', startTime: '2026-08-01 09:00:00', endTime: '2026-08-05 17:00:00', status: 'IN_PROGRESS', registrationMethod: 'ONLINE', maxParticipants: 20, awards: '考核通过颁发证书', rules: '1.具备基本攀爬能力；2.课程为期5天', description: '系统学习高空绳索救援技术。', contact: '0756-8888102', feeType: 'CHARGE', feeAmount: 500.0, createdAt: '2026-07-20 10:00:00' },
    { id: 4, activityNo: 'A20260720004', title: '周年庆会员活动', coverImage: '', categoryId: 4, categoryName: '会员活动', location: '极限运动馆全馆', startTime: '2026-09-01 10:00:00', endTime: '2026-09-03 22:00:00', status: 'NOT_STARTED', registrationMethod: 'ONLINE', maxParticipants: 200, awards: '现场抽奖', rules: '1.仅限会员；2.可携带一位亲友', description: '周年庆狂欢，全场免费体验。', contact: '0756-8888103', feeType: 'FREE', feeAmount: 0.0, createdAt: '2026-07-20 10:00:00' }
  ],
  registrations: [
    { id: 1, registrationNo: 'REG20260720001', activityId: 1, activityTitle: '第七届极限攀岩挑战赛', userId: 3, customerName: '张晓', customerPhone: '13912345678', activityType: '个人参赛', remark: '参加难度赛', auditStatus: 'APPROVED', auditComment: null, auditTime: '2026-07-20 14:00:00', createdAt: '2026-07-20 12:00:00' },
    { id: 2, registrationNo: 'REG20260720002', activityId: 1, activityTitle: '第七届极限攀岩挑战赛', userId: 4, customerName: '李伟', customerPhone: '13987654321', activityType: '个人参赛', remark: '参加速度赛', auditStatus: 'APPROVED', auditComment: null, auditTime: '2026-07-20 14:00:00', createdAt: '2026-07-20 12:00:00' },
    { id: 3, registrationNo: 'REG20260720003', activityId: 2, activityTitle: '滑板新手体验日', userId: 5, customerName: '王芳', customerPhone: '13799887766', activityType: '个人体验', remark: '完全新手', auditStatus: 'PENDING', auditComment: null, auditTime: null, createdAt: '2026-07-21 09:00:00' },
    { id: 4, registrationNo: 'REG20260720004', activityId: 3, activityTitle: '高空绳索救援培训课程', userId: 3, customerName: '张晓', customerPhone: '13912345678', activityType: '培训学员', remark: null, auditStatus: 'APPROVED', auditComment: null, auditTime: '2026-07-20 15:00:00', createdAt: '2026-07-20 11:00:00' }
  ],
  equipmentCategories: [
    { id: 1, categoryCode: 'EC001', categoryName: '攀岩设备', categoryDesc: '攀岩运动所需的安全和辅助设备' },
    { id: 2, categoryCode: 'EC002', categoryName: '滑板设备', categoryDesc: '滑板运动所需设备' },
    { id: 3, categoryCode: 'EC003', categoryName: '安全装备', categoryDesc: '各类安全防护装备' },
    { id: 4, categoryCode: 'EC004', categoryName: '急救设备', categoryDesc: '医疗急救相关设备' }
  ],
  equipments: [
    { id: 1, equipmentNo: 'E20260720001', name: 'Petzl安全绳-动力绳', categoryId: 1, categoryName: '攀岩设备', brand: 'Petzl', model: 'AXIS 11mm', serialNumber: 'PT20260001', location: '攀岩馆器材室A', responsiblePerson: '管理员', quantity: 20, availableQuantity: 18, status: 'NORMAL', createdAt: '2026-07-20 10:00:00' },
    { id: 2, equipmentNo: 'E20260720002', name: 'Black Diamond安全带', categoryId: 1, categoryName: '攀岩设备', brand: 'Black Diamond', model: 'Momentum', serialNumber: 'BD20260001', location: '攀岩馆器材室A', responsiblePerson: '管理员', quantity: 15, availableQuantity: 15, status: 'NORMAL', createdAt: '2026-07-20 10:00:00' },
    { id: 3, equipmentNo: 'E20260720003', name: '攀岩头盔', categoryId: 3, categoryName: '安全装备', brand: 'Petzl', model: 'ELIA', serialNumber: 'PT20260002', location: '攀岩馆器材室B', responsiblePerson: '管理员', quantity: 25, availableQuantity: 23, status: 'NORMAL', createdAt: '2026-07-20 10:00:00' },
    { id: 4, equipmentNo: 'E20260720004', name: '专业滑板-成人款', categoryId: 2, categoryName: '滑板设备', brand: 'Element', model: 'Section 8.0', serialNumber: 'EL20260001', location: '滑板区储物柜', responsiblePerson: '管理员', quantity: 30, availableQuantity: 28, status: 'NORMAL', createdAt: '2026-07-20 10:00:00' },
    { id: 5, equipmentNo: 'E20260720006', name: '自动体外除颤器(AED)', categoryId: 4, categoryName: '急救设备', brand: '飞利浦', model: 'HeartStart FRx', serialNumber: 'PH20260001', location: '场馆服务台', responsiblePerson: '管理员', quantity: 2, availableQuantity: 2, status: 'NORMAL', createdAt: '2026-07-20 10:00:00' }
  ],
  purchases: [
    { id: 1, purchaseNo: 'P20260720001', equipmentId: 1, equipmentName: 'Petzl安全绳-动力绳', brand: 'Petzl', model: 'AXIS 11mm', quantity: 20, purchasePrice: 12000.0, purchaseDate: '2026-06-01', supplier: '北京户外装备有限公司', createdAt: '2026-07-20 10:00:00' },
    { id: 2, purchaseNo: 'P20260720002', equipmentId: 2, equipmentName: 'Black Diamond安全带', brand: 'Black Diamond', model: 'Momentum', quantity: 15, purchasePrice: 7500.0, purchaseDate: '2026-06-01', supplier: '北京户外装备有限公司', createdAt: '2026-07-20 10:00:00' }
  ],
  rentals: [
    { id: 1, rentalNo: 'R20260720001', equipmentId: 1, equipmentName: 'Petzl安全绳-动力绳', quantity: 2, startTime: '2026-07-18 09:00:00', endTime: '2026-07-18 18:00:00', borrower: '赵六', contactPhone: '13855667788', rentalPrice: 100.0, status: 'RETURNED', actualReturnTime: '2026-07-18 17:30:00', createdAt: '2026-07-18 08:00:00' },
    { id: 2, rentalNo: 'R20260720002', equipmentId: 4, equipmentName: '专业滑板-成人款', quantity: 2, startTime: '2026-07-19 14:00:00', endTime: '2026-07-19 17:00:00', borrower: '孙七', contactPhone: '13999887766', rentalPrice: 60.0, status: 'RENTED', actualReturnTime: null, createdAt: '2026-07-19 13:00:00' }
  ],
  maintenances: [
    { id: 1, maintenanceNo: 'M20260720001', title: '安全绳年度检测', equipmentId: 1, equipmentName: 'Petzl安全绳-动力绳', maintenanceTime: '2026-07-01 09:00:00', content: '对所有动力绳进行年度安全检测', personnel: '设备维护部', equipmentCondition: 'NORMAL', createdAt: '2026-07-01 10:00:00' },
    { id: 2, maintenanceNo: 'M20260720002', title: '滑板支架更换', equipmentId: 4, equipmentName: '专业滑板-成人款', maintenanceTime: '2026-06-25 14:00:00', content: '更换专业滑板支架和轮子', personnel: '设备维护部', equipmentCondition: 'NORMAL', createdAt: '2026-06-25 15:00:00' }
  ],
  announcements: [
    { id: 1, announcementNo: 'N20260720001', title: '暑期营业时间调整通知', summary: '自7月15日起营业时间调整为每日9:00-22:00', coverImage: '', content: '<p>为满足暑期高峰需求，营业时间调整为 9:00-22:00。</p>', isTop: 1, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' },
    { id: 2, announcementNo: 'N20260720002', title: '第七届攀岩挑战赛报名开启', summary: '年度重磅赛事报名正式启动，冠军奖金5000元', coverImage: '', content: '<p>第七届极限攀岩挑战赛将于8月15-20日举行，现已开放报名。</p>', isTop: 1, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' },
    { id: 3, announcementNo: 'N20260720003', title: '场馆设备维护升级通知', summary: '7月22-24日部分区域暂停开放', coverImage: '', content: '<p>7月22-24日对攀岩B区、滑板公园进行维护。</p>', isTop: 0, status: 'PUBLISHED', createdAt: '2026-07-20 10:00:00' },
    { id: 4, announcementNo: 'N20260720004', title: '新会员注册优惠活动', summary: '新注册会员享首次预约8折优惠', coverImage: '', content: '<p>即日起至9月30日，新会员首次预约8折。</p>', isTop: 0, status: 'DRAFT', createdAt: '2026-07-20 10:00:00' }
  ]
}
 
// 模糊过滤工具
function like(list, key, kw) {
  if (!kw) return list
  return list.filter((x) => (x[key] || '').toString().toLowerCase().includes(kw.toString().toLowerCase()))
}
function eq(list, key, val) {
  if (val === undefined || val === null || val === '') return list
  return list.filter((x) => x[key] === val)
}
function upsert(list, body, idKey = 'id') {
  if (body[idKey]) {
    const i = list.findIndex((x) => x[idKey] === body[idKey])
    if (i >= 0) list[i] = { ...list[i], ...body }
    return list[i]
  }
  const item = { ...body, [idKey]: ++_uid, createdAt: '2026-07-21 10:00:00' }
  list.push(item)
  return item
}
function removeById(list, id) {
  const i = list.findIndex((x) => x.id === id)
  if (i >= 0) list.splice(i, 1)
  return true
}
 
export function mockHandle(url, method, body = {}) {
  console.log(`[MOCK] ${method.toUpperCase()} ${url}`, body)
  const seg = url.split('?')[0]
 
  // ===== 登录 =====
  if (seg === '/auth/login' && method === 'post') {
    const u = db.users.find((x) => x.username === body.username)
    if (u && body.password === '123456') {
      return ok({ token: 'mock-jwt-token-' + u.id, userInfo: { id: u.id, username: u.username, realName: u.realName, phone: u.phone, gender: u.gender, roleId: u.roleId, roleName: u.roleName } })
    }
    return { code: 400, message: '用户名或密码错误', data: null }
  }
 
  // ===== 用户 =====
  if (seg === '/users' && method === 'post') {
    let r = db.users
    r = like(r, 'username', body.username); r = like(r, 'realName', body.realName); r = like(r, 'phone', body.phone)
    r = eq(r, 'roleId', body.roleId); r = eq(r, 'status', body.status)
    return ok(page(r))
  }
  if (seg === '/user' && method === 'post') return ok(upsert(db.users, body).id)
  if (/^\/user\/\d+$/.test(seg) && method === 'put') return ok(upsert(db.users, { ...body, id: Number(seg.split('/')[2]) }))
  if (/^\/user\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.users, Number(seg.split('/')[2])))
  if (/^\/user\/\d+\/reset-password$/.test(seg) && method === 'put') return ok(true)
 
  // ===== 场地分类 =====
  if (seg === '/venue-categories' && method === 'post') return ok(page(like(db.venueCategories, 'categoryName', body.categoryName)))
  if (seg === '/venue-category' && method === 'post') return ok(upsert(db.venueCategories, body).id)
  if (/^\/venue-category\/\d+$/.test(seg) && method === 'put') return ok(upsert(db.venueCategories, { ...body, id: Number(seg.split('/')[2]) }))
  if (/^\/venue-category\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.venueCategories, Number(seg.split('/')[2])))
 
  // ===== 场地 =====
  if (seg === '/venues' && method === 'post') {
    let r = db.venues
    r = like(r, 'venueName', body.venueName); r = like(r, 'venueAddress', body.venueAddress)
    r = eq(r, 'categoryId', body.categoryId); r = eq(r, 'status', body.status)
    return ok(page(r))
  }
  if (seg === '/venue' && method === 'post') {
    const cat = db.venueCategories.find((c) => c.id === body.categoryId)
    return ok(upsert(db.venues, { ...body, categoryName: cat ? cat.categoryName : '' }).id)
  }
  if (/^\/venue\/\d+$/.test(seg) && method === 'put') {
    const cat = db.venueCategories.find((c) => c.id === body.categoryId)
    return ok(upsert(db.venues, { ...body, id: Number(seg.split('/')[2]), categoryName: cat ? cat.categoryName : body.categoryName }))
  }
  if (/^\/venue\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.venues, Number(seg.split('/')[2])))
  if (/^\/venue\/\d+$/.test(seg) && method === 'get') return ok(db.venues.find((v) => v.id === Number(seg.split('/')[2])) || {})
 
  // ===== 场地预约 =====
  if (seg === '/bookings' && method === 'post') {
    let r = db.bookings
    r = like(r, 'venueName', body.venueName); r = like(r, 'customerName', body.customerName); r = like(r, 'customerPhone', body.customerPhone)
    r = eq(r, 'status', body.status); r = eq(r, 'paymentStatus', body.paymentStatus); r = eq(r, 'userId', body.userId)
    return ok(page(r))
  }
  if (seg === '/booking' && method === 'post') {
    const v = db.venues.find((x) => x.id === body.venueId)
    const item = upsert(db.bookings, { ...body, bookingNo: 'B' + Date.now(), venueName: v ? v.venueName : '', venueAddress: v ? v.venueAddress : '', status: 'PENDING', paymentStatus: 'UNPAID', source: 'MINI_PROGRAM', feeAmount: v ? v.feeAmount : 0 })
    return ok({ id: item.id, bookingNo: item.bookingNo, status: item.status, feeAmount: item.feeAmount })
  }
  if (/^\/booking\/\d+\/cancel$/.test(seg) && method === 'put') { const b = db.bookings.find(x => x.id === Number(seg.split('/')[2])); if (b) b.status = 'CANCELLED'; return ok(true) }
  if (/^\/booking\/\d+\/checkin$/.test(seg) && method === 'put') { const b = db.bookings.find(x => x.id === Number(seg.split('/')[2])); if (b) { b.checkinTime = body.checkinTime || '2026-07-21 10:00:00'; b.attendeeCount = body.attendeeCount || 1; b.status = 'APPROVED' } return ok(true) }
  if (/^\/booking\/\d+\/checkout$/.test(seg) && method === 'put') { const b = db.bookings.find(x => x.id === Number(seg.split('/')[2])); if (b) { b.checkoutTime = body.checkoutTime || '2026-07-21 12:00:00'; b.status = 'COMPLETED' } return ok({ feeAmount: b ? b.feeAmount : 0, feeDetail: '按收费规则计算' }) }
  if (/^\/booking\/\d+\/pay$/.test(seg) && method === 'put') { const b = db.bookings.find(x => x.id === Number(seg.split('/')[2])); if (b) { b.paymentStatus = 'PAID'; b.paymentMethod = body.paymentMethod || '微信支付' } return ok(true) }
  if (/^\/booking\/\d+\/payment-status$/.test(seg) && method === 'put') { const b = db.bookings.find(x => x.id === Number(seg.split('/')[2])); if (b) b.paymentStatus = body.paymentStatus; return ok(true) }
  if (/^\/booking\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.bookings, Number(seg.split('/')[2])))
 
  // ===== 活动分类 / 活动 =====
  if (seg === '/activity-categories' && method === 'post') return ok(page(db.activityCategories))
  if (seg === '/activities' && method === 'post') {
    let r = db.activities
    r = like(r, 'title', body.title); r = eq(r, 'categoryId', body.categoryId); r = eq(r, 'status', body.status)
    return ok(page(r))
  }
  if (seg === '/activity' && method === 'post') {
    const cat = db.activityCategories.find((c) => c.id === body.categoryId)
    return ok(upsert(db.activities, { ...body, activityNo: 'A' + Date.now(), categoryName: cat ? cat.categoryName : '', status: body.status || 'NOT_STARTED' }).id)
  }
  if (/^\/activity\/\d+$/.test(seg) && method === 'put') return ok(upsert(db.activities, { ...body, id: Number(seg.split('/')[2]) }))
  if (/^\/activity\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.activities, Number(seg.split('/')[2])))
  if (/^\/activity\/\d+$/.test(seg) && method === 'get') return ok(db.activities.find((a) => a.id === Number(seg.split('/')[2])) || {})
 
  // ===== 活动报名 =====
  if (seg === '/activity-registrations' && method === 'post') {
    let r = db.registrations
    r = like(r, 'activityTitle', body.activityTitle); r = eq(r, 'auditStatus', body.auditStatus); r = eq(r, 'activityId', body.activityId); r = eq(r, 'userId', body.userId)
    return ok(page(r))
  }
  if (seg === '/activity-registration' && method === 'post') {
    const a = db.activities.find((x) => x.id === body.activityId)
    const item = upsert(db.registrations, { ...body, registrationNo: 'REG' + Date.now(), activityTitle: a ? a.title : '', auditStatus: 'PENDING' })
    return ok({ id: item.id, registrationNo: item.registrationNo, auditStatus: item.auditStatus })
  }
  if (/^\/activity-registration\/\d+\/audit$/.test(seg) && method === 'put') { const r = db.registrations.find(x => x.id === Number(seg.split('/')[2])); if (r) { r.auditStatus = body.auditStatus; r.auditComment = body.auditComment } return ok(true) }
  if (/^\/activity-registration\/\d+\/cancel$/.test(seg) && method === 'put') { const r = db.registrations.find(x => x.id === Number(seg.split('/')[2])); if (r) r.auditStatus = 'CANCELLED'; return ok(true) }
 
  // ===== 设备分类 / 清单 / 购买 / 租用 / 维护 =====
  if (seg === '/equipment-categories' && method === 'post') return ok(page(db.equipmentCategories))
  if (seg === '/equipment-list' && method === 'post') {
    let r = db.equipments
    r = like(r, 'name', body.name); r = eq(r, 'categoryId', body.categoryId); r = eq(r, 'status', body.status)
    return ok(page(r))
  }
  if (seg === '/equipment' && method === 'post') {
    const cat = db.equipmentCategories.find((c) => c.id === body.categoryId)
    return ok(upsert(db.equipments, { ...body, equipmentNo: 'E' + Date.now(), categoryName: cat ? cat.categoryName : '', availableQuantity: body.quantity || 1 }).id)
  }
  if (/^\/equipment\/\d+$/.test(seg) && method === 'put') return ok(upsert(db.equipments, { ...body, id: Number(seg.split('/')[2]) }))
  if (/^\/equipment\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.equipments, Number(seg.split('/')[2])))
  if (seg === '/equipment-purchases' && method === 'post') return ok(page(db.purchases))
  if (seg === '/equipment-purchase' && method === 'post') return ok(upsert(db.purchases, { ...body, purchaseNo: 'P' + Date.now() }).id)
  if (seg === '/equipment-rentals' && method === 'post') { let r = db.rentals; r = like(r, 'borrower', body.borrower); r = eq(r, 'status', body.status); return ok(page(r)) }
  if (seg === '/equipment-rental' && method === 'post') return ok(upsert(db.rentals, { ...body, rentalNo: 'R' + Date.now(), status: 'RENTED' }))
  if (/^\/equipment-rental\/\d+\/return$/.test(seg) && method === 'put') { const r = db.rentals.find(x => x.id === Number(seg.split('/')[2])); if (r) { r.status = 'RETURNED'; r.actualReturnTime = body.actualReturnTime || '2026-07-21 18:00:00' } return ok(true) }
  if (seg === '/equipment-maintenances' && method === 'post') { let r = db.maintenances; r = eq(r, 'equipmentId', body.equipmentId); return ok(page(r)) }
  if (seg === '/equipment-maintenance' && method === 'post') return ok(upsert(db.maintenances, { ...body, maintenanceNo: 'M' + Date.now() }).id)
 
  // ===== 公告 =====
  if (seg === '/announcements' && method === 'post') {
    let r = db.announcements
    r = like(r, 'title', body.title); r = eq(r, 'status', body.status)
    return ok(page(r))
  }
  if (seg === '/announcement' && method === 'post') return ok(upsert(db.announcements, { ...body, announcementNo: 'N' + Date.now() }).id)
  if (/^\/announcement\/\d+$/.test(seg) && method === 'put') return ok(upsert(db.announcements, { ...body, id: Number(seg.split('/')[2]) }))
  if (/^\/announcement\/\d+$/.test(seg) && method === 'delete') return ok(removeById(db.announcements, Number(seg.split('/')[2])))
  if (/^\/announcement\/\d+\/offline$/.test(seg) && method === 'put') { const a = db.announcements.find(x => x.id === Number(seg.split('/')[2])); if (a) a.status = 'OFFLINE'; return ok(true) }
  if (/^\/announcement\/\d+\/toggle-top$/.test(seg) && method === 'put') { const a = db.announcements.find(x => x.id === Number(seg.split('/')[2])); if (a) a.isTop = a.isTop ? 0 : 1; return ok(true) }
 
  // ===== 统计 =====
  if (seg === '/statistics/venue-utilization' && method === 'post') {
    return ok({ venueList: db.venues.map((v) => ({ venueId: v.id, venueName: v.venueName, totalSlots: 60, bookedSlots: 30 + v.id * 3, executedSlots: 28 + v.id * 3, utilizationRate: (50 + v.id * 4) + '%', conversionRate: '93%' })) })
  }
  if (seg === '/statistics/activity-registration' && method === 'post') {
    return ok({ activityList: db.activities.map((a) => ({ activityId: a.id, activityTitle: a.title, totalRegistrations: 20 + a.id * 10, approvedCount: 12 + a.id * 8, approvalRate: (60 + a.id * 5) + '%' })) })
  }
  if (seg === '/authors' && method === 'get') {
    return ok([{ studentId: '12023110001', name: '张三', year: 2023, className: '23计科2班' }])
  }
 
  return ok({})
}
