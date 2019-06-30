package com.itmyself.RPC.client;

import com.itmyself.RPC.server.HelloNetty;
import com.itmyself.RPC.server.HelloRPC;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/30 1:29
 * @since 0.1.0
 */
public class TestNettyRPC {

    public static void main(String[] args) {
        HelloNetty helloNetty = (HelloNetty)NettyRPCProxy.create(HelloNetty.class);
        System.out.println(helloNetty.hello());
        HelloRPC helloRPC = (HelloRPC)NettyRPCProxy.create(HelloRPC.class);
        System.out.println(helloRPC.hello("RPC"));
    }
}

