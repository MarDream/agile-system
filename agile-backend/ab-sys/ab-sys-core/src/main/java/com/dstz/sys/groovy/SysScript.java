package com.dstz.sys.groovy;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.async.ContextCleanTaskDecorator;
import com.dstz.base.common.async.ContextDuplicationTaskDecorator;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.base.common.script.ScriptLog;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.common.utils.TaskDecoratorUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.datasource.DataSourceLoading;
import com.dstz.base.utils.AbDataSourceUtils;
import com.dstz.groovy.script.api.IScript;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.core.entity.SysProperties;
import com.dstz.sys.core.manager.SysPropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @Name SysScript
 * @description: 系统默认的一些脚本
 * @date 2022/7/2814:13
 */
@Component
public class SysScript implements IScript {

    protected Logger LOG = LoggerFactory.getLogger(getClass());
 
    @Autowired
    private DataSourceLoading dataSourceLoading;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private  SysPropertiesManager sysPropertiesManager;
 

    /**
     * 获取系统属性
     * @param key code
     * @return value
     */
    public String getProperty(String key) {
        SysProperties properties = sysPropertiesManager.selectOne(Wrappers.lambdaQuery(SysProperties.class).eq(SysProperties::getCode,key));
        if (properties!=null){
            return properties.getValue();
        }
        return null;
    }


    public IUser getCurrentUser() {
        IUser user = UserContextUtils.getValidUser();
        return user;
    }

    public String getCurrentGroupName() {
        IGroup iGroup =UserContextUtils.getGroup().get();
        if (iGroup!= null) {
            return iGroup.getGroupName();
        } else {
            return "";
        }
    }

    public String getCurrentUserName() {
        return UserContextUtils.getValidUser().getFullName();
    }

    public Integer executeUpdateSql(String sql, Object ... params) {
        Integer result = jdbcTemplate.update(sql, params);
        return result ;
    }

    public Integer executeUpdateSqlOnDataSource(String dataSourceKey,String sql, Object ... params) {
        JdbcOperations jdbcOperations = AbDataSourceUtils.getByDsAlias(dataSourceKey).getJdbcOperations();
        return jdbcOperations.update(sql, params);
    }


    public Integer executeIntegerSql(String sql, Object ... params) {
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, params);
        return result ;
    }

    public Integer executeIntegerSqlOnDataSource(String dataSourceKey,String sql, Object ... params) {
        JdbcOperations jdbcOperations = AbDataSourceUtils.getByDsAlias(dataSourceKey).getJdbcOperations();
        return jdbcOperations.queryForObject(sql, Integer.class, params);
    }

    /**
     * 通过一个sql 获取候选人
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public Set<SysIdentity> getIdentityBySqlAndType(String sql, String type, Object... params) {
        List<SysIdentity> list =jdbcTemplate.query(sql, new RowMapperResultSetExtractor<>(((rs, rowNum) -> new SysIdentity(rs.getString(1), rs.getString(2), type))), params);
        return transitionIdentitys(list);
    }

    private Set<SysIdentity> transitionIdentitys(List<SysIdentity> list) {
        Set<SysIdentity> set = new HashSet<>();

        for(SysIdentity identity : list) {
            // 如果SQL 返回的候选人不完整则不予添加
            if(StrUtil.isEmpty(identity.getType()) || StrUtil.isEmpty(identity.getName())
                    || StrUtil.isEmpty(identity.getId())) {

                LOG.debug("通过 sql 获取用户候选人失败，sql 返回的 identity 信息不完整，请检查{}", JsonUtils.toJSONString(identity));
                continue;
            }
            set.add(identity);
        }

        return set;
    }

    /**
     * 通过一个sql 获取候选人  指定数据源  指定类型
     * @param dataSourceKey
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public Set<SysIdentity> getIdentityBySqlOnDataSource(String dataSourceKey,String sql,String type, Object ... params) {
        JdbcOperations jdbcOperations = AbDataSourceUtils.getByDsAlias(dataSourceKey).getJdbcOperations();
        List<SysIdentity> list = jdbcOperations.query(sql, new RowMapperResultSetExtractor<>(((rs, rowNum) -> new SysIdentity(rs.getString(1), rs.getString(2), type))), params);
        return transitionIdentitys(list);
    }


    // 开启线程日志
    public void openScriptLog(Boolean isOpen) {
    	ScriptLog.setConfigOpen(isOpen);
    }



    /**
     * 新线程执行一些业务逻辑 eg: sysScript.newThead(cust::save)  or sysScript.newThead( ()->{do any thing} )
     * @param target
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void newThead(Runnable target) throws InterruptedException, ExecutionException {
		Runnable runnable = TaskDecoratorUtils.decorate(target, ContextCleanTaskDecorator.INSTANCE, ContextDuplicationTaskDecorator.INSTANCE);
    	ThreadUtil.execAsync(runnable).get();
    }

}
