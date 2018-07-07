package com.newer.fileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;

public class ServerRunnable implements Runnable {

	Socket socket;

	HashMap<String, File> map = new HashMap<>();

	int i;

	public ServerRunnable(Socket socket, HashMap<String, File> map, int i) {
		this.socket = socket;
		this.map = map;
		this.i = i;
	}

	@Override
	public void run() {
		File file = null;
		try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream();) {
			byte[] hash = new byte[32];
			in.read(hash);
			String hashValue = new BigInteger(1, hash).toString(16);

			if (map.containsKey(hashValue)) {
				out.write("exist".getBytes());
				System.out.println("文件已存在");
			} else {
				FileOutputStream fileout = new FileOutputStream(file = new File(String.format("file_%03d.jpg", i)));
				map.put(hashValue, file);
				out.write("unexist".getBytes());
				byte[] buffer = new byte[1024 * 4];
				int size;
				while (-1 != (size = in.read(buffer))) {
					fileout.write(buffer, 0, size);
				}
				System.out.println("上传完成");
				fileout.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, File> getMap() {
		return map;
	}

}
