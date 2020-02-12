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

/**
 * @author baochaoh
 * @title: RPC_Drect_Sender rpc drect sender
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1113:44
 */
public class RPC_Work_Sender extends BaseRabbitmq {


    /***
     * 配置路由
     * @param durable
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configExchange(boolean durable, boolean autoDelete,
                                Map<String, Object> arguments,  String queue) throws IOException {
        //定义队列
        channel.queueDeclare(queue, durable, false, autoDelete, arguments);
    }

    /***
     *
     * @param routingKey     路由key
     * @param msgId          数据消息ID/唯一标志
     * @param data           待发送数据
     * @param consumer rpc 调用结果回调/回调结果消息
     * @throws IOException
     */
    public void pushMessage( String routingKey, String msgId, byte[] data, Consumer consumer) throws IOException {
        //获取rabbitmq 默认生成的 队列
        String replyQueue = channel.queueDeclare().getQueue();
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.replyTo(replyQueue).correlationId(msgId);
        super.channel.basicPublish("", routingKey, builder.build(), data);
        super.channel.basicConsume(replyQueue, true, consumer);
    }


    public static final String TEST_RPC_ROUTTING_KEY_MAP1 = "map1";
    public static final String TEST_RPC_ROUTTING_KEY_MAP2 = "map2";

    public static void main(String[] args) throws IOException, TimeoutException {
        RPC_Work_Sender rpc_work_sender = new RPC_Work_Sender();
        rpc_work_sender.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpc_work_sender.configExchange(false, false, null,  "rpc_work_sender_queue");
        for (int i=0;i<10;i++){
            rpc_work_sender.pushMessage(TEST_RPC_ROUTTING_KEY_MAP1, "111", new String("test"+i).getBytes(), new DefaultConsumer(rpc_work_sender.getChannel()) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("TEST_RPC_ROUTTING_KEY_MAP1 receiver rpc handle result :" + properties.getClusterId() + " data :" + new String(body));
                }
            });

        }

        rpc_work_sender.pushMessage(TEST_RPC_ROUTTING_KEY_MAP2, "888", "hrllo123466".getBytes(), new DefaultConsumer(rpc_work_sender.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("TEST_RPC_ROUTTING_KEY_MAP2  receiver rpc handle result :" + properties.getClusterId() + " data :" + new String(body));

            }
        });


    }


}
