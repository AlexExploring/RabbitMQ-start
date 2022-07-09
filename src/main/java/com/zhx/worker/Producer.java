package com.zhx.worker;
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
        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection("生产者");
            channel = connection.createChannel();

            for (int i = 1; i <= 20; i++) {
                String msg = "学相伴:" + i;
                //没有指定交换机，所以走的是默认的交换机， 如果没有指定交换机，这里的routingKey 就是队列名
                //由于这里的代码没有声明队列，所以要确保队列已经存在
                channel.basicPublish("", "queue_worker", null, msg.getBytes());
            }

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
