package com.damao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author DM
 * @since 2023/10/07
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@Slf4j
public class VideoApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(VideoApplication.class);
        application.run();
        log.info("服务启动╰(*°▽°*)╯");
    }
}
