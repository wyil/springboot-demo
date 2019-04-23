package com.reven.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.reven.controller.common.BaseController;
import com.reven.controller.common.ResResult;

/**
 * @ClassName: xss 测试
 * @author reven
 */
@RestController
@RequestMapping("/xss")
public class TestXssController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TestXssController.class);

    @RequestMapping("/test1")
    public ResResult test1(String p1, String p2) {
        logger.info("p1=" + p1);
        logger.info("p2=" + p2);
        return ResResult.success();
    }
    
    @GetMapping("/excludeUrl")
    public ResResult excludeUrl(String p1, String p2) {
        logger.info("p1=" + p1);
        logger.info("p1=" + HtmlUtils.htmlEscape(p1));
        logger.info("p2=" + p2);
        logger.info("p1=" + HtmlUtils.htmlEscape(p2));
        return ResResult.success();
    }

    
    @GetMapping("/testExcludePar")
    public ResResult testExcludePar(String content, String p2WithHtml) {
        logger.info("content=" + content);
        logger.info("p2WithHtml=" + p2WithHtml);
        return ResResult.success();
    }

    @PostMapping("/testBody")
    public ResResult testBody(String body) {
        logger.info("body=" + body);
        return ResResult.success();
    }
}
