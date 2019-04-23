package com.reven.task;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.reven.core.NamedThreadFactory;

/**
 * @author reven
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        // 给sched用的，默认只有1个，时间太长会阻塞
        // 多个@Scheduled可以并发执行了，最高并发度是3，但是同一个@Schedule不会并发执行
        // 数量跟Scheduled要多一些
        
        //创建线程以及线程池时候要指定与业务相关的名字 https://www.jianshu.com/p/d6245f2c3a9d
        ThreadFactory threadFactory=new NamedThreadFactory("taskScheduling");
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3,threadFactory);
        return executor;
    }
}