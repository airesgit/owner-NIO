package com.itmyself.niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/24 21:28
 * @since 0.1.0
 */
public class ChatClient {
    private final String HOST = "127.0.0.1";
    private int PORT = 9999;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("----------Client(" + userName + ") is ready------------");
    }

    public void sendMsg(String msg) throws IOException {
        if (msg.equalsIgnoreCase("bye")){
            socketChannel.close();
            socketChannel = null;
            return;
        }
        msg = userName + "说:"+msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    public void receiveMsg(){
        try {
            int count = selector.select();
            if (count > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }
                    iterator.remove();
                }
            }else {
                System.out.println("人呢？都去哪儿了？没人聊天啊...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

