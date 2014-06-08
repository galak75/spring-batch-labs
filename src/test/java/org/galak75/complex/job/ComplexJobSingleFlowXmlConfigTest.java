package org.galak75.complex.job;

import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ComplexJobSingleFlowXmlConfigTest extends AbstractComplexJobTest {

    @Configuration
    @EnableBatchProcessing
    @ImportResource("classpath:complex-job-single-flow-config.xml")
    public static class TestConfig {
        @Bean
        public DataSource jobRepositoryDataSource() {
            EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
            return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-hsqldb.sql")
                    .addScript("classpath:org/springframework/batch/core/schema-hsqldb.sql")
                    .setType(EmbeddedDatabaseType.HSQL)
                    .build();
        }

        @Bean
        JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }

}
