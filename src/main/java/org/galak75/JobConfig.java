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

    private static final String STEP_STATUS = "NOOP_CUSTOM_STEP_STATUS";
    private static final String FLOW_STATUS = "CUSTOM_FLOW_STATUS";
    private static final String JOB_EXIT = "CUSTOM_EXIT";


    @Bean
    public Job job() {
        return jobs.get("job")
                .start(flow_1())
                .on(FLOW_STATUS).end(JOB_EXIT)
                .next(flow_2())
                .build()
                .listener(traceListener())
                .build();
    }

    @Bean
    public TraceListener traceListener() {
        return new TraceListener();
    }

    @Bean
    public Flow flow_1() {
        return new FlowBuilder<SimpleFlow>("flow_1")
                .start(step_1_1())
                .on(STEP_STATUS).end(FLOW_STATUS)
                .next(step_1_2())
                .build();
    }

    @Bean
    public Step step_1_1() {
        return steps.get("step_1_1")
                .tasklet(tasklet_1_1())
                .listener(traceListener())
                .build();
    }

    @Bean
    public Tasklet tasklet_1_1() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                contribution.setExitStatus(new ExitStatus(STEP_STATUS));
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step step_1_2() {
        return steps.get("step_1_2")
                .tasklet(failingTasklet())
                .listener(traceListener())
                .build();
    }

    @Bean
    public Tasklet failingTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                throw new RuntimeException(String.format("step '%s' is failing!", chunkContext.getStepContext().getStepName()));
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
                .tasklet(failingTasklet())
                .listener(traceListener())
                .build();
    }

}
