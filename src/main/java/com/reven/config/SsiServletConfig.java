/**
 * 
 */
package com.reven.config;

import org.apache.catalina.ssi.SSIServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName:  SsiServletConfig   
 * @author reven
 */
@Configuration
public class SsiServletConfig {

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        SSIServlet ssiServlet = new SSIServlet();
        ServletRegistrationBean registration = new ServletRegistrationBean(ssiServlet);
        registration.addUrlMappings("*.shtml");
        //页面放哪？
        registration.addInitParameter("inputEncoding", "UTF-8");
        registration.addInitParameter("outputEncoding", "UTF-8");
        registration.setName("ssiServlet");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }
}
