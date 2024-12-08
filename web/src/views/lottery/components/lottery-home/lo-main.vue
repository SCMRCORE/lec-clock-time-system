<template>
  <div class="ma-layout">
    <div class="container">
      <div class="wrap" id="dice">
        <div class="face x first-face"></div>
        <div class="face second-face"></div>
        <div class="face third-face"></div>
        <div class="face forth-face"></div>
        <div class="face fifth-face"></div>
        <div class="face sixth-face"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
div {
  box-sizing: border-box; /*元素的width和height包括content、padding和border的宽度，但不包括margin*/
}
.ma-layout {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.face {
  position: absolute;
  width: 320px;
  height: 500px;
  padding: 20px; /*w/h/p都包裹边框的宽度，是从边框的最里往里padding*/
  opacity: 0.95;
  border: 8px solid transparent; /*8px的透明边框，就是渐变边框的宽度*/
  background-clip: padding-box, border-box; /*必要的两项*/
  background-origin: padding-box, border-box; /*必要的两项*/
  background-image: linear-gradient(135deg, #e1eeffbb, #f3d5ffbb),
    linear-gradient(135deg, rgb(0, 60, 255), rgb(0, 225, 255), #f0f); /*渐变角度+渐变颜色*/
  animation: rotate 8s linear infinite;
}

.first-face {
  transform: translateZ(50%);
}
.second-face {
  transform: translateX(-50%) rotateY(90deg);
}
.third-face {
  transform: translateX(50%) rotateY(90deg);
}
.forth-face {
  transform: translateY(50%) rotateX(90deg);
}
.fifth-face {
  transform: translateY(-50%) rotateX(90deg);
}
.sixth-face {
  transform: translateZ(-50%);
}
.wrap {
  position: relative;
  width: 320px;
  height: 500px;
  //   transform-style: preserve-3d;
  transform: rotateX(10deg) rotateY(20deg) rotateZ(-6deg);
  transition: transform 3s;
  transform-origin: center center;
}
// .wrap:hover {
//   transform: rotateX(-10deg) rotateY(-370deg) rotateZ(0deg) !important;
//   transition: transform 2s;
// }
/* 动画 */
@keyframes rotate {
  0% {
    transform:translateY(0%) rotateX(0deg);
    transform:translateY(0%) rotateY(0deg);
    transform:translateY(0%) rotateZ(-10deg);
  }
  50% {
    transform:translateY(-5%) rotateX(180deg);
    transform:translateY(-5%) rotateY(180deg);
  }
  100% {
    transform:translateY(0%) rotateX(360deg);
    transform:translateY(0%) rotateY(360deg);
    transform:translateY(0%) rotateZ(-10deg);
  }
}
</style>
