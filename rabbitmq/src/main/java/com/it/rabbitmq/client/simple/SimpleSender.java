package com.it.rabbitmq.client.simple;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: SimpleSender 简单队列模式  ,P->Q->C ，一个生产则一个消费者
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:02
 */
public class SimpleSender extends BaseRabbitmq {



    /***
     * 配置队列
     * @param queue
     * @param durable
     * @param exclusive
     * @param autoDelete
     * @param arguments
     * @throws IOException
     */
    private void configQueue(String queue, boolean durable, boolean exclusive, boolean autoDelete,
                             Map<String, Object> arguments) throws IOException {
        //声明队列
        channel.queueDeclare(queue,durable,exclusive,autoDelete,arguments);
    }

    /***
     * 推送消息
     * @param queue
     * @param data
     */
    public void pushMessage(String queue,byte[] data){
        try {
            channel.basicPublish("",queue,null,data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static final String TEST_SIMPEL_QUEUE = "simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        SimpleSender simpleSender = new SimpleSender();
        simpleSender.connection(IRabbitmq.host,IRabbitmq.port,IRabbitmq.userName,IRabbitmq.passWord,IRabbitmq.virHost);
        simpleSender.configQueue(TEST_SIMPEL_QUEUE,false,false,false,null);
        simpleSender.pushMessage(TEST_SIMPEL_QUEUE,"test1".getBytes());
        simpleSender.pushMessage(TEST_SIMPEL_QUEUE,"test2".getBytes());
    }





}
