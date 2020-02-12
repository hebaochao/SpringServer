package com.it.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author baochaoh
 * @title: RabbitmqUtils
 * @projectName SpringServer
 * @description: TODO
 * @date 2020/2/1010:51
 */
public class RabbitmqUtils {

    /***
     * 获取一个新的rabbitmq 服务连接
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param vriHost
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection(String host,int port,String  userName,String passWord,String vriHost) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(userName);
        factory.setPassword(passWord);
        factory.setVirtualHost(vriHost);
        Connection connection =  factory.newConnection();
        return  connection;
    }

    /***
     * 获取channel
     * @param connection
     * @return
     * @throws IOException
     */
    public  static Channel getChannel(Connection connection) throws IOException {
          return connection.createChannel();
    }

    /***
     * 关闭连接和channel
     * @param connection
     * @param channel
     */
    public static  void  close(Connection connection,Channel channel){
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }














}
