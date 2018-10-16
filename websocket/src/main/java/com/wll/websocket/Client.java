package com.wll.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.websocket
 * @Author: wll
 * @CreateTime: 2018-10-11 14:05
 * @Description:
 */
public class Client extends WebSocketClient {

    private String name;

    /**
     * @param serverUri 连接服务器的地址
     * @param name      客户端的名称
     */
    public Client(URI serverUri, String name) {
        super(serverUri);
        this.name = name;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("客户端_" + name + "连接了服务器成功");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("客户端_" + name + "收到了消息,内容是:" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端_" + name + "关闭连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端_" + name + "发生了错误,内容是:" + ex.getMessage());
    }
}
