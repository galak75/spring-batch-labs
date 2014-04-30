package org.galak75.split;

import org.galak75.TraceListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class ParallelFlowsJobConfig {

    public static final String STEP_1 = "Step 1";
    public static final String STEP_2 = "Step 2";
    public static final String STEP_3 = "Step 3";

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Job job() {
        return jobs.get("Job with parallel flows")
                .start(mainFlow())
                .build()
                .listener(traceListener())
                .build();
    }

    @Bean
    public SimpleFlow mainFlow() {
        return new FlowBuilder<SimpleFlow>("Main Flow")
                .start(flow1())
                .split(new SimpleAsyncTaskExecutor())
                .add(flow2(), flow3())
                .build();
    }

    @Bean
    public SimpleFlow flow1() {
        return new FlowBuilder<SimpleFlow>("flow 1")
                .start(step1())
                .build();
    }

    @Bean
    public SimpleFlow flow2() {
        return new FlowBuilder<SimpleFlow>("flow 2")
                .start(step2())
                .build();
    }

    @Bean
    public SimpleFlow flow3() {
        return new FlowBuilder<SimpleFlow>("flow 3")
                .start(step3())
                .build();
    }

    @Bean
    public Step step1() {
        return steps.get(STEP_1)
                .tasklet(waitingTasklet())
                .listener(traceListener())
                .build();
    }

    @Bean
    public Step step2() {
        return steps.get(STEP_2)
                .tasklet(waitingTasklet())
                .listener(traceListener())
                .build();
    }

    @Bean
    public Step step3() {
        return steps.get(STEP_3)
                .tasklet(waitingTasklet())
                .listener(traceListener())
                .build();
    }

    @Bean
    public Tasklet waitingTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println(String.format("  * Starting step '%s'", chunkContext.getStepContext().getStepName()));
                Thread.sleep(1000);
                System.out.println(String.format("  * Stopping step '%s'", chunkContext.getStepContext().getStepName()));
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public TraceListener traceListener() {
        return new TraceListener();
    }

}
