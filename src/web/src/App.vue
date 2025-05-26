<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <!-- Top Navigation -->
    <header class="bg-gray-800 text-white shadow px-6 py-3 flex items-center justify-between">
      <div class="flex items-center gap-4">
        <!-- Logo -->
        <RouterLink to="/">
          <div class="font-bold text-lg tracking-tight">🏗️ Etapownik</div>
        </RouterLink>

        <!-- Navigation -->
        <nav class="flex gap-4 text-sm">
          <RouterLink to="/projects" class="hover-scale" active-class="text-yellow-600">Projects</RouterLink>
          <RouterLink to="/store" class="hover-scale" active-class="text-yellow-600">Store</RouterLink>
        </nav>
      </div>
    </header>

    <!-- Routed Views -->
    <main class="flex-1 bg-gray-50 p-6">
      <RouterView />
    </main>
  </div>

</template>

<style scoped>
</style>
