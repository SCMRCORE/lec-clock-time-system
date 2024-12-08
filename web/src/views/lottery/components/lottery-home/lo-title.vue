<template>
  <div class="ti-layout">
    <div class="title">LUCKY CARD</div>
    <div class="footer">
      <div class="bar">
        <div class="icon">@</div>
        <div class="time">2023.01.20-2020.02.10</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.ti-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  .title {
    width: 100%;
    height: 60%;
    line-height: 70px;
    font-size: 62px;
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    text-align: center;
    color: #fff;
    background-color: #333;
  }
  .footer {
    display: flex;
    align-items: center;
    width: 80%;
    height: 40%;
    margin-left: 10px;
    .bar {
      display: flex;
      align-items: center;
      width: 100%;
      height: 80%;
      border-radius: 0px;
      background-color: #333;
      .icon {
        width: 30px;
        height: 30px;
        margin-left: 4px;
        font-size: 22px;
        font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande',
          'Lucida Sans', Arial, sans-serif;
        text-align: center;
        color: #fff;
      }
      .time {
        line-height: 30px;
        font-size: 20px;
        font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande',
          'Lucida Sans', Arial, sans-serif;
        // font-family: Arial, Helvetica, sans-serif;
        // font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
        // font-family: Verdana, Geneva, Tahoma, sans-serif;
        // font-family: Georgia, 'Times New Roman', Times, serif;
        // font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
        // font-family: fantasy;
        // font-family: cursive;
        // font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
        text-align: center;
        color: #fff;
      }
    }
  }
}
</style>
