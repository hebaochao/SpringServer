package com.it.rabbitmq.client.topic;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.topic.TopicSender.TEST_TOPIC_EXCHANG;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class TopicReceiver extends BaseRabbitmq {


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
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer,String exchange,String routingKey) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        //声明路由器，若已声明，则可省略声明路由器，直接绑定路由器即可
        channel.exchangeDeclare(exchange,"topic",durable,autoDelete,arguments);
        channel.queueBind(queue,exchange,routingKey);
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final TopicReceiver topicReceiver = new TopicReceiver();
        topicReceiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        topicReceiver.getChannel().basicQos(1);
        topicReceiver.configQueue("topicReceiver", true, false, false, null, false, new DefaultConsumer(topicReceiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("topicReceiver  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                topicReceiver.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_TOPIC_EXCHANG,"log.#");//监听所有log.XXX的消息



        //subscribe2

        final TopicReceiver topicReceiver1 = new TopicReceiver();
        topicReceiver1.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        topicReceiver1.getChannel().basicQos(1);
        topicReceiver1.configQueue("topicReceiver1", true, false, false, null, false, new DefaultConsumer(topicReceiver1.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("topicReceiver1  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                topicReceiver1.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_TOPIC_EXCHANG,"log.info");//监听所有log.XXX的消息





    }


}
