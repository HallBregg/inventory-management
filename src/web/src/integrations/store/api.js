import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api/products',
    timeout: 5000,
})

api.interceptors.response.use(
    res => res,
    err => {
        if (err.response?.data) {
            const { code, message, details } = err.response.data
            return Promise.reject({ code, message, details, status: err.response.status })
        }

        return Promise.reject({
            code: 'UNKNOWN',
            message: 'Unknown error occurred',
            details: {},
            status: err.response?.status ?? 0
        })
    }
)

export default api
