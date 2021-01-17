package com.xiaoai.group_chat;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    // 构造器
    // 初始化工作
    public GroupChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listenChannel.configureBlocking(false);
            // 将该listenChannel注册到selector
            listenChannel.register(selector,SelectionKey.OP_ACCEPT);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 监听
    public void listen(){
        try {
            while (true){
                int count = selector.select();
                if (count > 0){ // 有事件需要处理
                    //遍历selectionkey集合，处理事件
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()){
                        // 取出单个事件
                        SelectionKey key = keyIterator.next();

                        // 监听accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            // 将sc注册到seletor
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            // 提示
                            System.out.println("【"+sc.getRemoteAddress() + "】上线...");
                        }
                        if (key.isReadable()){ // 通道发送read事件 即通道为可读状态
                            // 读取事件业务
                            readData(key);
                        }
                        // 把当前的key删除 防止重复处理
                        keyIterator.remove();
                    }
                }else {
                    System.out.println("等待......");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key){
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            // 获取channel
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            // 根据count值判断是否有数据
            if (count > 0){
                // 把缓存区数据转字符串
                String msg = new String(buffer.array());
                System.out.println("form 客户端："+msg);

                // 向其他客户端转发消息
                sendInfoToOtherClients(msg,channel);
            }
        }catch (Exception e){
//            e.printStackTrace();
            try {
                System.out.println("【"+channel.getRemoteAddress()+"】离线了.....");
                // 取消注册
                key.channel();
                // 关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // 转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg,SocketChannel selfChannel) throws IOException {
        System.out.println("服务器转发消息中......");
        // 遍历所有注册到selector上的SocketChannel并排除自己
        for (SelectionKey key : selector.keys()) {
            // 通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != selfChannel){
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args){
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();

    }


}
