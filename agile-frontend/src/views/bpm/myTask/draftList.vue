<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="90px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="流程标题" prop="title$VLK">
              <el-input
                v-model="query.title$VLK"
                placeholder="请输入流程标题"
              />
            </el-form-item>
            <el-form-item label="流程编码" prop="defKey$VLK">
              <el-input v-model="query.defKey$VLK" placeholder="请输入编码" />
            </el-form-item>
            <el-form-item>
              <el-button :icon="Search" type="primary" @click="search()">
                查询
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <!-- 关键字段设置minwith，特定枚举字段设置with（管理列、日期、状态、字典等），
         不重要字段放最后，管理列设置 fixed="right"
    -->
    <ab-table
      ref="abTable"
      v-model="selectedData"
      :checkable="false"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="bpmApi.myTask.bpmMyDraftTaskListUrl"
    >
      <ab-column label="流程标题" min-width="250" prop="title" />
      <ab-column label="流程编码" min-width="100" prop="defKey" />
      <ab-column
        ab-tag-type="statusCss"
        label="状态"
        prop="statusDesc"
        width="90"
      />
      <ab-column label="创建时间" prop="createTime" width="160" />
      <ab-column ab-template="edit" fixed="right" label="管理" width="160" />
      <template #edit="{ scope }">
        <router-link
          :to="{
            name: 'DraftStart',
            query: {
              instId: scope.row.id,
            },
          }"
        >
          <el-button text type="primary">编辑</el-button>
        </router-link>
        <el-button
          text
          type="primary"
          @click="
            sendAction(
              bpmApi.instance.bpmInstanceDeleteUrl + scope.row.id,
              `确定删除 ${scope.row.title} 吗?`
            )
          "
        >
          删除
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, onMounted, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
  } from '@element-plus/icons-vue'
  export default defineComponent({
    name: 'BpmMyDraftList',
    mixins: [abTableMix],

    setup() {
      const query = reactive({
        title$VLK: '',
        defKey$VLK: '',
      })
      return {
        query,
        bpmApi,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
