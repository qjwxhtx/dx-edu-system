package com.edu.perm.dapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@EntityScan(basePackages = "com.edu.perm.infra.bean.entity")
@EnableJpaRepositories(basePackages = "com.edu.perm.infra.repositories")
public class PermAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermAdapterApplication.class, args);
        log.info("Perm Service started Successful");
    }

}
