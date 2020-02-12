package com.it.rabbitmq.client.simple;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.simple.SimpleSender.TEST_SIMPEL_QUEUE;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class SimpleReceiver extends BaseRabbitmq {


    /***
     * 配置队列
     * @param queue
     * @param durable
     * @param exclusive
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configQueue(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final SimpleReceiver simpleReceiver = new SimpleReceiver();
        simpleReceiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        // 手动确认消息，一次性接收完所有消息
//        simpleReceiver.configQueue(TEST_SIMPEL_QUEUE, false, false, false, null, false, new DefaultConsumer(simpleReceiver.getChannel()) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                System.out.println("handleDelivery :" + consumerTag + " data :" + new String(body));
//                simpleReceiver.getChannel().basicAck(envelope.getDeliveryTag(), false);
//
//            }
//        });
        //手动确认，接收完一个再收一个
        // 手动确认消息，一次性接收完所有消息
        simpleReceiver.getChannel().basicQos(1);
        simpleReceiver.configQueue(TEST_SIMPEL_QUEUE, false, false, false, null, false, new DefaultConsumer(simpleReceiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                simpleReceiver.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        });



    }


}
