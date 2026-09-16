// 全局配置
module.exports = {
  // Mock 开关：true 用本地假数据，false 连真实后端
  // 后端就绪后改为 false，并把 BASE_URL 改成后端地址
  USE_MOCK: false,
  // 后端基础地址（仅 USE_MOCK=false 时生效）
  BASE_URL: 'http://localhost:8080/api',
  // 模拟网络延迟（毫秒）
  MOCK_DELAY: 250
}
