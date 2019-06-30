package com.itmyself.RPC.server;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/29 23:53
 * @since 0.1.0
 */
public class HelloRPCImpl implements HelloRPC{
    @Override
    public String hello(String name) {
        return "hello," + name;
    }
}

