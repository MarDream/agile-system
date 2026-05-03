<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="62px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="标题" prop="title$VLK">
              <el-input v-model="query.title$VLK" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item label="状态" prop="status$VEQ">
              <el-select v-model="query.status$VEQ" placeholder="请选择">
                <el-option label="未发布" :value="0" />
                <el-option label="发布" :value="1" />
                <el-option label="下架" :value="2" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button :icon="Search" type="primary" @click="search()">
                查询
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/cms/news/newsEdit">
            <el-button :icon="Plus" type="primary">添加</el-button>
          </router-link>
          <el-button
            :disabled="!selectedData || selectedData.length == 0"
            :icon="Delete"
            style="margin-left: 12px"
            type="danger"
            @click="delBySeletedIds(cmsApi.news.removeNewsUrl)"
          >
            批量删除
          </el-button>
        </el-col>
      </el-row>
    </div>
    <!-- 关键字段设置minwith，特定枚举字段设置with（管理列、日期、状态、字典等），
         不重要字段放最后，管理列设置 fixed="right"
    -->
    <ab-table
      ref="abTable"
      v-model="selectedData"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="cmsApi.news.cmsNewsListUrl"
    >
      <ab-column label="标题" min-width="160" prop="title" />
      <ab-column
        ab-text-formatter="0-未发布-default-light|1-发布-success-light|2-下架-danger-light"
        align="left"
        label="状态"
        prop="status"
        width="90"
      />
      <ab-column label="操作人" prop="releaseName" width="140" />
      <ab-column label="发布时间" prop="releaseTime" width="180" />
      <ab-column label="访问量" prop="visitNum" width="90" />
      <ab-column label="评论量" prop="commentsNum" width="90" />
      <ab-column ab-template="edit" fixed="right" label="操作" width="320" />
      <template #edit="{ scope }">
        <router-link
          v-if="
            scope.row.status === 1 ||
            scope.row.status === 0 ||
            scope.row.status === 2
          "
          :to="{
            name: 'NewsDetails',
            query: { id: scope.row.id, form: true, type: 'NewsList' },
          }"
        >
          <el-button text type="primary">详情</el-button>
        </router-link>
        <el-button
          v-if="scope.row.status === 1"
          text
          type="primary"
          @click="
            sendAction(
              cmsApi.news.withdrawNewsUrl + scope.row.id,
              `确定下架${scope.row.title}吗?`
            )
          "
        >
          下架
        </el-button>
        <router-link
          v-if="scope.row.status === 0 || scope.row.status === 2"
          :to="{ name: 'NewsEdit', query: { id: scope.row.id } }"
        >
          <el-button text type="primary">编辑</el-button>
        </router-link>
        <el-button
          v-if="scope.row.status === 0 || scope.row.status === 2"
          text
          type="primary"
          @click="
            sendAction(
              cmsApi.news.removeNewsUrl + scope.row.id,
              `确定删除${scope.row.title}吗?`
            )
          "
        >
          删除
        </el-button>
        <el-button
          v-if="scope.row.status === 0 || scope.row.status === 2"
          text
          type="primary"
          @click="
            sendAction(
              cmsApi.news.releaseNewsUrl + scope.row.id,
              `确定发布${scope.row.title}吗?`
            )
          "
        >
          发布
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, cmsApi } from 'agilebpm'
  import {
    Delete, 
    Edit,
    Search, 
    Plus,
    RefreshRight,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'NewsList',
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query = reactive({
        title$VLK: '',
        status$VEQ: null,
      })
      return {
        query,
        cmsApi,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
