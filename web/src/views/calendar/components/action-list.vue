<template>
  <div class="action-list-wrapper">
    <div class="header">
      <div class="time">2023/11/19</div>
      <div class="options">
        <a-button type="primary" size="large">添加自定义活动</a-button>
      </div>
    </div>

    <div class="body">
      <template v-if="props.data?.length">
        <template v-for="item in props.data" :key="item.name">
          <a-card class="card" title="Arco Card" hoverable>
            <template #title>
              <h5>{{ item.Type }}</h5>
            </template>
            <template #extra>
              <a-link @click="goToRelatedPlatform(item.Url)">查看详情</a-link>
            </template>
            <template #default>
              <template v-for="config in actionCard" :key="config.key">
                <div class="meta">
                  <div class="label">{{ config.label }}</div>
                  <!-- TODO: 是的，这个地方写得很生草，后面的ts可以补补了 -->
                  <div class="value">
                    {{ 
                      config.render ? 
                      config.render((item as unknown as Record<string, string>)[config.key] as any)
                      : (item as unknown as Record<string, string>)[config.key] 
                    }}
                  </div>
                </div>
              </template>
            </template>
          </a-card>
        </template>
      </template>
      <template v-else>
        <a-empty class="card empty"></a-empty>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ActionDataList } from '../types'
import { actionCard } from '../configs'
import { Message } from '@arco-design/web-vue';
const props = defineProps<{
  data: ActionDataList
}>()

const goToRelatedPlatform = (url:string) => {
  try {
    window.open(url)
  } catch(err) {
    Message.error('无效链接, 跳转失败')
  }
}
</script>

<style lang="scss" scoped>
.action-list-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;

  .header {
    height: 80px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .time {
      font-size: 24px;
      font-weight: 500;
    }
  }

  .body {
    height: 100%;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    gap: 12px;
    .card {
      width: 360px;
      flex-shrink: 0;
      height: 180px;
      cursor: pointer;

      &.empty {
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }
    }
  }
}
.meta {
  margin-bottom: 10px;
  .label {
    font-weight: 800;
    margin-bottom: 4px;
  }

  .value {
    text-wrap: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}
</style>
