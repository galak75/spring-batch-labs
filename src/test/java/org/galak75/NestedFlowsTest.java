package org.galak75;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringBatchInfrastructureITestConfiguration.class, JobConfig.class})
@EnableBatchProcessing
public class NestedFlowsTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @Test
    public void firstTest() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(JobConfig.JOB_EXIT, jobExecution.getExitStatus().getExitCode());
    }

}
