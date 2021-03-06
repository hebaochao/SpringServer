package com.it.rabbitmq.client.model;

import com.it.rabbitmq.client.base.BaseRabbitmq;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;


/**
 * @author baochaoh
 * @title: IRabbitmqMessageCallBack
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1213:39
 */
public interface IRabbitmqMessageCallBack {
    /***
     * 处理消息结果
     * @param consumerTag
     * @param envelope
     * @param properties
     * @param body
     * @return
     */
     int handleDelivery(Channel channel, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body, BaseRabbitmq rabbitmq) ;

}
