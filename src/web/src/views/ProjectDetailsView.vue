<template>
  <div v-if="project" class="section">

    <div class="flex justify-between">
      <nav class="text-caption text-gray-500 mb-2">
        <router-link to="/projects" class="text-blue-600 hover:underline">Projects</router-link>
        <span class="mx-2">/</span>
        <span>{{ project?.name || 'Project Details' }}</span>
      </nav>
      <div v-if="isDirty" class="text-caption text-muted mb-2 flex items-center gap-2">
        <svg class="animate-spin h-4 w-4 text-muted" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 100 16 8 8 0 01-8-8z"></path>
        </svg>
        Saving
      </div>
      <div v-else class="text-caption text-muted mb-2">Saved</div>
    </div>
    <div class="flex justify-between items-center gap-4">
      <input
        v-model="project.name"
        @input="debouncedSaveName"
        class="text-base font-bold border px-3 py-1 rounded w-full max-w-md"
      />
      <div class="flex gap-2">
        <button
          @click="console.log('Export')"
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
          Export
        </button>
        <button @click="showAddStageModal = true" class="btn-standard">
          + Add Stage
        </button>
        <button @click="deleteProjectHandler" class="btn-standard-danger">
          Delete Project
        </button>
      </div>
    </div>
    <div class="text-muted mb-4">{{ project.id }}</div>

    <div
      v-for="(stage, stageIndex) in project.stages"
      :key="stage.id"
      class="mb-6 border p-4 rounded"
    >
      <div class="flex justify-between items-center">
        <input
          v-model="stage.name"
          @input="debouncedSaveStageName(stage)"
          class="text-base font-bold border px-2 py-1 rounded w-full max-w-xs"
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
          {{ isExpanded(stageIndex) ? 'Hide' : 'Show' }} details

        </button>
        <div class="space-x-2">
          <button @click="openModalForStage(stageIndex)" class="btn-standard">+ Add Product</button>
          <button @click="removeStage(stage.id)" class="btn-standard-danger">Delete Stage</button>
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
                <th class="px-3 py-2 text-left font-medium">Name</th>
                <th class="px-3 py-2 text-left font-medium">Identifier</th>
                <th class="px-3 py-2 text-left font-medium">Attributes</th>
                <th class="px-3 py-2 text-left font-medium">Quantity</th>
                <th class="px-3 py-2 text-left font-medium">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr
                v-for="(item, productIndex) in sortedProducts(stage)"
                :key="item.id"
                class="border-t hover:bg-gray-50"
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
                    class="border px-2 py-1 rounded w-full"
                  />
                </td>
                <td class="px-3 py-2 whitespace-nowrap space-x-1 text-xs text-gray-500">
                  <button
                    @click="moveProduct(stageIndex, productIndex, -1)"
                    :disabled="productIndex === 0"
                    class="hover-scale hover:scale-150 disabled:opacity-40">↑</button>
                  <button
                    @click="moveProduct(stageIndex, productIndex, 1)"
                    :disabled="productIndex === stage.products.length - 1"
                    class="hover-scale hover:scale-150 disabled:opacity-40">↓</button>
                  <button
                    @click="removeProduct(stageIndex, productIndex)"
                    class="text-red-600 hover-scale">Remove</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div v-else class="text-sm text-gray-500">No products added yet.</div>
      </div>
      <details class="mt-3">
        <summary class="cursor-pointer text-sm text-blue-700 font-medium">
          Stage Summary
        </summary>
        <div class="overflow-x-auto mt-2">
          <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
            <thead class="bg-gray-100">
            <tr>
              <th class="px-3 py-2 text-left font-medium">Name</th>
              <th class="px-3 py-2 text-left font-medium">Identifier</th>
              <th class="px-3 py-2 text-left font-medium">Attributes</th>
              <th class="px-3 py-2 text-left font-medium">Quantity</th>
            </tr>
            </thead>
            <tbody>
            <tr
              v-for="(summary, index) in summarizeStage(stage)"
              :key="index"
              class="border-t hover:bg-gray-50"
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


    <details class="mt-6 border-t pt-4">
      <summary class="cursor-pointer text-base text-green-700 font-semibold">
        Project Summary
      </summary>
      <div class="overflow-x-auto mt-2">
        <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
          <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 text-left font-medium">Name</th>
            <th class="px-3 py-2 text-left font-medium">Id</th>
            <th class="px-3 py-2 text-left font-medium">Attributes</th>
            <th class="px-3 py-2 text-left font-medium">Quantity</th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="(summary, index) in summarizeProject()"
            :key="index"
            class="border-t hover:bg-gray-50"
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
  createStage,
  deleteProject,
  deleteStage,
  getProjectDetails,
  updateProject,
  updateStage
} from '@/integrations/projects/services'
import ProductSelectModal from "@/components/ProductSelectModal.vue";
import AddStageModal from "@/components/AddStageModal.vue";
import {getAllProperties, getProducts} from "@/integrations/store/services.js";

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
  project.value = await getProjectDetails(id)

  products.value = await getProducts();
  availableAttributes.value = await getAllProperties();
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
      console.error(`Failed to update stage ${stageId}:`, err.message)
    } finally {
      isDirty.value = false
      stageSaveTimers.delete(stageId)
    }
  }, 1000)
  stageSaveTimers.set(stageId, timer)
}

const debouncedSaveStageName = (stage) => { updateStageHandler(stage); }

const deleteProjectHandler = async () => {
  await deleteProject(project.value.id)
  router.push({name: "projectListView"})
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
  const stage = project.value.stages[stageIndex];
  stage.products.splice(productIndex, 1)
  updateStageHandler(stage)
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
</script>
