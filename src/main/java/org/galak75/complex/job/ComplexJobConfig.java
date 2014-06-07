package org.galak75.complex.job;

import org.galak75.SystemPropertyTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ComplexJobConfig {

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;


    // ***********************************
    // Job

    @Bean
    public Job myJob() {
        return jobs.get("myJob")
                .start(step1()).on("CUSTOM_EXIT_STATUS").fail() // FIXME : Not able to choose a custom exit code
                .from(step1()).next(step2())

                .from(step2()).on("1ST_EXIT_STATUS").end("NOOP_EARLY_END")
                .from(step2()).on("").to(step10()).next(step11())
                .from(step2()).next(step20()).next(step21()).next(step22())

                .build().build();
    }


    // ***********************************
    // Steps

    @Bean
    public Step step1() {
        return steps.get("step1")
                .tasklet(tasklet1())
                .build();
    }

    @Bean
    public Step step2() {
        return steps.get("step2")
                .tasklet(tasklet2())
                .build();
    }

    @Bean
    public Step step10() {
        return steps.get("step10")
                .tasklet(defaultTasklet())
                .build();
    }

    @Bean
    public Step step11() {
        return steps.get("step11")
                .tasklet(defaultTasklet())
                .build();
    }

    @Bean
    public Step step20() {
        return steps.get("step20")
                .tasklet(defaultTasklet())
                .build();
    }

    @Bean
    public Step step21() {
        return steps.get("step21")
                .tasklet(defaultTasklet())
                .build();
    }

    @Bean
    public Step step22() {
        return steps.get("step22")
                .tasklet(defaultTasklet())
                .build();
    }


    // ***********************************
    // Tasklets

    @Bean
    @Scope("prototype")
    public SystemPropertyTasklet defaultTasklet() {
        return new SystemPropertyTasklet("DEFAULT");
    }

    @Bean
    public SystemPropertyTasklet tasklet1() {
        return new SystemPropertyTasklet("STEP1");
    }

    @Bean
    public SystemPropertyTasklet tasklet2() {
        return new SystemPropertyTasklet("STEP2");
    }

}
