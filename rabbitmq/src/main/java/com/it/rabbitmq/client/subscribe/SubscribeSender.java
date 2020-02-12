package com.it.rabbitmq.client.subscribe;

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
public class SubscribeSender extends BaseRabbitmq {


    /***
     * 配置路由
     * @param durable
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configExchange(boolean durable, boolean autoDelete,
                             Map<String, Object> arguments,String exchange) throws IOException {
        channel.exchangeDeclare(exchange,"fanout",durable,autoDelete,arguments);
    }

    /***
     * 推送消息
     * @param data
     */
    public void pushMessage(String exchange,byte[] data){
        try {
            channel.basicPublish(exchange,"",null,data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



        public static final String TEST_SUBSCRIBE_EXCHANG = "fanout_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        SubscribeSender subscribeSender = new SubscribeSender();
        subscribeSender.connection(IRabbitmq.host,IRabbitmq.port,IRabbitmq.userName,IRabbitmq.passWord,IRabbitmq.virHost);
        subscribeSender.configExchange(true,false,null,TEST_SUBSCRIBE_EXCHANG);
        for (int i = 0;i<10;i++){
            System.out.println(TEST_SUBSCRIBE_EXCHANG+ "send data"+i);
            String msg = "test"+i;
            subscribeSender.pushMessage(TEST_SUBSCRIBE_EXCHANG,msg.getBytes());
        }
    }



}
