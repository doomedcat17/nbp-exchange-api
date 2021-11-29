package com.doomedcat17.nbpexchangeapi;

import com.doomedcat17.nbpexchangeapi.task.UpdateTask;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AppStartupRunner implements ApplicationRunner {


    private final ThreadPoolTaskScheduler taskScheduler;

    private final UpdateTask updateTask;

    @Override
    public void run(ApplicationArguments args) {
        taskScheduler.schedule(updateTask, Instant.ofEpochMilli(System.currentTimeMillis()+1L));
        CronTrigger cronTrigger = new CronTrigger("* 31 12 * * *");
        taskScheduler.schedule(updateTask, cronTrigger);
    }

    public AppStartupRunner(ThreadPoolTaskScheduler taskScheduler, UpdateTask updateTask) {
        this.taskScheduler = taskScheduler;
        this.updateTask = updateTask;
    }
}
