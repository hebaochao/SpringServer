package com.it.rabbitmq.client.work;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;


import static com.it.rabbitmq.client.work.WorkSender.TEST_WORK_ROUTTING;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class WorkReceiver extends BaseRabbitmq {


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
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final WorkReceiver simpleReceiver = new WorkReceiver();
        simpleReceiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        simpleReceiver.getChannel().basicQos(1);//公平分发 ，设置手动确认消息， qos设置为一，否则 采用自动确认 默认为轮询分发
        simpleReceiver.configQueue(TEST_WORK_ROUTTING, true, false, false, null, false, new DefaultConsumer(simpleReceiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("simpleReceiver 1 handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                simpleReceiver.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        });

        final WorkReceiver simpleReceiver2 = new WorkReceiver();
        simpleReceiver2.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        simpleReceiver2.getChannel().basicQos(1);
        simpleReceiver2.configQueue(TEST_WORK_ROUTTING, true, false, false, null, false, new DefaultConsumer(simpleReceiver2.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("simpleReceiver2  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                simpleReceiver2.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        });


    }


}
