package com.reven.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author reven
 */
@Component
public class DemoJob {
    /**
     * 
     * 把系统时间改到从前，将无法触发。把系统时间还原也不行，需要重启。
     * @throws InterruptedException
     */

//    @Scheduled(cron = "0/2 * * 10-31 * *")
//    @Scheduled(cron = "${job.cron-expression.demo}")
//    @Async
    public void cronDemo() throws InterruptedException {
        // 获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out
                .println("cronDemo，start 当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Thread.sleep(1000*60*5);
        System.out
        .println("cronDemo，end 当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }

//    @Scheduled(cron = "0/5 * * * * *")
//  @Scheduled(cron = "${noset-job.cron-expression.demo:#{'0/5 * * * * *'}}")
  @Async
    public void cronDemo2() throws InterruptedException {
        // 获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(Thread.currentThread().getName());
        System.out
                .println("cronDemo2，当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

//    @Scheduled(cron = "0/2 * * * * *")
//  @Scheduled(cron = "${job.cron-expression.demo}")
    @Async
    public void cronDemo3() throws InterruptedException {
        // 获取当前时间
        Thread.sleep(10000);
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(Thread.currentThread().getName());
        System.out
                .println("cronDemo3，当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

//    @Scheduled(initialDelay = 60000, fixedDelayString = "${job.cron-expression.fixedDelay}")
    public void fixedDelayDemo() {
        // 获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(
                "fixedDelayDemo，当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
