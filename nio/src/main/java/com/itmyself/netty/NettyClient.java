package com.itmyself.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/29 0:03
 * @since 0.1.0
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() { //连接的时候触发
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast("encoder", new ProtobufEncoder());
                        sc.pipeline().addLast(new NettyClientHandler());
                        System.out.println(".....Client is ready.....");
                    }
                });
        ChannelFuture cf = b.connect("127.0.0.1", 9999).sync();
        cf.channel().closeFuture().sync();

    }
}

