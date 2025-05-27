<template>
  <div class="section">
  <div class="p-6 space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">{{ $t('projects') }}</h1>
      <button @click="showAddModal = true" class="btn-standard">+ {{ $t('addProject') }}</button>
    </div>

    <!-- Project Table -->
    <div class="overflow-x-auto">
      <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
        <thead class="bg-gray-100">
        <tr>
          <th class="px-4 py-2 text-left font-medium">{{ $t('name') }}</th>
          <th class="px-4 py-2 text-left font-medium">{{ $t('id') }}</th>
          <th class="px-4 py-2 text-left font-medium">{{ $t('actions') }}</th>
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
                <RouterLink :to="`/projects/${project.id}`">{{ $t('edit') }}</RouterLink>
              </button>
              <button
                @click="deleteProjectHandler(project.id)"
                class="text-red-600 hover-scale"
              >{{ $t('delete') }}</button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div v-if="!projects.length" class="text-sm text-gray-500 mt-4">{{ $t('noProjectsFound') }}</div>

    <!-- Modal -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded shadow w-[400px]">
        <h2 class="text mb-4">{{ $t('addNewProject') }}</h2>
        <input
          v-model="newProjectName"
          placeholder="Project name"
          class="border w-full px-1 py-1 rounded mb-4"
        />
        <div class="flex justify-end gap-2">
          <button @click="showAddModal = false" class="px-2 py-2 border rounded">{{ $t('cancel') }}</button>
          <button @click="addProject" class="px-2 py-2 bg-blue-600 text-white rounded">{{ $t('add') }}</button>
        </div>
      </div>
    </div>
  </div>
  </div>

  <ConfirmModal
    :visible="showDeleteModal"
    :title="t('deleteTitle')"
    :message="t('deleteProjectMessage')"
    @confirm="confirmDelete"
    @cancel="showDeleteModal = false"
  />

</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted } from 'vue'
import {getProjects, createProject, deleteProject} from "@/integrations/projects/services.js";
import ConfirmModal from "@/components/ConfirmModal.vue";

const projects = ref([])
const showAddModal = ref(false)
const newProjectName = ref('')
const showDeleteModal = ref(false)
const projectIdToDelete = ref(null)

const { t } = useI18n()

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
  await deleteProject(projectIdToDelete.value)
  await fetchAllProjects()
  showDeleteModal.value = false;
}
</script>
