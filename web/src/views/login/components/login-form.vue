<template>
  <div class="login-form" v-show="isActive">
    <div class="login-form__container">
      <a-form
        class="login-form__content"
        :model="loginInfo"
        @submit-success="debouncedLogin"
        layout="vertical"
      >
        <a-space class="board" direction="vertical">
          <h2 class="title">欢迎回来</h2>

          <a-form-item
            field="username"
            label="用户名"
            :rules="[
              { required: true, message: '用户名是必填项' },
              { minLength: 4, maxLength: 16, message: '用户名至少4位' }
            ]"
          >
            <a-input
              v-model="loginInfo.username"
              placeholder="输入用户名..."
              :max-length="16"
            />
          </a-form-item>

          <a-form-item
            field="password"
            label="密码"
            :rules="[
              { required: true, message: '密码是必填项' },
              { minLength: 6, maxLength: 20, message: '密码至少6位' }
            ]"
          >
            <a-input-password
              v-model="loginInfo.password"
              placeholder="输入密码..."
              :max-length="20"
            />
          </a-form-item>

          <a-form-item class="remember-check" field="isRemembered">
            <a-checkbox v-model="isRemembered">记住我</a-checkbox>
          </a-form-item>

          <a-button
            class="login-form__submit"
            type="primary"
            html-type="submit"
          >
            登录
          </a-button>

          <a-space class="register">
            <div>没有账号?</div>
            <a-link @click="checkToRegister">注册一个!</a-link>
          </a-space>
        </a-space>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useLoginStore } from '../store/login'
import { userLoginService } from '@/services'
import { Message } from '@arco-design/web-vue'
import { debounceAsync } from '@/services/debounce'
import router from '@/router'

const loginStore = useLoginStore()
const { activeForm, loginInfo } = storeToRefs(loginStore)
const isActive = computed(() => activeForm.value === 'login')
const isRemembered = ref(false)
const checkToRegister = () => {
    activeForm.value = 'register'
}

//记住我
watch(isRemembered,()=>{
    if(isRemembered.value===true){
        localStorage.setItem('username',loginInfo.value.username)
        localStorage.setItem('password',loginInfo.value.password)
    }else{
        let username = localStorage.getItem('username') || ""
        if(username !== ""){
            localStorage.removeItem('username')
            localStorage.removeItem('password')
        }
    }
})
const initLoginInfo = ()=>{
    loginInfo.value.username = localStorage.getItem('username') || ""
    loginInfo.value.password = localStorage.getItem('password') || ""
    if(loginInfo.value.username!==""){
        isRemembered.value = true
    }
}
initLoginInfo()


const handleSubmit = async (info: Record<string, any>) => {
  try {
    const res = await userLoginService(info)
    console.log(res)
    if (res.error) {
      if (res.error.code === 'ERR_TYPE_CHECK') {
        Message.error('账号或密码错误')
      } else {
        Message.error('登录失败：' + res.error.message)
      }
    } else {
      loginStore.setToken(res.response?.data.token)
      loginStore.setId(String(res.response?.data.userInfoVo.id));
      Message.success('登录成功')
      console.log(res.response?.data.userInfoVo.id)
      router.replace('/home')
    }
  } catch (error) {
    console.error(error)
    Message.error('登录失败：' + error)
  }
}
const debouncedLogin = debounceAsync(handleSubmit,500)

</script>

<style lang="scss">
.login-form {
  height: 100%;
  position: absolute;
  top: 0;
  right: 100px;

  &__container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 24px;
    width: 560px;
    height: 100%;
  }

  &__content {
    position: absolute;
    left: 96px;
    width: 420px;
    height: 400px;
    .board {
      min-height: 400px;
      padding: 24px 32px;
      border-radius: 36px;
      background-color: #fff;
      box-shadow: 2px 10px 20px #333e;

      .title {
        text-align: center;
        font-weight: 600;
        margin-bottom: 12px;
      }

      .remember-check {
        /* transform: translateY(-24px); */
        margin-top: -24px;
      }

      .register {
        width: 100%;
        display: flex;
        justify-content: center;
      }
    }
  }

  &__submit {
    width: 100%;
  }
}

.login-form-enter-active {
  transition: all 0.6s ease-in-out;
}

.login-form-leave-active {
  transition: all 0.6s ease-in-out;
}

.login-form-enter-from,
.login-form-leave-to {
  transform: translateX(40%);
  opacity: 0;
}
</style>
