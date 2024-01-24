package com.damao.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "damao.datasource")
@Data
public class DataSourceProperties {
    private String driver_class_name;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
}
