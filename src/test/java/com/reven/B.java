package com.reven;

public class B extends A {
    int i = 2;

    public static void main(String[] args) {
        B b = new B();
        b.printI();  
    }
//    public void printI(){   
//        super.printI();   
//    }   
}

abstract class A {
    int i = 1;

    public void printI() {
        System.out.println("i=" + i);
    }
}