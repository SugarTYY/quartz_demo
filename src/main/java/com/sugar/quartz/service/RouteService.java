package com.sugar.quartz.service;

import com.sugar.quartz.retry.RetryDot;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

/**
 * @author: Sugar
 * @create: 2019/6/29
 **/
@Slf4j
@Service
public class RouteService {

    @RetryDot(times = 10, sleep = 100, asyn = true)
    public String route(JobExecutionContext jobExecutionContext) throws Exception {
        double v = Math.random() * 10;
        System.out.println(v);
        if (v < 9) {
            throw new Exception("小于9，重试。");
        }
        log.info("执行任务了：name:{}, data:{}", jobExecutionContext.getJobDetail().getKey(),
                jobExecutionContext.getJobDetail().getJobDataMap().getInt("task"));
        return "执行完毕";
    }
}
