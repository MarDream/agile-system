package com.dstz.base.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.dstz.base.enums.AbDbType;
import com.dstz.base.model.AbDataSourceModel;
import com.dstz.base.utils.AbDataSourceUtils;
import org.apache.ibatis.executor.Executor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 重写分页逻辑，从当前上下文获取数据源类型
 *
 */
public class AbPaginationInnerInterceptor extends PaginationInnerInterceptor {

    private final Map<AbDbType, DbType> dbTypeRegistry = new ConcurrentHashMap<>();

    @Override
    protected IDialect findIDialect(Executor executor) {
        AbDataSourceModel abDataSourceModel = AbDataSourceUtils.getCurrentDataSource();
        AbDbType abDbType = abDataSourceModel.getDbType();
        DbType dbType = dbTypeRegistry.computeIfAbsent(abDbType, k -> DbType.getDbType(k.getDb()));
        return DialectFactory.getDialect(dbType);
    }
}
