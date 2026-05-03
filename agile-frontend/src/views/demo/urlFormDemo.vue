<template>
  <el-container style="padding: 20px">
    <el-main>
      <ab-load v-model="pageInfo.data" url="/ab-bpm/demo/applyOrder/get" />
      <el-form ref="formRef" label-suffix="：" :model="pageInfo.data">
        <el-form-item label="名字">
          <el-input v-model="pageInfo.data.qdjl" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="pageInfo.data.remark" type="textarea" />
        </el-form-item>
        <el-form-item label="当前页面URL">
          {{ url }}
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
  import { reactive, ref } from 'vue'
  import { ElMessage } from 'element-plus'
  const formRef = ref()
  const pageInfo: any = reactive({
    data: {
      qdjl: '',
      remark: '',
    },
  })

  const url = ref('')

  onMounted(() => {
    url.value = window.location.href

    // 【一】 向 BPM 页面 通知 我是存在事件交互的，如果不像父页面通知，父页面不会回调 获取数据校验表单
    window.parent.postMessage(
      { type: 'callback', name: 'subIframeCallback' },
      '*'
    )

    //【二】 监听主页面的获取数据的"getDataReady"事件，返回页面JSON，以及表单校验是否通过
    window.onmessage = (e) => {
      const data = e.data || {}
      if (data.type != 'getDataReady') {
        return
      }

      //【三】 自我校验当前表单，并返回结果
      let valid = true
      let errorMsg = ''
      if (!pageInfo.data.qdjl) {
        valid = false
        errorMsg = '名字不能为空'
        // 根据需要进行表单错误提示，点击流程图等非提交动作不需要校验
        if (data.isValid) {
          ElMessage({
            message:
              '业务表单校验不通过，名字不能为空！请根据需要进行表单错误提示。',
            type: 'warning',
          })
        }
      }

      //获取数据的逻辑，【必须复制】重要
      const formData = JSON.parse(JSON.stringify(pageInfo.data))

      // 发送数据报送事件
      window.parent.postMessage(
        {
          type: 'getData',
          url: document.location.href,
          valid: valid,
          errorMsg: errorMsg,
          data: formData,
        },
        data.url
      )
    }
  })
</script>
