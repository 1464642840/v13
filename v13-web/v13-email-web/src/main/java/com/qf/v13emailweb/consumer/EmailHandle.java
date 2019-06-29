package com.qf.v13emailweb.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.qcloudsms.httpclient.HTTPException;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TException;
import com.qf.v13.entity.TUser;
import com.rabbitmq.client.Channel;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

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
        try {
            emailService.sendEmail(user);
        } catch (Exception e) {
            e.printStackTrace();
            TException exception = new TException();
            exception.setCreateTime(new Date());
            exception.setReplyTimes(0);
            exception.setTo(user.getEmail());
            exception.setType("email");
            exception.setErrorMessage(e.getMessage());
            System.out.println(e.getMessage());
            exception.setCentent(user.getActiveCode());
            emailService.addOneException(exception);

        }

    }

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.USER_MSG_QUEUE)
    public void sendMsg(Map data) {
        try {
            emailService.sendMsg(data);
        } catch (Exception e) {
            System.out.println("发送短信异常");
            e.printStackTrace();
        }

    }
}
