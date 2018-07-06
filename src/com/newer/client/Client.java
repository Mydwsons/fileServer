package com.newer.client;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

		// 实例化客户端套接字
		socket = new Socket(serverAddress, serverPort);

		// 获得要上传的文件
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入要上传的文件：");
		String sourceFile = sc.next();
		sc.close();

		// 定义一个文件输入流
		FileInputStream filein = new FileInputStream(sourceFile);
		// 通过套接字建立客户端向服务端的输出流和输入流
		OutputStream out = socket.getOutputStream();
		InputStream in = socket.getInputStream();

		// 定义一个字节数组
		byte[] buffer = new byte[1024 * 4];
		int size;
		// 定义一个可变长度数组，用于存放文件的字节数组
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 将文件读进字节数组,并存入长度可变数组
		while (-1 != (size = filein.read(buffer))) {
			baos.write(buffer, 0, size);
		}

		// 计算文件的散列值的并发送到服务端
		try {
			byte[] hash = MessageDigest.getInstance("SHA-256").digest(baos.toByteArray());
			out.write(hash);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		size = in.read(buffer);
		String signal = new String(buffer, 0, size);

		if (signal.equals("unexist")) {
			out.write(baos.toByteArray());
			System.out.println("上传完成");
		} else {
			System.out.println("文件已存在！");
		}

		System.out.println("over");

		in.close();
		out.close();
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
