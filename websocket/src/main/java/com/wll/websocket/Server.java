package com.wll.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain.bean
 * @Author: wll
 * @CreateTime: 2018-10-11 13:58
 * @Description:
 */
public class Server extends WebSocketServer {
    private int port;

    public Server(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("服务器_" + port + "打开了连接,对方是" + conn.getRemoteSocketAddress().toString());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("服务器_" + port + "关闭了连接,对方是" + conn.getRemoteSocketAddress().toString());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("服务器_" + port + "收到了消息,对方是" + conn.getRemoteSocketAddress().toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("服务器_" + port + "发生了错误,错误是" + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("服务器_" + port + "启动成功");
    }
}
