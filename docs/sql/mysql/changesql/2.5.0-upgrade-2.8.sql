SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `ab_message_template` ADD COLUMN `sms_template_code_` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信供应商模板code' AFTER `template_param_`;

ALTER TABLE `ab_message_template` ADD COLUMN `template_file_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板附件' AFTER `sms_template_code_`;

ALTER TABLE `ab_message_template` ADD UNIQUE INDEX `message_template_code_index`(`code_`) USING BTREE COMMENT '消息模板编码唯一索引';

ALTER TABLE `biz_column` MODIFY COLUMN `default_value_` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL AFTER `sn_`;

ALTER TABLE `biz_cust_dialog` ADD COLUMN `type_code_` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典分类' AFTER `name_`;

ALTER TABLE `biz_cust_dialog` MODIFY COLUMN `obj_name_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `obj_type_`;

ALTER TABLE `biz_cust_grid` MODIFY COLUMN `code_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码' AFTER `name_`;

ALTER TABLE `biz_cust_grid` MODIFY COLUMN `extend_conf_` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展配置' AFTER `left_tree_`;

ALTER TABLE `biz_cust_grid_field` ADD COLUMN `bg_color_conf_` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '背景色配置' AFTER `fuzz_`;

ALTER TABLE `biz_object` ADD COLUMN `rev_` int(11) NULL DEFAULT 0 COMMENT '乐观锁' AFTER `interface_list_`;

ALTER TABLE `biz_permission` DROP INDEX `obj_type_obj_val_unique_idx_`;

ALTER TABLE `biz_permission` ADD COLUMN `version_` int(11) NULL DEFAULT NULL COMMENT '版本号' AFTER `update_by_`;

ALTER TABLE `biz_table` ADD COLUMN `rev_` int(11) NULL DEFAULT 0 COMMENT '乐观锁' AFTER `update_by_`;

CREATE TABLE `bpm_schedule_start_log` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `start_id_` varchar(64) NOT NULL COMMENT '流程定时启动ID',
  `def_key_` varchar(64) NOT NULL COMMENT '流程定义KEY',
  `def_id_` varchar(64) NOT NULL COMMENT '流程定义ID',
  `inst_id_` varchar(64) DEFAULT NULL COMMENT '流程实例ID',
  `successful_` tinyint(3) unsigned NOT NULL COMMENT '执行成功',
  `exception_stack_` text COMMENT '异常堆栈',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id_`),
  KEY `start_id_` (`start_id_`),
  KEY `def_id_` (`def_id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程定时启动日志';


CREATE TABLE `bpm_plugin_ensure_candidate` (
  `id_` varchar(64) NOT NULL COMMENT 'ID',
  `inst_id_` varchar(64) DEFAULT NULL COMMENT '实例 ID',
  `node_key_` varchar(255) DEFAULT NULL COMMENT '任务KEY',
  `task_name_` varchar(255) DEFAULT NULL COMMENT '任务名',
  `identities_` varchar(2000) DEFAULT NULL COMMENT '候选人JSON',
  `node_type_` varchar(64) DEFAULT NULL COMMENT '节点类型',
  `skip_reason_` varchar(255) DEFAULT NULL COMMENT '跳过原因',
  `is_skip_` varchar(64) DEFAULT NULL COMMENT '是否跳过',
  `distnation_` varchar(255) DEFAULT NULL COMMENT '目标节点，如果存在则跳转',
  `is_force_update_` varchar(20) DEFAULT NULL COMMENT '是否强制更新',
  `is_dynamic_task_` varchar(20) DEFAULT NULL COMMENT '是否动态任务',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
  `updater_` varchar(64) DEFAULT NULL COMMENT '更新人',
  `create_org_id_` varchar(64) DEFAULT NULL COMMENT '所属组织',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id_`),
  KEY `ensure_candidate_inst_id_idx` (`inst_id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='明确候选人插件';



  
CREATE TABLE `bpm_plugin_reminder_log`  (
  `id_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `instance_id_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `reminder_title_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办标题',
  `title_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程标题',
  `task_def_key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务定义KEY',
  `msg_type_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办消息类型',
  `reminder_users_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办人',
  `reminder_userids_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办人ID',
  `reminder_date_` datetime(0) NULL DEFAULT NULL COMMENT '催办时间',
  `supervisor_names_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '督办人',
  `supervisor_ids_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '督办人Id',
  `reminder_type_` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办类型',
  `flow_operate_` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程操作',
  `receiver_names_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转办人',
  `receiver_ids_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转办人ids',
  `extend_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他说明',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程催办日志' ROW_FORMAT = Dynamic;

CREATE TABLE `bpm_plugin_reminder_trigger`  (
  `id_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `task_id_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务ID',
  `reminder_desc_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办的描述',
  `before_script_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办前置脚本',
  `msg_type_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办消息类型',
  `html_msg_` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'html消息',
  `text_msg_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '普通消息',
  `template_code_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息模板编码',
  `is_calc_workday_` tinyint(4) NULL DEFAULT NULL COMMENT '是否工作日计算',
  `is_urgent_` tinyint(4) NULL DEFAULT NULL COMMENT '是否加急任务',
  `max_reminder_times` int(11) NULL DEFAULT NULL COMMENT '最多催办次数',
  `reminder_times_` int(11) NULL DEFAULT NULL COMMENT '催办次数',
  `reminder_cycle_` int(11) NULL DEFAULT NULL COMMENT '催办周期',
  `duedate_` datetime(0) NOT NULL COMMENT '到期时间',
  `retries_` tinyint(4) NULL DEFAULT NULL COMMENT '重试次数',
  `version_` tinyint(4) NULL DEFAULT NULL COMMENT '版本号',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time_` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `exception_id_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常ID',
  `lock_expire_at_` datetime(0) NULL DEFAULT NULL COMMENT '持锁到期时间，用于并发催办处理',
  `lock_machine_at_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '持锁机器',
  `exception_msg_` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  `reminder_type_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '催办类型：msg（发送消息），taskTurn（任务转办），flowOperate（流程操作），',
  `receiver_` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转办接收人',
  `supervise_template_code_` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '督办消息模板code',
  `supervisor_` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '督办人',
  `flow_operate_` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程操作： turn 转办，skip 跳过当前任务，end 结束流程',
  PRIMARY KEY (`id_`) USING BTREE,
  INDEX `task_id_`(`task_id_`) USING BTREE,
  INDEX `duedate_`(`duedate_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程催办触发' ROW_FORMAT = Dynamic;

CREATE TABLE `bpm_plugin_sign`  (
  `id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `inst_id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实例id',
  `task_id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务id',
  `setting_` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置json信息',
  `progress_` int(11) NULL DEFAULT NULL COMMENT '进度信息，下标',
  `node_key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点KEY',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_org_id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属组织',
  `updater_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time_` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人ID',
  `identitis_` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '候选人列表json信息',
  PRIMARY KEY (`id_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会签插件表' ROW_FORMAT = Dynamic;
 
CREATE TABLE `bpm_task_discuss`  (
  `id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `inst_id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例ID',
  `opinion_id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批ID',
  `task_key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务定义Key',
  `task_name_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `content_` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `type_` tinyint(4) NULL DEFAULT 1 COMMENT '类型：0:提交，1：附言，2：评论，3：点赞',
  `secret_` tinyint(1) NULL DEFAULT 0 COMMENT '是否私密：0:否，1：是',
  `attachment_` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件信息',
  `create_by_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `creator_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id_`) USING BTREE,
  INDEX `idx_discuss_opinion`(`opinion_id_`) USING BTREE,
  INDEX `idx_discuss_instId`(`inst_id_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务谈论记录' ROW_FORMAT = Dynamic;

CREATE TABLE `bpm_task_duration_statistics`  (
  `id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `def_key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '定义ID',
  `task_key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点标识',
  `duration` bigint(20) NULL DEFAULT NULL COMMENT '平均耗时，单位是毫秒',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id_`) USING BTREE,
  UNIQUE INDEX `def_id_`(`def_key_`, `task_key_`) USING BTREE,
  INDEX `def_id_idx`(`def_key_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点耗时统计' ROW_FORMAT = Dynamic;

ALTER TABLE `bpm_task_identitylink` DROP INDEX `idx_identitylink_instid`;

ALTER TABLE `bpm_task_identitylink` DROP INDEX `idx_taskcandidate_taskid`;

ALTER TABLE `bpm_task_identitylink` ADD INDEX `idx_identitylink_instid`(`inst_id_`, `task_id_`) USING BTREE;

ALTER TABLE `bpm_task_stack` DROP INDEX `idx_exestack_instid`;

ALTER TABLE `bpm_task_stack` DROP INDEX `idx_exestack_taskid`;

ALTER TABLE `bpm_task_stack` ADD INDEX `idx_taskstack_instid`(`inst_id_`, `task_id_`) USING BTREE;
 
CREATE TABLE `cms_msg_template`  (
  `id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模版名称',
  `key_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模版业务键',
  `is_default_` tinyint(4) NOT NULL COMMENT '默认模板，同一key下可以有多个模板。默认模板只有一个',
  `subject_` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `plain_` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纯文本',
  `html_` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模版体HTML',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `create_org_id_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属组织',
  `update_time_` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `updater_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id_`) USING BTREE,
  UNIQUE INDEX `key_`(`key_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息模版' ROW_FORMAT = Dynamic;
  
ALTER TABLE `sys_configuration` ADD COLUMN `code_` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置类型' AFTER `id_`;

ALTER TABLE `sys_configuration` ADD COLUMN `env_` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置所属环境' AFTER `code_`;

ALTER TABLE `sys_configuration` ADD COLUMN `json_` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置json' AFTER `env_`;

ALTER TABLE `sys_configuration` ADD COLUMN `name_` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置名称' AFTER `is_encrypt_`;

ALTER TABLE `sys_configuration` ADD COLUMN `desc_` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置描述' AFTER `name_`;

ALTER TABLE `sys_configuration` MODIFY COLUMN `is_enable_` tinyint(4) NULL DEFAULT 1 COMMENT '是否可用' AFTER `json_`;

ALTER TABLE `sys_configuration` MODIFY COLUMN `is_encrypt_` tinyint(4) NULL DEFAULT 0 COMMENT '是否加密' AFTER `is_enable_`;

ALTER TABLE `sys_configuration` DROP COLUMN `conf_type_`;

ALTER TABLE `sys_configuration` DROP COLUMN `conf_env_`;

ALTER TABLE `sys_configuration` DROP COLUMN `conf_json_`;

ALTER TABLE `sys_configuration` DROP COLUMN `conf_name_`;

ALTER TABLE `sys_configuration` DROP COLUMN `conf_comment_`;

ALTER TABLE `sys_configuration` ADD UNIQUE INDEX `code_index_`(`code_`) USING BTREE;
 
ALTER TABLE `sys_resource` ADD COLUMN `open_with_` tinyint(4) NULL DEFAULT 0 COMMENT '打开方式（0新页签 1跳转 2新页面）' AFTER `opened_`;

ALTER TABLE `sys_resource` ADD COLUMN `resource_list_` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源集合（菜单、按钮）' AFTER `type_`;

ALTER TABLE `sys_resource` MODIFY COLUMN `code_` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限编码' AFTER `app_id_`;

 
CREATE TABLE `sys_session_attribute`  (
  `id_` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `username_` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `key_` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性键',
  `value_` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值',
  `create_time_` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time_` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id_`) USING BTREE,
  UNIQUE INDEX `username_`(`username_`, `key_`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统会话属性' ROW_FORMAT = Dynamic;
 
CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `bpm_instance_view` AS select `bpm_definition`.`id_` AS `id_`,`bpm_definition`.`name_` AS `name_`,`bpm_definition`.`key_` AS `key_`,`bpm_definition`.`desc_` AS `desc_`,`sys_connect_record`.`target_id_` AS `target_id_`,`biz_form`.`name_` AS `form_name_`,`biz_form`.`bo_code_` AS `bo_code_` from ((`bpm_definition` left join `sys_connect_record` on((`bpm_definition`.`id_` = `sys_connect_record`.`source_id_`))) left join `biz_form` on((`sys_connect_record`.`target_id_` = `biz_form`.`code_`))) where ((`sys_connect_record`.`type_` = 'BPM-DEF-FORM') and (`bpm_definition`.`is_main_` = 1) and (`sys_connect_record`.`target_id_` like '%_pc'));

DROP VIEW `v_sys_tables`;

INSERT INTO  `sys_configuration`(`id_`, `code_`, `env_`, `json_`, `is_enable_`, `is_encrypt_`, `name_`, `desc_`, `create_by_`, `create_time_`, `update_by_`, `update_time_`) VALUES ('1750072528974196736', 'flowConf', NULL, '{\"bpmScheduleStartTaskInterval\":10,\"reminder_max_retries\":5,\"doTaskSuccessional\":true,\"task_remove_message_push\":false,\"bpmNotDefaultButtons\":\"oppose,reject2Start,custMultiExecution,addSign,manualEnd\",\"bpmNotDefaultButtonsList\":[\"oppose\",\"reject2Start\",\"custMultiExecution\",\"addSign\",\"manualEnd\"],\"taskDefaultDurationHour\":24,\"flowLeftTreeTypeSwitch\":true,\"taskRemind\":true,\"taskRemindTime\":\"07:01\",\"cronData\":{\"jobKey\":\"flowConfTaskJob\",\"executorType\":\"bean\",\"executor\":\"bpmTaskServiceImpl@taskStatisticsSendMsg\",\"enable\":1,\"executorParams\":\"\",\"cron\":\"\",\"description\":\"每日任务提醒自动生成任务\",\"typeCode\":\"lcxg\"},\"crontabId\":\"\",\"msgType\":\"email,dingding\"}', 1, 0, '流程全局配置', '流程全局配置，如：定时启动间隔、催办重试次数、连续处理任务、流程非默认按钮、每日任务提醒等配置', '1633022398138232832', '2024-01-24 16:26:20', '1633022398138232832', '2024-01-29 17:52:17');


CREATE TABLE `bpm_user_agency_config` (
  `id_` varchar(64) NOT NULL COMMENT '配置ID',
  `start_datetime_` datetime NOT NULL COMMENT '起始时间',
  `end_datetime_` datetime NOT NULL COMMENT '结束时间',
  `agency_flow_key_` varchar(1000) NOT NULL COMMENT '代理流程编码，多个中间逗号分隔(,)',
  `agency_flow_name_` varchar(5000) NOT NULL COMMENT '代理流程名称，多个中间逗号分隔(,)',
  `config_user_id_` varchar(64) NOT NULL COMMENT '配置用户编码',
  `config_user_name_` varchar(1000) DEFAULT NULL COMMENT '被代理人用户名称',
  `target_user_id_` varchar(1000) NOT NULL COMMENT '目标用户编码，多个中间逗号分隔(,)',
  `target_user_name_` varchar(1000) NOT NULL COMMENT '目标用户姓名，多个中间逗哥分隔(,)',
  `enable_` tinyint(4) NOT NULL DEFAULT '1' COMMENT '启用/未启用(1/0)',
  `desc_` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator_` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
  `create_org_id_` varchar(64) DEFAULT NULL COMMENT '所属组织',
  `updater_` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
  `rev_` int(11) DEFAULT NULL,
  `add_type_` tinyint(4) DEFAULT '0' COMMENT '添加类型：0，自己添加，1，admin 添加',
  `agency_task_` tinyint(4) DEFAULT '0' COMMENT '是否代理已有任务',
  PRIMARY KEY (`id_`),
  KEY `config_user_id_` (`config_user_id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务流程用户代理配置';

ALTER TABLE `sys_data_dict` 
ADD COLUMN `disable_` varchar(255) NULL AFTER `update_by_`;

SET FOREIGN_KEY_CHECKS=1;