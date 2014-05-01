package org.galak75.split;

import org.galak75.SpringBatchInfrastructureITestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = {SpringBatchInfrastructureITestConfiguration.class, ParallelFlowsJobConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ParallelFlowsJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void test() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        assertThat(jobExecution.getStepExecutions(), containsInAnyOrder(
                hasProperty("stepName", equalTo(ParallelFlowsJobConfig.STEP_1)),
                hasProperty("stepName", equalTo(ParallelFlowsJobConfig.STEP_2)),
                hasProperty("stepName", equalTo(ParallelFlowsJobConfig.STEP_3))
        ));
    }
}