<template>
  <div id="chart" ref="chartRef"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import { clockLineChartOption } from '../configs'
const props = defineProps(['weekClock'])
const {weekClock} = props;
let echart: echarts.ECharts;
watch(weekClock,()=>{
    if(weekClock.length===0) return;
    const data = []
    for(let i of weekClock){
        data.push(i)
    }
    dataList.value.push(data)
    console.log(dataList);
    updateChart()
})

function updateChart() {  
    echart = echarts.init(chartRef.value)
  // TODO: 获取每日打卡数据 dataList
  // 将数据整合进配置项
  clockLineChartOption.series = clockLineChartOption.series.map(
    (config, index) => {
      return {
        ...config,
        data: dataList.value[index] ?? []
      }
    }
  )
  echart.setOption(clockLineChartOption)
  window.addEventListener('resize', () => {
    echart.resize()
  })  
} 
const chartRef = ref()
const dataList: Ref<Array<Array<number>>> = ref([
])


</script>

<style scoped lang="scss">
#chart {
  flex: 1;
  height: 300px;
}
</style>
