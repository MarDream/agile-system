<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            :model="query"
            style="float: left"
            @submit.prevent
          >
            <el-form-item label="系统名称"   label-width="90px" prop="name$VLK">
              <el-input v-model="query.name$VLK" placeholder="请输入系统名称" />
            </el-form-item>
            <el-form-item label="状态"    label-width="62px" prop="enabled$VEQ">
              <el-select v-model="query.enabled$VEQ" placeholder="请选择状态">
                <el-option label="启用" value="1" />
                <el-option label="禁用" value="0" />
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
          <el-space wrap>
            <router-link
              :to="{
                name: 'ApplicationEdit',
                query: { type: '1' },
              }"
            >
              <el-button :icon="Plus" type="primary">添加</el-button>
            </router-link>
          </el-space>
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
      :url="sysApi.authApplication.applicationListJson"
    >
      <ab-column label="系统名称" min-width="120" prop="name" />
      <ab-column label="系统编码" min-width="120" prop="code" />
      <ab-column label="子系统地址" min-width="160" prop="url" />
      <ab-column
        ab-text-formatter="1-启用-success-dark|0-禁用-danger-dark"
        label="状态"
        prop="enabled"
        width="85"
      />
      <ab-column
        ab-text-formatter="1-是-default-light|0-否-success-light"
        label="是否默认"
        prop="isDefault"
        width="85"
      />
      <ab-column
        ab-text-formatter="1-启用-default-light|0-禁用-success-light"
        label="自动授权"
        prop="autoapprove"
        width="85"
      />
      <ab-column label="创建时间" prop="createTime" width="160 " />
      <ab-column ab-template="edit" fixed="right" label="操作" width="195" />
      <template #edit="{ scope }">
        <router-link
          :to="{
            name: 'ApplicationEdit',
            query: { id: scope.row.id },
          }"
        >
          <el-button text type="primary">详情</el-button>
        </router-link>
        <router-link
          :to="{
            name: 'ApplicationEdit',
            query: { id: scope.row.id, type: '1' },
          }"
        >
          <el-button text type="primary">编辑</el-button>
        </router-link>

        <el-button
          v-if="scope.row.id > 10"
          text
          type="primary"
          @click="
            sendAction(
              sysApi.authApplication.applicationRemove + scope.row.id,
              `确定删除${scope.row.name}吗?`
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
  import { reactive, defineComponent } from 'vue'
  import { abTableMix } from 'agilebpm'
  import { sysApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    RefreshLeft,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'ApplicationList',
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query = reactive({
        name$VLK: '',
        enabled$VEQ: '',
      })

      return {
        query,
        sysApi,
        Edit,
        Plus,
        Delete,
        Search,
        RefreshRight,
        RefreshLeft,
      }
    },
  })
</script>
