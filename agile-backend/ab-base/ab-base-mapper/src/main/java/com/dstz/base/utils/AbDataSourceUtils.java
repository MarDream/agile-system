package com.dstz.base.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.context.AbDataSourceContext;
import com.dstz.base.model.AbDataSourceModel;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * 数据源上下文工具类
 *
 */
public class AbDataSourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(AbDataSourceUtils.class);

    private static volatile AbDataSourceContext dataSourceContext;

    /**
     * 获取数据源上下文对象
     *
     * @return 数据源上下文对象
     */
    public static AbDataSourceContext getDataSourceContext() {
        if (dataSourceContext == null) {
            synchronized (AbDataSourceUtils.class) {
                if (dataSourceContext == null) {
                    dataSourceContext = SpringUtil.getBean(AbDataSourceContext.class);
                }
            }
        }
        return dataSourceContext;
    }

    /**
     * 切换数据源
     *
     * @param dsAlias 数据源别名
     * @return 切换后的数据源
     */
    public static void switchDataSource(String dsAlias) {
        getDataSourceContext().switchDataSource(dsAlias);
    }

    /**
     * 获取当前数据源
     *
     * @return 当前数据源
     */
    public static AbDataSourceModel getCurrentDataSource() {
        return getDataSourceContext().getCurrentDataSource();
    }

    /**
     * 获取数据源，该获取不会切换，但会加入到当前事务管理器当中
     *
     * @param dsAlias 数据源别名
     * @return 数据源模型
     */
    public static AbDataSourceModel getByDsAlias(String dsAlias) {
        return getDataSourceContext().getByDsAlias(dsAlias);
    }

    /**
     * 关闭数据源
     *
     * @param dataSource 数据源
     */
    public static void closeDataSource(DataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        try {
            if (dataSource instanceof AutoCloseable) {
                ((AutoCloseable) dataSource).close();
            } else if (dataSource instanceof PooledDataSource) {
                ((PooledDataSource) dataSource).forceCloseAll();
            } else {
                Class<?> targetClass = AopUtils.getTargetClass(dataSource);
                Class<?> dataSourceProxyClass = ClassUtil.loadClass("org.apache.tomcat.jdbc.pool.DataSourceProxy");
                if (dataSourceProxyClass.isAssignableFrom(targetClass)) {
                    ReflectUtil.invoke(dataSource, "close", true);
                }
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 切换数据源执行调用
     *
     * @param targetDsAlias 目标数据源
     * @param withSupplier  执行方法
     * @param <V>           方法返回结果类型
     * @return 方法执行结果
     */
    public static <V> V switchDataSourceRunWith(String targetDsAlias, Supplier<V> withSupplier) {
        AbDataSourceModel currentDataSource = getCurrentDataSource();
        try {
            switchDataSource(targetDsAlias);
            return withSupplier.get();
        } finally {
            // 切换回原调用数据源
            switchDataSource(currentDataSource.getDsAlias());
        }
    }
}
