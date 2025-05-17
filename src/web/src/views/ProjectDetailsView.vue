<template>
  <!-- Breadcrumb -->
  <nav class="text-sm text-gray-500 mb-4">
    <router-link to="/projects" class="text-blue-600 hover:underline">Projects</router-link>
    <span class="mx-2">/</span>
    <span>{{ projectName || 'Project Details' }}</span>
  </nav>

  <div>
    <!-- Project Header -->
    <div class="flex justify-between items-center mb-2">
      <input
        v-model="projectName"
        @input="markDirty"
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
      <button
        @click="deleteProject"
        class="bg-red-600 text-white px-4 py-2 rounded ml-4"
      >
        Delete Project
      </button>
    </div>


    <button @click="addStage" class="bg-blue-700 text-white px-4 py-2 mb-4 rounded">+ Add Stage</button>

    <div v-for="(stage, stageIndex) in stages" :key="stageIndex" class="mb-6 border p-4 rounded">
      <div class="flex justify-between items-center mb-2">
        <input
          v-model="stage.name"
          @input="markDirty"
          class="text-lg font-bold border px-2 py-1 rounded w-full max-w-xs"
        />
        <div class="space-x-2">
          <button @click="openModalForStage(stageIndex)" class="bg-blue-600 text-white px-3 py-1 rounded">
            + Add Product
          </button>
          <button @click="removeStage(stageIndex)" class="bg-red-500 text-white px-3 py-1 rounded">
            Delete Stage
          </button>
        </div>
      </div>

      <div v-if="stage.products.length" class="space-y-3">
        <div
          v-for="(item, productIndex) in stage.products"
          :key="productIndex"
          class="border rounded p-2 text-sm space-y-1"
        >
          <div class="flex justify-between items-center">
            <span class="font-medium">{{ item.product.name }}</span>
            <div class="space-x-2">
              <button
                @click="moveProduct(stageIndex, productIndex, -1); markDirty()"
                :disabled="productIndex === 0"
                class="text-xs text-gray-500 hover:text-black"
              >↑</button>
              <button
                @click="moveProduct(stageIndex, productIndex, 1); markDirty()"
                :disabled="productIndex === stage.products.length - 1"
                class="text-xs text-gray-500 hover:text-black"
              >↓</button>
              <button
                @click="removeProductFromStage(stageIndex, productIndex)"
                class="text-red-500 hover:text-red-700 text-xs"
              >
                Remove
              </button>
            </div>
          </div>
          <ul class="ml-4 text-gray-700 list-disc">
            <li v-for="(val, key) in item.product.attributes" :key="key">{{ key }}: {{ val }}</li>
          </ul>
          <div class="flex items-center space-x-2 mt-1">
            <label class="text-xs">Quantity:</label>
            <input
              v-model.number="stages[stageIndex].products[productIndex].quantity"
              @input="markDirty"
              type="number"
              min="0"
              class="border px-2 py-1 rounded w-20"
            />
          </div>
        </div>
      </div>
      <div v-else class="text-sm text-gray-500">No products added yet.</div>
    </div>


    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
      <div class="bg-white w-[700px] h-[600px] p-6 rounded shadow-lg overflow-hidden">
        <div class="flex justify-end mb-2">
          <button @click="showModal = false" class="text-gray-500 hover:text-black text-xl font-bold">&times;</button>
        </div>
        <div class="space-y-4 h-full overflow-y-auto pr-2">
          <!-- Product name filter -->
          <div>
            <label class="block text-sm font-medium mb-1">Product Name</label>
            <input
              v-model="productNameFilter"
              type="text"
              class="w-full border rounded px-2 py-1"
              placeholder="Filter by product name"
            />
          </div>

          <!-- Filter input group -->
          <div class="flex items-end space-x-2">
            <div class="flex-1">
              <label class="block text-sm font-medium mb-1">Attribute Name</label>
              <input
                v-model="attributeNameInput"
                type="text"
                class="w-full border rounded px-2 py-1"
                placeholder="Start typing..."
                @input="filterAttributeNames"
                list="attributeNameList"
              />
              <datalist id="attributeNameList">
                <option
                  v-for="attr in filteredAttributeNames"
                  :key="attr"
                  :value="attr"
                />
              </datalist>
            </div>
            <div class="flex-1">
              <label class="block text-sm font-medium mb-1">Value</label>
              <input
                v-model="attributeValueInput"
                type="text"
                class="w-full border rounded px-2 py-1"
                placeholder="Enter value"
              />
            </div>
            <button
              @click="addFilter"
              class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
            >
              + Add
            </button>
          </div>

          <!-- Filtered attributes list -->
          <div v-if="filters.length > 0" class="flex flex-wrap gap-2">
            <div
              v-for="(filter, index) in filters"
              :key="index"
              class="bg-gray-200 px-2 py-1 rounded flex items-center"
            >
              <span class="mr-2">{{ filter.name }}={{ filter.value }}</span>
              <button
                @click="removeFilter(index)"
                class="text-red-500 hover:text-red-700 font-bold"
              >
                &times;
              </button>
            </div>
          </div>

          <!-- Filtered products list -->
          <div class="pt-4">
            <h2 class="text-lg font-semibold mb-2">Filtered Products</h2>
            <div
              v-for="(product, index) in filteredProducts"
              :key="index"
              class="border rounded p-2 mb-2 space-y-2"
            >
              <div class="font-medium">{{ product.name }}</div>
              <ul class="text-sm text-gray-700 ml-4 list-disc">
                <li v-for="(val, key) in product.attributes" :key="key">{{ key }}: {{ val }}</li>
              </ul>
              <div class="flex items-center space-x-2">
                <input
                  v-model.number="productQuantities[product.name]"
                  type="number"
                  min="0"
                  class="border px-2 py-1 rounded w-24"
                  placeholder="Quantity"
                />
                <button
                  @click="selectProduct(product)"
                  class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                >
                  Add
                </button>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const showModal = ref(false)
const currentStageIndex = ref(null)
const projectName = ref('My Project')
const isDirty = ref(false)

let autoSaveTimer = null

const scheduleAutoSave = () => {
  if (autoSaveTimer) clearTimeout(autoSaveTimer)
  isDirty.value = true
  autoSaveTimer = setTimeout(() => {
    console.log('Auto-saved project')
    isDirty.value = false
    autoSaveTimer = null
  }, 3000)
}

const markDirty = () => {
  isDirty.value = true
  console.log('Auto-saved project')
  scheduleAutoSave()

}

const deleteProject = () => {
  console.log('Project deleted:', projectName.value)
}

const stages = ref([
  { name: 'Stage 1', products: [] },
  { name: 'Stage 2', products: [] },
  { name: 'Stage 3', products: [] },
])

const addStage = () => {
  stages.value.push({ name: `Stage ${stages.value.length + 1}`, products: [] })
  markDirty()
}

const removeStage = (index) => {
  stages.value.splice(index, 1)
  markDirty()
}

const moveProduct = (stageIndex, productIndex, direction) => {
  const products = stages.value[stageIndex].products
  const targetIndex = productIndex + direction
  if (targetIndex < 0 || targetIndex >= products.length) return
  const temp = products[productIndex]
  products[productIndex] = products[targetIndex]
  products[targetIndex] = temp
  markDirty()
}


const availableAttributes = ref([
  'color',
  'size',
  'weight',
  'material',
  'brand',
  'length',
])

const products = ref([
  {
    name: 'Brick A',
    attributes: { color: 'red', size: 'small', weight: '1kg' },
  },
  {
    name: 'Cement B',
    attributes: { material: 'portland', weight: '25kg' },
  },
  {
    name: 'Steel Rod',
    attributes: { length: '2m', material: 'steel', brand: 'XBrand' },
  },
])

const attributeNameInput = ref('')
const attributeValueInput = ref('')
const productNameFilter = ref('')
const filters = ref([])
const productQuantities = ref({})

const filteredAttributeNames = computed(() => {
  return availableAttributes.value.filter(attr =>
    attr.toLowerCase().includes(attributeNameInput.value.toLowerCase())
  )
})

const filteredProducts = computed(() => {
  return products.value.filter(product => {
    const matchesName = product.name.toLowerCase().includes(productNameFilter.value.toLowerCase())
    const matchesAttributes = filters.value.every(f =>
      product.attributes[f.name]?.toLowerCase() === f.value.toLowerCase()
    )
    return matchesName && matchesAttributes
  })
})

function addFilter() {
  if (!attributeNameInput.value || !attributeValueInput.value) return
  filters.value.push({
    name: attributeNameInput.value,
    value: attributeValueInput.value,
  })
  attributeNameInput.value = ''
  attributeValueInput.value = ''
}

function removeFilter(index) {
  filters.value.splice(index, 1)
}

function openModalForStage(index) {
  currentStageIndex.value = index
  showModal.value = true
}

function selectProduct(product) {
  const quantity = productQuantities.value[product.name] || 0
  if (currentStageIndex.value !== null) {
    stages.value[currentStageIndex.value].products.push({ product, quantity })
  }
  showModal.value = false
  markDirty()
}

function removeProductFromStage(stageIndex, productIndex) {
  stages.value[stageIndex].products.splice(productIndex, 1)
  markDirty()
}
</script>

<style scoped>
</style>
