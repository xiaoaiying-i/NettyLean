package com.xiaoai.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    
    public static void main(String[] args) throws Exception{

        // 创建一个输入流
        File file = new File("d:\\tem2\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过fileOutputStream 获取对应的 FileChannel
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建一个缓冲区buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将fileChannel 数据 放入byteBuffer
        fileChannel.read(byteBuffer);

        // 将byteBuffer字节数据转换输出
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();


    }
}
