package com.damao.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "damao.rabbitmq")
@Data
public class RabbitMQProperties {
    private String username;
    private String password;
    private String addresses;
}
