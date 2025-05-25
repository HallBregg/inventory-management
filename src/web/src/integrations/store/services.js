import api from './api.js'
import {Product} from '@/integrations/store/Product.js'

export const getProducts = async () => {
    const response = await api.get('')
    return response.data.map(
        product => new Product({
            id: product.id,
            name: product.name,
            attributes: Object.fromEntries(product.properties.map(
                property => [property.name, property.value]))}))
}


export const getAllProperties = async () => {
    const response = await api.get('/properties');
    return response.data;
}


export const createProduct = async (name, attributes) => {
    const response = await api.post('', {name: name, properties: Object.entries(attributes).map(([name, value]) => ({ name, value }))})
}


export const updateProduct = async (id, name, attributes) => {
    const response = await api
        .put(`/${id}`, {
            name: name,
            properties: Object
                .entries(attributes)
                .map(([name, value]) => ({ name, value }))})
}


export const deleteProduct = async (id) => {
    const response = await api.delete(`/${id}`)
}