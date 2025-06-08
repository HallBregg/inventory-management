import axios from 'axios'

const api = axios.create({
    baseURL: `${import.meta.env.VITE_API_URL}/api/products`,
    timeout: 5000,
})

api.interceptors.response.use(
    res => res,
    err => {
        if (err.response?.data) {
            const { code, message, details } = err.response.data
            return Promise.reject({ code, message, details, status: err.response.status })
        }
        if (!err.response){
            return Promise.reject({
                code: "NETWORK_ERROR",
                message: "Could not connect with the data source.",
                details: err.message,
                status: 0
            })
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
