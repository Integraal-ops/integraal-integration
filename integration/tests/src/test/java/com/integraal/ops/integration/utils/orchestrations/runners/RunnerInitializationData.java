package com.integraal.ops.integration.utils.orchestrations.runners;

import com.integraal.ops.integration.utils.orchestrations.beans.TestRunConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RunnerInitializationData implements TestStepRunner {
    @Override
    public void run(TestRunConfiguration runConfiguration) throws Exception {
        log.info("Running RunnerInitializationData - Not Implemented yet");
    }
}
