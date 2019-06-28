package com.sugar.quartz.controll;

import com.sugar.quartz.task.RouteJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Sugar
 * @create: 2019/6/28
 **/
@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @GetMapping("/add")
    public void add(String name, Integer task) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("task", task);
        JobDetail jobDetail = JobBuilder.newJob(RouteJob.class)
                .withIdentity(name).usingJobData(dataMap).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
        this.schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
    }

    @GetMapping("/stop")
    public void stop(String name) throws SchedulerException {
        JobKey jobKey = new JobKey(name);
        this.schedulerFactoryBean.getScheduler().pauseJob(jobKey);
    }

    @GetMapping("/restart")
    public void restart(String name) throws SchedulerException {
        this.schedulerFactoryBean.getScheduler().resumeJob(new JobKey(name));
    }
}
