<template>
  <div class="bu-layout">
    <div class="bu-area">
      <div class="button left-b">
        <div class="border">
          <!-- <div class="font left-f"> -->
            <a-popconfirm
              content="确定花费100积分购买1张卡吗?"
              type="success"
              class="border"
            >
              <a-button class="font left-f">抽取一次</a-button>
            </a-popconfirm>
          <!-- </div> -->
        </div>
      </div>
      <div class="button right-b">
        <div class="border">
          <!-- <div class="font left-f"> -->
            <a-popconfirm
              content="确定花费1000积分购买10张卡吗?"
              type="success"
              class="border"
            >
              <a-button class="font right-f">抽取十次</a-button>
            </a-popconfirm>
          <!-- </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.bu-layout {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  .bu-area {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    width: 36%;
    height: 100%;
    // background-color: aquamarine;
  }
  .button {
    width: 42%;
    height: 80%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 20px;
    border: 6px solid transparent; /*8px的透明边框*/
    background-clip: padding-box, border-box; /*background-clip 设置元素的背景是否延伸到边框、内边距盒子、内容盒子下面。背景延伸至边框外沿（但是在边框下层）且背景延伸至内边距（padding）外沿。不会绘制到边框处。*/
    background-origin: padding-box, border-box; /*背景图片的摆放以 border padding 区域为参考*/
    background-image: linear-gradient(60deg, #91c791, #48745e, #3b6853),
      linear-gradient(-35deg, #d4ad66, #d3bd73, #f3f0a2);
    .border {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      border: black 3px solid;
      .font {
        color: #fff;
        font-size: 24px;
        font-family: '黑体';
        font-weight: 600;
        letter-spacing: 1px;
        text-shadow: 2px 2px 1px #333;
      }
      .left-f{
        width: 100%;
        height: 100%;
        color: #fff;
        background-color:#577c6a;
      }
      .right-f{
        width: 100%;
        height: 100%;
        color: #131313;
        text-shadow: 1px 1px 2px #333;
        background-color:#f1fff8;
      }
    }
  }
}
</style>
