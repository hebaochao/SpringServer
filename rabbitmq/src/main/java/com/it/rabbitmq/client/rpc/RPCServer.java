package com.it.rabbitmq.client.rpc;


import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.client.model.IRabbitmqMessageCallBack;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

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
        return super.connection(host, port, userName, passWord, virHost);
    }


    /***
     * 自动配置新的channel
     * @param queue
     * @param callBack
     */
    public void configQueue( String tag,String queue, final IRabbitmqMessageCallBack callBack)  {
         Channel channel = super.createChannel(tag);
         this.configQueue(channel,queue,callBack);
    }
    /***
     * 配置队列
     * @param queue   routtingkey
     * @param callBack 消息回调接口
     * @throws IOException
     */
    public void configQueue(Channel channel, String queue, final IRabbitmqMessageCallBack callBack)  {
        //定义通道
        try {
            //手动确认模式
            channel.basicQos(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //声明队列
        try {
            channel.queueDeclare(queue, true, false, false, null);
            channel.basicConsume(queue, false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    int result = 0;
                    if (callBack != null) {
                        result = callBack.handleDelivery(channel,consumerTag, envelope, properties, body,RPCServer.this);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 回复消息
     * @param properties
     * @param data
     * @throws IOException
     */
    public void replyMessage(Channel channel, AMQP.BasicProperties properties, byte[] data) {
        //回复消息
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.correlationId(properties.getCorrelationId());
        try {
            channel.basicPublish("", properties.getReplyTo(), builder.build(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void replyMessage(Channel channel, String replyQueue, String correlationId, byte[] data) {
        //回复消息
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.correlationId(correlationId);
        try {
            channel.basicPublish("", replyQueue, builder.build(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        final RPCServer rpcServer = new RPCServer();
        rpcServer.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer.configQueue(TEST_RPC_CLIENT_KEY_MAP1,TEST_RPC_CLIENT_KEY_MAP1, new IRabbitmqMessageCallBack() {
            @Override
            public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                System.out.println("rpcServer  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 2);
                    RPCServer rpcServer1 = (RPCServer) rabbitmq;
                    rpcServer1.replyMessage(channel,properties, "rpcServer1 reply ! 00000".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


        final RPCServer rpcServer2 = new RPCServer();
        rpcServer2.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer2.configQueue(TEST_RPC_CLIENT_KEY_MAP1, TEST_RPC_CLIENT_KEY_MAP1,new IRabbitmqMessageCallBack() {
            @Override
            public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                System.out.println("rpcServer2  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 5);
                    RPCServer rpcServer1 = (RPCServer) rabbitmq;
                    rpcServer1.replyMessage(channel,properties, "rpcServer2  reply ! 22222".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


        final RPCServer rpcServer3 = new RPCServer();
        rpcServer3.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer3.configQueue(TEST_RPC_CLIENT_KEY_MAP1, TEST_RPC_CLIENT_KEY_MAP1,new IRabbitmqMessageCallBack() {
            @Override
            public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                System.out.println("rpcServer3  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 10);
                    RPCServer rpcServer1 = (RPCServer) rabbitmq;
                    rpcServer1.replyMessage(channel,properties, "rpcServer3  reply ! 33333".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


        final RPCServer rpcServer4 = new RPCServer();
        rpcServer4.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer4.configQueue(TEST_RPC_CLIENT_KEY_MAP2, TEST_RPC_CLIENT_KEY_MAP2,new IRabbitmqMessageCallBack() {
            @Override
            public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                System.out.println("rpcServer4  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 2);
                    RPCServer rpcServer1 = (RPCServer) rabbitmq;
                    rpcServer1.replyMessage(channel,properties, "rpcServer4  reply BBBB ! 44444".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


        final RPCServer rpcServer5 = new RPCServer();
        rpcServer5.connection(IRabbitmq.host, IRabbitmq.port, IRabbitmq.userName, IRabbitmq.passWord, IRabbitmq.virHost);
        rpcServer5.configQueue(TEST_RPC_CLIENT_KEY_MAP2, TEST_RPC_CLIENT_KEY_MAP2,new IRabbitmqMessageCallBack() {
            @Override
            public int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) {
                System.out.println("rpcServer5  handleDelivery :" + consumerTag + " data :" + new String(body));
                try {
                    Thread.sleep(1000 * 2);
                    RPCServer rpcServer1 = (RPCServer) rabbitmq;
                    rpcServer1.replyMessage(channel,properties, "rpcServer5  reply BBBB ! 55555".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


    }


}
