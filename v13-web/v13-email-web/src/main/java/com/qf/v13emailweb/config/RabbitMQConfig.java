package com.qf.v13emailweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author blxf
 * @Date ${Date}
 */
@Configuration
public class RabbitMQConfig {
    //声明队列
    @Bean
    public Queue initQueue() {
        return new Queue(RabbitMQConstant.USER_EMAIL_QUEUE, true, false, false);
    }

    @Bean
    public Queue initQueue2() {
        return new Queue(RabbitMQConstant.USER_MSG_QUEUE, true, false, false);
    }

    //声明交换机
    @Bean
    public TopicExchange initExchange() {
        return new TopicExchange(RabbitMQConstant.USER_EMAIL_EXCHANGE);
    }

    //绑定交换机
    @Bean
    public Binding initBindind(@Autowired Queue initQueue, @Autowired TopicExchange initExchange) {
        return BindingBuilder.bind(initQueue).to(initExchange).with("user.sendEamil");
    }

    @Bean
    public Binding initBindind2(@Autowired Queue initQueue2, @Autowired TopicExchange initExchange) {
        return BindingBuilder.bind(initQueue2).to(initExchange).with("user.sendMsg");
    }
}
