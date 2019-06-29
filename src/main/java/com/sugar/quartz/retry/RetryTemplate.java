package com.sugar.quartz.retry;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * 重试执行模板
 *
 * @author: Sugar
 * @create: 2019/6/29
 **/
@Slf4j
public abstract class RetryTemplate {

    private static final int DEFAULT_RETRY_TIME = 1;

    private int retryTime = DEFAULT_RETRY_TIME;

    private int sleepTime = 0;

    private double multiplier = 1;

    public int getRetryTime() {
        return retryTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public RetryTemplate setRetryTime(int retryTime) {
        if (retryTime <= 0) {
            throw new IllegalArgumentException("retryTime should bigger than 0！");
        }
        this.retryTime = retryTime;
        return this;
    }

    public RetryTemplate setSleepTime(int sleepTime) {
        if (sleepTime < 0) {
            throw new IllegalArgumentException("sleepTime should equal or bigger than 0!");
        }
        this.sleepTime = sleepTime;
        return this;
    }

    public RetryTemplate setMultiplier(double multiplier) {
        if (multiplier <= 0) {
            throw new IllegalArgumentException("multiplier should bigger than 0!");
        }
        this.multiplier = multiplier;
        return this;
    }

    /**
     * 重写方法的实现
     * 重试的业务执行代码
     * 失败时请抛出一个异常
     *
     * @return
     */
    protected abstract Object doBiz() throws Throwable;


    public Object execute(String location) throws InterruptedException {
        try {
            return doBiz();
        } catch (Exception e) {
            log.error("[业务执行出现异常] 准备重试。location：{}", location, e);
            Thread.sleep(sleepTime);
            return reTry(location);
        } catch (Throwable throwable) {
            log.error("[业务执行出现异常] 准备重试。location：{}", location, throwable);
            Thread.sleep(sleepTime);
            return reTry(location);
        }
    }


    /**
     * 异步执行
     *
     * @param executorService
     * @return: java.lang.Object
     * @Date: 2019/6/29
     */
    public Object submit(ExecutorService executorService, String location) throws ExecutionException, InterruptedException {
        if (executorService == null) {
            throw new IllegalArgumentException("please choose executorService!");
        }

        return executorService.submit((Callable) () -> execute(location)).get();
    }

    /**
     * 重试
     * @param
     * @return: java.lang.Object
     * @Date: 2019/6/29
     */
    private Object reTry(String location) throws InterruptedException {
        for (int i = 1; i <= retryTime; i++) {
            try {
                Object obj = doBiz();
                log.info("[业务执行第{}次重试成功] location：{}", i, location);
                return obj;
            } catch (Exception e) {
                log.error("[业务执行第{}次重试异常] location：{}", i, location, e);
                if (i < retryTime) {
                    Thread.sleep(sleepTime *= multiplier);
                }
            } catch (Throwable throwable) {
                log.error("[业务执行第{}次重试异常] location：{}", i, location, throwable);
                if (i < retryTime) {
                    Thread.sleep(sleepTime *= multiplier);
                }
            }
        }
        log.error("[业务执行全部重试{}次完毕，执行不成功！] location：{}", retryTime, location);
        return null;
    }
}
