package com.itmyself.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/24 0:44
 * @since 0.1.0
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
        if (!channel.connect(address)){
            while(!channel.finishConnect()){
                    System.out.println("Client: 连接得同时做其它事情");
            }
        }
        String msg = "hello,Server";
        ByteBuffer writeBuf = ByteBuffer.wrap(msg.getBytes());
        channel.write(writeBuf);
        System.in.read();
    }
}

