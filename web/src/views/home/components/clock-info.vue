<template>
  <div class="clock-info">
    <h2>
      {{
        selfUser.nickname
          ? `🎉 欢迎回来，${selfUser.nickname}`
          : '🤯 用户名加载失败'
      }}
    </h2>

    <div class="info-list">
      <div class="weather">
        <template v-for="item in weatherInfoList" :key="item.text">
          <div class="weather-item">
            <div>{{ item.date }}</div>
            <div>{{ item.text }}</div>
            <!-- TODO: 温度、天气及icon映射 -->
          </div>
        </template>
      </div>

      <div class="statistics">
        <template v-for="(item, index) in clockInfoDataList" :key="item.key">
          <div class="statistics-item">
            <div class="left">
              <img :src="item.icon" />
            </div>

            <div class="right">
              <div class="title">{{ item.title }}</div>
              <div class="data">
                <span class="current-value"
                  >{{ dataList[index] }} {{ item.unit }}</span
                >
                <span class="max-value">
                  / {{ item.maxValue }}{{ item.unit }}
                </span>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { clockInfoDataList, weatherInfoList } from '../configs'
const props = defineProps(['selfUser', 'dataList', 'userList'])
const selfUser = props.selfUser
const { userList } = toRefs(props)

watch(selfUser, () => {
  if (selfUser.adjustTargetDuration == 0) return
  // clockInfoDataList[0].maxValue = selfUser.adjustTargetDuration / 60
  const duration = Number(selfUser.value.adjustTargetDuration) || 0
  clockInfoDataList[0].maxValue = duration / 60
})
watch(userList!, () => {
  console.log(userList, 'userList')

  if (userList?.value.length === 0) return
  // clockInfoDataList[1].maxValue = userList?.value.length
  const count = userList.value?.length || 0
  clockInfoDataList[1].maxValue = count
})
</script>

<style scoped lang="scss">
.clock-info {
  .info-list {
    height: 120px;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    .weather {
      width: 240px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      gap: 12px;

      &-item {
        display: flex;
        gap: 9px;
        font-size: 14px;
        font-weight: 400;
        color: var(--color-neutral-6);
      }
    }

    .statistics {
      flex: 1;
      display: flex;
      justify-content: space-between;

      .statistics-item {
        height: 100%;
        width: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 12px;

        .left {
        }
        .right {
          display: inline-block;
          .title {
            font-size: 14px;
            color: var(--color-neutral-6);
            margin-bottom: 4px;
          }
          .data {
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: baseline;
            gap: 12px;

            .current-value {
              font-size: 20px;
              font-weight: 600;
            }

            .max-value {
              color: var(--color-neutral-6);
              font-size: 14px;
            }
          }
        }
      }
    }
  }
}
</style>
