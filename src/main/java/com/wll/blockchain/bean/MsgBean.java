package com.wll.blockchain.bean;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain.bean
 * @Author: wll
 * @CreateTime: 2018-10-11 17:57
 * @Description:
 */
public class MsgBean {

    // 标识消息的作用
    // 1 传递区块链数据
    // 2 节点数据
    // 3 区块
    // 4 交易数据
    public int code;
    public String msg;

    public MsgBean() {
    }

    public MsgBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
