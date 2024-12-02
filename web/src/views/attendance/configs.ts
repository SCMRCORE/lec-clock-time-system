interface RankListRow {
  rank: number | string
  avatar: string
  nickname: string
  currentTime: number
  targetTime: number
  completionRate: number
  status: number
}

export const rankListColumns = [
  {
    title: '排名',
    dataIndex: 'rank'
  },
  {
    title: '用户信息',
    dataIndex: 'userInfo',
    render: (row: RankListRow) => `<img src="${row.avatar}" alt="头像"/>`
  },
  {
    title: '当前时长',
    dataIndex: 'currentTime'
  },
  {
    title: '目标时长',
    dataIndex: 'targetTime'
  },
  {
    title: '完成度',
    dataIndex: 'completionRate'
  },
  {
    title: '状态',
    dataIndex: 'status'
  }
]

export const rankListGrades = [
  {
    value: 2020,
    text: '2020级'
  },
  {
    value: 2021,
    text: '2021级'
  },
  {
    value: 2022,
    text: '2022级'
  }
]
