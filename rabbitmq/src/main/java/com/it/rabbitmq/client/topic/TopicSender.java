package com.it.rabbitmq.client.topic;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: SubscribeSender  订阅模式，发送者将消息推送至路由器中，由多个消费者监听该路由器，则每个消费者都会收到同一个消息，达到分发/群发效果
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1110:12
 */
public class TopicSender extends BaseRabbitmq {


    /***
     * 配置路由
     * @param durable
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configExchange(boolean durable, boolean autoDelete,
                             Map<String, Object> arguments,String exchange) throws IOException {
        channel.exchangeDeclare(exchange,"topic",durable,autoDelete,arguments);
    }

    /***
     * 推送消息
     * @param data
     */
    public void pushMessage(String exchange,String routingKey,byte[] data){
        try {
            channel.basicPublish(exchange,routingKey,null,data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



        public static final String TEST_TOPIC_EXCHANG = "topic_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        TopicSender subscribeSender = new TopicSender();
        subscribeSender.connection(IRabbitmq.host,IRabbitmq.port,IRabbitmq.userName,IRabbitmq.passWord,IRabbitmq.virHost);
        subscribeSender.configExchange(true,false,null,TEST_TOPIC_EXCHANG);
        subscribeSender.pushMessage(TEST_TOPIC_EXCHANG,"log.info","test1".getBytes());
        subscribeSender.pushMessage(TEST_TOPIC_EXCHANG,"log.error","test2".getBytes());
    }



}
