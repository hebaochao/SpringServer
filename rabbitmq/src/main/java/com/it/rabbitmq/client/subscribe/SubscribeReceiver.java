package com.it.rabbitmq.client.subscribe;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.subscribe.SubscribeSender.TEST_SUBSCRIBE_EXCHANG;


/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class SubscribeReceiver extends BaseRabbitmq {


    /***
     *
     * 配置队列
     * @param queue
     * @param durable
     * @param exclusive
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configQueue(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer,String exchange) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        //声明路由器，若已声明，则可省略声明路由器，直接绑定路由器即可
        channel.exchangeDeclare(exchange,"fanout",durable,autoDelete,arguments);
        channel.queueBind(queue,exchange,"");
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final SubscribeReceiver subscribeReceiver = new SubscribeReceiver();
        subscribeReceiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        subscribeReceiver.getChannel().basicQos(1);
        subscribeReceiver.configQueue("subscribeReceiver", true, false, false, null, false, new DefaultConsumer(subscribeReceiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("simpleReceiver  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscribeReceiver.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_SUBSCRIBE_EXCHANG);


        //subscribe2


        final SubscribeReceiver subscribeReceiver1 = new SubscribeReceiver();
        subscribeReceiver1.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        subscribeReceiver1.getChannel().basicQos(1);
        subscribeReceiver1.configQueue("subscribeReceiver1", true, false, false, null, false, new DefaultConsumer(subscribeReceiver1.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("subscribeReceiver1 handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscribeReceiver1.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_SUBSCRIBE_EXCHANG);



    }


}
