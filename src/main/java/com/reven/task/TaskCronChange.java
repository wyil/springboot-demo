//package com.reven.task;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//@Component 
//@EnableScheduling
//public class TaskCronChange implements  SchedulingConfigurer{
//
//    public static String cron; 
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        //项目部署时，会在这里执行一次，从数据库拿到cron表达式
//        cron = timerQueryMapper.getCronTime();
//
//       Runnable task = new Runnable() {
//           @Override
//           public void run() {
//              //任务逻辑代码部分.
//              System.out.println("I am going:" + LocalDateTime.now());
//           }
//       };
//       Trigger trigger = new Trigger() {
//           @Override
//           public Date nextExecutionTime(TriggerContext triggerContext) {
//              //任务触发，可修改任务的执行周期.
//              //每一次任务触发，都会执行这里的方法一次，重新获取下一次的执行时间        
//              cron = timerQueryMapper.getCronTime();
//              CronTrigger trigger = new CronTrigger(cron);
//              Date nextExec = trigger.nextExecutionTime(triggerContext);
//              return nextExec;
//           }
//       };
//       taskRegistrar.addTriggerTask(task, trigger);
//    }
//
//}