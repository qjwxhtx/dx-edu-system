package com.edu.admin.adapter;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 *
 * @author hjw
 * @since 2025-11-03
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Test
    public void test1() {
        log.info("logback的日志信息过来了");
        log.error("logback的错误信息过来了");
    }

}
