package com.itmyself.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/28 23:22
 * @since 0.1.0
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    /**
     * 通道就绪事件
     * @param ctx
     * @throws Exception
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server：监听到连接");
        ctx.fireChannelActive();
    }


    /**
     * 通道读取数据事件
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server:" + ctx);
//        ByteBuf buf = (ByteBuf) msg;
        BookMessage.Book message = (BookMessage.Book) msg;
        System.out.println("客户端发来的消息 ：" + message.getName());
    }

    /**
     * 读取完毕事件
     * @param ctx
     * @throws Exception
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("就是没钱".getBytes()));
    }

    /**
     * 异常发生事件
     * @param ctx
     * @param cause
     * @throws Exception
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

