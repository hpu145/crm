package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Employee;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.example.TaskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.jobs.SendMessageJob;
import com.kaishengit.crm.mapper.TaskMapper;
import com.kaishengit.crm.service.TaskService;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import org.joda.time.format.DateTimeFormatter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyu on 2017/11/14.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    /**
     * 添加新的任务
     */
    @Override
    @Transactional
    public void addNewTask(Task task, String remindTime) {
        //createtime与done在数据库中有默认值
        taskMapper.insertSelective(task);
        //纪录日志
        logger.info("添加新的待办任务{}", task.getTitle());

        if (StringUtils.isNotEmpty(remindTime)) {
            //设定参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.putAsString("employeeId", task.getEmployeeId());
            dataMap.put("message", task.getTitle());
            //定义Job
            JobDetail jobDetail = JobBuilder
                    .newJob(SendMessageJob.class)
                    .setJobData(dataMap)
                    //设置JobKey 删除任务时使用
                    //要想插入数据后返回id值，需加useGeneratedKeys="true"
                    .withIdentity(new JobKey("taskID:" + task.getId(), "sendMessageGroup"))
                    .build();

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            DateTime dateTime = formatter.parseDateTime(remindTime);

            //拼接cron表达式
            StringBuilder cron = new StringBuilder("0")
                    .append(" ")
                    .append(dateTime.getMinuteOfHour())
                    .append(" ")
                    .append(dateTime.getHourOfDay())
                    .append(" ")
                    .append(dateTime.getDayOfMonth())
                    .append(" ")
                    .append(dateTime.getMonthOfYear())
                    .append(" ? ")
                    .append(dateTime.getYear());
            logger.info("cron表达式:{}", cron.toString());

            //定义trigger
            ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron.toString());
            Trigger cronTrigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();
            //创建调度者对象
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            try {
                scheduler.scheduleJob(jobDetail, cronTrigger);
                scheduler.start();
            } catch (Exception e) {
                throw new ServiceException("添加定时任务时异常");
            }
        }

    }

    /**
     * 查找我的任务列表
     *
     * @return List<Task>
     */
    @Override
    public List<Task> findTaskListByEmployeeId(Integer id) {
        /*List<Task> taskList = taskMapper.findTaskListByEmployeeId(id);
        return taskList;*/
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andEmployeeIdEqualTo(id);
        //根据业务需要对任务列表进行排序
        taskExample.setOrderByClause("id desc");
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 根据待办任务的id查找待办任务
     *
     * @param id
     * @return Task task
     */
    @Override
    public Task findTaskById(Integer id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id删除任务
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteTaskById(Integer id) {
        Task task = findTaskById(id);//删除定时任务时需要使用该task
        taskMapper.deleteByPrimaryKey(id);

        //删除定时任务
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String remindtime = sdf.format(task.getRemindTime());
        if (StringUtils.isNotEmpty(remindtime)) {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            try {
                scheduler.deleteJob(new JobKey("taskID:" + id, "sendMessageGroup"));
                logger.info("成功删除定时任务ID: {} groupName:{}", id, "sendMessageGroup");
            } catch (SchedulerException e) {
                throw new ServiceException("删除定时任务时异常");
            }
        }

    }

    /**
     * 修改任务的状态
     *
     * @param task
     */
    @Override
    public void updateStateUndoneToDone(Task task) {
        if (task.getDone() == 0) {
            task.setDone((byte) 1);
        }
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 根据id修改已选择任务状态
     * 将已经完成状态修改为未完成状态
     */
    @Override
    public void updateStateDoneToUndone(Task task) {
        if (task.getDone() == 1) {
            task.setDone((byte) 0);
        }
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 根据task的结束时间与状态查找逾期事项
     *
     * @return
     */
    @Override
    public List<Task> findOverdueTask(List<Task> taskList) {

        List<Task> taskOverdueList = new ArrayList<>();
        for (Task task : taskList) {
            //根据task是否逾期与状态查找逾期事项
            if (task.isOverTime() && task.getDone() == 0) {
                taskOverdueList.add(task);
            }
        }
        //System.out.println("列表：" + taskOverdueList.size());
        return taskOverdueList;
    }


}
