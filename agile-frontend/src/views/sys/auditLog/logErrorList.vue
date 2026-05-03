<template>
  <div class="comprehensive-table-container">
    <div>
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
              <el-form-item label="请求地址" label-width="90px" prop="url$VLK">
                <el-input
                  v-model="query.url$VLK"
                  placeholder="请输入请求地址"
                />
              </el-form-item>
              <el-form-item label="状态" label-width="62px" prop="status$VEQ">
                <el-select v-model="query.status$VEQ" placeholder="请选择状态">
                  <el-option label="未检查" value="unchecked" />
                  <el-option label="已检查" value="checked" />
                  <el-option label="已修复" value="fixed" />
                </el-select>
              </el-form-item>
              <el-form-item
                label="异常描述"
                label-width="90px"
                prop="content$VLK"
              >
                <el-input
                  v-model="query.content$VLK"
                  placeholder="请输入异常描述"
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
            <el-space wrap>
              <el-button
                v-ab-btn-rights:logError_del
                :disabled="!selectedData || selectedData.length == 0"
                :icon="Delete"
                type="danger"
                @click="delBySeletedIds(sysApi.sysAuditlog.logErrorRemove)"
              >
                批量删除
              </el-button>
              <el-button
                v-ab-btn-rights:logError_config
                :icon="Comment"
                type="primary"
                @click="openDialog()"
              >
                消息推送配置
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
        :url="sysApi.sysAuditlog.logErrorList"
      >
        <ab-column label="帐号" prop="account" width="80" />
        <ab-column label="IP地址" prop="ip" width="140" />
        <ab-column ab-template="param" label="IP归属" width="80" />
        <template #param="{ scope }">
          {{ scope.row.ipAddress ? scope.row.ipAddress : '-' }}
        </template>
        <ab-column
          ab-text-formatter="unchecked-未检查-danger-dark|checked-已检查-warning-dark|fixed-已修复-success-dark"
          label="状态"
          prop="status"
          width="85"
        />
        <ab-column label="请求地址" min-width="120" prop="url" />
        <ab-column label="出错信息" min-width="120" prop="content" />
        <ab-column
          ab-date-formatter="yyyy-MM-dd HH:mm"
          label="出错时间"
          prop="createTime"
          width="160"
        />
        <ab-column ab-template="edit" fixed="right" label="操作" width="85" />
        <template #edit="{ scope }">
          <router-link
            v-ab-btn-rights:logError_edit
            :to="{
              name: 'LogErrorEdit',
              query: { id: scope.row.id },
            }"
          >
            <el-button text type="primary">编辑</el-button>
          </router-link>
        </template>
      </ab-table>
    </div>
    <el-dialog
      v-model="pageData.showDialog"
      :append-to-body="true"
      :destroy-on-close="true"
      title="消息推送配置"
      @open="openDialog()"
    >
      <el-form
        ref="formRef"
        label-position="top"
        label-suffix="："
        label-width="120px"
        :model="pageData.msgConfObj.confJson"
        :status-icon="false"
        width="30%"
      >
        <el-form-item label="是否启用">
          <el-switch
            v-model="pageData.msgConfObj.isEnable"
            active-text="是"
            :active-value="1"
            inactive-text="否"
            :inactive-value="0"
            inline-prompt
          />
        </el-form-item>
        <el-form-item label="人员配置">
          <el-space>
            <el-radio-group
              v-model="pageData.identityType"
              @change="dialogChange()"
            >
              <el-radio-button label="user">用户</el-radio-button>
              <el-radio-button label="org">组织</el-radio-button>
              <el-radio-button label="role">角色</el-radio-button>
              <el-radio-button label="post">岗位</el-radio-button>
            </el-radio-group>
            <ab-cust-dialog
              :dialog-key="pageData.dialogKey"
              style="margin-left: 10px"
              type="primary"
              @ok="setreceivers"
            >
              {{ pageData.dialogButtonDesc }}
            </ab-cust-dialog>
          </el-space>
          <div style="width: 100%; margin-top: 10px">
            <el-tag
              v-if="pageData.msgConfObj.confJson.receivers.length === 0"
              type="warning"
            >
              未选择
            </el-tag>
            <el-tag
              v-for="(identity, index) in pageData.msgConfObj.confJson
                .receivers"
              :key="index"
              closable
              type="success"
              @close="deletereceivers(index)"
            >
              {{ identity.assign }}
            </el-tag>
          </div>
        </el-form-item>

        <el-form-item
          label="消息类型"
          prop="msgType"
          :rules="{
            required: true,
            message: '至少选择一个消息类型',
            trigger: 'change',
          }"
        >
          <ab-msg-type v-model="pageData.msgConfObj.confJson.msgType" />
        </el-form-item>

        <el-form-item
          label="消息内容"
          prop="msgContent"
          :rules="{
            required: true,
            message: '请输入消息体',
            trigger: 'change',
          }"
        >
          <div style="width: 100%; margin-bottom: 10px">
            <el-tag
              v-for="(item, index) in msgTag"
              :key="index"
              @click="addParam(item)"
            >
              {{ item.desc }}
            </el-tag>
          </div>
          <!-- <el-input
            v-model="pageData.msgConfObj.confJson.msgContent"
            :rows="5"
            type="textarea"
          /> -->
          <ab-rich-editor v-model="pageData.msgConfObj.confJson.msgContent" />
        </el-form-item>
        <el-form-item label="示例">
          <!-- <el-input v-model="sampleData" readonly :rows="5" type="textarea" /> -->
          <ab-rich-editor v-model="sampleData" :disabled="true" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog()">取消</el-button>
          <el-button type="primary" @click="saveData()">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import { abTableMix, sysApi, abMsgType, abRichEditor } from 'agilebpm'
  export default {
    name: 'LogErrorList',
    mixins: [abTableMix, abMsgType, abRichEditor],
  }
</script>

<script setup lang="ts">
  import {
    Delete,
    Search,
    Comment,
    RefreshRight,
  } from '@element-plus/icons-vue'

  // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
  const query = reactive({
    url$VLK: '',
    status$VEQ: '',
    content$VLK: '',
  })

  const pageData = reactive({
    showDialog: false,
    identityType: 'user',
    dialogKey: 'userSelector',
    dialogButtonDesc: '请选择用户',
    identityTypeDesc: '用户',
    msgContentSampleData: '',
    msgConfObj: {
      id: null as any,
      confType: 'sysLogErrorMsgConf',
      isEnable: 1,
      confJson: {
        receivers: [] as any,
        msgType: '',
        enable: 1,
        msgContent:
          '<p>用户：${fullName}（${account}）,系统操作异常。</p><p><br></p><p>异常信息为：${content}</p><p><br></p><p>出现时间：${createTime}</p><p><br></p><p>ip归属地：${ipAddress}（${ip}）</p><p><br></p><p>请关注！</p><p><br></p><p>${path}</p>',
      },
    },
  })

  const msgTag = [
    { value: '${fullName}', desc: '用户名', default: '管理员' },
    { value: '${account}', desc: '账号', default: 'admin' },
    {
      value: '${content}',
      desc: '异常信息',
      default: 'NullPointerException: null',
    },
    {
      value: '${path}',
      desc: '请求地址',
      default: 'http://localhost:8080/ab-bpm/bpm/instance/doAction',
    },
    {
      value: '${createTime}',
      desc: '出现时间',
      default: '2023-06-21 14:51:39',
    },
    { value: '${ip}', desc: 'IP', default: '172.0.0.1' },
    { value: '${ipAddress}', desc: 'IP归属地', default: '深圳.宝安' },
  ]

  const openDialog = () => {
    pageData.showDialog = true
    //请求数据回显
    sysApi.configuration
      .getSysConfObj('sysLogErrorMsgConf')
      .then((res: any) => {
        if (res.isOk && res.data) {
          pageData.msgConfObj.id = res.data.id
          pageData.msgConfObj.isEnable = res.data.isEnable
          pageData.msgConfObj.confJson = JSON.parse(res.data.json)
        }
      })
  }
  const closeDialog = () => {
    pageData.showDialog = false
  }

  const saveData = () => {
    pageData.showDialog = false
    const param = {
      id: pageData.msgConfObj.id,
      code: pageData.msgConfObj.confType,
      isEnable: pageData.msgConfObj.isEnable,
      json: JSON.stringify(pageData.msgConfObj.confJson),
    }
    sysApi.configuration.saveSysConfObj(param)
  }

  const dialogChange = () => {
    if (pageData.identityType == 'user') {
      pageData.identityTypeDesc = '用户'
      pageData.dialogKey = 'userSelector'
      pageData.dialogButtonDesc = '请选择用户'
    }
    if (pageData.identityType == 'post') {
      pageData.identityTypeDesc = '岗位'
      pageData.dialogKey = 'postSelector'
      pageData.dialogButtonDesc = '请选择岗位'
    }
    if (pageData.identityType == 'org') {
      pageData.identityTypeDesc = '组织'
      pageData.dialogKey = 'orgSelector'
      pageData.dialogButtonDesc = '请选择组织'
    }
    if (pageData.identityType == 'role') {
      pageData.identityTypeDesc = '角色'
      pageData.dialogButtonDesc = '请选择角色'
      pageData.dialogKey = 'roleSelector'
    }
  }

  const setreceivers = (list: any) => {
    if (!list || list.length == 0) {
      return
    }
    list.forEach((item: any) => {
      // 去重
      const ident = pageData.msgConfObj.confJson.receivers.find(
        (ident: any) => {
          return (
            `${item.id}${pageData.identityType}` === `${ident.id}${ident.type}`
          )
        }
      )
      if (!ident) {
        pageData.msgConfObj.confJson.receivers.push({
          id: item.id,
          name: item.name,
          type: pageData.identityType,
          assign: `(${pageData.identityTypeDesc})${item.name}`,
        })
      }
    })
  }

  const deletereceivers = (index: number) => {
    pageData.msgConfObj.confJson.receivers.splice(index, 1)
  }

  const addParam = (msgTag: any) => {
    pageData.msgConfObj.confJson.msgContent =
      pageData.msgConfObj.confJson.msgContent + msgTag.value
  }

  const sampleData = computed(() => {
    let result = pageData.msgConfObj.confJson.msgContent
    msgTag.forEach((tag: any) => {
      result = result.replaceAll(tag.value, tag.default)
    })
    return result
  })
</script>
