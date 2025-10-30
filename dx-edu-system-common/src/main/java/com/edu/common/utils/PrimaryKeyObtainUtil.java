package com.edu.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrimaryKeyObtainUtil implements CommandLineRunner {

    private static SnowflakeIdWorker snowflakeIdWorker;

    private static boolean initialized = false;

    @Override
    public void run(String... args) throws Exception {
        Long workerId = 1L;
        Long datacenterId = 1L;
        // 解析命令行参数
        for (String arg : args) {
            if (arg.startsWith("--workerId=")) {
                workerId = Long.parseLong(arg.substring("--workerId=".length()));
            } else if (arg.startsWith("--datacenterId=")) {
                datacenterId = Long.parseLong(arg.substring("--datacenterId=".length()));
            }
        }
        log.info("初始化ID生成器，workerId: {}, datacenterId: {}", workerId, datacenterId);
        snowflakeIdWorker = new SnowflakeIdWorker(workerId, datacenterId);
        initialized = true;
    }

    public static Long getNextId() {
        if (!initialized) {
            throw new IllegalStateException("ID生成器未初始化，请检查启动参数");
        }
        return snowflakeIdWorker.nextId();
    }
}
