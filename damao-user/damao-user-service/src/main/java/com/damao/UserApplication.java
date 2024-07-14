package com.damao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author DM
 * @since 2023/10/07
 */
@SpringBootApplication
@EnableCaching
@Slf4j
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UserApplication.class);
        application.run();
        log.info("服务启动╰(*°▽°*)╯");
    }
}
