package com.xiaoai.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    
    public static void main(String[] args) throws Exception{
        String str = "hello 小艾";
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\tem2\\file01.txt");
        // 通过fileOutputStream 获取对应的 FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将str 放入byteBuffer
        byteBuffer.put(str.getBytes());

        // 对放入byteBuffer转换，进行flip
        byteBuffer.flip();

        // 将byteBuffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();



    }
}
