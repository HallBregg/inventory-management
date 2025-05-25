<template>
  <div class="section">
  <div class="p-6 space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">Projects</h1>
      <button @click="showAddModal = true" class="btn-standard">+ Add Project</button>
    </div>

    <!-- Project Table -->
    <div class="overflow-x-auto">
      <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
        <thead class="bg-gray-100">
        <tr>
          <th class="px-4 py-2 text-left font-medium">Name</th>
          <th class="px-4 py-2 text-left font-medium">Identifier</th>
          <th class="px-4 py-2 text-left font-medium">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr
          v-for="project in projects"
          :key="project.id"
          class="border-t hover:bg-gray-50"
        >
          <td class="px-4 py-2">{{ project.name }}</td>
          <td class="px-4 py-2 text-xs text-gray-500">{{ project.id }}</td>
          <td class="px-4 py-2 whitespace-nowrap">
            <div class="flex flex-wrap gap-1">
              <button class="text-blue-600 hover-scale mr-4">
                <RouterLink :to="`/projects/${project.id}`">View</RouterLink>
              </button>
              <button
                @click="deleteProjectHandler(project.id)"
                class="text-red-600 hover-scale"
              >
                Delete
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-if="!projects.length" class="text-sm text-gray-500 mt-4">
      No projects found.
    </div>

    <!-- Modal -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded shadow w-[400px]">
        <h2 class="text-lg font-semibold mb-4">Add New Project</h2>
        <input
          v-model="newProjectName"
          placeholder="Project name"
          class="border w-full px-3 py-2 rounded mb-4"
        />
        <div class="flex justify-end gap-2">
          <button @click="showAddModal = false" class="px-4 py-2 border rounded">Cancel</button>
          <button @click="addProject" class="px-4 py-2 bg-blue-600 text-white rounded">Add</button>
        </div>
      </div>
    </div>
  </div>
  </div>

  <ConfirmModal
    :visible="showDeleteModal"
    title="Confirm delete."
    message="Are you sure you want to delete the project?"
    @confirm="confirmDelete"
    @cancel="showDeleteModal = false"
  />

</template>

<script setup>
import { ref, onMounted } from 'vue'
import {getProjects, createProject, deleteProject} from "@/integrations/projects/services.js";
import ConfirmModal from "@/components/ConfirmModal.vue";

const projects = ref([])
const showAddModal = ref(false)
const newProjectName = ref('')
const showDeleteModal = ref(false)
const projectIdToDelete = ref(null)

onMounted(async () => {
  await fetchAllProjects()
})

const fetchAllProjects = async () => {
  try{
    projects.value = await getProjects()
  } catch (err) {
    console.error('Failed to load projects: ', err.message)
  }
}

const addProject = async () => {
  await createProject(newProjectName.value)
  newProjectName.value = ''
  showAddModal.value = false
  await fetchAllProjects()
}

const deleteProjectHandler = async (id) => {
  showDeleteModal.value = true;
  projectIdToDelete.value = id;
}

const confirmDelete = async () => {
  await deleteProject(projectIdToDelete)
  await fetchAllProjects()
  showDeleteModal.value = false;
}
</script>
