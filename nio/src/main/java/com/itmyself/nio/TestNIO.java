package com.itmyself.nio;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/6/23 18:27
 * @since 0.1.0
 */
public class TestNIO {

    @Test
    public void test1() throws IOException {
        FileOutputStream fos = new FileOutputStream("basic.txt");
        FileChannel fc = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello nio".getBytes());
        buffer.flip();
        fc.write(buffer);
        fos.close();
    }


    @Test
    public void test2() throws IOException {
        File file = new File("basic.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        fc.read(buffer);
        System.out.println(new String(buffer.array()));
        fis.close();
    }

    @Test
    public void test3() throws IOException {
        File file = new File("basic.txt");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("D:\\tresource\\网上找的资源\\java高级教程\\阶段7 项目框架架构与优化\\06-NIO与Netty编程\\03 NIO编程\\02 文件IO\\test.txt");
        FileChannel sourceChannel = fis.getChannel();
        FileChannel destChannel = fos.getChannel();

        long l = destChannel.transferFrom(sourceChannel, 0, file.length());
        System.out.println(l);
        fis.close();
        fos.close();
    }
}

