package com.wll.blockchain.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wll.blockchain.bean.Block;
import com.wll.blockchain.bean.MsgBean;
import com.wll.blockchain.pojo.NoteBooK;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.List;

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
        System.out.println("服务器_" + port + "收到了消息,对方是" + conn.getRemoteSocketAddress().toString() + "_消息内容是:" + message);
        if ("请把你的数据给我一份".equals(message)) {
            //获取本地区块链数据
            NoteBooK notebook = NoteBooK.getInstance();
            List<Block> list = notebook.showData();
            //封装数据
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String chain = objectMapper.writeValueAsString(list);
                MsgBean msgBean = new MsgBean(1, chain);
                String msg = objectMapper.writeValueAsString(msgBean);
                //广播数据
                broadcast(msg);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
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
