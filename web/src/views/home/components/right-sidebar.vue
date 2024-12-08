<template>
  <div class="right-side">
    <IconSettings class="settings" @click="routeToUser" />
    <template v-for="platform in platforms" :key="platform.name">
      <div class="platform">
        <h5 class="platform-name">{{ platform.name }}</h5>
        <div class="platform-items">
          <template v-for="item in platform.list" :key="item.text">
            <!-- TODO: 加一个判断逻辑，判断是icon还是img url -->
            <div class="item" @click="item.clickEvent">
              <component
                class="item-icon"
                :is="item.icon || 'icon-question-circle'"
              ></component>
              <div class="item-text">{{ item.text }}</div>
            </div>
          </template>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { platforms } from '../configs'
import { Message } from '@arco-design/web-vue'

// TODO: 跳转到用户配置页，可以编辑内容
const routeToUser = () => {
  Message.info('功能开发中...')
  // router.push('/user')
}
</script>

<style scoped lang="scss">
$gap: 13px;
$icon-size: 24px;
.right-side {
  width: 100%;
  height: 100%;
  position: relative;

  .settings {
    position: absolute;
    right: 6px;
    top: 0;
    width: $icon-size + 10px;
    height: $icon-size + 10px;
    cursor: pointer;
    color: var(--color-neutral-6);

    &:hover {
      transform: rotate(360deg);
      transition: 3s linear;
    }
  }
  .platform {
    margin: 4px 0 $gap 2px;
    &-name {
      font-size: 16px;
      margin-bottom: $gap;
    }
    &-items {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      .item {
        text-align: center;
        max-width: 72px;
        min-width: 72px;
        border-radius: 4px;
        margin-bottom: $gap;
        text-overflow: ellipsis;
        overflow: hidden;
        cursor: pointer;

        transition-duration: 0.5s;
        transition-delay: 0.2s;
        transition-property: all;
        &:hover {
          position: relative;
          z-index: 999;
          width: $gap + 10px;
          background: #e3eef0;
          opacity: 0.8;
          transform: scale(1.08);
          transition: 0.2s ease-in-out all;
        }

        &-icon {
          width: $icon-size;
          height: $icon-size;
          margin-bottom: 4px;
        }

        &-text {
          font-size: $gap;
          color: var(--color-neutral-6);
          text-wrap: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
        }
      }
    }
  }
}
</style>
