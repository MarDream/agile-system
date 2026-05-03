package com.dstz.base.common.constats;

import java.util.List;

import cn.hutool.core.collection.CollUtil;

/**
 * 线程变量KEY常量定义
 *
 */
public abstract class ThreadMapKeyConstant {

	public static class DataPrivilege {

		public static final String PREFIX = "DataPrivilege";

		/**
		 * 当前用户数据权限
		 */
		public static final String CURRENT_USER = PREFIX + ".currentUser";
	}

	public static class QueryFilter {

		/**
		 *
		 */
		public static final String PREFIX = "QueryFilter";

		/**
		 * 允许参数过滤
		 */
		public static final String ACCESS_QUERY_FILTERS = PREFIX + ".accessQueryFilters";
	}

	public static class BizFormDesign {
		public static final String PREFIX = "BizFormDesign";

		public static final String IMP_CODES = PREFIX + ".impCodes";
	}

	public static class BizData {
		public static final String PREFIX = "BizData";

		public static final String IS_FLOW = PREFIX + ".isFlow";

	}

	public static class BpmFormDataService {
		public static final String PREFIX = "BpmFormDataService";

		public static final String VAR_MAP = PREFIX + ".varMap";

		public static final String BIZ_DATA_MAP = PREFIX + ".bizDataMap";
	}

	public static class DataFormatTools {
		public static final String PREFIX = "DataFormatTools";

		public static final String ERR_MSG_MAP = PREFIX + ".errMsgMap";
	}

	public static class CtrlParserExport {
		public static final String PREFIX = "CtrlParserExport";

		public static final String DIALOG_KEY_SET = PREFIX + ".dialogKeySet";
	}
	
	public static class FlowImgStream {
		public static final String PREFIX = "FlowImgStream";
		
		public static final String NODE_MAP = PREFIX + ".nodeMap";
		public static final String FLOW_MAP = PREFIX + ".flowMap";
		public static final String GATE_MAP = PREFIX + ".gateMap";
	}
	
	public static class DesignQuery {
		public static final String PREFIX = "DesignQuery";

		public static final String MAPPING = PREFIX + ".mapping";
		
		public static final String INDEX = PREFIX + ".index";
	}

	/**
	 * 应用安装，安装的相关信息，
	 * 应用列表的key、表单的key，流程的key，自定义对话框，数据字典,组联式表单等
	 *
	 */
	public static class AppInstallInfo {
		public static final String PREFIX = "AppInfo";

		/**
		 * 自定义对话框
		 */
		public static final String DIALOG_KEY = PREFIX + ".dialogKey";
		/**
		 * 自定义列表
		 */
		public static final String GRID_KEY = PREFIX + ".gridKey";
		/**
		 * 表单
		 */
		public static final String FORM_KEY = PREFIX + ".formKey";
		public static final String FORM_ID = PREFIX + ".formId";
		/**
		 * 业务实体
		 */
		public static final String TABLE_KEY = PREFIX + ".tableKey";
		/**
		 * 业务对象
		 */
		public static final String BO_KEY = PREFIX + ".boKey";
		/**
		 * 流程
		 */
		public static final String BPM_DEF_KEY = PREFIX + ".bpmDefKey";
		public static final String BPM_DEF_ID = PREFIX + ".bpmDefId";
		/**
		 * 数据字典
		 */
		public static final String DICT_KEY = PREFIX + ".dictKey";
		/**
		 * 组联式表单
		 */
		public static final String FORM_COMBINATION_KEY = PREFIX + ".formCombinationKey";

		/**
		 * 流水号
		 */
		public static final String SERIAL_NO_KEY = PREFIX + ".serialNoKey";

		public static final String MENU_KEY = PREFIX + ".menuKey";

		public static final List<String> APP_INFO_KEY_LIST = CollUtil.toList(DIALOG_KEY, GRID_KEY, FORM_KEY, TABLE_KEY, BO_KEY, BPM_DEF_KEY, DICT_KEY, FORM_COMBINATION_KEY, SERIAL_NO_KEY,MENU_KEY);

	}

	/**
	 * 流程启动测试变量
	 */
	public static class FlowStartTest {

		/**
		 * 流程启动测试
		 */
		public static final String START_TEST = "flowStartTest";

	}
	
}
