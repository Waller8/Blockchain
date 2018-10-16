package com.wll.blockchain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wll.blockchain.BlockchainApplication;
import com.wll.blockchain.bean.Block;
import com.wll.blockchain.bean.MsgBean;
import com.wll.blockchain.bean.Transaction;
import com.wll.blockchain.pojo.NoteBooK;
import com.wll.blockchain.ws.Client;
import com.wll.blockchain.ws.Server;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain.controller
 * @Author: wll
 * @CreateTime: 2018-10-09 20:13
 * @Description:
 */
@RestController
public class BlockController {

    private NoteBooK book = NoteBooK.getInstance();

    @PostMapping("/addGenesis")
    public String addGenesis(String genesis) {
        try {
            book.addGenesis(genesis);
            return "success";
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }

    @PostMapping("/addNote")
    public String addNote(Transaction transaction) {
        try {
            System.out.println(transaction);
            //检验交易数据
            if (transaction.verify()) {
                // 包装数据
                MsgBean bean = new MsgBean(4, transaction.getInfom());
                //将交易数据转为字符串
                ObjectMapper objectMapper = new ObjectMapper();
                String msg = objectMapper.writeValueAsString(bean);
                //广播交易数据
                server.broadcast(msg);
                book.addNote(transaction.getInfom());
                return "success";
            } else {
                throw new RuntimeException("校验数据失败");
            }
        } catch (Exception e) {
            return "fail" + e.getMessage();
        }
    }

    @GetMapping("/showData")
    public List<Block> showData() throws InterruptedException {
        Thread.sleep(1000);
        return book.showData();
    }

    @GetMapping("/check")
    public String check() {
        String checked = book.checked();
        if (StringUtils.isEmpty(checked)) {
            return "数据是安全的";
        }
        return checked;
    }


    private HashSet<String> set = new HashSet();

    List<Client> clients = new ArrayList();

    private Server server;

    // Controller创建后立刻调用该方法
    @PostConstruct
    public void init() {
        server = new Server(Integer.parseInt(BlockchainApplication.port) + 1);
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

    @PostMapping("/syncData")
    public String syncData(Transaction transaction) {
        for (Client client : clients) {
            client.send("请把你的数据给我一份");
        }
        return "同步成功";
    }
}
