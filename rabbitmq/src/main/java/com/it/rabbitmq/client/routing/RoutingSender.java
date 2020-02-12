package com.it.rabbitmq.client.routing;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: SubscribeSender  路由模式，路由本身不具备存储功能，若发送的路由器，没有对应的消息队列绑定供给路由，则消息会被丢弃.发送者将消息推送至路由器中，根据routingkey 区分不同的消息，消费者根据绑定不同的routingkey，达到消息路由的功能。PS：多个消费者监听相同的routingkey ,则他们均会收到消息
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1110:12
 */
public class RoutingSender extends BaseRabbitmq {


    /***
     * 配置路由
     * @param durable
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configExchange(boolean durable, boolean autoDelete,
                                Map<String, Object> arguments, String exchange) throws IOException {
        channel.exchangeDeclare(exchange, "direct", durable, autoDelete, arguments);
    }

    /***
     * 推送消息
     * @param data
     */
    public void pushMessage(String exchange, String routingKey, byte[] data) {
        try {
            channel.basicPublish(exchange, routingKey, null, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static final String TEST_ROUTING_EXCHANG = "routing_exchange";
    public static final String TEST_ROUTING_ROUTING_KEY1 = "routing_exchange_key1";
    public static final String TEST_ROUTING_ROUTING_KEY2 = "routing_exchange_key2";
    public static final String TEST_ROUTING_ROUTING_KEY3 = "routing_exchange_key3";

    public static void main(String[] args) throws IOException, TimeoutException {
        RoutingSender subscribeSender = new RoutingSender();
        subscribeSender.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        subscribeSender.configExchange(true, false, null, TEST_ROUTING_EXCHANG);
        String msg = "test" + System.currentTimeMillis();
        subscribeSender.pushMessage(TEST_ROUTING_EXCHANG, TEST_ROUTING_ROUTING_KEY1, msg.getBytes());
        subscribeSender.pushMessage(TEST_ROUTING_EXCHANG, TEST_ROUTING_ROUTING_KEY2, msg.getBytes());
        subscribeSender.pushMessage(TEST_ROUTING_EXCHANG, TEST_ROUTING_ROUTING_KEY3, msg.getBytes());
        subscribeSender.pushMessage(TEST_ROUTING_EXCHANG, TEST_ROUTING_ROUTING_KEY3, msg.getBytes());
    }


}
