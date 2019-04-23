package com.reven;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMatcher {
    public static void main(String[] args) {
        String url = "/design/qu-radio/ajaxSave.do";
        Pattern p = Pattern.compile("^" + "/design/.*/ajaxSave.do");
        Matcher m = p.matcher(url);
        System.out.println(m.find());
    }
}
