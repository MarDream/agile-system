package com.dstz.base.common.identityconvert;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.enums.IdentityType;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 描述：流程与组织挂接实体接口 type : user / 其他group 类型
 *
 */
public class SysIdentity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String type;

	public SysIdentity() {
	}

	public SysIdentity(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public SysIdentity(IUser user) {
		this.id = user.getUserId();
		this.name = user.getFullName();
		this.type = IdentityType.USER.getKey();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public int hashCode() {
		return this.id.hashCode() + this.type.hashCode();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof SysIdentity)) {
			return false;
		}

		if (StrUtil.isEmpty(id) || StrUtil.isEmpty(name)) {
			return false;
		}

		SysIdentity identity = (SysIdentity) obj;

		if (this.type.equals(identity.getType()) && this.id.equals(identity.getId())) {
			return true;
		}

		return false;
	}

	/**
	 * 从group创建SysIdentity实例
	 *
	 * @param group group
	 * @return SysIdentity 实例
	 */
	public static SysIdentity of(IGroup group) {
		return new SysIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType());
	}

	/**
	 * 从参数创建SysIdentity实例
	 *
	 * @param id           id
	 * @param name         名称
	 * @param identityType 类型
	 * @return 系统人员
	 */
	public static SysIdentity of(String id, String name, IdentityType identityType) {
		return new SysIdentity(id, name, identityType.getKey());
	}

	/**
	 * <pre>
	 * 返回封装了type和name的显示用的信息
	 * </pre>	
	 * @return
	 */
	@Deprecated
	public String getAssign() {
		String str = "(";
		if ("role".equals(type)) {
			str += "角色";
		} else if ("org".equals(type)) {
			str += "组织";
		} else if ("group".equals(type)) {
			str += "小组";
		} else if ("post".equals(type)) {
			str += "岗位";
		} else if ("user".equals(type)) {
			str += "用户";
		}
		return str + ")" + name;
	}

	/**
	 * 生成人员分配名称
	 *
	 * @return [人员类型]人员名称
	 */
	public String genAssignName() {
		String typeDesc;
		if (IdentityType.USER.getKey().equalsIgnoreCase(this.type)) {
			typeDesc = IdentityType.USER.getValue();
		} else {
			typeDesc = Arrays.stream(GroupType.values())
					.filter(ele -> ele.getType().equalsIgnoreCase(this.type))
					.map(GroupType::getDesc)
					.findFirst()
					.orElse(StrUtil.EMPTY);
		}
		return String.format("[%s]%s", typeDesc, this.name);
	}

	/**
	 * 生成人员分配信息
	 *
	 * @return 人员类型/人员名称/人员ID
	 */
	public String genAssignInfo() {
		return String.format("%s/%s/%s", this.type, this.name, this.id);
	}
}
