package com.edu.admin.infra.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * mongodb配置类: mongodb集成、mongodb事务
 *
 * @author hjw
 * @since 2025-10-24
 */
@Configuration
@ConditionalOnProperty(prefix = "component.enabled", name = "mongodb", havingValue = "true", matchIfMissing = false)
@EnableMongoRepositories(basePackages = "com.edu.admin.infra.repositories.mongo")
public class MongoDbConfig {

    @Bean
    @ConditionalOnMissingBean
    public MongoClient mongoClient(Environment env) {
        String uri = env.getProperty("spring.data.mongodb.uri", "mongodb://localhost:27017/test");
        return MongoClients.create(uri);
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoTemplate mongoTemplate(Environment env, MongoClient mongoClient) {
        String database = env.getProperty("spring.data.mongodb.database", "test");
        return new MongoTemplate(mongoClient, database);
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoDatabaseFactory mongoDatabaseFactory(Environment env, MongoClient mongoClient) {
        String database = env.getProperty("spring.data.mongodb.database", "test");
        return new SimpleMongoClientDatabaseFactory(mongoClient, database);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.data.mongodb", name = "transaction", havingValue = "true",
        matchIfMissing = false)
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

}
