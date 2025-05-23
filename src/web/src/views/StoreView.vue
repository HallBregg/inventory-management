<template>

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
        <strong class="font-medium">Something went wrong</strong>
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


  <div class="p-6 space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold">Product Store</h1>
      <button @click="openAddModal" class="bg-blue-600 text-white px-4 py-2 rounded">+ Add Product</button>
    </div>

    <!-- Filters -->
    <div class="border p-4 rounded mb-4 space-y-4">
      <h2 class="font-semibold">Filter Products</h2>

      <input
        v-model="filterName"
        placeholder="Search by name"
        class="border px-3 py-1 rounded w-full"
      />

      <div class="flex items-center gap-2">
        <input
          v-model="attrKey"
          list="attrNames"
          placeholder="Attribute"
          class="border px-2 py-1 rounded w-1/3"
        />
        <input
          v-model="attrValue"
          placeholder="Value"
          class="border px-2 py-1 rounded w-1/2"
        />
        <button @click="addFilterAttribute" class="text-green-600 text-sm">+ Add</button>
      </div>

      <datalist id="attrNames">
        <option v-for="name in allAttributeNames" :key="name">{{ name }}</option>
      </datalist>

      <div class="flex flex-wrap gap-2">
        <span
          v-for="(filter, index) in filterAttributes"
          :key="index"
          class="bg-blue-100 text-sm px-2 py-1 rounded-full flex items-center gap-2"
        >
          {{ filter.key }}={{ filter.value }}
          <button @click="removeFilterAttribute(index)" class="text-red-600 text-xs">✕</button>
        </span>
      </div>
    </div>

    <!-- Product List -->
    <div class="space-y-4">
      <div
        v-for="product in filteredProducts"
        :key="product.id"
        class="border rounded p-4 space-y-2"
      >
        <div class="flex justify-between items-center">
          <span class="font-semibold">{{ product.name }}</span>
          <div class="space-x-2">
            <button @click="openEditModal(product)" class="text-blue-600 text-sm">Edit</button>
            <button @click="deleteProduct(product.id)" class="text-red-600 text-sm">Delete</button>
          </div>
        </div>
        <div class="flex flex-wrap gap-2 text-sm">
          <span
            v-for="(val, key) in product.attributes"
            :key="key"
            class="bg-gray-200 px-2 py-1 rounded-full"
          >
            {{ key }}={{ val }}
          </span>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <ProductModal
      v-if="showModal"
      :mode="modalMode"
      :product="selectedProduct"
      :all-attribute-names="allAttributeNames"
      @close="closeModal"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import ProductModal from '@/components/ProductModal.vue'
import {createProduct, getProducts} from "@/integrations/store/services.js";

const products = ref([
  { id: 1, name: 'Cement A', attributes: { Type: 'Portland', Grade: '43' } },
  { id: 2, name: 'Brick B', attributes: { Color: 'Red', Size: 'Small' } },
])

const showModal = ref(false)
const modalMode = ref('create') // 'create' or 'edit'
const selectedProduct = ref(null)

const filterName = ref('')
const filterAttributes = ref([]) // array of { key, value }
const attrKey = ref('')
const attrValue = ref('')
const error = ref(null)
const errorAlert = ref(false)

onMounted(async () => {
  await getProductsHandler()


})

const errorAlertCloseHandler = () => {
  errorAlert.value = false
  error.value = null
}

const propagateError = (err) => {
  error.value = err
  errorAlert.value = true
  console.log(err)
}

const getProductsHandler = async () => {
  try{ products.value = await getProducts(); } catch (err) { propagateError(err) }

}

const allAttributeNames = computed(() => {
  const keys = new Set()
  products.value.forEach(p => {
    Object.keys(p.attributes).forEach(k => keys.add(k))
  })
  return [...keys]
})

const filteredProducts = computed(() => {
  return products.value.filter(p => {
    if (filterName.value && !p.name.toLowerCase().includes(filterName.value.toLowerCase())) {
      return false
    }
    return filterAttributes.value.every(filter =>
      p.attributes[filter.key]?.toLowerCase() === filter.value.toLowerCase()
    )
  })
})

const addFilterAttribute = () => {
  if (attrKey.value && attrValue.value) {
    filterAttributes.value.push({ key: attrKey.value, value: attrValue.value })
    attrKey.value = ''
    attrValue.value = ''
  }
}

const removeFilterAttribute = (index) => {
  filterAttributes.value.splice(index, 1)
}

const openAddModal = () => {
  modalMode.value = 'create'
  selectedProduct.value = { name: '', attributes: {} }
  showModal.value = true
}

const openEditModal = (product) => {
  modalMode.value = 'edit'
  selectedProduct.value = { ...product }
  showModal.value = true
}

const handleSubmit = async (newProduct) => {
  if (modalMode.value === 'edit') {
    const index = products.value.findIndex(p => p.id === selectedProduct.value.id)
    if (index !== -1) products.value[index] = { ...selectedProduct.value, ...newProduct }
  } else {
    await createProduct(newProduct.name, newProduct.attributes)
    await getProductsHandler()
  }
  showModal.value = false
}

const closeModal = () => {
  showModal.value = false
}

const deleteProduct = (id) => {
  products.value = products.value.filter(p => p.id !== id)
}
</script>
