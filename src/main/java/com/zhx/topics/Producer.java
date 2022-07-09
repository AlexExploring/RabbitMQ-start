package com.zhx.topics;
 
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
 
/**
 * direct
 */
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
 
            String message = "hello topic";
            String exchangeName = "topic-exchange";
            String routeKey = "com.order.test.xxx";

            //已经在web端对topic-exchange和 队列进行了绑定,见topic-exchange.png；这里不需要绑定队列

            channel.basicPublish(exchangeName, routeKey, null, message.getBytes());
            System.out.println("消息发送成功!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("发送消息出现异常...");
        } finally {
            // 7: 释放连接关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

