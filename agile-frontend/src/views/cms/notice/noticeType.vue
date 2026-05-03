<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="50px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="名称" prop="typeName$VLK">
              <el-input v-model="query.typeName$VLK" placeholder="请输入名称" />
            </el-form-item>
            <el-form-item>
              <el-button icon="search" type="primary" @click="search()">
                查询
              </el-button>
              <el-button icon="refreshRight" @click="reset()">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/cms/notice/noticeTypeEdit">
            <el-button icon="Plus" type="primary">添加</el-button>
          </router-link>
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
      :url="cmsNotifyTypeListUrl"
    >
      <ab-column label="名称" min-width="120" prop="typeName" />
      <ab-column label="备注" min-width="120" prop="remark" />
      <ab-column ab-template="edit" fixed="right" label="操作" width="160" />
      <template #edit="{ scope }">
        <router-link :to="{ name: 'NoticeTypeEdit', params: scope.row }">
          <el-button type="text">编辑</el-button>
        </router-link>
        <el-button
          type="text"
          @click="
            sendAction(
              cmsNotifyTypeRemoveUrl + scope.row.id,
              `确定删除${scope.row.typeName}吗?`
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
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  import { abTableMix, cmsApi } from 'agilebpm'
  // import {
  //   cmsNotifyTypeListUrl,
  //   cmsNotifyTypeRemoveUrl,
  // } from '@/api/cms/ab-cms-notify'
  export default defineComponent({
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query = reactive({
        typeName$VLK: '',
      })
      // return {
      //   query,
      //   cmsNotifyTypeListUrl,
      //   cmsNotifyTypeRemoveUrl,
      // }
    },
  })
</script>
