package com.damao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class UploadApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UploadApplication.class);
        springApplication.run();
    }
}
