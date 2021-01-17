package com.xiaoai.nio_selector;

import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOserver {
    public static void main(String[] args) throws Exception {
        // 创建serversoketChaneel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(6666)); // 绑定端口
        serverSocketChannel.configureBlocking(false);// 设置非阻塞

        // 得到一个selector对象
        Selector selector = Selector.open();
        // 把serverSocketChannel注册到selector   关系事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true){
            // 等待1秒，如果没有事件发生
            if (selector.select(3000) == 0){  // selector开始监听
                System.out.println("服务器等待了3秒，无连接");
                continue;
            }

            // 如果返回>0 获取到selectionKey集合
            // selector.selectedKeys() 返回关注事件集合
            // 反向获取通道
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()){// 遍历有操作的selectionkey
                SelectionKey key = keyIterator.next();// 获取到selectionkey

                if (key.isAcceptable()){ // 如果事件是OP_ACCEPT  有新客户端连接
                    // 给该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);// 将socketChannel设置为非阻塞
                    System.out.println("客户端连接成功 生成一个socketChannel:"+socketChannel.hashCode());

                    // 将当前socketChannel 注册到selector  关注事件为OP_READ  通过socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()){  // 如果事件是OP_READ
                    // 通过key 反向获取channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.configureBlocking(false);
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("form client:"+ new String(buffer.array()));

                }
                // 手动从集合中移动当前的selectionKey 防止重复操作
                keyIterator.remove();
            }
//        System.out.println("无连接，关闭！！！");

        }

    }
}
