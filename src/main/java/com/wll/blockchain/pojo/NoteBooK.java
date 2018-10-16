package com.wll.blockchain.pojo;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wll.blockchain.bean.Block;
import com.wll.blockchain.utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain
 * @Author: wll
 * @CreateTime: 2018-10-09 19:39
 * @Description: 账本
 */
public class NoteBooK {
    private List<Block> list = new ArrayList<>();

    //构造函数
    //加载本地数据
    //使NoteBook变为单例模式
    private NoteBooK() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("a.json");
            // 判断文件是否存在
            if (file.exists() && file.length() > 0) {
                //  如果文件存在,读取之前的数据
                JavaType javatype = objectMapper.getTypeFactory().constructParametricType(List.class, Block.class);
                list = objectMapper.readValue(file, javatype);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static volatile NoteBooK instance;

    //单例模式  饿汉式
    public static NoteBooK getInstance() {
        if (instance == null) {
            synchronized (NoteBooK.class) {
                instance = new NoteBooK();
            }
        }
        return instance;
    }

    //添加封面 = 创世区块   添加封面时,账本必须是全新的
    public void addGenesis(String genesis) {
        if (list.size() > 0) {
            throw new RuntimeException("添加封面时,账本必须是全新的");
        }
        //创世区块前面没有区块,哈希值给定为64个0
        String preHash = "0000000000000000000000000000000000000000000000000000000000000000";
        int nonce = mining(genesis, preHash);
        list.add(new Block(
                list.size() + 1,  //id
                genesis,    //content
                //传入的参数必须要跟挖矿时参数一一对应起来,顺序不能乱
                HashUtils.sha256(nonce + genesis + preHash),
                nonce,
                preHash
        ));
        saveToDisk();
    }

    //添加交易记录 = 普通区块  添加交易记录时,封面必须存在
    public void addNote(String note) {
        if (list.size() < 1) {
            throw new RuntimeException(" 添加交易记录时,封面必须存在");
        }
        //获取前一个区块
        Block prioBlock = list.get(list.size() - 1);
        //获取前一个区块的hash值
        String prioBlockHash = prioBlock.hash;
        int nonce = mining(note, prioBlockHash);
        list.add(new Block(
                list.size() + 1,  //id
                note,    //content
                //传入的参数必须要跟挖矿时参数一一对应起来,顺序不能乱
                HashUtils.sha256(nonce + note + prioBlockHash),
                nonce,
                prioBlockHash
        ));
        saveToDisk();
    }

    //展示数据
    public List<Block> showData() {
        return list;
    }

    //保存到本地硬盘
    public void saveToDisk() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("a.json");
            objectMapper.writeValue(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //校验数据的方法()
    public String checked() {
        StringBuilder sb = new StringBuilder();
        for (Block block : list) {
            //获取内容
            String getContent = block.content;
            //根据内容获取hash值
            String getHash = HashUtils.sha256(getContent);
            //比对hash,如果不同说明数据被改动
            if (!getHash.equals(block.hash)) {
                sb.append("id为" + block.id + "数据可能被改动,请注意防范黑客<br/>");
            }
        }
        return sb.toString();
    }

    //挖矿的方法
    // 挖矿的过程就是在进行数学运算
    // 求取一个以特定规则开头的hash值
    // 得到的i值就是工作量证明
    // 求i值的过程就叫做挖矿
    private int mining(String content, String prioBlockHash) {
        // 求取一个符合特定规则的hash值,并将运算次数返回
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            //挖矿的过程
            String hash = HashUtils.sha256(i + content + prioBlockHash);
            if (hash.startsWith("0000")) {
                //头部包含4个0,挖矿成功
                System.out.println("挖矿成功" + i);
                return i;
            } else {
                System.out.println("挖矿失败" + i);
            }
        }
        throw new RuntimeException("挖矿超时,未挖到...");
    }

    public void checkList(List<Block> newlist) {

        if (newlist.size() > list.size()) {
            list = newlist;
        }
    }


    public static void main(String[] args) {
//        NoteBooK noteBooK = new NoteBooK();
//        noteBooK.addGenesis("封面");
//        noteBooK.addNote("1给2转了200元");
//        noteBooK.showData();
    }
}
