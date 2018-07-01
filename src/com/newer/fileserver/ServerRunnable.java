package com.newer.fileserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerRunnable implements Runnable {
	// 用于交换数据的套接字
	Socket socket;

	// 目标路径
	String filePath = System.getProperty("user.dir");

	public ServerRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {

			byte[] buffer = new byte[1024 * 4];
			int size;

			while (-1 != (size = in.read(buffer))) {
				baos.write(buffer, 0, size);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		// 接受到的信息
		byte[] info = baos.toByteArray();

		// 获取文件的散列值
		String hashValue = "";
		try {
			byte[] hash = MessageDigest.getInstance("SHA-256").digest(info);
			hashValue = new BigInteger(1, hash).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try (FileOutputStream fileout = new FileOutputStream(new File(filePath, hashValue))) {
			fileout.write(info);
			System.out.println("over");
		} catch (Exception e) {
			System.out.println("error");
		}

	}

}
