<template>
  <div class="ranking-list">
    <a-radio-group class="radio-group" type="button">
      <template v-for="grade in rankListGrades" :key="grade.value">
        <a-radio
          :value="grade.value"
          @click="handleGradeChange(grade.value)"
          :checked="grade.value === selfUser.grade"
        >
          {{ grade.text }}
        </a-radio>
      </template>
      <template #empty>
        <a-empty />
      </template>
    </a-radio-group>

    <a-table
      :data="listData"
      :pagination="false"
      :scroll="{ y: 540 }"
      class="table"
    >
      <template #columns>
        <template v-for="col in rankListColumns" :key="col.dataIndex">
          <a-table-column :title="col.title" :data-index="col.dataIndex">
            <template #cell="{ record }" v-if="col.render">
              <div v-html="col.render?.(record)"></div>
            </template>
          </a-table-column>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { rankListColumns, rankListGrades } from '../configs'
import { ref, toRefs } from 'vue'
import { RankListRow } from '../type'
import { checkoutList } from '@/services'
import { User } from '../type/index'

const token = localStorage.getItem('token') || ''

const props = defineProps(['selfUser', 'userList'])
const { selfUser } = props
const { userList } = toRefs(props)

// TODO: 临时mock数据，类型有一点点问题，调真实数据的时候记得改改
const listData = ref<Array<RankListRow>>([])
const toggleCurrentGradeData = () => {
  listData.value = []
  userList?.value.forEach((item: User, index: number) => {
    let obj: RankListRow = {
      rank: 5,
      avatar: '',
      nickname: 'asd',
      currentTime: 1,
      targetTime: 23,
      completionRate: 1 / 23,
      status: 1
    }
    obj.rank = index + 1
    obj.avatar = item.avatar
    obj.nickname = item.nickname
    obj.currentTime = item.totalDuration
    obj.targetTime = item.targetDuration
    obj.completionRate = Number((obj.currentTime / obj.targetTime).toFixed(2))
    obj.status = item.status
    listData.value.push(obj)
  })
}
//打开页面时加载
watch(userList!, toggleCurrentGradeData)

const handleGradeChange = async (grade: number) => {
  if (grade === selfUser.grade) {
    toggleCurrentGradeData()
    return
  }
  const res = await checkoutList({
    grade: grade,
    pageSize: 40,
    pageNum: 1,
    token: token
  })

  if (res.response?.code === 200) {
    listData.value = []
    res.response.data.rows
      .sort((a: User, b: User) => b.totalDuration - a.totalDuration)
      .forEach((item: User, index: number) => {
        let obj: RankListRow = {
          rank: 5,
          avatar: '',
          nickname: 'asd',
          currentTime: 1,
          targetTime: 23,
          completionRate: 1 / 23,
          status: 1
        }
        obj.rank = index + 1
        obj.avatar = item.avatar
        obj.nickname = item.nickname
        obj.currentTime = Number((item.totalDuration / 60).toFixed(1))
        obj.targetTime = item.targetDuration / 60
        obj.completionRate = Number(
          (obj.currentTime / obj.targetTime).toFixed(2)
        )
        obj.status = item.status
        listData.value.push(obj)
      })
  }
}
</script>

<style scoped>
.ranking-list {
  .table {
    overflow: hidden;
  }
  .radio-group {
    margin-bottom: 6px;
  }
}

.status-red {
  background-color: rgba(20, 19, 19, 0.508);
}
</style>
