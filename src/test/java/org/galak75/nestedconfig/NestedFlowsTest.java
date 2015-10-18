package org.galak75.nestedconfig;

import org.galak75.SpringBatchInfrastructureITestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringBatchInfrastructureITestConfiguration.class, JobConfig.class})
public class NestedFlowsTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @Test
    public void firstTest() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        System.out.println(String.format(" *** Batch Status : %s", jobExecution.getStatus()));
        System.out.println(String.format(" *** Exit Status : %s", jobExecution.getExitStatus()));

        assertEquals(JobConfig.JOB_EXIT, jobExecution.getExitStatus().getExitCode());
    }

}
