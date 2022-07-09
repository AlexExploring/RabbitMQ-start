package com.zhx.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description: 简单模式Simple
 */
public class Producer {


    public static void main(String[] args) {

        // 所有的中间件技术都是基于tcp/ip协议基础之上构建新型的协议规范，只不过rabbitmq遵循的是amqp

        // 1: 创建连接工厂
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
            // 2: 创建连接Connection Rabbitmq为什么是基于channel去处理而不是链接? 长连接----信道channel
            connection = connectionFactory.newConnection("生产者");
            // 3: 通过连接获取通道Channel
            channel = connection.createChannel();
            // 4: 通过通创建交换机，声明队列，绑定关系，路由key,发送消息，和接收消息
            String queueName = "simple_queue";

            /*
             * @params1 队列的名称
             * @params2 是否要持久化durable=false 所谓持久化消息是否存盘，如果false 非持久化 true是持久化?
             *      非持久化会存盘吗？ 会存盘，但是会随重启服务而丢失。
             * @params3 排他性，是否是独占独立
             * @params4 是否自动删除，随着最后一个消费者消息完毕消息以后是否把队列自动删除
             * @params5 携带附属参数
             */
            channel.queueDeclare(queueName, true, false, false, null);
            // 5: 准备消息内容
            String message = "Hello zhx!!!";
            // 6: 发送消息给队列queue
            // @params1: 交换机  @params2 队列、路由key @params 消息的状态控制  @params4 消息主题
            // 面试题：可以存在没有交换机的队列吗？不可能，虽然没有指定交换机但是一定会存在一个默认的交换机。
            channel.basicPublish("", queueName, null, message.getBytes());

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

