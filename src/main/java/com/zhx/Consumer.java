package com.zhx;

import com.rabbitmq.client.*;
import java.io.IOException;

/**
 * 公共的消费者
 */
public class Consumer {

    public static void main(String[] args) {
        // 启动三个线程去执行
        new Thread(runnable, "fanout_queue1").start();
        new Thread(runnable, "fanout_queue2").start();
        new Thread(runnable, "fanout_queue3").start();
    }

    private static Runnable runnable = new Runnable() {
        public void run() {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("121.4.91.181");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("admin");//rabbitmq登录的账号
            connectionFactory.setPassword("admin");//rabbitmq登录的密码
            connectionFactory.setVirtualHost("/");
            Connection connection = null;
            Channel channel = null;
            final String queueName = Thread.currentThread().getName();

            try {
                connection = connectionFactory.newConnection("生产者");
                channel = connection.createChannel();

                Channel finalChannel = channel;
                finalChannel.basicConsume(queueName, true, new DeliverCallback() {
                    @Override
                    public void handle(String s, Delivery delivery) throws IOException {
                        System.out.println(queueName + "：收到消息是：" + new String(delivery.getBody(), "UTF-8"));
                    }
                }, new CancelCallback() {
                    @Override
                    public void handle(String s) throws IOException {
                    }
                });

                System.out.println(queueName + "：开始接受消息");
                System.in.read();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("发送消息出现异常...");
            } finally {
                if (channel != null && channel.isOpen()) {
                    try {
                        channel.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (connection != null && connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    };
}
