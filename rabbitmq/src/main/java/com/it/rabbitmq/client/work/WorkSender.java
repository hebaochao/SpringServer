package com.it.rabbitmq.client.work;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.it.rabbitmq.constant.IRabbitmq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: SimpleSender 工作队列模式
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:02
 */
public class WorkSender extends BaseRabbitmq {



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


    public static final String TEST_WORK_ROUTTING = "work_routing_1";
    public static void main(String[] args) throws IOException, TimeoutException {
        WorkSender simpleSender = new WorkSender();
        simpleSender.connection(IRabbitmq.host,IRabbitmq.port,IRabbitmq.userName,IRabbitmq.passWord,IRabbitmq.virHost);
        simpleSender.configQueue("work_queue_1",true,false,false,null);
        for (int i = 0;i<10;i++){
            System.out.println("send data"+i);
            String msg = "test"+i;
            simpleSender.pushMessage(TEST_WORK_ROUTTING,msg.getBytes());
        }
    }





}
