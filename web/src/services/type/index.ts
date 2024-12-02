import type { RequestError } from '../request'

export interface ResponseData<T = any> {
[x: string]: any
  code: number
  msg: string
  data: T
}

export interface RequestResult<T> {
  data: any
  response?: ResponseData<T>
  error?: RequestError
}

export * from './api'
