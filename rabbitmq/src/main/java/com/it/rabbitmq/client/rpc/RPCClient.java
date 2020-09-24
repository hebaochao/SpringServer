package com.it.rabbitmq.client.rpc;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.client.model.IRabbitmqMessageCallBack;
import com.it.rabbitmq.client.model.RabbitmqMessage;
import com.it.rabbitmq.constant.IRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


import java.io.IOException;
import java.util.UUID;

/**
 * @author baochaoh
 * @title: RPCClient  rpc客户端（生产者）
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1119:36
 */
public class RPCClient extends BaseRabbitmq {

    /***
     * 队列名称
     */
    private String queue = "client-" + UUID.randomUUID().toString();

    /***
     * conncetion 只需要调用一次
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param virHost
     * @return
     */
    @Override
    public int connection(String host, int port, String userName, String passWord, String virHost) {
        int result = super.connection(host, port, userName, passWord, virHost);
        if (result < 0) {
            return -1;
        }

        return 1;
    }


    @Override
    public Channel createChannel(String tag) {
        Channel channel = super.createChannel(tag);
        this.initChannel(channel);
        return channel;
    }


    private void initChannel(Channel channel) {
        //定义队列
        try {
            channel.queueDeclare(this.queue, true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pushMessage(String tag, final RabbitmqMessage message) throws IOException {
           Channel channel = super.createChannel(tag);
           this.pushMessage(channel,message);
    }

    /***
     * 推送消息
     * @param message
     * @throws IOException
     */
    public void pushMessage(final Channel channel, final RabbitmqMessage message) throws IOException {
        //获取rabbitmq 默认生成的 队列
        String replyQueue = channel.queueDeclare().getQueue();
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.replyTo(replyQueue).correlationId(message.getMsgId());
        channel.basicPublish("", message.getRoutingKey(), builder.build(), message.getData());
        channel.basicConsume(replyQueue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                if (message.getRabbitmqMessageCallBack() != null) {
                    message.getRabbitmqMessageCallBack().handleDelivery(channel, consumerTag, envelope, properties, body, RPCClient.this);
                    try {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (IOException e) {
                            e.printStackTrace();
                    }
                }
            }
        });
    }


    public static final String TEST_RPC_CLIENT_KEY_MAP1 = "rpc_map1";
    public static final String TEST_RPC_CLIENT_KEY_MAP2 = "rpc_map2";

    public static void main(String[] args) throws IOException {
        //创建RPC客户端
        RPCClient rpcClient = new RPCClient();
        //设置Rabbitmq 连接参数
        rpcClient.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        Channel channel = rpcClient.createChannel(TEST_RPC_CLIENT_KEY_MAP1);
        for (int i = 0; i < 100; i++) {
            //多次发送同一种消息
            RabbitmqMessage rabbitmqMessage = new RabbitmqMessage(TEST_RPC_CLIENT_KEY_MAP1, "A" + i, new String("test" + i).getBytes(), new IRabbitmqMessageCallBack() {
                public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                    //处理回调消息
                    System.out.println("TEST_RPC_ROUTTING_KEY_MAP1 receiver rpc handle result :" + properties.getClusterId() + " data :" + new String(body));
                    return 0;
                }
            });
            //发送消息
            rpcClient.pushMessage(channel, rabbitmqMessage);
        }


        Channel channel1 = rpcClient.createChannel(TEST_RPC_CLIENT_KEY_MAP2);
        for (int i = 0; i < 50; i++) {
            //定义第二种消息
            RabbitmqMessage rabbitmqMessage1 = new RabbitmqMessage(TEST_RPC_CLIENT_KEY_MAP2, "BB", new String("test BB" + i).getBytes(), new IRabbitmqMessageCallBack() {
                public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                    System.out.println("TEST_RPC_ROUTTING_KEY_MAP2 receiver rpc handle result :" + properties.getClusterId() + " data :" + new String(body));
                    return 0;
                }
            });
            rpcClient.pushMessage(channel1, rabbitmqMessage1);
        }


    }

}
