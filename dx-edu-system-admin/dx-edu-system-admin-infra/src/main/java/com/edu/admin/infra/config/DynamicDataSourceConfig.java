package com.edu.admin.infra.config;

import com.edu.admin.infra.factory.DataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置
 *
 * @author hjw
 * @since 2025-11-03
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.edu.admin.infra.bean.entity")
@EnableJpaRepositories(basePackages = "com.edu.admin.infra.repositories",
    entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DynamicDataSourceConfig {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    /**
     * 根据配置创建数据源
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        DatabaseConfig.DatabaseType databaseType = DatabaseConfig.DatabaseType.fromValue(databaseConfig.getType());
        log.info("初始化数据源，类型: {}", databaseType);
        return dataSourceFactory.createDataSource(databaseType);
    }

    /**
     * 实体管理器工厂
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        DatabaseConfig.DatabaseType databaseType = DatabaseConfig.DatabaseType.fromValue(databaseConfig.getType());

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.edu.admin.infra.bean.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dataSourceFactory.getDialect(databaseType));
        properties.put("hibernate.hbm2ddl.auto", dataSourceFactory.getDdlAuto(databaseType));
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.jdbc.batch_size", 20);

        // 根据数据库类型调整配置
        if (databaseType == DatabaseConfig.DatabaseType.SQLITE) {
            properties.put("hibernate.hbm2ddl.auto", "update"); // SQLite 只支持 update
            properties.put("hibernate.jdbc.batch_size", 10); // SQLite 批处理较小
        }

        em.setJpaPropertyMap(properties);
        return em;
    }

    /**
     * 事务管理器
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

}
