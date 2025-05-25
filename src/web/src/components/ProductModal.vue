<template>
  <div class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
    <div class="bg-white w-[600px] max-h-[90vh] p-6 rounded shadow overflow-y-auto">
      <h2 class="text-lg font-bold mb-4">{{ mode === 'edit' ? 'Edit Product' : 'Add Product' }}</h2>

      <!-- Name input -->
      <input v-model="localProduct.name" placeholder="Product name" class="border w-full px-3 py-2 rounded mb-4" />

      <!-- Attribute entry -->
      <div class="flex items-center gap-2 mb-4">
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
        <button @click="addAttribute" class="text-green-600 text-sm">+ Add</button>

        <datalist id="attrNames">
          <option v-for="name in allAttributeNames" :key="name">{{ name }}</option>
        </datalist>
      </div>

      <!-- Attribute pills -->

      <div class="flex flex-wrap gap-2">
        <span
          v-for="(value, key) in localProduct.attributes"
          :key="key"
          class="inline-flex items-center justify-center rounded-full border border-blue-500 px-2.5 py-0.5 text-blue-600">
        <p class="text-sm whitespace-nowrap">{{ key }}: {{ value }}</p>

        <button
          @click="removeAttribute(key)"
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

      <!-- Footer -->
      <div class="flex justify-end gap-2">
        <button @click="$emit('close')" class="px-4 py-2 border rounded">Cancel</button>
        <button @click="submit" class="px-4 py-2 bg-blue-600 text-white rounded">
          {{ mode === 'edit' ? 'Save Changes' : 'Add Product' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  mode: { type: String, default: 'create' },
  product: Object,
  allAttributeNames: Array,
})
const emit = defineEmits(['submit', 'close'])

const localProduct = reactive({
  name: '',
  attributes: {}
})

watch(() => props.product, () => {
  if (props.product) {
    localProduct.name = props.product.name
    localProduct.attributes = { ...props.product.attributes }
  }
}, { immediate: true })

const attrKey = ref('')
const attrValue = ref('')

const addAttribute = () => {
  if (attrKey.value && attrValue.value) {
    localProduct.attributes[attrKey.value] = attrValue.value
    attrKey.value = ''
    attrValue.value = ''
  }
}

const removeAttribute = (key) => {
  delete localProduct.attributes[key]
}

const submit = () => {
  emit('submit', { ...localProduct })
}
</script>
