package com.it.rabbitmq.client.rpc;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.rpc.RPC_Drect_Sender.TEST_RPC_DRECT_EXCHANGE;
import static com.it.rabbitmq.client.rpc.RPC_Drect_Sender.TEST_RPC_DRECT_ROUTINGKEY;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class RPC_Drect_Receiver extends BaseRabbitmq {


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
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer, String exchange, String routingkey) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        //声明路由器，若已声明，则可省略声明路由器，直接绑定路由器即可
        channel.exchangeDeclare(exchange, "direct", durable, autoDelete, arguments);
        channel.queueBind(queue, exchange, routingkey);
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final RPC_Drect_Receiver rpc_drect_receiver = new RPC_Drect_Receiver();
        rpc_drect_receiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        //手动确认，接收完一个再收一个
        rpc_drect_receiver.getChannel().basicQos(1);
        rpc_drect_receiver.configQueue("rpc_drect_receiver", false, false, false, null, false, new DefaultConsumer(rpc_drect_receiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(TEST_RPC_DRECT_EXCHANGE + "|" + TEST_RPC_DRECT_ROUTINGKEY + "routingReceiver  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rpc_drect_receiver.getChannel().basicAck(envelope.getDeliveryTag(), false);
                //回复消息
                AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                builder.correlationId(properties.getCorrelationId());
                rpc_drect_receiver.getChannel().basicPublish("", properties.getReplyTo(), builder.build(), "reply1 :rpc_drect_receiver !".getBytes());

            }
        }, TEST_RPC_DRECT_EXCHANGE, TEST_RPC_DRECT_ROUTINGKEY);


    }


}
