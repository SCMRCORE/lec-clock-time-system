<template>
  <div :class="['login-slider', activeForm]">
    <a-typography>
      <a-typography-title>Hello, Lec!</a-typography-title>
      <a-typography-paragraph>
        这里写一段描述但是我想不出来了
      </a-typography-paragraph>
    </a-typography>
    <div class="login-slider__button" @click="changeActiveForm">
      <template v-if="activeForm == 'login'">注册</template>
      <template v-else>登录</template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useLoginStore } from '../store/login'

const loginStore = useLoginStore()
const { activeForm } = storeToRefs(loginStore)
const { changeActiveForm } = loginStore
</script>

<style lang="scss">
.login-slider {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 450px;
  height: 100%;
  background: url('/src/assets/login.jpg') no-repeat;
  background-size: cover;
  transition:
    right 0.6s ease-in-out,
    transform 0.6s ease-in-out;

  @media screen and (min-width: 1280px) {
    width: 600px;
  }

  &.login {
    position: absolute;
    top: 0;
    right: 0;
  }

  &.register {
    position: absolute;
    top: 0;
    right: 100%;
    transform: translateX(100%);
  }

  .arco-typography {
    color: var(--color-neutral-2);
  }

  &__button {
    cursor: pointer;
    border: 1px solid var(--color-neutral-2);
    border-radius: 8px;
    margin-top: 24px;
    padding: 6px 16px;
    width: fit-content;
    height: 32px;
    color: var(--color-neutral-2);

    &:hover {
      border-color: var(--color-neutral-4);
      color: var(--color-neutral-4);
    }
  }
}
</style>
