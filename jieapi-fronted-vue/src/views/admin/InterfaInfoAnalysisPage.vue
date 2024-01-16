<script setup lang="ts">
import * as echarts from 'echarts';
import {onMounted} from "vue";
import {ElMessage} from "element-plus";
import {listTopInvokeInterfaceInfo} from "../../api/interfaceInfo.ts";

const getTopData = async () => {
  const res = await listTopInvokeInterfaceInfo()
  if (res.code === 0) {
    return res.data.map((item) => {
      return {
        value: item.totalCount,
        name: item.name,
      };
    })
  } else {
    ElMessage.error(res.message)
  }
}

const drawChart = async () => {
  // 基于准备好的dom，初始化echarts实例
  const myChart = echarts.init(document.getElementById('EChart'));

  const chartData = await getTopData()
  const option = {
    title: {
      text: '调用次数最多的接口TOP3',
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '50%',
        data: chartData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  };
// 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
}

// 在Mounted钩子函数中，调用drawChart方法
onMounted(() => {
  drawChart()
})

</script>

<template>

  <div id="EChart" style="width: 800px; height: 500px;"></div>
</template>

<style scoped>

</style>