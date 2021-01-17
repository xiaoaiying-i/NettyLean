package com.xiaoai.group_chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    // 定义相关属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 构造器
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        // 连接服务器
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok......");

    }

    // 向服务器发送消息
    public void sendInfo(String info){
        info = username +" say: "+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 读取从服务器端回复的消息
    public void readInfo(){
        try {
            int readChannelCount = selector.select();
            if (readChannelCount > 0 ){ // 有可用的通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();

                    if (key.isReadable()){ // 可读的获取相关通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取数据
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.toString());
                    }
                    keyIterator.remove(); // 防止重复操作
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        // 启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        // 隔三秒读取数据
        new Thread(){
            @Override
            public void run() {
                while (true){
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
