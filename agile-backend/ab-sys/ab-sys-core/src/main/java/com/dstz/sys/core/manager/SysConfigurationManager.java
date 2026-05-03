package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysConfiguration;
import com.dstz.sys.rest.model.vo.SysConfigurationVO;

import java.util.List;

/**
 * <p>
 *  通用业务类
 * </p>
 *
 * @since 2023-05-11
 */
public interface SysConfigurationManager extends AbBaseManager<SysConfiguration> {

    /**
     * 根据编码获取系统配置
     * @param code 配置编码
     * @return 配置信息
     */
    SysConfiguration getConfByCode(String code);


    /**
     * 保存系统配置
     *
     * @param sysConfiguration 配置信息
     */
    void save(SysConfiguration sysConfiguration);

    /**
     * 根据编码删除配置
     * @param code 配置编码
     */
    void deleteByCode(String code);

    /**
     * 获取系统属性集合
     * @return
     */
    List<SysConfigurationVO>  getSysProperties(String codes);

    /**
     * 禁用启用
     * @param code
     * @return
     */
    int changeEnableByCode(String code);

    /**
     * 修改登录类型
     * @param code
     * @return
     */
    int changeQuickLoginType(String code);
}
