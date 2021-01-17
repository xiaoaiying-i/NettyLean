package com.xiaoai.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {
    
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\Users\\xiaoaiying\\IDEAProjects\\netty\\NettyPro\\1.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * MappedByteBuffer，可以让文件直接在内存（堆外的内存）中进行修改，而如何同步到文件由NIO来完成
         * 参数1:Filechannel.MapMode . READ_wRITE使用的读写模式
         * 参数2:可以直接修改的起始位置
         * 参数3:是映射到内存的大小,即将文件的多少个字节映射到内存
         * 可以直接修改的范围就是0-5,不包括5
         * 实际类型DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(
                FileChannel.MapMode.READ_WRITE,
                0,
                5);

        // 修改内容
        mappedByteBuffer.put(0,(byte)'H');
        mappedByteBuffer.put(3,(byte)'9');
    }


}
