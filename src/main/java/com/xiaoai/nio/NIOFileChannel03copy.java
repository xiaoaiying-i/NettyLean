package com.xiaoai.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03copy {
    
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("D:\\Users\\xiaoaiying\\IDEAProjects\\netty\\NettyPro\\1.txt");
        FileChannel fileChannel_in = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\Users\\xiaoaiying\\IDEAProjects\\netty\\NettyPro\\2.txt");
        FileChannel fileChannel_out = fileOutputStream.getChannel();

        //buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){

            // 重要步骤，把buffer标志位恢复到初始状态，否则循环读写时如果一次没读完可能会发生错误
            byteBuffer.clear();

            // 循环将读取到的数据放入buffer,
            int read = fileChannel_in.read(byteBuffer);
            if (read == -1){
                break;
            }

            // 将buffer内数据写出
            // 记得先反转buffer
            byteBuffer.flip();

            fileChannel_out.write(byteBuffer);

        }
    }
}
