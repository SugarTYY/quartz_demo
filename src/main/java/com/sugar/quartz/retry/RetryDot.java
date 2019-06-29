package com.sugar.quartz.retry;

import java.lang.annotation.*;

/**
 * 重试机制注解
 *
 * @author: Sugar
 * @Date: 2019/6/28
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryDot {

    /**
     * 重试次数
     * @param
     * @return: int
     * @Date: 2019/6/29
     */
    int times() default 0;

    /**
     * 重试间隔
     * @param
     * @return: int
     * @Date: 2019/6/29
     */
    int sleep() default 0;

    /**
     * 重试时间递增间隔倍数
     * multiplier大于0
     * eg：sleep=100，multiplier=1
     *  延时100ms进行下次重试
     * eg1：sleep=100，multiplier=2
     *  延时100ms第一次重试，延时200ms重试第二次，延时400ms重试第三次。。。
     * eg2：sleep=100，multiplier=0.5
     *  延时100ms第一次重试，延时50ms第二次重试，延时25ms重试第三次。。。
     * @param
     * @return: double
     * @Date: 2019/6/29 
     */
    double multiplier() default 1;

    /**
     * 是否异步
     * @param
     * @return: boolean
     * @Date: 2019/6/29
     */
    boolean asyn() default false;
}
