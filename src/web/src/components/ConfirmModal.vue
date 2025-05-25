<template>
  <div v-if="visible" class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
    <div class="bg-white p-6 rounded shadow">
      <h2 class="text-lg font-semibold mb-4">{{ title }}</h2>
      <p class="mb-4">{{ message }}</p>
      <div class="flex justify-end space-x-2">
        <button @click="cancel" class="px-4 py-2 bg-gray-200 rounded">Cancel</button>
        <button @click="confirm" class="px-4 py-2 bg-red-600 text-white rounded">Delete</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['visible', 'title', 'message'],
  emits: ['confirm', 'cancel'],
  methods: {
    confirm() {
      this.$emit('confirm');
    },
    cancel() {
      this.$emit('cancel');
    }
  }
}
</script>
