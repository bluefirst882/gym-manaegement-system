SET FOREIGN_KEY_CHECKS = 0;

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

CREATE TABLE sys_role (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    role_code   VARCHAR(20)  NOT NULL UNIQUE,
    role_name   VARCHAR(50)  NOT NULL,
    role_desc   VARCHAR(255) DEFAULT NULL,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_user (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    real_name     VARCHAR(50)  DEFAULT NULL,
    phone         VARCHAR(20)  NOT NULL,
    gender        VARCHAR(10)  DEFAULT NULL,
    birthday      DATE         DEFAULT NULL,
    role_id       INT          DEFAULT NULL,
    status        TINYINT      NOT NULL DEFAULT 1,
    avatar        VARCHAR(255) DEFAULT NULL,
    registered_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at DATETIME     DEFAULT NULL,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE SET NULL
);

CREATE TABLE venue_category (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(20)  NOT NULL UNIQUE,
    category_name VARCHAR(50)  NOT NULL,
    category_desc VARCHAR(255) DEFAULT NULL,
    sort_order    INT          NOT NULL DEFAULT 0,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venue (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    venue_code      VARCHAR(50)  NOT NULL UNIQUE,
    venue_name      VARCHAR(100) NOT NULL,
    venue_address   VARCHAR(255) DEFAULT NULL,
    contact_phone   VARCHAR(20)  DEFAULT NULL,
    category_id     INT          DEFAULT NULL,
    dimensions      VARCHAR(100) DEFAULT NULL,
    material        VARCHAR(100) DEFAULT NULL,
    capacity        INT          DEFAULT NULL,
    facilities      VARCHAR(500) DEFAULT NULL,
    fee_type        VARCHAR(20)  NOT NULL DEFAULT 'PER_HOUR',
    fee_amount      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    cover_image     VARCHAR(255) DEFAULT NULL,
    detail_images   TEXT         DEFAULT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE',
    remark          VARCHAR(500) DEFAULT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES venue_category(id) ON DELETE SET NULL
);

CREATE TABLE booking (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_no      VARCHAR(50)  NOT NULL UNIQUE,
    user_id         INT          NOT NULL,
    venue_id        INT          NOT NULL,
    venue_name      VARCHAR(100) DEFAULT NULL,
    venue_address   VARCHAR(255) DEFAULT NULL,
    booking_date    DATE         NOT NULL,
    start_time      TIME         NOT NULL,
    end_time        TIME         NOT NULL,
    duration_hours  DECIMAL(5,1) DEFAULT NULL,
    remark          VARCHAR(500) DEFAULT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    source          VARCHAR(20)  NOT NULL DEFAULT 'MINI_PROGRAM',
    checkin_time    DATETIME     DEFAULT NULL,
    checkout_time   DATETIME     DEFAULT NULL,
    attendee_count  INT          DEFAULT 1,
    fee_amount      DECIMAL(10,2) DEFAULT 0.00,
    payment_status  VARCHAR(20)  NOT NULL DEFAULT 'UNPAID',
    payment_method  VARCHAR(50)  DEFAULT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE
);

CREATE TABLE activity_category (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(20)  NOT NULL UNIQUE,
    category_name VARCHAR(50)  NOT NULL,
    category_desc VARCHAR(255) DEFAULT NULL,
    sort_order    INT          NOT NULL DEFAULT 0,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE activity (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    activity_no         VARCHAR(50)  NOT NULL UNIQUE,
    title               VARCHAR(200) NOT NULL,
    cover_image         VARCHAR(255) DEFAULT NULL,
    carousel_images     TEXT         DEFAULT NULL,
    category_id         INT          DEFAULT NULL,
    location            VARCHAR(255) NOT NULL,
    start_time          DATETIME     NOT NULL,
    end_time            DATETIME     NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    registration_method VARCHAR(20)  DEFAULT 'ONLINE',
    max_participants    INT          DEFAULT NULL,
    awards              VARCHAR(500) DEFAULT NULL,
    rules               TEXT         NOT NULL,
    description         TEXT         NOT NULL,
    contact             VARCHAR(100) DEFAULT NULL,
    fee_type            VARCHAR(20)  DEFAULT 'FREE',
    fee_amount          DECIMAL(10,2) DEFAULT 0.00,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES activity_category(id) ON DELETE SET NULL
);

CREATE TABLE activity_registration (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_no VARCHAR(50)  NOT NULL UNIQUE,
    activity_id     INT          NOT NULL,
    user_id         INT          NOT NULL,
    activity_type   VARCHAR(50)  DEFAULT NULL,
    remark          VARCHAR(500) DEFAULT NULL,
    audit_status    VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    audit_comment   VARCHAR(500) DEFAULT NULL,
    audit_time      DATETIME     DEFAULT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (activity_id) REFERENCES activity(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
);

CREATE TABLE equipment_category (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(20)  NOT NULL UNIQUE,
    category_name VARCHAR(50)  NOT NULL,
    category_desc VARCHAR(255) DEFAULT NULL,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE equipment (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    equipment_no        VARCHAR(50)  NOT NULL UNIQUE,
    name                VARCHAR(100) NOT NULL,
    category_id         INT          DEFAULT NULL,
    brand               VARCHAR(100) NOT NULL,
    model               VARCHAR(100) NOT NULL,
    serial_number       VARCHAR(100) NOT NULL UNIQUE,
    location            VARCHAR(200) NOT NULL,
    responsible_person  VARCHAR(50)  NOT NULL,
    quantity            INT          NOT NULL DEFAULT 1,
    available_quantity  INT          NOT NULL DEFAULT 1,
    status              VARCHAR(20)  NOT NULL DEFAULT 'NORMAL',
    cover_image         VARCHAR(255) DEFAULT NULL,
    detail_images       TEXT         DEFAULT NULL,
    remark              VARCHAR(500) DEFAULT NULL,
    created_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES equipment_category(id) ON DELETE SET NULL
);

CREATE TABLE equipment_purchase (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    purchase_no     VARCHAR(50)  NOT NULL UNIQUE,
    equipment_id    INT          DEFAULT NULL,
    equipment_name  VARCHAR(100) NOT NULL,
    brand           VARCHAR(100) DEFAULT NULL,
    model           VARCHAR(100) DEFAULT NULL,
    serial_number   VARCHAR(100) DEFAULT NULL,
    quantity        INT          NOT NULL DEFAULT 1,
    purchase_price  DECIMAL(10,2) DEFAULT 0.00,
    purchase_date   DATE         DEFAULT NULL,
    supplier        VARCHAR(200) DEFAULT NULL,
    invoice_no      VARCHAR(100) DEFAULT NULL,
    remark          VARCHAR(500) DEFAULT NULL,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE SET NULL
);

CREATE TABLE equipment_rental (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    rental_no         VARCHAR(50)  NOT NULL UNIQUE,
    equipment_id      INT          DEFAULT NULL,
    quantity          INT          NOT NULL DEFAULT 1,
    start_time        DATETIME     NOT NULL,
    end_time          DATETIME     NOT NULL,
    borrower          VARCHAR(100) NOT NULL,
    contact_phone     VARCHAR(20)  DEFAULT NULL,
    rental_price      DECIMAL(10,2) DEFAULT 0.00,
    status            VARCHAR(20)  NOT NULL DEFAULT 'RENTED',
    actual_return_time DATETIME    DEFAULT NULL,
    remark            VARCHAR(500) DEFAULT NULL,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE SET NULL
);

CREATE TABLE equipment_maintenance (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    maintenance_no     VARCHAR(50)  NOT NULL UNIQUE,
    title              VARCHAR(200) NOT NULL,
    equipment_id       INT          DEFAULT NULL,
    maintenance_time   DATETIME     NOT NULL,
    content            TEXT         NOT NULL,
    personnel          VARCHAR(100) NOT NULL,
    equipment_condition VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    images             TEXT         DEFAULT NULL,
    remark             VARCHAR(500) DEFAULT NULL,
    created_at         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE SET NULL
);

CREATE TABLE announcement (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    announcement_no VARCHAR(50)  NOT NULL UNIQUE,
    title           VARCHAR(200) NOT NULL,
    summary         VARCHAR(500) DEFAULT NULL,
    cover_image     VARCHAR(255) DEFAULT NULL,
    content         TEXT         NOT NULL,
    is_top          TINYINT      NOT NULL DEFAULT 0,
    status          VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
