package com.itmyself.talk;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/24 21:48
 * @since 0.1.0
 */
public class TestChat {
    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        new Thread(()->{ // 开辟一个线程接收数据
            while (true){
                chatClient.receiveMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String msg = scanner.next();
            chatClient.sendMsg(msg);
        }
    }
}

