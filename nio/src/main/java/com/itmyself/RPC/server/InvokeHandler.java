package com.itmyself.RPC.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/30 0:05
 * @since 0.1.0
 */
public class InvokeHandler extends ChannelInboundHandlerAdapter{

    private String getImplClassName(ClassInfo classInfo) throws ClassNotFoundException {
        String interfacePath = "com.itmyself.RPC.server";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);
        Class<?> superClass = Class.forName(interfacePath + interfaceName);
        Reflections reflections = new Reflections(interfaceName);
        Set<Class<?>> implClassSet = (Set<Class<?>>)reflections.getSubTypesOf(superClass);
        if (implClassSet.size() == 0){
            System.out.println("未找到实现类");
            return null;
        }else if (implClassSet.size()>1){
            System.out.println("找到多个实现类，未明确使用哪一个");
            return null;
        }else {
            //把集合转换为数组
            Class[] classes=implClassSet.toArray(new Class[0]);
            return classes[0].getName(); //得到实现类的名字
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        Object obj = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = obj.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        Object result = method.invoke(obj, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }

}

