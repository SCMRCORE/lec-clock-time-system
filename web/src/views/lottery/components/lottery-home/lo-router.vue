<template>
  <div>
    <div class="menu-demo">
      <a-menu mode="horizontal" :default-selected-keys="['1']">
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div
            :style="{
              width: '40px',
              height: '40px',
              borderRadius: '2px',
              background: 'var(--color-fill-3)',
              cursor: 'text'
            }"
          />
        </a-menu-item>
        <a-menu-item key="1" class="sel">抽卡</a-menu-item>
        <a-menu-item key="2" class="sel">我的卡库</a-menu-item>
        <a-menu-item key="3" class="sel">Cloud</a-menu-item>
        <a-menu-item key="4" class="sel">Cooperation</a-menu-item>
      </a-menu>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.menu-demo {
  box-sizing: border-box;
  width: 100%;
  padding: 0px;
  background-color: #dbdbdb;
  .arco-menu-inner {
    width: 100%;
    height: 100%;
    padding: 0 20px !important;
  }
  .sel{
    width: 100px;
    height: 100%;
    padding: 20px;
    text-align: center;
  }
}
</style>
