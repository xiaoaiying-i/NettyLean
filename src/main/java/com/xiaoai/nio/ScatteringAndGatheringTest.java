package com.xiaoai.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {
        // 使用
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 假定从客户端接收8字节
        int messageLenght = 8;
        // 循环读取
        while (true){
            int byteRead = 0;
            while (byteRead < messageLenght){
                long l = socketChannel.read(byteBuffers);
                byteRead += l; // 累计读取的字节数
                System.out.println("byteRead=" + byteRead);
                // 使用流打印，看看当前这个buffer的position和limit
                Arrays.asList(byteBuffers)
                        .stream()
                        .map(buffer -> "postion="+buffer.position()+",limit="+buffer.limit())
                        .forEach(System.out::println);

                // 将所有buffer进行flip
                Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

                // 将数据读出显示到客户端
                long byteWrite = 0;
                while (byteWrite < messageLenght){
                    long wlen = socketChannel.write(byteBuffers);
                    byteWrite += wlen;
                }

                // 将所有buffer进行flip
                Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
                System.out.println("byteRead="+byteRead +"byteWrie="+byteWrite + ",messagelenght="+messageLenght);
            }
        }
    }
}
