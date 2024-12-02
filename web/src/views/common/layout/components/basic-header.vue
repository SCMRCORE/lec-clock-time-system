<template>
  <div class="basic-header">
    <div class="left">
      <div class="logo" @click="routeTo('/home')">
        <img class="pic" src="/common/lec@640x640.png" alt="这是logo" />
        <div class="text">
          <i> 乐程聚合平台 </i>
        </div>
      </div>
    </div>
    <div class="right">
      <a-dropdown>
        <a-avatar :size="42" class="avatar">LEC</a-avatar>
        <template #content>
          <template v-for="item in avatarDropdownItems" :key="item.key">
            <a-doption
              class="dropdown-item"
              :value="item.text"
              @click="item.clickEvent"
            >
              <a-space>
                <component :is="item.icon"></component>
                <span>
                  {{ item.text }}
                </span>
              </a-space>
            </a-doption>
          </template>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import router from '@/router'
import { avatarDropdownItems } from '../configs'

const routeTo = (str: string) => {
  router.replace(str)
}
</script>

<style scoped lang="scss">
$header-height: 64px;
.basic-header {
  line-height: $header-height;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 0 40px;
  width: 100%;
  box-shadow: 10px 10px 10px #333;
  .logo {
    display: flex;
    gap: 4px;
    cursor: pointer;
    .pic {
      height: 74px;
    }
    .text {
      font-size: 32px;
      font-weight: 900;
      color: #336add;
    }
  }
  .avatar {
    width: 100px;
    height: 100px;
    cursor: pointer;
  }
}
</style>
