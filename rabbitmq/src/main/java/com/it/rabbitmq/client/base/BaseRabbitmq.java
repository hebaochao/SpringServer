package com.it.rabbitmq.client.base;

import com.it.rabbitmq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: BaseRabbitmq
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1011:01
 */

public class BaseRabbitmq {


    protected String host;

    protected int port;

    protected String userName;

    protected String passWord;


    protected String virHost;

    protected Connection connection;




    public BaseRabbitmq() {
    }


    /***
     * 配置，连接rabbitmq server
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param virHost
     * @return
     */
    public int connection(String host, int port, String userName, String passWord, String virHost) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
        this.virHost = virHost;
        try {
            this.connection = RabbitmqUtils.getConnection(host, port, userName, passWord, virHost);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }


    /***
     * 创建通道
     * @return
     */
    public Channel createChannel(String tag){
        Channel channel = null;
        try {
            channel = RabbitmqUtils.getChannel(this.connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  channel;
    }





    public void close(){
        try {
            this.connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void  closeChannel(Channel channel){
        try {
            channel.close();
//            this.channelMap.remove(channel);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
