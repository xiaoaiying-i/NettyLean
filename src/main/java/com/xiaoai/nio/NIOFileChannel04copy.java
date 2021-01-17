package com.xiaoai.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel04copy {
    
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("D:\\Users\\xiaoaiying\\IDEAProjects\\netty\\NettyPro\\1.txt");
        FileChannel fileChannel_in = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\Users\\xiaoaiying\\IDEAProjects\\netty\\NettyPro\\2.txt");
        FileChannel fileChannel_out = fileOutputStream.getChannel();


        fileChannel_in.transferTo(0, fileChannel_in.size(),fileChannel_out);
        // 或者
//        fileChannel_out.transferFrom(fileChannel_in,0,fileChannel_in.size());

        fileChannel_in.close();
        fileChannel_out.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
