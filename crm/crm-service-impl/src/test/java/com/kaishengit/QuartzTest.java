package com.kaishengit;

import com.kaishengit.crm.jobs.MyQuartzJob;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

/**
 * Created by zhangyu on 2017/11/15.
 */
public class QuartzTest {


    @Test
    public void simpleTrigger() throws SchedulerException, IOException {
        //定义Job
        JobDetail detail = JobBuilder.newJob(MyQuartzJob.class).build();
        //定义trigger
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        scheduleBuilder.withIntervalInSeconds(5); //间隔5s执行
        scheduleBuilder.repeatForever(); //永远执行下去

        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();
        //创建调度者对象
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.scheduleJob(detail,trigger);
        scheduler.start();

        System.in.read();
    }

    @Test
    public void cronTrigger() throws SchedulerException, IOException {
        //在定义job之前可以设定JobDetail中的参数
        JobDataMap dataMap = new JobDataMap();
        //如果通过putAsString()方法存值，需要用getIntegerFromString()取值
        //如果通过put()方法存值，需要用getInt()取值
        dataMap.putAsString("employeeId",1);

        //定义Job
        JobDetail detail = JobBuilder.newJob(MyQuartzJob.class)
                .setJobData(dataMap)
                .build();
        //定义trigger
        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ? *");
        Trigger cronTrigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();
        //创建调度者对象
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.scheduleJob(detail,cronTrigger);
        scheduler.start();

        System.in.read();
    }






}
