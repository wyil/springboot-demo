package com.reven.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reven.controller.common.BaseController;

/**
 * @author reven
 */
@Controller
public class IndexController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping({ "/index", "/" })
    public String index(Model model) {
        model.addAttribute("hello", "张三" + new Date());
        String serverIp = getServerIp();
        String userIp = getCliectIp();
        model.addAttribute("UserIp", userIp);
        logger.info("ServerIp={}", serverIp);
        logger.info("UserIp={}", userIp);
        // 返回 /templates/index.html页面
        return "/index";
    }
}
