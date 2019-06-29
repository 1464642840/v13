package com.qf.v13searchweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author blxf
 * @Date ${Date}
 */
@Configuration
public class RabbitMqConfig {
    //声明队列
    @Bean
    public Queue initQueue() {
        return new Queue(RabbitMQConstant.PRODUCT_SEARCH_QUEUE_ADDORUPDATE, true, false, false);
    }

    @Bean
    public Queue initQueue2() {
        return new Queue(RabbitMQConstant.PRODUCT_SEARCH_QUEUE_DELETE, true, false, false);
    }

    //声明交换机
    @Bean
    public TopicExchange initExchange() {
        return new TopicExchange(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE);
    }

    //绑定交换机
    @Bean
    public Binding initBindind(Queue initQueue, TopicExchange initExchange) {
        return BindingBuilder.bind(initQueue).to(initExchange).with("product.addOrUpdate");
    }

    @Bean
    public Binding initBindind2(Queue initQueue2, TopicExchange initExchange) {
        return BindingBuilder.bind(initQueue2).to(initExchange).with("product.delete");
    }
}
