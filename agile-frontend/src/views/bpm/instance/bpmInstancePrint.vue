<template>
  <div class="printBox">
    <el-container>
      <el-header class="noprint" style="margin-top: 20px">
        <el-space>
          <el-button v-print="print" type="primary" @click="print">
            打印
          </el-button>
          <el-button type="primary" @click="closeWindow">取消</el-button>
          <el-checkbox
            v-model="info.showApproveHistory"
            label="打印审批历史"
            size="large"
          />
        </el-space>
      </el-header>

      <el-divider class="dividermar" />
      <div id="printTest" ref="printTest" class="print">
        <el-main v-if="info.bpmData.bpmForm && info.bpmData.bpmForm.type">
          <ab-url-form
            v-if="info.bpmData.bpmForm.type == 'url'"
            ref="url"
            :bpm-form="info.bpmData.bpmForm"
          />
          <cust-form v-else ref="inner" :form-data="info.formData" />
        </el-main>
        <el-footer>
          <div v-if="info.showApproveHistory && info.bpmData.bpmInstance">
            <h4>审批历史</h4>
            <approve-history-table
              :instance-id="historyParam.instId"
              :opinion-id="historyParam.opId"
              :task-id="historyParam.taskId"
            />
          </div>
        </el-footer>
      </div>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'
import {
    abForm as custForm,
    bpmButtons,
    abUrlForm,
    bpmTools,
    abTools,
    bpmApi,
    ApproveHistoryTable,
  } from 'agilebpm'

  import { getCurrentInstance, Ref } from 'vue'
  const { changeTabsMeta } = useAbStoreAdapter()

  const spacer = h(ElDivider, { direction: 'vertical' })
  const vueContext = getCurrentInstance()

  const info: any = reactive({
    bpmData: {} as any,
    formData: {} as any,
    showApproveHistory: false,
    instanceId: '',
    nodeKey: '',
  })

  const historyParam = computed(() => {
    const resultParam = {
      instId: info.bpmData.bpmInstance?.id,
      taskId: info.bpmData.bpmTask?.id,
    }
    const newParam = getParam()

    if (!newParam || !newParam.type) {
      return resultParam
    }
    if (type == 'opId') {
      resultParam.opId = newParam.id
    } else if (type == 'taskId') {
      resultParam.taskId = newParam.id
    }
    return resultParam
  })
  //这里是打印的配置项
  // const print = ref({
  //   id: 'printTest', //这里的id就是上面我们的打印区域id，实现指哪打哪
  //   popTitle: '打印', // 打印配置页上方的标题
  //   extraHead:
  //     '<meta http-equiv="Content-Language"content="zh-cn"/>,<style>  #printId {  100%;  } <style>', // 最上方的头部文字，附加在head标签上的额外标签，使用逗号分割
  //   preview: false, // 是否启动预览模式，默认是false
  //   previewTitle: '预览的标题', // 打印预览的标题
  //   previewPrintBtnLabel: '预览结束，开始打印', // 打印预览的标题下方的按钮文本，点击可进入打印
  //   zIndex: 20002, // 预览窗口的z-index，默认是20002，最好比默认值更高
  //   previewBeforeOpenCallback() {
  //     console.log('正在加载预览窗口！')
  //   }, // 预览窗口打开之前的callback
  //   previewOpenCallback() {
  //     console.log('已经加载完预览窗口，预览打开了！')
  //   }, // 预览窗口打开时的callback
  //   beforeOpenCallback() {
  //     console.log('开始打印之前！')
  //   }, // 开始打印之前的callback
  //   openCallback() {
  //     console.log('执行打印了！')
  //   }, // 调用打印时的callback
  //   closeCallback() {
  //     console.log('关闭了打印工具！')
  //   }, // 关闭打印的callback(无法区分确认or取消)
  //   clickMounted() {
  //     console.log('点击v-print绑定的按钮了！')
  //   },
  // })
  const { proxy } = abTools.useCurrentInstance()

  const instId = vueContext?.proxy?.$route.query.instId
  const opId = vueContext?.proxy?.$route.query.opId
  const bizId = vueContext?.proxy?.$route.query.bizId
  const nodeKey = vueContext?.proxy?.$route.query.nodeKey
  const carbonId = vueContext?.proxy?.$route.query.carbonId
  const isStart = !(vueContext?.proxy?.$route.query.isStart == '0')
  //id 就是taskId
  const taskId = vueContext?.proxy?.$route.query.id

  //使用新的方式获取打印表单
  const id = vueContext?.proxy?.$route.query.id
  const type = vueContext?.proxy?.$route.query.type

  const { delVisitedRoute } = useTabsStore()
  const closeWindow = () => {
    const route = proxy.$route
    delVisitedRoute(route.path)
    proxy.$router ? proxy.$router.back() : window.history.back()
  }
  const print = () => {
    window.print()
  }

  const getParam = () => {
    if (id && type) {
      return { id: id, type: type }
    }

    if (carbonId) {
      return { id: carbonId, type: 'carbonId' }
    }
    if (bizId) {
      return { id: bizId, type: 'bizId' }
    }
    if (opId) {
      return { id: 'opId', type: 'opId' }
    }
    if (instId) {
      let typeTemp = 'instId'
      if (isStart) {
        typeTemp = 'instIdStart'
      }
      return { id: 'instId', type: typeTemp }
    }
  }

  const initData = () => {
    const param = getParam()
    bpmApi.bpmData
      .getInstDataNew(param.id, param.type, true)
      .then(({ data }) => {
        bpmTools.initBpmDataAndFormData(
          data,
          vueContext,
          info.bpmData,
          info.formData
        )
        info.bpmData.actionData.instanceId = data.bpmInstance.id

        changeTabsMeta({
          fullPath: proxy.$route.fullPath,
          meta: { title: `实例详情-${data.bpmInstance.title}` },
        })
      })
  }
  initData()
</script>

<style lang="scss" scoped>
  @media print {
    .noprint {
      display: none;
    }
  }
  .printBox {
    height: 100vh;
    overflow: auto;
    background: #fff;
  }
</style>
