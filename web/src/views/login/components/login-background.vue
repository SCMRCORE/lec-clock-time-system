<template>
  <div class="wrapper">
    <div class="left">
      <div class="left-top">
        <img src="/login/block2.png" alt="lec" />
        <span class="white">
          <i>LEC STUDIO</i>
        </span>
        <span class="blue">
          <i>ALL IN ONE</i>
        </span>
      </div>

      <div class="left-bottom" ref="modelRef"></div>
    </div>

    <div class="right">
      <h1 class="title">乐程聚合平台</h1>
      <img src="/login/block1.png" alt="lec" />
    </div>
  </div>
</template>

<script lang="ts" setup>
// @ts-ignore
import lec3d from '@trickle/lec3d'
import { ref, onMounted } from 'vue'

const modelRef = ref()

onMounted(() => {
    
  const { scene, camera, mountTo, addControls } = lec3d.init()
  camera.position.set(-10, 1, 1000)
  camera.lookAt(0, 0, 0)
  addControls()
  lec3d.loadGLTF({
    modelPath: 'models/planet/scene.gltf',
    options: {
      scale: 400,
      position: {
        x: 0,
        y: 0,
        z: 0
      }
    },
    callback: (_: unknown, model: any) => {
      const playAnimation = () => {
        model.rotation.y += 0.001
        requestAnimationFrame(playAnimation)
      }
      playAnimation()
      scene.add(model)
    }
  })

  mountTo(modelRef.value)
})
</script>

<style lang="scss" scoped>
.wrapper {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  width: 100vw;
  min-width: 1120px;
  z-index: -10;

  .left {
    height: 100vh;
    width: 100vw;
    min-width: 600px;

    &-top {
      z-index: -1;
      font-size: 72px;
      font-weight: 900;

      .white {
        position: absolute;
        top: 32px;
        left: 32px;
        color: #fff;
      }

      .blue {
        position: absolute;
        left: 550px;
        top: 32px;
        color: #336add;
      }
    }

    &-bottom {
      position: absolute;
      /* TODO: 这是为了 lec v0.4.x 没有适配挂载容器的问题 */
      left: -400px;
      top: 200px;
      width: 300px;
      background-color: transparent;
    }
  }

  .right {
    z-index: -2;
    position: absolute;
    right: 0;
    top: 0px;
    img {
      height: 100vh;
      min-height: 900px;
    }
    .title {
      position: absolute;
      top: 48px;
      left: 100px;
      font-size: 72px;
      width: 100%;
      color: #fff;
      text-align: center;
    }
  }
}
</style>
