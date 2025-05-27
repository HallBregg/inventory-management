<template>
  <div class="max-w-3xl mx-auto text-center space-y-8">
    <div class="flex flex-col items-center text-center">
      <!-- Logo + Name -->
      <div class="flex flex-col items-center">
        <img src="/src/assets/logo.png" alt="Logo" class="h-10 w-auto" />
        <p class="text-blue-950 text-3xl mt-4">Etapownik</p>
      </div>

      <!-- Divider (centered automatically) -->
      <div class="w-32 h-px bg-blue-800 my-4"></div>

      <!-- Welcome Text -->
      <p class="text-blue-950 text-lg mt-6">
        {{ $t('welcome') }}
      </p>
    </div>

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
