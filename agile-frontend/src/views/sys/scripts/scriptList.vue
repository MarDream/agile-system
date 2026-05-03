<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VIN"
          type-code="script"
          @node-click="searchResetPage()"
        />
      </el-aside>
      <el-main>
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
                  <el-form-item label="名称" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="请输入名称"
                    />
                  </el-form-item>
                  <el-form-item label="分类" prop="typeCode$VEQ">
                    <ab-select-tree
                      v-model="query.typeCode$VEQ"
                      :operation="false"
                      type-code="script"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      查询
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      重置
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-space>
                  <router-link
                    :to="{
                      name: 'ScriptEdit',
                      query: { typeCode: query.typeCode$VEQ },
                    }"
                  >
                    <el-button icon="Plus" type="primary">添加</el-button>
                  </router-link>
                  <el-button
                    v-ab-btn-rights:script_del
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Delete"
                    type="danger"
                    @click="delBySeletedIds(sysApi.script.scriptRemove)"
                  >
                    批量删除
                  </el-button>
                </el-space>
              </el-col>
            </el-row>
          </div>

          <ab-table
            ref="abTable"
            v-model="selectedData"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="sysApi.script.scriptList"
          >
            <ab-column label="名称" min-width="80" prop="name" />
            <ab-column label="备注" min-width="280" prop="desc" />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="更新时间"
              width="160"
              prop="updateTime"
            />
            <ab-column label="更新人" width="160" prop="updater" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="操作"
              prop="key"
              width="140"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{ name: 'ScriptEdit', query: { id: scope.row.id } }"
              >
                <el-button text type="primary">编辑</el-button>
              </router-link>
              <el-button
                text
                type="primary"
                @click="
                  sendAction(
                    sysApi.script.scriptRemove + scope.row.id,
                    `确定删除${scope.row.name}吗?`
                  )
                "
              >
                删除
              </el-button>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts">
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, abDictTree, sysApi } from 'agilebpm'
  import {
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
    Delete,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'ScriptList',
    components: { abDictTree },
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query: any = reactive({
        name$VLK: '',
        typeCode$VEQ: '',
      })
      return {
        abDictTree,
        query,
        sysApi,
        Search,
        Plus,
        RefreshRight,
        InfoFilled,
        Delete,
      }
    },
  })
</script>
