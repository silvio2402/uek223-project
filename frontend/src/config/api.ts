import axios, { AxiosInstance, InternalAxiosRequestConfig } from "axios"
import {
  getAccessToken,
  getRefreshToken,
  logoutAuth,
  refreshAuth,
} from "../services/authService"

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
})

// Set the Authorization header for each request
api.interceptors.request.use(
  async (request) => {
    const token =
      request.url === "/auth/refresh" ? getRefreshToken() : getAccessToken()
    request.headers.Authorization = token ? `Bearer ${token}` : undefined

    return request
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Refresh the access token if it has expired
api.interceptors.response.use(
  async (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean
    }
    const status = error.response?.status
    if ((status === 401 || status === 403) && !originalRequest._retry) {
      if (originalRequest.url === "/auth/refresh") {
        logoutAuth()
        return Promise.reject(error)
      }
      if (originalRequest.url === "/auth/authenticate") {
        return Promise.reject(error)
      }
      originalRequest._retry = true
      await refreshAuth()
      return api(originalRequest)
    }
    return Promise.reject(error)
  }
)

export default api
