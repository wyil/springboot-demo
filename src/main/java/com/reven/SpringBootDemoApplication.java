package com.reven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.Banner;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName:  SpringBootDemoApplication   
 * @author reven
 */
@SpringBootApplication
@MapperScan("com.reven.dao")
@EnableScheduling
@EnableTransactionManagement
@EnableCaching
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		// 代码中配置秘钥
//      System.setProperty("jasypt.encryptor.password", "Yghsakdgh");
        /** 方法一：关闭springboot LOGO日志打印 */
        SpringApplication springApplication = new SpringApplication(SpringBootDemoApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
        /** 方法二：默认开启springboot LOGO日志打印 */
//		SpringApplication.run(CmopWebApplication.class, args);
	}

}
