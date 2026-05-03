<template>
  <el-button @click="countPlus">{{ user.userName }} - 点击加价</el-button>
  <el-input-number v-model="num" :max="10" :min="1" @change="num++" />
</template>
<script lang="ts" setup>
  import { ref, onMounted } from 'vue'
  import { abTools } from 'agilebpm'

  const num = ref(2)
  // 所有组件的入参定义
  const props = defineProps({
    // 用户默认会定义为Object any ，如果指定类型则： type: Object as PropType<User>,
    user: { required: true, type: Object },
    // 步长
    stepLenth: { required: false, default: 1, type: Number },
  })

  // emit 事件，父类根据事件对自身进行更新
  const emit = defineEmits(['custEvent'])
  //自身引用类似 之前的this，比如获取路由等信息
  const { proxy } = abTools.useCurrentInstance()
  console.info(proxy)

  // mounted 时机
  onMounted(() => {
    // 外部用不了的
    console.info(props.user.userName)
  })

  // function
  const countPlus = () => {
    // 让父组件不畅
    emit('custEvent', num.value)
  }

  const childConst = ref('我是子子组件内的一些 function 对象 等')
  // 将外部允许调用的方法
  defineExpose({ countPlus, childConst })
</script>
