package com.itmyself.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/28 23:56
 * @since 0.1.0
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter{



    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client:" + ctx);
        BookMessage.Book message = BookMessage.Book.newBuilder().setId(1).setName("Java 从入门到精通").build();
//        ctx.writeAndFlush(Unpooled.copiedBuffer("老板，还钱吧", CharsetUtil.UTF_8));
        ctx.writeAndFlush(message);
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务端发来的消息:" + buf.toString(CharsetUtil.UTF_8));
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

