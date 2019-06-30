package com.itmyself.nettychat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/29 0:25
 * @since 0.1.0
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String>{

    public static List<Channel> channels = new ArrayList<>();

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.add(incoming);
        System.out.println("[Server]:" + incoming.remoteAddress().toString().substring(1) + "在线");
    }


    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.remove(incoming);
        System.out.println("[Server]:" + incoming.remoteAddress().toString().substring(1) + "掉线");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels){
            if (channel != incoming){
                channel.writeAndFlush("["+incoming.remoteAddress().toString().substring(1) + "]说:" + s + "\n");
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("[Server]:" + incoming.remoteAddress().toString().substring(1) + "异常");
        ctx.close();
    }
}

