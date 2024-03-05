package com.damao;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.damao.mapper")
@Slf4j
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CommentApplication.class);
        application.run();
        log.info("服务启动╰(*°▽°*)╯");
    }
}
