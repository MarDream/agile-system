package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysConnectRecord;

import java.util.List;

/**
 * <p>
 * 公共业务关联记录 通用业务类
 * </p>
 *
 * @since 2022-02-17
 */
public interface SysConnectRecordManager extends AbBaseManager<SysConnectRecord> {

    /**
     * 根据目标Id 和类型获取
     *
     * @param targetId
     * @param type
     * @return
     */
    List<SysConnectRecord> getByTargetId(String targetId, String type);

    /**
     * 根据资源Id和类型获取
     * @param sourceId
     * @param type
     * @return
     */
    List<SysConnectRecord> getBySourceId(List<String> sourceIds, String type);

    /**
     * 根据源Id删除记录
     *
     * @param sourceId
     * @param type
     */
    void removeBySourceId(String sourceId, String type);
}
