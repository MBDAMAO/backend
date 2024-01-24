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
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GatewayApplication.class);
        application.run();
        log.info("服务启动╰(*°▽°*)╯");
    }
}
