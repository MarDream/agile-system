<template>
  <div
    v-if="pageInfo.data"
    class="previewBox"
    :class="
      pageInfo.data.designJson.theme == '2'
        ? 'gatewayThemeClass'
        : pageInfo.data.designJson.theme == '3'
        ? 'largeScreenThemeClass'
        : pageInfo.data.designJson.theme == '4'
        ? 'redThemeClass'
        : 'formThemeClass'
    "
    style="position: relative"
    :style="setDashboardBg()"
  >
    <!-- <div style="position: absolute; top: 0; right: 2px; z-index: 99">
      <span style="cursor: pointer" text type="default" @click="back()">
        <el-icon :size="20"><Close /></el-icon>
      </span>
    </div> -->
    <FormCombinationPreview
      v-if="pageInfo.data"
      :design-code="pageInfo.data.code"
      :design-json="pageInfo.data.designJson"
      :params="pageInfo.data.varibales"
    />
  </div>
</template>
<script lang="ts" setup>
  import { abFormCombinationPreview as FormCombinationPreview } from 'abLayoutDesigner'
  import { bizApi, abUtil, abTools, sysApi } from 'agilebpm'
  import { Close } from '@element-plus/icons-vue'

  const { proxy } = abTools.useCurrentInstance()
  /* const { changeTabsMeta } = useTabsStore()

  if (proxy.$route.params.code) {
    bizApi.formCombination
      .getByCode(proxy.$route.params.code)
      .then((rel: any) => {
        changeTabsMeta({
          fullPath: proxy.$route.fullPath,
          meta: { title: `${rel.data.name}` },
        })
      })
  } */

  const pageInfo = reactive({
    data: null as any,
    isLoad: false,
    fullPath: '',
  })

  // changeTabsMeta({
  //   fullPath: proxy.$route.fullPath,
  //   meta: { title: `实例详` },
  // })

  const loadData = () => {
    const code = proxy.$route.params.code

    const params = {} as any
    abUtil.easyClone(proxy.$route.params, params)
    abUtil.easyClone(proxy.$route.query, params)
    if (code) {
      bizApi.formCombination.getDesignJson(code, params).then((result) => {
        pageInfo.data = result.data
      })
    }
  }

  loadData()
  // 缓存唤醒，或者第一次进入时，加载页面
  // onActivated(() => {
  //   if (pageInfo.fullPath != proxy.$route.fullPath) {
  //     pageInfo.fullPath = proxy.$route.fullPath
  //     loadData()
  //   }
  // })

  const back = () => {
    abTools.closeTab(proxy.$route.path)
    proxy.$router ? proxy.$router.back() : window.history.back()
  }

  const setDashboardBg = () => {
    const designJson = pageInfo.data.designJson
    let backgroundStyle = {}
    // 如果是图片背景
    if (designJson.dashboardBg == 'picture') {
      let url = ''
      if (designJson.dashboardBgPicture.length > 0) {
        const dashboardBgPicture = JSON.parse(designJson.dashboardBgPicture)
        if (
          dashboardBgPicture[0] &&
          dashboardBgPicture[0].id &&
          dashboardBgPicture[0].id.length > 0
        ) {
          url = sysApi.sysFile.getViewFileUrl(dashboardBgPicture[0].id)
        }
      } else {
        url = ''
      }
      if (url.length > 0) {
        backgroundStyle = {
          backgroundImage: `url("${url}")`,
          backgroundRepeat: ' no-repeat' /* 设置背景图片不重复 */,
          backgroundSize: 'cover' /* 设置背景图片自适应容器大小 */,
          backgroundPosition: 'center' /* 设置背景图片在容器中居中显示 */,
          opacity: designJson.dashboardTransparency,
        }
      } else {
        // backgroundStyle = {
        //   background: `#fff`,
        // }
      }
    }
    // 如果是颜色背景
    if (designJson.dashboardBg && designJson.dashboardBg == 'color') {
      if (
        designJson.dashboardBgColor &&
        designJson.dashboardBgColor.length > 0
      ) {
        backgroundStyle = {
          background: designJson.dashboardBgColor,
        }
      } else {
        // backgroundStyle = {
        //   background: `#fff`,
        // }
      }
    }
    return backgroundStyle
  }
</script>
<style lang="scss" scoped></style>
