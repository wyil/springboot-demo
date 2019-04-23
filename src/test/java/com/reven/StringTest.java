package com.reven;

public class StringTest {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        sb.append("ssss");
        sb.insert(2, "gg");
        System.out.println(sb.toString());

        StringBuffer sb2 = new StringBuffer(20);
        sb2.append("gggggg");
        System.out.println(sb2.length());

        String strByBuilder = new StringBuilder().append("aa").append("bb").append("cc").append("dd").toString();

        String strByConcat = "aa" + "bb" + "cc" + "dd";

        System.out.println(strByConcat);

        String s2 = new String("AB");
        System.out.println("AB"==s2);
        String s3 = new String("AB").intern();
        System.out.println("AB"==s3);
        
        String s4 = new String("ABC");
        String s5 = new String("ABC");
        System.out.println(s4==s5);
    }
}
