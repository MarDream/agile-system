package com.dstz.base.enums;

import java.util.Arrays;

import org.springframework.util.StringUtils;

/**
 * 数据库类型
 *
 */
public enum AbDbType {

    /**
     * MYSQL
     */
    MYSQL("MySQL", "mysql", "MySQL数据库","com.mysql.jdbc.Driver", "jdbc:mysql://主机:3306/数据库名?useUnicode=true&characterEncoding=utf-8"),
    /**
     * MARIADB
     */
    MARIADB("Mariadb", "mariadb", "Mariadb数据库","org.mariadb.jdbc.Driver", "jdbc:mariadb://主机:3306/数据库名?useUnicode=true&characterEncoding=utf-8"),

    /**
     * ORACLE
     */
    ORACLE("Oracle", "oracle", "Oracle数据库","oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@主机:1521:数据库实例"),

    /**
     * SQLSERVER
     */
    SQLSERVER("Microsoft SQL Server", "sqlserver", "SQLServer数据库","com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://主机:1433;databaseName=数据库名"),

    /**
     * POSTGRE
     */
    POSTGRESQL("PostgreSQL", "postgresql", "Postgre数据库","org.postgresql.Driver", "jdbc:postgresql://主机:5432/postgres"),

    /**
     * Kingbase
     */
    KINGBASE("kingbasees", "kingbasees", "人大金仓数据库","com.kingbase8.Driver", "jdbc:kingbase://主机:54321/bpm"),

    /**
     * 达梦数据库
     */
    DM("DM DBMS", "DM", "达梦数据库","dm.jdbc.driver.DmDriver","jdbc:dm://主机:5236/bpm"),

    /**
     * H2 数据库
     */
    H2("H2", "H2", "H2数据库", "org.h2.Driver", "jdbc:h2://路径"),

    /**
     * 未知数据库
     */
    UNKNOW(null, "UNKNOW", "未知数据库",null,null);


    /**
     * 数据库厂商名称
     */
    private final String productName;

    /**
     * 数据库名称
     */
    private final String db;

    /**
     * 数据库描述
     */
    private final String desc;

    /**
     * 驱动类型
     */
    private final String driverClassName;
    /**
     * 默认的url
     */
    private final String defaultUrl;

    AbDbType(String productName, String db, String desc, String driverClassName, String defaultUrl) {
        this.productName = productName;
        this.db = db;
        this.desc = desc;
        this.driverClassName = driverClassName;
        this.defaultUrl = defaultUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getDb() {
        return db;
    }

    public String getDesc() {
        return desc;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    /**
     * 获取数据库类型
     *
     * @param dbType 字符串数据库类型
     * @return 数据库类型
     */
    public static AbDbType getDbType(String dbType) {
        return Arrays.stream(values()).filter(item -> item.getDb().equalsIgnoreCase(dbType)).findFirst().orElse(AbDbType.UNKNOW);

    }

    boolean matchProductName(String productName) {
        return this.productName != null && this.productName.equalsIgnoreCase(productName);
    }

    public boolean equalsAny(AbDbType... any) {
        for (AbDbType target : any) {
            if (equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数据库厂商名称获取数据库类型
     * @param productName product name
     * @return the database driver or {@link #UNKNOW} if not found
     */
    public static AbDbType fromProductName(String productName) {
        if (StringUtils.hasLength(productName)) {
            for (AbDbType candidate : values()) {
                if (candidate.matchProductName(productName)) {
                    return candidate;
                }
            }
        }
        return UNKNOW;
    }
}
