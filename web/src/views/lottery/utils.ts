import dayjs from 'dayjs'
import { TIME_FORMAT_MODE } from './types'

/** 获取某一年某一月的第一天和最后一天 */
export const getBoundaryDayOfOneMonth = (year: number, month: number) => {
  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)
  return [firstDay, lastDay]
}

/** 格式化时间 */
export const formatTime = (
  time: Date | string | number,
  mode = TIME_FORMAT_MODE.default
) => {
  switch (mode) {
    case TIME_FORMAT_MODE.day:
      return dayjs(time).format('YYYY-MM-DD')
    default:
      return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
  }
}
