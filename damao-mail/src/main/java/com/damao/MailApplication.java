package com.damao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DM
 * @since 2023/10/07
 */
@SpringBootApplication
@Slf4j
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MailApplication.class);
        application.run();
        log.info("服务启动╰(*°▽°*)╯");
    }
}
