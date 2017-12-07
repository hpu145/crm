package com.kaishengit.crm.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by zhangyu on 2017/11/15.
 */
public class MyQuartzJob implements Job{
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        //从JobDetail中的参数取值
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer employeeId = dataMap.getIntegerFromString("employeeId");
        System.out.println("你好，Quartz!" +  employeeId);
    }
}
