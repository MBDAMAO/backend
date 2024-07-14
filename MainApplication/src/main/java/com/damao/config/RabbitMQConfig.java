package com.damao.config;

import com.damao.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     *  配置RabbitMQ的JSON消息转换器,发送时自动检查是否注册
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange getDirectExchange(){
        return new DirectExchange(RabbitMQConstant.EXCHANGE);
    }

    @Bean
    public Queue getQueue() {
        return new Queue(RabbitMQConstant.QUEUE);
    }

    @Bean
    public Queue deadQueue() {
        return new Queue(RabbitMQConstant.DEAD_MEG_QUEUE);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(RabbitMQConstant.DEAD_MEG_EXCHANGE);
    }

    @Bean
    public Binding bind() {
        return BindingBuilder.bind(getQueue()).to(getDirectExchange()).with("123");
    }

    @Bean
    public Binding deadBind() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("114514");
    }
}
