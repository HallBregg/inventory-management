<template>

  <div class="section">


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
      <h1 class="text-title">Product Store</h1>
      <button @click="openAddModal" class="btn-standard">+ Add Product</button>
    </div>

    <!-- Filters -->
    <div class="rounded mb-4 space-y-4">
      <h2 class="text">Filter Products</h2>
      <div class="flex items-center gap-2">
        <input
          v-model="filterName"
          placeholder="Search by name"
          class="border px-3 py-1 rounded w-1/4"
        />
        <input
          v-model="attrKey"
          list="attrNames"
          placeholder="Attribute"
          class="border px-2 py-1 rounded w-1/4"
        />
        <input
          v-model="attrValue"
          placeholder="Value"
          class="border px-2 py-1 rounded w-1/4"
        />
        <button @click="addFilterAttribute" class="text-green-600 text-sm hover-scale px-3 py-2 border border-current  rounded-sm">+ Add</button>
      </div>

      <datalist id="attrNames">
        <option v-for="name in allAttributeNames" :key="name">{{ name }}</option>
      </datalist>
      <div class="flex flex-wrap gap-2">
        <span
          v-for="(filter, index) in filterAttributes"
          :key="index"
          class="inline-flex items-center justify-center rounded-full border border-blue-500 px-2.5 py-0.5 text-blue-600">
        <p class="text-sm whitespace-nowrap">{{ filter.key }}: {{ filter.value }}</p>

        <button
          @click="removeFilterAttribute(index)"
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

    <!-- Product List -->
    <div class="space-y-4">
      <div
        v-for="product in filteredProducts"
        :key="product.id"
        class="border rounded p-4 space-y-2"
      >
        <div class="flex justify-between items-center">
          <div>
            <span class="font-semibold">{{ product.name }}</span>
            <div class="text-xs text-gray-500">ID: {{ product.id }}</div>
          </div>
          <div class="space-x-2">
            <button @click="openEditModal(product)" class="text-blue-600 text-sm hover-scale hover:underline">Edit</button>
            <button @click="deleteProduct(product.id)" class="text-red-600 text-sm hover-scale hover:underline">Delete</button>
          </div>
        </div>

        <div class="flex flex-wrap gap-2 text-sm">

          <span
            v-for="(value, key) in product.attributes"
            :key="key"
            class="inline-flex items-center justify-center rounded-full border border-blue-500 px-2.5 py-0.5 text-blue-600 bg-blue-200">
            <p class="text-xs whitespace-nowrap">{{ key }}: {{ value }}</p>
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
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import ProductModal from '@/components/ProductModal.vue'
import {createProduct, getProducts} from "@/integrations/store/services.js";

const products = ref([
  { id: "fdgjahfdkjahfjhaghfaksfhsajkd", name: 'Cement A', attributes: { Type: 'Portland', Grade: '43' } },
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
