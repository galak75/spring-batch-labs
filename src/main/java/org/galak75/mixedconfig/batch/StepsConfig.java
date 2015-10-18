package org.galak75.mixedconfig.batch;

import org.galak75.SystemPropertyTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
    public SystemPropertyTasklet commonTasklet() {
        return new SystemPropertyTasklet("COMMON_EXIT");
    }

}
