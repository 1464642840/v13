package com.qf.simple;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import sun.applet.Main;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author blxf
 * @Date ${Date}
 */
public class Producer {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("120.77.154.118");
        factory.setPort(5672);
        factory.setVirtualHost("/blxf");
        factory.setUsername("blxf");
        factory.setPassword("457862154");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("simply_queue", false, false, false, null);
            channel.basicPublish("", "simply_queue", null, "cc".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
