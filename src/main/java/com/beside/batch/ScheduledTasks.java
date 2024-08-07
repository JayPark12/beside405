package com.beside.batch;

import com.beside.config.BatchConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTasks {

    private final JobLauncher jobLauncher;
    private final Job updateStatusJob;

    @Autowired
    public ScheduledTasks(JobLauncher jobLauncher, Job updateStatusJob) {
        this.jobLauncher = jobLauncher;
        this.updateStatusJob = updateStatusJob;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void runUpdateStatusJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();
        jobLauncher.run(updateStatusJob, jobParameters);
    }
}
