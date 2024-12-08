<template>
  <div class="se-layout">
    <div class="description">
      <div class="font">限定道具卡获取概率提升中，使用积分抽取卡牌，快来试试吧！</div>
    </div>
    <div class="pool"></div>
    <div class="footer"></div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.se-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  .description {
    width: 100%;
    height: 12%;
    background-color: rgb(64, 73, 70);
    .font {
      margin: 10px;
      font-size: 18px;
      font-family: '黑体';
      font-weight: 600;
      letter-spacing: 0.1em;
      color: #fff;
    }
  }
  .pool {
    width: 100%;
    height: 58%;
    background-color: #333;
  }
  .footer {
    width: 100%;
    height: 20%;
    background-color: #bc6b6b;
  }
}
</style>
