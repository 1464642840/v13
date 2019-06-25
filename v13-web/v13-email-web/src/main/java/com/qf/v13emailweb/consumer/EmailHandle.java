package com.qf.v13emailweb.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.entity.TUser;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author blxf
 * @Date ${Date}
 */
@Component
public class EmailHandle {
    @Reference
    private IEmailService emailService;

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.USER_EMAIL_QUEUE)

    public void activeUser(TUser user) {
        emailService.sendEmail(user);

    }
}
