<template>
  <div v-if="project">
    <!-- Breadcrumb -->
    <nav class="text-sm text-gray-500 mb-4">
      <router-link to="/projects" class="text-blue-600 hover:underline">Projects</router-link>
      <span class="mx-2">/</span>
      <span>{{ project?.name || 'Project Details' }}</span>
    </nav>

    <!-- Project Header -->
    <div class="flex justify-between items-center mb-2">
      <input
        v-model="project.name"
        @input="debouncedSaveName"
        class="text-2xl font-bold border px-3 py-1 rounded w-full max-w-md"
      />
      <div v-if="isDirty" class="text-yellow-600 text-sm mb-4 flex items-center gap-2">
        <svg class="animate-spin h-4 w-4 text-yellow-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 100 16 8 8 0 01-8-8z"></path>
        </svg>
        Saving...
      </div>
      <div v-else class="text-gray-600 text-sm mb-4">Saved</div>
      <button @click="deleteProject" class="bg-red-600 text-white px-4 py-2 rounded ml-4">Delete Project</button>
    </div>

    <button @click="showAddStageModal = true" class="bg-blue-700 text-white px-4 py-2 mb-4 rounded">
      + Add Stage
    </button>

    <AddStageModal
      v-if="showAddStageModal"
      @close="showAddStageModal = false"
      @submit="handleCreateStage"
    />

    <div
      v-for="(stage, stageIndex) in project.stages"
      :key="stage.id"
      class="mb-6 border p-4 rounded"
    >
      <div class="flex justify-between items-center mb-2">
        <input
          v-model="stage.name"
          @input="debouncedSaveStageName(stage)"
          class="text-lg font-bold border px-2 py-1 rounded w-full max-w-xs"
        />
        <div class="space-x-2">
          <button @click="openModalForStage(stageIndex)" class="bg-blue-600 text-white px-3 py-1 rounded">
            + Add Product
          </button>
          <button @click="removeStage(stage.id)" class="bg-red-500 text-white px-3 py-1 rounded">
            Delete Stage
          </button>
        </div>
      </div>
      <div v-if="stage.products.length" class="space-y-3">
        <div
          v-for="(item, productIndex) in sortedProducts(stage)"
          :key="item.id"
          class="border rounded p-2 text-sm space-y-1"
        >
          <div class="flex justify-between items-center">
            <span class="font-medium">{{ item.name }}</span>
            <div class="space-x-2">
              <button
                @click="moveProduct(stageIndex, productIndex, -1)"
                :disabled="productIndex === 0"
                class="text-xs text-gray-500 hover:text-black"
              >↑</button>
              <button
                @click="moveProduct(stageIndex, productIndex, 1)"
                :disabled="productIndex === stage.products.length - 1"
                class="text-xs text-gray-500 hover:text-black"
              >↓</button>
              <button
                @click="removeProduct(stageIndex, productIndex)"
                class="text-red-500 hover:text-red-700 text-xs"
              >
                Remove
              </button>
            </div>
          </div>
          <ul class="ml-4 text-gray-700 list-disc">
            <li v-for="(val, key) in item.attributes" :key="key">{{ key }}: {{ val }}</li>
          </ul>
          <div class="flex items-center space-x-2 mt-1">
            <label class="text-xs">Quantity:</label>
            <input
              v-model.number="item.quantity"
              type="number"
              min="0"
              class="border px-2 py-1 rounded w-20"
            />
          </div>
        </div>
      </div>
      <div v-else class="text-sm text-gray-500">No products added yet.</div>

      <details class="mt-3">
        <summary class="cursor-pointer text-sm text-blue-700 font-medium">Stage Summary</summary>
        <ul class="mt-2 text-sm list-disc ml-6">
          <li v-for="(summary, index) in summarizeStage(stage)" :key="index">
            <div class="font-medium">{{ summary.name }} – Quantity: {{ summary.quantity }}</div>
            <ul class="ml-4 text-gray-600 list-disc">
              <li v-for="(val, key) in summary.attributes" :key="key">{{ key }}: {{ val }}</li>
            </ul>
          </li>
        </ul>
      </details>

    </div>

    <details class="mt-6 border-t pt-4">
      <summary class="cursor-pointer text-base text-green-700 font-semibold">Project Summary</summary>
      <ul class="mt-2 text-sm list-disc ml-6 space-y-2">
        <li v-for="(summary, index) in summarizeProject()" :key="index">
          <div class="font-medium">{{ summary.name }} – Quantity: {{ summary.quantity }}</div>
          <ul class="ml-4 text-gray-600 list-disc">
            <li v-for="(val, key) in summary.attributes" :key="key">{{ key }}: {{ val }}</li>
          </ul>
        </li>
      </ul>
    </details>

    <!-- Modal -->

    <ProductSelectModal
      v-if="showModal"
      :products="products"
      :attribute-names="availableAttributes"
      @close="showModal = false"
      @select="handleProductSelect"
    />

  </div>
  <div v-else>Nie mamy pańskiego płaszcza i co nam pan zrobi?</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {createStage, deleteStage, getProjectDetails, updateProject, updateStage} from '@/integrations/projects/services'
import ProductSelectModal from "@/components/ProductSelectModal.vue";
import AddStageModal from "@/components/AddStageModal.vue";

const route = useRoute()
const project = ref(null)
const isDirty = ref(false)
const showModal = ref(false)
const currentStageIndex = ref(null)
let saveTimer = null
const stageSaveTimers = new Map()
const showAddStageModal = ref(false)

const products = ref([
  {
    id: 'p1',
    name: 'Brick A',
    attributes: { color: 'red', size: 'small' }
  },
  {
    id: 'p2',
    name: 'Cement B',
    attributes: { material: 'portland' }
  }
])
const availableAttributes = ref([
  'color', 'size', 'material', 'brand', 'length'
])


onMounted(async () => {
  const id = route.params.id
  project.value = await getProjectDetails(id)
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

const debouncedSaveStageName = (stage) => {
  const stageId = stage.id
  if (stageSaveTimers.has(stageId)) clearTimeout(stageSaveTimers.get(stageId))

  isDirty.value = true

  const timer = setTimeout(async () => {
    try {
      await updateStage(project.value.id, stage)
    } catch (err) {
      console.error(`Failed to update stage ${stageId}:`, err.message)
    } finally {
      isDirty.value = false
      stageSaveTimers.delete(stageId)
    }
  }, 1000)
  stageSaveTimers.set(stageId, timer)
}

const deleteProject = () => {
  console.log('Project deleted:', project.value.id)
}

const handleCreateStage = async (name) => {
  try {
    await createStage(project.value.id, name)
    project.value = await getProjectDetails(project.value.id)
    // project.value.stages.push(stage)
    showAddStageModal.value = false
  } catch (err) {
    console.error('Failed to create stage:', err.message)
  }
}

const removeStage = async (stageId) => {
  isDirty.value = true
  try {
    await deleteStage(project.value.id, stageId)
    project.value = await getProjectDetails(project.value.id)
    isDirty.value = false
  } catch (err) {
    console.error()
  }
}

const openModalForStage = (index) => {
  currentStageIndex.value = index
  showModal.value = true
}

const removeProduct = (stageIndex, productIndex) => {
  project.value.stages[stageIndex].products.splice(productIndex, 1)
}

const moveProduct = (stageIndex, productIndex, direction) => {
  const list = project.value.stages[stageIndex].products
  const target = productIndex + direction
  if (target < 0 || target >= list.length) return
  const temp = list[productIndex]
  list[productIndex] = list[target]
  list[target] = temp
}

const summarizeStage = (stage) => {
  const map = new Map()
  for (const item of stage.products) {
    const key = item.name + JSON.stringify(item.attributes)
    if (!map.has(key)) {
      map.set(key, {
        name: item.name,
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
        map.set(key, { name: item.name, attributes: item.attributes, quantity: 0 })
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
    position: stage.products.length,
    attributes: product.attributes
  })
  showModal.value = false
}
</script>
