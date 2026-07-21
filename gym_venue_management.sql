-- ============================================================
-- 极限运动馆管理系统 - 数据库建表脚本
-- 数据库: MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS extreme_sports_venue DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE extreme_sports_venue;

-- 先禁用外键检查，避免删表时外键约束冲突
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 清空旧表（按依赖顺序：子表先删，父表后删）
-- ============================================================
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS activity_registration;
DROP TABLE IF EXISTS equipment_rental;
DROP TABLE IF EXISTS equipment_maintenance;
DROP TABLE IF EXISTS equipment_purchase;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS venue;
DROP TABLE IF EXISTS announcement;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS venue_category;
DROP TABLE IF EXISTS activity_category;
DROP TABLE IF EXISTS equipment_category;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. 角色表 (sys_role)
-- ============================================================
CREATE TABLE sys_role (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_code   VARCHAR(20)  NOT NULL UNIQUE COMMENT '角色编码',
    role_name   VARCHAR(50)  NOT NULL COMMENT '角色名称',
    role_desc   VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================================
-- 2. 用户表 (sys_user)
-- ============================================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username      VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名(登录名)',
    password      VARCHAR(255) NOT NULL COMMENT '密码(加密存储)',
    real_name     VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    phone         VARCHAR(20)  NOT NULL COMMENT '手机号',
    gender        VARCHAR(10)  DEFAULT NULL COMMENT '性别(男/女)',
    birthday      DATE         DEFAULT NULL COMMENT '出生日期',
    role_id       INT          DEFAULT NULL COMMENT '角色ID',
    status        TINYINT      NOT NULL DEFAULT 1 COMMENT '账户状态(1正常 0已禁用)',
    avatar        VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    registered_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    last_login_at DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================================
-- 3. 场地分类表 (venue_category)
-- ============================================================
DROP TABLE IF EXISTS venue_category;
CREATE TABLE venue_category (
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    category_code VARCHAR(20)  NOT NULL UNIQUE COMMENT '分类编码',
    category_name VARCHAR(50)  NOT NULL COMMENT '分类名称',
    category_desc VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    sort_order    INT          NOT NULL DEFAULT 0 COMMENT '显示顺序',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地分类表';

-- ============================================================
-- 5. 场地表 (venue)
-- ============================================================
DROP TABLE IF EXISTS venue;
CREATE TABLE venue (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '场地ID',
    venue_code      VARCHAR(50)  NOT NULL UNIQUE COMMENT '场地编码(系统自动生成)',
    venue_name      VARCHAR(100) NOT NULL COMMENT '场地名称',
    venue_address   VARCHAR(255) DEFAULT NULL COMMENT '场地地址',
    contact_phone   VARCHAR(20)  DEFAULT NULL COMMENT '联系方式',
    category_id     INT          DEFAULT NULL COMMENT '场地分类ID',
    dimensions      VARCHAR(100) DEFAULT NULL COMMENT '场地尺寸(长×宽×高)',
    material        VARCHAR(100) DEFAULT NULL COMMENT '场地材质',
    capacity        INT          DEFAULT NULL COMMENT '容纳人数',
    facilities      VARCHAR(500) DEFAULT NULL COMMENT '设施设备',
    fee_type        VARCHAR(20)  NOT NULL DEFAULT 'PER_HOUR' COMMENT '费用标准(ONCE按次/PER_HOUR按小时)',
    fee_amount      DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '收费金额',
    cover_image     VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
    detail_images   TEXT         DEFAULT NULL COMMENT '详情图URL(JSON数组)',
    status          VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE' COMMENT '场地状态(AVAILABLE可用/MAINTENANCE维护中/DISABLED已停用)',
    remark          VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES venue_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地表';

-- ============================================================
-- 6. 场地预约表 (booking)
-- ============================================================
DROP TABLE IF EXISTS booking;
CREATE TABLE booking (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '预约ID',
    booking_no      VARCHAR(50)  NOT NULL UNIQUE COMMENT '预约编号(系统自动生成)',
    user_id         INT          NOT NULL COMMENT '预约客户ID',
    venue_id        INT          NOT NULL COMMENT '场地ID',
    venue_name      VARCHAR(100) DEFAULT NULL COMMENT '场地名称(冗余)',
    venue_address   VARCHAR(255) DEFAULT NULL COMMENT '场地地址(冗余)',
    booking_date    DATE         NOT NULL COMMENT '预约日期',
    start_time      TIME         NOT NULL COMMENT '开始时间',
    end_time        TIME         NOT NULL COMMENT '结束时间',
    duration_hours  DECIMAL(5,1) DEFAULT NULL COMMENT '预约时长(小时)',
    remark          VARCHAR(500) DEFAULT NULL COMMENT '备注信息',
    status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '预约状态(PENDING待审核/APPROVED已通过/REJECTED已拒绝/CANCELLED已取消/COMPLETED已完成)',
    source          VARCHAR(20)  NOT NULL DEFAULT 'MINI_PROGRAM' COMMENT '预约来源(MINI_PROGRAM小程序/BACKEND后台)',
    checkin_time    DATETIME     DEFAULT NULL COMMENT '实际到店时间',
    checkout_time   DATETIME     DEFAULT NULL COMMENT '实际离店时间',
    attendee_count  INT          DEFAULT 1 COMMENT '到场人数',
    fee_amount      DECIMAL(10,2) DEFAULT 0.00 COMMENT '费用金额',
    payment_status  VARCHAR(20)  NOT NULL DEFAULT 'UNPAID' COMMENT '缴费状态(UNPAID待支付/PAID已支付/REFUNDED已退款)',
    payment_method  VARCHAR(50)  DEFAULT NULL COMMENT '支付方式',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地预约表';

-- ============================================================
-- 7. 活动分类表 (activity_category)
-- ============================================================
DROP TABLE IF EXISTS activity_category;
CREATE TABLE activity_category (
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    category_code VARCHAR(20)  NOT NULL UNIQUE COMMENT '分类编码',
    category_name VARCHAR(50)  NOT NULL COMMENT '分类名称',
    category_desc VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    sort_order    INT          NOT NULL DEFAULT 0 COMMENT '显示顺序',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动分类表';

-- ============================================================
-- 8. 活动表 (activity)
-- ============================================================
DROP TABLE IF EXISTS activity;
CREATE TABLE activity (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '活动ID',
    activity_no         VARCHAR(50)  NOT NULL UNIQUE COMMENT '活动编号(系统自动生成)',
    title               VARCHAR(200) NOT NULL COMMENT '活动主题',
    cover_image         VARCHAR(255) DEFAULT NULL COMMENT '活动封面图URL',
    carousel_images     TEXT         DEFAULT NULL COMMENT '轮播图URL(JSON数组)',
    category_id         INT          DEFAULT NULL COMMENT '活动分类ID',
    location            VARCHAR(255) NOT NULL COMMENT '活动地点',
    start_time          DATETIME     NOT NULL COMMENT '开始时间',
    end_time            DATETIME     NOT NULL COMMENT '截止时间',
    status              VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' COMMENT '活动状态(DRAFT草稿/NOT_STARTED未开始/IN_PROGRESS进行中/ENDED已结束)',
    registration_method VARCHAR(20)  DEFAULT 'ONLINE' COMMENT '报名方式(ONLINE线上报名/OFFLINE线下报名)',
    max_participants    INT          DEFAULT NULL COMMENT '报名人数上限(NULL则不限制)',
    awards              VARCHAR(500) DEFAULT NULL COMMENT '奖项设置',
    rules               TEXT         NOT NULL COMMENT '活动规则',
    description         TEXT         NOT NULL COMMENT '活动详细介绍(支持富文本)',
    contact             VARCHAR(100) DEFAULT NULL COMMENT '联系方式',
    fee_type            VARCHAR(20)  DEFAULT 'FREE' COMMENT '活动费用(FREE免费/CHARGE收费)',
    fee_amount          DECIMAL(10,2) DEFAULT 0.00 COMMENT '收费金额',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES activity_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- ============================================================
-- 9. 活动报名表 (activity_registration)
-- ============================================================
DROP TABLE IF EXISTS activity_registration;
CREATE TABLE activity_registration (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '报名ID',
    registration_no VARCHAR(50)  NOT NULL UNIQUE COMMENT '报名编号(系统自动生成)',
    activity_id     INT          NOT NULL COMMENT '报名活动ID',
    user_id         INT          NOT NULL COMMENT '报名客户ID',
    activity_type   VARCHAR(50)  DEFAULT NULL COMMENT '活动类型(如个人参赛/团队参赛/观赛等)',
    remark          VARCHAR(500) DEFAULT NULL COMMENT '备注信息',
    audit_status    VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '审核状态(PENDING待审核/APPROVED已通过/REJECTED已拒绝/CANCELLED已取消)',
    audit_comment   VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    audit_time      DATETIME     DEFAULT NULL COMMENT '审核时间',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    FOREIGN KEY (activity_id) REFERENCES activity(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动报名表';

-- ============================================================
-- 10. 设备分类表 (equipment_category)
-- ============================================================
DROP TABLE IF EXISTS equipment_category;
CREATE TABLE equipment_category (
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    category_code VARCHAR(20)  NOT NULL UNIQUE COMMENT '分类编码',
    category_name VARCHAR(50)  NOT NULL COMMENT '分类名称',
    category_desc VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备分类表';

-- ============================================================
-- 11. 设备清单表 (equipment)
-- ============================================================
DROP TABLE IF EXISTS equipment;
CREATE TABLE equipment (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '设备ID',
    equipment_no        VARCHAR(50)  NOT NULL UNIQUE COMMENT '设备编号(系统自动生成)',
    name                VARCHAR(100) NOT NULL COMMENT '设备名称',
    category_id         INT          DEFAULT NULL COMMENT '设备分类ID',
    brand               VARCHAR(100) NOT NULL COMMENT '品牌',
    model               VARCHAR(100) NOT NULL COMMENT '型号',
    serial_number       VARCHAR(100) NOT NULL UNIQUE COMMENT '序列号',
    location            VARCHAR(200) NOT NULL COMMENT '存放位置',
    responsible_person  VARCHAR(50)  NOT NULL COMMENT '责任人',
    quantity            INT          NOT NULL DEFAULT 1 COMMENT '数量',
    available_quantity  INT          NOT NULL DEFAULT 1 COMMENT '可用库存数量(系统自动计算)',
    status              VARCHAR(20)  NOT NULL DEFAULT 'NORMAL' COMMENT '设备状态(NORMAL正常/FAULT故障/SCRAPPED报废/RENTED已借出)',
    cover_image         VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
    detail_images       TEXT         DEFAULT NULL COMMENT '详情图URL(JSON数组)',
    remark              VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES equipment_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备清单表';

-- ============================================================
-- 12. 设备购买记录表 (equipment_purchase)
-- ============================================================
DROP TABLE IF EXISTS equipment_purchase;
CREATE TABLE equipment_purchase (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '购买记录ID',
    purchase_no     VARCHAR(50)  NOT NULL UNIQUE COMMENT '购买编号(系统自动生成)',
    equipment_id    INT          DEFAULT NULL COMMENT '关联设备ID',
    equipment_name  VARCHAR(100) NOT NULL COMMENT '设备名称',
    equipment_type  VARCHAR(50)  DEFAULT NULL COMMENT '设备类型',
    brand           VARCHAR(100) DEFAULT NULL COMMENT '品牌',
    model           VARCHAR(100) DEFAULT NULL COMMENT '型号',
    serial_number   VARCHAR(100) DEFAULT NULL COMMENT '序列号',
    quantity        INT          NOT NULL COMMENT '购买数量',
    purchase_price  DECIMAL(10,2) NOT NULL COMMENT '购买价格',
    purchase_date   DATE         NOT NULL COMMENT '购买日期',
    supplier        VARCHAR(200) DEFAULT NULL COMMENT '供应商',
    invoice_no      VARCHAR(100) DEFAULT NULL COMMENT '发票号',
    remark          VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备购买记录表';

-- ============================================================
-- 13. 设备租用记录表 (equipment_rental)
-- ============================================================
DROP TABLE IF EXISTS equipment_rental;
CREATE TABLE equipment_rental (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '租用记录ID',
    rental_no           VARCHAR(50)  NOT NULL UNIQUE COMMENT '租用编号(系统自动生成)',
    equipment_id        INT          NOT NULL COMMENT '租用设备ID',
    quantity            INT          NOT NULL COMMENT '租用数量',
    start_time          DATETIME     NOT NULL COMMENT '租用开始时间',
    end_time            DATETIME     NOT NULL COMMENT '租用结束时间',
    borrower            VARCHAR(50)  NOT NULL COMMENT '租借人',
    contact_phone       VARCHAR(20)  NOT NULL COMMENT '联系方式',
    rental_price        DECIMAL(10,2) NOT NULL COMMENT '租借价格',
    status              VARCHAR(20)  NOT NULL DEFAULT 'RENTED' COMMENT '租借状态(RENTED借用中/RETURNED已归还/OVERDUE逾期未还)',
    actual_return_time  DATETIME     DEFAULT NULL COMMENT '实际归还时间',
    remark              VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备租用记录表';

-- ============================================================
-- 14. 设备维护记录表 (equipment_maintenance)
-- ============================================================
DROP TABLE IF EXISTS equipment_maintenance;
CREATE TABLE equipment_maintenance (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '维护记录ID',
    maintenance_no      VARCHAR(50)  NOT NULL UNIQUE COMMENT '维护编号(系统自动生成)',
    title               VARCHAR(200) NOT NULL COMMENT '维护标题',
    equipment_id        INT          NOT NULL COMMENT '维护设备ID',
    maintenance_time    DATETIME     NOT NULL COMMENT '维护时间',
    content             TEXT         NOT NULL COMMENT '维护内容',
    personnel           VARCHAR(50)  NOT NULL COMMENT '维护人员',
    equipment_condition VARCHAR(20)  NOT NULL DEFAULT 'NORMAL' COMMENT '设备情况(NORMAL正常/REPAIRING维修中/SCRAPPED报废)',
    images              TEXT         DEFAULT NULL COMMENT '维护图片URL(JSON数组)',
    remark              VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备维护记录表';

-- ============================================================
-- 15. 公告表 (announcement)
-- ============================================================
DROP TABLE IF EXISTS announcement;
CREATE TABLE announcement (
    id                  INT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    announcement_no     VARCHAR(50)  NOT NULL UNIQUE COMMENT '公告编号(系统自动生成)',
    title               VARCHAR(200) NOT NULL COMMENT '公告标题',
    summary             VARCHAR(500) NOT NULL COMMENT '公告简介',
    cover_image         VARCHAR(255) DEFAULT NULL COMMENT '公告封面图URL',
    content             TEXT         NOT NULL COMMENT '公告正文(支持富文本)',
    is_top              TINYINT      NOT NULL DEFAULT 0 COMMENT '是否置顶(1是 0否)',
    status              VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' COMMENT '公告状态(DRAFT草稿/PUBLISHED已发布/OFFLINE已下架)',
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ============================================================
-- 索引创建
-- ============================================================
-- 用户表
CREATE INDEX idx_user_username ON sys_user(username);
CREATE INDEX idx_user_phone ON sys_user(phone);
CREATE INDEX idx_user_role ON sys_user(role_id);
CREATE INDEX idx_user_status ON sys_user(status);

-- 场地表
CREATE INDEX idx_venue_category ON venue(category_id);
CREATE INDEX idx_venue_status ON venue(status);
CREATE INDEX idx_venue_name ON venue(venue_name);

-- 预约表
CREATE INDEX idx_booking_user ON booking(user_id);
CREATE INDEX idx_booking_venue ON booking(venue_id);
CREATE INDEX idx_booking_date ON booking(booking_date);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_booking_payment ON booking(payment_status);
CREATE INDEX idx_booking_no ON booking(booking_no);

-- 活动表
CREATE INDEX idx_activity_category ON activity(category_id);
CREATE INDEX idx_activity_status ON activity(status);
CREATE INDEX idx_activity_time ON activity(start_time, end_time);

-- 报名表
CREATE INDEX idx_reg_activity ON activity_registration(activity_id);
CREATE INDEX idx_reg_user ON activity_registration(user_id);
CREATE INDEX idx_reg_audit ON activity_registration(audit_status);

-- 设备表
CREATE INDEX idx_equipment_category ON equipment(category_id);
CREATE INDEX idx_equipment_status ON equipment(status);

-- 设备租用表
CREATE INDEX idx_rental_equipment ON equipment_rental(equipment_id);
CREATE INDEX idx_rental_status ON equipment_rental(status);

-- 设备维护表
CREATE INDEX idx_maint_equipment ON equipment_maintenance(equipment_id);

-- 公告表
CREATE INDEX idx_announcement_status ON announcement(status);
CREATE INDEX idx_announcement_top ON announcement(is_top);



-- ============================================================
-- 初始数据插入
-- ============================================================

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
('A20260720001', '第七届极限攀岩挑战赛', '/images/activities/climbing_comp.jpg', 1, '极限运动馆1层攀岩区', '2026-08-15 09:00:00', '2026-08-20 18:00:00', 'NOT_STARTED', 'ONLINE', 100, '冠军：5000元+奖杯; 亚军：3000元+奖牌; 季军：1000元+奖牌', '1.参赛者需年满16周岁；2.自备攀岩鞋和安全装备；3.遵守现场工作人员安排', '第七届极限攀岩挑战赛是面向全国攀岩爱好者的年度赛事，设有难度赛、速度赛和抱石赛三个项目。', '0756-8888100', 'FREE'),
('A20260720002', '滑板新手体验日', '/images/activities/skate_beginner.jpg', 2, '极限运动馆2层滑板训练场', '2026-08-10 14:00:00', '2026-08-10 18:00:00', 'NOT_STARTED', 'ONLINE', 30, '参与即赠送滑板护具一套', '1.无需滑板经验；2.场馆提供滑板和护具；3.需穿着运动服装', '专业教练指导，从零开始学习滑板基础技巧，安全有趣！', '0756-8888101', 'FREE'),
('A20260720003', '高空绳索救援培训课程', '/images/activities/rescue_training.jpg', 3, '极限运动馆户外拓展区', '2026-08-01 09:00:00', '2026-08-05 17:00:00', 'IN_PROGRESS', 'ONLINE', 20, '考核通过颁发高空绳索救援证书', '1.报名者需具备基本攀爬能力；2.课程为期5天；3.考核不合格可免费补考一次', '系统学习高空绳索救援技术，包括绳索系统搭建、高空救援技术、团队协作救援等。', '0756-8888102', 'CHARGE'),
('A20260720004', '极限运动馆周年庆会员活动', '/images/activities/anniversary.jpg', 4, '极限运动馆全馆', '2026-09-01 10:00:00', '2026-09-03 22:00:00', 'NOT_STARTED', 'ONLINE', 200, '现场抽奖，大奖为全年免费畅玩卡', '1.仅限会员参加；2.可携带一位亲友', '周年庆狂欢，全场项目免费体验，精彩表演，抽奖不断！', '0756-8888103', 'FREE');

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
('E20260720006', '自动体外除颤器(AED)', 4, '飞利浦', 'HeartStart FRx', 'PH20260001', '场馆服务台', '管理员', 2, 2, 'NORMAL'),
('E20260720007', '急救箱', 4, '国产', '标准型', 'CH20260001', '各区域分布', '管理员', 8, 8, 'NORMAL');

-- 9. 设备购买记录
INSERT INTO equipment_purchase (purchase_no, equipment_id, equipment_name, brand, model, serial_number, quantity, purchase_price, purchase_date, supplier) VALUES
('P20260720001', 1, 'Petzl安全绳-动力绳', 'Petzl', 'AXIS 11mm', 'PT20260001', 20, 12000.00, '2026-06-01', '北京户外装备有限公司'),
('P20260720002', 2, 'Black Diamond安全带', 'Black Diamond', 'Momentum', 'BD20260001', 15, 7500.00, '2026-06-01', '北京户外装备有限公司'),
('P20260720003', 4, '专业滑板-成人款', 'Element', 'Section 8.0', 'EL20260001', 30, 9000.00, '2026-06-15', '上海极限运动用品有限公司'),
('P20260720004', 6, '自动体外除颤器(AED)', '飞利浦', 'HeartStart FRx', 'PH20260001', 2, 30000.00, '2026-05-20', '广州医疗设备有限公司');

-- 10. 设备租用记录
INSERT INTO equipment_rental (rental_no, equipment_id, quantity, start_time, end_time, borrower, contact_phone, rental_price, status, actual_return_time) VALUES
('R20260720001', 1, 2, '2026-07-18 09:00:00', '2026-07-18 18:00:00', '赵六', '13855667788', 100.00, 'RETURNED', '2026-07-18 17:30:00'),
('R20260720002', 4, 2, '2026-07-19 14:00:00', '2026-07-19 17:00:00', '孙七', '13999887766', 60.00, 'RENTED', NULL),
('R20260720003', 3, 5, '2026-07-20 10:00:00', '2026-07-20 16:00:00', '周八', '13766554433', 75.00, 'RENTED', NULL);

-- 11. 设备维护记录
INSERT INTO equipment_maintenance (maintenance_no, title, equipment_id, maintenance_time, content, personnel, equipment_condition) VALUES
('M20260720001', '安全绳年度检测', 1, '2026-07-01 09:00:00', '对所有动力绳进行年度安全检测，检查磨损情况和承重能力，更换2条不合格绳索', '设备维护部', 'NORMAL'),
('M20260720002', '滑板支架更换', 4, '2026-06-25 14:00:00', '更换专业滑板支架和轮子，检查轴承状态', '设备维护部', 'NORMAL'),
('M20260720003', 'AED电池更换', 6, '2026-07-10 10:00:00', '更换AED设备的电池和电极片，确保设备处于可用状态', '设备维护部', 'NORMAL');

-- 12. 公告数据
INSERT INTO announcement (announcement_no, title, summary, cover_image, content, is_top, status) VALUES
('N20260720001', '极限运动馆2026年暑期营业时间调整通知', '自2026年7月15日起，场馆营业时间调整为每日9:00-22:00', '/images/announcements/summer_hours.jpg', '<p>亲爱的顾客朋友们：</p><p>为满足暑期高峰需求，自<strong>2026年7月15日</strong>起，本馆营业时间调整如下：</p><p>周一至周日：<strong>9:00 - 22:00</strong></p><p>请各位合理安排运动时间，如有疑问请致电服务热线：0756-8888000</p>', 1, 'PUBLISHED'),
('N20260720002', '第七届攀岩挑战赛报名开启', '年度重磅赛事报名正式启动，冠军奖金5000元！', '/images/announcements/climbing_comp.jpg', '<p>各位攀岩爱好者：</p><p>第七届极限攀岩挑战赛将于<strong>2026年8月15日-20日</strong>举行，现已开放报名！</p><p>比赛项目：难度赛、速度赛、抱石赛</p><p>报名截止：2026年8月10日</p><p>详情请在活动页面查看。</p>', 1, 'PUBLISHED'),
('N20260720003', '场馆设备维护升级通知', '7月22日-24日部分区域暂停开放，敬请谅解', '/images/announcements/maintenance.jpg', '<p>为提升服务质量，本馆将于<strong>2026年7月22日至24日</strong>对以下区域进行设备维护升级：</p><p>1层攀岩B区：7月22日暂停开放</p><p>户外滑板公园：7月23日-24日暂停开放</p><p>其他区域正常营业。给您带来不便，敬请谅解！</p>', 0, 'PUBLISHED'),
('N20260720004', '新会员注册优惠活动', '即日起新注册会员享首次场地预约8折优惠', '/images/announcements/member_discount.jpg', '<p>为回馈新客户，即日起至<strong>2026年9月30日</strong>，新注册会员享以下优惠：</p><p>1. 首次场地预约8折优惠</p><p>2. 免费获得一次体验课资格</p><p>3. 设备租用首单免租金</p><p>快来加入我们吧！</p>', 0, 'PUBLISHED');

-- 13. 预约数据
INSERT INTO booking (booking_no, user_id, venue_id, venue_name, venue_address, booking_date, start_time, end_time, duration_hours, status, fee_amount, payment_status) VALUES
('B20260720001', 3, 1, '室内攀岩馆-A区', '极限运动馆1层A区', '2026-07-18', '10:00:00', '12:00:00', 2.0, 'COMPLETED', 88.00, 'PAID'),
('B20260720002', 3, 4, '室内滑板训练场', '极限运动馆2层', '2026-07-19', '14:00:00', '16:00:00', 2.0, 'COMPLETED', 90.00, 'PAID'),
('B20260720003', 4, 1, '室内攀岩馆-A区', '极限运动馆1层A区', '2026-07-20', '09:00:00', '11:00:00', 2.0, 'APPROVED', 88.00, 'UNPAID'),
('B20260720004', 5, 3, '室外滑板公园', '极限运动馆户外区域', '2026-07-21', '15:00:00', '17:00:00', 2.0, 'PENDING', 70.00, 'UNPAID'),
('B20260720005', 3, 6, '高空拓展训练基地', '极限运动馆户外拓展区', '2026-07-22', '09:00:00', '12:00:00', 3.0, 'PENDING', 360.00, 'UNPAID'),
('B20260720006', 4, 2, '室内攀岩馆-B区', '极限运动馆1层B区', '2026-07-16', '14:00:00', '16:00:00', 2.0, 'CANCELLED', 0.00, 'UNPAID'),
('B20260720007', 5, 5, '蹦极跳台', '极限运动馆户外高空区', '2026-07-17', '10:00:00', '10:30:00', 0.5, 'REJECTED', 0.00, 'UNPAID');

-- 14. 活动报名数据
INSERT INTO activity_registration (registration_no, activity_id, user_id, activity_type, remark, audit_status) VALUES
('REG20260720001', 1, 3, '个人参赛', '参加难度赛项目', 'APPROVED'),
('REG20260720002', 1, 4, '个人参赛', '参加速度赛项目', 'APPROVED'),
('REG20260720003', 2, 5, '个人体验', '完全新手，请多关照', 'PENDING'),
('REG20260720004', 3, 3, '培训学员', NULL, 'APPROVED'),
('REG20260720005', 3, 4, '培训学员', NULL, 'PENDING'),
('REG20260720006', 1, 5, '团队参赛', '与朋友组队参赛', 'PENDING'),
('REG20260720007', 2, 6, '个人体验', NULL, 'CANCELLED');
