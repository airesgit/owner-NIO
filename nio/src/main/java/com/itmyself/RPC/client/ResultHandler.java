package com.itmyself.RPC.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/30 1:08
 * @since 0.1.0
 */
public class ResultHandler extends ChannelInboundHandlerAdapter{

    private Object response;

    public Object getResponse(){
        return response;
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        ctx.close();
    }
}

