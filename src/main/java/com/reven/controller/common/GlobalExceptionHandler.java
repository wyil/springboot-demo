package com.reven.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.reven.core.ServiceException;

/**
 * @ClassName:  GlobalExceptionHandler   
 * @author reven
 * @date   2018年8月30日
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public static final String ERROR_VIEW = "common/error";

    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        String msg;
        if (e instanceof ServiceException) {
            msg = e.getMessage();
        } else {
            //不是最佳实践
            msg = "操作异常!"+ e.getMessage();
        }
        logger.error(msg, e);
        // 返回ModelAndView对象就是返回页面，返回一个其他对象就会转换为json串
        if (isAjax(request)) {
            return ResResult.fail(msg);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("exception", e);
            mav.addObject("url", request.getRequestURL());
            mav.setViewName(ERROR_VIEW);
            return mav;
        }
    }

    /**   
     * * 判断是否是ajax请求
     * @param httpRequest
     * @return      
     */
    public static boolean isAjax(HttpServletRequest httpRequest) {
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        return (xRequestedWith != null && "XMLHttpRequest".equals(xRequestedWith));
    }
}