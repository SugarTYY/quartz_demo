package com.sugar.quartz.task;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: Sugar
 * @create: 2019/6/28
 **/
@Component
public class Task {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void postConstruct() throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("task", 0);
        JobDetail jobDetail = JobBuilder.newJob(RouteJob.class)
                .withIdentity("routeJob").usingJobData(dataMap).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
        this.schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
    }
}
