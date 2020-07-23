package com.it.rabbitmq.client.rpc;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: RPC_Drect_Sender rpc drect sender
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1113:44
 */
public class RPC_Drect_Sender extends BaseRabbitmq {


    /***
     * 配置路由
     * @param durable
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void  configExchange(Channel channel,boolean durable, boolean autoDelete,
                                Map<String, Object> arguments, String exchange, String queue, String routingKey) throws IOException {
        //定义路由
        channel.exchangeDeclare(exchange, "direct", durable, autoDelete, arguments);
        //定义队列
        channel.queueDeclare(queue, durable, false, autoDelete, arguments);
        //绑定队列
        channel.queueBind(queue, exchange, routingKey);
    }

    /***
     *
     * @param exchange       路由名称
     * @param routingKey     路由key
     * @param msgId          数据消息ID/唯一标志
     * @param data           待发送数据
     * @param consumer rpc 调用结果回调/回调结果消息
     * @throws IOException
     */
    public void pushMessage(Channel channel,String exchange, String routingKey, String msgId, byte[] data, Consumer consumer) throws IOException {
        //获取rabbitmq 默认生成的 队列
        String replyQueue = channel.queueDeclare().getQueue();
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.replyTo(replyQueue).correlationId(msgId);
        channel.basicPublish(exchange, routingKey, builder.build(), data);
         channel.basicConsume(replyQueue, true, consumer);
    }


    public static final String TEST_RPC_DRECT_EXCHANGE = "rpc_drect_exchange";
    public static final String TEST_RPC_DRECT_ROUTINGKEY = "rpc_drect_routingkey";

    public static void main(String[] args) throws IOException, TimeoutException {
        RPC_Drect_Sender rpc_drect_sender = new RPC_Drect_Sender();
        rpc_drect_sender.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        Channel channel = rpc_drect_sender.createChannel("test_rpc_sender_channel");
           rpc_drect_sender.configExchange(channel,false, false, null, TEST_RPC_DRECT_EXCHANGE, "rpc_drect_sender_queue", TEST_RPC_DRECT_ROUTINGKEY);
        rpc_drect_sender.pushMessage(channel,TEST_RPC_DRECT_EXCHANGE, TEST_RPC_DRECT_ROUTINGKEY, "123456", "test123466".getBytes(), new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("receiver rpc handle result :" + properties.getClusterId() + " data :" + new String(body));

            }
        });
    }


}
