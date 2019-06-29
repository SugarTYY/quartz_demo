package com.sugar.quartz.task;

import com.sugar.quartz.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author: Sugar
 * @create: 2019/6/28
 **/
@Slf4j
public class RouteJob extends QuartzJobBean {
    @Autowired
    private RouteService routeService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            String route = this.routeService.route(jobExecutionContext);
            System.out.println(route);
        } catch (Exception e) {
        }
    }
}
