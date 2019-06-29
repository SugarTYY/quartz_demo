package com.sugar.quartz.configuration;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 任务调度配置
 *
 * @author: Sugar
 * @create: 2019/6/28
 **/
@Configuration
public class QuartzConfiguration {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        //quartz的factorybean引用spring的bean，解决job中无法注入spring管理bean问题
        factoryBean.setJobFactory(customJobFactory());
        return factoryBean;
    }


    @Bean
    public CustomJobFactory customJobFactory() {
        return new CustomJobFactory();
    }

    public class CustomJobFactory extends SpringBeanJobFactory {

        @Autowired
        private AutowireCapableBeanFactory capableBeanFactory;

        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            //调用父类的方法
            Object jobInstance = super.createJobInstance(bundle);
            //进行注入
            capableBeanFactory.autowireBean(jobInstance);
            return jobInstance;
        }
    }
}
