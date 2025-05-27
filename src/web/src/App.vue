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
          <RouterLink to="/projects" class="hover-scale" active-class="text-yellow-600">{{ $t('projects') }}</RouterLink>
          <RouterLink to="/store" class="hover-scale" active-class="text-yellow-600">{{ $t('store') }}</RouterLink>
        </nav>
      </div>

      <!-- Language Switcher -->
      <select v-model="$i18n.locale" class="bg-gray-800 text-xs text-white border border-gray-600 px-1 py-1 rounded">
        <option value="en">en</option>
        <option value="pl">pl</option>
      </select>
    </header>

    <!-- Routed Views -->
    <main class="flex-1 bg-gray-50 p-6">
      <RouterView />
    </main>
  </div>
</template>


<style scoped>
</style>
