package com.dstz.base.common.context;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

/**
 * <pre>
 * 用户上下文，从当前线程中获取用户、用户所在组织及权限信息
 * 日期:2022-03-22
 * </pre>
 */
public interface UserContext extends ContextDuplication {

    /**
     * 获取用户
     *
     * @return 用户
     */
    Optional<IUser> getUser();

    /**
     * 设置当前用户
     *
     * @param user 用户
     * @return 用户
     */
    void setUser(IUser user);

    /**
     * 获取当前组织
     *
     * @return 当前组织
     */
    Optional<IGroup> getOrg();

    /**
     * 设置当前组织
     *
     * @param org 组织
     * @return 当前组织
     */
    void setOrg(IGroup org);

    /**
     * 获取权限
     *
     * @return 权限
     */
    Collection<String> getAuthorities();

    /**
     * 设置权限
     *
     * @param authorities 权限
     */
    void setAuthorities(Collection<String> authorities);

    /**
     * 是否超级管理员
     *
     * @return 是否超级管理员
     */
    boolean isSuperAdmin();

    /**
     * 设置超级管理员
     *
     * @param superAdmin 超级管理员
     */
    void setSuperAdmin(boolean superAdmin);
    
    /**
     * 清理
     */
    void clear();

    /**
     * 过滤用户 工作交接相关
     * @param userId
     * @return
     */
    Set<String> getFilterUserIds(String userId);
}
