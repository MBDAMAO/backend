package com.damao.config;

import com.damao.properties.CosProperties;
import com.damao.utils.CosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CosConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CosUtil aliOssUtil(CosProperties cosProperties) {
        log.info("创建腾讯云oss上传工具类对象ಥ_ಥ");
        return new CosUtil(
                cosProperties.getBaseUrl(),
                cosProperties.getSecretKey(),
                cosProperties.getAccessKey(),
                cosProperties.getRegionName(),
                cosProperties.getBucketName(),
                cosProperties.getFolderPrefix());
    }
}
