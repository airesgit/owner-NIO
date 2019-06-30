package com.itmyself.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author aires.huang
 * @Description:
 * @date 2019/4/8 12:34
 * @since 0.1.0
 */
public class ChannelDemo {

    public static void main(String[] args) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile("D:\\nio-data.txt","rw");
        FileChannel channel = accessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        int read = 0;
        //读到缓存
        read = channel.read(byteBuffer);
        while (read != -1){
//            read = channel.read(byteBuffer);
            System.err.println("read " + read);
            //切换buffer 为写模式
            byteBuffer.flip();
//            byteBuffer.put()
            //从缓存中读取到channel
            while (byteBuffer.hasRemaining()){
                System.out.println((char)byteBuffer.get());
            }
            byteBuffer.clear(); //读取完毕之后情况缓存
            read = channel.read(byteBuffer);  //为了跳出循环
        }
        accessFile.close();  //关闭channel

    }
}

