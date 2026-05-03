<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="NoticeList"
        :before-save-fn="handlebeforesaveFn"
        :form-ref="formRef"
        :save-data="info.data"
        :url="
          id ? cmsApi.notify.cmsNotifyUpdateUrl : cmsApi.notify.cmsNotifySaveUrl
        "
      />
      <ab-load
        v-model="info.data"
        :url="cmsApi.notify.cmsNotifyGetUrl"
        @after-fn="afterFn"
      />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="120px"
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="标题"
            placeholder="请输入标题"
            prop="title"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-input v-model="info.data.title" />
          </el-form-item>

          <el-form-item
            label="公告类型"
            prop="typeId"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-select v-model="info.data.typeId" value-key="key">
              <el-option
                v-for="item in info.arr"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>

          <el-form-item
            label="通知部门"
            :rules="[{ required: true, message: '必填' }]"
          >
            <template v-if="info.cmsNotifyShareList.length > 0">
              <el-tag
                v-for="(tag, index) in info.cmsNotifyShareList"
                :key="index"
                class="mx-1"
                closable
                @close="handleClose(tag)"
              >
                {{ tag.groupName ? tag.groupName : tag.name }}
              </el-tag>
            </template>

            <ab-cust-dialog
              v-model="info.cmsNotifyShareList"
              dialog-key="orgSelector"
              :style="{
                marginLeft: info.cmsNotifyShareList.length > 0 ? '10px' : '0px',
              }"
              type="primary"
            >
              选择
            </ab-cust-dialog>
          </el-form-item>
          <el-form-item
            label="内容"
            prop="content"
            :rules="[{ required: true, message: '必填' }]"
          >
            <ab-rich-editor v-model="info.data.content" />
          </el-form-item>
          <el-form-item label="上传">
            <!-- <ab-upload-file
              v-if="id && info.data.attachments.length > 0"
              v-model="info.data.attachments"
              dic-code="notice"
            />
            <ab-upload-file
              v-else
              v-model="info.data.attachments"
              dic-code="notice"
            /> -->
            <ab-upload-file
              v-model="info.data.attachments"
              dic-code="notice"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import {
    sysApi,
    cmsApi,
    abRichEditor,
    abUploadFile,
    abTools,
  } from 'agilebpm'
  import { reactive, ref, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  const formRef = ref()
  //引入 当前实例
  const { proxy } = abTools.useCurrentInstance()
  const id = proxy.$route.query.id
  const typeAlloptions = ref([])
  const info: any = reactive({
    cmsNotifyShareList: [],
    arr: [],
    data: {
      title: '',
      typeId: '',
      groupIdList: [],
      content: '',
      attachments: '',
    },
  })
  //保存前对各个提交字段 非空校验
  const handlebeforesaveFn = () => {
    if (info.data.title.length <= 0) {
      ElMessage({
        message: '请填写标题',
        type: 'warning',
      })
      return false
    }

    info.data.groupIdList = []
    info.cmsNotifyShareList.forEach((element: { groupId: any; id: any }) => {
      info.data.groupIdList.push(element.groupId ? element.groupId : element.id)
    })
    if (info.data.groupIdList.length <= 0) {
      ElMessage({
        message: '请选择通知部门',
        type: 'warning',
      })
      return false
    }
    if (!info.data.content || info.data.content.length <= 0) {
      ElMessage({
        message: '请填写内容',
        type: 'warning',
      })
      return false
    }
    return true
  }
  const handleClose = (tag: string) => {
    info.cmsNotifyShareList.splice(info.cmsNotifyShareList.indexOf(tag), 1)
  }
  //获取分类(数据字典)
  sysApi.dict.getDictListByCode('gglx').then((result) => {
    info.arr = result
  })
  // 修改通知部门后渲染成 tag 展示
  const getdepart = (tags: any) => {
    info.cmsNotifyShareList = tags
  }

  const afterFn = (data: any) => {
    info.cmsNotifyShareList = data.cmsNotifyShareList
  }

  onMounted(() => {})
</script>
