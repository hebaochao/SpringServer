package com.it.rabbitmq.client.rpc;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.rpc.RPC_Work_Sender.TEST_RPC_ROUTTING_KEY_MAP1;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class RPC_Work_Receiver extends BaseRabbitmq {


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
    private void configQueue(Channel channel, String queue, boolean durable, boolean exclusive, boolean autoDelete,
                             Map<String, Object> arguments, boolean autoAck, Consumer consumer) throws IOException {
        //声明队列
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
        channel.basicConsume(queue, autoAck, consumer);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        final RPC_Work_Receiver rpc_work_receiver = new RPC_Work_Receiver();
        rpc_work_receiver.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        final Channel channel = rpc_work_receiver.createChannel("rpc_receiver_channel");
        //手动确认，接收完一个再收一个
        channel.basicQos(1);
        rpc_work_receiver.configQueue(channel,TEST_RPC_ROUTTING_KEY_MAP1, false, false, false, null, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println( "rpc_work_receiver  handleDelivery :" + consumerTag + " data :" + new String(body) );
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
                //回复消息
                AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                builder.correlationId(properties.getCorrelationId());
                channel.basicPublish("", properties.getReplyTo(), builder.build(), "reply1 :rpc_work_receiver ! 00000".getBytes());

            }
        });


        final RPC_Work_Receiver rpc_work_receiver1 = new RPC_Work_Receiver();
        rpc_work_receiver1.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        final Channel channel1 = rpc_work_receiver1.createChannel("rpc_receiver_channel");
        //手动确认，接收完一个再收一个
        channel1.basicQos(1);
        rpc_work_receiver1.configQueue(channel,TEST_RPC_ROUTTING_KEY_MAP1, false, false, false, null, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println( "rpc_work_receiver1  handleDelivery :" + consumerTag + " data :" + new String(body) );
                try {
                    Thread.sleep(1000 *5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel1.basicAck(envelope.getDeliveryTag(), false);
                //回复消息
                AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                builder.correlationId(properties.getCorrelationId());
                channel1.basicPublish("", properties.getReplyTo(), builder.build(), "reply1 :rpc_work_receiver1 ! 22222".getBytes());

            }
        });


    }


}
