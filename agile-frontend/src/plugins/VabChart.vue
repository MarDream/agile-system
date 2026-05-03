<template>
  <div ref="chartRef" class="vab-chart"></div>
</template>

<script setup lang="ts">
  import * as echarts from 'echarts'
  import {
    onBeforeUnmount,
    onMounted,
    ref,
    shallowRef,
    watch,
  } from 'vue'

  const props = defineProps({
    option: {
      type: Object,
      required: true,
    },
    initOptions: {
      type: Object,
      default: () => ({}),
    },
    theme: {
      type: String,
      default: '',
    },
  })

  const chartRef = ref<HTMLElement | null>(null)
  const chartInstance = shallowRef<echarts.ECharts | null>(null)

  const resizeChart = () => {
    chartInstance.value?.resize()
  }

  const renderChart = () => {
    if (!chartRef.value) {
      return
    }

    if (!chartInstance.value) {
      chartInstance.value = echarts.init(
        chartRef.value,
        props.theme || undefined,
        props.initOptions || undefined
      )
    }

    chartInstance.value.setOption(props.option, true)
  }

  onMounted(() => {
    renderChart()
    window.addEventListener('resize', resizeChart)
  })

  watch(
    () => props.option,
    () => {
      renderChart()
    },
    { deep: true }
  )

  watch(
    () => props.theme,
    () => {
      if (!chartRef.value) {
        return
      }
      chartInstance.value?.dispose()
      chartInstance.value = null
      renderChart()
    }
  )

  onBeforeUnmount(() => {
    window.removeEventListener('resize', resizeChart)
    chartInstance.value?.dispose()
    chartInstance.value = null
  })
</script>

<style scoped>
  .vab-chart {
    width: 100%;
    height: 100%;
  }
</style>
