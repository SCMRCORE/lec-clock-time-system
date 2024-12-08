<template>
  <a-layout class="basic-layout">
    <!-- 乐程聚合平台 -->
    <template v-if="!props.hideHeader">
      <a-layout-header class="header">
        <basic-header></basic-header>
      </a-layout-header>
    </template>

    <div class="main">
      <!-- 左侧栏目 -->
      <template v-if="!props.hideLeftSidebar">
        <a-layout-sider class="sider" collapsible breakpoint="xl">
          <basic-left-sidebar></basic-left-sidebar>
        </a-layout-sider>
      </template>

      <!-- 中间主体 -->
      <a-layout-content class="content">
        <slot></slot>
      </a-layout-content>

      <!-- 右侧栏 -->
      <template v-if="!props.hideRightSidebar">
        <a-layout-sider
          class="sider right-sider"
          :hide-trigger="false"
          collapsible
          breakpoint="xl"
        >
          <basic-right-sidebar>
            <slot name="right-sidebar"></slot>
          </basic-right-sidebar>
        </a-layout-sider>
      </template>
    </div>

    <!-- 底部（没写） -->
    <a-layout-footer class="footer"></a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import BasicLeftSidebar from './components/basic-left-sidebar.vue'
import BasicHeader from './components/basic-header.vue'

const props = defineProps<{
  hideLeftSidebar?: boolean
  hideRightSidebar?: boolean
  hideHeader?: boolean
}>()
</script>

<style scoped lang="scss">
$header-height: 64px;
$header-bottom-gap: 12px;
$footer-height: 14px; // footer后面再加
$footer-top-gap: 40px;
$common-gap: 14px;
$common-radius: 4px;
.basic-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0 $common-gap $common-gap $common-gap;

  .header {
    position: fixed;
    left: 0;
    top: 0;
    width: 100vw;
    height: $header-height;
    display: flex;
    overflow: hidden;
    box-shadow: 1px 1px 6px #aaa;
    z-index: 100;
    background: #fff;
  }

  .main {
    display: flex;
    gap: $common-gap;
    margin-top: calc($header-height + $header-bottom-gap);
    height: 100%;
    max-height: calc(
      100vh - $header-height - $header-bottom-gap - $footer-height
    );
    min-height: 600px;
    /* overflow: hidden; */
    .sider {
      height: 100%;
      box-shadow: 1px 1px 10px #aaa;
      border-radius: $common-radius;
      overflow: hidden;
    }

    .content {
      height: 100%;
      overflow: auto;
      box-shadow: 1px 1px 6px #aaa;
      border-radius: $common-radius;
      padding: $common-gap;
      overflow: hidden;
    }

    .right-sider {
      min-width: 320px;
      max-width: 320px;
      padding: $common-gap;
      overflow-x: hidden;
    }
  }

  .footer {
  }
}
</style>
