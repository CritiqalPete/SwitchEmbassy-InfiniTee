package com.tshirt_os_server;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	public static void copyStream(InputStream inputStrm, OutputStream outputStrm) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = inputStrm.read(bytes, 0, buffer_size);
				if (count == -1){
					break;
				}
				outputStrm.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}