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
