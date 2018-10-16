package com.wll.blockchain.bean;

import com.wll.blockchain.utils.RSAUtils;

import java.security.PublicKey;

/**
 * @BelongsProject: blockchain
 * @BelongsPackage: com.wll.blockchain.bean
 * @Author: wll
 * @CreateTime: 2018-10-10 19:32
 * @Description: 交易实体类
 */
public class Transaction {
    //付款方公钥
    private String senderAddress;
    //收款方公钥
    private String receiverAddress;
    //转账信息(原文)
    private String infom;
    //签名
    private String signation;

    public Transaction() {
    }

    public Transaction(String senderAddress, String receiverAddress, String infom, String signation) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.infom = infom;
        this.signation = signation;
    }

    //校验交易是否合法
    public boolean verify() {
        PublicKey publicKey = RSAUtils.getPublicKeyFromString("RSA", senderAddress);
        return RSAUtils.verifyDataJS("SHA256withRSA", publicKey, infom, signation);
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getInfom() {
        return infom;
    }

    public void setInfom(String infom) {
        this.infom = infom;
    }

    public String getSignation() {
        return signation;
    }

    public void setSignation(String signation) {
        this.signation = signation;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderAddress='" + senderAddress + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", infom='" + infom + '\'' +
                ", signation='" + signation + '\'' +
                '}';
    }
}
