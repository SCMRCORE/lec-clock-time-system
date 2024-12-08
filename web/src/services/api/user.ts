import { request } from '@/services/request'
import { number } from 'echarts';
//注册功能
export const userRegisterService = ({
  username,
  nickname,
  password,
  grade,
  email,
  code
}: Record<string, any>) => {
  return request.post('/user/register', {username,nickname,password,grade,email,code,});
}

//发送验证码功能
export const userCodeService = (email: string) => {
  const formData = new FormData()
  formData.append('email', email)

  return request.post('/user/register/sendCode', formData)
}

//登陆功能
export const userLoginService = ({
  username,
  password
}:Record<string,any>)=>{
  return request.post('/user/login',{
    username,
    password
  })
}

//上下卡
export const startClockInService = (token:string)=>{
    const config = {
        headers: {
            token: token,
        },
    }
    return request.post('/clock/clock',null,config)
}

// 验证token
export const checkoutToken = (token:string)=>{
    const config:Object = {
        headers:{
            token:token,
        },
    }
    return request.get('/user/isDead',config)
}

// 获取个人信息
export const getUserInfoService =async ({
    id,
    token,
}:Record<string,any>)=>{
    const config = {
        headers: {
          token: token
        }
      }
    return await request.get(`/user/info/${id}`,config)  
}
//个人信息————保存个人信息
export const saveinfoService = async(name:any,signature:any,token:string)=>{
    const config = {
        headers:{
            token:token
        }
    }
    const data = {
        nickname:name,
        signature:signature
    }
    return await request.put('/user/info/update',data,config)
}

// 上传头像
export const uploadFileServices = async (file:any,token:string)=>{  
    const formData = new FormData()  
    formData.append('image',file) 
    const config = {  
        headers: {  
          token: token,  
        },  
      }  
    return await request.put('/user/upload',formData,config)  
}

// 查看打卡列表
export const checkoutList = async({
    grade:grade,
    pageSize:pageSize,
    pageNum:pageNum,
    token
}:Record<string,any>) =>{
    const data = {grade,pageSize,pageNum}
    const config = {
        headers: {
            token: token
        }
    }
    return await request.post('/clock/list',data,config)
}

//查看打卡信息
export const checkoutInfo = async({
    token,
    id
}:Record<string,any>) =>{
    const config = {
        headers:{
            token
        }
    }
    return await request.get(`/clock/nowClock/${id}`,config)
}

export const getWeekClock = async(token:string) =>{
    const config = {
        headers:{
            token
        }
    }
    return await request.get(`user/day`,config)
}