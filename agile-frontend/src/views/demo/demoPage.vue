<template>
  <DemoChild ref="childRef" :user="user" @cust-event="custEventFn" />
  <h1>amount :{{ amount }}</h1>
  <h2>无双向绑定number:{{ number }}</h2>
  <abTest />
  <chooseIconNew />
  <abUploadFile />
</template>

<script lang="ts" setup>
  import {
    reactive,
    onMounted,
    ref,
    computed,
    watch,
    getCurrentInstance,
  } from 'vue'

  import DemoChild from './demoPageChild.vue'
  //import {abTest,chooseIconNew,abUploadFile} from '@agilebpm/build/ab-core/ab-core.umd.min.js'
  const vueContext = getCurrentInstance()
  // 源码引入，
  // 库文件引入
  //import abBpm from '~/libs/ab-bpm/ab-bpm.umd.min.js'

  // reactive 返回对象的响应式副本
  const user = reactive({
    userName: '王小二',
    age: 11,
    phone: '111',
  })
  // 基本类型
  const count = ref(1)
  // 指定对象类型等用泛型, any 任意类型，多类型 <string | number>
  const price = ref<number>(1)
  // 计算类型的属性
  const amount = computed(() => {
    return price.value * count.value
  })

  // 定义类型 let 为可变类型 const不可变，类似 java final 修饰符
  const numberType = 100
  // 根据默认值自动推到类型
  let number = 100
  // 限定值仅能为 某种类型的几个值
  // let size: 100 | 200 | 300
  //size = 110 error
  const size = 100
  // 定义类型为两种或者任意类型 any
  let numbers: number | string

  // 定义function  ：定义入参类型
  const custEventFn = (step: number) => {
    // 封装类型必须通过value拿
    count.value = count.value + step
    // 没有ref 不会双向绑定，修改值也无法渲染
    number = number + step
  }

  // 拿到子组件的引用（setup时机还不能用，onMounted才可以用）  => void }| null
  const childRef = ref<{ childConst: string; countPlus: () => void } | null>(
    null
  )

  // 对 双向绑定的值进行watch 监控变化
  watch(
    () => amount.value,
    (amount, prevamount) => {
      console.info(`newValue ${amount}，oldValue ${prevamount}`)
    }
  )

  // mounted 时机会执行的动作,更多时机如：beforeMount，updated 等
  onMounted(() => {
    // 通过子组件的引用调用子组件内容
    console.info(childRef.value?.childConst)
    childRef.value?.countPlus()

    console.info(
      `可以初始化动作，此时已经能用 入参啦,如 ${vueContext} 又如： ${count.value}
      template中不会双向绑定 ${numberType}, ${number} ，${size} ,${numbers}
      `
    )
  })
</script>
