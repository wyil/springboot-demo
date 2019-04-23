package com.reven.jvm;

public class VolatileTest extends Thread {
    public volatile static int count;

    private static void addCount() {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println(currentThread().getName() + "，count=" + count);
    }

    @Override
    public void run() {
        addCount();
    }

    public static void main(String[] args) {
        VolatileTest[] mythreadArray = new VolatileTest[100];
        for (int i = 0; i < 100; i++) {
            mythreadArray[i] = new VolatileTest();
        }

        for (int i = 0; i < 100; i++) {
            mythreadArray[i].setName("thread_" + i);
            mythreadArray[i].start();
        }
    }
}
