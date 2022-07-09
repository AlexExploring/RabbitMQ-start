package com.zhx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 完整的声明创建方式
 */
public class Producer {

    public static void main(String[] args) {
        //创建连接工厂
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
            //获取连接
            connection = connectionFactory.newConnection("生产者");
            //获取channel
            channel = connection.createChannel();

            //声明交换机
            String exchange = "direct_message_exchange";
            String exchangeType = "direct";
            channel.exchangeDeclare(exchange,exchangeType,true);

            //声明队列
            channel.queueDeclare("direct_message_1",true,false,false,null);
            channel.queueDeclare("direct_message_2",true,false,false,null);
            channel.queueDeclare("direct_message_3",true,false,false,null);

            //绑定队列和交换机的关系
            channel.queueBind("direct_message_1",exchange,"order");
            channel.queueBind("direct_message_2",exchange,"course");
            channel.queueBind("direct_message_3",exchange,"order");

            //准备要发送的消息
            String message = "hello order!";

            channel.basicPublish(exchange, "order", null, message.getBytes());

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

