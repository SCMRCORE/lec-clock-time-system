<template>
  <basic-layout hide-right-sidebar>
    <a-space>
      <indivadual-info :selfUser="selfUser"></indivadual-info>
    </a-space>
  </basic-layout>
</template>

<script setup lang="ts">
import { getUserInfoService } from '@/services'
import BasicLayout from '@/views/common/layout/basic-layout.vue'
import { Message } from '@arco-design/web-vue'
const selfUser = reactive({
  avatar: '',
  nickname: '',
  signature: '',
  grade: 0
})
const id = localStorage.getItem('id') || ''
const token = localStorage.getItem('token') || ''
onMounted(async () => {
  try {
    const res = await getUserInfoService({ id, token })
    const info = res.response?.data
    if (info) {
      selfUser.avatar = info.avatar
      selfUser.nickname = info.nickname
      selfUser.grade = info.grade
      selfUser.signature = info.signature
    } else {
      Message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error()
  }
})
</script>

<style scoped lang="scss"></style>
