package com.mirror.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@Slf4j
public class RabbitConfigureation {
    @Resource
    private CachingConnectionFactory connectionFactory;

    @Resource
    StringRedisTemplate template;

    @Bean(name = "listenerContainer") //监听管道设置
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(50);   //将PrefetchCount设定为1表示一次只能取一个
        return factory;
    }

    @Bean("directExchange")  //定义交换机Bean，可以很多个
    public Exchange exchange(){
        return ExchangeBuilder.directExchange("ticketOrder.direct").build();
    }

    @Bean("directorderQueue")     //定义消息队列
    public Queue queue(){
        return QueueBuilder
                .durable("ticketOrder")  //非持久化类型
                .build();
    }

    @Bean("directbinding")
    public Binding binding(@Qualifier("directExchange") Exchange exchange,
                           @Qualifier("directorderQueue") Queue queue){
        //将我们刚刚定义的交换机和队列进行绑定
        return BindingBuilder
                .bind(queue)   //绑定队列
                .to(exchange)  //到交换机
                .with("my-ticketOrder")   //使用自定义的routingKey
                .noargs();
    }


    @Bean("jacksonConverter")   //直接创建一个用于JSON转换的Bean
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if(!ack){
                log.error("消息:{}投递失败",correlationData);
            }
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            template.opsForList().leftPush("porducer_failed", String.valueOf(returnedMessage.getMessage()));
            log.error("消息:{}进入队列失败",returnedMessage.getMessage());
        });

        return rabbitTemplate;
    }
}
