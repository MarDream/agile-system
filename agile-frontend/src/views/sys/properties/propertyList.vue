<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VIN"
          type-code="property"
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
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item
                    label="参数名"
                    label-width="76px"
                    prop="name$VLK"
                  >
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="请输入参数名"
                    />
                  </el-form-item>
                  <el-form-item label="编码" label-width="62px" prop="code$VLK">
                    <el-input
                      v-model="query.code$VLK"
                      placeholder="请输入编码"
                    />
                  </el-form-item>
                  <el-form-item
                    v-if="collapse"
                    label="环境"
                    label-width="62px"
                    prop="environment$VLK"
                  >
                    <ab-select-tree
                      v-model="query.environment$VLK"
                      :has-root="false"
                      :operation="false"
                      style="width: 160px"
                      type-code="environment"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      查询
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      重置
                    </el-button>
                    <el-button type="text" @click="handleCollapse()">
                      <span v-if="!collapse" type="text">展开</span>
                      <span v-else type="text">合并</span>
                      <el-icon class="el-icon--right">
                        <ArrowUp v-if="collapse" />
                        <ArrowDown v-else />
                      </el-icon>
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-space>
                  <router-link
                    :to="{
                      name: 'PropertyEdit',
                      query: { typeCode: query.typeCode$VEQ },
                    }"
                  >
                    <el-button :icon="Plus" type="primary">添加</el-button>
                  </router-link>
                  <el-button
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Delete"
                    type="danger"
                    @click="delBySeletedIds(sysApi.property.propertyRemove)"
                  >
                    批量删除
                  </el-button>
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
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="sysApi.property.propertyList"
          >
            <ab-column label="参数名" min-width="80" prop="name" />
            <ab-column label="编码" min-width="80" prop="code" />
            <ab-column label="描述" min-width="120" prop="desc" />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="更新时间"
              prop="updateTime"
              width="160"
            />
            <ab-column label="更新人" width="120" prop="updater" />
            <ab-column
              ab-template="environment"
              label="环境"
              prop="environmentName"
              width="100"
            />
            <template #environment="{ scope }">
              <el-tag class="mx-1" :type="envCss(scope.row.environment)">
                {{ scope.row.environmentName }}
              </el-tag>
            </template>
            <ab-column
              ab-text-formatter="1-是-success-dark|0-否-danger-dark"
              label="是否加密"
              prop="encrypt"
              width="90"
            />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="操作"
              width="140"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{ name: 'PropertyEdit', query: { id: scope.row.id } }"
              >
                <el-button text type="primary">编辑</el-button>
              </router-link>
              <el-button
                text
                type="primary"
                @click="
                  sendAction(
                    sysApi.property.propertyRemove + scope.row.id,
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
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, abDictTree, sysApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
    ArrowUp,
    ArrowDown,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'PropertyList',
    components: { ArrowUp, ArrowDown, abDictTree },
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query: any = reactive({
        name$VLK: '',
        code$VLK: '',
        environment$VLK: '',
        typeCode$VIN: '',
      })

      const envCss = (env: string) => {
        if (env === 'DEV') return 'primary'
        if (env === 'SIT') return 'success'
        if (env === 'GRAY') return 'warning'
        if (env === ' PROD') return 'danger'
        return 'info'
      }

      return {
        query,
        sysApi,
        abDictTree,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
        InfoFilled,
        ArrowUp,
        ArrowDown,
        envCss,
      }
    },
  })
</script>
