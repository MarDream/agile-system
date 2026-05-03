package com.dstz.base.model;

import com.dstz.base.enums.AbDbType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * 数据源模型
 *
 */
public interface AbDataSourceModel extends AutoCloseable {

	/**
	 * 获取数据源别名
	 *
	 * @return 数据源别名
	 */
	String getDsAlias();

	/**
	 * 获取数据库类型
	 *
	 * @return 数据库类型
	 */
	AbDbType getDbType();

	/**
	 * 获取事务管理器
	 *
	 * @return 事务管理器
	 */
	DataSourceTransactionManager getDataSourceTransactionManager();

	/**
	 * 获取数据源
	 *
	 * @return 数据源
	 */
	DataSource getDataSource();

	/**
	 * 获取JDBC操作器
	 *
	 * @return JDBC操作器
	 */
	JdbcOperations getJdbcOperations();

	/**
	 * 获取资源
	 *
	 * @param key             资源KEY
	 * @param mappingFunction 资源不存在将调用函数获取
	 * @return 资源
	 */
	Object getResource(Object key, Supplier<Object> mappingFunction);

	/**
	 * 解绑资源
	 *
	 * @param key 资源KEY
	 * @return 资源对象
	 */
	Object unbindResource(Object key);
}
