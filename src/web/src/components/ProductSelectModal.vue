<!-- components/ProductSelectModal.vue -->
<template>
  <div class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
    <div class="bg-white w-[600px] max-h-[80vh] p-6 rounded shadow-lg overflow-auto">
      <div class="flex justify-between mb-4">
        <h2 class="text-lg font-semibold">Add Product</h2>
        <button @click="$emit('close')" class="text-gray-600 hover:text-black font-bold text-xl">&times;</button>
      </div>

      <input
        v-model="productNameFilter"
        type="text"
        placeholder="Filter by name"
        class="w-full border px-2 py-1 mb-4 rounded"
      />

      <div class="flex items-end gap-2 mb-4">
        <div class="flex-1">
          <label class="text-sm font-medium block">Attribute Name</label>
          <input
            v-model="attributeNameInput"
            class="w-full border px-2 py-1 rounded"
            list="attributeNameList"
          />
          <datalist id="attributeNameList">
            <option v-for="attr in attributeNames" :key="attr" :value="attr" />
          </datalist>

        </div>
        <div class="flex-1">
          <label class="text-sm font-medium block">Value</label>
          <input v-model="attributeValueInput" class="w-full border px-2 py-1 rounded" />
        </div>
        <button @click="addFilter" class="bg-blue-600 text-white px-3 py-1 rounded h-[38px]">+ Add</button>
      </div>

      <div v-if="filters.length" class="flex flex-wrap gap-2 mb-4">
        <div v-for="(f, i) in filters" :key="i" class="bg-gray-200 px-2 py-1 rounded flex items-center">
          <span>{{ f.name }}={{ f.value }}</span>
          <button @click="removeFilter(i)" class="ml-1 text-red-500">&times;</button>
        </div>
      </div>

      <div v-for="product in filteredProducts" :key="product.id" class="border p-3 rounded mb-2">
        <div class="font-medium">{{ product.name }}</div>
        <ul class="ml-4 text-sm list-disc text-gray-600">
          <li v-for="(val, key) in product.attributes" :key="key">{{ key }}: {{ val }}</li>
        </ul>
        <div class="flex items-center gap-2 mt-2">
          <input
            type="number"
            v-model.number="productQuantities[product.id]"
            min="0"
            class="border px-2 py-1 rounded w-24"
            placeholder="Quantity"
          />
          <button @click="emitAdd(product)" class="bg-green-600 text-white px-3 py-1 rounded">Add</button>
        </div>
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
