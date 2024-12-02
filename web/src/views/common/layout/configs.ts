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
    key: '999999',
    icon: 'icon-question-circle',
    text: '更多功能',
    clickEvent: () => {
      Message.info('开发中，欢迎贡献 good idea~')
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

export const platforms = {
  internDevelopment: {
    name: '内部产品导航',
    list: [
      {
        icon: 'icon-github',
        text: '乐程开源',
        clickEvent: () => {
          window.open('https://github.com/lec-org')
        }
      }
    ]
  },
  externalApplication: {
    name: '第三方产品导航',
    list: [
      {
        icon: '',
        text: '学习通',
        clickEvent: () => {
          window.open('https://i.chaoxing.com/')
        }
      },
      {
        icon: '',
        text: 'bilibili',
        clickEvent: () => {
          window.open('https://www.bilibili.com/')
        }
      },
      {
        icon: '',
        text: '学校官网',
        clickEvent: () => {
          window.open('https://www.swpu.edu.cn/')
        }
      },
      {
        icon: '',
        text: '教务处',
        clickEvent: () => {
          window.open(
            'https://www.swpu.edu.cn/dean/searchList.jsp?wbtreeid=1193'
          )
        }
      },
      {
        icon: '',
        text: '力扣',
        clickEvent: () => {
          window.open('https://leetcode.cn/')
        }
      },
      {
        icon: '',
        text: '洛谷',
        clickEvent: () => {
          window.open('https://www.luogu.com.cn/')
        }
      }
    ]
  }
}
