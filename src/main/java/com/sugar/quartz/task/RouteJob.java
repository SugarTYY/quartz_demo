package com.sugar.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author: Sugar
 * @create: 2019/6/28
 **/
@Slf4j
public class RouteJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("执行任务了：name:{}, data:{}", jobExecutionContext.getJobDetail().getKey(), jobExecutionContext.getJobDetail().getJobDataMap().getInt("task"));
    }
}
