package com.itmyself.RPC.server;

public class HelloNettyImpl implements HelloNetty{

    @Override
    public String hello() {
        return "hello,netty";
    }
}