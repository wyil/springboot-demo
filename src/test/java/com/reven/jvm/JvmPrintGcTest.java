package com.reven.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author reven
 */
public class JvmPrintGcTest {
	public static void main(String[] args) {
	    printMemory();
//		JvmPrintGcTest.test();
		JvmPrintGcTest.oom();
		printMemory();
		
	}

	static void printMemory() {
	    System.out.println("最大堆：" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
        System.out.println("空闲堆：" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
        System.out.println("总的堆：" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
	}
	static void test() {
		// jvm添加参数 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps
		// -Xloggc:d:/gc.log
		// -Xmx20m -Xms5m -Xmn2m -XX:+PrintGCDetails
	    
	    //case2 : 加参数 -XX:+UseSerialGC
		@SuppressWarnings("unused")
        byte[] bytes ;
		for (int i = 0; i < 200; i++) {
			bytes = new byte[1 * 1024 * 1024];
		}
	}
	
	static void oom() {
        // jvm添加参数 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps
        // -Xmx20m -Xms5m -Xmn2m
        
        //case2 : 加参数 -XX:+UseSerialGC
        List<byte[]> list=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            byte[]  bytes = new byte[1 * 1024 * 1024];
            list.add(bytes);
        }
        System.out.println("总的堆：" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
        list.size();
    }

}
