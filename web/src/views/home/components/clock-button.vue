<template>
    <div class="go" @click="startClockIn">
        <template v-if="!isClocking">
            <div class="text">开始打卡</div>
        </template>
        <template v-else>
            <div class="text">
                <div class="main">结束打卡</div>
                <div class="time">
                    {{ dayjs.unix(clockingTime).format('HH:mm:ss') }}
                </div>
            </div>
        </template>
        <div class="L_">
            <div class="l_"></div>
            <div class="l_"></div>
            <div class="l_"></div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { ref, defineProps } from 'vue'
import dayjs from 'dayjs'
import { startClockInService } from '@/services';

const props = defineProps(['selfUser'])

// eslint-disable-next-line vue/no-dupe-keys
const { selfUser } = props
// 时区偏差修正
const timeZoneOffset = -8 * 1000 * 60 * 60
// TODO: 查询当前打卡状态
const isClocking = ref(false)
const clockingTime = ref(timeZoneOffset)
/** 计时器单例 */
const singleInterval = ref<number>()
const beginTime = localStorage.getItem("beginTime") || ""
const token = localStorage.getItem('token') || ""

const startClockIn = async () => {
    try {
        const response = await startClockInService(token)
        isClocking.value = !isClocking.value
        selfUser.status = response.response?.data.status
        
        if (response.response?.data.status == 1) {
            Message.success("成功上卡")
            localStorage.setItem('beginTime', response.response?.data.beginTime);
            
            singleInterval.value = setInterval(() => {
                clockingTime.value++
            }, 1000)
        } else if (response.response?.data === "打卡失败！！！，请在团队内打卡") {
            Message.error('打卡失败！！！，请在团队内打卡')
            return;
        }else if(response.error?.response?.data.code===405){
            Message.error("打卡失败,打卡时间超过5h!!!!")
                return;
        } 
        else {
            console.log(response.response);
            Message.success(`成功下卡，本周已经成功打卡${response.response?.data.totalDuration}分钟`)
            localStorage.removeItem('beginTime');
            clearInterval(singleInterval.value)
            singleInterval.value = 0
            clockingTime.value = timeZoneOffset
        }
    } catch (error) {
        Message.error('未知错误')
        console.error(error)
    }
}
let flag = false;

watch(()=> selfUser.status==1, () => {
    isClocking.value = selfUser.status == 1 ? true : false
    
    //防止开启多个定时器
    if (isClocking.value && !flag && !singleInterval.value) {
        clockingTime.value += Math.round(((new Date()).getTime() - (new Date(localStorage.getItem("beginTime") || "")).getTime())/1000)
        flag = true
        singleInterval.value = setInterval(() => {
            clockingTime.value++
        }, 1000)
    }
})

</script>

<style scoped lang="scss">
.go {
    width: 18em;
    height: 18em;
    min-width: 18em;
    min-height: 18em;
    position: relative;
    display: flex;
    cursor: pointer;

    .text {
        position: absolute;
        width: 100%;
        left: 50%;
        top: 50%;
        text-align: center;
        transform: translate(-50%, -50%);
        font-size: 36px;
        color: #336add;
        font-weight: bolder;

        .time {
            font-size: 16px;
        }
    }
}

/* .go::after {
  content: '';
  position: absolute;
  display: block;
  height: 50%;
  width: 50%;
} */

.L_ {
    position: absolute;
    width: 18em;
    height: 18em;
    border-radius: 50%;
    top: 0px;
}

.l_ {
    position: absolute;
    width: 100%;
    height: 100%;
    border-radius: 50%;
}

.l_:nth-of-type(1) {
    border-bottom: 6px solid #eba4d2;
    transform: rotatex(35deg) rotatey(-45deg);
    animation: one 3s linear infinite;
}

.l_:nth-of-type(2) {
    border-right: 6px solid #6aaff2;
    transform: rotatex(50deg) rotatey(10deg);
    animation: two 3s linear infinite;
}

.l_:nth-of-type(3) {
    border-top: 6px solid #cc54d0;
    transform: rotatex(35deg) rotatey(55deg);
    animation: three 3s linear infinite;
}

@keyframes one {
    to {
        transform: rotatex(35deg) rotatey(-45deg) rotatez(360deg);
    }
}

@keyframes two {
    to {
        transform: rotatex(50deg) rotatey(10deg) rotatez(360deg);
    }
}

@keyframes three {
    to {
        transform: rotatex(35deg) rotatey(55deg) rotatez(360deg);
    }
}
</style>
