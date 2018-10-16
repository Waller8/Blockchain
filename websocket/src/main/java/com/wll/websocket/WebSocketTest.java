package com.wll.websocket;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.websocket
 * @Author: wll
 * @CreateTime: 2018-10-11 14:11
 * @Description: 测试类
 */
public class WebSocketTest {
    public static void main(String[] args) {

        try {
            //创建服务器
            Server server = new Server(8000);
            server.start();//开启服务器
            //指定服务器地址
            URI uri = new URI("ws://localhost:8000");
            //创建客户端
            Client client1 = new Client(uri, "1111");
            Client client2 = new Client(uri, "2222");
            //客户端连接服务器
            client1.connect();
            client2.connect();

            //避免连接不成功,就发送消息,所以加上延时
            Thread.sleep(2000);
            //服务器发送广播
            server.broadcast("这是服务器的广播");

            //客户端发消息
            client1.send("这是客户端1的消息");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
