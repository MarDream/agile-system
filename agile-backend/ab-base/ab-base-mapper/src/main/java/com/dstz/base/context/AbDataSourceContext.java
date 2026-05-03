package com.dstz.base.context;

import com.dstz.base.model.AbDataSourceModel;

/**
 * 数据库上下文对象
 *
 */
public interface AbDataSourceContext {

    /**
     * 默认数据源别名
     */
    String DEFAULT_DATASOURCE_ALIAS = "dataSourceDefault";

    /**
     * 切换数据源
     *
     * @param dsAlias 数据源别名
     */
    void switchDataSource(String dsAlias);

    /**
     * 获取当前数据源
     *
     * @return 数据源
     */
    AbDataSourceModel getCurrentDataSource();

    /**
     * 获取默认数据源
     *
     * @return 默认数据源
     */
    AbDataSourceModel getDefaultDataSource();

    /**
     * 获取数据源，该获取不会切换，会加入到当前事务管理器当中
     *
     * @param dsAlias 数据源别名
     * @return 数据源模型
     */
    AbDataSourceModel getByDsAlias(String dsAlias);

    /**
     * 移除数据源
     *
     * @param dsAlias 数据源别名
     */
    void removeDataSource(String dsAlias);

    /**
     * 清理上下文
     */
    void clear();
}
