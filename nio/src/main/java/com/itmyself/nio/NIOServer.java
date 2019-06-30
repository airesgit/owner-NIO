package com.itmyself.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/23 23:02
 * @since 0.1.0
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.bind(new InetSocketAddress(9999));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            if (selector.select(2000) == 0){
                System.out.println("Server:没有客户端连接");
                continue;
            }
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()){ // 请求事件
                    System.out.println("OP_ACCEPT");
                    SocketChannel channel = socketChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024)); // ???
                }
                if (key.isReadable()){
                    System.out.println("OP_READ");
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("客户端发来数据: " + new String(buffer.array()));
                }
                System.out.println("循环了一次");
                keyIterator.remove();
            }
        }
    }
}

