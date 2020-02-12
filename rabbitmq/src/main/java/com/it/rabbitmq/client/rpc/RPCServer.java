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
import java.util.concurrent.TimeoutException;

import static com.it.rabbitmq.client.rpc.RPCClient.TEST_RPC_CLIENT_KEY_MAP1;
import static com.it.rabbitmq.client.rpc.RPCClient.TEST_RPC_CLIENT_KEY_MAP2;
import static com.it.rabbitmq.client.rpc.RPC_Work_Sender.TEST_RPC_ROUTTING_KEY_MAP1;

/**
 * @author baochaoh
 * @title: SimpleReceiver
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:31
 */
public class RPCServer extends BaseRabbitmq {


    @Override
    public int connection(String host, int port, String userName, String passWord, String virHost) {
        int result = super.connection(host, port, userName, passWord, virHost);
        if (result > 0) {
            //手动确认模式
            try {
                super.channel.basicQos(1);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 1;
    }


    /***
     * 配置队列
     * @param queue   routtingkey
     * @param callBack 消息回调接口
     * @throws IOException
     */
    private void configQueue(String queue, final IRabbitmqMessageCallBack callBack) throws IOException {
        //声明队列
        channel.queueDeclare(queue, true, false, false, null);
        channel.basicConsume(queue, false, new DefaultConsumer(super.channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                int result = 0;
                if (callBack != null) {
                    result = callBack.handleDelivery(consumerTag, envelope, properties, body);
                }
                //确认消息/退回
                if (result == -1) {//退回消息
                    channel.basicReject(envelope.getDeliveryTag(), false);
                } else {
                    //确认消息已被处理
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }

            }
        });
    }

    /***
     * 回复消息
     * @param properties
     * @param data
     * @throws IOException
     */
    public void replyMessage(AMQP.BasicProperties properties, byte[] data) throws IOException {
        //回复消息
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.correlationId(properties.getCorrelationId());
        super.channel.basicPublish("", properties.getReplyTo(), builder.build(), data);
    }

    public static void main(String[] args) throws IOException {
        final RPCServer rpcServer = new RPCServer();
        rpcServer.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer.configQueue(TEST_RPC_CLIENT_KEY_MAP1, new IRabbitmqMessageCallBack() {
            public int handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                System.out.println("rpcServer  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 2);
                    rpcServer.replyMessage(properties, "rpcServer1 reply ! 00000".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


        final RPCServer rpcServer2 = new RPCServer();
        rpcServer2.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer2.configQueue(TEST_RPC_CLIENT_KEY_MAP1, new IRabbitmqMessageCallBack() {

            public int handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                System.out.println("rpcServer2  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 2);
                    rpcServer2.replyMessage(properties, "rpcServer2  reply ! 00000".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }


}
