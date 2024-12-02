export interface ActionDataItem {
  ContestTime: string | Date
  Name: string
  Url: string
  Type: string
}

export type ActionDataList = Array<ActionDataItem>

export enum TIME_FORMAT_MODE {
  default = 1,
  day = 10
}
