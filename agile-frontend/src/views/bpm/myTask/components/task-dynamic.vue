<template>
  <div class="task-dynamic-panel">
    <el-card shadow="never" class="task-dynamic-card">
      <template #header>
        <div class="task-dynamic-header">
          <span>流程动态</span>
          <el-tag size="small" :type="statusTagType">
            {{ statusLabel }}
          </el-tag>
        </div>
      </template>

      <el-empty
        v-if="!instance"
        description="暂无流程实例信息"
        :image-size="88"
      />

      <template v-else>
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="标题">
            {{ instance.title || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="实例ID">
            {{ instance.id || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="流程定义">
            {{ instance.defName || instance.name || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            {{ statusLabel }}
          </el-descriptions-item>
          <el-descriptions-item label="发起人">
            {{ starterName }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatValue(instance.createTime || instance.startTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatValue(instance.updateTime || instance.endTime) }}
          </el-descriptions-item>
        </el-descriptions>

        <div v-if="summaryList.length" class="task-dynamic-summary">
          <div
            v-for="item in summaryList"
            :key="item.label"
            class="task-dynamic-summary-item"
          >
            <div class="task-dynamic-summary-label">{{ item.label }}</div>
            <div class="task-dynamic-summary-value">{{ item.value }}</div>
          </div>
        </div>
      </template>
    </el-card>
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue'

  const props = defineProps({
    instance: {
      type: Object,
      default: null,
    },
  })

  const statusMap: Record<string, string> = {
    draft: '草稿',
    running: '进行中',
    approve: '审批中',
    completed: '已完成',
    end: '已结束',
    cancel: '已取消',
    cancelled: '已取消',
    reject: '已驳回',
  }

  const statusLabel = computed(() => {
    const status = props.instance?.status
    if (!status) {
      return '未知'
    }
    return statusMap[status] || status
  })

  const statusTagType = computed(() => {
    const status = props.instance?.status
    if (status === 'completed' || status === 'end') {
      return 'success'
    }
    if (status === 'cancel' || status === 'cancelled' || status === 'reject') {
      return 'danger'
    }
    if (status === 'draft') {
      return 'info'
    }
    return 'warning'
  })

  const starterName = computed(() => {
    return (
      props.instance?.startUserName ||
      props.instance?.starterName ||
      props.instance?.createByName ||
      '-'
    )
  })

  const summaryList = computed(() => {
    const candidates = [
      { label: '业务主键', value: props.instance?.bizKey },
      { label: '业务ID', value: props.instance?.bizId },
      { label: '流程编码', value: props.instance?.defKey },
    ]

    return candidates.filter((item) => item.value)
  })

  const formatValue = (value: unknown) => {
    if (value === null || value === undefined || value === '') {
      return '-'
    }
    return String(value)
  }
</script>

<style lang="scss" scoped>
  .task-dynamic-panel {
    width: 100%;
  }

  .task-dynamic-card {
    border: 0;
  }

  .task-dynamic-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    font-weight: 600;
  }

  .task-dynamic-summary {
    display: grid;
    grid-template-columns: repeat(1, minmax(0, 1fr));
    gap: 12px;
    margin-top: 16px;
  }

  .task-dynamic-summary-item {
    padding: 12px;
    border-radius: 8px;
    background: #f6f8fb;
  }

  .task-dynamic-summary-label {
    margin-bottom: 6px;
    color: #909399;
    font-size: 12px;
  }

  .task-dynamic-summary-value {
    color: #303133;
    font-size: 14px;
    line-height: 1.5;
    word-break: break-all;
  }
</style>
