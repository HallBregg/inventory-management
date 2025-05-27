<template>
  <div class="max-w-3xl mx-auto text-center space-y-8">
    <h1 class="text-4xl font-bold text-gray-800">🏗️ Etapownik</h1>
    <p class="text-gray-600 text-lg">
      {{ $t('welcome') }}
    </p>

    <div class="flex justify-center gap-6 mt-6">
      <button class="btn-standard">
        <RouterLink to="/projects">{{ $t('goToProjects') }}</RouterLink>
      </button>
      <button class="btn-standard">
        <RouterLink to="/store">{{ $t('goToStore') }}</RouterLink>
      </button>
    </div>
  </div>
</template>

<script setup>
</script>
