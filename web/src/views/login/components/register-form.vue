<template>
  <transition name="register-form">
    <div class="register-form" v-show="isActive">
      <div class="register-form__container">
        <a-form
          class="register-form__content"
          :model="registerInfo"
          @submit-success="debouncedRegister"
          layout="vertical"
        >
          <a-space class="board" direction="vertical">
            <h2 class="register-form__title">用户注册</h2>
            <a-form-item
              field="username"
              label="用户名"
              :rules="[
                { required: true, message: '用户名是必填项' },
                { minLength: 4, maxLength: 16, message: '用户名应在4-16位' }
              ]"
            >
              <a-input
                v-model="registerInfo.username"
                placeholder="输入用户名..."
                :max-length="16"
              />
            </a-form-item>
            <a-form-item
              field="nickname"
              label="昵称"
              :rules="[{ required: true, message: '昵称是必填项' }]"
            >
              <a-input
                v-model="registerInfo.nickname"
                placeholder="输入昵称..."
                :max-length="8"
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
                v-model="registerInfo.password"
                placeholder="输入密码..."
                :max-length="20"
                allow-clear
              />
            </a-form-item>
            <a-form-item
              field="grade"
              label="年级"
              :rules="[{ required: true, message: '年级是必填项' }]"
            >
              <a-select
                placeholder="please select"
                v-model="registerInfo.grade"
              >
                <a-option :value="1">大一萌新</a-option>
                <a-option :value="2">大二中登</a-option>
                <a-option :value="3">大三老东西</a-option>
              </a-select>
            </a-form-item>
            <a-form-item
              field="email"
              label="邮箱"
              :rules="[
                { required: true, message: '邮箱是必填项' },
                { type: 'email', message: '邮箱格式不正确' },
                { minLength: 1, message: '邮箱不能为空' }
              ]"
            >
              <a-input
                placeholder="邮箱是必填项"
                v-model="registerInfo.email"
              ></a-input>
              <a-button type="primary" shape="round" @click="debouncedEmail"
                >发送验证码</a-button
              >
            </a-form-item>
            <a-form-item
              field="code"
              label="验证码"
              :rules="[{ required: true, message: '验证码是必填项' }]"
            >
              <a-input
                placeholder="请输入邮箱验证码"
                v-model="registerInfo.code"
              ></a-input>
            </a-form-item>
            <a-button
              class="register-form__submit"
              type="primary"
              html-type="submit"
            >
              注册
            </a-button>

            <a-space class="login">
              <div>注册完成?</div>
              <a-link @click="checkToLogin">返回登陆</a-link>
            </a-space>
          </a-space>
        </a-form>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { useLoginStore } from '../store/login'
import { userRegisterService, userCodeService } from '@/services'
import { Message } from '@arco-design/web-vue'
import { debounceAsync } from '@/services/debounce'

const loginStore = useLoginStore()
const { activeForm, registerInfo } = storeToRefs(loginStore)
const { changeActiveForm } = loginStore
const isActive = computed(() => activeForm.value === 'register')
const checkToLogin = () => {
  activeForm.value = 'login'
}
const handleSubmit = async (info: Record<string, any>) => {
  const res = await userRegisterService(info)
  console.log(res)

  if (res.error?.response?.data.code == 200) {
    Message.success('注册成功')
    changeActiveForm()
  } else if (res.response?.data.code == 200) {
    Message.success('注册成功')
    changeActiveForm()
  } else {
    Message.error('注册失败')
  }
}
const debouncedRegister = debounceAsync(handleSubmit, 500)

const sendEmail = async () => {
  const { email } = unref(registerInfo)
  if (email === '') {
    Message.error('邮箱不能为空')
    return
  }
  Message.success('发送成功，让验证码飞一会')
  await userCodeService(email)
  //   console.log(res)
}
const debouncedEmail = debounceAsync(sendEmail, 500)
</script>
<style lang="scss">
.register-form {
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
    height: 710px;
    .board {
      min-height: 710px;
      padding: 24px 32px;
      border-radius: 36px;
      background-color: #fff;
      box-shadow: 2px 10px 20px #333e;

      .register-form__title {
        text-align: center;
        font-weight: 600;
        margin-bottom: 12px;
      }
      .login {
        width: 100%;
        display: flex;
        justify-content: center;
        max-height: 60px;
      }
    }
  }

  &__title {
    text-align: center;
    margin-bottom: 24px;
  }

  &__submit {
    display: block;
    width: fit-content;
    margin: auto;
  }
}

.register-form-enter-active {
  transition: all 0.6s ease-in-out;
}

.register-form-leave-active {
  transition: all 0.6s ease-in-out;
}

.register-form-enter-from,
.register-form-leave-to {
  transform: translateX(-40%);
  opacity: 0;
}
</style>
