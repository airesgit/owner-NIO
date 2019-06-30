package com.itmyself.nettychat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/29 1:06
 * @since 0.1.0
 */
public class ChatClient {

    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast("decode", new StringDecoder());
                        pipeline.addLast("encode", new StringEncoder());
                        pipeline.addLast("handler", new ChatClientHandler());
                    }
                });
        Channel channel = b.connect(host, port).sync().channel();
        System.out.println("-----" + channel.remoteAddress().toString().substring(1) + "------------");
        System.out.println("-----" + channel.localAddress().toString().substring(1) + "------------");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            String msg = sc.nextLine();
            channel.writeAndFlush(msg + "\r\n");
        }


        group.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        new ChatClient("127.0.0.1", 9999).run();
    }
}

