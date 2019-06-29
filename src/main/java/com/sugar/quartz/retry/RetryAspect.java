package com.sugar.quartz.retry;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 重试切面处理
 *
 * @author: Sugar
 * @create: 2019/6/29
 **/
@Slf4j
@Aspect
@Component
public class RetryAspect {

    //创建线程池
    ExecutorService executorService = new ThreadPoolExecutor(
            2, 4, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder("重试POOL"));

    /**
     * 增强注解方法
     * @param joinPoint
     * @param retryDot
     * @return: java.lang.Object
     * @Date: 2019/6/29
     */
    @Around("@annotation(retryDot)")
    public Object execute(ProceedingJoinPoint joinPoint, RetryDot retryDot) throws Exception{
        RetryTemplate retryTemplate = new RetryTemplate() {
            @Override
            protected Object doBiz() throws Throwable {
                return joinPoint.proceed();
            }
        };

        retryTemplate.setRetryTime(retryDot.times())
                .setSleepTime(retryDot.sleep())
                .setMultiplier(retryDot.multiplier());

        if (retryDot.asyn()) {
            return retryTemplate.submit(executorService, joinPoint.toString());
        } else {
            return retryTemplate.execute( joinPoint.toString());
        }
    }
}
