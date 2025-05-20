import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api/products',
    timeout: 5000,
})

export default api
