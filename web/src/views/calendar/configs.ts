import { formatTime } from './utils'
export const actionCard = [
  {
    label: '比赛名称',
    key: 'Name'
  },
  {
    label: '开始时间',
    key: 'ContestTime',
    render: (value: Date) => {
      return formatTime(value)
    }
  }
]
