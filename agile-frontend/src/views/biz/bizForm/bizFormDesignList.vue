<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query['a.typeCode$VIN']"
          type-code="biz"
          @node-click="searchResetPage()"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <!--查询区域-->
            <el-row>
              <el-col class="top-panel" :span="24">
                <el-form ref="queryForm" :inline="true" :model="query">
                  <el-form-item
                    label="名称"
                    label-width="62px"
                    prop="a.name$VLK"
                  >
                    <el-input
                      v-model="query['a.name$VLK']"
                      placeholder="请输入名称"
                    />
                  </el-form-item>
                  <el-form-item
                    label="编码"
                    label-width="62px"
                    prop="a.code$VLK"
                  >
                    <el-input
                      v-model="query['a.code$VLK']"
                      placeholder="请输入编码"
                    />
                  </el-form-item>
                  <el-form-item
                    label="业务对象编码"
                    label-width="118px"
                    prop="a.boCode$VLK"
                  >
                    <el-input
                      v-model="query['a.boCode$VLK']"
                      placeholder="业务对象编码"
                    />
                  </el-form-item>

                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      查询
                    </el-button>
                    <el-button :icon="Refresh" @click="reset()">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
            <!--按钮区域-->
            <el-button
              :icon="Plus"
              type="primary"
              @click="diy"
            >
              表单设计
            </el-button>
            <!-- 导入 -->
            <span style="margin-left: 5px">
              <span >
                <ab-upload-dialog
                  accept=".form"
                  :after-save="afterImp"
                  :data="uploadData"
                  :search="search"
                  :timeout="2 * 60 * 1000"
                />
              </span>
            </span>
            <el-button
              :disabled="!selectedData || selectedData.length == 0"
              style="margin-left: 5px"
              type="primary"
              @click="exportXml()"
            >
              导出
            </el-button>
            <el-button
              :disabled="!selectedData || selectedData.length == 0"
              :icon="Delete"
              type="danger"
              @click="delBySeletedIds(bizApi.bizForm.BizFormDesignRemove)"
            >
              批量删除
            </el-button>
          </div>
          <!--列表区域-->
          <ab-table
            ref="abTable"
            v-model="selectedData"
            :checkable="true"
            :height="tableHeight"
            highlight-current-row
            is-have-tree
            :query-param="query"
            row-key="id"
            :url="bizApi.bizForm.BizFormDesignListVOJson"
          >
            <ab-column label="名称" min-width="200" prop="name" />
            <ab-column label="编码" min-width="120" prop="code" />
            <ab-column label="业务对象编码" min-width="180" prop="boCode" />
            <ab-column label="业务对象" min-width="160" prop="boName" />
            <ab-column
              ab-text-formatter="byBo-专业-success-dark|diyBo-简单-warning-dark"
              label="模式"
              prop="mode"
              width="80"
            />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="更新时间"
              prop="updateTime"
              width="150"
            />
            <ab-column label="更新人" min-width="120" prop="operator" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="操作"
              width="220"
            />
            <template #edit="{ scope }">
              <el-button
                text
                type="primary"
                @click="open(scope.row)"
              >
                设计
              </el-button>
              <el-button
                text
                type="primary"
                @click="previewPc(scope.row.pcFormCode)"
                >
                预览
              </el-button>
              <el-dropdown>
                <el-button :icon="ArrowDown" text type="primary">
                  更多
                  <!-- <el-icon class="el-icon--right"><ArrowDown /></el-icon> -->
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <span v-if="scope.row.pcFormCode">
                      <el-dropdown-item>
                        <biz-permission
                          :bo-codes="scope.row.boCode"
                          type="form"
                          :value="scope.row.pcFormCode"
                        >
                          表单权限
                        </biz-permission>
                      </el-dropdown-item>
                    </span>
                    <span v-if="scope.row.mbFormCode">
                      <!-- <el-dropdown-item
                        divided
                        @click="previewMb(scope.row.mbFormCode)"
                      >
                        预览移动端
                      </el-dropdown-item> -->
                    </span>
                    <span >
                      <el-dropdown-item
                        divided
                        @click="exportXml(scope.row.code)"
                      >
                        导出
                      </el-dropdown-item>
                    </span>
                    <span >
                      <el-dropdown-item @click="copy(scope.row)">
                        <el-popover
                          placement="right"
                          title="提示"
                          trigger="hover"
                          :width="300"
                        >
                          <template #reference>复制</template>
                          <span v-if="scope.row.mode == 'diyBo'">
                            会连同业务对象一并复制
                          </span>
                          <span v-else>共用当前表单业务对象</span>
                        </el-popover>
                      </el-dropdown-item>
                    </span>

                    <span >
                      <el-dropdown-item
                        @click="
                          sendAction(
                            bizApi.bizForm.BizFormDesignRemove + scope.row.id,
                            '确定删除' + scope.row.name + '吗?'
                          )
                        "
                      >
                        删除
                      </el-dropdown-item>
                    </span>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import { ElMessage } from 'element-plus'
  import {
    abTableMix,
    abDictTree,
    bizApi,
    sysApi,
    abTools,
    abUploadDialog,
  } from 'agilebpm'
  import { Search, Refresh, Plus, ArrowDown } from '@element-plus/icons-vue'
  import BizPermission from '@/views/biz/bizObject/bizPermission.vue'

  export default {
    components: { BizPermission, abDictTree, abUploadDialog },
    mixins: [abTableMix],
    setup() {
      return {
        bizApi,
        abDictTree,
        Search,
        Refresh,
        Plus,
        ArrowDown,
        sysApi,
        abTools,
        abUploadDialog,
      }
    },
    data() {
      return {
        mobileUrl: '',
        uploadData: {
          btnText: '导入',
          tip: '请选择.form文件',
          url: bizApi.bizForm.BizFormDesignImportXml,
        },
        proxy: null,
      }
    },
    created() {
      const { proxy } = abTools.useCurrentInstance()
      this.proxy = proxy

      this.getMobileUrl()
    },
    methods: {
      add(data) {
        if (data.length == 0) {
          ElMessage({
            message: '请选择业务对象',
            type: 'warning',
          })
          return
        }
        window.open(
          abTools.getResolveUrl(
            `/formDesigner?boCode=${data[0].code}&typeCode=${
              this.query['a.typeCode$VEQ'] || ''
            }`,
            this.proxy
          )
        )
      },
      diy() {
        window.open(
          abTools.getResolveUrl(
            `/formDesigner?mode=diyBo&typeCode=${
              this.query['a.typeCode$VEQ'] || ''
            }`,
            this.proxy
          )
        )
      },
      open(row) {
        const url = abTools.getResolveUrl(
          `/formDesigner?id=${row.id}&mode=${row.mode}&typeCode=${
            this.query['a.typeCode$VEQ'] || ''
          }`,
          this.proxy
        )

        window.open(url)
      },
      codePc(code) {
        window.open(
          abTools.getResolveUrl(`/bizFormCode?code=${code}`, this.proxy)
        )
      },
      codeMb(code) {
        window.open(
          abTools.getResolveUrl(`/bizFormCode?type=mb&code=${code}`, this.proxy)
        )
      },
      previewPc(code) {
        window.open(
          abTools.getResolveUrl(`/bizForm/preview/${code}?del=1`, this.proxy)
        )
      },
      copy(row) {
        window.open(
          abTools.getResolveUrl(
            `/formDesigner?copyId=${row.id}&mode=${row.mode}&typeCode=${
              this.query['a.typeCode$VEQ'] || ''
            }`,
            this.proxy
          )
        )
      },
      exportXml(code) {
        if (!code) {
          code = this.selectedData.map((d) => d.code).join(',')
        }
        bizApi.bizForm.exportXml(code).then(({ data }) => {
          let name
          if (!code.includes(',')) {
            name = data[code].design.name
          } else {
            name = `${this.selectedData[0].name}等${this.selectedData.length}个表单`
          }
          abTools.downImgFile(`${name}.form`, JSON.stringify(data))
        })
      },
      async getMobileUrl() {
        const data = await sysApi.property.getByCode('mobileUrl')
        this.mobileUrl = data.data
        console.info(this.mobileUrl)
      },
      afterImp(result) {
        if (!result.isOk) {
          ElMessage({
            message: '导入失败',
            type: 'error',
          })
          return
        }
        ElMessage({
          message: result.data,
          type: 'success',
          dangerouslyUseHTMLString: true,
        })
      },
    },
  }
</script>
