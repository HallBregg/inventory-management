<!-- components/ProductSelectModal.vue -->
<template>
  <div class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
    <div class="bg-white w-[700px] max-h-[80vh] p-6 rounded shadow-lg overflow-auto">
      <!-- Header -->
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-lg font-semibold text-gray-800">Add Product</h2>
        <button @click="$emit('close')" class="text-gray-600 hover:text-black font-bold text-xl">&times;</button>
      </div>

      <!-- Name filter -->
      <input
        v-model="productNameFilter"
        type="text"
        placeholder="Filter by name"
        class="w-full border px-2 py-1 mb-4 rounded text-sm"
      />

      <!-- Attribute filters -->
      <div class="flex items-end gap-2 mb-4">
        <div class="flex-1">
          <label class="text-sm font-medium block">Attribute Name</label>
          <input
            v-model="attributeNameInput"
            class="w-full border px-2 py-1 rounded text-sm"
            list="attributeNameList"
          />
          <datalist id="attributeNameList">
            <option v-for="attr in attributeNames" :key="attr" :value="attr" />
          </datalist>
        </div>
        <div class="flex-1">
          <label class="text-sm font-medium block">Value</label>
          <input v-model="attributeValueInput" class="w-full border px-2 py-1 rounded text-sm" />
        </div>
        <button @click="addFilter" class="text-green-600 text-sm hover-scale px-3 py-2 border border-current  rounded-sm">+ Add</button>
      </div>

      <!-- Filter summary -->
      <div v-if="filters.length" class="flex flex-wrap gap-2 mb-4">

        <div class="flex flex-wrap gap-2">
        <span
          v-for="(filter, index) in filters"
          :key="index"
          class="inline-flex items-center justify-center rounded-full border border-blue-500 px-2.5 py-0.5 text-blue-600">
        <p class="text-sm whitespace-nowrap">{{ filter.name }}: {{ filter.value }}</p>

        <button
          @click="removeFilter(index)"
          class="ms-1.5 -me-1 inline-block rounded-full bg-blue-200 p-0.5 text-blue-700 transition hover:bg-blue-300">
          <span class="sr-only">Remove badge</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            class="size-3"
          >
            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
        </span>
        </div>
      </div>

      <!-- Product Table -->
      <div class="overflow-x-auto">
        <table class="min-w-full table-auto border border-gray-300 text-sm text-gray-800">
          <thead class="bg-gray-100">
          <tr>
            <th class="px-3 py-2 text-left font-medium">Product</th>
            <th class="px-3 py-2 text-left font-medium">Attributes</th>
            <th class="px-3 py-2 text-left font-medium">Quantity</th>
            <th class="px-3 py-2 text-left font-medium">Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="product in filteredProducts"
            :key="product.id"
            class="border-t hover:bg-gray-50"
          >
            <td class="px-3 py-2 font-medium">{{ product.name }}</td>
            <td class="px-3 py-2">
              <div class="flex flex-wrap gap-1">
                  <span
                    v-for="(val, key) in product.attributes"
                    :key="key"
                    class="inline-block rounded-full bg-blue-100 px-2 py-0.5 text-xs text-blue-700 border border-blue-300"
                  >
                    {{ key }}: {{ val }}
                  </span>
              </div>
            </td>
            <td class="px-3 py-2 w-24">
              <input
                type="number"
                v-model.number="productQuantities[product.id]"
                min="0"
                class="border px-2 py-1 rounded w-full"
                placeholder="Qty"
              />
            </td>
            <td class="px-3 py-2">
              <button
                @click="emitAdd(product)"
                class="text-blue-600 hover:underline hover-scale mr-2"
              >Add</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  products: Array,
  attributeNames: Array
})
const emit = defineEmits(['close', 'select'])

const filters = ref([])
const productNameFilter = ref('')
const productQuantities = ref({})
const attributeNameInput = ref('')
const attributeValueInput = ref('')

const addFilter = () => {
  if (!attributeNameInput.value || !attributeValueInput.value) return
  filters.value.push({ name: attributeNameInput.value, value: attributeValueInput.value })
  attributeNameInput.value = ''
  attributeValueInput.value = ''
}
const removeFilter = (i) => filters.value.splice(i, 1)

const filteredProducts = computed(() => {
  const result = props.products.filter(p => {
    const nameMatch = p.name.toLowerCase().includes(productNameFilter.value.toLowerCase())
    const attrMatch = filters.value.every(f =>
      p.attributes[f.name]?.toLowerCase() === f.value.toLowerCase()
    )
    return nameMatch && attrMatch
  })

  result.forEach(product => {
    if (productQuantities.value[product.id] == null) {
      productQuantities.value[product.id] = 1
    }
  })
  return result
})


const emitAdd = (product) => {
  const quantity = productQuantities.value[product.id] || 0
  emit('select', { product, quantity })
}
</script>
