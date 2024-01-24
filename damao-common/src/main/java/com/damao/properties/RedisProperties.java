package com.damao.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "damao.redis")
@Data
public class RedisProperties {
    private String host;
    private String port;
    private String password;
}
