<template>
  <div v-if="project" class="section">
    <!--  Error message-->
    <div
      v-if="errorAlert"
      role="alert"
      class="relative border-s-4 border-red-700 bg-red-50 p-4 rounded"
    >
      <div class="flex items-start gap-2 text-red-700">
        <svg xmlns="http://www.w3.org/2000/svg" class="size-5 mt-0.5" viewBox="0 0 24 24" fill="currentColor">
          <path
            fill-rule="evenodd"
            d="M9.401 3.003c1.155-2 4.043-2 5.197 0l7.355 12.748c1.154 2-.29 4.5-2.599 4.5H4.645c-2.309 0-3.752-2.5-2.598-4.5L9.4 3.003zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z"
            clip-rule="evenodd"
          />
        </svg>

        <div class="flex-1">
          <strong class="font-medium">{{ $t('somethingWentWrong') }}</strong>
          <div class="mt-1 text-sm text-red-700">
            <pre>{{ error }}</pre>
          </div>
        </div>

        <button
          @click="errorAlertCloseHandler()"
          class="absolute top-2 right-2 text-red-500 hover:text-red-700"
        >
          &times;
        </button>
      </div>
    </div>

    <div class="flex justify-between">
      <nav class="text-caption text-gray-500 mb-2">
        <router-link to="/projects" class="text-blue-600 hover:underline">{{ $t('projects') }}</router-link>
        <span class="mx-2">/</span>
        <span>{{ project?.name || 'Project Details' }}</span>
      </nav>
      <div v-if="isDirty" class="text-caption text-muted mb-2 flex items-center gap-2">
        <svg class="animate-spin h-4 w-4 text-muted" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 100 16 8 8 0 01-8-8z"></path>
        </svg>{{ $t('saving') }}
      </div>
      <div v-else class="text-caption text-muted mb-2">{{ $t('saved') }}</div>
    </div>
    <div class="flex justify-between items-center gap-4">
      <input
        v-model="project.name"
        @input="debouncedSaveName"
        class="text-base font-bold border border-gray-300 px-3 py-1 rounded w-full max-w-md"
      />
      <div class="flex gap-2">
        <button
          @click="exportButtonHandler(project.id)"
          class="inline-flex items-center gap-1 btn-standard">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="w-4 h-4"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            stroke-width="1.5"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              d="M12 16V4m0 12l-4-4m4 4l4-4M4 20h16"
            />
          </svg>
          {{ $t('download') }}
        </button>
        <button @click="showAddStageModal = true" class="btn-standard">+ {{ $t('addStage') }}</button>
        <button @click="deleteProjectHandler" class="btn-standard-danger">{{ $t('deleteProject') }}</button>
      </div>
    </div>
    <div class="text-muted mb-4">{{ project.id }}</div>

    <div
      v-for="(stage, stageIndex) in project.stages"
      :key="stage.id"
      class="mb-6 border border-gray-300 p-4 rounded"
    >
      <div class="flex justify-between items-center">
        <input
          v-model="stage.name"
          @input="debouncedSaveStageName(stage)"
          class="text-base font-bold border border-gray-300 px-2 py-1 rounded w-full max-w-xs"
        />
        <button
          @click="toggleSection(stageIndex)"
          class="flex items-center gap-1 text-caption text-blue-600 hover-scale"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="w-3 h-3 transform duration-200"
            :class="{ 'rotate-90': isExpanded(stageIndex) }"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            stroke-width="2"
          ><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" />
          </svg>
          {{ isExpanded(stageIndex) ? $t('hideDetails') : $t('showDetails') }}

        </button>
        <div class="space-x-2">
          <button @click="openModalForStage(stageIndex)" class="btn-standard">+ {{ $t('addProduct') }}</button>
          <button @click="removeStage(stage.id)" class="btn-standard-danger">{{ $t('deleteStage') }}</button>
        </div>
      </div>
      <div class="text-muted text-caption mb-2">{{ stage.id }}</div>
      <div v-if="isExpanded(stageIndex)" class="transition">
        <div v-if="stage.products.length" class="space-y-3">
          <!-- Stage Product Table -->
          <div class="overflow-x-auto">
            <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
              <thead class="bg-gray-100">
              <tr>
                <th class="px-3 py-2 text-left font-medium">{{ $t('name') }}</th>
                <th class="px-3 py-2 text-left font-medium">{{ $t('id') }}</th>
                <th class="px-3 py-2 text-left font-medium">{{ $t('attributes') }}</th>
                <th class="px-3 py-2 text-left font-medium">{{ $t('quantity') }}</th>
                <th class="px-3 py-2 text-left font-medium">{{ $t('actions') }}</th>
              </tr>
              </thead>
              <tbody>
              <tr
                v-for="(item, productIndex) in sortedProducts(stage)"
                :key="item.id"
                class="border-t border-gray-300 hover:bg-gray-50"
              >
                <td class="px-3 py-2 font-medium">{{ item.name }}</td>
                <td class="px-3 py-2 font-medium">{{ item.id }}</td>
                <td class="px-3 py-2">
                  <div class="flex flex-wrap gap-1">
            <span
              v-for="(val, key) in item.attributes"
              :key="key"
              class="inline-block rounded-full bg-blue-100 px-2 py-0.5 text-xs text-blue-700 border border-blue-300"
            >
              {{ key }}: {{ val }}
            </span>
                  </div>
                </td>
                <td class="px-3 py-2 w-28">
                  <input
                    v-model.number="item.quantity"
                    @input="handleProductQuantityChange(item, stage)"
                    type="number"
                    min="0"
                    class="border border-gray-300 px-2 py-1 rounded w-full"
                  />
                </td>
                <td class="px-2 py-2 whitespace-nowrap space-x-1 text-xs text-gray-500">
                  <button
                    @click="moveProduct(stageIndex, productIndex, -1)"
                    :disabled="productIndex === 0"
                    class="hover-scale hover:scale-150 disabled:opacity-40 px-2">↑</button>
                  <button
                    @click="moveProduct(stageIndex, productIndex, 1)"
                    :disabled="productIndex === stage.products.length - 1"
                    class="hover-scale hover:scale-150 disabled:opacity-40 px-2">↓</button>
                  <button
                    @click="removeProduct(stageIndex, productIndex)"
                    class="text-red-600 hover-scale">{{ $t('remove') }}</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div v-else class="text-sm text-gray-500">{{ $t('noProducts') }}</div>
      </div>
      <details class="mt-3">
        <summary class="cursor-pointer text-sm text-blue-700 font-medium">
          {{ $t('stageSummary') }}
        </summary>
        <div class="overflow-x-auto mt-2">
          <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
            <thead class="bg-gray-100">
            <tr>
              <th class="px-3 py-2 text-left font-medium">{{ $t('name') }}</th>
              <th class="px-3 py-2 text-left font-medium">{{ $t('id') }}</th>
              <th class="px-3 py-2 text-left font-medium">{{ $t('attributes') }}</th>
              <th class="px-3 py-2 text-left font-medium">{{ $t('quantity') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="(summary, index) in summarizeStage(stage)"
              :key="index"
              class="border-t border-gray-300 hover:bg-gray-50"
            >
              <td class="px-3 py-2 font-medium">{{ summary.name }}</td>
              <td class="px-3 py-2 font-medium">{{ summary.id }}</td>
              <td class="px-3 py-2">
                <div class="flex flex-wrap gap-1">
              <span
                v-for="(val, key) in summary.attributes"
                :key="key"
                class="inline-block rounded-full bg-blue-100 px-2 py-0.5 text-xs text-blue-700 border border-blue-300"
              >
                {{ key }}: {{ val }}
              </span>
                </div>
              </td>
              <td class="px-3 py-2">{{ summary.quantity }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </details>
    </div>


    <details class="mt-6 border-t border-gray-300 pt-4">
      <summary class="cursor-pointer text-base text-green-700 font-semibold">
        {{ $t('projectSummary') }}
      </summary>
      <div class="overflow-x-auto mt-2">
        <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
          <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 text-left font-medium">{{ $t('name') }}</th>
            <th class="px-3 py-2 text-left font-medium">{{ $t('id') }}</th>
            <th class="px-3 py-2 text-left font-medium">{{ $t('attributes') }}</th>
            <th class="px-3 py-2 text-left font-medium">{{ $t('quantity') }}</th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="(summary, index) in summarizeProject()"
            :key="index"
            class="border-t border-gray-300 hover:bg-gray-50"
          >
            <td class="px-3 py-2 font-medium">{{ summary.name }}</td>
            <td class="px-3 py-2 font-medium">{{ summary.id }}</td>
            <td class="px-3 py-2">
              <div class="flex flex-wrap gap-1">
              <span
                v-for="(val, key) in summary.attributes"
                :key="key"
                class="inline-block rounded-full bg-blue-100 px-2 py-0.5 text-xs text-blue-700 border border-blue-300"
              >
                {{ key }}: {{ val }}
              </span>
              </div>
            </td>
            <td class="px-3 py-2">{{ summary.quantity }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </details>

    <!-- Modals -->
    <ProductSelectModal
      v-if="showModal"
      :products="products"
      :attribute-names="availableAttributes"
      @close="showModal = false"
      @select="handleProductSelect"
    />
    <AddStageModal
      v-if="showAddStageModal"
      @close="showAddStageModal = false"
      @submit="handleCreateStage"
    />
    <ConfirmModal
      :visible="showDeleteProductModal"
      :title="t('deleteTitle')"
      :message="t('deleteMessage')"
      @confirm="showDeleteProductModalHandler"
      @cancel="showDeleteProductModal = false"
    />
    <ConfirmModal
      :visible="showDeleteStageModal"
      :title="t('deleteTitle')"
      :message="t('deleteStageMessage')"
      @confirm="showDeleteStageModalHandler"
      @cancel="showDeleteStageModal = false"
    />
    <ConfirmModal
      :visible="showDeleteProjectModal"
      :title="t('deleteTitle')"
      :message="t('deleteProjectMessage')"
      @confirm="showDeleteProjectModalHandler"
      @cancel="showDeleteProjectModal = false"
    />

  </div>
  <div v-else class="text-gray-800 text-sm mb-4 flex items-center gap-2">
    <svg class="animate-spin h-4 w-4 text-gray-800" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 100 16 8 8 0 01-8-8z"></path>
    </svg>
    <span>Nie mamy pańskiego płaszcza i co nam pan zrobi?</span>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {
  createStage, csvSummary,
  deleteProject,
  deleteStage,
  getProjectDetails,
  updateProject,
  updateStage
} from '@/integrations/projects/services'
import ProductSelectModal from "@/components/ProductSelectModal.vue";
import AddStageModal from "@/components/AddStageModal.vue";
import {deleteProduct, getAllProperties, getProducts} from "@/integrations/store/services.js";
import { useI18n } from 'vue-i18n'
import ConfirmModal from "@/components/ConfirmModal.vue";

const route = useRoute()
const router = useRouter();
const project = ref(null)
const isDirty = ref(false)
const showModal = ref(false)
const currentStageIndex = ref(null)
let saveTimer = null
const stageSaveTimers = new Map()
const showAddStageModal = ref(false)

const products = ref([])
const availableAttributes = ref([])
const expandedSections = ref(new Set())
const { t } = useI18n()
const error = ref(null)
const errorAlert = ref(false)

const propagateError = (err) => {
  error.value = err
  errorAlert.value = true
  console.log(err)
}

const errorAlertCloseHandler = () => {
  errorAlert.value = false
  error.value = null
}

const showDeleteProjectModal = ref(false)
const showDeleteProjectModalHandler = async () => {
  await deleteProject(project.value.id)
  router.push({name: "projectListView"})
  showDeleteProjectModal.value = false;
}

const showDeleteStageModal = ref(false)
const stageIdToDelete = ref(null)
const showDeleteStageModalHandler = async () => {
  isDirty.value = true
  try {
    await deleteStage(project.value.id, stageIdToDelete.value)
    project.value = await getProjectDetails(project.value.id)
    isDirty.value = false
  } catch (err) {
    console.error()
  }
  stageIdToDelete.value = null
  showDeleteStageModal.value = false;
}

const showDeleteProductModal = ref(false)
const deleteProductStageIndex = ref(null)
const deleteProductIndex = ref(null)

const showDeleteProductModalHandler = async () => {
  const stage = project.value.stages[deleteProductStageIndex.value];
  stage.products.splice(deleteProductIndex.value, 1)
  updateStageHandler(stage)
  showDeleteProductModal.value = false;
  deleteProductIndex.value = null;
  deleteProductStageIndex.value = null;
}

const toggleSection = (index) => {
  if (expandedSections.value.has(index)) {
    expandedSections.value.delete(index)
  } else {
    expandedSections.value.add(index)
  }
}

const isExpanded = (index) => {
  return expandedSections.value.has(index)
}

onMounted(async () => {
  const id = route.params.id
  try{
    project.value = await getProjectDetails(id)
    products.value = await getProducts();
    availableAttributes.value = await getAllProperties();
  }catch(err){
    propagateError(err)
  }
})

const sortedProducts = (stage) => {
  return [...stage.products].sort((a, b) => a.position - b.position)
}

const debouncedSaveName = () => {
  if (!project.value) return
  if (saveTimer) clearTimeout(saveTimer)
  isDirty.value = true
  saveTimer = setTimeout(async () => {
    try {
      await updateProject(project.value.id, project.value.name)
    } catch (err) {
      console.error('Failed to update project:', err.message)
    } finally {
      isDirty.value = false
    }
  }, 1000)
}

const updateStageHandler = (stage) => {
  const stageId = stage.id
  if (stageSaveTimers.has(stageId)) clearTimeout(stageSaveTimers.get(stageId))

  isDirty.value = true

  const timer = setTimeout(async () => {
    try {
      await updateStage(project.value.id, stage)
    } catch (err) {
      propagateError(err)
    } finally {
      isDirty.value = false
      stageSaveTimers.delete(stageId)
    }
  }, 1000)
  stageSaveTimers.set(stageId, timer)
}

const debouncedSaveStageName = (stage) => { updateStageHandler(stage); }

const deleteProjectHandler = async () => {
  showDeleteProjectModal.value = true;
}

const handleCreateStage = async (name) => {
  try {
    await createStage(project.value.id, name)
    project.value = await getProjectDetails(project.value.id)
    // project.value.stages.push(stage)
    showAddStageModal.value = false
  } catch (err) {
    propagateError(err)
  }
}

const removeStage = (stageId) => {
  showDeleteStageModal.value = true
  stageIdToDelete.value = stageId
}

const openModalForStage = (index) => {
  currentStageIndex.value = index
  showModal.value = true
}

const removeProduct = (stageIndex, productIndex) => {
  showDeleteProductModal.value = true
  deleteProductStageIndex.value = stageIndex
  deleteProductIndex.value = productIndex
}

const moveProduct = async (stageIndex, productIndex, direction) => {
  const stage = project.value.stages[stageIndex]

  // Make a copy sorted by position
  const sorted = [...stage.products].sort((a, b) => a.position - b.position)
  const targetIndex = productIndex + direction

  if (targetIndex < 0 || targetIndex >= sorted.length) return

  // Swap positions
  const from = sorted[productIndex]
  const to = sorted[targetIndex]
  const temp = from.position
  from.position = to.position
  to.position = temp

  // Reassign sorted (modified) list back to stage.products
  stage.products = sorted

  updateStageHandler(stage);
}

const summarizeStage = (stage) => {
  const map = new Map()
  for (const item of stage.products) {
    const key = item.name + JSON.stringify(item.attributes)
    if (!map.has(key)) {
      map.set(key, {
        name: item.name,
        id: item.id,
        attributes: item.attributes,
        quantity: 0
      })
    }
    map.get(key).quantity += item.quantity
  }
  return Array.from(map.values())
}

const summarizeProject = () => {
  const map = new Map()
  for (const stage of project.value.stages) {
    for (const item of stage.products) {
      const key = item.name + JSON.stringify(item.attributes)
      if (!map.has(key)) {
        map.set(key, { name: item.name, id: item.id, attributes: item.attributes, quantity: 0 })
      }
      map.get(key).quantity += item.quantity
    }
  }
  return Array.from(map.values())
}

const handleProductSelect = ({ product, quantity }) => {
  const stage = project.value.stages[currentStageIndex.value]

  stage.products.push({
    id: product.id,
    name: product.name,
    quantity,
    position: stage.products.length + 1,
    attributes: product.attributes
  })
  updateStageHandler(stage)
  showModal.value = false
}

const handleProductQuantityChange = (product, stage) => {
  updateStageHandler(stage);
}

const exportButtonHandler = async (projectId) => {
  try {
    const response = await csvSummary(projectId)
    const blob = new Blob([response.data], { type: 'text/csv' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `summary-${projectId}.csv`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  } catch (error) {
    console.error('CSV download failed:', error)
    propagateError(error)
  }
}

</script>
