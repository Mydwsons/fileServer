package com.newer.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 客户端
 * 
 * @author Mydwsons
 *
 */
public class Client {

	// 声明一个客户端套接字
	Socket socket;

	String serverAddress = "";

	int serverPort = 10001;

	public void start() throws UnknownHostException, IOException {

		// 获得要上传的文件
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入要上传的文件：");
		String sourceFile = sc.next();
		sc.close();

		//
		socket = new Socket(serverAddress, serverPort);

		FileInputStream filein = new FileInputStream(sourceFile);
		OutputStream out = socket.getOutputStream();

		byte[] buffer = new byte[1024 * 4];
		int size;
		while (-1 != (size = filein.read(buffer))) {
			out.write(buffer, 0, size);
			out.flush();
		}

		System.out.println("over");

		filein.close();
		socket.close();

	}

	public static void main(String[] args) {
		Client client = new Client();
		try {
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
