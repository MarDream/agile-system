package com.dstz.base.model;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.enums.AbDbType;
import com.dstz.base.utils.AbDataSourceUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.function.SingletonSupplier;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 数据源模型
 *
 */
public class DefaultAbDataSourceModel implements AbDataSourceModel {

    private static final String SEATA_DS_PROXY_CLASS = "io.seata.rm.datasource.DataSourceProxy";

    private static final boolean SEATA_DS_PROXY_CLASS_PRESENT = ClassUtils.isPresent(SEATA_DS_PROXY_CLASS, Thread.currentThread().getContextClassLoader());

    /**
     * 数据源别名
     */
    private final String dsAlias;

    /**
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * 数据库类型
     */
    private final AbDbType dbType;

    /**
     * 操作模板
     */
    private final SingletonSupplier<JdbcOperations> jdbcOperations = SingletonSupplier.of(() -> new JdbcTemplate(getDataSource()));

    /**
     * jdbc 事务处理器
     */
    private final SingletonSupplier<DataSourceTransactionManager> dataSourceTransactionManager = SingletonSupplier.of(() -> new DataSourceTransactionManager(getDataSource()));

    private final SingletonSupplier<ConcurrentHashMap<Object, Object>> resources = SingletonSupplier.of(ConcurrentHashMap::new);

    public DefaultAbDataSourceModel(String dbAlias, DataSource dataSource) {
        this.dsAlias = dbAlias;
        this.dataSource = proxyDataSource(dataSource);
        try {
            this.dbType = AbDbType.fromProductName(JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getDatabaseProductName));
        } catch (MetaDataAccessException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            // 绕过编译检查
            throw new RuntimeException();
        }
        // 发布创建事件
        SpringUtil.getBeansOfType(AbDataSourceModeInitializeSchema.class).values().forEach(initializer -> initializer.initialize(DefaultAbDataSourceModel.this));
    }

    private DataSource proxyDataSource(DataSource dataSource) {
        // 代理Seata
        if (SEATA_DS_PROXY_CLASS_PRESENT) {
            Class<? extends DataSource> seataProxyClass = ClassUtil.loadClass(SEATA_DS_PROXY_CLASS, false);
            if (!seataProxyClass.isAssignableFrom(AopUtils.getTargetClass(dataSource))) {
                return ReflectUtil.newInstance(seataProxyClass, dataSource);
            }
        }
        return dataSource;
    }

    @Override
    public String getDsAlias() {
        return dsAlias;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbDbType getDbType() {
        return dbType;
    }

    @Override
    public JdbcOperations getJdbcOperations() {
        return this.jdbcOperations.get();
    }

    @Override
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        return dataSourceTransactionManager.get();
    }

    /**
     * 获取资源
     *
     * @param key             关联资源KEY
     * @param mappingFunction 如果不存在调用映射函数
     * @return 资源
     */
    @Override
    public Object getResource(Object key, Supplier<Object> mappingFunction) {
        return Objects.requireNonNull(resources.get()).computeIfAbsent(key, k -> mappingFunction.get());
    }

    /**
     * 解绑资源
     *
     * @param key 关联键
     * @return 解绑资源
     */
    @Override
    public Object unbindResource(Object key) {
        return Objects.requireNonNull(resources.get()).remove(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbDataSourceModel that = (AbDataSourceModel) o;
        return dsAlias.equals(that.getDsAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(dsAlias);
    }

    @Override
    public void close() throws Exception {
        Objects.requireNonNull(resources.get()).clear();
        AbDataSourceUtils.closeDataSource(getDataSource());
    }
}
