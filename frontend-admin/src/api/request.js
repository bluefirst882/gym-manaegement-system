import axios from 'axios'
import { ElMessage } from 'element-plus'
import { mockHandle } from '@/mock'
 
// ===== Mock 开关 =====
// true：用本地假数据（后端未就绪时），false：连真实后端
// 后端就绪后改成 false 即可，前端代码无需改动
const USE_MOCK = false
 
// 后端接口统一前缀 /api（见接口文档），开发期走 vite proxy 到 8080
const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})
 
// 启用 Mock：拦截请求，直接返回假数据（模拟 300ms 网络延迟）
if (USE_MOCK) {
  service.defaults.adapter = (config) => {
    const method = (config.method || 'get').toLowerCase()
    const body = config.data ? JSON.parse(config.data) : (config.params || {})
    return new Promise((resolve) => {
      const payload = mockHandle(config.url, method, body)
      setTimeout(() => {
        resolve({
          data: payload,
          status: 200,
          statusText: 'OK',
          headers: {},
          config
        })
      }, 250)
    })
  }
}
 
// 请求拦截：自动带 JWT token（接口文档：Authorization: Bearer token）
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  (error) => Promise.reject(error)
)
 
// 响应拦截：接口文档成功码为 0
service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 0) {
      return res.data
    }
    // 401 未登录 / token 失效
    if (res.code === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
      return Promise.reject(res.message)
    }
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(res.message)
  },
  (error) => {
    ElMessage.error('网络异常，请稍后重试')
    return Promise.reject(error)
  }
)
 
export default service
