package com.reven;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.web.util.HtmlUtils;

import com.reven.uitl.JsoupUtil;

public class TestXssRequestWrapper {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String text1 = "%2033%3Cscript%3Ealert(12)%3C%2Fscript%3E%20";
        String text2 = " 33<script>alert(12)</script>";
        System.out.println(URLDecoder.decode(text1, "UTF-8"));
        System.out.println(JsoupUtil.clean(URLDecoder.decode(text1, "UTF-8")));
        System.out.println(HtmlUtils.htmlEscape(URLDecoder.decode(text1, "UTF-8")));
        System.out.println("-------text2-------------------");
        System.out.println(URLDecoder.decode(text2, "UTF-8"));
        System.out.println(JsoupUtil.clean(URLDecoder.decode(text2, "UTF-8")));
        System.out.println(HtmlUtils.htmlEscape(URLDecoder.decode(text2, "UTF-8")));
    }
}
