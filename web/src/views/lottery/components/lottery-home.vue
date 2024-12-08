<template>
  <div class="layout">
    <div class="router">
      <!-- 顶部导航 -->
      <Router />
    </div>

    <div class="main">
      <div class="left">
        <div class="title">
          <!-- 抽卡标题 -->
          <Title />
        </div>
        <div class="select">
          <!-- 卡池选项 -->
          <Select />
        </div>
      </div>
      <div class="mid">
        <!-- 抽卡内容 -->
        <Main />
      </div>
      <div class="right">
        <!-- 抽卡记录 -->
        <Record />
      </div>
    </div>

    <div class="button-area">
      <!-- 抽卡按钮 -->
      <Button />
    </div>

    <div class="footer">
      <!-- 底部 -->
      <Footer />
    </div>
  </div>
</template>

<script setup lang="ts">
import Router from './lottery-home/lo-router.vue'
import Title from './lottery-home/lo-title.vue'
import Select from './lottery-home/lo-select.vue'
import Main from './lottery-home/lo-main.vue'
import Record from './lottery-home/lo-record.vue'
import Button from './lottery-home/lo-button.vue'
import Footer from './lottery-home/lo-footer.vue'
</script>

<style lang="scss" scoped>
body {
  width: 100%;
  height: 100%;
  overflow: hidden;
  margin: 0;
}
.layout {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-width: 1500px;
  max-width: 1600px;
  height: 100%;
  // background: url(https://images.unsplash.com/photo-1709794819134-49a31ffe5b13?q=80&w=2340&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D)
  //   no-repeat;
  background-size: cover;
}
.router {
  width: 100%;
  // height: 80px;
  height: 120px;
  background-color: aqua;
}
.main {
  display: flex;
  justify-content: center;
  width: 100%;
  min-width: 1500px;
  max-width: 1600px;
  height: 640px;
  // background-color: bisque;
  .left {
    width: 20%;
    height: 100%;
    // background-color: red;
    .title {
      width: 100%;
      height: 18.8%;
      // background-color: rgb(139, 188, 188);
    }
    .select {
      width: 100%;
      height: 81.2%;
      // background-color: rgb(170, 229, 229);
    }
  }
  .mid {
    width: 46%;
    height: 100%;
    // background-color: yellow;
  }
  .right {
    width: 20%;
    height: 100%;
    background-color: blue;
  }
}
.button-area {
  width: 100%;
  height: 100px;
  background-color: #dbdbdb;
  .button {
  }
}
</style>
