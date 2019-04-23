package com.reven;

import java.lang.reflect.Field;

public class FinalTest {
    public final Integer info = 123;
    public final int a = 99;

    public static void main(String[] args)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        FinalTest util = new FinalTest();
        Field field = util.getClass().getDeclaredField("info");
        field.setAccessible(true);
        field.set(util, 789);
        System.out.println(field.get(util));
        System.out.println(util.info);
        
        Field fielda = util.getClass().getDeclaredField("a");
        fielda.setAccessible(true);
        fielda.set(util, 567);
        System.out.println(fielda.get(util));
        System.out.println(util.a);
    }
}