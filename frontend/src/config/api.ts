import axios, { AxiosInstance } from "axios"

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
})

// Set the Authorization header for each request
api.interceptors.request.use(
  (request) => {
    const token = localStorage.getItem("token")
    if (token) {
      request.headers.Authorization = token
    }
    return request
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default api
