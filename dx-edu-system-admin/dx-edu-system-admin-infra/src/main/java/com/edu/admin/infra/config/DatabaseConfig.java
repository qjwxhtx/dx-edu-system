package com.edu.admin.infra.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置
 *
 * @author hjw
 * @since 2025-11-03
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.database")
public class DatabaseConfig {
    /**
     * 数据库类型: mysql 或 sqlite
     */
    private String type = "mysql";

    /**
     * 是否在启动时初始化数据
     */
    private boolean initialize = false;

    @Getter
    public enum DatabaseType {
        MYSQL("mysql"), SQLITE("sqlite");

        private final String value;

        DatabaseType(String value) {
            this.value = value;
        }

        public static DatabaseType fromValue(String value) {
            for (DatabaseType type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("不支持的数据库类型: " + value);
        }
    }
}
