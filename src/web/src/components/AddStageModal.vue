<!-- components/AddStageModal.vue -->
<template>
  <div class="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center z-50">
    <div class="bg-white w-[400px] p-6 rounded shadow">
      <h2 class="text-lg font-semibold mb-4">{{ $t('addStage') }}</h2>
      <input
        v-model="name"
        type="text"
        placeholder="Stage name"
        class="w-full border px-2 py-1 rounded mb-4"
      />
      <div class="flex justify-end gap-2">
        <button @click="$emit('close')" class="px-3 py-1 btn-standard-danger">{{ $t('cancel') }}</button>
        <button @click="submit" class="btn-standard">{{ $t('save') }}</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref } from 'vue'
const emit = defineEmits(['close', 'submit'])
const name = ref('')
const submit = () => {
  if (name.value.trim()) emit('submit', name.value.trim())
}
const { t } = useI18n()
</script>
