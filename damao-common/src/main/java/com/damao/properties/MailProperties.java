package com.damao.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "damao.mail")
@Data
public class MailProperties {
    private String host;
    private String username;
    private String password;
}
