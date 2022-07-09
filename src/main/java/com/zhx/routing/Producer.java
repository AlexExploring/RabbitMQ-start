package com.zhx.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("121.4.91.181");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");//rabbitmq登录的账号
        connectionFactory.setPassword("admin");//rabbitmq登录的密码
        connectionFactory.setVirtualHost("/");
        //springboot ---rabbitmq
        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection("生产者");
            channel = connection.createChannel();
            String exchange = "direct-exchange";
            String message = "hello direct message";
            String routeKey = "email";

            //已经在web端对fanout-exchang和 队列进行了绑定,见fanout-exchange.png；这里不需要绑定队列

            channel.basicPublish(exchange, routeKey, null, message.getBytes());

            System.out.println("消息发送成功!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 7: 关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // 8: 关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

