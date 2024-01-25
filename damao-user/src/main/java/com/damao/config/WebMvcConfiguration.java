package com.damao.config;

import com.damao.interceptor.AuthorizeInterceptor;
import com.damao.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private AuthorizeInterceptor authorizeInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/registry")
                .excludePathPatterns("/user/login");
    }
    protected void extendMessageConverters (List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器o(*￣▽￣*)ブ");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }
}
