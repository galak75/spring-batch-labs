package org.galak75.mixedconfig.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepsConfig {

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Step step1() {
        return steps.get("step1")
                .tasklet(commonTasklet())
                .build();
    }

    @Bean
    public Step step2() {
        return steps.get("step2")
                .tasklet(commonTasklet())
                .build();
    }

    @Bean
    public Tasklet commonTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println(String.format("Executing %s ...", chunkContext.getStepContext().getStepName()));
                return null;
            }
        };
    }

}
