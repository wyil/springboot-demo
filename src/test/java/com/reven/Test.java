package com.reven;
class Base{
    static{     System.out.println("base static");    }
    public Base(){
        System.out.println("base constructor");
    }
}
public class Test extends Base{
    static{      System.out.println("test static");    }
    public Test(){
        System.out.println("test constructor");
    }
    public static void main(String[] args) {
        new Test();
    }
}
