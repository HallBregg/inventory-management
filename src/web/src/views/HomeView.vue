<template>
  <div class="max-w-3xl mx-auto text-center space-y-8">
    <h1 class="text-4xl font-bold text-gray-800">🏗️ Welcome to BuildMate</h1>
    <p class="text-gray-600 text-lg">
      Manage your building projects and material inventory with ease. Navigate below to get started.
    </p>

    <div class="flex justify-center gap-6 mt-6">
      <RouterLink to="/projects" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded text-lg">
        Go to Projects
      </RouterLink>
      <RouterLink to="/store" class="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded text-lg">
        Go to Store
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
</script>
