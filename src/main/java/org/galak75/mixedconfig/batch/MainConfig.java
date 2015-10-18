package org.galak75.mixedconfig.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(StepsConfig.class)
@ImportResource({"classpath:org/galak75/mixedconfig/batch/main-flow.xml"})
public class MainConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    @Qualifier("main-flow")
    private Flow jobFlow;

    @Bean
    public Job mixedConfigJob() {
        return jobs.get("mixed-config-job")
                .start(jobFlow)
                .build()
                .build();
    }

}
