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
            <el-form-item label="常用语" label-width="76px" prop="locution$VLK">
              <el-input
                v-model="query.locution$VLK"
                placeholder="请输入常用语"
              />
            </el-form-item>
            <el-form-item label="状态" label-width="62px" prop="enable$VEQ">
              <el-select v-model="query.enable$NEQ" placeholder="请选择状态">
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
                name: 'DailyPhrasesEdit',
                query: { type: '1' },
              }"
            >
              <el-button :icon="Plus" type="primary">添加</el-button>
            </router-link>
            <el-button
              :disabled="!selectedData || selectedData.length == 0"
              :icon="Delete"
              type="danger"
              @click="
                delBySeletedIds(sysApi.sysDailyPhrases.dailyPhrasesRemove)
              "
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
      :url="sysApi.sysDailyPhrases.dailyPhrasesListJson"
    >
      <ab-column label="常用语" min-width="160" prop="locution" />
      <ab-column ab-template="changeEnable" label="是否启用" width="100" />
      <template #changeEnable="{ scope }">
        <el-switch
          v-model="scope.row.enable"
          :active-value="1"
          :inactive-value="0"
          inline-prompt
          @change="changeEnable(scope.row.id, scope.row.enable)"
        />
      </template>
      <ab-column
        ab-text-formatter="1-是-default-light|0-否-success-light"
        label="是否内置"
        prop="isDefault"
        width="120"
      />
      <ab-column label="创建人" prop="creator" width="160" />
      <ab-column label="创建时间" prop="createTime" width="200" />
      <ab-column label="修改时间" prop="updateTime" width="200" />
      <ab-column ab-template="edit" fixed="right" label="操作" width="195" />
      <template #edit="{ scope }">
        <router-link
          :to="{
            name: 'DailyPhrasesEdit',
            query: { id: scope.row.id },
          }"
        >
          <el-button text type="primary">详情</el-button>
        </router-link>
        <router-link
          :to="{
            name: 'DailyPhrasesEdit',
            query: { id: scope.row.id, type: '1' },
          }"
        >
          <el-button text type="primary">编辑</el-button>
        </router-link>
        <el-button
          v-if="scope.row.isDefault === 0"
          text
          type="primary"
          @click="
            sendAction(
              sysApi.sysDailyPhrases.dailyPhrasesRemove + scope.row.id,
              `确定删除${scope.row.locution}吗?`
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
  import { ElMessage } from 'element-plus'
  import { abTableMix } from 'agilebpm'
  import { sysApi, postData } from 'agilebpm'

  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    RefreshLeft,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'DailyPhrasesList',
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query = reactive({
        locution$VLK: '',
        enable$NEQ: '',
      })
      const loading = ref(false)

      const changeEnable = (id: string, enable: number) => {
        if (id)
          postData(sysApi.sysDailyPhrases.dailyPhrasesUpdateEnable, {
            id: id,
            enable: enable,
          })
            .then(({ msg }) => {
              ElMessage.success('操作成功')
              loading.value = true
            })
            .catch(() => (loading.value = true))
      }
      return {
        changeEnable,
        query,
        sysApi,
        Plus,
        Edit,
        Delete,
        Search,
        RefreshRight,
        RefreshLeft,
      }
    },
  })
</script>
