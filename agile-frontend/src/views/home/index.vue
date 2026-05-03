<template>
  <div v-if="pageLoading">
    <el-main>
      <el-card shadow="never" style="margin-top: 15px">
        <el-skeleton></el-skeleton>
      </el-card>
      <el-card shadow="never">
        <el-timeline>
			<el-timeline-item timestamp="2024/7" placement="top">
            <el-card shadow="hover">
              <h1>v2.6</h1><br/>
			  <h3>流程新增功能:</h3>
              <p>无候选人策略</p>
			  <p>是否必填意见</p>
              <p>驳回后流转方式</p>
              <p>驳回后执行人设定</p>
              <p>条件分支可配置化</p>
              <p>转办跳过</p>
              <p>流程干预：超管跳转</p>
			  <p>流程干预：超管重启</p>
              <p>流程干预：超管终止</p>
			  <p>流程干预：指派</p>
			  <p>流程支持导入导出</p>
              <p>流程功能诸多优化</p>

              <h3>表单设计器新增功能:</h3>
			  <p>新增辅助控件：标题组件、标题、分割线、提示、外部Iframe组件</p>
              <p>新增常用控件：评分、开关、等</p>
              <p>新增高级控件：签字、行政区域选择、数据字段控件</p>
			  <p>新增新增十几种函数计算插件</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2023/7" placement="top">
            <el-card shadow="hover">
              <h1>v2.5</h1><br/>
              <p>
                个人办公：
                待办事项、抄送传阅、办理历史、发起申请、申请历史、我的草稿
              </p>
              <p>内容管理： 公告、新闻</p>
              <p>
                组织管理：组织管理、用户管理、角色管理、
                岗位管理。笔者十多年研发见过无数组织架构，它堪称最精简最完美的设计。
              </p>
              <p>
                流程管理：
                表单设计、流程设计、流程实例管理、任务管理、系统对话框管理。
              </p>
              <p>
                系统管理：字典分类管理、异常日志、系统资源、系统属性、常用脚本、短信邮件通知
              </p>
            </el-card>
          </el-timeline-item>
           
        </el-timeline>
      </el-card>
    </el-main>
  </div>
  <work></work>
</template>

<script lang="ts" setup>
  import { defineAsyncComponent, ref } from 'vue'
  const dashboard = ref<string>('0')
  const pageLoading = ref<boolean>(true)
  const work = defineAsyncComponent(() => import('./work/index.vue'))
</script>

<style></style>
