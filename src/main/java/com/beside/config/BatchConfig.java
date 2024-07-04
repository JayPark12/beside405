package com.beside.config;

import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.repository.ReserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
@EnableBatchProcessing
public class BatchConfig {

    private final ReserRepository reserRepository;

    public BatchConfig(ReserRepository reserRepository) {
        this.reserRepository = reserRepository;
    }

    @Bean(name = "updateStatusJob")
    public Job updateStatusJob(JobRepository jobRepository, Step updateStatusStep) {
        return new JobBuilder("updateStatusJob", jobRepository)
                .start(updateStatusStep)
                .build();
    }

    @Bean(name = "updateStatusStep")
    public Step updateStatusStep(JobRepository jobRepository, Tasklet updateStatusTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("updateStatusStep", jobRepository)
                .tasklet(updateStatusTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet updateStatusTasklet() {
        return (contribution, chunkContext) -> {
            // 오늘 이전의 데이터 상태를 업데이트하는 로직을 여기에 작성
            List<MntiReserEntity> reserList = Optional.ofNullable(reserRepository.findReserExpired(LocalDate.now())).orElse(null);

            for(MntiReserEntity reserList2  : reserList){
                if(StringUtils.equals(reserList2.getMntiSts(), "0")){
                    reserList2.setMntiSts("3");
                    reserRepository.save(reserList2); // 예시: 상태를 "NEW_STATUS"로 업데이트
                    log.debug("reserList2 성공");
                }
            }
            return RepeatStatus.FINISHED;
        };
    }
}
