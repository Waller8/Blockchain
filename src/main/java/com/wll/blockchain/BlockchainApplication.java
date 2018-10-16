package com.wll.blockchain;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

@SpringBootApplication
public class BlockchainApplication {

    public static String port;

    public static void main(String[] args) {
        //获取用户输入的端口号
        Scanner sc = new Scanner(System.in);
        port = sc.nextLine();
        //启动服务
        new SpringApplicationBuilder(BlockchainApplication.class).properties("server.port=" + port).run(args);
    }
}
