package com.it.rabbitmq.client.routing;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.routing.RoutingSender.*;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class RoutingReceiver extends BaseRabbitmq {


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
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer,String exchange,String routingkey) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        //声明路由器，若已声明，则可省略声明路由器，直接绑定路由器即可
        channel.exchangeDeclare(exchange,"direct",durable,autoDelete,arguments);
        channel.queueBind(queue,exchange,routingkey);
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final RoutingReceiver routingReceiver = new RoutingReceiver();
        routingReceiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        routingReceiver.getChannel().basicQos(1);
        routingReceiver.configQueue("routingReceiver1", true, false, false, null, false, new DefaultConsumer(routingReceiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(TEST_ROUTING_EXCHANG+"|"+TEST_ROUTING_ROUTING_KEY1+ "routingReceiver  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                routingReceiver.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_ROUTING_EXCHANG,TEST_ROUTING_ROUTING_KEY1);

         //key2 receiver -1
        final RoutingReceiver routingReceiver2 = new RoutingReceiver();
        routingReceiver2.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        routingReceiver2.getChannel().basicQos(1);
        routingReceiver2.configQueue("routingReceiver2", true, false, false, null, false, new DefaultConsumer(routingReceiver2.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(TEST_ROUTING_EXCHANG+"|"+TEST_ROUTING_ROUTING_KEY2+ "routingReceiver2  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                routingReceiver2.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_ROUTING_EXCHANG,TEST_ROUTING_ROUTING_KEY2);


        //key2 receiver -2
        final RoutingReceiver routingReceiver3 = new RoutingReceiver();
        routingReceiver3.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        routingReceiver3.getChannel().basicQos(1);
        routingReceiver3.configQueue("routingReceiver3", true, false, false, null, false, new DefaultConsumer(routingReceiver3.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(TEST_ROUTING_EXCHANG+"|"+TEST_ROUTING_ROUTING_KEY2+ "routingReceiver3  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                routingReceiver3.getChannel().basicAck(envelope.getDeliveryTag(), false);

            }
        },TEST_ROUTING_EXCHANG,TEST_ROUTING_ROUTING_KEY2);


    }


}
