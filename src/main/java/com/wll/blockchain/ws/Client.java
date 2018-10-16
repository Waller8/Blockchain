package com.wll.blockchain.ws;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wll.blockchain.bean.Block;
import com.wll.blockchain.bean.MsgBean;
import com.wll.blockchain.pojo.NoteBooK;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

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
        NoteBooK book = NoteBooK.getInstance();
        try {
            if (!StringUtils.isEmpty(message)) {
                //解析消息对象
                ObjectMapper objectMapper = new ObjectMapper();
                MsgBean msgBean = objectMapper.readValue(message, MsgBean.class);
                //判断消息类型
                if (msgBean.code == 1) {      // 表示收到的消息是区块链数据.List<Block>
                    //获取数据
                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Block.class);
                    ArrayList<Block> list = objectMapper.readValue(msgBean.msg, javaType);
                    //进行数据对比,判断是否接受对方传递过来的区块链数据
                    book.checkList(list);
                } else if (msgBean.code == 4) {
                    // 交易数据
                    String msg = msgBean.msg;
                    // 添加数据到区块
                    book.addNote(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
