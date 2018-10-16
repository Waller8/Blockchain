package com.wll.blockchain.bean;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.wll.blockchain.utils.RSAUtils;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain.bean
 * @Author: wll
 * @CreateTime: 2018-10-10 17:19
 * @Description: 钱包类
 */
public class Wallet {
    private PublicKey publicKey;    //公钥,也就是钱包地址
    private PrivateKey privatKey;   //私钥,也就是钱包密码
    private String name;

    public Wallet(String name) {
        //保存公钥/私钥的文件
        File publicFile = new File(name + ".pub");
        File privateFile = new File(name + ".pri");
        //如果没与秘钥,那就创建
        if (!publicFile.exists() || publicFile.length() == 0 || !privateFile.exists() || privateFile.length() == 0) {
            RSAUtils.generateKeysJS("RSA", name + ".pri", name + ".pub");
        }
    }

    //转账的逻辑
    public Transaction transfer(String infom, String recvPublicKey) {
        //将公钥对象类型转为字符串类型
        String senderPublickey = Base64.encode(publicKey.getEncoded());
        //根据私钥生成签名
        String signation = RSAUtils.getSignature("SHA256withRSA", privatKey, infom);
        //生成交易对象
        Transaction transaction = new Transaction(senderPublickey, recvPublicKey, infom, signation);
        return transaction;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivatKey() {
        return privatKey;
    }

    public void setPrivatKey(PrivateKey privatKey) {
        this.privatKey = privatKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static void main(String[] args) {
        Wallet a = new Wallet("a");
        Wallet b = new Wallet("b");

    }
}
