package com.it.rabbitmq.client.model;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author baochaoh
 * @title: RabbitmqMessage rabbitmq message
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1119:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitmqMessage {


  private String routingKey;

  private  String msgId;

  private  byte[]  data;

  private IRabbitmqMessageCallBack rabbitmqMessageCallBack;





}
