package com.reven.uitl;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author reven
 */
public class CompressGzip {

	 public static final int BUFFER = 1024;
	    public static final String EXT = ".gz";
	    public static void main(String[] args) throws Exception {
	    	String sourcePathAndName = "E:/test/test";
			String pressPathAndName = "E:/a.gz";
			CompressGzip.compress(sourcePathAndName,pressPathAndName);
		}
	 /**
     * 文件压缩
     * 
     * @throws Exception
     */
    public static void compress(String inputFileName, String outputFileName)
            throws Exception {
        FileInputStream inputFile = new FileInputStream(inputFileName);
        FileOutputStream outputFile = new FileOutputStream(outputFileName);
        compress(inputFile, outputFile);
        inputFile.close();
        outputFile.flush();
        outputFile.close();
    }
    
	/**
     * 数据压缩
     * 
     * @param is
     * @param os
     * @throws Exception
     */
    public static void compress(InputStream is, OutputStream os)
            throws Exception {

        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }
}