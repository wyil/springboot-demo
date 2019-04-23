package com.reven.cache;
 
public class Test {
 
	public static void main(String[] args) throws InterruptedException {
		LocalCache lcCache = new LocalCache();
		lcCache.putValue("1001", "zs", 5);
		lcCache.putValue("1002", "ls", 30);
		lcCache.putValue("1003", "ww", 30);
		lcCache.putValue("1004", "zq", 30);
		
		String strValue = (String)lcCache.getValue("1002");
		System.out.println(strValue);
		Thread.sleep(6000);
		Object strValue2=lcCache.getValue("1001");
		System.out.println(strValue2);
		
		LocalCache lcCache2 = new LocalCache();
		System.out.println(lcCache2.getValue("1003"));
		Thread.sleep(6000);
		System.out.println(lcCache2.getValue("1003"));
	}
 
}