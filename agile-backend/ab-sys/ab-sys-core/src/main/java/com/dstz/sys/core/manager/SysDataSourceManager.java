package com.dstz.sys.core.manager;

import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.rest.model.dto.SysDataSourceSaveDTO;
import com.dstz.sys.core.entity.SysDataSource;

import java.util.List;

/**
 * <p>
 * 数据源 通用业务类
 * </p>
 *
 * @since 2022-02-17
 */
public interface SysDataSourceManager extends AbBaseManager<SysDataSource> {

    /**
     * 判断别名是否存在
     *
     * @param alias
     * @param id
     * @return
     */
    boolean isExist(String alias, String id);

    /**
     * 保存数据源
     *
     * @param saveDTO
     * @return
     */
    int save(SysDataSourceSaveDTO saveDTO);


    List<SysDataSource> query(QueryParamDTO dto);
}
