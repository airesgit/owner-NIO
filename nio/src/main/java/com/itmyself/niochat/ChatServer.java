package com.itmyself.niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/24 20:30
 * @since 0.1.0
 */
public class ChatServer {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private static final int PORT = 9999;

    public ChatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("Chat Server is ready......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start(){
        try {
            while (true){
                int count = selector.select(); // 获取注册到selector的事件
                if(count > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()){ // 监听到请求事件
                            SocketChannel socketChannel = serverSocketChannel.accept(); //获取监听到 SockerChannel
                            socketChannel.configureBlocking(false); // 设置为非阻塞
                            socketChannel.register(selector, SelectionKey.OP_READ); // 注册读事件到 selector，为了后续其它channel从selector种获取读事件
                            System.out.println(socketChannel.getRemoteAddress().toString().substring(1) + "上线了。。。");
                            // 将该channel 设置为 accpet，用于接收其它的客户端
                            key.interestOps(SelectionKey.OP_ACCEPT);
                        }
                        if (key.isReadable()){ // 监听到 read
                            readMsg(key);
                        }
                        iterator.remove(); // 防止重复处理
                    }
                }else { // 没有事件连接
                    System.out.println("独自在寒风中等待...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readMsg(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(buffer);//将数据读取到缓冲区
            if (count > 0){
                String msg = new String(buffer.array());
                printInfo(msg);
                // 将关联的channel 设置为 read，继续准备接收数据，没重新设置事件，执行完就没了，因为上面删除了当前事件
                key.interestOps(SelectionKey.OP_READ);
                // 向所有客户端发送广播
                broadCast(socketChannel, msg);
            }
        } catch (IOException e) {// 客户端关闭会抛出异常
            try {
                printInfo(socketChannel.getRemoteAddress().toString() + "下线了。。。");
                key.cancel(); // 取消注册
                socketChannel.close();//关闭通道
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }


    public void broadCast(SocketChannel except, String msg) throws IOException {
        System.out.println("发送广播。。。");
        for (SelectionKey key : selector.keys()){ // 迭代每个事件，广播消息给每个注册的事件
            SelectableChannel targetChannel = key.channel(); // 注意：这里返回的是父接口，所以监听到的事件不一定是sockerChannel，所以先验证再强转
            if (targetChannel instanceof SocketChannel && targetChannel != except){
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }


    private void printInfo(String str) { //往控制台打印消息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}

