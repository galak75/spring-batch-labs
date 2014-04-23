package org.galak75;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;


    @Bean
    public Job job() {
        return jobs.get("job")
                .start(flow_1())
                .on("CUSTOM_FLOW_STATUS").end("CUSTOM_EXIT")
                .next(flow_2())
                .build().build();
    }

    @Bean
    public Flow flow_1() {
        return new FlowBuilder<SimpleFlow>("flow_1")
                .start(step_1_1())
                .on("CUSTOM_STEP_STATUS").end("CUSTOM_FLOW_STATUS")
                .next(step_1_2())
                .build();
    }

    @Bean
    public Step step_1_1() {
        return steps.get("step_1_1")
                .tasklet(tasklet_1_1())
                .build();
    }

    @Bean
    public Tasklet tasklet_1_1() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                contribution.setExitStatus(new ExitStatus("NOOP_CUSTOM_STEP_STATUS"));
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step step_1_2() {
        return steps.get("step_1_2")
                .tasklet(tasklet_1_2())
                .build();
    }

    @Bean
    public Tasklet tasklet_1_2() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                throw new RuntimeException("step_1_2 is failing!");
            }
        };
    }

    @Bean
    public SimpleFlow flow_2() {
        return new FlowBuilder<SimpleFlow>("flow_2")
                .start(step_2_1())
                .build();
    }

    @Bean
    public Step step_2_1() {
        return steps.get("step_2_1")
                .tasklet(tasklet_2_1())
                .build();
    }

    @Bean
    public Tasklet tasklet_2_1() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                throw new RuntimeException("step_2_1 is failing!");
            }
        };
    }

}
