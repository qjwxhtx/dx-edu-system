package com.edu.admin.infra.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qianwj
 * @since 2025-10-24
 */

@Configuration
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true", matchIfMissing = false)
public class RocketMQConfig {

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    @Bean
    @ConditionalOnMissingBean
    public RocketMQTemplate rocketMqTemplate() {
        RocketMQTemplate rocketMqTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMqProducer = new DefaultMQProducer();
        defaultMqProducer.setProducerGroup(producerGroup);
        defaultMqProducer.setNamesrvAddr(nameServer);
        rocketMqTemplate.setProducer(defaultMqProducer);
        return rocketMqTemplate;
    }

}
