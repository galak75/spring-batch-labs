package org.galak75.complex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ComplexFlowJavaConfigTest {

    @Configuration
    @EnableBatchProcessing
    @Import(ComplexFlowConfig.class)
    public static class TestConfig {
        @Bean
        public DataSource jobRepositoryDataSource(){
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

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @Before
    public void beforeEachTest() {
        System.setProperty("STEP1", "");
        System.setProperty("STEP2", "");
    }

    @Test
    public void testSuccessfulExecution() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
        assertThat(jobExecution.getExitStatus().getExitCode(), is(ExitStatus.COMPLETED.getExitCode()));

        assertThat(jobExecution.getStepExecutions(), contains(
                hasProperty("stepName", equalTo("step1")),
                hasProperty("stepName", equalTo("step2")),
                hasProperty("stepName", equalTo("step20")),
                hasProperty("stepName", equalTo("step21")),
                hasProperty("stepName", equalTo("step22"))
        ));
    }

    @Test
    public void testFailingAfterStep1() throws Exception {
        System.setProperty("STEP1", "CUSTOM_EXIT_STATUS");
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getStatus(), is(BatchStatus.FAILED));
        assertThat(jobExecution.getExitStatus().getExitCode(), is(ExitStatus.FAILED.getExitCode()));

        assertThat(jobExecution.getStepExecutions(), contains(
                hasProperty("stepName", equalTo("step1"))
        ));
    }

    @Test
    public void testEndingAfterStep2() throws Exception {
        System.setProperty("STEP2", "1ST_EXIT_STATUS");
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
        assertThat(jobExecution.getExitStatus().getExitCode(), is(equalTo("NOOP_EARLY_STATUS")));

        assertThat(jobExecution.getStepExecutions(), contains(
                hasProperty("stepName", equalTo("step1")),
                hasProperty("stepName", equalTo("step2"))
        ));
    }

    @Test
    public void testChangingFlowAfterStep2() throws Exception {
        System.setProperty("STEP2", "2ND_EXIT_STATUS");
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
        assertThat(jobExecution.getExitStatus().getExitCode(), is(ExitStatus.COMPLETED.getExitCode()));

        assertThat(jobExecution.getStepExecutions(), contains(
                hasProperty("stepName", equalTo("step1")),
                hasProperty("stepName", equalTo("step2")),
                hasProperty("stepName", equalTo("step10")),
                hasProperty("stepName", equalTo("step11"))
        ));
    }
}
