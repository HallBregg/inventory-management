import api from './api.js'
import {Project} from "@/integrations/projects/models/Project.js";

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