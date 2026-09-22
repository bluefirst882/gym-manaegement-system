# 极限运动场馆管理平台

面向运动场馆的数字化运营平台，覆盖**场地预约、活动报名、器材采购与租借、公告发布**等业务，
提供 **运营管理后台（Web）** 与 **微信小程序** 两端服务。

后端采用 Spring Boot + MyBatis 分层架构，数据库为 MySQL 8；管理端为 Vue 3 单页应用；
小程序为微信原生开发。

> 说明：本项目为参考教学案例实现的实践项目，在前人基础上对预约与器材租借模块进行了重构与功能扩展。

---

## 技术栈

| 层次 | 技术 |
|---|---|
| 后端 | Java 21、Spring Boot 4、MyBatis、PageHelper、Maven |
| 数据库 | MySQL 8（14 张表 / 26 个索引 / utf8mb4） |
| 鉴权 | JWT（jjwt 0.12.6）、BCrypt（spring-security-crypto） |
| 管理端 | Vue 3、Vue Router、Element Plus、ECharts、Axios、Vite |
| 小程序 | 微信原生小程序（12 个页面） |
| 测试 | JUnit 5、H2 内存数据库（MySQL 兼容模式） |

---

## 核心功能

**场地预约**
- 预约创建 → 审核 → 到店签到 → 离场结算 → 支付 的完整生命周期
- 按次 / 按小时两种计费模型，金额以 `BigDecimal` 计算
- 状态流转：`PENDING → APPROVED → COMPLETED`（含 `REJECTED` / `CANCELLED`）

**器材管理**
- 器材分类、采购入库、租借出库、归还、维护记录
- 租借扣减与归还回补保证库存一致性

**活动管理**
- 活动发布、报名、审核、取消

**公告管理**
- 草稿 → 发布 → 下线 生命周期，支持置顶

**数据统计**
- 场地利用率、预约量、累计时长、营收统计
- 活动报名数与审核通过数统计

---

## 技术要点

**1. 预约时段冲突校验**

采用标准区间重叠判定，在 SQL 层一次查询完成冲突检测，并限定在有效状态内：

```sql
SELECT COUNT(*) FROM booking
WHERE venue_id = #{venueId}
  AND booking_date = #{bookingDate}
  AND status IN ('PENDING', 'APPROVED')
  AND (#{startTime} < end_time AND #{endTime} > start_time)
```

**2. 器材库存并发控制**

将「查询 — 判断 — 更新」收敛为单条原子语句，以影响行数判定扣减是否成功，
避免并发场景下超借；归还回补时以库存上限兜底：

```sql
-- 扣减：库存不足时影响 0 行，由 Service 层据此抛出业务异常
UPDATE equipment SET available_quantity = available_quantity - #{quantity}
WHERE id = #{id} AND available_quantity >= #{quantity}

-- 回补：LEAST 保证不会超过总量
UPDATE equipment SET available_quantity = LEAST(quantity, available_quantity + #{quantity})
WHERE id = #{id}
```

**3. 统一响应与全局异常处理**

`Result<T>` / `PageResult<T>` 统一响应结构，配合 `@RestControllerAdvice` 集中处理业务异常，
兜底处理器不向客户端泄露内部异常信息。

**4. 统计聚合**

以 `LEFT JOIN` + `GROUP BY` + `SUM(CASE WHEN ...)` 单次查询完成交叉聚合；
日期筛选条件写在 `ON` 子句而非 `WHERE`，保证无预约的场地仍以 0 值出现在报表中。

---

## 项目结构

```
.
├── backend/                    # Spring Boot 后端
│   └── src/
│       ├── main/java/org/limitless/backend/
│       │   ├── controller/     # 15 个 Controller
│       │   ├── service/        # 15 个 Service
│       │   ├── mapper/         # 15 个 Mapper 接口
│       │   ├── entity/         # 14 个实体
│       │   ├── dto/            # 请求 / 响应 DTO
│       │   ├── common/         # Result / PageResult / 全局异常处理
│       │   ├── config/         # JWT 过滤器、CORS
│       │   └── util/           # JwtUtil
│       ├── main/resources/
│       │   ├── mapper/         # 15 个 MyBatis XML（83 条语句）
│       │   └── application.yaml
│       └── test/               # JUnit 测试（H2 内存库）
├── frontend-admin/             # Vue 3 管理后台（11 个页面）
├── miniprogram/                # 微信小程序（12 个页面）
└── gym_venue_management.sql    # 数据库建表 + 初始化数据
```

---

## 快速开始

### 1. 数据库

```bash
mysql -u root -p < gym_venue_management.sql
```

### 2. 配置环境变量

复制 `.env.example` 为 `.env` 并填写：

```ini
DB_HOST=localhost
DB_PORT=3306
DB_NAME=extreme_sports_venue
DB_USERNAME=root
DB_PASSWORD=你的数据库密码

# JWT 签名密钥，至少 32 字符，务必自行生成
# 生成方式：openssl rand -base64 48
JWT_SECRET=你的随机密钥
```

> `JWT_SECRET` 未设置时应用会在启动阶段直接失败，以避免误用不安全的默认密钥。

### 3. 启动后端

```bash
cd backend
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

后端默认监听 `http://localhost:8080`。

### 4. 启动管理端

```bash
cd frontend-admin
npm install
npm run dev
```

管理端默认监听 `http://localhost:5173`，已配置 `/api` 代理到后端。

### 5. 小程序

使用微信开发者工具导入 `miniprogram/` 目录。
注意：`miniprogram/utils/config.js` 中的 `BASE_URL` 默认为本地地址，
真机调试需改为可访问的 HTTPS 域名。

### 6. 运行测试

```bash
cd backend
./mvnw test
```

测试使用 H2 内存数据库（MySQL 兼容模式），自带独立的 `schema.sql` 与 `data.sql`，
无需连接真实 MySQL。

---

## 已知局限

出于学习目的实现，以下方面**未做完整实现**，如用于生产环境需补充：

- **接口级权限控制**：JWT 过滤器完成身份认证，但未实现基于角色的接口鉴权
  （`loginAdmin` 仅校验管理端登录资格）
- **预约冲突校验的并发安全**：冲突检测为「先查后插」，未加行锁或唯一约束，
  高并发下存在竞态，建议增加 `(venue_id, booking_date, start_time)` 唯一索引配合重试
- **活动报名人数上限**：`max_participants` 字段未在报名逻辑中校验
- **请求参数校验**：未引入 Bean Validation，校验以手写判断为主
- **缓存 / 消息队列**：未引入 Redis、MQ 等中间件

---

## 许可

本项目仅用于学习与技术交流。
