/**
 * 
 */
package com.heiya.mobileapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author Dian Krisnanjaya
 *
 */
@Configuration
@EnableScheduling
public class ScheduledConfiguration implements SchedulingConfigurer {
	
	/**
     * The pool size.
     */
    private final int POOL_SIZE = 1;

    /**
     * Configures the scheduler to allow multiple pools.
     * @param taskRegistrar The task registrar.
     */
	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);// Set the pool of threads
        threadPoolTaskScheduler.setThreadNamePrefix("pay-thread");
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
