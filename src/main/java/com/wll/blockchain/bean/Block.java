package com.wll.blockchain.bean;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain.bean
 * @Author: wll
 * @CreateTime: 2018-10-09 21:20
 * @Description:
 */
public class Block {
    public int id;  //id
    public String content;  //内容
    public String hash; //哈希值
    public int nonce;   //工作量证明
    public String prioBlockHash; //前一个区块的哈希


    public Block() {
    }

    public Block(int id, String content, String hash, int nonce, String prioHash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
        this.nonce = nonce;
        this.prioBlockHash = prioHash;
    }
}
