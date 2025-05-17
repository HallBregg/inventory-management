<template>
  <div class="p-6 space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">Projects</h1>
      <button @click="showAddModal = true" class="bg-blue-600 text-white px-4 py-2 rounded">+ Add Project</button>
    </div>

    <!-- Project List -->
    <ul class="space-y-2">
      <li
        v-for="project in projects"
        :key="project.id"
        class="border rounded p-4 flex justify-between items-center"
      >
        <div>
          <div class="font-semibold">{{ project.name }}</div>
          <div class="text-xs text-gray-500">ID: {{ project.id }}</div>
        </div>
        <div class="space-x-4">
          <RouterLink
            :to="`/projects/${project.id}`"
            class="text-blue-600 hover:underline text-sm"
          >
            View
          </RouterLink>
          <button
            @click="deleteProject(project.id)"
            class="text-red-600 text-sm hover:underline"
          >
            Delete
          </button>
        </div>
      </li>
    </ul>

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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios';

const projects = ref([])
const showAddModal = ref(false)
const newProjectName = ref('')

onMounted(async () => {
  axios.get("http://localhost:8080/api/projects")
    .then((response) => {
      projects.value = response.data.map(p => ({
        id: p.id,
        name: p.name,
        createdAt: null,      // placeholder for future API support
        updatedAt: null
      }))
    })

})
const addProject = () => {
  const id = crypto.randomUUID()
  projects.value.push({
    id,
    name: newProjectName.value,
    createdAt: new Date(),
    updatedAt: new Date()
  })
  console.log('Project added (mock):', newProjectName.value)
  newProjectName.value = ''
  showAddModal.value = false
}

const deleteProject = (id) => {
  projects.value = projects.value.filter(p => p.id !== id)
  console.log('Project deleted (mock):', id)
}
</script>
