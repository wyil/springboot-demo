package com.reven.uitl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipPcompress {
	public static void main(String args[]) {
		GzipPcompress gzip = new GzipPcompress();
		String sourcePathAndName = "E:/test/test.txt";
		String pressPathAndName = "E:/a.gz";
		gzip.gzPressFile(sourcePathAndName, pressPathAndName);
	}

	public void gzPressFile(String sourcePathAndName, String pressPathAndName) {
		try {
			FileInputStream fileInputStream = new FileInputStream(sourcePathAndName);
			GZIPInputStream zin = new GZIPInputStream(fileInputStream);
			GzipPcompress.extractFile(zin, pressPathAndName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void extractFile(GZIPInputStream in, String name) throws IOException {
		byte[] buffer = new byte[1000];
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(name)));
		int count = -1;
		while ((count = in.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}
		out.close();
	}
}