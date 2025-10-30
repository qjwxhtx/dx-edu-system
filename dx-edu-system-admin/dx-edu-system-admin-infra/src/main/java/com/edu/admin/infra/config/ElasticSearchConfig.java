package com.edu.admin.infra.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ElasticSearchClient配置类
 *
 * @author zhouqingrui
 * @since 2025-10-22
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(RestHighLevelClient.class)
@ConditionalOnProperty(
        prefix = "es",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ElasticSearchConfig {

    @Value("${es.clients:}")
    private String hosts;

    @Value("${es.scheme:http}")
    private String scheme;

    @Value("${es.user:elastic}")
    private String esUser;

    @Value("${es.password:123456}")
    private String esPassword;


    @Bean
    public RestHighLevelClient restHighLevelClient() {
        log.info("init esClient--------");
        log.info("hosts:{}", hosts);
        List<String> collect = Arrays.stream(hosts.split(",")).collect(Collectors.toList());
        List<HttpHost> httpHostList = new ArrayList<>();
        for (String host : collect) {
            String[] ipAndPort = host.split(":");
            httpHostList.add(new HttpHost(ipAndPort[0], Integer.valueOf(ipAndPort[1]), scheme));
        }
        log.info("hosts:{}", (Object) httpHostList.toArray(new HttpHost[0]));
        return new RestHighLevelClient(
                RestClient.builder(
                                httpHostList.toArray(new HttpHost[0]))
                        .setHttpClientConfigCallback(httpClientBuilder -> {
                            // 设置用户名密码进行基本认证
                            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                            credentialsProvider.setCredentials(AuthScope.ANY,
                                    new UsernamePasswordCredentials(esUser, esPassword));

                            // 设置认证信息到请求配置
                            return httpClientBuilder
                                    .setDefaultCredentialsProvider(credentialsProvider);
                        })
        );
    }

}
