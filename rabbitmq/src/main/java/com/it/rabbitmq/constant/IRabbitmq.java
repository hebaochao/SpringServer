package com.it.rabbitmq.constant;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author baochaoh
 * @title: IRabbitmq
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:36
 */
public interface IRabbitmq {


    String host = "127.0.0.1";

    int port = 5672;

    String userName = "guest";

    String passWord = "guest";


    String virHost = "/v22";




}
