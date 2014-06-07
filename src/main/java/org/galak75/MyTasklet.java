package org.galak75;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class MyTasklet implements Tasklet {

    private String exitCodeProperty;

    public MyTasklet(String exitCodeProperty) {
        this.exitCodeProperty = exitCodeProperty;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String exitCode = System.getProperty(exitCodeProperty);
        if (StringUtils.isNotBlank(exitCode)) {
            chunkContext.getStepContext().getStepExecution().setExitStatus(new ExitStatus(exitCode));
        }
        return null;
    }
}
