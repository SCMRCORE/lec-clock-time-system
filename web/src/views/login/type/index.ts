export interface LoginInfo {
  username: string
  password: string
}

export interface RegisterInfo extends LoginInfo {
  nickname: string
  email: string
  grade: number
  code:string
}
