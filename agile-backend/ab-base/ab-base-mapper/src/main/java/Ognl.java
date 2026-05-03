import cn.hutool.core.util.StrUtil;
import com.dstz.base.enums.AbDbType;
import com.dstz.base.utils.AbDataSourceUtils;

/**
 * 给 mybatis mapper 扩展 工具类。
 *
 * @date 2020-10-10
 */
public class Ognl {

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStrEmplty(String str) {
        return StrUtil.isEmpty(str);
    }


    /**
     * 当前线程请求是否为SQLServer
     *
     * @return
     */
    public static boolean isSqlServer() {
        return AbDbType.SQLSERVER.equals(AbDataSourceUtils.getCurrentDataSource().getDbType());
    }

    /**
     * 当前线程请求是否为 postgresql
     *
     * @return
     */
    public static boolean isPostgreSql() {
        return AbDbType.POSTGRESQL.equals(AbDataSourceUtils.getCurrentDataSource().getDbType());
    }
    
    public static String $aaaaaaa$(){
        return "001";
    }
}
