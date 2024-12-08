import { LoginInfo, RegisterInfo } from '../type'
type ActiveForm = 'login' | 'register'

export interface LoginStoreState {
  activeForm: ActiveForm
}

export const useLoginStore = defineStore('loginStore', () => {
  const activeForm = ref<ActiveForm>('login')

  const loginInfo = ref<LoginInfo>({
    username: '',
    password: ''
  })
  const registerInfo = ref<RegisterInfo>({
    username: '',
    nickname: '',
    password: '',
    email: '',
    grade: 1,
    code: ''
  })

  // 切换自动清空
  watch(activeForm, (value) => {
    if (value === 'register') {
      loginInfo.value = { username: '', password: '' }
    } else {
      registerInfo.value = {
        username: '',
        nickname: '',
        password: '',
        email: '',
        grade: 1,
        code: ''
      }
    }
  })

  const changeActiveForm = () => {
    if (activeForm.value === 'login') {
      activeForm.value = 'register'
    } else {
      activeForm.value = 'login'
    }
  }
  //  设置token
  const token = ref('')
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', token.value)
    //loginInfo
  }
  const setId = (newId: string | number) => {
    const idAsString = String(newId) // 确保 id 是字符串
    localStorage.setItem('id', idAsString)
  }
  return {
    activeForm,
    loginInfo,
    registerInfo,
    token,
    changeActiveForm,
    setToken,
    setId
  }
})
