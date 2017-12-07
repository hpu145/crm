package com.kaishengit.crm.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 定时发送提醒消息的任务
 * Created by zhangyu on 2017/11/15.
 */
public class SendMessageJob implements Job{

    private Logger logger = LoggerFactory.getLogger(SendMessageJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String message = (String) dataMap.get("message");
        Integer employeeId = dataMap.getIntegerFromString("employeeId");

        try {
            ApplicationContext applicationContext =
                    (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("springApplicationContext");
            JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
            jmsTemplate.send("weixinMessageQueue", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    String json = "{\"id\":\""+ employeeId +"\",\"message\":\"待办事项消息提醒："+ message +"\"}";
                    TextMessage textMessage = session.createTextMessage(json);
                    return textMessage;
                }
            });

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        logger.info("To...发送给:{},消息:{}",employeeId,message);

    }

}
