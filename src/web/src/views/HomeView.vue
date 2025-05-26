<template>
  <div class="max-w-3xl mx-auto text-center space-y-8">
    <h1 class="text-4xl font-bold text-gray-800">🏗️ Welcome to Etapownik</h1>
    <p class="text-gray-600 text-lg">
      Manage your building projects and material inventory with ease. Navigate below to get started.
    </p>

    <div class="flex justify-center gap-6 mt-6">
      <button class="btn-standard text-heading">
        <RouterLink to="/projects">Go to Projects</RouterLink>
      </button>
      <button class="btn-standard">
        <RouterLink to="/store">Go to Store</RouterLink>
      </button>
    </div>
  </div>
</template>

<script setup>
</script>
