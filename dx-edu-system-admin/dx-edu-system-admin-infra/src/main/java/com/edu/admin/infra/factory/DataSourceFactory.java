package com.edu.admin.infra.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.edu.admin.infra.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * 数据源仓库
 *
 * @author hjw
 * @since 2025-11-03
 */
@Component
public class DataSourceFactory {

    @Autowired
    private Environment environment;

    /**
     * 创建数据源
     */
    public DataSource createDataSource(DatabaseConfig.DatabaseType databaseType) {
        DruidDataSource dataSource = new DruidDataSource();

        // 公共配置
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        // 根据数据库类型配置
        switch (databaseType) {
            case MYSQL:
                configureMySQL(dataSource);
                break;
            case SQLITE:
                configureSQLite(dataSource);
                break;
            default:
                throw new IllegalArgumentException("不支持的数据库类型: " + databaseType);
        }

        return dataSource;
    }

    private void configureMySQL(DruidDataSource dataSource) {
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(environment.getProperty("app.database.mysql.url",
            "jdbc:mysql://localhost:3306/edu-admin?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"));
        dataSource.setUsername(environment.getProperty("app.database.mysql.username", "root"));
        dataSource.setPassword(environment.getProperty("app.database.mysql.password", "123456"));
        dataSource.setValidationQuery("SELECT 1");
    }

    private void configureSQLite(DruidDataSource dataSource) {
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(environment.getProperty("app.database.sqlite.url", "jdbc:sqlite:edu-admin.db"));
        dataSource.setUsername("");
        dataSource.setPassword("");
        dataSource.setValidationQuery("SELECT 1");

        // SQLite 特定的连接池配置
        dataSource.setMaxActive(10);
        dataSource.setTestOnBorrow(true);
    }

    /**
     * 获取数据库方言
     */
    public String getDialect(DatabaseConfig.DatabaseType databaseType) {
        if (Objects.requireNonNull(databaseType) == DatabaseConfig.DatabaseType.SQLITE) {
            return "org.hibernate.community.dialect.SQLiteDialect";
        }
        return "org.hibernate.dialect.MySQL8Dialect";
    }

    /**
     * 获取DDL自动配置策略
     */
    public String getDdlAuto(DatabaseConfig.DatabaseType databaseType) {
        return switch (databaseType) {
            case MYSQL -> "update";
            // SQLite 不支持某些 DDL 操作
            case SQLITE -> "update";
        };
    }
}
