package com.qf.v13userweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author blxf
 * @Date ${Date}
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public TopicExchange initExchange() {
        return new TopicExchange(RabbitMQConstant.USER_EMAIL_EXCHANGE);
    }
}
