<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Projects</h1>
    <div v-if="projects.length" class="space-y-3">
      <div
        v-for="project in projects"
        :key="project.id"
        class="border rounded p-4 flex justify-between items-center"
      >
        <div>
          <h2 class="text-lg font-semibold">{{ project.name }}</h2>
          <p class="text-sm text-gray-500">Created: {{ project.createdAt }}</p>
          <p class="text-sm text-gray-500">Updated: {{ project.updatedAt }}</p>
        </div>
        <div class="flex space-x-2">
          <button
            @click="goToDetails(project.id)"
            class="bg-blue-600 text-white px-3 py-1 rounded"
          >
            Details
          </button>
          <button
            @click="deleteProject(project.id)"
            class="bg-red-500 text-white px-3 py-1 rounded"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
    <div v-else class="text-gray-500">No projects found.</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const projects = ref([
  { id: 1, name: 'Project Alpha', createdAt: '2024-01-01', updatedAt: '2024-05-15' },
  { id: 2, name: 'Project Beta', createdAt: '2024-02-20', updatedAt: '2024-05-10' },
])

const goToDetails = (id) => {
  router.push({ name: 'projectDetails', params: { id } })
}

const deleteProject = (id) => {
  console.log('Deleting project with id:', id)
  projects.value = projects.value.filter(p => p.id !== id)
}
</script>

<style scoped>
</style>
