import router from '@/router'
import { request } from '@/services/request'
import { Message } from '@arco-design/web-vue'

export const sidebarItems = [
  {
    key: '1',
    icon: 'icon-home',
    text: '工作台',
    clickEvent: () => {
      router.replace('/home')
    }
  },
  {
    key: '2',
    icon: 'icon-calendar-clock',
    text: '日程表',
    clickEvent: () => {
      router.replace('/calendar')
    }
  },
  {
    key: '3',
    icon: 'icon-common',
    text: '抽卡',
    clickEvent: () => {
      // router.replace('/lottery')
      Message.info('火热开发中，预计月底前上线~')
    }
  },
  {
    key: '999999',
    icon: 'icon-question-circle',
    text: '更多功能',
    clickEvent: () => {
      Message.info('开发ing~')
    }
  }
]

export const avatarDropdownItems = [
  {
    key: '1',
    icon: 'icon-home',
    text: '个人设置',
    clickEvent: () => {
      router.replace('/user')
    }
  },
  {
    key: '2',
    icon: 'icon-home',
    text: '退出登录',
    clickEvent: async () => {
      const token = localStorage.getItem('token')
      try {
        await request.post('/user/logout', null, {
          headers: {
            token
          }
        })
        Message.info('退出登录成功！')
        router.replace('/login')
      } catch (error) {
        Message.error('出错啦！')
        console.error(error)
      }
    }
  }
]
