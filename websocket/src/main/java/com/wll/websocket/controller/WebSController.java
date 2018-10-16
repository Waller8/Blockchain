package com.wll.websocket.controller;

import com.wll.websocket.Client;
import com.wll.websocket.Server;
import com.wll.websocket.WebsocketApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.websocket.controller
 * @Author: wll
 * @CreateTime: 2018-10-11 14:30
 * @Description:
 */
@RestController
public class WebSController {

    private Server server;
    private HashSet<String> set = new HashSet();

    // Controller创建后立刻调用该方法
    @PostConstruct
    public void init() {
        server = new Server(Integer.parseInt(WebsocketApplication.port) + 1);
        server.start();
    }

    //注册节点
    @RequestMapping("/regist")
    public String regist(String port) {
        set.add(port);
        return "注册成功" + port;
    }

    //连接
    @RequestMapping("/conn")
    public String conn() {
//遍历集合连接其他服务器
        try {
            for (String node : set) {
                URI uri = new URI("ws://localhost:" + node);
                Client client = new Client(uri, node);
                System.out.println("执行了连接~~~");
                client.connect();
            }
            return "节点连接成功";
        } catch (URISyntaxException e) {
            return "连接失败:" + e.getMessage();
        }
    }

    //广播
    @RequestMapping("/broadcast")
    public String broadcast(String msg) {
        server.broadcast(msg);
        return "广播成功";
    }

}
