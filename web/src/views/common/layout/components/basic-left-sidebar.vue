<template>
  <a-menu :default-open-keys="['1']" :style="{ width: '100%' }">
    <template v-for="item in sidebarItems" :key="item.key">
      <a-menu-item @click="item.clickEvent">
        <component :is="item.icon"></component>
        {{ item.text }}
      </a-menu-item>
    </template>
  </a-menu>
</template>

<script setup lang="ts">
import { sidebarItems } from '../configs'
</script>
