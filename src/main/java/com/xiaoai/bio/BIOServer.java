package com.xiaoai.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    
    public static void main(String[] args) throws Exception {
        // 线程池

        //创建一个线程池

        // 如果有客户端连狙就创建一个线程与之通信
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建serversocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了。。。");

        while (true){
            System.out.println("等待连接。。。");
            // 监听等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("----连接：------------------------------------------");
            System.out.println("连接到了一个客户端");

            //创建一个线程与之通信
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    // 可以和客户端通讯\
                    handler(socket);
                }
            });
        }
    }

    // 编写一个handler方法 和客户端通讯
    public static void handler(Socket socket){
        try {
            System.out.println("线程信息 id="+Thread.currentThread().getId());
            System.out.println("线程信息 name="+Thread.currentThread().getName());
            System.out.println("等待读取。。。");

            byte[] bytes = new byte[1024];
            // 通过socket获取一个输入流
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            while (true){
                System.out.println("----读取信息-------------------------------------");
                System.out.println("线程信息 id="+Thread.currentThread().getId());
                System.out.println("线程信息 name="+Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭cliant连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
