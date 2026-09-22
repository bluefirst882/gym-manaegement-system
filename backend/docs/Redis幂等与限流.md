# Redis 幂等与限流

Redis 只负责短期请求控制，预约、租借和支付结果仍以 MySQL 事务为准，不使用 Redis 锁替代数据库行锁。

- 客户端可为预约创建、租借创建和支付携带 `Idempotency-Key`；相同用户、相同操作和请求号在 10 分钟内只处理一次，成功结果会复用已有记录。
- 键格式为 `gym:idempotency:{operation}:{userId}:{requestId}`，完成值保存为 `DONE:{业务记录ID}`，处理中重复请求返回 409。
- 预约创建和租借创建按用户使用 10 秒窗口、最多 5 次的 Redis 计数限流，超限返回 429；计数键格式为 `gym:rate:{operation}:{userId}`。
- Redis 不可用时不泄漏连接异常：幂等和限流降级为数据库事务、条件更新和库存原子更新，并记录服务端告警。该降级不声称提供跨实例幂等保证。

本地启动可执行 `docker compose up -d mysql redis`，默认 Redis 地址为 `localhost:6379`，也可通过 `REDIS_HOST`、`REDIS_PORT` 和 `REDIS_PASSWORD` 配置。
