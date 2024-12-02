<template>
  <div class="calendar-wrapper">
    <action-list :data="actionsOfOneDay"></action-list>
    <a-calendar
      v-model="currentTime"
      @panel-change="handlePanelChange"
      class="calendar-table"
    >
      <!-- TODO: 抽离成组件，不然看着有点乱 -->
      <template #default="scope">
        <div
          :class="getCalendarCellClassName({ ...scope })"
          @click="
            handleDateChange({
              ...scope
            })
          "
        >
          <div class="calendar-cell-date">
            {{ scope.date }}
          </div>
          <div class="calendar-cell-actions">
            <template
              v-for="action in actionsOfThreeMonth"
              :key="action.Name + action.ContestTime"
            >
              <template
                v-if="
                  formatTime(action.ContestTime, TIME_FORMAT_MODE.day) ===
                  formatTime(
                    new Date(scope.year, scope.month - 1, scope.date),
                    TIME_FORMAT_MODE.day
                  )
                "
              >
                <!-- TODO: 不同类型的活动可以用不同颜色 -->
                <div class="action-thumbnail">{{ action.Type }}</div>
              </template>
            </template>
          </div>
        </div>
      </template>
    </a-calendar>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
import { request } from '@/services/request'
import { ActionDataList, TIME_FORMAT_MODE } from '../types'
import { getBoundaryDayOfOneMonth, formatTime } from '../utils'

const currentTime = ref<Date>(new Date())
const currentDate = ref<number>(currentTime.value.getDate())
// 注意月是从0开始数的, 但是我们都从1开始
const currentMonth = ref<number>(currentTime.value.getMonth() + 1)
const currentYear = ref<number>(currentTime.value.getFullYear())

// TODO: 这是一个mock数据，之后记得换成真实数据
// 本月及相邻两个月的全部活动
const actionsOfThreeMonth = ref<ActionDataList>([
  {
    ContestTime: '2023-11-25T22:35:00+08:00',
    Name: 'CodeTON Round 7 (Div. 1 + Div. 2, Rated, Prizes!)',
    Url: 'https://codeforces.com/contests/1896',
    Type: 'CodeForces'
  },
  {
    ContestTime: '2023-11-25T22:35:00+08:00',
    Name: 'CodeTON Round 8 (Div. 1 + Div. 2, Rated, Prizes!)',
    Url: 'https://codeforces.com/contests/1896',
    Type: 'CodeForces'
  },
  {
    ContestTime: '2023-11-25T22:35:00+08:00',
    Name: 'CodeTON Round 9 (Div. 1 + Div. 2, Rated, Prizes!)',
    Url: 'https://codeforces.com/contests/1896',
    Type: 'CodeForces'
  },
  {
    ContestTime: '2023-11-24T22:35:00+08:00',
    Name: 'Educational Codeforces Round 158 (Rated for Div. 2)',
    Url: 'https://codeforces.com/contests/1901',
    Type: 'CodeForces'
  },
  {
    ContestTime: '2023-11-20T15:00:00+08:00',
    Name: 'ICPC 2023 Online Challenge powered by Huawei',
    Url: 'https://codeforces.com/contests/1885',
    Type: 'CodeForces'
  }
])
// 某一天的活动列表
const actionsOfOneDay = ref<ActionDataList>([])

// 更新年月
const updateTime = (time: Record<string, number>) => {
  // 不知道为什么arco-vue的双向绑定失效了，这里手动更新一下
  currentTime.value = new Date(time.year, time.month - 1, time.date)
  currentDate.value = time.date
  currentMonth.value = time.month
  currentYear.value = time.year
}

// 更新某一天的活动列表
const updateActionsOfOneDay = () => {
  actionsOfOneDay.value = actionsOfThreeMonth.value.filter((action) => {
    const actionDayTime = formatTime(action.ContestTime, TIME_FORMAT_MODE.day)
    const currentDayTime = formatTime(currentTime.value, TIME_FORMAT_MODE.day)
    return actionDayTime === currentDayTime
  })
}

const handleDateChange = (time: Record<string, number>) => {
  // 点击不会修改当前月份, 不然样式上会有难以调和的bug
  // 非本月的日期也不允许点击(因为也会有难以调和的bug，这里使用css禁用了点击)
  updateTime({
    ...time,
    month: currentMonth.value
  })
  updateActionsOfOneDay()
  console.log(currentMonth.value, currentDate.value)
}

const handlePanelChange = (date: Date) => {
  updateTime({
    year: date.getFullYear(),
    month: date.getMonth() + 1,
    date: date.getDate()
  })
}

// TODO: zhr的临时接口，目前貌似还没解决跨域问题, 后面可以考虑迁移一下
const getActionData = async (
  beginTime: Date | string,
  endTime: Date | string
) => {
  const data = await request.post(
    '/calendar',
    { beginTime: formatTime(beginTime), endTime: formatTime(endTime) },
    { baseURL: 'http://47.108.52.110:8888/' }
  )
  return data
}

// 更新一个月的活动数据
const updateActionsOfThreeMonth = async () => {
  const [beginTime, endTime] = getBoundaryDayOfOneMonth(
    currentYear.value,
    currentMonth.value
  )
  // 请求一个月内的全部活动
  const data = await getActionData(beginTime, endTime)
  // TODO: 改变最终的数值
  console.log('最终的数值', data)
}

const getCalendarCellClassName = (time: Record<string, number>) => {
  const { year, month, date } = time
  let className = 'calendar-cell'
  const nowTime = new Date()

  if (
    year === nowTime.getFullYear() &&
    month - 1 === nowTime.getMonth() &&
    date === nowTime.getDate()
  ) {
    className += ' today'
  } else if (currentYear.value === year && currentMonth.value === month) {
    if (date === currentDate.value) {
      className += ' active'
    }
  } else {
    className += ' non-same-month'
  }

  return className
}

watch(
  () => [currentMonth.value, currentYear.value],
  () => {
    updateActionsOfThreeMonth()
  }
)

onMounted(() => {
  updateActionsOfThreeMonth()
})
</script>

<style lang="scss" scoped>
.calendar-wrapper {
  height: 100%;
  display: flex;
  justify-content: space-between;
  gap: 14px;
  .calendar-table {
    flex: 1;
  }
}

.calendar-cell {
  height: 100%;
  padding: 8px 4px;
  position: relative;

  .calendar-cell-date {
    position: absolute;
    left: 4px;
    top: 10px;
    width: 24px;
    height: 24px;
    text-align: center;
    border-radius: 12px;
    cursor: pointer;
  }
  .calendar-cell-actions {
    display: flex;
    flex-direction: column;
    gap: 2px;
    position: absolute;
    top: 40px;
    width: 92%;
    height: 60%;
    padding: 2px;
    overflow-y: auto;

    &::-webkit-scrollbar {
      display: none; /* Chrome Safari */
    }
    .action-thumbnail {
      color: #fff;
      font-weight: 600;
      font-size: 12px;
      padding: 4px;
      text-overflow: ellipsis;
      background-color: #3c74f8ee;
      border-radius: 4px;
    }
  }

  &.today {
    .calendar-cell-date {
      color: #fff;
      padding: 4px;
      top: 6px;
      background-color: #165dff;
    }
  }

  &.non-same-month {
    cursor: not-allowed;
    pointer-events: none;
    color: #3337;
  }

  &.active {
    .calendar-cell-date {
      top: 6px;
      padding: 2px;
      text-align: center;
      border: #165dff 2px solid;
    }
  }
}
</style>
