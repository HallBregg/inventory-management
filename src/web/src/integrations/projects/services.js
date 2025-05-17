import api from './api.js'
import {Project} from "@/integrations/projects/models/Project.js";
import {ProjectDetails, Stage, StageProduct} from "@/integrations/projects/models/ProjectDetails.js";

export const getProjects = async () => {
    const response = await api.get('')
    return response.data.map(project => new Project({id: project.id, name: project.name}))
}


export const createProject = async (name) => {
    const response = await api.post('', {name: name})
}


export const deleteProject = async (id) => {
    const response = await api.delete(`/${id}`)
}


export const getProjectDetails = async (id) => {
    const res = await api.get(`/full/${id}`)
    const data = res.data

    const stages = data.stages.map(stage => new Stage({
        id: stage.id,
        name: stage.name,
        products: stage.products.map(product => {
            const attrObj = Object.fromEntries(
                product.properties.map(attr => [attr.name, attr.value])
            )
            return new StageProduct({
                id: product.id,
                name: product.name,
                position: product.position,
                quantity: product.quantity,
                attributes: attrObj
            })
        })
    }))

    return new ProjectDetails({
        id: data.id,
        name: data.name,
        stages
    })
}


export const updateProject = async (id, name) => {
    const res = await api.put(`/${id}`, {name: name})
}


export const updateStage = async (projectId, stage) => {
    const body = {
        name: stage.name,
        products: stage.products.map((p) => ({
            productId: p.id,
            quantity: p.quantity,
            position: p.position,
        }))
    }
    const response = await api.put(
        `/${projectId}/stages/${stage.id}`,
        body
    )
}