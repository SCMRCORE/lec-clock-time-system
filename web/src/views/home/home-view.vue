<script setup lang="ts">
import { ref, onMounted } from 'vue'
import BasicLayout from '@/views/common/layout/basic-layout.vue'
import RankList from './components/rank-list.vue'
import clockInfo from './components/clock-info.vue'
import { checkoutToken, getWeekClock } from '@/services'
import router from '@/router'
import { checkoutInfo, checkoutList, getUserInfoService } from '@/services'
import { User } from './type/index'
import { Message } from '@arco-design/web-vue'

const userList:Ref<User[]> = ref<Array<User>>([])
const selfUser:User = reactive<User>({
    avatar: '',
    nickname: '',
    totalDuration: 0,
  adjustTargetDuration: 0,
    grade: 0,
    status: 0
})
const dataList:Ref<any[]> = ref<any>([])

// 检查 token 是否存在
const weekClock:Ref<any[]> = ref<any>([])
const token = localStorage.getItem('token') || ''

const checkToken = async () => {
    const res = await checkoutToken(token)
    if(res.response?.code!==200){
        localStorage.removeItem("token")
        Message.error("token过期啦")
        router.replace('/login')
    }
    const res1 = await getWeekClock(token)
    console.log(res1);
    const data = res1.response?.data
    const week = ['mon','tue','wed','thu','fri','sat','sun']
    for(let i of week){
        weekClock.value.push(data[i])
    }
}

// 页面加载时检查 token

onMounted(() => {
  checkToken()
  const sidebar = document.getElementById('sidebar')
  if (sidebar) {
    sidebar.style.width = '0'
    sidebar.style.height = '0'
  }
})

const id = localStorage.getItem('id') || ''
//更新个人信息
onMounted(async () => {
  try {
    const res = await getUserInfoService({ id, token })
    const info = res.response?.data
    if (info) {
      selfUser.nickname = info.nickname
      selfUser.grade = info.grade
      getCardList()      
    } else {
      Message.error('获取用户信息失败')
    }
  } catch (error) {
    console.error()
  }
})

//获取打卡时长
onMounted(async () => {
  const res = await checkoutInfo({ token, id })
  if (res.response?.code === 200) {
    selfUser.totalDuration = res.response?.data.totalDuration
    selfUser.adjustTargetDuration = res.response?.data.adjustTargetDuration
    selfUser.status = res.response?.data.status
    dataList.value.push(Math.floor(selfUser.totalDuration / 60))
  }
})

//获取打卡排名
const getCardList = async () => {
  const response = await checkoutList({
    grade: selfUser.grade as number,
    pageSize: 40,
    pageNum: 1,
    //TODO token测试
    token: token
  })
  const responseData = response.response
  // console.log('responseData',responseData);
  if (responseData?.code === 200) {
    userList.value = responseData.data.rows.map((row: any) => {
        return {
        avatar: row.avatar,
        nickname: row.nickname,
        totalDuration: Number((row.totalDuration / 60).toFixed(1)),
          adjustTargetDuration: row.adjustTargetDuration / 60,
        grade: selfUser.grade,
        status: row.status
      }
    }) as User[]
    userList.value.sort((a:User, b:User) => b.totalDuration - a.totalDuration)
  }
  userList.value.forEach((item:User, index:number) => {
    if (item.nickname === selfUser.nickname) {
      dataList.value.push(index + 1)
    }
  })
  console.log(userList);
//   TODO:代办没有完成
  dataList.value.push(0)
}
</script>

<template>
  <basic-layout>
    <template #default>
      <div class="content-wrapper">
        <div class="left">
          <a-layout>
            <a-layout-header class="header">
              <!-- 头部信息栏目 -->
              <clock-info :selfUser="selfUser" :dataList="dataList" :userList="userList"/>
            </a-layout-header>

            <a-layout-content class="content">
              <!-- 开始打卡 -->
              <clock-button :selfUser="selfUser"/>
              <!-- 图表(折线图) -->
              <line-chart :weekClock="weekClock"/>
            </a-layout-content>

            <a-layout-footer>
              <!-- 排行榜 -->
              <rank-list :userList="userList" :selfUser="selfUser" v-if="selfUser.grade!==0"></rank-list>
            </a-layout-footer>

            <!-- 好像没搞？ -->
            <a-layout-footer> </a-layout-footer>
          </a-layout>
        </div>
      </div>
    </template>

    <template #right-sidebar>
      <!-- 右侧导航 -->
      <right-sidebar></right-sidebar>
    </template>
  </basic-layout>
</template>

<style scoped lang="scss">
.content-wrapper {
  display: flex;
  height: 100%;
  gap: 24px;

  .header {
    border-bottom: 1px solid #3333;
  }
  .left {
    flex: 2;
    width: 100%;

    .content {
      display: flex;
      align-items: center;
      gap: 12px;
      max-height: 300px;
    }
  }

  .right {
  }
}
</style>
