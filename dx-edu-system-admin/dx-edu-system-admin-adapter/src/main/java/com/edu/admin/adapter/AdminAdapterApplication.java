package com.edu.admin.adapter;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.edu.admin"},
    exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class, RocketMQAutoConfiguration.class})
public class AdminAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminAdapterApplication.class, args);
        log.info("Admin Service started successfully!");
    }

}
