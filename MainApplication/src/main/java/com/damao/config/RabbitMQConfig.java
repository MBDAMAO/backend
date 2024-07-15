package com.damao.config;

import com.damao.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
//
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Value("${spring.rabbitmq.port}")
//    private int port;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    @Value("${spring.rabbitmq.virtual-host}")
//    private String virtualhost;
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost(virtualhost);
//        //connectionFactory.setPublisherConfirms(true);
//        return connectionFactory;
//    }
//
//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    //必须是prototype类型
//    public RabbitTemplate rabbitTemplate() {
//        return new RabbitTemplate(connectionFactory());
//    }
}
