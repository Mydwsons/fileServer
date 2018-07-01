package com.newer.fileserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 * 
 * @author Mydwsons
 *
 */
public class FileServer {
	// 声明:服务端套接字
	ServerSocket serversocket;

	// 声明:服务端端口
	int port = 10001;

	// 建立一个线程池
	ExecutorService pool;

	public void start() throws IOException {
		// 实例化服务端套接字
		serversocket = new ServerSocket(port);
		System.out.println("服务器启动");

		// 实例化线程池
		pool = Executors.newFixedThreadPool(5);
		System.out.println("loading...");
		// 侦听并返回连接至此的客户端套接字
		Socket socket = serversocket.accept();
		System.out.println("建立一个连接");

		pool.submit(new ServerRunnable(socket));

	}

	public static void main(String[] args) {
		FileServer fileServer = new FileServer();
		try {
			fileServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
