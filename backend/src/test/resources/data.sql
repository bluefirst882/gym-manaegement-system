-- 1. 角色数据
INSERT INTO sys_role (role_code, role_name, role_desc) VALUES
('ADMIN', '系统管理员', '拥有系统全部操作权限'),
('USER', '普通客户', '通过小程序使用场馆服务');

-- 2. 用户数据 (密码均为加密后的 "123456")
INSERT INTO sys_user (username, password, real_name, phone, gender, birthday, role_id, status) VALUES
('admin', '$2b$10$V6/GYy3jrHbX8bh9PafNM.X9OdeUbZ.CKRzIMNFejDkbo1pD8etES', '系统管理员', '13800000001', '男', '1990-01-01', 1, 1),
('manager01', '$2b$10$V6/GYy3jrHbX8bh9PafNM.X9OdeUbZ.CKRzIMNFejDkbo1pD8etES', '张三', '13800000002', '男', '1992-05-15', 1, 1),
('zhangxiao', '$2b$10$V6/GYy3jrHbX8bh9PafNM.X9OdeUbZ.CKRzIMNFejDkbo1pD8etES', '张晓', '13912345678', '女', '1998-08-20', 2, 1),
('liwei', '$2b$10$V6/GYy3jrHbX8bh9PafNM.X9OdeUbZ.CKRzIMNFejDkbo1pD8etES', '李伟', '13987654321', '男', '2000-03-12', 2, 1),
('wangfang', '$2b$10$V6/GYy3jrHbX8bh9PafNM.X9OdeUbZ.CKRzIMNFejDkbo1pD8etES', '王芳', '13799887766', '女', '2001-11-05', 2, 1),
('test_customer', '$2b$10$V6/GYy3jrHbX8bh9PafNM.X9OdeUbZ.CKRzIMNFejDkbo1pD8etES', '测试客户', '13600000000', '男', '1999-06-30', 2, 1);

-- 3. 场地分类数据
INSERT INTO venue_category (category_code, category_name, category_desc, sort_order) VALUES
('VC001', '攀岩类', '攀岩运动相关场地，按次收费', 1),
('VC002', '滑板类', '滑板运动相关场地，按小时收费', 2),
('VC003', '蹦极类', '蹦极运动相关场地', 3),
('VC004', '高空拓展类', '高空拓展训练相关场地', 4);

-- 4. 场地数据
INSERT INTO venue (venue_code, venue_name, venue_address, contact_phone, category_id, dimensions, material, capacity, facilities, fee_type, fee_amount, cover_image, status) VALUES
('V20260720001', '室内攀岩馆-A区', '极限运动馆1层A区', '0756-8888001', 1, '30m×20m×15m', '人工岩壁/高强度树脂', 50, '攀岩墙、安全绳、防护垫、空调', 'ONCE', 88.00, '/images/venues/climbing_a.jpg', 'AVAILABLE'),
('V20260720002', '室内攀岩馆-B区', '极限运动馆1层B区', '0756-8888001', 1, '25m×18m×12m', '人工岩壁/高强度树脂', 30, '攀岩墙、速度赛道、防护垫', 'ONCE', 68.00, '/images/venues/climbing_b.jpg', 'AVAILABLE'),
('V20260720003', '室外滑板公园', '极限运动馆户外区域', '0756-8888002', 2, '50m×30m×0m', '高强度水泥/不锈钢栏杆', 80, 'U型池、栏杆、台阶、斜坡', 'PER_HOUR', 35.00, '/images/venues/skatepark.jpg', 'AVAILABLE'),
('V20260720004', '室内滑板训练场', '极限运动馆2层', '0756-8888002', 2, '40m×25m×8m', '木质地板/金属框架', 40, '迷你坡道、街式道具、空调', 'PER_HOUR', 45.00, '/images/venues/skate_indoor.jpg', 'AVAILABLE'),
('V20260720005', '蹦极跳台', '极限运动馆户外高空区', '0756-8888003', 3, '10m×10m×50m', '钢结构/高强度绳索', 10, '跳台、安全绳系统、缓冲气垫', 'ONCE', 198.00, '/images/venues/bungee.jpg', 'AVAILABLE'),
('V20260720006', '高空拓展训练基地', '极限运动馆户外拓展区', '0756-8888004', 4, '60m×40m×12m', '钢结构/钢缆/木质平台', 60, '高空断桥、攀岩墙、空中抓杠、毕业墙', 'PER_HOUR', 120.00, '/images/venues/outdoor_training.jpg', 'AVAILABLE');

-- 5. 活动分类数据
INSERT INTO activity_category (category_code, category_name, category_desc, sort_order) VALUES
('AC001', '赛事活动', '各类极限运动比赛和竞技活动', 1),
('AC002', '体验活动', '面向初学者的极限运动体验活动', 2),
('AC003', '培训课程', '极限运动技能培训课程', 3),
('AC004', '会员活动', '面向会员的专属活动', 4);

-- 6. 活动数据
INSERT INTO activity (activity_no, title, cover_image, category_id, location, start_time, end_time, status, registration_method, max_participants, awards, rules, description, contact, fee_type) VALUES
('A20260720001', '第七届极限攀岩挑战赛', '/images/activities/climbing_comp.jpg', 1, '极限运动馆1层攀岩区', '2026-08-15 09:00:00', '2026-08-20 18:00:00', 'NOT_STARTED', 'ONLINE', 100, '冠军：5000元+奖杯', '1.参赛者需年满16周岁', '第七届极限攀岩挑战赛', '0756-8888100', 'FREE'),
('A20260720002', '滑板新手体验日', '/images/activities/skate_beginner.jpg', 2, '极限运动馆2层滑板训练场', '2026-08-10 14:00:00', '2026-08-10 18:00:00', 'NOT_STARTED', 'ONLINE', 30, '参与即赠送滑板护具一套', '1.无需滑板经验', '专业教练指导', '0756-8888101', 'FREE');

-- 7. 设备分类数据
INSERT INTO equipment_category (category_code, category_name, category_desc) VALUES
('EC001', '攀岩设备', '攀岩运动所需的安全和辅助设备'),
('EC002', '滑板设备', '滑板运动所需设备'),
('EC003', '安全装备', '各类安全防护装备'),
('EC004', '急救设备', '医疗急救相关设备');

-- 8. 设备清单数据
INSERT INTO equipment (equipment_no, name, category_id, brand, model, serial_number, location, responsible_person, quantity, available_quantity, status) VALUES
('E20260720001', 'Petzl安全绳-动力绳', 1, 'Petzl', 'AXIS 11mm', 'PT20260001', '攀岩馆器材室A', '管理员', 20, 18, 'NORMAL'),
('E20260720002', 'Black Diamond安全带', 1, 'Black Diamond', 'Momentum', 'BD20260001', '攀岩馆器材室A', '管理员', 15, 15, 'NORMAL'),
('E20260720003', '攀岩头盔', 3, 'Petzl', 'ELIA', 'PT20260002', '攀岩馆器材室B', '管理员', 25, 23, 'NORMAL'),
('E20260720004', '专业滑板-成人款', 2, 'Element', 'Section 8.0', 'EL20260001', '滑板区储物柜', '管理员', 30, 28, 'NORMAL'),
('E20260720005', '滑板护具套装', 3, 'Triple Eight', 'Sweatsaver', 'TT20260001', '滑板区储物柜', '管理员', 30, 30, 'NORMAL'),
('E20260720006', '自动体外除颤器(AED)', 4, '飞利浦', 'HeartStart FRx', 'PH20260001', '场馆服务台', '管理员', 2, 2, 'NORMAL');

-- 9. 设备购买记录
INSERT INTO equipment_purchase (purchase_no, equipment_id, equipment_name, brand, model, serial_number, quantity, purchase_price, purchase_date, supplier) VALUES
('P20260720001', 1, 'Petzl安全绳-动力绳', 'Petzl', 'AXIS 11mm', 'PT20260001', 20, 12000.00, '2026-06-01', '北京户外装备有限公司'),
('P20260720002', 2, 'Black Diamond安全带', 'Black Diamond', 'Momentum', 'BD20260001', 15, 7500.00, '2026-06-01', '北京户外装备有限公司');

-- 10. 设备租用记录
INSERT INTO equipment_rental (rental_no, equipment_id, quantity, start_time, end_time, borrower, contact_phone, rental_price, status, actual_return_time) VALUES
('R20260720001', 1, 2, '2026-07-18 09:00:00', '2026-07-18 18:00:00', '赵六', '13855667788', 100.00, 'RETURNED', '2026-07-18 17:30:00'),
('R20260720002', 4, 2, '2026-07-19 14:00:00', '2026-07-19 17:00:00', '孙七', '13999887766', 60.00, 'RENTED', NULL);

-- 11. 设备维护记录
INSERT INTO equipment_maintenance (maintenance_no, title, equipment_id, maintenance_time, content, personnel, equipment_condition) VALUES
('M20260720001', '安全绳年度检测', 1, '2026-07-01 09:00:00', '对所有动力绳进行年度安全检测', '设备维护部', 'NORMAL'),
('M20260720002', '滑板支架更换', 4, '2026-06-25 14:00:00', '更换专业滑板支架和轮子', '设备维护部', 'NORMAL');

-- 12. 公告数据
INSERT INTO announcement (announcement_no, title, summary, cover_image, content, is_top, status) VALUES
('N20260720001', '极限运动馆2026年暑期营业时间调整通知', '营业时间调整为每日9:00-22:00', '/images/announcements/summer_hours.jpg', '<p>亲爱的顾客朋友们...</p>', 1, 'PUBLISHED'),
('N20260720002', '第七届攀岩挑战赛报名开启', '年度重磅赛事报名正式启动', '/images/announcements/climbing_comp.jpg', '<p>第七届极限攀岩挑战赛...</p>', 1, 'PUBLISHED');

-- 13. 预约数据
INSERT INTO booking (booking_no, user_id, venue_id, venue_name, venue_address, booking_date, start_time, end_time, duration_hours, status, fee_amount, payment_status) VALUES
('B20260720001', 3, 1, '室内攀岩馆-A区', '极限运动馆1层A区', '2026-07-18', '10:00:00', '12:00:00', 2.0, 'COMPLETED', 88.00, 'PAID'),
('B20260720002', 3, 4, '室内滑板训练场', '极限运动馆2层', '2026-07-19', '14:00:00', '16:00:00', 2.0, 'COMPLETED', 90.00, 'PAID'),
('B20260720003', 4, 1, '室内攀岩馆-A区', '极限运动馆1层A区', '2026-07-20', '09:00:00', '11:00:00', 2.0, 'APPROVED', 88.00, 'UNPAID'),
('B20260720004', 5, 3, '室外滑板公园', '极限运动馆户外区域', '2026-07-21', '15:00:00', '17:00:00', 2.0, 'PENDING', 70.00, 'UNPAID');

-- 14. 活动报名数据
INSERT INTO activity_registration (registration_no, activity_id, user_id, activity_type, remark, audit_status) VALUES
('REG20260720001', 1, 3, '个人参赛', '参加难度赛项目', 'APPROVED'),
('REG20260720002', 1, 4, '个人参赛', '参加速度赛项目', 'APPROVED'),
('REG20260720003', 2, 5, '个人体验', '完全新手', 'PENDING');
