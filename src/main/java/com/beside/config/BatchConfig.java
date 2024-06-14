package com.beside.config;

import com.beside.mountain.repository.ReserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

//    private  final ReserRepository reserRepository;
//
//    @Bean(name = "updateStatusJob")
//    public Job updateStatusJob(JobRepository jobRepository, Step updateStatusStep) {
//        return new JobBuilder("updateStatusJob", jobRepository)
//                .start(updateStatusStep)
//                .build();
//    }
//
//    @Bean(name = "updateStatusStep")
//    public Step updateStatusStep(JobRepository jobRepository, Tasklet updateStatusTasklet, PlatformTransactionManager platformTransactionManager) {
//        return new StepBuilder("updateStatusStep", jobRepository)
//                .tasklet(updateStatusTasklet, platformTransactionManager)
//                .build();
//    }
//
//    @Bean
//    public Tasklet updateStatusTasklet() {
//        return (contribution, chunkContext) -> {
//            // 오늘 이전의 데이터 상태를 업데이트하는 로직을 여기에 작성
//            reserRepository.updateStatusForOlderDates(LocalDate.now(), "3"); // 예시: 상태를 "NEW_STATUS"로 업데이트
//            return RepeatStatus.FINISHED;
//        };
//    }
}
