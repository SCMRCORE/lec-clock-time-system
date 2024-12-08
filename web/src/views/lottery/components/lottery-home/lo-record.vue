<template>
  <div class="re-layout">
    <div class="top">
      <div class="left">
        <div class="icon">积</div>
        <div class="num">156,666</div>
      </div>
      <div class="mid"></div>
      <div class="right">图标</div>
    </div>
    <div class="list">
      <a-button type="primary" @click="handleClick">Open Drawer</a-button>
      <a-drawer
        :width="340"
        :visible="visible"
        @ok="handleOk"
        @cancel="handleCancel"
        unmountOnClose
      >
        <template #title> Title </template>
        <div>
          You can customize modal body text by the current situation. This modal
          will be closed immediately once you press the OK button.
        </div>
      </a-drawer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const visible = ref(false)

const handleClick = () => {
  visible.value = true
}
const handleOk = () => {
  visible.value = false
}
const handleCancel = () => {
  visible.value = false
}
</script>

<style scoped lang="scss">
.re-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  .top {
    width: 100%;
    height: 10%;
    background-color: #333;
    display: flex;
    justify-content: end;
    .left {
      width: 34%;
      display: flex;
      align-items: center;
      justify-content: center;
      .icon {
        font-size: 20px;
        font-weight: 600;
        color: #fff;
      }
      .num {
        font-size: 20px;
        font-weight: 600;
        font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande',
          'Lucida Sans', Arial, sans-serif;
        color: #fff;
      }
    }
    .mid {
      width: 4%;
      height: 100%;
      background-color: #333;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .right {
      width: 26%;
      height: 100%;
      background-color: #b5b3b3;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
</style>
