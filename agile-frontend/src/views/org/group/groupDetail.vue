<template>
  <div v-loading="info.loading">
    <ab-save
      v-if="!info.readonly"
      :after-save-fn="saveAfter"
      after-save-stay
      :back-btn="false"
      back-name="groupDetail"
      :before-save-fn="saveBefore"
      :form-ref="formRef"
      :save-data="info.group"
      :url="orgApi.group.saveOrgGroupUrl"
    />
    <el-button
      v-show="info.readonly"
      v-ab-btn-rights:groupManager_edit
      style="margin-left: 10px"
      @click="editOrg"
    >
      编辑
    </el-button>
    <el-button
      v-if="props.operationType !== 'add'"
      v-show="!info.readonly"
      style="margin-left: 10px"
      @click="cancel"
    >
      取消
    </el-button>
    <el-form
      ref="formRef"
      :label-width="100"
      :model="info.group"
      style="margin-top: 20px"
    >
      <el-form-item label="上级组织" prop="parentName">
        <el-input v-model="info.group.parentName" :disabled="true" />
      </el-form-item>
      <el-form-item
        label="组织名称"
        prop="name"
        :rules="[
          { required: true, message: '必填', trigger: 'blur' },
          { max: 50, message: '最多可输入50个字符' },
        ]"
      >
        <ab-pinyin
          v-model="info.group.name"
          v-model:to="info.group.code"
          clearable
          :disabled="info.readonly"
        />
      </el-form-item>
      <el-form-item
        label="组织编码"
        prop="code"
        :rules="[
          { required: true, message: '必填', trigger: 'blur' },
          { max: 50, message: '最多可输入50个字符' },
        ]"
      >
        <el-input
          v-model="info.group.code"
          :disabled="info.readonly || !!info.group.id"
        />
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number
          v-model="info.group.sn"
          clearable
          controls-position="right"
          :disabled="info.readonly"
          :max="100"
          :min="1"
          size="default"
        />
        <el-tooltip
          class="box-item"
          content="默认值1，数字越大同级组织排序越靠前"
          effect="light"
          placement="right-start"
        >
          <el-icon color="#409EFF" size="20" style="margin-left: 12px">
            <WarningFilled />
          </el-icon>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="组织类型">
        <abSelectTree
          v-model="info.group.type"
          desc="组织类别"
          :disabled="info.readonly"
          :has-root="false"
          :multiple="false"
          type-code="groupGrade"
        />
        <el-tooltip
          class="box-item"
          content="当对组织划分不同类型后可以非常方便的直接查找对应类型的上级岗位人员，如获取分公司的负责人"
          effect="light"
          placement="right-start"
        >
          <el-icon color="#409EFF" size="20" style="margin-left: 12px">
            <WarningFilled />
          </el-icon>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="组织岗位">
        <el-tag
          v-for="(relation, index) in info.group.orgRelationList"
          v-show="relation.type === 'groupRole'"
          :key="index"
          :closable="!info.readonly"
          :index="index"
          @close="removeRelation(index)"
        >
          {{ relation.roleName }}
        </el-tag>
        <ab-cust-dialog
          v-if="!info.readonly"
          dialog-key="roleSelector"
          :param="{}"
          size="small"
          style="margin-left: 3px"
          @ok="pushOrgRelationList"
        >
          选择
        </ab-cust-dialog>
        <el-tooltip class="box-item" effect="light" placement="right-start">
          <template #content>
            定义该组织下的岗位，岗位 = 组织 + 角色
            <br />
            当您需要指派工作给某个部门的某个岗位的时候，岗位会非常有用，
            <br />
            比如：部门负责人处理该部门的员工请假申请。
          </template>
          <el-icon color="#409EFF" size="20" style="margin-left: 12px">
            <WarningFilled />
          </el-icon>
        </el-tooltip>
      </el-form-item>
      <el-form-item
        label="描述"
        prop="desc"
        :rules="[{ max: 300, message: '最多可输入300个字符' }]"
      >
        <el-input
          v-model="info.group.desc"
          autosize
          clearable
          :disabled="info.readonly"
          type="textarea"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
  import {
    ChatDotRound,
    ChatSquare,
    WarningFilled,
  } from '@element-plus/icons-vue'
  import { ref } from 'vue'
  import { FormInstance } from 'element-plus'
  import {
    orgApi,
    sysApi,
    getData,
    postData,
    abUtil,
    abSelectTree,
  } from 'agilebpm'

  const { proxy } = getCurrentInstance() as any
  const name = 'groupDetail'
  const formRef = ref<FormInstance>()
  const props = defineProps({
    orgId: {
      type: String,
      required: true,
    },
    parentOrg: {
      type: Object,
      required: true,
    },
    isEdit: {
      type: Boolean,
      required: false,
    },
    operationType: {
      type: String,
      required: true,
    },
    readonlyTmp: {
      type: Boolean,
      required: false,
    },
  })

  const info: any = reactive({
    group: {},
    readonly: true,
    loading: false,
    isAdd: true,
  })

  const emit = defineEmits([
    'appendChildMsgEvent',
    'reloadTree',
    'editOrg',
    'cancel',
  ])

  const loadOrg = (orgId: string) => {
    info.loading = true
    getData(orgApi.group.getOrgGroupUrl + orgId).then((result) => {
      info.loading = false
      info.group = result.data
    })
  }

  const editOrg = () => {
    info.readonly = false
    emit('editOrg')
  }

  const cancel = () => {
    info.readonly = true
    emit('cancel')
  }

  /*
  删除组织上的角色
  删除前先检查关系信息
  * 前端使用 AbUtil.Arrays.del删除
  * 后端调用删除组织关系接口
  * */
  const removeRelation = (index: number) => {
    postData(orgApi.post.removeCheckGroupUrl, {
      roleId: info.group.orgRelationList[index].roleId,
      groupId: info.group.orgRelationList[index].groupId,
    }).then((result) => {
      if (result.isOk) {
        abUtil.Arrays.del(index, info.group.orgRelationList)
      } else {
        info.$Notice.error({
          title: '删除失败',
          desc: result.msg,
          duration: 5,
        })
      }
    })
  }

  const saveBefore = () => {}
  const saveAfter = (data: string) => {
    info.loading = false
    info.readonly = true
    info.group.id = info.group.id ? info.group.id : data
    emit('reloadTree')
    // proxy.$emit('getDataTree')
    // 如果是新增则继续新增
    emit(
      'appendChildMsgEvent',
      abUtil.clone(info.group),
      proxy.operationType === 'add'
    )
  }
  const pushOrgRelationList = (dataArr: []) => {
    if (!dataArr) return
    if (!info.group.orgRelationList) {
      info.group.orgRelationList = []
    }

    dataArr.forEach((item: any) => {
      //校验是否重复存在
      let i = 0,
        relation
      for (; (relation = info.group.orgRelationList[i++]); ) {
        if (relation.roleId === item.id) {
          return
        }
      }
      info.group.orgRelationList.push({
        type: 'groupRole',
        roleId: item.id,
        roleName: item.name,
        groupId: info.group.id || 'asdf',
      })
    })
  }

  watch(
    () => props.orgId,
    (orgId) => {
      if (props.operationType === 'add') {
        info.readonly = false
        if (orgId == '0') {
          info.group = {
            parentName: '',
            parentId: '0',
            sn: 1,
          }
        } else {
          info.group = {
            parentName: props.parentOrg.name,
            parentId: props.parentOrg.id,
            sn: 1,
          }
        }
        return
      }
      if (orgId) {
        loadOrg(orgId)
      }
    }
  )

  watch(
    () => props.operationType,
    (operationType, oldValue) => {
      //edit情况
      if (operationType === 'edit') {
        if (oldValue === 'add') {
          loadOrg(proxy.orgId)
        }
        info.readonly = false
      }
      //add情况
      else if (operationType === 'add') {
        info.readonly = false
        info.group = {
          parentName: proxy.parentOrg.name,
          parentId: proxy.parentOrg.id,
          sn: 1,
        }
      }
      // detail 情况，即点击树的时候
      else {
        loadOrg(proxy.orgId)
        info.readonly = true
      }
    }
  )

  watch(
    () => props.readonlyTmp,
    () => {
      info.readonly = !info.readonly
    }
  )
  onMounted(() => {
    if (!info.id) {
      info.id = 1
    }
    if (info.isEdit) {
      info.readonly = false
    }
  })
</script>
<style scoped>
  /deep/.el-input-number .el-input__inner {
    text-align: left;
  }
</style>
